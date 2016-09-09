package com.co.showcase.ui.util;

import android.support.annotation.NonNull;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import com.orhanobut.logger.Logger;
import org.json.JSONObject;

/**
 * Created by home on 13/08/16.
 */

public class MapUtils {

  public static void setLayerStyle(@NonNull GeoJsonLayer layer, BaseActivity baseActivity) {


    for (GeoJsonFeature feature : layer.getFeatures()) {
      String baseRes = "ic_pin_";
      int drawable =
          baseActivity.getResourceId(baseRes + feature.getProperty("marker-symbol"), "drawable");
      baseActivity.log(baseRes + feature.getProperty("marker-symbol"));
      GeoJsonPointStyle style = feature.getPointStyle();
      style.setDraggable(false);
      style.setTitle(feature.getProperty("marker-symbol"));
      style.setIcon(BitmapDescriptorFactory.fromResource(drawable));
    }
  }

  public synchronized static GeoJsonLayer initLayer(@NonNull GoogleMap map,
      @NonNull JSONObject geoJson) {
    return new GeoJsonLayer(map, geoJson);
  }

  public static void clearLayer(@NonNull GeoJsonLayer layer) {
    layer.removeLayerFromMap();
  }

  public static void addLayerToMap(@NonNull GeoJsonLayer layer) {
    layer.addLayerToMap();
  }
}
