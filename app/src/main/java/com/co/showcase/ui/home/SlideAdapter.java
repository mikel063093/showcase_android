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
import com.co.showcase.model.Slides;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;

/**
 * Created by home on 30/06/16.
 */

public class SlideAdapter extends PagerAdapter {
  private Context context;
  private List<Slides> imagesUrl;
  private ViewHolder holder;

  public SlideAdapter(Context context, List<Slides> imagesUrl) {
    this.context = context;
    this.imagesUrl = imagesUrl;
  }

  @Override public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override public int getCount() {
    return imagesUrl.size();
  }

  @Override public View instantiateItem(@NonNull ViewGroup container, final int position) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.item_slide, container, false);
    holder = new ViewHolder(view);
    view.setTag(holder);
    Log.e("HOLDER", imagesUrl.get(position).getUrlImagen());
    Picasso.with(context).setLoggingEnabled(true);
    Picasso.with(context).load(imagesUrl.get(position).getUrlImagen()).into(holder.image_display);
    container.addView(view);
    return view;
  }

  public static class ViewHolder {
    @Bind(R.id.image_display) ImageView image_display;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
