package com.co.showcase.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.co.showcase.BuildConfig;

import com.co.showcase.model.Categoria;
import com.co.showcase.model.Usuario;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

import io.realm.Realm;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by miguelalegria on 5/6/16 for showcase.
 */

public class BaseFragment extends RxFragment {
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private static final String NAME_PATTERN = "^[\\\\p{L} .'-]+$";
  private Pattern patternName = Pattern.compile(NAME_PATTERN);
  private Matcher matcher;
  public boolean isOnPause = false;
  private Realm realm;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    realm = Realm.getDefaultInstance();
  }

  @Override public void onResume() {
    super.onResume();
    log("onResume");
    isOnPause = false;
    if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
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

  @Override public void onPause() {
    super.onPause();
    log("onPause");
    isOnPause = true;
  }

  @Override public void onStop() {
    super.onStop();
    log("onStop");
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    log("onDestroyView");
    EventBus.getDefault().unregister(this);
  }

  @Subscribe public void onEvent(Object obj) {

  }

  @Subscribe public void onEvent(List<Categoria> categorias) {

  }

  @Subscribe public void onEvent(Usuario usuario) {
    log("onEvent Usuario");
  }

  public boolean validateEmail(@NonNull String email) {
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validatePassword(@NonNull String password) {
    return password.length() >= 5;
  }

  public void log(String json) {
    if (BuildConfig.DEBUG) Logger.e(json);
  }

  public boolean validate(@Nullable EditText autoCompleteTextView) {
    boolean res = false;
    if (autoCompleteTextView != null
        && autoCompleteTextView.getText() != null
        && !autoCompleteTextView.getText().toString().equalsIgnoreCase("")) {
      //log("act --> " + autoCompleteTextView.getText().toString());
      res = true;
    }
    return res;
  }

  public boolean validateName(@NonNull CharSequence name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }

  public boolean validateName(@NonNull String name) {
    Matcher matcher = patternName.matcher(name);
    return matcher.matches();
  }
}
