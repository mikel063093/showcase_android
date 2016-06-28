package com.co.showcase.api;

import android.support.annotation.NonNull;

import com.co.showcase.model.EntryResponse;
import com.co.showcase.model.Usuario;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by miguelalegria on 13/5/16 for DemoMike.
 */
public interface API {
  @NonNull @GET("us/rss/topfreeapplications/limit=20/json") Observable<EntryResponse> getAppsList();

  @NonNull @FormUrlEncoded @POST("validar") Observable<Usuario> validar(@FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("registrar") Observable<Usuario> registrar(@FieldMap Map<String, Object> param);

  @NonNull @FormUrlEncoded @POST("registroRedesMovil") Observable<Usuario> registrarFB(@FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("registroNotificacion") Observable<Usuario> gcm(@FieldMap Map<String, String> param);

  @NonNull @FormUrlEncoded @POST("recuperar") Observable<Usuario> recuperar(@FieldMap Map<String, String> param);
}
