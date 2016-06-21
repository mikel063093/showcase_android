package com.co.showcase.api;

import android.support.annotation.NonNull;
import android.util.Log;
import com.co.showcase.BuildConfig;
import com.co.showcase.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import java.io.IOException;
import java.util.ServiceLoader;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by miguelalegria on 13/5/16 for DemoMike.
 */
public class REST {
  public static API getRest() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
      gsonBuilder.registerTypeAdapterFactory(factory);
    }

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY);
    httpClient.addInterceptor(logging);
    httpClient.addInterceptor(chain -> {
      Request original = chain.request();
      Usuario usuario = Usuario.GetItem();
      String token = "";
      if (usuario != null) {
        token = usuario.getToken();
      } else {
        Log("Header not found ");
      }
      Log("header " + token);
      Request request = original.newBuilder()
          .header("User-Agent", "Your-App-Name")
          .header("Authorization", token)
          .method(original.method(), original.body())
          .build();

      return chain.proceed(request);
    });

    OkHttpClient client = httpClient.build();
    Retrofit retrofit =
        new Retrofit.Builder().baseUrl("https://test.showcase.com.co/app.php/movil/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build();
    return retrofit.create(API.class);
  }

  private static void Log(@NonNull String txt) {
    if (BuildConfig.DEBUG) Log.e(REST.class.getSimpleName(), txt);
  }
}
