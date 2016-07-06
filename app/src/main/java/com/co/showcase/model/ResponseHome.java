package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 1/07/16.
 */

public class ResponseHome extends BaseModel {
  String estado;
  String mensaje;
  List<Categoria> categorias;
  List<Slides> promociones;

  public String getEstado() {
    return estado;
  }

  public List<Categoria> getCategorias() {
    return categorias;
  }

  public void setCategorias(List<Categoria> categorias) {
    this.categorias = categorias;
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

  @Override public String getTag() {
    return getClassName();
  }
}
