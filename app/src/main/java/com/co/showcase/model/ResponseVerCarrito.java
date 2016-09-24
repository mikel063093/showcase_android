package com.co.showcase.model;

/**
 * Created by home on 24/09/16.
 */

public class ResponseVerCarrito {

  /**
   * estado : 1
   * mensaje : Exito al obtener el carrito
   */

  public int estado;
  public String mensaje;
  public Carrito carrito;

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

  public Carrito getCarrito() {
    return carrito;
  }

  public void setCarrito(Carrito carrito) {
    this.carrito = carrito;
  }
}
