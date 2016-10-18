package com.co.showcase.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.api.errorControl.RetrofitException;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.EntryResponse;
import com.co.showcase.model.ErrorControl;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.categoria.categoria;
import com.co.showcase.ui.splash.Splash;
import com.facebook.login.LoginManager;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;
import com.pavlospt.rxfile.RxFile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import fr.tvbarthel.intentshare.IntentShare;
import io.realm.Realm;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by miguelalegria on 15/5/16 for DemoMike.
 */
public class BaseActivity extends RxAppCompatActivity {
  protected static final String KEY_POSITION = "KEYPOSTION";
  private static final int SHARED_IMAGE_QUALITY = 100;
  private static final String SHARED_DIRECTORY = "sharing";
  private static final String SHARED_IMAGE_FILE = "shared_img.png";
  private static final String FILE_PROVIDER_AUTHORITY = "com.co.showcase.SharingFileProvider";
  @NonNull public Gson gson = new Gson();
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  private static final String NAME_PATTERN = "^[\\\\p{L} .'-]+$";
  private Pattern patternName = Pattern.compile(NAME_PATTERN);
  private boolean isOnpause;
  private MaterialDialog loading;
  private Realm realm;
  @Nullable private MaterialDialog materialDialog;

  private void showMessageOnSnakeBar(@NonNull View view, @NonNull String msg) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
  }

  @Override protected void onPause() {
    super.onPause();
    isOnpause = true;
    log("onPause");
  }

  @Override protected void onResume() {
    super.onResume();
    log("onResume");
    initDB();
    isOnpause = false;
    updateGcm(getUserSync());
  }

  @Override protected void onStop() {
    EventBus.getDefault().unregister(this);
    log("onStop");
    super.onStop();
  }

  @Override protected void onStart() {
    super.onStart();
    log("onStart");
    EventBus.getDefault().register(this);
  }

  private void initDB() {
    AppMain appmain = (AppMain) getApplication();
    appmain.initRxDb();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    log("onCreate");
    realm = Realm.getDefaultInstance();
    initDB();
    new ReactiveNetwork().observeConnectivity(this)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(connectivityStatus -> {
          switch (connectivityStatus) {
            case WIFI_CONNECTED_HAS_NO_INTERNET:
            case OFFLINE:
            case UNKNOWN:

              //showMessageOnSnakeBar(findViewById(R.id.container_root), getString(R.string.offile));
              break;
          }
        });
    //
    //
  }

  private void updateGcm(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
      OneSignal.idsAvailable((userId, registrationId) -> {
        Map<String, String> param = new HashMap<>();
        param.put("id", usuario.getId());
        param.put("gcmid", userId);
        REST.getRest()
            .gcm(usuario.getToken(), param)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(Observable.empty())
            .subscribe();
      });
    }
  }

  public void Log(String msg) {
    if (BuildConfig.DEBUG) Logger.e(msg);
  }

  public void Log(@NonNull Throwable throwable) {
    if (BuildConfig.DEBUG) Logger.e(throwable.getMessage());
  }

  @NonNull protected Observable<EntryResponse> getLocalEntry() {
    EntryResponse entryResponse = new EntryResponse();
    return entryResponse.getObject(EntryResponse.class).asObservable();
  }

  protected void configToolbarChild(@NonNull Toolbar toolbar, int idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
    toolbarText.setText(getString(idRes));
    final Drawable upArrow = getResources().getDrawable(R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  protected void configBackToolbar(@NonNull Toolbar toolbar) {
    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  protected void configToolbar(@NonNull Toolbar toolbar, int idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
    toolbarText.setText(getString(idRes));
  }

  protected void configToolbarChild(@NonNull Toolbar toolbar, String idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);

    toolbarText.setText(idRes);
    final Drawable upArrow = getResources().getDrawable(R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  public void goActv(Class<?> cls, boolean clear) {
    Intent intent = new Intent(getApplicationContext(), cls);
    if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
  }

  public void goActv(@NonNull Intent intent, boolean clear) {
    if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
  }

  @NonNull protected Observable<Object> RxParseJson(@NonNull String json, @NonNull Class clss) {
    return Observable.create(sub -> {
      try {
        sub.onNext(gson.fromJson(json, clss));
      } catch (Throwable e) {
        Log(e.getMessage());
        sub.onError(e);
      }
      sub.onCompleted();
    });
  }

  @NonNull protected Observable<Object> RxParseJson(@NonNull String json, @NonNull Type clss) {
    return Observable.create(sub -> {
      try {
        sub.onNext(gson.fromJson(json, clss));
      } catch (Throwable e) {
        Log(e.getMessage());
        sub.onError(e);
      }
      sub.onCompleted();
    });
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
  }

  public boolean validateEmail(@NonNull String email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validateEmail(@NonNull CharSequence email) {
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validateName(@NonNull CharSequence name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }

  public boolean validateName(@NonNull String name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }

  public boolean validateFirstName(@NonNull String firstName) {
    return firstName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
  } // end method validateFirstName

  // validate last name
  public boolean validateLastName(@NonNull String lastName) {
    return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
  }

  public boolean validatePassword(@NonNull String password) {
    return password.length() > 4;
  }

  private void hideKeyboard() {
    View view = getCurrentFocus();
    if (view != null) {
      ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
          hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void showDialog(@NonNull String text) {
    runOnUiThread(() -> {

      if (!isOnpause) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).title(R.string.app_name)
            .content(text)
            .progress(true, 0)
            .autoDismiss(false)
            .progressIndeterminateStyle(true);
        loading = builder.build();
        if (!isOnpause) loading.show();
      }
    });
  }

  public void dismissDialog() {
    runOnUiThread(() -> {
      if (loading != null && !isOnpause) loading.dismiss();
    });
  }

  @NonNull private static String getAppLable(@NonNull Context pContext) {
    PackageManager lPackageManager = pContext.getPackageManager();
    ApplicationInfo lApplicationInfo = null;
    try {
      lApplicationInfo =
          lPackageManager.getApplicationInfo(pContext.getApplicationInfo().packageName, 0);
    } catch (@NonNull final PackageManager.NameNotFoundException ignored) {
    }
    return (String) (lApplicationInfo != null ? lPackageManager.getApplicationLabel(
        lApplicationInfo) : "Unknown");
  }

  public void showMaterialDialog(@NonNull String body,
      @NonNull final onClickCallback onClickCallback) {
    dismissDialog();
    if (!isOnpause) {
      clearMdialog();
      materialDialog = new MaterialDialog.Builder(this).title(getAppLable(this))
          .content(body)
          .positiveText(getString(R.string.acept))
          .negativeText(getString(R.string.cancel))
          //.showListener(dialog -> log("onShow"))
          //.cancelListener(dialog -> log("onCancel"))
          .dismissListener(dialog -> {
            //log("onDismiss");
            onClickCallback.onDissmis();
          })
          .callback(new MaterialDialog.ButtonCallback() {
            @Override public void onPositive(MaterialDialog dialog) {
              onClickCallback.onPositive(true);
            }

            @Override public void onNegative(MaterialDialog dialog) {
              onClickCallback.onNegative(true);
            }

            @Override public void onNeutral(MaterialDialog dialog) {
              onClickCallback.onPositive(true);
            }
          })
          .show();
    }
  }

  private void clearMdialog() {
    if (materialDialog != null && materialDialog.isShowing()) {
      materialDialog.dismiss();
      materialDialog = null;
    }
  }

  public void showErr(@NonNull String text) {
    dismissDialog();
    runOnUiThread(() -> showMaterialDialog(text, new onClickCallback() {
      @Override public void onPositive(boolean result) {

      }

      @Override public void onDissmis() {

      }

      @Override public void onNegative(boolean result) {

      }
    }));
  }

  public interface onClickCallback {
    void onPositive(boolean result);

    void onDissmis();

    void onNegative(boolean result);
  }

  @Subscribe public void onEvent(TabPosition tabPosition) {
  }

  @Subscribe public void onEvent(List<Categoria> categorias) {
    log("onEvent categorias");
  }

  public void errControl(@NonNull Throwable throwable) {
    dismissDialog();
    if (throwable instanceof RetrofitException) {
      try {
        RetrofitException error = (RetrofitException) throwable;
        if (error.getErrorBodyAs(ErrorControl.class) != null) {
          ErrorControl errorControl = error.getErrorBodyAs(ErrorControl.class);
          assert errorControl != null;
          showErr(errorControl.getMensaje() != null ? errorControl.getMensaje()
              : getString(R.string.general_err));
        }
      } catch (IOException e) {
        log(e.getMessage());
        dismissDialog();
        String msg = getString(R.string.general_err);
        showErr(msg);
      }
    } else {
      log("posible time out" + throwable.getMessage());
      String msg = getString(R.string.general_err);
      dismissDialog();
      showErr(msg);
    }
  }

  public boolean validate(@Nullable EditText autoCompleteTextView) {
    boolean res = false;
    if (autoCompleteTextView != null
        && autoCompleteTextView.getText() != null
        && !autoCompleteTextView.getText().toString().equalsIgnoreCase("")) {
      // log("act --> " + autoCompleteTextView.getText().toString());
      res = true;
    }
    return res;
  }

  public void log(String log) {
    Logger.e(log);
  }

  private String getClassName() {
    return this.getClass().getName();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
    realm.close();
  }

  public Realm getRealm() {

    realm = !realm.isClosed() ? realm = Realm.getDefaultInstance() : realm;
    String status = realm.isClosed() + "";
    String transacction = realm.isInTransaction() + "";

    log("getRealm status  isClosed: " + status + " isInTransaction: " + transacction);
    return realm;
  }

  public Usuario getUserSync() {
    return getRealm().where(Usuario.class).findFirst();
  }

  public void updateRealmUser(@NonNull Usuario usuario) {
    log("upDateRealmUser");

    getRealm().executeTransaction(realm1 -> {
      Usuario u = realm1.where(Usuario.class).findFirst();

      if (usuario.getFoto() != null) {
        u.setFoto(usuario.getFoto());
      }
      if (usuario.getNombre() != null) {
        u.setNombre(usuario.getNombre());
      }
      if (usuario.getApellido() != null) {
        u.setApellido(usuario.getApellido());
      }
      if (usuario.getCorreo() != null) {
        u.setCorreo(usuario.getCorreo());
      }
      if (usuario.getTelefono() != null) {
        u.setTelefono(usuario.getTelefono());
      }
      realm1.copyToRealmOrUpdate(u);
      u.addChangeListener(() -> log("usuario ha cambiado " + AppMain.getGson().toJson(u)));
    });
  }

  public void setItemsVisibility(@NonNull Menu menu, MenuItem exception, boolean visible) {
    for (int i = 0; i < menu.size(); ++i) {
      MenuItem item = menu.getItem(i);
      if (item != exception) item.setVisible(visible);
    }
  }

  public void goCategoria(Categoria c) {
    Intent i = new Intent(this, categoria.class);
    i.putExtra(categoria.class.getSimpleName(), AppMain.getGson().toJson(c));
    goActv(i, false);
  }

  public Gson getGson() {
    return AppMain.getGson();
  }

  public int getResourceId(String pVariableName, String pResourcename) {
    try {
      return getResources().getIdentifier(pVariableName, pResourcename, getPackageName());
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void clearDB() {

    getRealm().executeTransaction(realm1 -> {
      realm1.where(Usuario.class).findFirst().removeFromRealm();
      realm1.close();
      LoginManager.getInstance().logOut();
      runOnUiThread(() -> goActv(Splash.class, true));
    });
  }

  public void share(@NonNull String body, String urlImage) {
    Picasso.with(this).load(urlImage).into(new Target() {
      @Override public void onBitmapLoaded(@NonNull Bitmap bitmap, Picasso.LoadedFrom from) {
        Uri uri = getShareableUri(BaseActivity.this, bitmap);
        assert uri != null;
        IntentShare.with(BaseActivity.this)
            .chooserTitle(getString(R.string.compartir))
            .text(body)
            .mailBody(body)
            .mailBody(getAppLable(getBaseContext()))
            .image(uri)
            .deliver();
      }

      @Override public void onBitmapFailed(Drawable errorDrawable) {
        log("bitmapFailed");
      }

      @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
        log("onPrepareload");
      }
    });
  }

  @Nullable protected Uri convertFileToContentUri(@NonNull Context context, @NonNull File imageFile)
      throws Exception {

    String filePath = imageFile.getAbsolutePath();
    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
            new String[] { filePath }, null);

    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      Uri baseUri = Uri.parse("content://media/external/images/media");
      return Uri.withAppendedPath(baseUri, "" + id);
    } else {
      if (imageFile.exists()) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        return context.getContentResolver()
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
      } else {
        return null;
      }
    }
  }

  private static String convertToHex(@NonNull byte[] data) {
    StringBuilder buf = new StringBuilder();
    for (byte b : data) {
      int halfbyte = (b >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
            : (char) ('a' + (halfbyte - 10)));
        halfbyte = b & 0x0F;
      } while (two_halfs++ < 1);
    }
    return buf.toString();
  }

  public static String SHA1(@NonNull String text)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.update(text.getBytes("iso-8859-1"), 0, text.length());
    byte[] sha1hash = md.digest();
    return convertToHex(sha1hash);
  }

  public void openUrl(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(intent);
  }

  public void share(@NonNull String body) {
    IntentShare.with(this).chooserTitle(getString(R.string.compartir)).text(body).deliver();
  }

  @Nullable public Uri getImageContentUri(@NonNull Context context, @NonNull String absPath) {
    log("getImageContentUri: " + absPath);

    Cursor cursor = context.getContentResolver()
        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
            new String[] { absPath }, null);

    if (cursor != null && cursor.moveToFirst()) {
      int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
      return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
          Integer.toString(id));
    } else if (!absPath.isEmpty()) {
      ContentValues values = new ContentValues();
      values.put(MediaStore.Images.Media.DATA, absPath);
      return context.getContentResolver()
          .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    } else {
      return null;
    }
  }

  public void getFileFromUrl(String url) {
    RxFile.createFileFromUri(this, Uri.parse(url))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<File>() {
          @Override public void onCompleted() {
            log("onCompleted() for File called");
          }

          @Override public void onError(@NonNull Throwable e) {
            log("Error on file fetching:" + e.getMessage());
          }

          @Override public void onNext(@NonNull File file) {
            log("onNext() file called:" + file.getAbsolutePath());
          }
        });
  }

  private Uri getShareableUri(@NonNull Context context, @NonNull Bitmap bitmap) {
    // Compress the drawingCache before saving and sharing.
    final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, SHARED_IMAGE_QUALITY, bytes);

    // Write the compressed bytes to a files
    final File outputDirectory = new File(context.getFilesDir(), SHARED_DIRECTORY);
    if (outputDirectory.isDirectory() || outputDirectory.mkdirs()) {
      final File shareColorFile = new File(outputDirectory, SHARED_IMAGE_FILE);
      try {
        final FileOutputStream fo = new FileOutputStream(shareColorFile);
        fo.write(bytes.toByteArray());
        fo.close();

        // Get the content uri.
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, shareColorFile);
      } catch (IOException e) {
        e.printStackTrace();
        log("Fail to write bitmap inside the temp file.");
      }
    } else {
      log("Fail to create temp file for bitmap sharing.");
    }
    return null;
  }
}
