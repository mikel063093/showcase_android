package com.co.showcase;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import io.supercharge.rxsnappy.RxSnappy;
import io.supercharge.rxsnappy.RxSnappyClient;
import org.json.JSONObject;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by miguelalegria
 */
public class AppMain extends Application {
  private static Context context;
  private RxSnappyClient rxSnappyClient;

  public static Context getContex() {
    return context;
  }

  @NonNull public static AppMain getApp(@NonNull Context context) {
    return (AppMain) context.getApplicationContext();
  }

  @NonNull public static AppMain getApp() {
    return getApp(getContex());
  }

  public RxSnappyClient getRxSnappyClient() {
    return rxSnappyClient;
  }

  public void initRxDb() {
    // Logger.e("Appmain InitDB");
    Log.e("AppMain", "initDB");
    if (getContex() != null) {
      RxSnappy.init(getContex());
      if (rxSnappyClient == null) rxSnappyClient = new RxSnappyClient();
    }
  }

  @Override public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    initRxDb();
    FacebookSdk.sdkInitialize(this.getApplicationContext());
    Logger.init(getString(R.string.app_name))
        .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);

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

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    @Override
    public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
    }
  }
}
