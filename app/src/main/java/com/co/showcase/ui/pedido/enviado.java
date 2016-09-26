package com.co.showcase.ui.pedido;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.home.home;

public class enviado extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.envio_enviado);
    ButterKnife.bind(this);
    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> goActv(home.class, true));
  }

  @OnClick(R.id.btn_siguiente) public void onClick() {
    goActv(home.class, true);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    goActv(home.class, true);
  }
}
