package com.co.showcase.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 30/06/16.
 */

public class Establecimiento extends BaseModel {

  /**
   * estado : exito
   * mensaje : Establecimiento obtenido exitosamente
   * descripcion : ESTABLECIMIENTO 1
   * direccion : ESTABLECIMIENTO 1
   * telefono : ESTABLECIMIENTO 1
   * sitioWeb : null
   * facebook : null
   * twitter : null
   * snapchat : null
   * youtube : null
   * instagram : null
   * puntuacionUsuario : null
   * puntuacion : 3
   */

  @SerializedName("logo") List<String> urlImagen;
  //@SerializedName("logo") String logo;
  //@SerializedName("logo") String urlImagen;
  String nombre;
  String id;
  String marcador;
  String estado;
  String mensaje;
  String descripcion;
  String direccion;
  String telefono;
  String sitioWeb;
  String facebook;
  String twitter;
  String snapchat;
  String youtube;
  String instagram;
  String puntuacionUsuario;
  Double puntuacion;
  String celular;
  String correo;

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  ArrayList<Articulo> articulos;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<String> getUrlImagen() {
    return urlImagen;
  }

  public void setUrlImagen(List<String> urlImagen) {
    this.urlImagen = urlImagen;
  }

  @Override public String getTag() {
    return getClassName();
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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getSitioWeb() {
    return sitioWeb;
  }

  public void setSitioWeb(String sitioWeb) {
    this.sitioWeb = sitioWeb;
  }

  public String getFacebook() {
    return facebook;
  }

  public void setFacebook(String facebook) {
    this.facebook = facebook;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public String getSnapchat() {
    return snapchat;
  }

  public void setSnapchat(String snapchat) {
    this.snapchat = snapchat;
  }

  public String getYoutube() {
    return youtube;
  }

  public void setYoutube(String youtube) {
    this.youtube = youtube;
  }

  public String getInstagram() {
    return instagram;
  }

  public void setInstagram(String instagram) {
    this.instagram = instagram;
  }

  public String getPuntuacionUsuario() {
    return puntuacionUsuario;
  }

  public void setPuntuacionUsuario(String puntuacionUsuario) {
    this.puntuacionUsuario = puntuacionUsuario;
  }

  public Double getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(Double puntuacion) {
    this.puntuacion = puntuacion;
  }

  public ArrayList<Articulo> getArticulos() {
    return articulos;
  }

  public void setArticulos(ArrayList<Articulo> articulos) {
    this.articulos = articulos;
  }

  public String getMarcador() {
    return marcador;
  }

  public void setMarcador(String marcador) {
    this.marcador = marcador;
  }

  public String getCelular() {
    return celular;
  }

  public void setCelular(String celular) {
    this.celular = celular;
  }
}
