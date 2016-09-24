package com.co.showcase.ui.pedido;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;

public class checkout extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Bind(R.id.edt_direccion) AppCompatEditText edtDireccion;
  @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Bind(R.id.edt_telefono) AppCompatEditText edtTelefono;
  @Bind(R.id.edt_pago) AppCompatEditText edtPago;
  @Bind(R.id.edt_cupon) AppCompatEditText edtCupon;
  @Bind(R.id.txt_subtotal) AppCompatTextView txtSubtotal;
  @Bind(R.id.txt_domicilio) AppCompatTextView txtDomicilio;
  @Bind(R.id.txt_total_final) AppCompatTextView txtTotalFinal;
  @Bind(R.id.root) LinearLayout root;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkout);
    ButterKnife.bind(this);
    configBackToolbar(toolbar);
    init(getUserSync());
  }

  private void init(Usuario usuario) {
    edtNombre.setText(usuario.getNombre());
    edtApellido.setText(usuario.getApellido());
  }

  @OnClick({ R.id.btn_siguiente, R.id.txt_cancelar_pedido }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_siguiente:
        break;
      case R.id.txt_cancelar_pedido:
        break;
    }
  }
}
