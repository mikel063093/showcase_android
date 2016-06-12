package com.co.showcase.ui.login_main;

import android.os.Bundle;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.singin.singin;
import com.facebook.login.widget.LoginButton;

public class login extends BaseActivity {

 // @Bind(R.id.auth_fb) LoginButton authFb;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login);
    ButterKnife.bind(this);
  }

  @OnClick({ R.id.btn_fb, R.id.btn_ingresar, R.id.btn_registrar }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_fb:
        break;
      case R.id.btn_ingresar:
        goActv(singin.class, false);
        break;
      case R.id.btn_registrar:
        goActv(singin.class, false);

        break;
    }
  }
}
