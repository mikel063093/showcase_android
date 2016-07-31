package com.co.showcase.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by home on 30/06/16.
 */

public class Establecimiento extends BaseModel {

  @SerializedName("logo") String urlImagen;
  String nombre;
  String id;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrlImagen() {
    return urlImagen;
  }

  public void setUrlImagen(String urlImagen) {
    this.urlImagen = urlImagen;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
