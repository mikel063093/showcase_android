package com.co.showcase.ui.categoria;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.co.showcase.ui.perfil.perfil;
import com.co.showcase.ui.slide.slide;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class categoria extends BaseActivity implements SearchView.OnQueryTextListener {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.rv_home) RecyclerView rvHome;

  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.txt_section) AppCompatTextView txtSection;
  private MenuItem searchItem;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.categoria_layout);
    ButterKnife.bind(this);
    setupToolbar();
    setupSlider();
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      Categoria categoria = AppMain.getGson().fromJson(json, Categoria.class);
      Usuario user = getUserSync();
      getDetalleCategoria(user, categoria);
    }
  }

  private void getDetalleCategoria(Usuario usuario, Categoria categoria) {
    if (usuario.getToken().length() > 2) {
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

  private void updateUi(ResponseCategoriaDetalle categoria) {
    if (categoria.getEstado() == 1) {
      setTupRecyclerView(categoria.getCategorias());
      txtSection.setText(categoria.getCategorias().getNombre());
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  private void setTupRecyclerView(Categoria categoria) {
    log(categoria.getJson());
    establecimientoAdapter adapter =
        new establecimientoAdapter(this, categoria.getEstablecimientos());
    GridLayoutManager glm = new GridLayoutManager(this, 2);
    rvHome.setLayoutManager(glm);
    rvHome.setAdapter(adapter);
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
    SearchView searchView =
        (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
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
    searchView.setOnSearchClickListener(v -> {
      Log("openSearch");
      setItemsVisibility(menu, searchItem, false);
    });
    searchView.setOnCloseListener(() -> {
      Log("closeSearch");
      setItemsVisibility(menu, searchItem, true);
      return false;
    });
    return true;
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    switch (item.getItemId()) {
      //case R.id.action_buy:
      //  log("action buy");
      //  //Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();
      //  return true;

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

  @Override public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    return false;
  }
}
