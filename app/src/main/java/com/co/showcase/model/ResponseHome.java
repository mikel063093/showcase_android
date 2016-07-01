package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 1/07/16.
 */

public class ResponseHome extends BaseModel {
  String estado;
  String mensaje;
  List<Slides> promociones;
  List<Establecimiento> establecimientos;

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public List<Slides> getPromociones() {
    return promociones;
  }

  public void setPromociones(List<Slides> promociones) {
    this.promociones = promociones;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void setEstablecimientos(List<Establecimiento> establecimientos) {
    this.establecimientos = establecimientos;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
