package com.co.showcase.model;

/**
 * Created by home on 25/09/16.
 */

public class ResponseCupon {
  /**
   * estado : 0
   * mensaje : Cupon No Valido
   */

  public int estado;
  public String mensaje;

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
}
