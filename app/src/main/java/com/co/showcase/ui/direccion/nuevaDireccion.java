package com.co.showcase.ui.direccion;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;

public class nuevaDireccion extends BaseActivity {

  @Bind(R.id.toolbar_perfil) Toolbar toolbarPerfil;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nuevadireccion);
    ButterKnife.bind(this);
    configBackToolbar(toolbarPerfil);
  }

  @OnClick(R.id.btn_nueva_direccion) public void onClick() {
  }
}
