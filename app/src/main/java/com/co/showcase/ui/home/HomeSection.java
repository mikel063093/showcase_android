package com.co.showcase.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Establecimiento;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import java.util.List;

/**
 * Created by home on 6/07/16.
 */

class HomeSection extends StatelessSection {

  Categoria categoria;
  List<Establecimiento> list;
  Context context;

  public HomeSection(Context context, Categoria categoria, List<Establecimiento> list) {
    super(R.layout.section_item, R.layout.item_general);
    this.categoria = categoria;
    this.list = list;
    this.context = context;
  }

  @Override public int getContentItemsTotal() {
    return list != null ? list.size() : 0;
  }

  @NonNull @Override public RecyclerView.ViewHolder getItemViewHolder(@NonNull View view) {
    return new ViewHolder(view);
  }

  @Override public void onBindItemViewHolder(RecyclerView.ViewHolder holder_, int position) {
    final ViewHolder holder = (ViewHolder) holder_;
    Establecimiento establecimiento = list.get(position);
    if (establecimiento != null) {
      holder.txtItemGeneral.setText(establecimiento.getNombre());
      Picasso.with(context).load(establecimiento.getUrlImagen()).fit().into(holder.imageView5);
      holder.rootSection.setOnClickListener(view -> log(establecimiento.toJson()));
    }
  }

  private void log(String txt) {
    if (BuildConfig.DEBUG) {
      Logger.e(txt);
    }
  }

  @NonNull @Override public RecyclerView.ViewHolder getHeaderViewHolder(@NonNull View view) {
    return new SectionViewHolder(view);
  }

  @Override public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder_) {
    SectionViewHolder holder = (SectionViewHolder) holder_;
    holder.txtSection.setText(categoria.getNombre());
    holder.rootSection.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        log(categoria.toJson());
      }
    });
  }

  public static class SectionViewHolder extends RecyclerView.ViewHolder {

    @Nullable @Bind(R.id.txt_section) AppCompatTextView txtSection;
    @Nullable @Bind(R.id.btn_section) AppCompatButton btnSection;
    @Nullable @Bind(R.id.root_section) CardView rootSection;

    SectionViewHolder(@NonNull View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Nullable @Bind(R.id.imageView5) ImageView imageView5;
    @Nullable @Bind(R.id.txt_item_general) AppCompatTextView txtItemGeneral;
    @Nullable @Bind(R.id.root_section) CardView rootSection;

    ViewHolder(@NonNull View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}



