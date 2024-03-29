package com.co.showcase.ui.categoria;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.usuario.ResponseCategoriaDetalle;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.establecimiento.establecimientoAdapter;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class categoria extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.rv_home) RecyclerView rvHome;

  @Nullable @Bind(R.id.txt_section) AppCompatTextView txtSection;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.categoria_layout);
    ButterKnife.bind(this);
    assert toolbar != null;
    configBackToolbar(toolbar);
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      Categoria categoria = AppMain.getGson().fromJson(json, Categoria.class);
      Usuario user = getUserSync();
      getDetalleCategoria(user, categoria);
    }
  }

  private void getDetalleCategoria(Usuario usuario, @NonNull Categoria categoria) {
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
      Map<String, Object> param = new HashMap<>();
      param.put("id", categoria.getId() + "");
      REST.getRest()
          .categoria(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::updateUi, this::errControl);
    }
  }

  private void updateUi(@NonNull ResponseCategoriaDetalle categoria) {
    if (categoria.getEstado() == 1) {
      setTupRecyclerView(categoria.getCategorias());
      assert txtSection != null;
      txtSection.setText(categoria.getCategorias().getNombre());
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void setTupRecyclerView(@NonNull Categoria categoria) {
    log(categoria.getJson());
    establecimientoAdapter adapter =
        new establecimientoAdapter(this, categoria.getEstablecimientos());
    GridLayoutManager glm = new GridLayoutManager(this, 2);
    assert rvHome != null;
    rvHome.setNestedScrollingEnabled(false);
    rvHome.setLayoutManager(glm);
    rvHome.setAdapter(adapter);
  }
}

