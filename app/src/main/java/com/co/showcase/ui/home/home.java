package com.co.showcase.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.ResponseHome;
import com.co.showcase.model.Slides;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class home extends BaseActivity {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  @Bind(R.id.rv_home) RecyclerView rvHome;
  private SlideAdapter adapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    ButterKnife.bind(this);
    Usuario usuario = Usuario.GetItem();
    if (usuario != null) {
      getEstblecimientos(usuario);
    }
  }

  private void getEstblecimientos(@NonNull Usuario usuario) {
    Map<String, String> param = new HashMap<>();
    param.put("id", usuario.getId());
    REST.getRest()
        .establecimientos(param)
        .compose(bindToLifecycle())
        .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
        .subscribeOn(Schedulers.io())
        .doOnCompleted(this::dismissDialog)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::succesEstablecimiento, this::errControl);
  }

  private void succesEstablecimiento(ResponseHome responseHome) {
    dismissDialog();
    if (responseHome.getEstado().equalsIgnoreCase("exito")) {
        renderSlideImages(responseHome.getPromociones());
    } else {
      showErr(responseHome.getMensaje());
    }
  }

  private void renderSlideImages(List<Slides> imgs) {
    adapter = new SlideAdapter(this, imgs);
    viewPagerSlide.setAdapter(adapter);
    indicatorSlides.setViewPager(viewPagerSlide);
  }
}
