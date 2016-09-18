package com.co.showcase.ui.direccion;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;

public class nuevaDireccion extends BaseActivity {

  @Bind(R.id.toolbar_perfil) Toolbar toolbarPerfil;
  @Bind(R.id.edt_direccion) AppCompatEditText edtDireccion;
  @Bind(R.id.addAddresWrapper) TextInputLayout addAddresWrapper;
  @Bind(R.id.edt_numero) AppCompatEditText edtNumero;
  @Bind(R.id.numerWrapper) TextInputLayout numerWrapper;
  @Bind(R.id.edt_numero2) AppCompatEditText edtNumero2;
  @Bind(R.id.numer2Wrapper) TextInputLayout numer2Wrapper;
  @Bind(R.id.edt_numero3) AppCompatEditText edtNumero3;
  @Bind(R.id.numer3Wrapper) TextInputLayout numer3Wrapper;
  @Bind(R.id.edt_info_adicional) AppCompatEditText edtInfoAdicional;
  @Bind(R.id.informacionAdicionalWrapper) TextInputLayout informacionAdicionalWrapper;
  @Bind(R.id.edt_barrio) AppCompatEditText edtBarrio;
  @Bind(R.id.barrioWrapper) TextInputLayout barrioWrapper;
  @Bind(R.id.edt_ciudad) AppCompatEditText edtCiudad;
  @Bind(R.id.ciuadadWrapper) TextInputLayout ciuadadWrapper;
  @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
  @Bind(R.id.btn_nueva_direccion) AppCompatButton btnNuevaDireccion;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nuevadireccion);
    ButterKnife.bind(this);
    configBackToolbar(toolbarPerfil);
  }

  @OnClick(R.id.btn_nueva_direccion) public void onClick() {
  }
}
