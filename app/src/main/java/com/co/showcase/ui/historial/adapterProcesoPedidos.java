package com.co.showcase.ui.historial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.model.Pedido;
import com.orhanobut.logger.Logger;
import java.util.List;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by home on 26/09/16.
 */

public class adapterProcesoPedidos extends RecyclerView.Adapter<adapterProcesoPedidos.ViewHolder> {

  private Context context;
  private List<Pedido> pedidos;
  private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

  public adapterProcesoPedidos(Context context, List<Pedido> pedidos) {
    this.context = context;
    this.pedidos = pedidos;
  }

  @NonNull public Observable<Integer> getPositionClicks() {
    return onClickSubject.asObservable();
  }

  @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view =
        LayoutInflater.from(context).inflate(R.layout.item_estadp_pedido, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Pedido item = pedidos.get(position);
    if (item != null) {
      holder.txtNumeroPedido.setText(
          context.getString(R.string.pedido_contaodr, item.getId() + ""));
      holder.txtFechaPedido.setText(
          context.getString(R.string.pedido_fecha, item.getFechaCreacion()));

      String estado =
          "<font color=#9b9b9b>" + context.getString(R.string.estado_color) + "</font> ";
      Logger.e((estado + " " + item.estado));
      Spanned spanned = Html.fromHtml(estado + item.estado);

      Logger.e(spanned.toString());
      holder.txtEstadoPedido.setText(spanned);

      holder.rootSection.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtEstadoPedido.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtFechaPedido.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.txtNumeroPedido.setOnClickListener(view -> onClickSubject.onNext(position));
      holder.flechaIzq.setOnClickListener(view -> onClickSubject.onNext(position));
    }
  }

  @Override public int getItemCount() {
    return pedidos != null && pedidos.size() > 0 ? pedidos.size() : 0;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    @Nullable @Bind(R.id.flecha_izq) ImageView flechaIzq;
    @Nullable @Bind(R.id.txt_numero_pedido) AppCompatTextView txtNumeroPedido;
    @Nullable @Bind(R.id.txt_fecha_pedido) AppCompatTextView txtFechaPedido;
    @Nullable @Bind(R.id.txt_estado_pedido) TextView txtEstadoPedido;
    @Nullable @Bind(R.id.root_section) CardView rootSection;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
