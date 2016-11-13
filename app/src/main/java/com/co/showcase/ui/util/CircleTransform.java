package com.co.showcase.ui.util;

/**
 * Created by home on 24/06/16.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.support.annotation.NonNull;
import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {
  private final int BORDER_COLOR = Color.WHITE;
  private int BORDER_RADIUS = 10;
  int color;

  public CircleTransform(float diemen) {
    BORDER_RADIUS = Math.round(diemen);
    color = Color.parseColor("#ff50e3c2");
  }

  public CircleTransform(float diemen, String color) {
    BORDER_RADIUS = Math.round(diemen);
    this.color = Color.parseColor(color);
  }

  @Override public Bitmap transform(@NonNull Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());

    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;

    Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
    if (squaredBitmap != source) {
      source.recycle();
    }
    //#ff50e3c2

    Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    BitmapShader shader =
        new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
    paint.setShader(shader);
    paint.setAntiAlias(true);
    //paint.setStyle(Paint.Style.STROKE);
    float r = size / 2f;
    // Prepare the background
    Paint paintBg = new Paint();
    paintBg.setColor(color);
    paintBg.setAntiAlias(true);

    // Draw the background circle
    canvas.drawCircle(r, r, r, paintBg);

    // Draw the image smaller than the background so a little border will be seen
    canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

    squaredBitmap.recycle();
    return bitmap;
  }

  @NonNull @Override public String key() {
    return "circle";
  }
}

