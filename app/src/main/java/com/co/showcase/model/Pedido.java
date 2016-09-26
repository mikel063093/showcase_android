package com.co.showcase.model;

/**
 * Created by home on 26/09/16.
 */

public class Pedido {

  /**
   * id : 1
   * fechaCreacion : 26 26America/New_York September 2016
   * estado : <font color='#fff5a623'>Procesando pedido</font>
   */

  public int id;
  public String fechaCreacion;
  public String estado;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(String fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }
}
