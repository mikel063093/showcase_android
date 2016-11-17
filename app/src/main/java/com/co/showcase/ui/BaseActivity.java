package com.co.showcase.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.EntryResponse;
import com.co.showcase.model.ResponseAutoComplete;
import com.co.showcase.model.ResponseVerCarrito;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.Zonas;
import com.co.showcase.ui.categoria.categoria;
import com.co.showcase.ui.direccion.direcciones;
import com.co.showcase.ui.historial.historial;
import com.co.showcase.ui.historial.pedidos_proceso;
import com.co.showcase.ui.home.SuggestionAdapter;
import com.co.showcase.ui.home.home;
import com.co.showcase.ui.map.map;
import com.co.showcase.ui.pedido.carritoPedidos;
import com.co.showcase.ui.perfil.perfil;
import com.co.showcase.ui.search_result.result;
import com.co.showcase.ui.splash.Splash;
import com.co.showcase.ui.terminos.terminos;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.facebook.login.LoginManager;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import fr.tvbarthel.intentshare.IntentShare;
import io.realm.Realm;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by miguelalegria on 15/5/16 for DemoMike.
 */
public class BaseActivity extends RxAppCompatActivity implements SearchView.OnQueryTextListener {
  protected static final String KEY_POSITION = "KEYPOSTION";
  private static final int SHARED_IMAGE_QUALITY = 100;
  private static final String SHARED_DIRECTORY = "sharing";
  private static final String SHARED_IMAGE_FILE = "shared_img.png";
  private static final String FILE_PROVIDER_AUTHORITY = "com.co.showcase.SharingFileProvider";
  private static final String URL_ERROR_KEY = "Url_err";
  private static final String ZONA_ERROR_KEY = "Zona_err";
  @NonNull public Gson gson = new Gson();
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  public static String NETWORK_ERROR_KEY = "Netwoerk_error";
  private static final String NAME_PATTERN = "^[\\\\p{L} .'-]+$";
  private Pattern patternName = Pattern.compile(NAME_PATTERN);
  private boolean isOnpause;
  private MaterialDialog loading;
  private Realm realm;
  @Nullable private MaterialDialog materialDialog;
  private boolean isToolbarPretty = false;
  private Menu menu;
  private SubMenu subMenuMap;
  private SearchView.SearchAutoComplete searchSrcTextView;
  private MenuItem searchItem;
  private SearchView searchView;

  public void setToolbarPretty(boolean toolbarPretty) {
    isToolbarPretty = toolbarPretty;
  }

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
    hideKeyboard();
    initDB();
    isOnpause = false;
    updateGcm(getUserSync());
    verCarrito(getUserSync());
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
    //appmain.initRxDb();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    log("onCreate");
    realm = Realm.getDefaultInstance();
    initDB();
    autocompleteSearch("", true);

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

    return null;
  }

  protected void configBackToolbar(@NonNull Toolbar toolbar) {
    setToolbarPretty(true);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  protected void configBackToolbar(@NonNull Toolbar toolbar, boolean goHome) {
    setToolbarPretty(true);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setTitle(R.string.app_name);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> {
      if (goHome) {
        goActv(home.class, true);
      } else {
        finish();
      }
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  //protected void configToolbar(@NonNull Toolbar toolbar, int idRes) {
  //  AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
  //  toolbarText.setText(getString(idRes));
  //}
  //
  //protected void configToolbarChild(@NonNull Toolbar toolbar, String idRes) {
  //  AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
  //  toolbarText.setText(idRes);
  //  final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
  //  toolbar.setNavigationIcon(upArrow);
  //  toolbar.setNavigationOnClickListener(v -> {
  //    finish();
  //    overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
  //  });
  //}

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
    if (searchView != null && searchView.isShown()) {
      searchItem.collapseActionView();
      searchView.setQuery("", false);
    } else {
      super.onBackPressed();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
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
          .dismissListener(dialog -> onClickCallback.onDissmis())
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

  @Subscribe public void onEvent(Usuario usuario) {
    log("onEvent Usuario act");
  }

  @Subscribe public void onEvent(List<Categoria> categorias) {
    log("onEvent categorias");
  }

  public void errControl(@NonNull Throwable throwable) {
    dismissDialog();
    if (throwable instanceof HttpException) {
      // We had non-2XX http error
      log("F** error" + throwable.getMessage());
      String msg = getString(R.string.general_err);
      dismissDialog();
      showErr(msg);
      Answers.getInstance()
          .logCustom(new CustomEvent(NETWORK_ERROR_KEY).putCustomAttribute("ERROR",
              throwable.getMessage()));
    }
    if (throwable instanceof IOException) {
      // A network or conversion error happened
      log("F** error" + throwable.getMessage());
      String msg = getString(R.string.internet_err);
      dismissDialog();
      showErr(msg);
      Answers.getInstance()
          .logCustom(new CustomEvent(NETWORK_ERROR_KEY).putCustomAttribute("ERROR",
              throwable.getMessage()));
    }
  }

  public boolean validate(@Nullable EditText autoCompleteTextView) {
    boolean res = false;
    if (autoCompleteTextView != null
        && autoCompleteTextView.getText() != null
        && !autoCompleteTextView.getText().toString().equalsIgnoreCase("")) {
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
    //    log("getRealm status  isClosed: " + status + " isInTransaction: " + transacction);
    return realm;
  }

  public Usuario getUserSync() {
    return getRealm().where(Usuario.class).findFirst();
  }

  public void updateRealmUser(@NonNull Usuario usuario) {
    log("upDateRealmUser");
    runOnUiThread(() -> EventBus.getDefault().post(usuario));

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

      u.addChangeListener(element -> {
        log("u ha cambiado ");
      });
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
      log(e.getMessage());
      return -1;
    }
  }

  public void clearDB() {
    getRealm().executeTransaction(realm1 -> {
      realm1.where(Usuario.class).findFirst().deleteFromRealm();
      realm1.close();
      LoginManager.getInstance().logOut();
      runOnUiThread(() -> goActv(Splash.class, true));
    });
  }

  public static final int MAX_WIDTH = 1024;
  public static final int MAX_HEIGHT = 768;

  public void share(@NonNull String body, String urlImage) {
    if (urlImage != null) {
      body = Html.fromHtml(body).toString();
      String finalBody = body;

      Picasso.with(this)
          .load(urlImage)
          .resize(MAX_WIDTH, MAX_HEIGHT)
          .onlyScaleDown()
          .into(new Target() {
            @Override public void onBitmapLoaded(@NonNull Bitmap bitmap, Picasso.LoadedFrom from) {
              log(finalBody + " img size " + bitmap.getRowBytes());
              Uri uri = getShareableUri(BaseActivity.this, bitmap);
              assert uri != null;
              IntentShare.with(BaseActivity.this)
                  .chooserTitle(getString(R.string.compartir))
                  .text(finalBody)
                  .mailBody(finalBody)
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
  }

  public void share(@NonNull String body, Bitmap bitmap, String url) {
    if (bitmap != null) {
      body = Html.fromHtml(body).toString();
      String finalBody = body;
      log(finalBody + " img size " + bitmap.getRowBytes());
      Uri uri = getShareableUri(BaseActivity.this, bitmap);
      assert uri != null;

      if(url!=null){
        IntentShare.with(BaseActivity.this)
            .chooserTitle(getString(R.string.compartir))
            .text(finalBody)
            .facebookBody(Uri.parse( url))
            .mailBody(finalBody)
            .mailSubject(this.validateEmail(finalBody)? finalBody:"")
            .twitterBody(finalBody)
            .image(uri)
            .deliver();
      }else {
        IntentShare.with(BaseActivity.this)
            .chooserTitle(getString(R.string.compartir))
            .text(finalBody)
            .mailBody(finalBody)
            .mailSubject(this.validateEmail(finalBody)? finalBody:"")
            .twitterBody(finalBody)
            .image(uri)
            .deliver();
      }

    }
  }

  public void openUrl(String url) {
    if (url != null && url.length() > 0 && URLUtil.isValidUrl(url)) {
      // URL is valid
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
      startActivity(intent);
    } else {
      Answers.getInstance()
          .logCustom(new CustomEvent(URL_ERROR_KEY).putCustomAttribute("ERROR", url));
    }
  }

  public void share(@NonNull String body) {
    body = Html.fromHtml(body).toString();
    IntentShare.with(this).chooserTitle(getString(R.string.compartir)).text(body).deliver();
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

  @Override public boolean onQueryTextSubmit(String query) {
    Log(query);
    if (query.length() > 0) autocompleteSearch(query);
    return query != null;
  }

  private void autocompleteSearch(String query) {
    Usuario user = getUserSync();
    if (user != null && user.getToken() != null) {
      Map<String, String> param = new HashMap<>();
      param.put("palabra", query != null ? query.toLowerCase() : "");
      REST.getRest()
          .autoCompletarBusqueda(user.getToken(), param)
          .debounce(150, MILLISECONDS)
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.empty())
          .subscribe(this::succesAutoComplete);
    }
  }

  private void autocompleteSearch(String query, boolean first) {
    Usuario user = getUserSync();
    log("autocompletSearch");
    if (query != null && first && user != null && user.getToken() != null) {
      log("autocompletSearch OK");
      Map<String, String> param = new HashMap<>();
      param.put("palabra", query);
      REST.getRest()
          .autoCompletarBusqueda(user.getToken(), param)
          .debounce(150, MILLISECONDS)
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.empty())
          .subscribe(this::succesAutoComplete);
    }
  }

  private void succesAutoComplete(@NonNull ResponseAutoComplete responseAutoComplete) {
    if (responseAutoComplete.estado == 1 && searchView != null) {
      searchSrcTextView.setAdapter(new SuggestionAdapter<>(this, R.layout.item_search_auto_complete,
          responseAutoComplete.palabras));
      searchSrcTextView.setOnItemClickListener((adapterView, view, i, l) -> {
        log("autocomplete position: " + i + " data : " + searchSrcTextView.getAdapter()
            .getItem(i)
            .toString());
        Intent goResult = new Intent(this, result.class);
        goResult.putExtra(result.class.getSimpleName(),
            searchSrcTextView.getAdapter().getItem(i).toString());
        goActv(goResult, false);
      });
    }
  }

  @Override public boolean onQueryTextChange(String newText) {
    Log(newText);
    autocompleteSearch(newText);
    return newText != null;
  }

  public void setupToolbar(Toolbar toolbar) {
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
  }

  @Override public boolean onCreateOptionsMenu(@NonNull Menu menu) {
    if (isToolbarPretty) {
      this.menu = menu;
      getMenuInflater().inflate(R.menu.menu_main, menu);
      MenuItem map = menu.findItem(R.id.action_map);
      subMenuMap = map.getSubMenu();
      Usuario usuario = getUserSync();
      verCarrito(usuario);
      getZonas(usuario);
      searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
      searchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(
          android.support.v7.appcompat.R.id.search_src_text);
      searchSrcTextView.setThreshold(0);
      autocompleteSearch("", true);
      autocompleteSearch("", true);
      searchItem = menu.findItem(R.id.action_search);
      MenuItemCompat.setOnActionExpandListener(searchItem,
          new MenuItemCompat.OnActionExpandListener() {
            @Override public boolean onMenuItemActionCollapse(MenuItem item) {
              Log("closeMenuSearch");
              setItemsVisibility(menu, searchItem, true);
              return true;
            }

            @Override public boolean onMenuItemActionExpand(MenuItem item) {
              return true;
            }
          });
      searchView.setOnQueryTextListener(this);
      searchView.setIconifiedByDefault(true);
      searchView.setSubmitButtonEnabled(false);
      searchView.setOnSearchClickListener(v -> {
        Log("openSearch");
        setItemsVisibility(menu, searchItem, false);
      });
      searchView.setOnCloseListener(() -> {
        Log("closeSearch");
        setItemsVisibility(menu, searchItem, true);
        return false;
      });
    }
    return isToolbarPretty;
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    TypedArray typedArray = getResources().obtainTypedArray(R.array.zona_ids);

    for (int ind = 0; ind < typedArray.length(); ind++) {
      if (typedArray.getResourceId(ind, 0) == item.getItemId()) {
        log(this.getClassName());
        if (this.getClass() != map.class) {
          goActv(new Intent(this, map.class).putExtras(item.getIntent().getExtras()), false);
        }
        break;
      }
    }

    typedArray.recycle();
    switch (item.getItemId()) {
      case R.id.action_buy:
        if (this.getClass() != carritoPedidos.class) goActv(carritoPedidos.class, false);
        break;
      case R.id.action_map:
        log("action map");
        break;
      case R.id.action_search:
        log("action search");
        return true;
      case R.id.action_perfil:
        if (this.getClass() != perfil.class) goActv(perfil.class, false);
        break;
      case R.id.action_direcciones:
        if (this.getClass() != direcciones.class) goActv(direcciones.class, false);
        break;
      case R.id.action_salir:
        showMaterialDialog(getString(R.string.salir), new onClickCallback() {
          @Override public void onPositive(boolean result) {
            clearDB();
          }

          @Override public void onDissmis() {

          }

          @Override public void onNegative(boolean result) {

          }
        });
        break;
      case R.id.action_pedidos:
        if (this.getClass() != pedidos_proceso.class) goActv(pedidos_proceso.class, false);
        break;
      case R.id.action_historial:
        if (this.getClass() != historial.class) goActv(historial.class, false);
        break;
      case R.id.terminos_condiciones:
        if (this.getClass() != terminos.class) goActv(terminos.class, false);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void verCarrito(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      REST.getRest()
          .verCarrito(usuario.getToken(), new HashMap<>())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.empty())
          .subscribe(this::succesVerCarrito);
    }
  }

  private void succesVerCarrito(ResponseVerCarrito responseVerCarrito) {
    if (responseVerCarrito != null
        && responseVerCarrito.getEstado() == 1
        && responseVerCarrito.getCarrito() != null
        && responseVerCarrito.getCarrito().getFechaCreacion() != null) {

      showMenuCarrito(responseVerCarrito.getCarrito().getItems() != null
          && responseVerCarrito.getCarrito().getItems().size() > 0);
    } else {
      showMenuCarrito(false);
    }
  }

  private void showMenuCarrito(boolean show) {
    log("shoeMenuCarrito");
    if (menu != null) {
      log("shoeMenuCarrito ok");
      MenuItem carrito = menu.findItem(R.id.action_buy);
      Drawable drawable = show ? ContextCompat.getDrawable(this, R.drawable.btn_carrito_bandera)
          : ContextCompat.getDrawable(this, R.drawable.btn_carrito);
      if (carrito != null) carrito.setIcon(drawable);
    }
  }

  private void getZonas(Usuario usuario) {
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", usuario.getId());
      REST.getRest()
          .zonas(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.empty())
          .subscribe(this::renderZonasMenu);
    }
  }

  private void renderZonasMenu(Zonas zonas) {
    if (zonas != null && zonas.getEstado() == 1 && subMenuMap != null) {
      for (Zonas.ZonasBean zonasBean : zonas.getZonas()) {
        int id = getResourceId(zonasBean.nombre.toLowerCase(), "id");
        if (id != -1) {
          MenuItem menu = subMenuMap.add(0, id, Menu.NONE, zonasBean.nombre);
          Intent i = new Intent(this, com.co.showcase.ui.map.map.class);
          i.putExtra(map.class.getSimpleName(), zonasBean.id);
          menu.setIntent(i);
        } else {
          log("err buscado id zona " + zonasBean.nombre.toLowerCase());
          Answers.getInstance()
              .logCustom(
                  new CustomEvent(ZONA_ERROR_KEY).putCustomAttribute("ERROR", zonasBean.nombre));
        }
      }
    }
  }
}
