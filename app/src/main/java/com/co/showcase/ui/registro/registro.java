package com.co.showcase.ui.registro;

import android.os.Bundle;
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
import com.co.showcase.ui.BaseFragment;

/**
 * Created by miguelalegria on 5/6/16 for showcase.
 */

public class registro extends BaseFragment {

  @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Bind(R.id.apellidoWrapper) TextInputLayout apellidoWrapper;
  @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.registro, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @OnClick({ R.id.btn_ingresar, R.id.txt_no_cuenta }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_ingresar:
        break;
      case R.id.txt_no_cuenta:
        break;
    }
  }
}
