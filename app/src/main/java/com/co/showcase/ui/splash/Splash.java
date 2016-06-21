package com.co.showcase.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.Master.MasterActivity;
import com.co.showcase.ui.login_main.login;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import gr.net.maroulis.library.EasySplashScreen;

public class Splash extends RxAppCompatActivity {
  private final Handler handler = new Handler();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDB();
    handler.postDelayed(() -> {
      Intent intent = new Intent(getApplicationContext(), login.class);
      goActv(intent, true);
    }, 1500);
    setContentView(R.layout.splash);
  }

  private void initDB() {
    AppMain appmain = (AppMain) getApplication();
    appmain.initRxDb();
  }

  @Override protected void onResume() {
    super.onResume();
    initDB();
  }

  private void goActv(@NonNull Intent intent, boolean clear) {
    if (clear) intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
  }
}
