package com.co.showcase.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.Establecimiento;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by home on 6/07/16.
 */

public class EstablecimientoAdapter
    extends RecyclerView.Adapter<EstablecimientoAdapter.ViewHolder> {

  private final Context mContext;
  @Nullable private List<Establecimiento> mData;

  public void add(Establecimiento s, int position) {
    position = position == -1 ? getItemCount() : position;
    mData.add(position, s);
    notifyItemInserted(position);
  }

  public void remove(int position) {
    if (position < getItemCount()) {
      mData.remove(position);
      notifyItemRemoved(position);
    }
  }

  public EstablecimientoAdapter(Context context, @Nullable List<Establecimiento> data) {
    mContext = context;
    if (data != null) {
      mData = data;
    } else {
      mData = new ArrayList<>();
    }
  }

  @NonNull public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(mContext).inflate(R.layout.item_general, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    assert mData != null;
    Establecimiento establecimiento = mData.get(position);
    if (establecimiento != null) {
      assert holder.txtItemGeneral != null;
      holder.txtItemGeneral.setText(establecimiento.getNombre());
      Picasso.with(mContext).load(establecimiento.getUrlImagen().get(0)).fit().into(holder.imageView5);
      assert holder.rootSection != null;
      holder.rootSection.setOnClickListener(view -> log(establecimiento.toJson()));
    }
  }

  private void log(String txt) {
    if (BuildConfig.DEBUG) {
      Logger.e(txt);
    }
  }

  @Override public int getItemCount() {
    return mData.size();
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