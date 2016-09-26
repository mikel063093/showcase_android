package com.co.showcase.ui.pedido;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Articulo;
import com.co.showcase.model.Carrito;
import com.co.showcase.model.ResponseDetalleArticulo;
import com.co.showcase.model.ResponseVerCarrito;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class edtiarItem extends BaseActivity {

  @Bind(R.id.img_item) ImageView imgItem;
  @Bind(R.id.txt_name_item) AppCompatTextView txtNameItem;
  @Bind(R.id.txt_price_item) AppCompatTextView txtPriceItem;
  @Bind(R.id.txt_units_item) AppCompatTextView txtUnitsItem;
  @Bind(R.id.txt_item_count) AppCompatTextView txtItemCount;
  private Articulo articulo;
  private DecimalFormat df = new DecimalFormat("###.#");

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.item_reserva_editar);
    ButterKnife.bind(this);
    getArticulo(getItem(), getUserSync());
  }

  private void getArticulo(Carrito.ItemsBean item, Usuario usuario) {
    if (item != null && usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", item.getIdArticulo());
      REST.getRest()
          .detalleProducto(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesDetalleProducto, this::errControl);
    }
  }

  private void eliminarItem(Carrito.ItemsBean item, Usuario usuario) {
    if (item != null && usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("idItem", item.getId());
      REST.getRest()
          .eliminarProductoCarrito(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesEliminarItem, this::errControl);
    }
  }

  private void editarItem(Carrito.ItemsBean item, Usuario usuario) {
    if (item != null && usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      Double current_payment = Double.parseDouble(txtItemCount.getText().toString());
      param.put("idItem", item.getId());
      param.put("cantidad", current_payment.intValue());
      REST.getRest()
          .editarProductoCarrito(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesEditarItem, this::errControl);
    }
  }

  private void succesEditarItem(ResponseVerCarrito responseVerCarrito) {
    dismissDialog();
    if (responseVerCarrito.getEstado() == 1) {

    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void succesEliminarItem(ResponseVerCarrito responseVerCarrito) {
    dismissDialog();
    if (responseVerCarrito.getEstado() == 1) {
      finish();
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void succesDetalleProducto(ResponseDetalleArticulo responseDetalleArticulo) {
    dismissDialog();
    if (responseDetalleArticulo.getEstado() == 1) {
      articulo = responseDetalleArticulo.getArticulo();
      updateUi(getItem());
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private Carrito.ItemsBean getItem() {
    Carrito.ItemsBean item = null;
    String json;
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      json = getIntent().getStringExtra(this.getClass().getSimpleName());
      item = AppMain.getGson().fromJson(json, Carrito.ItemsBean.class);
      //updateUi(item);
    }
    return item;
  }

  private void updateUi(Carrito.ItemsBean item) {
    if (item != null && articulo != null) {
      Picasso.with(this).load(articulo.getImagen()).fit().into(imgItem);
      txtNameItem.setText(articulo.getNombre());
      txtPriceItem.setText("$" + (articulo.getPrecio() * item.getCantidad()));
      String unitItem =
          articulo != null ? (articulo.getUnidades() + " " + articulo.getValorUnidades()) : "";
      txtUnitsItem.setText(unitItem);
      txtItemCount.setText(item.getCantidad() + "");
    }
  }

  @OnClick({ R.id.btn_less, R.id.btm_more, R.id.btn_reservar, R.id.img_elimiar })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_less:
        if (articulo != null) {
          onAddOrLess(false);
        }
        break;
      case R.id.btm_more:
        if (articulo != null) onAddOrLess(true);
        break;
      case R.id.btn_reservar:
        editarItem(getItem(), getUserSync());
        break;
      case R.id.img_elimiar:
        showMaterialDialog(getString(R.string.seguro_eliminar_producto), new onClickCallback() {
          @Override public void onPositive(boolean result) {
            eliminarItem(getItem(), getUserSync());
          }

          @Override public void onDissmis() {

          }

          @Override public void onNegative(boolean result) {

          }
        });
        break;
    }
  }

  private void onAddOrLess(boolean add) {

    Double current_payment = Double.parseDouble(txtItemCount.getText().toString());
    Double unidades = articulo.getCantidad();
    log("onAddorLess " + add + " " + current_payment + " " + unidades);
    if (add) {
      if (unidades >= current_payment + 1) {
        current_payment += 1;
        txtPriceItem.setText("$" + articulo.getPrecio() * current_payment.intValue());
      } else {
        showErr(getString(R.string.max_item));
      }
    } else {
      if (current_payment > 0) {
        current_payment -= 1;
        String txt = current_payment == 0 ? "$" + articulo.getPrecio()
            : "$" + articulo.getPrecio() * current_payment.intValue();
        txtPriceItem.setText(txt);
      } else {
        txtPriceItem.setText("$" + articulo.getPrecio());
      }
    }
    txtItemCount.setText(df.format(current_payment.intValue()));
  }
}
