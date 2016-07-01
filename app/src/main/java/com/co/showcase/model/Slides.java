package com.co.showcase.model;



/**
 * Created by home on 30/06/16.
 */

public class Slides extends BaseModel {
  String urlImagen;
  String id;

  public String getUrlImagen() {
    return urlImagen;
  }

  public void setUrlImagen(String urlImagen) {
    this.urlImagen = urlImagen;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
