package com.co.showcase.model;

/**
 * Created by home on 22/10/16.
 */

public class Terminos {

  /**
   * estado : 1
   * mensaje : Exito al obtener los terminos y condiciones
   * vista :
   */

  public int estado;
  public String mensaje;
  public String vista;

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

  public String getVista() {
    return vista;
  }

  public void setVista(String vista) {
    this.vista = vista;
  }
}
