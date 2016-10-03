package com.co.showcase.ui.historial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Pedido;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.detallePedidos;
import com.co.showcase.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class detallePedido extends BaseActivity {

  @Nullable @Bind(R.id.toolbar) Toolbar toolbar;
  @Nullable @Bind(R.id.appCompatTextView) AppCompatTextView appCompatTextView;
  @Nullable @Bind(R.id.txt_estado) AppCompatTextView txtEstado;
  @Nullable @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Nullable @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Nullable @Bind(R.id.edt_direccion) AppCompatEditText edtDireccion;
  @Nullable @Bind(R.id.edt_telefono) AppCompatEditText edtTelefono;
  @Nullable @Bind(R.id.edt_forma_pago) AppCompatEditText edtFormaPago;

  @Nullable @Bind(R.id.rv_items) RecyclerView rvItems;
  @Nullable @Bind(R.id.txt_subtotal) AppCompatTextView txtSubtotal;
  @Nullable @Bind(R.id.txt_domicilio) AppCompatTextView txtDomicilio;
  @Nullable @Bind(R.id.txt_total_final) AppCompatTextView txtTotalFinal;

  @Nullable @Bind(R.id.edt_cupon) AppCompatEditText edtCupon;
  private adapterPedidos adapter;
  private LinearLayoutManager mLinearLayoutManager;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.detalle_pedido);
    ButterKnife.bind(this);
    configBackToolbar(toolbar);
    getPedidos(getIdPedido(), getUserSync());
  }

  private void getPedidos(@Nullable Pedido pedido, @Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null && pedido != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", pedido.getId());
      REST.getRest()
          .detallePedido(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesDetallePedido, this::errControl);
    }
  }

  private void succesDetallePedido(@NonNull detallePedidos response) {
    dismissDialog();
    if (response.getEstado() == 1) {
      updadeUi(response);
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void updadeUi(@NonNull detallePedidos response) {
    detallePedidos.PedidoBean pedido = response.getPedido();
    String estado = "<font color=#4a4a4a>" + getString(R.string.estado_color) + "</font> " +
        "<font color=#f5a623>" + pedido.getEstado() + "</font> ";
    txtEstado.setText(Html.fromHtml(estado));
    appCompatTextView.setText(getString(R.string.datos_pedido, pedido.getId() + ""));
    edtNombre.setText(pedido.getNombres());
    edtApellido.setText(pedido.getApellidos());
    edtDireccion.setText(pedido.getDireccion());
    edtTelefono.setText(pedido.getTelefono());
    edtFormaPago.setText(pedido.getMetodoPago());
    edtCupon.setText(pedido.getCupon());

    adapter = new adapterPedidos(response.getPedido().getItems(), this);
    mLinearLayoutManager = new LinearLayoutManager(this);
    rvItems.setLayoutManager(mLinearLayoutManager);
    rvItems.setAdapter(adapter);

    txtSubtotal.setText(pedido.getSubtotal()+"");
    txtDomicilio.setText(pedido.getDomicilio() + "");
    txtTotalFinal.setText(pedido.getTotal()+"");
  }

  @Nullable private Pedido getIdPedido() {
    Pedido pedido = null;
    String clssName = this.getClass().getSimpleName();
    if (getIntent() != null
        && getIntent().getExtras() != null
        && getIntent().getStringExtra(clssName) != null) {
      String json = getIntent().getStringExtra(clssName);
      pedido = AppMain.getGson().fromJson(json, Pedido.class);
    }
    return pedido;
  }
}
