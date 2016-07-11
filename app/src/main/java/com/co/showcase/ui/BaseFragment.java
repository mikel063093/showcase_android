package com.co.showcase.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.co.showcase.BuildConfig;

import com.co.showcase.model.Categoria;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxFragment;

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


  @Override public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe public void onEvent(Object obj) {

  }

  @Subscribe public void onEvent(List<Categoria> categorias) {

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
