package com.co.showcase.ui.perfil;

import android.os.Bundle;
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

  @Bind(R.id.toolbar_perfil) Toolbar toolbar;
  @Bind(R.id.img_perfil) ImageView perfil;
  @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Bind(R.id.apellidoWrapper) TextInputLayout apellidoWrapper;
  @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

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

  private void updateUi(Usuario usuario) {
    edtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
    edtApellido.setText(usuario.getApellido() != null ? usuario.getApellido() : "");
    edtEmail.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
    edtPassword.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
  }

  @OnClick({ R.id.img_perfil, R.id.btn_guardar }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.img_perfil:
        break;
      case R.id.btn_guardar:
        break;
    }
  }
}
