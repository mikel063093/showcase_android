package com.co.showcase.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.EntryResponse;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by miguelalegria on 15/5/16 for DemoMike.
 */
public class BaseActivity extends RxAppCompatActivity {
  protected static final String KEY_POSITION = "KEYPOSTION";

  @NonNull public Gson gson = new Gson();
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

  private static final String NAME_PATTERN = "^[\\\\p{L} .'-]+$";
  private Pattern patternName = Pattern.compile(NAME_PATTERN);
  private boolean isOnpause;
  private MaterialDialog loading;
  @Nullable private MaterialDialog materialDialog;

  private void showMessageOnSnakeBar(@NonNull View view, @NonNull String msg) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
  }

  @Override protected void onPause() {
    super.onPause();
    isOnpause = true;
  }

  @Override protected void onResume() {
    super.onResume();
    initDB();
    isOnpause = false;
    Usuario usuario = Usuario.GetItem();
    if (usuario != null) {
      updateGcm(usuario);
    }
  }

  @Override protected void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Override protected void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  private void initDB() {
    AppMain appmain = (AppMain) getApplication();
    appmain.initRxDb();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDB();
    new ReactiveNetwork().observeConnectivity(this)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(connectivityStatus -> {
          switch (connectivityStatus) {
            case WIFI_CONNECTED_HAS_NO_INTERNET:
            case OFFLINE:
            case UNKNOWN:
              showMessageOnSnakeBar(findViewById(R.id.container_root), getString(R.string.offile));
              break;
          }
        });
    //
    //
  }

  private void updateGcm(@NonNull Usuario usuario) {
    OneSignal.idsAvailable((userId, registrationId) -> {
      Map<String, String> param = new HashMap<>();
      param.put("id", usuario.getId());
      param.put("gcmid", userId);
      REST.getRest()
          .gcm(param)
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.empty())
          .subscribe();
    });
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

  protected void goActv(Class<?> cls, boolean clear) {
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

  public boolean validateFirstName(String firstName) {
    return firstName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
  } // end method validateFirstName

  // validate last name
  public boolean validateLastName(String lastName) {
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
    if (!isOnpause) {
      clearMdialog();
      materialDialog = new MaterialDialog.Builder(this).title(getAppLable(AppMain.getContex()))
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

  public void errControl(@NonNull Throwable throwable) {
    dismissDialog();
    //log(throwable.getMessage());
    if (throwable instanceof HttpException) {
      HttpException err = (HttpException) throwable;
      // log("Status err " + err.code() + err.message());
      showErr(err.getMessage() != null ? err.getMessage() : getString(R.string.internet_err));
    } else {
      showErr(getString(R.string.general_err));
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

  protected void log(String log) {
    Log.e(getClassName(), log);
  }

  private String getClassName() {
    return this.getClass().getName();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    ButterKnife.unbind(this);
  }
}
