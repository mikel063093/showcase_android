package com.co.showcase.ui.direccion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Direccion;
import com.co.showcase.model.ResponseDirecciones;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class direcciones extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_perfil) Toolbar toolbarPerfil;
  @Nullable @Bind(R.id.rv_direcciones) RecyclerView rvDirecciones;
  private adapterDireccion adapter;
  private LinearLayoutManager mLinearLayoutManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.direcciones);
    ButterKnife.bind(this);
    configBackToolbar(toolbarPerfil);
    isForResult();
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

  private void getDirecciones(@NonNull Usuario usuario) {
    Map<String, Object> param = new HashMap<>();
    param.put("id", usuario.getId());

    REST.getRest()
        .direcciones(usuario.getToken(), param)
        .compose(bindToLifecycle())
        .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
        .subscribeOn(Schedulers.io())
        .doOnCompleted(this::dismissDialog)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::succesDirecciones, this::errControl);
  }

  @Override protected void onResume() {
    super.onResume();
    getDirecciones(getUserSync());
  }

  private void succesDirecciones(@NonNull ResponseDirecciones responseDirecciones) {
    dismissDialog();
    if (responseDirecciones.getEstado() == 1 && responseDirecciones.direcciones.size() >= 1) {
      log("size " + responseDirecciones.getDirecciones().size());
      adapter = new adapterDireccion(this, responseDirecciones.getDirecciones());

      mLinearLayoutManager = new LinearLayoutManager(this);
      rvDirecciones.setLayoutManager(mLinearLayoutManager);

      //rvDirecciones.addItemDecoration(new DividerItemDecoration(this),
      //    R.drawable.item_decoratio_address);

      rvDirecciones.setAdapter(adapter);

      adapter.getPositionClicks().compose(bindToLifecycle()).subscribe(position -> {
        log("click position ->" + position);
        if (isForResult()) {
          Intent data = new Intent();
          Direccion item = responseDirecciones.getDirecciones().get(position);

          data.putExtra(direcciones.class.getSimpleName(), AppMain.getGson().toJson(item));
          setResult(RESULT_OK, data);
          finish();
        }
      });
    }
  }

  @OnClick(R.id.btn_guardar_direccion) public void onClick() {
    goActv(nuevaDireccion.class, false);
  }
}
