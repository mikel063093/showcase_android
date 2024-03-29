package com.co.showcase.api;

import android.support.annotation.NonNull;
import com.co.showcase.model.EntryResponse;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.EventoCategoria;
import com.co.showcase.model.ResponseAgregarCarrito;
import com.co.showcase.model.ResponseAutoComplete;
import com.co.showcase.model.ResponseCategorias;
import com.co.showcase.model.ResponseCupon;
import com.co.showcase.model.ResponseDetalleArticulo;
import com.co.showcase.model.ResponseDirecciones;
import com.co.showcase.model.ResponseHome;
import com.co.showcase.model.ResponsePuntuacion;
import com.co.showcase.model.ResponseRealizarPedido;
import com.co.showcase.model.ResponseResultSearch;
import com.co.showcase.model.ResponseVerCarrito;
import com.co.showcase.model.Terminos;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.Zonas;
import com.co.showcase.model.detallePedidos;
import com.co.showcase.model.responseAgregarDireccion;
import com.co.showcase.model.responsePedidos;
import com.co.showcase.model.usuario.ResponseCategoriaDetalle;
import com.co.showcase.model.zonaDetalle;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by miguelalegria on 13/5/16 for DemoMike.
 */
public interface API {
  @NonNull @GET("us/rss/topfreeapplications/limit=20/json") Observable<EntryResponse> getAppsList();

  @NonNull @FormUrlEncoded @POST("validar") Observable<Usuario> validar(
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("registrar") Observable<Usuario> registrar(
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("registroRedesMovil") Observable<Usuario> registrarFB(
      @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("registroNotificacion") Observable<Usuario> gcm(
      @Header("Authorization") String authorization, @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("recuperarContrasena") Observable<Usuario> recuperar(
      @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("editarPerfil") Observable<Usuario> subirFoto(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("establecimientos") Observable<ResponseHome> establecimientos(
      @Header("Authorization") String authorization, @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("establecimiento") Observable<Establecimiento> establecimiento(
      @Header("Authorization") String authorization, @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("categorias") Observable<ResponseCategorias> categorias(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("categoria") Observable<ResponseCategoriaDetalle> categoria(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("puntuarEstablecimiento")
  Observable<ResponsePuntuacion> puntuarEstablecimiento(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("detalleProducto")
  Observable<ResponseDetalleArticulo> detalleProducto(@Header("Authorization") String authorization,
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("categoriasLocalizacion")
  Observable<zonaDetalle> categoriasLocalizacion(@Header("Authorization") String authorization,
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("zonas") Observable<Zonas> zonas(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("autoCompletarBusqueda")
  Observable<ResponseAutoComplete> autoCompletarBusqueda(
      @Header("Authorization") String authorization, @FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("realizarBusqueda")
  Observable<ResponseResultSearch> realizarBusqueda(@Header("Authorization") String authorization,
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("direcciones") Observable<ResponseDirecciones> direcciones(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("agregarDireccion")
  Observable<responseAgregarDireccion> agregarDireccion(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("agregarProductoCarrito")
  Observable<ResponseAgregarCarrito> agregarProductoCarrito(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("verCarrito") Observable<ResponseVerCarrito> verCarrito(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("cancelarCarrito") Observable<ResponseVerCarrito> cancelarCarrito(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("eliminarProductoCarrito")
  Observable<ResponseVerCarrito> eliminarProductoCarrito(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("editarProductoCarrito")
  Observable<ResponseVerCarrito> editarProductoCarrito(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("redimirCupon") Observable<ResponseCupon> redimirCupon(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("realizarPedido")
  Observable<ResponseRealizarPedido> realizarPedido(@Header("Authorization") String authorization,
      @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("pedidosActivos") Observable<responsePedidos> pedidosActivos(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("pedidos") Observable<responsePedidos> pedidos(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("detallePedido") Observable<detallePedidos> detallePedido(
      @Header("Authorization") String authorization, @FieldMap Map<String, Object> param);

  @GET("eventos") Observable<EventoCategoria> eventos(
      @Header("Authorization") String authorization);

  @GET("terminos") Observable<Terminos> terminos(@Header("Authorization") String authorization);
}

