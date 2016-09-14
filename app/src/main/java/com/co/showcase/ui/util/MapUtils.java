package com.co.showcase.ui.util;

import android.support.annotation.NonNull;
import com.co.showcase.ui.BaseActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import org.json.JSONObject;

/**
 * Created by home on 13/08/16.
 */

public class MapUtils {

  public static void setLayerStyle(@NonNull GeoJsonLayer layer, BaseActivity baseActivity) {

    for (GeoJsonFeature feature : layer.getFeatures()) {

      switch (feature.getProperty("marker-symbol")) {
        case "1":
          drawMarker(baseActivity, feature);
          break;
        case "2":
          drawMarker(baseActivity, feature);
          break;
        case "3":
          drawMarker(baseActivity, feature);
          break;
        case "4":
          drawMarker(baseActivity, feature);
          break;
        case "5":
          drawMarker(baseActivity, feature);
          break;
        case "6":
          drawMarker(baseActivity, feature);
          break;
        case "7":
          drawMarker(baseActivity, feature);
          break;
        case "8":
          drawMarker(baseActivity, feature);
          break;
        case "9":
          drawMarker(baseActivity, feature);
          break;
        case "10":
          drawMarker(baseActivity, feature);
          break;
        case "11":
          drawMarker(baseActivity, feature);
          break;
        case "12":
          drawMarker(baseActivity, feature);
          break;
        case "13":
          drawMarker(baseActivity, feature);
          break;
        case "14":
          drawMarker(baseActivity, feature);
          break;
        case "15":
          drawMarker(baseActivity, feature);
          break;
        case "16":
          drawMarker(baseActivity, feature);
          break;
        case "17":
          drawMarker(baseActivity, feature);
          break;
        case "18":
          drawMarker(baseActivity, feature);
          break;
        case "19":
          drawMarker(baseActivity, feature);
          break;
        case "20":
          drawMarker(baseActivity, feature);
          break;
        default:
          baseActivity.log("default case");
          drawMarker(baseActivity, feature);
          break;
      }
    }
  }

  private static void drawMarker(BaseActivity baseActivity, GeoJsonFeature feature) {
    String baseRes = "ic_pin_";
    int drawable =
        baseActivity.getResourceId(baseRes + feature.getProperty("marker-symbol"), "drawable");
    baseActivity.log(baseRes + feature.getProperty("marker-symbol"));
    GeoJsonPointStyle style = feature.getPointStyle();
    style.setDraggable(false);
    style.setTitle(feature.getProperty("marker-symbol"));
    style.setIcon(BitmapDescriptorFactory.fromResource(drawable));
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
