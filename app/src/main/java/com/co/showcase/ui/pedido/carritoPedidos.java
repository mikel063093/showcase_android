package com.co.showcase.ui.pedido;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.ResponseVerCarrito;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import java.util.HashMap;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class carritoPedidos extends BaseActivity {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.rv_direcciones) RecyclerView rv;
  @Bind(R.id.txt_subtotal) AppCompatTextView txtSubtotal;
  @Bind(R.id.txt_domicilio) AppCompatTextView txtDomicilio;
  @Bind(R.id.txt_total_final) AppCompatTextView txtTotalFinal;

  private adapterPedidos adapter;
  private LinearLayoutManager mLinearLayoutManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.carrito_de_pedidos);
    ButterKnife.bind(this);
    configBackToolbar(toolbar);
  }

  @Override protected void onResume() {
    super.onResume();
    verCarrito(getUserSync());
  }

  private void verCarrito(Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      REST.getRest()
          .verCarrito(usuario.getToken(), new HashMap<>())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesVerCarrito, this::errControl);
    }
  }

  private void cancelearCarrito(Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      REST.getRest()
          .cancelarCarrito(usuario.getToken(), new HashMap<>())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(responseVerCarrito -> {
            if (responseVerCarrito.getEstado() == 1) {
              finish();
            }
          }, this::errControl);
    }
  }

  private void succesVerCarrito(ResponseVerCarrito responseVerCarrito) {
    if (responseVerCarrito.getEstado() == 1
        && responseVerCarrito.getCarrito() != null
        && responseVerCarrito.getCarrito().getFechaCreacion() != null) {
      updateUi(responseVerCarrito);
    } else {
      showMaterialDialog(getString(R.string.no_tienes_elementos), new onClickCallback() {
        @Override public void onPositive(boolean result) {

        }

        @Override public void onDissmis() {

        }

        @Override public void onNegative(boolean result) {

        }
      });
    }
  }

  private void updateUi(ResponseVerCarrito responseVerCarrito) {
    txtSubtotal.setText(responseVerCarrito.carrito.getSubtotal() + "");
    txtDomicilio.setText(responseVerCarrito.carrito.getDomicilio() + "");
    txtTotalFinal.setText(responseVerCarrito.carrito.getTotal() + "");

    adapter = new adapterPedidos(responseVerCarrito.getCarrito().getItems(), this);
    mLinearLayoutManager = new LinearLayoutManager(this);
    rv.setLayoutManager(mLinearLayoutManager);
    rv.setAdapter(adapter);
    adapter.getPositionClicks().subscribe(position -> {
      //modificar pedido
      log(responseVerCarrito.getCarrito().getItems().get(position).nombre);
    });
  }

  @OnClick({ R.id.btn_siguiente, R.id.txt_cancelar_pedido }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_siguiente:
        goActv(checkout.class, false);
        break;
      case R.id.txt_cancelar_pedido:
        showMaterialDialog(getString(R.string.seguro_cancelar), new onClickCallback() {
          @Override public void onPositive(boolean result) {
            cancelearCarrito(getUserSync());
          }

          @Override public void onDissmis() {

          }

          @Override public void onNegative(boolean result) {

          }
        });
        break;
    }
  }
}
