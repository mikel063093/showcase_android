package com.co.showcase.ui.registro;

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
import com.co.showcase.ui.perfil.perfil;
import com.jakewharton.rxbinding.widget.RxTextView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import static android.text.TextUtils.isEmpty;

/**
 * Created by miguelalegria on 5/6/16 for showcase.
 */

public class registro extends BaseFragment {

  @Nullable @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Nullable @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Nullable @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Nullable @Bind(R.id.apellidoWrapper) TextInputLayout apellidoWrapper;
  @Nullable @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Nullable @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  private BaseActivity baseActivity;

  private Observable<CharSequence> emailChangeObservable;
  private Observable<CharSequence> passwordChangeObservable;
  private Observable<CharSequence> firstNameChangeObservable;
  private Observable<CharSequence> lastNameChangeObservable;

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.registro, container, false);
    ButterKnife.bind(this, view);
    baseActivity = (BaseActivity) getActivity();
    emailChangeObservable = RxTextView.textChanges(edtEmail).skip(1);
    passwordChangeObservable = RxTextView.textChanges(edtPassword).skip(1);
    firstNameChangeObservable = RxTextView.textChanges(edtNombre).skip(1);
    lastNameChangeObservable = RxTextView.textChanges(edtApellido).skip(1);
    rxValidationRegister();
    return view;
  }

  private void rxValidationRegister() {
    Observable.combineLatest(emailChangeObservable, passwordChangeObservable,
        firstNameChangeObservable, lastNameChangeObservable, (email, pass, first, last) -> {
          boolean emailValid = !isEmpty(email) && baseActivity.validateEmail(email);

          if (!emailValid) {
            assert emailWrapper != null;
            emailWrapper.setErrorEnabled(true);
            emailWrapper.setError(getString(R.string.err_email));
          } else {
            assert emailWrapper != null;
            emailWrapper.setError(null);
            emailWrapper.setErrorEnabled(false);
            //baseActivity.Log("email ok");
          }
          boolean passValid = !isEmpty(pass);
          if (!passValid) {
            assert passwordWrapper != null;
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError(getString(R.string.err_pass));
          } else {
            assert passwordWrapper != null;
            passwordWrapper.setError(null);
            passwordWrapper.setErrorEnabled(false);
            //baseActivity.Log("passok ok");
          }
          boolean firstValid = !isEmpty(first) && validateName(first);
          if (!firstValid) {
            assert nombreWrapper != null;
            nombreWrapper.setErrorEnabled(true);
            nombreWrapper.setError(getString(R.string.name_err));
          } else {
            assert nombreWrapper != null;
            nombreWrapper.setError(null);
            nombreWrapper.setErrorEnabled(false);
          }

          boolean lastValid = !isEmpty(last) && validateName(last);
          if (!lastValid) {
            assert apellidoWrapper != null;
            apellidoWrapper.setErrorEnabled(true);
            apellidoWrapper.setError(getString(R.string.lastName_err));
          } else {
            assert apellidoWrapper != null;
            apellidoWrapper.setError(null);
            apellidoWrapper.setErrorEnabled(false);
          }

          return emailValid && passValid && firstValid && lastValid;
        }).subscribe();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({ R.id.btn_ingresar, R.id.txt_no_cuenta }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_ingresar:
        validar();
        break;
      case R.id.txt_no_cuenta:
        EventBus.getDefault().post(new TabPosition(0));
        break;
    }
  }

  private void validar() {
    if (validate(edtNombre) && validate(edtApellido) && validate(edtPassword) && validate(
        edtEmail)) {
      Usuario usuario = new Usuario();
      assert edtPassword != null;
      usuario.setClave(edtPassword.getText().toString());
      assert edtEmail != null;
      usuario.setCorreo(edtEmail.getText().toString());
      assert edtNombre != null;
      usuario.setNombre(edtNombre.getText().toString());
      assert edtApellido != null;
      usuario.setApellido(edtApellido.getText().toString());

      REST.getRest()
          .registrar(usuario.jsonToMap())
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> baseActivity.showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(() -> baseActivity.dismissDialog())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onSuccesRegistro, throwable -> baseActivity.errControl(throwable));
    }
  }

  private void onSuccesRegistro(@NonNull Usuario usuario) {
    if (usuario.getEstado().equalsIgnoreCase("exito")) {
      usuario.save();
      Intent intent = new Intent(getContext(), perfil.class);
      baseActivity.goActv(intent, true);
    } else {
      baseActivity.showErr(usuario.getMensaje());
    }
  }
}
