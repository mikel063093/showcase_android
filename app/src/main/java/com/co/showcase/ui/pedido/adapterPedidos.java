package com.co.showcase.ui.pedido;

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
import com.co.showcase.R;
import com.co.showcase.model.Carrito;
import com.squareup.picasso.Picasso;
import java.util.List;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by home on 24/09/16.
 */

public class adapterPedidos extends RecyclerView.Adapter<adapterPedidos.ViewHolder> {
  private List<Carrito.ItemsBean> items;
  private Context context;
  private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

  public adapterPedidos(List<Carrito.ItemsBean> items, Context context) {
    this.items = items;
    this.context = context;
  }

  @NonNull public Observable<Integer> getPositionClicks() {
    return onClickSubject.asObservable();
  }

  @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(context).inflate(R.layout.item_carrito_de_pedidos, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Carrito.ItemsBean item = items.get(position);
    if (item != null) {
      holder.txtCantidad.setText(item.cantidad + "");
      holder.txtNameItem.setText(item.nombre);
      holder.txtPrecioItem.setText(item.precio + "");
      Picasso.with(context).load(item.imagen).fit().into(holder.imgItem);

      holder.imgItem.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtNameItem.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtCantidad.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtPrecioItem.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.rootSection.setOnClickListener(view -> onClickSubject.onNext(position));
    }
  }

  @Override public int getItemCount() {
    return items != null ? items.size() : 0;
  }

  static

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Nullable @Bind(R.id.txt_cantidad) AppCompatTextView txtCantidad;
    @Nullable @Bind(R.id.img_item) ImageView imgItem;
    @Nullable @Bind(R.id.txt_name_item) AppCompatTextView txtNameItem;
    @Nullable @Bind(R.id.txt_precio_item) AppCompatTextView txtPrecioItem;
    @Nullable @Bind(R.id.root_section) CardView rootSection;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
