package com.co.showcase.ui.historial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.responsePedidos;
import com.co.showcase.ui.BaseActivity;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class pedidos_proceso extends BaseActivity {

  @Nullable @Bind(R.id.toolbar) Toolbar toolbar;
  @Nullable @Bind(R.id.rv_pedidos) RecyclerView rvPedidos;
  private adapterProcesoPedidos adapter;
  private LinearLayoutManager mLinearLayoutManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.pedidos_proceso);
    ButterKnife.bind(this);
    assert toolbar != null;
    configBackToolbar(toolbar);
    getPedidos(getUserSync());
  }

  private void getPedidos(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();

      REST.getRest()
          .pedidosActivos(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesPedidos, this::errControl);
    }
  }

  private void succesPedidos(@NonNull responsePedidos responsePedidos) {
    dismissDialog();
    if (responsePedidos.getEstado() == 1) {
      if (responsePedidos.getPedidos() != null && responsePedidos.getPedidos().size() > 0) {
        updateUi(responsePedidos);
      }
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void updateUi(@NonNull responsePedidos responsePedidos) {
    adapter = new adapterProcesoPedidos(this, responsePedidos.getPedidos());
    mLinearLayoutManager = new LinearLayoutManager(this);
    assert rvPedidos != null;
    rvPedidos.setLayoutManager(mLinearLayoutManager);
    rvPedidos.setAdapter(adapter);
    adapter.getPositionClicks().subscribe(position -> {
      //detalle pedido
      log(AppMain.getGson().toJson(responsePedidos.getPedidos().get(position)));
      Intent intent = new Intent(this, detallePedido.class);
      String json = AppMain.getGson().toJson(responsePedidos.getPedidos().get(position));
      intent.putExtra(detallePedido.class.getSimpleName(), json);
      goActv(intent, false);
    });
  }
}
