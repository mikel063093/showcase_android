package com.co.showcase.ui.establecimiento;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.ResponsePuntuacion;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import com.co.showcase.ui.home.SlideAdapter;
import com.co.showcase.ui.util.ItemDecorationAlbumColumns;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class establecimiento extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.indicator_home) CirclePageIndicator indicatorHome;
  @Nullable @Bind(R.id.view_pager_home) ViewPager viewPagerHome;
  @Nullable @Bind(R.id.txt_name_company) AppCompatTextView txtNameCompany;
  @Nullable @Bind(R.id.txt_description) AppCompatTextView txtDescription;
  @Nullable @Bind(R.id.txt_addres) AppCompatTextView txtAddres;
  @Nullable @Bind(R.id.txt_phone) AppCompatTextView txtPhone;
  @Nullable @Bind(R.id.txt_celphone) AppCompatTextView txtCelphone;
  @Nullable @Bind(R.id.txt_email) AppCompatTextView txtEmail;
  @Nullable @Bind(R.id.txt_website) AppCompatTextView txtWebsite;
  @Nullable @Bind(R.id.btn_sahre_fb) ImageView btnSahreFb;
  @Nullable @Bind(R.id.btn_sahre_tw) ImageView btnSahreTw;
  @Nullable @Bind(R.id.ratingBar) MaterialRatingBar ratingBar;
  @Nullable @Bind(R.id.rv_home) RecyclerView rvHome;

  @Nullable @Bind(R.id.share_general) ImageView shareGeneral;
  @Bind(R.id.btn_snap) ImageView btnSnap;
  @Bind(R.id.btn_instagram) ImageView btnInstagram;
  @Bind(R.id.btn_youtube) ImageView btnYoutube;

  @Bind(R.id.ico_direccion) ImageView icoDireccion;
  @Bind(R.id.ico_telefono) ImageView icoTelefono;
  @Bind(R.id.ico_whatsapp) ImageView icoWhatsapp;
  @Bind(R.id.ico_correo_establecimiento) ImageView icoCorreoEstablecimiento;
  @Bind(R.id.ico_sitioweb) ImageView icoSitioweb;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.establecimiento);
    ButterKnife.bind(this);
    assert toolbar != null;
    configBackToolbar(toolbar);
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      Establecimiento establecimiento = AppMain.getGson().fromJson(json, Establecimiento.class);
      if (establecimiento != null) {
        updateUI(establecimiento);
      }
    }
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

  private void updateUI(@NonNull Establecimiento establecimiento) {
    assert txtNameCompany != null;
    txtNameCompany.setText(establecimiento.getNombre());
    assert txtDescription != null;
    txtDescription.setText(Html.fromHtml(establecimiento.getDescripcion()));
    assert txtAddres != null;
    if (establecimiento.getDireccion() != null && establecimiento.getDireccion().length() > 0) {
      txtAddres.setText(establecimiento.getDireccion());
    } else {
      icoDireccion.setVisibility(View.GONE);
      txtAddres.setVisibility(View.GONE);
    }
    assert txtCelphone != null;
    if (establecimiento.getCelular() != null && establecimiento.getCelular().length() > 0) {
      txtCelphone.setText(establecimiento.getCelular() != null ? establecimiento.getCelular() : "");
    } else {
      icoWhatsapp.setVisibility(View.GONE);
      txtCelphone.setVisibility(View.GONE);
    }
    assert txtPhone != null;
    if (establecimiento.getTelefono() != null && establecimiento.getTelefono().length() > 0) {
      txtPhone.setText(establecimiento.getTelefono());
    } else {
      icoTelefono.setVisibility(View.GONE);
      txtPhone.setVisibility(View.GONE);
    }
    assert txtEmail != null;
    if (establecimiento.getCorreo() != null && establecimiento.getCorreo().length() > 0) {
      txtEmail.setText(establecimiento.getCorreo() != null ? establecimiento.getCorreo() : "");
    } else {
      icoCorreoEstablecimiento.setVisibility(View.GONE);
      txtEmail.setVisibility(View.GONE);
    }
    assert txtWebsite != null;
    if (establecimiento.getSitioWeb() != null && establecimiento.getSitioWeb().length() > 0) {
      txtWebsite.setText(establecimiento.getSitioWeb());
    } else {
      icoSitioweb.setVisibility(View.GONE);
      txtWebsite.setVisibility(View.GONE);
    }
    assert ratingBar != null;
    ratingBar.setRating(Float.parseFloat(establecimiento.getPuntuacion() + ""));
    assert btnSahreFb != null;
    btnSahreFb.setVisibility(establecimiento.getFacebook() != null ? View.VISIBLE : View.GONE);
    btnSahreFb.setOnClickListener(view -> openUrl(
        establecimiento.getFacebook() != null ? establecimiento.getFacebook() : ""));
    assert btnSahreTw != null;
    btnSahreTw.setVisibility(establecimiento.getTwitter() != null ? View.VISIBLE : View.GONE);
    btnSahreTw.setOnClickListener(
        view -> openUrl(establecimiento.getTwitter() != null ? establecimiento.getTwitter() : ""));

    btnSnap.setVisibility(establecimiento.getSnapchat() != null ? View.VISIBLE : View.GONE);
    btnSnap.setOnClickListener(view -> openUrl(
        establecimiento.getSnapchat() != null ? establecimiento.getSnapchat() : ""));

    btnInstagram.setVisibility(establecimiento.getInstagram() != null ? View.VISIBLE : View.GONE);
    btnSnap.setOnClickListener(view -> openUrl(
        establecimiento.getInstagram() != null ? establecimiento.getInstagram() : ""));

    btnYoutube.setVisibility(establecimiento.getYoutube() != null ? View.VISIBLE : View.GONE);
    btnYoutube.setOnClickListener(
        view -> openUrl(establecimiento.getYoutube() != null ? establecimiento.getYoutube() : ""));

    renderSlideImages(establecimiento.getUrlImagen());

    String share = getString(R.string.compartir_establecimiento, establecimiento.getNombre(),
        getString(R.string.url) + "establecimiento/" + establecimiento.getId());
    assert shareGeneral != null;
    ImageView tmp = new ImageView(this);
    Picasso.with(this)
        .load(establecimiento.getUrlImagen().get(0))
        .resize(MAX_WIDTH, MAX_HEIGHT)
        .onlyScaleDown()
        .into(tmp, new Callback() {
          @Override public void onSuccess() {
            log("Picasso onSucces");
            Bitmap bitmap = ((BitmapDrawable) tmp.getDrawable()).getBitmap();
            if (shareGeneral != null) {
              shareGeneral.setOnClickListener(view -> share(share, bitmap,
                  getString(R.string.url) + "establecimiento/" + (establecimiento.getSlug() != null
                      ? establecimiento.getSlug() : "")));
            }
          }

          @Override public void onError() {
            log("Picasso onErr");
          }
        });

    establecimientoItemsAdapter adapter =
        new establecimientoItemsAdapter(this, establecimiento.getArticulos());
    GridLayoutManager glm = new GridLayoutManager(this, 2);
    assert rvHome != null;
    rvHome.setNestedScrollingEnabled(false);
    rvHome.setLayoutManager(glm);
    rvHome.addItemDecoration(
        new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen._6sdp),
            getResources().getInteger(R.integer.photo_list_preview_columns)));
    rvHome.setAdapter(adapter);

    ratingBar.setOnRatingBarChangeListener(
        (ratingBar1, v, b) -> onRatingChange(v, establecimiento));
  }

  private void onRatingChange(float value, @NonNull Establecimiento establecimiento) {
    Usuario usuario = getUserSync();
    if (usuario.getToken() != null && usuario.getToken().length() > 2) {
      Map<String, Object> param = new HashMap<>();
      param.put("valor", value);
      param.put("id", establecimiento.getId());
      REST.getRest()
          .puntuarEstablecimiento(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onSuccesPuntuacion, this::errControl);
    }
  }

  private void onSuccesPuntuacion(@NonNull ResponsePuntuacion responsePuntuacion) {
    dismissDialog();
    if (responsePuntuacion.getEstado() == 1) {
      showMaterialDialog(getString(R.string.puntuacion_ok), new onClickCallback() {
        @Override public void onPositive(boolean result) {

        }

        @Override public void onDissmis() {

        }

        @Override public void onNegative(boolean result) {

        }
      });
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  @OnClick({
      R.id.btn_sahre_fb, R.id.btn_sahre_tw, R.id.share_general, R.id.btn_snap, R.id.btn_instagram,
      R.id.btn_youtube
  }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_sahre_fb:
        break;
      case R.id.btn_sahre_tw:
        break;
      case R.id.share_general:

        break;
      case R.id.btn_snap:
        break;
      case R.id.btn_instagram:
        break;
      case R.id.btn_youtube:
        break;
    }
  }
}
