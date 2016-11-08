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
import com.co.showcase.model.Articulo;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.producto.producto;
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

public class establecimientoItemsAdapter
    extends RecyclerView.Adapter<establecimientoItemsAdapter.ViewHolder> {

  private Context mContext;
  @Nullable private List<Articulo> mData = new ArrayList<>();

  public void add(Articulo s, int position) {
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

  public establecimientoItemsAdapter(Context context, @Nullable List<Articulo> data) {
    mContext = context;
    mData = data;
  }

  @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(mContext).inflate(R.layout.item_establecimiento, parent, false);
    return new ViewHolder(view);
  }

  private void goArticuloDetalle(@NonNull Articulo articulo) {
    BaseActivity ac = (BaseActivity) mContext;
    Usuario user = ac.getUserSync();
    if (user != null) goArticuloDetalle(user, articulo);
  }

  private void goArticuloDetalle(@NonNull Usuario usuario, @NonNull Articulo articulo) {
    BaseActivity ac = (BaseActivity) mContext;
    if (usuario.getToken().length() > 2) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", articulo.getId());

      REST.getRest()
          .detalleProducto(usuario.getToken(), param)
          .compose(ac.bindToLifecycle())
          .doOnSubscribe(() -> ac.showDialog(ac.getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(ac::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(e1 -> {
            if (e1.getEstado() == 1) {
              Intent i = new Intent(mContext, producto.class);

              i.putExtra(producto.class.getSimpleName(),
                  AppMain.getGson().toJson(e1.getArticulo()));

              ac.goActv(i, false);
            } else {
              ac.showErr(mContext.getString(R.string.general_err));
            }
          }, ac::errControl);
    }
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    assert mData != null;
    Articulo item = mData.get(position);
    log(AppMain.getGson().toJson(item));

    if (item != null) {
      if (item.getImagen() != null
          && item.getImagen().size() > 0
          && item.getImagen().get(0) != null
          && item.getImagen().get(0).length() > 0) {
        String url = item.getImagen().get(0) != null && item.getImagen().get(0).length() > 1
            ? item.getImagen().get(0) : null;
        if (url != null) {
          Picasso.with(mContext).load(url).fit().into(holder.imgItem);
        }
      }
      holder.txtDescriptionItem.setText(item.getNombre());
      holder.txtPriceItem.setText("$" + item.getPrecio() + "");
      holder.txtUnitsItem.setText(item.getUnidades() + " " + item.getValorUnidades());
      holder.rootItemEstablecimiento.setOnClickListener(view -> goArticuloDetalle(item));
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
    @Nullable @Bind(R.id.img_item) ImageView imgItem;
    @Nullable @Bind(R.id.txt_description_item) AppCompatTextView txtDescriptionItem;
    @Nullable @Bind(R.id.txt_price_item) AppCompatTextView txtPriceItem;
    @Nullable @Bind(R.id.txt_units_item) AppCompatTextView txtUnitsItem;
    @Nullable @Bind(R.id.root_item_establecimiento) CardView rootItemEstablecimiento;

    ViewHolder(@NonNull View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
