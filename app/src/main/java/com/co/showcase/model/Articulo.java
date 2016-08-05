package com.co.showcase.model;

import com.co.showcase.AppMain;

/**
 * Created by home on 4/08/16.
 */

public class Articulo {

  /**
   * descripcion : camisa lacoste
   * cantidad : 10
   */

  public String descripcion;
  public Double cantidad;
  /**
   * id : 1
   * nombre : camisa lacoste
   * precio : 1000
   * unidades : talla
   * valorUnidades : 10
   * imagen : https://test.showcase.com.co//imagenes/articulos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png
   */

  private int id;
  private String nombre;
  private int precio;
  private String unidades;
  private int valorUnidades;
  private String imagen;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getPrecio() {
    return precio;
  }

  public void setPrecio(int precio) {
    this.precio = precio;
  }

  public String getUnidades() {
    return unidades;
  }

  public void setUnidades(String unidades) {
    this.unidades = unidades;
  }

  public int getValorUnidades() {
    return valorUnidades;
  }

  public void setValorUnidades(int valorUnidades) {
    this.valorUnidades = valorUnidades;
  }

  public String getImagen() {
    return imagen;
  }

  public void setImagen(String imagen) {
    this.imagen = imagen;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  @Override public String toString() {
    return AppMain.getGson().toJson(this);
  }
}
