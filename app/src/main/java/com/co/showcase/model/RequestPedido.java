package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 25/09/16.
 */

public class RequestPedido {

  /**
   * direccion : 1
   * telefono : 000000
   * formaPago : efectivo
   * cupon : 2
   * items : [{"id":"1","cantidad":2},{"id":"2","cantidad":2}]
   */

  public int direccion;
  public String telefono;
  public String formaPago;
  public String cupon;

  public int getDireccion() {
    return direccion;
  }

  public void setDireccion(int direccion) {
    this.direccion = direccion;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getFormaPago() {
    return formaPago;
  }

  public void setFormaPago(String formaPago) {
    this.formaPago = formaPago;
  }

  public String getCupon() {
    return cupon;
  }

  public void setCupon(String cupon) {
    this.cupon = cupon;
  }

  public List<ItemsBean> getItems() {
    return items;
  }

  public void setItems(List<ItemsBean> items) {
    this.items = items;
  }

  /**
   * id : 1
   * cantidad : 2
   */

  public List<ItemsBean> items;

  public static class ItemsBean {
    public String id;
    public int cantidad;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public int getCantidad() {
      return cantidad;
    }

    public void setCantidad(int cantidad) {
      this.cantidad = cantidad;
    }
  }
}
