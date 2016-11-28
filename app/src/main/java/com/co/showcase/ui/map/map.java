package com.co.showcase.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.co.showcase.model.zonaDetalle;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.home.HomeSection;
import com.co.showcase.ui.home.home;
import com.co.showcase.ui.util.ItemDecorationAlbumColumns;
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

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.rv_home) RecyclerView rvHome;
  @Nullable @Bind(R.id.imgmaptransparent) ImageView imgmaptransparent;
  @Nullable @Bind(R.id.scroll) NestedScrollView scroll;

  @NonNull private CompositeSubscription subscriptions = Subscriptions.from();
  private GeoJsonLayer geoJsonLayer;
  private MapObservableProvider mapObservableProvider;
  //private SectionedRecyclerViewAdapter sectionAdapter;
  private LatLng lantLongCenter;
  private int zoom;
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
    configBackToolbar(toolbar, true);
  }

  private String getZona() {
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      return getIntent().getStringExtra(this.getClass().getSimpleName());
    }
    return "-1";
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

  private void setUpRV(@Nullable List<Categoria> categorias) {
    sectionAdapter = new SectionedRecyclerViewAdapter();
    if (categorias != null
        && categorias.get(0) != null
        && categorias.get(0).getEstablecimientos() != null
        && categorias.get(0).getEstablecimientos().size() > 0) {

      for (Categoria categoria : categorias) {
        sectionAdapter.addSection(
            new HomeSection(this, categoria, categoria.getEstablecimientos(),false));
      }
    }
    //List<Establecimiento> establecimientos = new ArrayList<>();
    //for (Categoria categoria : categorias) {
    //  establecimientos.addAll(categoria.getEstablecimientos());
    //}
    //itemMapadapter mapadapter = new itemMapadapter(this,establecimientos);

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

    rvHome.setNestedScrollingEnabled(false);
    rvHome.addItemDecoration(
        new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen._6sdp),
            getResources().getInteger(R.integer.photo_list_preview_columns)));
    rvHome.setLayoutManager(glm);
    rvHome.setAdapter(sectionAdapter);
  }

  private void updateMap(@NonNull JSONObject jsonObject) {

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

  private void setLayerStyle(@NonNull GeoJsonLayer layer, @NonNull BaseActivity baseActivity) {
    for (GeoJsonFeature feature : layer.getFeatures()) {
      drawMarker(baseActivity, feature);
    }
  }

  private void drawMarker(@NonNull BaseActivity baseActivity, @NonNull GeoJsonFeature feature) {
    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
    String baseRes = "ic_pin_";
    int drawable =
        baseActivity.getResourceId(baseRes + feature.getProperty("marker-symbol"), "drawable");
    pointStyle.setIcon(BitmapDescriptorFactory.fromResource(drawable));
    try {
      pointStyle.setTitle(
          feature.getProperty("marker-title") != null ? feature.getProperty("marker-title") : "");
    } catch (Throwable e) {
      log(e.getMessage());
    }

    feature.setPointStyle(pointStyle);
  }

  private void getGeoJson(@NonNull Usuario usuario, String idZona) {
    log(idZona);
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

  private void centerMap(@NonNull LatLngBounds latLngBounds) {
    mapObservableProvider.getMapReadyObservable()
        .compose(bindToLifecycle())
        .subscribe(googleMap -> {
          CameraPosition cameraPosition = new CameraPosition.Builder().target(
              latLngBounds.getCenter())      // Sets the center of the map to Mountain View
              .zoom(13)                   // Sets the zoom
              .build();
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
  }

  private void centerMap(@NonNull LatLng latLng, int zoom) {
    mapObservableProvider.getMapReadyObservable()
        .compose(bindToLifecycle())
        .subscribe(googleMap -> {
          CameraPosition cameraPosition = new CameraPosition.Builder().target(
              latLng)      // Sets the center of the map to Mountain View
              .zoom(zoom)                   // Sets the zoom
              .build();
          googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        });
  }

  private void succesCategoriasLocalizacion(@NonNull zonaDetalle zonaDetalle) {

    if (zonaDetalle.estado.equalsIgnoreCase("exito")
        && zonaDetalle.categorias.size() > 0
        && zonaDetalle.centro != null
        && zonaDetalle.centro.length() > 0
        && zonaDetalle.zoom != null) {

      try {
        Double lat = Double.parseDouble(zonaDetalle.centro.split(",")[0]);
        Double lng = Double.parseDouble(zonaDetalle.centro.split(",")[1]);
        this.lantLongCenter = new LatLng(lat, lng);
        String json = getGson().toJson(zonaDetalle.localizacion);
        this.zoom = Integer.parseInt(zonaDetalle.zoom);
        centerMap(lantLongCenter, zoom);
        log(json);
        JSONObject jsonObj = new JSONObject(json);
        updateMap(jsonObj);
        setUpRV(zonaDetalle.categorias);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
      showMaterialDialog(getString(R.string.err_datos_zona), new onClickCallback() {
        @Override public void onPositive(boolean result) {
          goActv(home.class, true);
        }

        @Override public void onDissmis() {
          goActv(home.class, true);
        }

        @Override public void onNegative(boolean result) {
          goActv(home.class, true);
        }
      });
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
