package com.co.showcase.ui.establecimiento;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
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
import com.co.showcase.model.Establecimiento;
import com.co.showcase.model.Slides;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import com.co.showcase.ui.home.SlideAdapter;
import com.co.showcase.ui.perfil.perfil;
import com.co.showcase.ui.slide.slide;
import java.util.ArrayList;
import java.util.List;

public class establecimiento extends BaseActivity implements SearchView.OnQueryTextListener {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorHome;
  @Bind(R.id.view_pager_home) ViewPager viewPagerHome;
  @Bind(R.id.txt_name_company) AppCompatTextView txtNameCompany;
  @Bind(R.id.txt_description) AppCompatTextView txtDescription;
  @Bind(R.id.txt_addres) AppCompatTextView txtAddres;
  @Bind(R.id.txt_phone) AppCompatTextView txtPhone;
  @Bind(R.id.txt_celphone) AppCompatTextView txtCelphone;
  @Bind(R.id.txt_email) AppCompatTextView txtEmail;
  @Bind(R.id.txt_website) AppCompatTextView txtWebsite;
  @Bind(R.id.btn_sahre_fb) ImageView btnSahreFb;
  @Bind(R.id.btn_sahre_tw) ImageView btnSahreTw;
  @Bind(R.id.ratingBar) RatingBar ratingBar;
  @Bind(R.id.rv_home) RecyclerView rvHome;
  @Bind(R.id.drawer) RelativeLayout drawer;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.share_general) ImageView shareGeneral;
  private SearchView searchView;
  private MenuItem searchItem;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.establecimiento);
    ButterKnife.bind(this);
    setupToolbar();
    setupSlider();
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

  private void updateUI(Establecimiento establecimiento) {
    txtNameCompany.setText(establecimiento.getNombre());
    txtDescription.setText(establecimiento.getDescripcion());
    txtAddres.setText(establecimiento.getDireccion());
    txtCelphone.setText(establecimiento.getTelefono());
    txtPhone.setText(establecimiento.getTelefono());
    txtEmail.setText("");
    txtWebsite.setText(establecimiento.getSitioWeb());
    ratingBar.setRating(establecimiento.getPuntuacion());
    List<String> imgs = new ArrayList<>();
    imgs.add(establecimiento.getUrlImagen());
    renderSlideImages(imgs);
    //txtDescription.setText();
  }

  @OnClick({ R.id.btn_sahre_fb, R.id.btn_sahre_tw, R.id.share_general })
  public void onClick(View view) {
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
