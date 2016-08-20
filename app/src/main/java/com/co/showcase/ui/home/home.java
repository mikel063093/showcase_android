package com.co.showcase.ui.home;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.co.showcase.model.ResponseHome;
import com.co.showcase.model.Slides;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import com.co.showcase.ui.map.map;
import com.co.showcase.ui.perfil.perfil;
import com.co.showcase.ui.slide.slide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class home extends BaseActivity implements SearchView.OnQueryTextListener {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Nullable @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  @Nullable @Bind(R.id.rv_home) RecyclerView mRecyclerView;
  @Nullable @Bind(R.id.drawer) RelativeLayout drawer;
  @Nullable @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;

  private SectionedRecyclerViewAdapter sectionAdapter;
  private SearchView searchView;
  private MenuItem searchItem;
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    ButterKnife.bind(this);

    Usuario.getItem()
        .compose(this.bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::getEstblecimientos);
    setupToolbar();
    setupSlider();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    getMenuInflater().inflate(R.menu.menu_main, menu);
    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    searchItem = menu.findItem(R.id.action_search);
    MenuItemCompat.setOnActionExpandListener(searchItem,
        new MenuItemCompat.OnActionExpandListener() {
          @Override public boolean onMenuItemActionCollapse(MenuItem item) {
            Log("closeMenuSearch");
            setItemsVisibility(menu, searchItem, true);
            return true;
          }

          @Override public boolean onMenuItemActionExpand(MenuItem item) {
            return true;
          }
        });
    searchView.setOnQueryTextListener(this);
    searchView.setIconifiedByDefault(true);
    searchView.setSubmitButtonEnabled(false);
    searchView.setOnSearchClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Log("openSearch");
        setItemsVisibility(menu, searchItem, false);
      }
    });
    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override public boolean onClose() {
        Log("closeSearch");
        setItemsVisibility(menu, searchItem, true);
        return false;
      }
    });
    return true;
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    switch (item.getItemId()) {
      //case R.id.action_buy:
      //  log("action buy");
      //  //Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
      //  return true;
      case R.id.action_map:
        goActv(map.class, false);
        break;
      case R.id.action_search:
        log("action search");
        //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.action_perfil:
        goActv(perfil.class, false);
        break;
      case R.id.action_salir:

        showMaterialDialog(getString(R.string.salir), new onClickCallback() {
          @Override public void onPositive(boolean result) {

          }

          @Override public void onDissmis() {

          }

          @Override public void onNegative(boolean result) {

          }
        });
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setTupRecyclerView(ResponseHome response) {
    log(response.toJson());
    sectionAdapter = new SectionedRecyclerViewAdapter();
    if (response.getCategorias() != null
        && response.getCategorias().get(0) != null
        && response.getCategorias().get(0).getEstablecimientos() != null
        && response.getCategorias().get(0).getEstablecimientos().size() > 0) {

      for (Categoria categoria : response.getCategorias()) {
        sectionAdapter.addSection(
            new HomeSection(this, categoria, categoria.getEstablecimientos()));
      }
    }

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
  }

  private void getEstblecimientos(@NonNull Usuario usuario) {
    if (usuario.getToken().length() > 2) {
      Map<String, String> param = new HashMap<>();
      param.put("id", usuario.getId());
      REST.getRest()
          .establecimientos(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesEstablecimiento, this::errControl);
    }
  }

  private void succesEstablecimiento(@NonNull ResponseHome responseHome) {
    dismissDialog();
    if (responseHome.getEstado().equalsIgnoreCase("exito")) {
      if (responseHome.getPromociones() != null) {
        renderSlideImages(responseHome.getPromociones());
      } else {

        List<Slides> img = new ArrayList<>();
        img.add(new Slides("http://afindemes.republica.com/files/2012/03/Imagen-2.png", "1"));
        img.add(new Slides("http://media.cuponofertas.com.mx/2014/04/levis-rebajas-abril-2014.jpg",
            "2"));

        renderSlideImages(img);
      }
      setTupRecyclerView(responseHome);
    } else {
      showErr(responseHome.getMensaje());
    }
  }

  private void renderSlideImages(@NonNull List<Slides> imgs) {
    if (imgs != null && imgs.size() >= 1) {
      log("renderSlides");
      SlideAdapter adapter = new SlideAdapter(this, imgs);
      assert viewPagerSlide != null;
      viewPagerSlide.setAdapter(adapter);
      assert indicatorSlides != null;
      indicatorSlides.setViewPager(viewPagerSlide);
    }
  }

  @Override public boolean onQueryTextSubmit(String query) {
    Log(query);
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    Log(newText);
    return false;
  }

  @Override public void onBackPressed() {
    if (searchView.isShown()) {
      searchItem.collapseActionView();
      searchView.setQuery("", false);
    } else {
      super.onBackPressed();
    }
  }

  public Action getIndexApiAction() {
    Thing object =
        new Thing.Builder().setName("home Page") // TODO: Define a title for the content shown.
            // TODO: Make sure this auto-generated URL is correct.
            .setUrl(Uri.parse("https://showcase.com.co")).build();
    return new Action.Builder(Action.TYPE_VIEW).setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
  }

  @Override public void onStart() {
    super.onStart();
    client.connect();
    AppIndex.AppIndexApi.start(client, getIndexApiAction());
  }

  @Override public void onStop() {
    super.onStop();
    AppIndex.AppIndexApi.end(client, getIndexApiAction());
    client.disconnect();
  }
}
