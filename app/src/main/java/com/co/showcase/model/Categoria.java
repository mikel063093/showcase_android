package com.co.showcase.model;

import com.co.showcase.AppMain;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by home on 6/07/16.
 */

public class Categoria extends BaseModel {

  public int id;
  public String nombre;
  public String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @SerializedName("establecimientos") public List<Establecimiento> establecimientos;

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void setEstablecimientos(List<Establecimiento> establecimientos) {
    this.establecimientos = establecimientos;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Override public String getTag() {
    return getClassName();
  }

  public String getJson() {
    return AppMain.getGson().toJson(this);
  }
}
