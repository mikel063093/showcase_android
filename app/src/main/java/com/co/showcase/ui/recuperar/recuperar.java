package com.co.showcase.ui.recuperar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;

import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class recuperar extends BaseActivity {

  @Nullable @Bind(R.id.toolbar) Toolbar toolbar;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recupear_pass);
    ButterKnife.bind(this);
    renderToolbar();
  }

  private void renderToolbar() {
    final Drawable upArrow = getResources().getDrawable(R.drawable.btn_flechaizquierda);
    assert toolbar != null;
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  @OnClick(R.id.btn_ingresar) public void onClick() {
    assert edtEmail != null;
    if (validateEmail(edtEmail.getText().toString())) {
      assert emailWrapper != null;
      emailWrapper.setError(null);
      Map<String, String> param = new HashMap<>();
      param.put("correo", edtEmail.getText().toString());
      REST.getRest()
          .recuperar(param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onSuccesRecuperar, this::errControl);
    } else {
      assert emailWrapper != null;
      emailWrapper.setError(getString(R.string.err_email));
    }
  }

  private void onSuccesRecuperar(@NonNull Usuario usuario) {
    if (usuario.getEstado().equalsIgnoreCase("exito")) {
      showMaterialDialog(getString(R.string.check_email), new onClickCallback() {
        @Override public void onPositive(boolean result) {

        }

        @Override public void onDissmis() {

        }

        @Override public void onNegative(boolean result) {

        }
      });
    } else {

      showErr(
          usuario.getMensaje() != null ? usuario.getMensaje() : getString(R.string.general_err));
    }
  }
}
