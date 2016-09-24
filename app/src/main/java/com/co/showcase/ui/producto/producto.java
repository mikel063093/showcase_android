package com.co.showcase.ui.producto;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Articulo;
import com.co.showcase.model.ResponseAgregarCarrito;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class producto extends BaseActivity {

  @Bind(R.id.share_general) ImageView shareGeneral;
  @Bind(R.id.img_item) ImageView imgItem;
  @Bind(R.id.txt_name_item) AppCompatTextView txtNameItem;
  @Bind(R.id.txt_description_item) AppCompatTextView txtDescriptionItem;
  @Bind(R.id.txt_price_item) AppCompatTextView txtPriceItem;
  @Bind(R.id.txt_units_item) AppCompatTextView txtUnitsItem;
  @Bind(R.id.txt_item_count) AppCompatTextView txtItemCount;
  private Articulo articulo;
  private DecimalFormat df = new DecimalFormat("###.#");
  private Usuario usuario;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.item_reserva);
    ButterKnife.bind(this);
    usuario = getUserSync();
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      Articulo articulo = AppMain.getGson().fromJson(json, Articulo.class);
      updateUi(articulo);
    }
  }

  private void updateUi(Articulo articulo) {
    this.articulo = articulo;
    Picasso.with(this).load(articulo.getImagen()).fit().into(imgItem);
    txtNameItem.setText(articulo.getNombre());
    txtDescriptionItem.setText(articulo.getDescripcion());
    txtPriceItem.setText("$" + articulo.getPrecio());
    txtUnitsItem.setText(articulo.getUnidades() + " " + articulo.getValorUnidades());
    txtItemCount.setText("0");
  }

  @OnClick({ R.id.btn_less, R.id.btm_more, R.id.btn_reservar }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_less:
        if (articulo != null) {
          onAddOrLess(false);
        }
        break;
      case R.id.btm_more:
        onAddOrLess(true);
        break;
      case R.id.btn_reservar:
        Double current_payment = Double.parseDouble(txtItemCount.getText().toString());

        if (current_payment >= 1 && usuario != null && usuario.getToken() != null) {
          Map<String, Object> param = new HashMap<>();
          param.put("idArticulo", articulo.getId());
          param.put("cantidad", current_payment.intValue());

          REST.getRest()
              .agregarProductoCarrito(usuario.getToken(), param)
              .compose(bindToLifecycle())
              .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
              .subscribeOn(Schedulers.io())
              .doOnCompleted(this::dismissDialog)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::succesAgregarCarrito, this::errControl);
        } else {
          showErr(getString(R.string.item_count_err));
        }
        break;
    }
  }

  private void succesAgregarCarrito(ResponseAgregarCarrito responseAgregarCarrito) {
    dismissDialog();
    if (responseAgregarCarrito.getEstado() == 1) {
      showMaterialDialog(getString(R.string.reserva_ok, articulo.getNombre()),
          new onClickCallback() {
            @Override public void onPositive(boolean result) {
              finish();
            }

            @Override public void onDissmis() {
              finish();
            }

            @Override public void onNegative(boolean result) {
              finish();
            }
          });
    } else {
      showErr(getString(R.string.general_err));
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
