package com.co.showcase.model;

import com.co.showcase.AppMain;
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
  ArrayList<BeanArticulo> articulos;
  @SerializedName("whatsapp") String celular;
  String correo;

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

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
    ArrayList<Articulo> arr = new ArrayList<>();
    for (BeanArticulo beanArticulo : articulos) {
      Articulo articulo = new Articulo();
      articulo.setCantidad(beanArticulo.getCantidad());
      articulo.setDescripcion(beanArticulo.getDescripcion());
      articulo.setId(beanArticulo.getId());
      List<String> imagenes = new ArrayList<>();
      imagenes.add(beanArticulo.getImagen());
      articulo.setImagen(imagenes);
      articulo.setNombre(beanArticulo.getNombre());
      articulo.setPrecio(beanArticulo.getPrecio());
      articulo.setUnidades(beanArticulo.getUnidades());
      articulo.setValorUnidades(beanArticulo.getValorUnidades());
      arr.add(articulo);
    }
    return arr;
  }

  public void setArticulos(ArrayList<BeanArticulo> articulos) {
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

  public class BeanArticulo {
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
    String imagen;

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
}
