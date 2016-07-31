package com.co.showcase.ui.ingresar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.BaseFragment;
import com.co.showcase.ui.home.home;
import com.co.showcase.ui.recuperar.recuperar;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.HashMap;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

public class ingresar extends BaseFragment {

  @Nullable @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Nullable @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  private BaseActivity baseActivity;
  private Observable<CharSequence> emailChangeObservable;
  private Observable<CharSequence> passwordChangeObservable;

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ingresar, container, false);
    ButterKnife.bind(this, view);
    baseActivity = (BaseActivity) getActivity();
    emailChangeObservable = RxTextView.textChanges(edtEmail).skip(1);
    passwordChangeObservable = RxTextView.textChanges(edtPassword).skip(1);
    rxValidationLogin();
    return view;
  }

  private void rxValidationLogin() {
    Observable.combineLatest(emailChangeObservable, passwordChangeObservable,
        new Func2<CharSequence, CharSequence, Boolean>() {
          @NonNull @Override
          public Boolean call(@NonNull CharSequence newEmail, CharSequence newPassword) {
            boolean emailValid = !isEmpty(newEmail) && baseActivity.validateEmail(newEmail);

            if (!emailValid) {
              emailWrapper.setErrorEnabled(true);
              emailWrapper.setError(getString(R.string.err_email));
            } else {
              emailWrapper.setError(null);
              emailWrapper.setErrorEnabled(false);
              //baseActivity.Log("email ok");
            }
            boolean passValid = !isEmpty(newPassword);
            if (!passValid) {
              passwordWrapper.setErrorEnabled(true);
              passwordWrapper.setError(getString(R.string.err_pass));
            } else {
              passwordWrapper.setError(null);
              passwordWrapper.setErrorEnabled(false);
              //baseActivity.Log("passok ok");
            }

            return emailValid && passValid;
          }
        }).compose(bindToLifecycle()).subscribe(aBoolean -> {
      if (aBoolean) {
        // baseActivity.Log("validacion email && pass ok");
      }
    });
  }

  @OnClick({ R.id.btn_ingresar, R.id.txt_no_cuenta, R.id.txt_recover })
  public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_ingresar:
        validar();
        break;
      case R.id.txt_no_cuenta:
        EventBus.getDefault().post(new TabPosition(1));
        break;
      case R.id.txt_recover:
        baseActivity.goActv(recuperar.class, false);
        break;
    }
  }

  private void validar() {
    assert edtEmail != null;
    if (edtEmail.getText().toString().length() > 0 && validateEmail(
        edtEmail.getText().toString())) {
      assert edtPassword != null;
      if (edtPassword.getText().length() > 0) {
        Map<String, Object> param = new HashMap<>();
        param.put("login", edtEmail.getText().toString());
        param.put("pass", edtPassword.getText().toString());
        REST.getRest()
            .validar(param)
            .compose(bindToLifecycle())
            .doOnSubscribe(() -> baseActivity.showDialog(getString(R.string.loading)))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(() -> baseActivity.dismissDialog())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccesValidar, throwable -> baseActivity.errControl(throwable));
      } else {
        baseActivity.showMaterialDialog(getString(R.string.err_pass),
            new BaseActivity.onClickCallback() {
              @Override public void onPositive(boolean result) {

              }

              @Override public void onDissmis() {

              }

              @Override public void onNegative(boolean result) {

              }
            });
      }
    } else {
      baseActivity.showMaterialDialog(getString(R.string.err_email),
          new BaseActivity.onClickCallback() {
            @Override public void onPositive(boolean result) {
            }

            @Override public void onDissmis() {
            }

            @Override public void onNegative(boolean result) {

            }
          });
    }
  }

  private void onSuccesValidar(@NonNull Usuario usuario) {
    if (usuario.getEstado().equalsIgnoreCase("exito")) {
      usuario.save()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .compose(this.bindToLifecycle())
          .subscribe();
      Intent intent = new Intent(getContext(), home.class);
      baseActivity.goActv(intent, true);
    } else {
      baseActivity.showErr(usuario.getMensaje());
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
