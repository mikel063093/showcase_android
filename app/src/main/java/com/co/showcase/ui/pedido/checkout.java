package com.co.showcase.ui.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Carrito;
import com.co.showcase.model.Direccion;
import com.co.showcase.model.RequestPedido;
import com.co.showcase.model.ResponseCupon;
import com.co.showcase.model.ResponseDirecciones;
import com.co.showcase.model.ResponseRealizarPedido;
import com.co.showcase.model.ResponseVerCarrito;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.direccion.direcciones;
import com.co.showcase.ui.home.home;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx_activity_result.RxActivityResult;

public class checkout extends BaseActivity {

  @Nullable @Bind(R.id.toolbar) Toolbar toolbar;
  @Nullable @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Nullable @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Nullable @Bind(R.id.edt_direccion) AppCompatEditText edtDireccion;
  @Nullable @Bind(R.id.edt_telefono) AppCompatEditText edtTelefono;
  @Nullable @Bind(R.id.edt_pago) MaterialBetterSpinner edtPago;
  @Nullable @Bind(R.id.edt_cupon) AppCompatEditText edtCupon;
  @Nullable @Bind(R.id.txt_subtotal) AppCompatTextView txtSubtotal;
  @Nullable @Bind(R.id.txt_domicilio) AppCompatTextView txtDomicilio;
  @Nullable @Bind(R.id.txt_total_final) AppCompatTextView txtTotalFinal;
  @Nullable @Bind(R.id.root) LinearLayout root;
  private ResponseCupon cupon;
  private Direccion direccion;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkout);
    ButterKnife.bind(this);
    configBackToolbar(toolbar);
    init(getUserSync());
    edtCupon.setOnTouchListener((view, motionEvent) -> {
      log("click cupon");
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) selectCupon();

      return true;
    });
    edtDireccion.setOnTouchListener((view, motionEvent) -> {
      log("click direccion");
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) selectDireccion();
      return true;
    });
  }

  @Nullable private ResponseVerCarrito getPedido() {
    ResponseVerCarrito result = null;
    if (getIntent() != null
        && getIntent().getExtras() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      result = AppMain.getGson()
          .fromJson(getIntent().getStringExtra(this.getClass().getSimpleName()),
              ResponseVerCarrito.class);
    }
    return result;
  }

  private void getDirecciones(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", usuario.getId());

      REST.getRest()
          .direcciones(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesDirecciones, this::errControl);
    }
  }

  private void realizarPedido(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("datosPedido", getPedidoJson());
      REST.getRest()
          .realizarPedido(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesPedido, this::errControl);
    }
  }

  private String getPedidoJson() {
    RequestPedido pedido = new RequestPedido();
    pedido.setDireccion(direccion != null ? direccion.getId() : -1);
    if (!TextUtils.isEmpty(edtCupon.getText()) && cupon != null) {
      pedido.setCupon(cupon.getId() + "");
    }
    pedido.setFormaPago(edtPago.getText().toString());
    pedido.setTelefono(edtTelefono.getText().toString());
    ResponseVerCarrito carrito = getPedido();
    if (carrito != null) {
      List<RequestPedido.ItemsBean> items = new ArrayList<>();
      for (Carrito.ItemsBean item : carrito.getCarrito().getItems()) {
        RequestPedido.ItemsBean i = new RequestPedido.ItemsBean();
        i.setCantidad(item.getCantidad());
        i.setId(item.getIdArticulo() + "");
        items.add(i);
      }
      pedido.setItems(items);
    }
    log(AppMain.getGson().toJson(pedido));

    return AppMain.getGson().toJson(pedido);
  }

  private void succesPedido(@NonNull ResponseRealizarPedido response) {
    dismissDialog();
    if (response.getEstado() == 1) {
      goActv(enviado.class, true);
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void succesDirecciones(@NonNull ResponseDirecciones responseDirecciones) {
    dismissDialog();
    if (responseDirecciones.getEstado() == 1 && responseDirecciones.direcciones.size() >= 1) {
      Direccion item = responseDirecciones.getDirecciones().get(0);
      this.direccion = item;
      String txtDireccion =
          String.format("%s %s # %s ", item.getTipo(), item.getNomenclatura(), item.getNumero());
      edtDireccion.setText(txtDireccion);
    }
  }

  private void init(@NonNull Usuario usuario) {

    getDirecciones(usuario);
    edtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
    edtApellido.setText(usuario.getApellido() != null ? usuario.getApellido() : "");
    edtTelefono.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
    ResponseVerCarrito carrito = getPedido();
    if (carrito != null) {
      txtDomicilio.setText(carrito.carrito.getDomicilio() + "");
      txtSubtotal.setText(carrito.carrito.getSubtotal() + "");
      txtTotalFinal.setText(carrito.carrito.getTotal() + "");
    }
    String[] list = getResources().getStringArray(R.array.formas_pago);
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);

    edtPago.setAdapter(adapter);

    edtPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        log("pago-> " + list[i]);
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }

  private void cancelearCarrito(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      REST.getRest()
          .cancelarCarrito(usuario.getToken(), new HashMap<>())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(responseVerCarrito -> {
            if (responseVerCarrito.getEstado() == 1) {
              goActv(home.class, true);
            }
          }, this::errControl);
    }
  }

  @OnClick({
      R.id.btn_siguiente, R.id.txt_cancelar_pedido
  }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_siguiente:
        //goActv(enviado.class, true);
        log(edtPago.getText().toString());
        if (validarCheckout()) {
          realizarPedido(getUserSync());
        } else {
          showErr(getString(R.string.datos_invalidos));
        }
        break;
      case R.id.txt_cancelar_pedido:
        showMaterialDialog(getString(R.string.seguro_cancelar), new onClickCallback() {
          @Override public void onPositive(boolean result) {
            cancelearCarrito(getUserSync());
          }

          @Override public void onDissmis() {

          }

          @Override public void onNegative(boolean result) {

          }
        });
        break;
    }
  }

  private boolean validarCheckout() {
    boolean result = true;
    if (TextUtils.isEmpty(edtNombre.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtApellido.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtDireccion.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtTelefono.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtPago.getText())) {
      result = false;
    }

    return result;
  }

  private void selectDireccion() {
    Intent intent = new Intent(this, direcciones.class);
    intent.putExtra(direcciones.class.getSimpleName(), "result");

    RxActivityResult.on(this).startIntent(intent).subscribe(result -> {
      Intent data = result.data();
      int resultCode = result.resultCode();
      if (resultCode == RESULT_OK) {
        String direccion = data.getStringExtra(direcciones.class.getSimpleName());
        Direccion item = AppMain.getGson().fromJson(direccion, Direccion.class);
        if (item != null) {
          this.direccion = item;
          String txtDireccion = String.format("%s %s # %s ", item.getTipo(), item.getNomenclatura(),
              item.getNumero());
          edtDireccion.setText(txtDireccion != null ? txtDireccion : edtDireccion.getText());
        }
      }
    });
  }

  private void selectCupon() {
    Intent intent = new Intent(this, cupon.class);
    intent.putExtra(cupon.class.getSimpleName(), "result");

    RxActivityResult.on(this).startIntent(intent).subscribe(result -> {
      Intent data = result.data();
      int resultCode = result.resultCode();
      if (resultCode == RESULT_OK) {
        String json = data.getStringExtra(cupon.class.getSimpleName());
        cupon = AppMain.getGson().fromJson(json, ResponseCupon.class);
        if (cupon != null && cupon.getEstado() == 1) {
          assert edtCupon != null;
          edtCupon.setText(cupon.getMensaje());
          Double current_payment = Double.parseDouble(txtTotalFinal.getText().toString());
          Double descuento = current_payment - Double.parseDouble(cupon.getValor() + "");
          txtTotalFinal.setText(descuento.intValue() + "");
        }
      }
    });
  }
}
