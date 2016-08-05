package com.co.showcase.ui.establecimiento;

import android.content.Context;
import android.content.Intent;
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
import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by home on 5/08/16.
 */

public class establecimientoAdapter
    extends RecyclerView.Adapter<establecimientoAdapter.ViewHolder> {

  private Context mContext;
  @Nullable private List<Establecimiento> mData;

  public void add(Establecimiento s, int position) {
    position = position == -1 ? getItemCount() : position;
    assert mData != null;
    mData.add(position, s);
    notifyItemInserted(position);
  }

  public void remove(int position) {
    if (position < getItemCount()) {
      assert mData != null;
      mData.remove(position);
      notifyItemRemoved(position);
    }
  }

  public establecimientoAdapter(Context context, @Nullable List<Establecimiento> data) {
    mContext = context;
    if (data != null) {
      mData = data;
    } else {
      mData = new ArrayList<>();
    }
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(mContext).inflate(R.layout.item_general, parent, false);
    return new establecimientoAdapter.ViewHolder(view);
  }

  private void goEstablicimientoDetail(Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) mContext;
    Usuario user = ac.getUserSync();
    if (user != null) getEstablecimientoDetalle(user, establecimiento);
  }

  private void getEstablecimientoDetalle(Usuario usuario, Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) mContext;
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
            Intent i = new Intent(mContext, establecimiento.class);
            i.putExtra(establecimiento.class.getSimpleName(), AppMain.getGson().toJson(e1));
            ac.goActv(i, false);
          }, ac::errControl);
    }
  }

  @Override public void onBindViewHolder(@NonNull establecimientoAdapter.ViewHolder holder,
      final int position) {
    assert mData != null;
    Establecimiento item = mData.get(position);
    if (item != null) {
      //assert holder.txtItemGeneral != null;
      holder.txt.setText(item.getNombre());
      Picasso.with(mContext).load(item.getUrlImagen()).fit().into(holder.img);
      //assert holder.rootSection != null;
      holder.rootSection.setOnClickListener(view -> goEstablicimientoDetail(item));
    }
  }

  private void log(String txt) {
    if (BuildConfig.DEBUG) {
      Logger.e(txt);
    }
  }

  @Override public int getItemCount() {
    return mData != null ? mData.size() : 0;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.imageView5) ImageView img;
    @Bind(R.id.txt_item_general) AppCompatTextView txt;
    @Bind(R.id.root_section) CardView rootSection;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
