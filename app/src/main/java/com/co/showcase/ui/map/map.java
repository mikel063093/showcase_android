package com.co.showcase.ui.map;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.geoJson.feature;
import com.co.showcase.model.zonaDetalle;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.home.HomeSection;
import com.co.showcase.ui.home.home;
import com.co.showcase.ui.util.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.sdoward.rxgooglemap.MapObservableProvider;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class map extends BaseActivity {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.rv_home) RecyclerView rvHome;
  @Bind(R.id.imgmaptransparent) ImageView imgmaptransparent;
  @Bind(R.id.scroll) NestedScrollView scroll;

  private CompositeSubscription subscriptions = Subscriptions.from();
  private GeoJsonLayer geoJsonLayer;
  private MapObservableProvider mapObservableProvider;
  private SectionedRecyclerViewAdapter sectionAdapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_map);
    ButterKnife.bind(this);
    imgmaptransparent.setOnTouchListener((view, event) -> {
      int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:

          scroll.requestDisallowInterceptTouchEvent(true);
          return false;

        case MotionEvent.ACTION_UP:
          scroll.requestDisallowInterceptTouchEvent(false);
          return true;

        case MotionEvent.ACTION_MOVE:
          scroll.requestDisallowInterceptTouchEvent(true);
          return false;
        default:
          return true;
      }
    });
    initMap();
    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> {
      goActv(home.class, true);
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  private String getZona() {
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      return getIntent().getStringExtra(this.getClass().getSimpleName());
    }
    return "1";
  }

  private void initMap() {

    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

    mapObservableProvider = new MapObservableProvider(mapFragment);
    subscriptions.add(mapObservableProvider.getMapReadyObservable()
        .compose(bindToLifecycle())
        .subscribe(googleMap -> {
          log("onMapReady");
          getGeoJson(getUserSync(), getZona());
        }));
  }

  private void setUpRV(List<Categoria> categorias) {
    sectionAdapter = new SectionedRecyclerViewAdapter();
    if (categorias != null
        && categorias.get(0) != null
        && categorias.get(0).getEstablecimientos() != null
        && categorias.get(0).getEstablecimientos().size() > 0) {

      for (Categoria categoria : categorias) {
        sectionAdapter.addSection(
            new HomeSection(this, categoria, categoria.getEstablecimientos()));
      }
    }

    GridLayoutManager glm = new GridLayoutManager(this, 2);
    glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        switch (sectionAdapter.getSectionItemViewType(position)) {
          case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
            return 2;
          default:
            return 1;
        }
      }
    });
    rvHome.setLayoutManager(glm);
    rvHome.setAdapter(sectionAdapter);
  }

  private void updateMap(JSONObject jsonObject) {

    if (geoJsonLayer != null && geoJsonLayer.isLayerOnMap()) {
      geoJsonLayer.removeLayerFromMap();
    }

    mapObservableProvider.getMapReadyObservable()
        .compose(bindToLifecycle())
        .subscribe(googleMap1 -> {
          log("updateMap");
          geoJsonLayer = MapUtils.initLayer(googleMap1, jsonObject);
          setLayerStyle(geoJsonLayer, this);
          geoJsonLayer.addLayerToMap();
        });
  }

  private void setLayerStyle(@NonNull GeoJsonLayer layer, BaseActivity baseActivity) {
    for (GeoJsonFeature feature : layer.getFeatures()) {
      drawMarker(baseActivity, feature);
    }
  }

  private void drawMarker(BaseActivity baseActivity, GeoJsonFeature feature) {
    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
    String baseRes = "ic_pin_";
    int drawable =
        baseActivity.getResourceId(baseRes + feature.getProperty("marker-symbol"), "drawable");
    pointStyle.setIcon(BitmapDescriptorFactory.fromResource(drawable));
    pointStyle.setTitle(feature.getProperty("marker-symbol"));
    feature.setPointStyle(pointStyle);
  }

  private void getGeoJson(Usuario usuario, String idZona) {
    Map<String, Object> param = new HashMap<>();
    param.put("filtro", idZona);

    REST.getRest()
        .categoriasLocalizacion(usuario.getToken(), param)
        .compose(bindToLifecycle())
        .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
        .subscribeOn(Schedulers.io())
        .doOnCompleted(this::dismissDialog)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::succesCategoriasLocalizacion, this::errControl);
  }

  private void centerMap(LatLngBounds latLngBounds) {
    mapObservableProvider.getMapReadyObservable()
        .compose(bindToLifecycle())
        .subscribe(googleMap -> {
          CameraPosition cameraPosition = new CameraPosition.Builder().target(
              latLngBounds.getCenter())      // Sets the center of the map to Mountain View
              .zoom(15)                   // Sets the zoom
              .build();
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
  }

  private void succesCategoriasLocalizacion(zonaDetalle zonaDetalle) {

    if (zonaDetalle.estado.equalsIgnoreCase("exito")) {
      ArrayList<LatLng> latLngs = new ArrayList<>();
      for (feature item : zonaDetalle.localizacion.features) {
        Double lat = item.geometry.coordinates.get(1);
        Double lng = item.geometry.coordinates.get(0);
        LatLng latLng = new LatLng(lat, lng);
        latLngs.add(latLng);
      }
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      builder.include(latLngs.get(0));
      builder.include(latLngs.get(1));
      centerMap(builder.build());
      try {
        String json = getGson().toJson(zonaDetalle.localizacion);
        log(json);
        JSONObject jsonObj = new JSONObject(json);
        updateMap(jsonObj);
        setUpRV(zonaDetalle.categorias);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @Override protected void onStop() {
    super.onStop();
    subscriptions.unsubscribe();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    goActv(home.class, true);
  }
}
