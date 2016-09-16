package com.co.showcase.model;

/**
 * Created by home on 16/09/16.
 */

public class Direccion {

  /**
   * id : 1
   * nombre : casa
   * tipo : Carrera
   * numero : 51A
   * nomenclatura : 2A
   */

  public int id;
  public String nombre;
  public String tipo;
  public String numero;
  public String nomenclatura;

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

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getNomenclatura() {
    return nomenclatura;
  }

  public void setNomenclatura(String nomenclatura) {
    this.nomenclatura = nomenclatura;
  }
}
