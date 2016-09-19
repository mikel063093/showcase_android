package com.co.showcase.ui.direccion;

import android.content.Context;
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
import com.co.showcase.R;
import com.co.showcase.model.Direccion;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by home on 16/09/16.
 */

public class adapterDireccion extends RecyclerView.Adapter<adapterDireccion.ViewHolder> {

  private Context mContext;
  private List<Direccion> mData;
  private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

  public adapterDireccion(Context mContext, List<Direccion> mData) {
    Logger.e("size --> " + mData.size());
    this.mContext = mContext;
    if (mData != null) {
      this.mData = mData;
    } else {
      this.mData = new ArrayList<>();
    }
  }

  public Observable<Integer> getPositionClicks() {
    return onClickSubject.asObservable();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(mContext).inflate(R.layout.item_direccion_min, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, final int position) {
    Logger.e("postion " + position);
    assert mData != null;
    Direccion item = mData.get(position);
    if (item != null) {
      holder.txtDireccion.setText(
          String.format("%s %s # %s ", item.getTipo(), item.getNomenclatura(), item.getNumero()));
      holder.txtNombreDireccion.setText(item.getNombre());

      holder.txtDireccion.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtNombreDireccion.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.rootSection.setOnClickListener(view -> onClickSubject.onNext(position));
    }
  }

  @Override public int getItemCount() {
    assert mData != null;
    return mData.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.flecha_izq) ImageView flechaIzq;
    @Bind(R.id.txt_direccion) AppCompatTextView txtDireccion;
    @Bind(R.id.txt_nombre_direccion) AppCompatTextView txtNombreDireccion;
    @Bind(R.id.root_section) CardView rootSection;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
