package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 16/09/16.
 */

public class ResponseDirecciones {

  /**
   * estado : 1
   * mensaje : Direcciones encontradas con exito.
   * direcciones : [{"id":1,"nombre":"casa","tipo":"Carrera","numero":"51A","nomenclatura":"2A"},{"id":2,"nombre":"casa","tipo":"Carrera","numero":"51A","nomenclatura":"2A"}]
   */

  public int estado;
  public String mensaje;
  /**
   * id : 1
   * nombre : casa
   * tipo : Carrera
   * numero : 51A
   * nomenclatura : 2A
   */

  public List<Direccion> direcciones;

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

  public List<Direccion> getDirecciones() {
    return direcciones;
  }

  public void setDirecciones(List<Direccion> direcciones) {
    this.direcciones = direcciones;
  }

}
