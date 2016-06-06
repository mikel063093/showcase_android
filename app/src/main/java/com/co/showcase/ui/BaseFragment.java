package com.co.showcase.ui;

import android.support.v4.app.Fragment;
import com.co.showcase.BuildConfig;
import com.orhanobut.logger.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by miguelalegria on 5/6/16 for showcase.
 */

public class BaseFragment extends Fragment {
  private static final String EMAIL_PATTERN =
      "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
  private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private Matcher matcher;

  public boolean validateEmail(String email) {
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

  public boolean validatePassword(String password) {
    return password.length() > 5;
  }

  public void log(String json) {
    if (BuildConfig.DEBUG) Logger.e(json);
  }
}
