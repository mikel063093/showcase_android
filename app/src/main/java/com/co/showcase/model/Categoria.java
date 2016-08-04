package com.co.showcase.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by home on 6/07/16.
 */

public class Categoria extends BaseModel {

  public int id;
  public String nombre;

  @SerializedName("establecimientos")
  public List<Establecimiento> establecimientos;

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
}
