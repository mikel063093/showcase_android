package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 24/09/16.
 */

public class Carrito {

  /**
   * id : 2
   * fechaCreacion : 2016-09-24 15:18:50
   */
  /**
   * id : 1
   * idArticulo : 1
   * nombre : camisa lacoste
   * imagen : https://test.showcase.com.co//imagenes/articulos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png
   * precio : 1000
   * cantidad : 1
   */
  /**
   * subtotal : 1000
   * domicilio : null
   * total : 1000
   * items : [{"id":1,"idArticulo":1,"nombre":"camisa lacoste","imagen":"https://test.showcase.com.co//imagenes/articulos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png","precio":1000,"cantidad":1}]
   */

  public int id;
  public String fechaCreacion;

  public int subtotal;
  public String domicilio;
  public int total;

  public List<ItemsBean> items;

  public int getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(int subtotal) {
    this.subtotal = subtotal;
  }

  public String getDomicilio() {
    return domicilio != null ? domicilio : "";
  }

  public void setDomicilio(String domicilio) {
    this.domicilio = domicilio;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<ItemsBean> getItems() {
    return items;
  }

  public void setItems(List<ItemsBean> items) {
    this.items = items;
  }

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

  public static class ItemsBean {
    public int id;
    public int idArticulo;
    public String nombre;
    public String imagen;
    public int precio;
    public int cantidad;
  }
}
