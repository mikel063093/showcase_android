package com.co.showcase.ui.search_result;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.ResponseResultSearch;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.establecimiento.establecimientoItemsAdapter;
import com.co.showcase.ui.home.home;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class result extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.txt_section) AppCompatTextView txtSection;
  @Nullable @Bind(R.id.rv_result) RecyclerView rvResult;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.search_result);
    ButterKnife.bind(this);

    final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.btn_flechaizquierda);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    toolbar.setNavigationOnClickListener(v -> {
      goActv(home.class, true);
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
    log("onCreate");
    searchItem(getSearchItem(), getUserSync());
  }

  private void searchItem(@Nullable String searchItem, Usuario usuario) {
    log("searchItem");
    if (searchItem != null && usuario != null) {
      assert txtSection != null;
      txtSection.setText(searchItem);
      if (usuario.getToken() != null && usuario.getToken().length() > 0) {
        Map<String, Object> param = new HashMap<>();
        param.put("palabra", searchItem);
        REST.getRest()
            .realizarBusqueda(usuario.getToken(), param)
            .compose(bindToLifecycle())
            .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(this::dismissDialog)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::succes, this::errControl, () -> log("onComplete"));
      }
    }
  }

  private void succes(ResponseResultSearch responseResultSearch) {
    log("succes" + AppMain.getGson().toJson(responseResultSearch));
    if (responseResultSearch.estado == 1) {
      establecimientoItemsAdapter adapter =
          new establecimientoItemsAdapter(this, responseResultSearch.articulos);
      GridLayoutManager glm = new GridLayoutManager(this, 2);
      assert rvResult != null;
      rvResult.setLayoutManager(glm);
      rvResult.setAdapter(adapter);
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  @Nullable private String getSearchItem() {
    String result = null;
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      result = getIntent().getStringExtra(this.getClass().getSimpleName());
    }
    return result;
  }
}
