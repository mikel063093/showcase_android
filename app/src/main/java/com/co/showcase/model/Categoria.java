package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 6/07/16.
 */

public class Categoria extends BaseModel {
  public int firstPosition;
  public int sectionedPosition;

  public Categoria(int firstPosition, String nombre) {
    this.firstPosition = firstPosition;
    this.nombre = nombre;
  }

  public Categoria() {
  }

  public Categoria(String nombre) {
    this.nombre = nombre;
  }

  String id;
  String nombre;

  List<Establecimiento> establecimientos;

  public String getId() {
    return id;
  }

  public int getFirstPosition() {
    return firstPosition;
  }

  public void setFirstPosition(int firstPosition) {
    this.firstPosition = firstPosition;
  }

  public int getSectionedPosition() {
    return sectionedPosition;
  }

  public void setSectionedPosition(int sectionedPosition) {
    this.sectionedPosition = sectionedPosition;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<Establecimiento> getEstablecimientos() {
    return establecimientos;
  }

  public void setEstablecimientos(List<Establecimiento> establecimientos) {
    this.establecimientos = establecimientos;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
