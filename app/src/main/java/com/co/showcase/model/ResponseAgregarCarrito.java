package com.co.showcase.model;

/**
 * Created by home on 24/09/16.
 */

public class ResponseAgregarCarrito {

  /**
   * estado : 1
   * mensaje : Exito al agregar el producto al carrito
   * carrito : {"id":2,"fechaCreacion":"2016-09-24 15:18:50"}
   */

  public int estado;
  public String mensaje;
  /**
   * id : 2
   * fechaCreacion : 2016-09-24 15:18:50
   */

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
