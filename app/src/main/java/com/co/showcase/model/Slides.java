package com.co.showcase.model;

import java.util.ArrayList;

/**
 * Created by home on 30/06/16.
 */

public class Slides extends BaseModel {
  ArrayList<String> imagenes;

  public ArrayList<String> getImagenes() {
    return imagenes;
  }

  @Override public String getTag() {
    return getClassName();
  }
}
