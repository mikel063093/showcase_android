package com.co.showcase.model;

import java.util.List;

/**
 * Created by home on 26/09/16.
 */

public class detallePedidos {

  /**
   * estado : 1
   * mensaje : Exito al obtener el detalle del pedido
   * pedido : {"id":1,"estado":"Activo","direccion":"Calle 28 # n-33","telefono":"3666888","metodoPago":"efectivo","nombres":"Mike","apellidos":"Alegria","cupon":"Sin
   * cupón","subtotal":120000,"domicilio":null,"total":120000,"items":[{"nombre":"estuche
   * nike","imagen":"https://test.showcase.com.co//imagenes/articulos/f57aab14d104c4368deac53937f773c4ae17a9e3.jpg","precio":60000,"cantidad":2}]}
   */

  public int estado;
  public String mensaje;

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

  public PedidoBean getPedido() {
    return pedido;
  }

  public void setPedido(PedidoBean pedido) {
    this.pedido = pedido;
  }

  /**
   * id : 1
   * estado : Activo
   * direccion : Calle 28 # n-33
   * telefono : 3666888
   * metodoPago : efectivo
   * nombres : Mike
   * apellidos : Alegria
   * cupon : Sin cupón
   * subtotal : 120000
   * domicilio : null
   * total : 120000
   * items : [{"nombre":"estuche nike","imagen":"https://test.showcase.com.co//imagenes/articulos/f57aab14d104c4368deac53937f773c4ae17a9e3.jpg","precio":60000,"cantidad":2}]
   */

  public PedidoBean pedido;

  public static class PedidoBean {
    public int id;
    public String estado;
    public String direccion;
    public String telefono;
    public String metodoPago;
    public String nombres;
    public String apellidos;
    public String cupon;
    public int subtotal;
    public Object domicilio;
    public int total;

    public List<ItemsBean> getItems() {
      return items;
    }

    public void setItems(List<ItemsBean> items) {
      this.items = items;
    }

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }

    public Object getDomicilio() {
      return domicilio;
    }

    public void setDomicilio(Object domicilio) {
      this.domicilio = domicilio;
    }

    public int getSubtotal() {
      return subtotal;
    }

    public void setSubtotal(int subtotal) {
      this.subtotal = subtotal;
    }

    public String getCupon() {
      return cupon;
    }

    public void setCupon(String cupon) {
      this.cupon = cupon;
    }

    public String getApellidos() {
      return apellidos;
    }

    public void setApellidos(String apellidos) {
      this.apellidos = apellidos;
    }

    public String getNombres() {
      return nombres;
    }

    public void setNombres(String nombres) {
      this.nombres = nombres;
    }

    public String getMetodoPago() {
      return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
      this.metodoPago = metodoPago;
    }

    public String getTelefono() {
      return telefono;
    }

    public void setTelefono(String telefono) {
      this.telefono = telefono;
    }

    public String getDireccion() {
      return direccion;
    }

    public void setDireccion(String direccion) {
      this.direccion = direccion;
    }

    public String getEstado() {
      return estado;
    }

    public void setEstado(String estado) {
      this.estado = estado;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    /**
     * nombre : estuche nike
     * imagen : https://test.showcase.com.co//imagenes/articulos/f57aab14d104c4368deac53937f773c4ae17a9e3.jpg
     * precio : 60000
     * cantidad : 2
     */

    public List<ItemsBean> items;

    public static class ItemsBean {
      public String nombre;
      public String imagen;
      public int precio;
      public int cantidad;

      public String getNombre() {
        return nombre;
      }

      public void setNombre(String nombre) {
        this.nombre = nombre;
      }

      public String getImagen() {
        return imagen;
      }

      public void setImagen(String imagen) {
        this.imagen = imagen;
      }

      public int getPrecio() {
        return precio;
      }

      public void setPrecio(int precio) {
        this.precio = precio;
      }

      public int getCantidad() {
        return cantidad;
      }

      public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
      }
    }
  }
}
