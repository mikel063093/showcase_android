package com.co.showcase.ui.map;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.zonaDetalle;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.home.HomeSection;
import com.co.showcase.ui.util.MapUtils;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.sdoward.rxgooglemap.MapObservableProvider;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
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

  @Bind(R.id.toolbar_home) Toolbar toolbarHome;
  @Bind(R.id.rv_home) RecyclerView rvHome;
  @Bind(R.id.drawer) RelativeLayout drawer;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  private CompositeSubscription subscriptions = Subscriptions.from();
  private GeoJsonLayer geoJsonLayer;

  MapObservableProvider mapObservableProvider;
  private SectionedRecyclerViewAdapter sectionAdapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_map);
    ButterKnife.bind(this);
    initMap();
    configBackToolbar(toolbarHome);
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
          googleMap.getUiSettings().setScrollGesturesEnabled(true);
          googleMap.getUiSettings().setTiltGesturesEnabled(false);
          googleMap.getUiSettings().setZoomGesturesEnabled(true);
          googleMap.getUiSettings().setRotateGesturesEnabled(false);
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
          MapUtils.setLayerStyle(geoJsonLayer);
          MapUtils.addLayerToMap(geoJsonLayer);
        });
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

  private void succesCategoriasLocalizacion(zonaDetalle zonaDetalle) {
    if (zonaDetalle.estado.equalsIgnoreCase("exito")) {
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
}
