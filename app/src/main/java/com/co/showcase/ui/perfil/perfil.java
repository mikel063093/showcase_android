package com.co.showcase.ui.perfil;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class perfil extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_perfil) Toolbar toolbar;
  @Nullable @Bind(R.id.img_perfil) ImageView perfil;
  @Nullable @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Nullable @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Nullable @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Nullable @Bind(R.id.apellidoWrapper) TextInputLayout apellidoWrapper;
  @Nullable @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Nullable @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.perfil);
    ButterKnife.bind(this);
    configToolbarChild(toolbar, R.string.perfil);
    Usuario.getItem()
        .compose(bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::updateUi);
  }

  private void updateUi(@NonNull Usuario usuario) {
    assert edtNombre != null;
    edtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
    assert edtApellido != null;
    edtApellido.setText(usuario.getApellido() != null ? usuario.getApellido() : "");
    assert edtEmail != null;
    edtEmail.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
    assert edtPassword != null;
    edtPassword.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
  }

  @OnClick({ R.id.img_perfil, R.id.btn_guardar }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.img_perfil:
        break;
      case R.id.btn_guardar:
        break;
    }
  }
}
