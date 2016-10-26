package com.co.showcase.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import com.co.showcase.AppMain;
import com.co.showcase.utils.JSONUtils;
import com.co.showcase.BuildConfig;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

import rx.schedulers.Schedulers;

/**
 * Created by miguelalegria on 1/22/16 for epass.
 */
public abstract class BaseModel {







  public abstract String getTag();

  public void save() {

  }

  private static void Log(String txt) {
    if (BuildConfig.DEBUG) Logger.e(txt);
  }

  @Nullable public Map<String, Object> jsonToMap(@NonNull String json) {
    try {
      JSONObject jsonObject = new JSONObject(json);
      return jsonToMap(jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
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

  @NonNull public Map<String, Object> jsonToMap(@NonNull JSONObject json) throws JSONException {
    Map<String, Object> retMap = new HashMap<>();
    if (json != JSONObject.NULL) {
      retMap = JSONUtils.toMap(json);
    }
    return retMap;
  }





  public String toJson() {
    return new Gson().toJson(this);
  }

  String getClassName() {
    return this.getClass().getSimpleName();
  }
}
