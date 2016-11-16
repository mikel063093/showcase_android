package com.co.showcase.ui.slide;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.model.Categoria;
import com.co.showcase.ui.BaseActivity;
import java.util.List;

/**
 * Created by home on 7/07/16.
 */

public class SlideAdapter extends BaseAdapter {
  private List<Categoria> categorias;
  private Context context;
  @Nullable private ViewHolder holder;

  public SlideAdapter(List<Categoria> categorias, Context context) {
    this.categorias = categorias;
    this.context = context;
  }

  @Override public int getCount() {
    return categorias.size();
  }

  @Override public Object getItem(int i) {
    return categorias.get(i);
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Nullable @Override
  public View getView(int position, @Nullable View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater mInflater =
          (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
      convertView = mInflater.inflate(R.layout.item_list_slide, null);
    }
    holder = new ViewHolder(convertView);
    assert holder.txtItemSlide != null;
    //holder.txtItemSlide.setOnClickListener(view -> {
    //  assert holder.root != null;
    //  holder.root.performClick();
    //});
    //assert holder.root != null;
    //holder.root.setOnClickListener(view -> holder.txtItemSlide.performClick());
    Categoria categoria = categorias.get(position);
    if (categoria != null) {
      assert holder.txtItemSlide != null;
      holder.txtItemSlide.setText(categoria.getNombre());
      holder.txtItemSlide.setOnClickListener(view -> {
        if (categoria.getUrl() != null) {
          ((BaseActivity) context).openUrl(categoria.getUrl());
        } else {
          ((BaseActivity) context).goCategoria(categoria);
        }
      });
    }

    return convertView;
  }

  static class ViewHolder {
    @Nullable @Bind(R.id.txt_item_slide) AppCompatTextView txtItemSlide;
    @Nullable @Bind(R.id.root) LinearLayout root;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
