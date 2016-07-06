package com.co.showcase.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.ResponseHome;
import com.co.showcase.model.Slides;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class home extends BaseActivity implements Toolbar.OnMenuItemClickListener {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  @Bind(R.id.rv_home) RecyclerView mRecyclerView;
  private SlideAdapter adapter;
  private EstablecimientoAdapter mAdapter;
  private SectionedRecyclerViewAdapter sectionAdapter;
  // private SectionAdapter sectionAdapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    ButterKnife.bind(this);
    Usuario usuario = Usuario.GetItem();
    if (usuario != null) {
      // getEstblecimientos(usuario);
    }
    setTupRecyclerView();
    setupToolbar();
  }

  private void setupToolbar() {
    toolbar.setTitle(R.string.app_name);
    toolbar.inflateMenu(R.menu.menu_main);
    toolbar.setOnMenuItemClickListener(this);
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_buy:
        log("action buy");
        //Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
        return true;

      case R.id.action_search:
        log("action search");
        //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        return true;
    }

    return true;
  }

  private void setTupRecyclerView() {
    sectionAdapter = new SectionedRecyclerViewAdapter();
    Categoria categoria = new Categoria("Almacenes de ropa");
    sectionAdapter.addSection(new HomeSection(this, categoria, getDemoData()));
    sectionAdapter.addSection(new HomeSection(this, categoria, getDemoData()));
    sectionAdapter.addSection(new HomeSection(this, categoria, getDemoData()));

    GridLayoutManager glm = new GridLayoutManager(this, 2);
    glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        switch (sectionAdapter.getSectionItemViewType(position)) {
          case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
            return 2;
          default:
            return 1;
        }
      }
    });
    mRecyclerView.setLayoutManager(glm);
    mRecyclerView.setAdapter(sectionAdapter);
    //mRecyclerView.setHasFixedSize(false);
    //
    //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
    //mAdapter = new EstablecimientoAdapter(this, getDemoData());
    //
    //List<Categoria> sections = new ArrayList<>();
    //sections.add(new Categoria(0, "Almacenes de ropa"));
    //sections.add(new Categoria(2, "Almacenes de ropa"));
    //
    //sectionAdapter = new SectionAdapter(this, mAdapter);
    //Categoria[] dummy = new Categoria[sections.size()];
    //sectionAdapter.setSections(sections.toArray(dummy));
    //
    //mRecyclerView.setAdapter(sectionAdapter);
  }

  private List<Establecimiento> getDemoData() {
    List<Establecimiento> establecimientos = new ArrayList<>();
    Establecimiento establecimiento;
    for (int i = 0; i < 4; i++) {
      establecimiento = new Establecimiento();
      establecimiento.setNombre(i % 2 == 0 ? "Levis" : "Addidas");
      establecimiento.setId("" + i);
      establecimiento.setUrlImagen(i % 2 == 0
          ? "https://img.grouponcdn.com/coupons/dc6ZM97sA2uzsQoKxRbroC/levi-highres-500x500"
          : "https://media.base.net/manufacturers/adidas.png");
      establecimientos.add(establecimiento);
    }

    return establecimientos;
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
