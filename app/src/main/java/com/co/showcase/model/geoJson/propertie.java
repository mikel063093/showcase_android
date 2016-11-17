package com.co.showcase.model.geoJson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by home on 13/08/16.
 */

public class propertie {

  /**
   * marker-color : #f5a623
   * marker-size : small
   * marker-symbol : 2
   */

  @SerializedName("marker-color") public String marker_color;
  @SerializedName("marker-size") public String marker_size;
  @SerializedName("marker-title") public String marker_title;
  @SerializedName("marker-symbol") public int marker_symbol;
}
