package com.co.showcase.model;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by home on 20/06/16.
 */

public class Usuario extends BaseModel {

  String id;
  String nombre;
  String apellido;
  String correo;
  String estado;
  String mensaje;
  String token;
  String clave;
  String telefono;

  public String getClave() {
    return clave;
  }

  public void setClave(String clave) {
    this.clave = clave;
  }

  public String getId() {
    return id;
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

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getMensaje() {
    return mensaje;
  }

  public void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public String getToken() {
    return "Bearer " + '"' + token + '"';
  }

  public void setToken(String token) {
    this.token = token;
  }

  public static Observable<Usuario> getItem() {
    Usuario usuario = new Usuario();
    return usuario.getObject(Usuario.class).subscribeOn(Schedulers.io()).asObservable();
  }

  public static Usuario GetItem() {
    return getObject(Usuario.class.getSimpleName(), Usuario.class);
  }


  @Override public String getTag() {
    return getClassName();
  }

  public String getTelefono() {
    return telefono;
  }
}
