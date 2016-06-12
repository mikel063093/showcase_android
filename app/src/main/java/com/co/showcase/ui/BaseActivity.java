package com.co.showcase.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.EntryResponse;
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by miguelalegria on 15/5/16 for DemoMike.
 */
public class BaseActivity extends AppCompatActivity {
  public Gson gson = new Gson();
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private Matcher matcher;

  public void showMessageOnSnakeBar(View view, String msg) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
  }

  public void Log(String msg) {
    if (BuildConfig.DEBUG) Logger.e(msg);
  }

  public void Log(Throwable throwable) {
    if (BuildConfig.DEBUG) Logger.e(throwable.getMessage());
  }

  public Observable<EntryResponse> getLocalEntry() {
    EntryResponse entryResponse = new EntryResponse();
    return entryResponse.getObject(EntryResponse.class).asObservable();
  }

  public void configToolbarChild(Toolbar toolbar, int idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
    toolbarText.setText(getString(idRes));
    final Drawable upArrow = getResources().getDrawable(R.drawable.ico_flecha_blanca);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  public void configToolbar(Toolbar toolbar, int idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);
    toolbarText.setText(getString(idRes));
  }

  public void configToolbarChild(Toolbar toolbar, String idRes) {
    AppCompatTextView toolbarText = (AppCompatTextView) toolbar.findViewById(R.id.txt_toolbar);

    toolbarText.setText(idRes);
    final Drawable upArrow = getResources().getDrawable(R.drawable.ico_flecha_blanca);
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

  public Observable<Object> RxParseJson(@NonNull String json, @NonNull Class clss) {
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

  public Observable<Object> RxParseJson(@NonNull String json, @NonNull Type clss) {
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

  public boolean validateEmail(String email) {
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validatePassword(String password) {
    return password.length() > 5;
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
}
