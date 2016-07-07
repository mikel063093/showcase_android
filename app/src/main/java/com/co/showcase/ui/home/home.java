package com.co.showcase.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.co.showcase.ui.slide.slide;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class home extends BaseActivity {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  @Bind(R.id.rv_home) RecyclerView mRecyclerView;
  @Bind(R.id.drawer) RelativeLayout drawer;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  private SlideAdapter adapter;
  //private EstablecimientoAdapter mAdapter;
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
    setupToolbar();
    setupSlider();
    setTupRecyclerView();
  }

  private void setupSlider() {
    getSupportFragmentManager().beginTransaction().replace(R.id.drawer, new slide()).commit();

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.open) {

          @Override public void onDrawerClosed(View drawerView) {
            // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
            super.onDrawerClosed(drawerView);
          }

          @Override public void onDrawerOpened(View drawerView) {
            // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

            super.onDrawerOpened(drawerView);
          }
        };
    toggle.setDrawerIndicatorEnabled(false);

    Drawable drawable =
        ResourcesCompat.getDrawable(getResources(), R.drawable.btn_menu_principal, getTheme());

    toggle.setHomeAsUpIndicator(drawable);
    toggle.setToolbarNavigationClickListener(v -> {
      if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START);
      } else {
        drawerLayout.openDrawer(GravityCompat.START);
      }
    });
    assert drawerLayout != null;
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState();
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

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
    return super.onOptionsItemSelected(item);
  }

  private void setTupRecyclerView() {
    sectionAdapter = new SectionedRecyclerViewAdapter();
    Categoria categoria = new Categoria("Almacenes de ropa");

    List<Categoria> sections = new ArrayList<>();
    sections.add(new Categoria(0, "Almacenes de ropa"));
    sections.add(new Categoria(2, "Almacenes de ropa"));
    EventBus.getDefault().post(sections);

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
