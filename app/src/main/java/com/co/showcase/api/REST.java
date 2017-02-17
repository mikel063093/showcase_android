package com.co.showcase.api;

import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by miguelalegria on 13/5/16 for DemoMike.
 */
public class REST {

  public static API getRest() {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY);
    httpClient.addInterceptor(logging);
    httpClient.connectTimeout(5, TimeUnit.SECONDS);
    OkHttpClient client = httpClient.build();

    Retrofit retrofit =
        new Retrofit.Builder().baseUrl("https://showcase.com.co/app_dev.php/movil/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(AppMain.getGson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build();
    return retrofit.create(API.class);

  }
}
