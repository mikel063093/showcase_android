package com.co.showcase.ui.ingresar;

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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.TabPosition;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class ingresar extends BaseFragment {

  @Nullable @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Nullable @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  private BaseActivity baseActivity;

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ingresar, container, false);
    ButterKnife.bind(this, view);
    baseActivity = (BaseActivity) getActivity();
    return view;
  }

  @OnClick({ R.id.btn_ingresar, R.id.txt_no_cuenta }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_ingresar:
        validar();
        break;
      case R.id.txt_no_cuenta:
        EventBus.getDefault().post(new TabPosition(1));
        break;
    }
  }

  private void validar() {
    assert edtEmail != null;
    if (edtEmail.getText().toString().length() > 0 && validateEmail(
        edtEmail.getText().toString())) {
      assert edtPassword != null;
      if (edtPassword.getText().length() > 0) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(edtEmail.getText().toString());
        usuario.setClave(edtPassword.getText().toString());
        REST.getRest()
            .validar(usuario.jsonToMap())
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
            @Override public void onPositive(boolean result) {}

            @Override public void onDissmis() {
            }

            @Override public void onNegative(boolean result) {

            }
          });
    }
  }

  private void onSuccesValidar(@NonNull Usuario usuario) {
    if (usuario.getEstado().equalsIgnoreCase("exito")) {
      usuario.save();
    } else {
      baseActivity.showErr(usuario.getMensaje());
    }
  }
}
