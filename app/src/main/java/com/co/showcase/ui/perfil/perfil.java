package com.co.showcase.ui.perfil;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.util.Base64Utils;
import com.co.showcase.ui.util.CircleTransform;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.fuck_boilerplate.rx_paparazzo.entities.Size;
import com.squareup.picasso.Picasso;
import rx.Observable;
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
  private String filePath;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.perfil);
    ButterKnife.bind(this);
    assert toolbar != null;
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
        requestImage();
        break;
      case R.id.btn_guardar:
        guardar();
        break;
    }
  }

  private void guardar() {
    if (filePath != null) {
      Base64Utils.rxFile2B64(filePath)
          .compose(bindToLifecycle())
          // .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          //.doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.error(new Throwable("Custom error")))
          .subscribe(this::guardar, this::errControl);
    } else {
      Log("noFilePath");
    }
  }

  private void guardar(String base64Img) {
    Log("base64Image " + base64Img);
  }

  private void requestImage() {
    RxPaparazzo.takeImage(this).size(Size.Screen).usingGallery().subscribe(response -> {
      if (response.resultCode() != RESULT_OK) {
        response.targetUI().showUserCanceled();
        return;
      }
      response.data();
      response.targetUI().loadImage(response.data());
    });
  }

  private void loadImage(String filePath) {
    this.filePath = filePath;
    Picasso.with(getApplicationContext()).setLoggingEnabled(true);
    Picasso.with(getApplicationContext()).invalidate("file://" + filePath);

    Picasso.with(getApplicationContext())
        .load("file://" + filePath)
        .transform(new CircleTransform())
        .into(perfil);
  }

  private void showUserCanceled() {
    Toast.makeText(this, getString(R.string.user_canceled), Toast.LENGTH_SHORT).show();
  }
}
