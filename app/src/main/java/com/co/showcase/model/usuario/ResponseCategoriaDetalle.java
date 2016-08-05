package com.co.showcase.model.usuario;

import com.co.showcase.model.Categoria;

/**
 * Created by home on 5/08/16.
 */

public class ResponseCategoriaDetalle {

  /**
   * estado : 1
   * mensaje : Categorias encontradas con exito.
   */

  int estado;
  String mensaje;
  Categoria categorias;

  public Categoria getCategorias() {
    return categorias;
  }

  public void setCategorias(Categoria categorias) {
    this.categorias = categorias;
  }

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
}
