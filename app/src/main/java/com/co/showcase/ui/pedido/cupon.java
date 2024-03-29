package com.co.showcase.ui.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.ResponseCupon;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class cupon extends BaseActivity {

  @Nullable @Bind(R.id.edt_cupon) AppCompatEditText edtCupon;
  @Nullable @Bind(R.id.txt_error) AppCompatTextView txtError;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cupon);
    ButterKnife.bind(this);
    enableErr(false);
  }

  private void enableErr(boolean visible) {
    assert txtError != null;
    txtError.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
  }

  private boolean isForResult() {
    boolean result = false;
    if (getIntent() != null
        && getIntent().getExtras() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      result = true;
      log("isForResult");
    }
    return result;
  }

  private void redimir(String codigo, @Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("codigo", codigo);
      REST.getRest()
          .redimirCupon(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesCupon, this::errControl);
    }
  }

  private void succesCupon(@NonNull ResponseCupon responseCupon) {
    dismissDialog();
    if (responseCupon.getEstado() == 1) {
      enableErr(false);
      if (isForResult()) {
        Intent data = new Intent();
        assert edtCupon != null;
        data.putExtra(this.getClass().getSimpleName(), AppMain.getGson().toJson(responseCupon));
        setResult(RESULT_OK, data);
        finish();
      }
    } else {
      enableErr(true);
    }
  }

  @OnClick({ R.id.img_cancelar_cupon, R.id.btn_cupon }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.img_cancelar_cupon:
        finish();
        break;
      case R.id.btn_cupon:
        assert edtCupon != null;
        if (!TextUtils.isEmpty(edtCupon.getText())) {
          redimir(edtCupon.getText().toString(), getUserSync());
        } else {
          showErr(getString(R.string.cupon_invalido));
        }
        break;
    }
  }
}
