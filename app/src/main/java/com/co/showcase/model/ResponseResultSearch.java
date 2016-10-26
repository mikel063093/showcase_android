package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 4/09/16.
 */

public class ResponseResultSearch {

  /**
   * estado : 1
   * mensaje : Exito al buscar posibles palabras
   * articulos : [{"id":1,"nombre":"camisa lacoste","precio":1000,"unidades":"talla","valorUnidades":10,"imagen":"https://test.showcase.com.co//imagenes/articulos/cc1b91f532c1b888d9056792a282fa8dc7e28f4f.png"},{"id":2,"nombre":"pantalon
   * levis","precio":20000,"unidades":"talla","valorUnidades":2,"imagen":"https://test.showcase.com.co//imagenes/articulos/f57aab14d104c4368deac53937f773c4ae17a9e3.jpg"},{"id":5,"nombre":"Condones
   * today","precio":2000,"unidades":"unidades","valorUnidades":3,"imagen":"https://test.showcase.com.co//imagenes/articulos/a171ed704d50d39dd9bfd4628069791a8bdda06b.jpg"}]
   */

  public int estado;
  public String mensaje;


  public List<Articulo> articulos;

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

  public List<Articulo> getArticulos() {
    return articulos;
  }

  public void setArticulos(List<Articulo> articulos) {
    this.articulos = articulos;
  }
}
