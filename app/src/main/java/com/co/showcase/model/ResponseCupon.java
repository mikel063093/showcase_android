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
  /**
   * id : 1
   * valor : 10000
   */

  public int id;
  public int valor;

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

  public int getValor() {
    return valor;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
