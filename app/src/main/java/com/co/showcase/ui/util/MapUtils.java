package com.co.showcase.ui.util;

import android.support.annotation.NonNull;
import com.co.showcase.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonPointStyle;
import org.json.JSONObject;

/**
 * Created by home on 13/08/16.
 */

public class MapUtils {

  public static void setLayerStyle(@NonNull GeoJsonLayer layer) {
    GeoJsonPointStyle pointStyle = layer.getDefaultPointStyle();
    pointStyle.setDraggable(false);
    pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
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
