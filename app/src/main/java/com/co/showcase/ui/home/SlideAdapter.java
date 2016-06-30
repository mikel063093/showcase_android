package com.co.showcase.ui.home;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.co.showcase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by home on 30/06/16.
 */

public class SlideAdapter extends PagerAdapter {
  Context context;
  ArrayList<String> imgaesUrl;
  private ViewHolder holder;

  public SlideAdapter(Context context, ArrayList<String> imgaesUrl) {
    this.context = context;
    this.imgaesUrl = imgaesUrl;
  }

  @Override public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public int getCount() {
    return imgaesUrl.size();
  }

  View view;
  LayoutInflater inflater;

  @Override public View instantiateItem(@NonNull ViewGroup container, final int position) {
    inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    view = inflater.inflate(R.layout.item_slide, container, false);
    holder = new ViewHolder(view);
    view.setTag(holder);
    Log.e("HOLDER", imgaesUrl.get(position));
    Picasso.with(context).setLoggingEnabled(true);
    Picasso.with(context).load(imgaesUrl.get(position)).into(holder.image_display);
    container.addView(view);
    return view;
  }

  private static class ViewHolder {
    @Bind(R.id.image_display) ImageView image_display;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
