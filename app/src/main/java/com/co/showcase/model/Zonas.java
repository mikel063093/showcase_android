package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 1/09/16.
 */

public class Zonas {
  /**
   * estado : 1
   * mensaje : Exito al obtener las zonas
   * zonas : [{"id":1,"nombre":"Norte"}]
   */

  public int estado;
  public String mensaje;
  /**
   * id : 1
   * nombre : Norte
   */

  public List<ZonasBean> zonas;

  public static class ZonasBean {
    public String id;
    public String nombre;
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

  public List<ZonasBean> getZonas() {
    return zonas;
  }

  public void setZonas(List<ZonasBean> zonas) {
    this.zonas = zonas;
  }
}
