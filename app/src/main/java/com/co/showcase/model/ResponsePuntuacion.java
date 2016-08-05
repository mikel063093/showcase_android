package com.co.showcase.model;

/**
 * Created by home on 5/08/16.
 */

public class ResponsePuntuacion {

  /**
   * estado : 1
   * mensaje : Exito al puntuar establecimiento
   * puntuacionUsuario : 3
   * puntuacion : 3
   */

  public int estado;
  public String mensaje;
  public String puntuacionUsuario;
  public float puntuacion;

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

  public String getPuntuacionUsuario() {
    return puntuacionUsuario;
  }

  public void setPuntuacionUsuario(String puntuacionUsuario) {
    this.puntuacionUsuario = puntuacionUsuario;
  }

  public float getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(float puntuacion) {
    this.puntuacion = puntuacion;
  }
}
