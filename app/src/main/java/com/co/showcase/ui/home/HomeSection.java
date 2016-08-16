package com.co.showcase.ui.home;

import android.content.Context;
import android.content.Intent;
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
import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.establecimiento.establecimiento;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by home on 6/07/16.
 */

public class HomeSection extends StatelessSection {

  private Categoria categoria;
  private List<Establecimiento> list;
  private Context context;

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
      assert holder.txtItemGeneral != null;
      holder.txtItemGeneral.setText(establecimiento.getNombre());

      Picasso.with(context).load(establecimiento.getUrlImagen()).fit().into(holder.imageView5);
      assert holder.rootSection != null;
      holder.rootSection.setOnClickListener(view -> {
        log(establecimiento.toJson());
        goEstablicimientoDetail(establecimiento);
      });
    }
  }

  private void goEstablicimientoDetail(Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) context;
    Usuario user = ac.getUserSync();
    if (user != null) getEstablecimientoDetalle(user, establecimiento);
  }

  private void getEstablecimientoDetalle(Usuario usuario, Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) context;
    if (usuario.getToken().length() > 2) {
      Map<String, String> param = new HashMap<>();
      param.put("id", establecimiento.getId());
      REST.getRest()
          .establecimiento(usuario.getToken(), param)
          .compose(ac.bindToLifecycle())
          .doOnSubscribe(() -> ac.showDialog(ac.getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(ac::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(e1 -> {
            Intent i = new Intent(context, establecimiento.class);
            i.putExtra(establecimiento.class.getSimpleName(), AppMain.getGson().toJson(e1));
            ac.goActv(i, false);
          }, ac::errControl);
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
    assert holder.txtSection != null;
    holder.txtSection.setText(categoria.getNombre());
    assert holder.rootSection != null;
    holder.rootSection.setOnClickListener(view -> ((BaseActivity) context).goCategoria(categoria));
    assert holder.btnSection != null;
    holder.btnSection.setOnClickListener(view -> ((BaseActivity) context).goCategoria(categoria));
  }

  static class SectionViewHolder extends RecyclerView.ViewHolder {

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



