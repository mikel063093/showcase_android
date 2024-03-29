package com.co.showcase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import org.json.JSONObject;
import rx_activity_result.RxActivityResult;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by miguelalegria
 */
public class AppMain extends MultiDexApplication {

  private static Gson gson;

  @NonNull public static AppMain getApp(@NonNull Context context) {
    return (AppMain) context.getApplicationContext();
  }

  @Override public void onCreate() {
    super.onCreate();
    Fabric.with(this, new Crashlytics());
    Logger.init(getString(R.string.app_name))
        .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
        .methodCount(4);

    Logger.e("onCreate App");
    RxActivityResult.register(this);

    Realm.init(this);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
    Realm.setDefaultConfiguration(realmConfiguration);
    GsonBuilder gsonBuilder = new GsonBuilder();

    gson = gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
      @Override public boolean shouldSkipField(@NonNull FieldAttributes f) {
        //Logger.e("exclusion  RealObject " + f.getDeclaringClass().equals(RealmObject.class));
        return f.getDeclaringClass().equals(RealmObject.class);
      }

      @Override public boolean shouldSkipClass(Class<?> clazz) {
        return false;
      }
    }).create();

    RxPaparazzo.register(this);

    FacebookSdk.sdkInitialize(this.getApplicationContext());

    OneSignal.startInit(this)
        .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
        .setAutoPromptLocation(false)
        .init();

    OneSignal.setLogLevel(BuildConfig.DEBUG ? OneSignal.LOG_LEVEL.DEBUG : OneSignal.LOG_LEVEL.NONE,
        OneSignal.LOG_LEVEL.NONE);

    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Circular-Std-Book.otf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }

  public static Gson getGson() {
    return gson;
  }

  private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    @Override
    public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
    }
  }
}
