package com.co.showcase.model;

/**
 * Created by home on 5/08/16.
 */

public class ResponseDetalleArticulo {

  /**
   * estado : 1
   * mensaje : Exito al buscar el articulo
   */

  public int estado;
  public String mensaje;
  public Articulo articulo;

  public int getEstado() {
    return estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public Articulo getArticulo() {
    return articulo;
  }

  public void setArticulo(Articulo articulo) {
    this.articulo = articulo;
  }
}
