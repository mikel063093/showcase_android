package com.co.showcase.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.Usuario;

import com.co.showcase.ui.home.home;
import com.co.showcase.ui.login_main.login;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import rx.android.schedulers.AndroidSchedulers;

public class Splash extends RxAppCompatActivity {
  private final Handler handler = new Handler();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDB();
    setContentView(R.layout.splash);

    Usuario.getItem()
        .compose(this.bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onSuccesUser, this::onFailUser);
  }

  private void onSuccesUser(Usuario usuario) {
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
      log("userOnDB " + usuario.getNombre());
      Intent intent = new Intent(getApplicationContext(), home.class);
      goActv(intent, true);
    } else {
      onFailUser(new Throwable("user null"));
    }
  }

  private void onFailUser(Throwable throwable) {
    log("onFailUser " + throwable.getMessage());
    Intent intent = new Intent(getApplicationContext(), login.class);
    goActv(intent, true);
  }

  private void initDB() {
    AppMain appmain = (AppMain) getApplication();
    appmain.initRxDb();
  }

  @Override protected void onResume() {
    super.onResume();
    initDB();
  }

  private void log(String msg) {
    if (BuildConfig.DEBUG) {
      Logger.e(msg);
    }
  }

  private void goActv(@NonNull Intent intent, boolean clear) {
    log("goActv");
    runOnUiThread(() -> {
      log("handler");
      handler.postDelayed(() -> {
        log("handler ok");
        if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
      }, 2000);

      //overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    });
  }
}
