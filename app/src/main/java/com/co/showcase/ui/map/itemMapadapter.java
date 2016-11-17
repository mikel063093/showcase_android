package com.co.showcase.ui.map;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.establecimiento.establecimiento;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by home on 11/17/16.
 */

public class itemMapadapter extends RecyclerView.Adapter<itemMapadapter.ViewHolder> {
  private Context context;

  private List<Establecimiento> list;

  public itemMapadapter(Context context, List<Establecimiento> list) {
    this.context = context;
    this.list = list;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = LayoutInflater.from(context).inflate(R.layout.item_general, parent, false);
    return new itemMapadapter.ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Establecimiento establecimiento = list.get(position);
    if (establecimiento != null) {
      assert holder.txtItemGeneral != null;
      holder.txtItemGeneral.setText(establecimiento.getNombre());
      assert holder.rootBookmark != null;
      holder.rootBookmark.setVisibility(
          establecimiento.getMarcador() != null ? View.VISIBLE : View.GONE);
      assert holder.txtItemMap != null;
      holder.txtItemMap.setText(
          establecimiento.getMarcador() != null ? establecimiento.getMarcador() : "");
      Picasso.with(context)
          .load(establecimiento.getUrlImagen().get(0))
          .fit()
          .into(holder.imageView5);
      assert holder.rootSection != null;
      holder.rootSection.setOnClickListener(view -> {

        goEstablicimientoDetail(establecimiento);
      });
    }
  }
  private void goEstablicimientoDetail(@NonNull Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) context;
    Usuario user = ac.getUserSync();
    if (user != null) getEstablecimientoDetalle(user, establecimiento);
  }

  private void getEstablecimientoDetalle(Usuario usuario,
      @NonNull Establecimiento establecimiento) {
    BaseActivity ac = (BaseActivity) context;
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
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
            ac.log("ok " + AppMain.getGson().toJson(e1));
            Intent i = new Intent(context, com.co.showcase.ui.establecimiento.establecimiento.class);
            i.putExtra(establecimiento.class.getSimpleName(), AppMain.getGson().toJson(e1));
            ac.goActv(i, false);
          }, ac::errControl);
    }
  }

    @Override public int getItemCount () {
      return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      @Nullable @Bind(R.id.imageView5) ImageView imageView5;
      @Nullable @Bind(R.id.txt_item_general) AppCompatTextView txtItemGeneral;
      @Nullable @Bind(R.id.root_section) CardView rootSection;
      @Nullable @Bind(R.id.root_bookmark) RelativeLayout rootBookmark;
      @Nullable @Bind(R.id.txt_item_map) TextView txtItemMap;

      ViewHolder(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
      }
    }
  }

