package com.co.showcase.ui.establecimiento;

import android.graphics.drawable.Drawable;
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
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.ResponsePuntuacion;
import com.co.showcase.model.Slides;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import com.co.showcase.ui.home.SlideAdapter;
import com.co.showcase.ui.perfil.perfil;
import com.co.showcase.ui.slide.slide;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class establecimiento extends BaseActivity implements SearchView.OnQueryTextListener {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.indicator_home) CirclePageIndicator indicatorHome;
  @Nullable @Bind(R.id.view_pager_home) ViewPager viewPagerHome;
  @Nullable @Bind(R.id.txt_name_company) AppCompatTextView txtNameCompany;
  @Nullable @Bind(R.id.txt_description) AppCompatTextView txtDescription;
  @Nullable @Bind(R.id.txt_addres) AppCompatTextView txtAddres;
  @Nullable @Bind(R.id.txt_phone) AppCompatTextView txtPhone;
  @Nullable @Bind(R.id.txt_celphone) AppCompatTextView txtCelphone;
  @Nullable @Bind(R.id.txt_email) AppCompatTextView txtEmail;
  @Nullable @Bind(R.id.txt_website) AppCompatTextView txtWebsite;
  @Nullable @Bind(R.id.btn_sahre_fb) ImageView btnSahreFb;
  @Nullable @Bind(R.id.btn_sahre_tw) ImageView btnSahreTw;
  @Nullable @Bind(R.id.ratingBar) SimpleRatingBar ratingBar;
  @Nullable @Bind(R.id.rv_home) RecyclerView rvHome;
  @Nullable @Bind(R.id.drawer) RelativeLayout drawer;
  @Nullable @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Nullable @Bind(R.id.share_general) ImageView shareGeneral;
  private SearchView searchView;
  private MenuItem searchItem;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.establecimiento);
    ButterKnife.bind(this);
    configBackToolbar(toolbar);
    if (getIntent() != null
        && getIntent().getStringExtra(this.getClass().getSimpleName()) != null) {
      String json = getIntent().getStringExtra(this.getClass().getSimpleName());
      Establecimiento establecimiento = AppMain.getGson().fromJson(json, Establecimiento.class);
      if (establecimiento != null) {
        updateUI(establecimiento);
      }
    }
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

  @Override public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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
      case R.id.action_search:
        log("action search");
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

  private void renderSlideImages(@NonNull List<String> imgs) {
    if (imgs.size() >= 1) {
      log("list logos" + imgs.size());
      SlideAdapter adapter = new SlideAdapter(this, imgs, true);
      assert viewPagerHome != null;
      viewPagerHome.setAdapter(adapter);
      assert indicatorHome != null;
      indicatorHome.setViewPager(viewPagerHome);
    }
  }

  private void updateUI(@NonNull Establecimiento establecimiento) {
    txtNameCompany.setText(establecimiento.getNombre());
    txtDescription.setText(establecimiento.getDescripcion());
    txtAddres.setText(establecimiento.getDireccion());
    txtCelphone.setText(establecimiento.getTelefono());
    txtPhone.setText(establecimiento.getTelefono());
    txtEmail.setText("");
    txtWebsite.setText(establecimiento.getSitioWeb());

    ratingBar.setRating(Float.parseFloat(establecimiento.getPuntuacion() + ""));
    btnSahreFb.setVisibility(establecimiento.getFacebook() != null ? View.VISIBLE : View.INVISIBLE);
    btnSahreFb.setOnClickListener(view -> openUrl(
        establecimiento.getFacebook() != null ? establecimiento.getFacebook() : ""));
    btnSahreTw.setVisibility(establecimiento.getTwitter() != null ? View.VISIBLE : View.INVISIBLE);
    btnSahreTw.setOnClickListener(
        view -> openUrl(establecimiento.getTwitter() != null ? establecimiento.getTelefono() : ""));
    renderSlideImages(establecimiento.getUrlImagen());
    shareGeneral.setOnClickListener(view -> share(establecimiento.getDescripcion()));
    establecimientoItemsAdapter adapter =
        new establecimientoItemsAdapter(this, establecimiento.getArticulos());
    GridLayoutManager glm = new GridLayoutManager(this, 2);
    rvHome.setLayoutManager(glm);
    rvHome.setAdapter(adapter);

    ratingBar.setOnRatingBarChangeListener(
        (ratingBar1, v, b) -> onRatingChange(v, establecimiento));
    //txtDescription.setText();
  }

  private void onRatingChange(float value, @NonNull Establecimiento establecimiento) {
    Usuario usuario = getUserSync();
    if (usuario.getToken().length() > 2) {
      Map<String, Object> param = new HashMap<>();
      param.put("valor", value);
      param.put("id", establecimiento.getId());
      REST.getRest()
          .puntuarEstablecimiento(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::onSuccesPuntuacion, this::errControl);
    }
  }

  private void onSuccesPuntuacion(@NonNull ResponsePuntuacion responsePuntuacion) {
    dismissDialog();
    if (responsePuntuacion.getEstado() == 1) {
      showMaterialDialog(getString(R.string.puntuacion_ok), new onClickCallback() {
        @Override public void onPositive(boolean result) {

        }

        @Override public void onDissmis() {

        }

        @Override public void onNegative(boolean result) {

        }
      });
    } else {
      showErr(getString(R.string.general_err));
    }
  }

  @OnClick({ R.id.btn_sahre_fb, R.id.btn_sahre_tw, R.id.share_general })
  public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.btn_sahre_fb:
        break;
      case R.id.btn_sahre_tw:
        break;
      case R.id.share_general:

        break;
    }
  }

  @Override public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    return false;
  }
}
