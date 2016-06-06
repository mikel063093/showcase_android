package com.co.showcase.ui.ingresar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseFragment;

public class ingresar extends BaseFragment {

  @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  @SuppressWarnings("unchecked") @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.ingresar, container, false);
    ButterKnife.bind(this, view);
    return view;
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
