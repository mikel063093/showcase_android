package com.co.showcase.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.Master.MasterActivity;
import com.co.showcase.ui.login_main.login;
import gr.net.maroulis.library.EasySplashScreen;

public class Splash extends BaseActivity {
  private final Handler handler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    handler.postDelayed(() -> {
      Intent intent = new Intent(getApplicationContext(), login.class);
      goActv(intent, true);
    }, 1500);
    setContentView(R.layout.splash);
  }
}
