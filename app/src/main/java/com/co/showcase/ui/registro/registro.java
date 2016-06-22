package com.co.showcase.ui.registro;

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

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.registro, container, false);
    ButterKnife.bind(this, view);
    baseActivity = (BaseActivity) getActivity();
    return view;
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
    usuario.save();
  }
}
