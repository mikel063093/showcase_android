package com.co.showcase.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.co.showcase.AppMain;
import com.co.showcase.model.usuario.userInterator;
import com.co.showcase.model.usuario.userIteractorImpl;
import com.co.showcase.utils.JSONUtils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.HashMap;
import java.util.Map;
import org.immutables.value.Value;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by home on 20/06/16.
 */
@Value.Immutable public class Usuario extends RealmObject {

  @PrimaryKey String id;
  String nombre;
  String apellido;
  String correo;
  String estado;
  String mensaje;
  String token;
  String clave;
  String telefono;
  String foto;
  private static userInterator interactor = new userIteractorImpl();

  public Usuario() {
    interactor = new userIteractorImpl();
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  @NonNull public String getFullName() {
    return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
  }

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

  @NonNull public String getToken() {
    return "Bearer " + token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public static Observable<Usuario> getItem() {
    // Usuario usuario = new Usuario();
    //return interactor.getUser().map(usuarios -> usuarios.get(0)).asObservable();
    return interactor.getUser();
    // return usuario.getObject(Usuario.class).subscribeOn(Schedulers.io()).asObservable();
  }

  public static Observable<Usuario> getLiveItem() {
    // Usuario usuario = new Usuario();
    return interactor.getLiveUser();
    // return usuario.getObject(Usuario.class).subscribeOn(Schedulers.io()).asObservable();
  }

  public Observable<Void> save() {

    return interactor.createOrUpdateUser(this).asObservable();
  }

  public void deleted() {
    interactor.deleteUser();
  }

  @Nullable public Map<String, Object> jsonToMap() {
    try {
      JSONObject jsonObject = new JSONObject(this.toJson());
      return jsonToMap(jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  @NonNull private Map<String, Object> jsonToMap(@NonNull JSONObject json) throws JSONException {
    Map<String, Object> retMap = new HashMap<>();
    if (json != JSONObject.NULL) {
      retMap = JSONUtils.toMap(json);
    }
    return retMap;
  }

  //
  //@Nullable public static Usuario GetItem() {
  //  return getObject(Usuario.class.getSimpleName(), Usuario.class);
  //}
  //
  //@Override public String getTag() {
  //  return getClassName();
  //}

  private String toJson() {
    return AppMain.getGson().toJson(this);
  }

  public String getTelefono() {
    return telefono;
  }
}
