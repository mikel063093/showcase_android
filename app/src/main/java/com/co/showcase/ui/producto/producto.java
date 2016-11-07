package com.co.showcase.ui.producto;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
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
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import com.co.showcase.ui.home.SlideAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class producto extends BaseActivity {

  @Nullable @Bind(R.id.share_general) ImageView shareGeneral;
  @Nullable @Bind(R.id.img_item) ImageView imgItem;
  @Nullable @Bind(R.id.txt_name_item) AppCompatTextView txtNameItem;
  @Nullable @Bind(R.id.txt_description_item) AppCompatTextView txtDescriptionItem;
  @Nullable @Bind(R.id.txt_price_item) AppCompatTextView txtPriceItem;
  @Nullable @Bind(R.id.txt_units_item) AppCompatTextView txtUnitsItem;
  @Nullable @Bind(R.id.txt_item_count) AppCompatTextView txtItemCount;
  @Bind(R.id.view_pager_home) ViewPager viewPagerHome;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorHome;
  private Articulo articulo;
  @NonNull private DecimalFormat df = new DecimalFormat("###.#");
  private Usuario usuario;
  private ImageView tmp;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.item_reserva);
    ButterKnife.bind(this);
    tmp = new ImageView(this);
    usuario = getUserSync();
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      log(json);
      Articulo articulo = AppMain.getGson().fromJson(json, Articulo.class);
      updateUi(articulo);
      Picasso.with(this)
          .load(articulo.getImagen().get(0))
          .resize(MAX_WIDTH, MAX_HEIGHT)
          .onlyScaleDown()
          .into(tmp, new Callback() {
            @Override public void onSuccess() {
              log("Picasso onSucces");
            }

            @Override public void onError() {
              log("Picasso onErr");
            }
          });
    }
  }

  private void updateUi(@NonNull Articulo articulo) {
    this.articulo = articulo;
    // Picasso.with(this).load(articulo.getImagen().get(0)).fit().into(imgItem);
    assert txtNameItem != null;
    txtNameItem.setText(articulo.getNombre());
    assert txtDescriptionItem != null;
    log(articulo.getDescripcion());
    String html = articulo.getDescripcion().replace("\\r\\n", "<br>").replace("\\n", "<br>");
    String rm = " <p style=\"padding:0; margin:0;\"> ";

    log(html);
    log(Html.toHtml(Html.fromHtml(html)));

    txtDescriptionItem.setText(articulo.getDescripcion());
    assert txtPriceItem != null;
    txtPriceItem.setText("$" + articulo.getPrecio());
    assert txtUnitsItem != null;
    txtUnitsItem.setText(articulo.getUnidades() + " " + articulo.getValorUnidades());
    assert txtItemCount != null;
    txtItemCount.setText("0");
    renderSlideImages(articulo.getImagen());
  }

  private void renderSlideImages(@NonNull List<String> imgs) {
    if (imgs.size() >= 1) {
      log("list logos" + imgs.size());
      SlideAdapter adapter = new SlideAdapter(this, imgs, true);
      assert viewPagerHome != null;
      viewPagerHome.setAdapter(adapter);
      assert indicatorHome != null;
      indicatorHome.setViewPager(viewPagerHome);
    }
  }

  @OnClick({ R.id.btn_less, R.id.btm_more, R.id.btn_reservar, R.id.share_general })
  public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.share_general:
        if (tmp.getDrawable() != null) {
          Bitmap bitmap = ((BitmapDrawable) tmp.getDrawable()).getBitmap();
          String share = getString(R.string.compartir_articulo, articulo.getNombre(),
              articulo.getEstablecimiento() != null ? articulo.getEstablecimiento() : " ");
          share(share, bitmap);
        }
        break;
      case R.id.btn_less:
        if (articulo != null) {
          onAddOrLess(false);
        }
        break;
      case R.id.btm_more:
        onAddOrLess(true);
        break;
      case R.id.btn_reservar:
        assert txtItemCount != null;
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

  private void succesAgregarCarrito(@NonNull ResponseAgregarCarrito responseAgregarCarrito) {
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

    assert txtItemCount != null;
    Double current_payment = Double.parseDouble(txtItemCount.getText().toString());
    Double unidades = articulo.getCantidad();
    log("onAddorLess " + add + " " + current_payment + " " + unidades);
    if (add) {
      if (unidades >= current_payment + 1) {
        current_payment += 1;
        assert txtPriceItem != null;
        txtPriceItem.setText("$" + articulo.getPrecio() * current_payment.intValue());
      } else {
        showErr(getString(R.string.max_item));
      }
    } else {
      if (current_payment > 0) {
        current_payment -= 1;
        String txt = current_payment == 0 ? "$" + articulo.getPrecio()
            : "$" + articulo.getPrecio() * current_payment.intValue();
        assert txtPriceItem != null;
        txtPriceItem.setText(txt);
      } else {
        assert txtPriceItem != null;
        txtPriceItem.setText("$" + articulo.getPrecio());
      }
    }
    txtItemCount.setText(df.format(current_payment.intValue()));
  }
}
