package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 26/09/16.
 */

public class responsePedidos {

  /**
   * estado : 1
   * mensaje : Exito al obtener los pedidos del usuario
   */

  public int estado;
  public String mensaje;
  public List<Pedido> pedidos;

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

  public List<Pedido> getPedidos() {
    return pedidos;
  }

  public void setPedidos(List<Pedido> pedidos) {
    this.pedidos = pedidos;
  }
}
