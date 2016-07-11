package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 11/07/16.
 */

public class ResponseCategorias extends BaseModel {
  String estado;
  String mensaje;
  List<Categoria> categorias;

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

  public List<Categoria> getCategorias() {
    return categorias;
  }

  public void setCategorias(List<Categoria> categorias) {
    this.categorias = categorias;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
