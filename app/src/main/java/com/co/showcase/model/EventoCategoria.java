package com.co.showcase.model;

/**
 * Created by home on 22/10/16.
 */

public class EventoCategoria {

  /**
   * estado : 0
   * mensaje : Error al obtener la categoria eventos
   */

  public int estado;
  public String mensaje;
  public String categoria;

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

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
}
