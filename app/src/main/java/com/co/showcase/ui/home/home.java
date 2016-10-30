package com.co.showcase.ui.home;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.co.showcase.ui.slide.slide;
import com.co.showcase.ui.util.ItemDecorationAlbumColumns;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import retrofit2.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class home extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Nullable @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Nullable @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  @Nullable @Bind(R.id.rv_home) RecyclerView mRecyclerView;
  @Nullable @Bind(R.id.drawer) RelativeLayout drawer;
  @Nullable @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;

  private SectionedRecyclerViewAdapter sectionAdapter;
  private GoogleApiClient client;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    ButterKnife.bind(this);
    init(getUserSync());
    setToolbarPretty(true);
    setupToolbar(toolbar);
    setupSlider();
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  @Override public void onStart() {
    super.onStart();
    client.connect();
    AppIndex.AppIndexApi.start(client, getIndexApiAction());
  }

  @Override protected void onResume() {
    super.onResume();
    EventBus.getDefault().post(getUserSync());
  }

  @Override public void onStop() {
    super.onStop();
    AppIndex.AppIndexApi.end(client, getIndexApiAction());
    client.disconnect();
  }

  private void init(@NonNull Usuario userSync) {
    getEstblecimientos(userSync);
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

  private void setTupRecyclerView(@NonNull ResponseHome response) {
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
    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerView.setLayoutManager(glm);
    mRecyclerView.addItemDecoration(
        new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen._6sdp),
            getResources().getInteger(R.integer.photo_list_preview_columns)));
    mRecyclerView.setAdapter(sectionAdapter);
  }

  private void getEstblecimientos(@NonNull Usuario usuario) {
    if (usuario != null && usuario.getToken() != null && usuario.getToken().length() > 2) {
      Map<String, String> param = new HashMap<>();
      param.put("id", usuario.getId());
      REST.getRest()
          .establecimientos(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesEstablecimiento, throwable -> {
            dismissDialog();
            if (throwable instanceof HttpException) {
              log("F** error" + throwable.getMessage());
              dismissDialog();
              Answers.getInstance()
                  .logCustom(new CustomEvent(NETWORK_ERROR_KEY).putCustomAttribute("ERROR",
                      throwable.getMessage()));

              showMaterialDialog(getString(R.string.intenar_de_nuevo), new onClickCallback() {
                @Override public void onPositive(boolean result) {
                  init(getUserSync());
                }

                @Override public void onDissmis() {

                }

                @Override public void onNegative(boolean result) {

                }
              });
            }
            if (throwable instanceof IOException) {
              log("F** error" + throwable.getMessage());
              dismissDialog();
              Answers.getInstance()
                  .logCustom(new CustomEvent(NETWORK_ERROR_KEY).putCustomAttribute("ERROR",
                      throwable.getMessage()));
              showMaterialDialog(getString(R.string.intenar_de_nuevo), new onClickCallback() {
                @Override public void onPositive(boolean result) {
                  init(getUserSync());
                }

                @Override public void onDissmis() {

                }

                @Override public void onNegative(boolean result) {

                }
              });
            }
          });
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

        renderSlideImagesSlide(img);
      }
      setTupRecyclerView(responseHome);
    } else {
      showErr(responseHome.getMensaje());
    }
  }

  private void renderSlideImagesSlide(@NonNull List<Slides> imgs) {
    if (imgs != null && imgs.size() >= 1) {
      log("renderSlides");
      SlideAdapter adapter = new SlideAdapter(this, imgs);
      assert viewPagerSlide != null;
      viewPagerSlide.setAdapter(adapter);
      assert indicatorSlides != null;
      indicatorSlides.setViewPager(viewPagerSlide);
    }
  }

  private void renderSlideImages(@NonNull List<String> imgs) {
    if (imgs != null && imgs.size() >= 1) {
      log("renderSlides");
      SlideAdapter adapter = new SlideAdapter(this, imgs, false);
      assert viewPagerSlide != null;
      viewPagerSlide.setAdapter(adapter);
      assert indicatorSlides != null;
      indicatorSlides.setViewPager(viewPagerSlide);
    }
  }

  @NonNull public Action getIndexApiAction() {
    Thing object =
        new Thing.Builder().setName("home Page").setUrl(Uri.parse(getString(R.string.url))).build();
    return new Action.Builder(Action.TYPE_VIEW).setObject(object)
        .setActionStatus(Action.STATUS_TYPE_COMPLETED)
        .build();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
  }
}
