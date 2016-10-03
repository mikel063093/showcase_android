package com.co.showcase.ui.login_main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.home.home;
import com.co.showcase.ui.singin.singin;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class login extends BaseActivity {

  @Nullable @Bind(R.id.auth_fb) LoginButton authButton;
  private CallbackManager callbackManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FacebookSdk.sdkInitialize(getApplicationContext());
    callbackManager = CallbackManager.Factory.create();
    setContentView(R.layout.login);
    ButterKnife.bind(this);
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    if (accessToken != null) {
      socialSingin(accessToken.getToken());
    }
  }

  @Nullable private FacebookCallback<LoginResult> loginResultFacebookCallback =
      new FacebookCallback<LoginResult>() {
        @Override public void onSuccess(@NonNull LoginResult loginResult) {
          dismissDialog();
          AccessToken access_token = loginResult.getAccessToken();
          socialSingin(access_token.getToken());
        }

        @Override public void onCancel() {
          dismissDialog();
          AccessToken accessToken = AccessToken.getCurrentAccessToken();
          if (accessToken != null) {
            LoginManager.getInstance().logOut();
          }
        }

        @Override public void onError(@NonNull FacebookException exception) {

          dismissDialog();

          AccessToken accessToken = AccessToken.getCurrentAccessToken();
          if (accessToken != null) {
            LoginManager.getInstance().logOut();
          }
        }
      };

  @Override protected void onResume() {
    super.onResume();
    assert authButton != null;
    authButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
    authButton.registerCallback(callbackManager, loginResultFacebookCallback);
  }

  private void socialSingin(@NonNull String token) {

    log("socialSingin " + token);
    Map<String, String> param = new HashMap<>();
    param.put("access_token", token);

    REST.getRest()
        .registrarFB(param)
        .compose(bindToLifecycle())
        .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
        .subscribeOn(Schedulers.io())
        .doOnCompleted(this::dismissDialog)
        .observeOn(AndroidSchedulers.mainThread())
        //.onErrorResumeNext(Observable.error(new Throwable("Custom error")))
        .subscribe(this::onSuccesLogin, this::errControl);
  }

  private void onSuccesLogin(Usuario u) {
    //log("onSucceslogin " + u.getEstado());
    if (u != null && u.getEstado().equalsIgnoreCase("exito")) {
      // log("onSuccesOK");
      u.save()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .compose(this.bindToLifecycle())
          .subscribe(aVoid -> {
            LoginManager.getInstance().logOut();
            goActv(home.class, true);
          });
    } else {
      assert u != null;
      showErr(u.getMensaje() != null ? u.getMensaje() : getString(R.string.general_err));
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @OnClick({ R.id.btn_fb, R.id.btn_ingresar, R.id.btn_registrar })
  public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_fb:
        assert authButton != null;
        authButton.performClick();
        break;
      case R.id.btn_ingresar:
        Intent intent = new Intent(this, singin.class);
        intent.putExtra(KEY_POSITION, 0);
        goActv(intent, false);
        break;
      case R.id.btn_registrar:
        Intent intent2 = new Intent(this, singin.class);
        intent2.putExtra(KEY_POSITION, 1);
        goActv(intent2, false);
        break;
    }
  }
}
