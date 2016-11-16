package com.co.showcase.ui.slide;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.co.showcase.AppMain;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.ResponseCategorias;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.BaseFragment;
import com.co.showcase.ui.util.CircleTransform;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



/**
 * Created by home on 7/07/16.
 */

public class slide extends BaseFragment {
  ImageView imgSliderPhoto;
  AppCompatTextView txtNamePerson;
  ListView menu;
  private SlideAdapter adapter;
  private BaseActivity base;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.slide_menu, container, false);
    //  ButterKnife.bind(this, view);
    imgSliderPhoto = (ImageView) view.findViewById(R.id.img_slider_photo);
    txtNamePerson = (AppCompatTextView) view.findViewById(R.id.txt_name_person);
    menu = (ListView) view.findViewById(R.id.menu);
    base = (BaseActivity) getActivity();
    updateUi(getUserSync());
    getCategorias(getUserSync());
    //Realm realm = base.getRealm();
    //Usuario usuario = realm.where(Usuario.class).findFirst();
    //updateUi(usuario);
    //usuario.addChangeListener(element -> {
    //  log("onChange user "+usuario.getNombre());
    //  updateUi(usuario);
    //});
    if (BuildConfig.DEBUG) setupList(new ArrayList<>(), base.getUserSync());
    return view;
  }

  @Override public void onResume() {
    super.onResume();
    if (!isOnPause) updateUi(getUserSync());
  }

  @Override public void onStart() {
    super.onStart();
    log("onStart");
    //updateUi(base.getUserSync());
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    log("onAttach");
  }

  private void updateUi(Usuario usuario) {
    log("updateui");
    if (usuario != null && usuario.getToken() != null && imgSliderPhoto != null && !isOnPause) {
      if (usuario.getFoto() != null && usuario.getFoto().length() > 0) {
        log("updateui ok");
        float diemen = getResources().getDimension(R.dimen._2sdp);
        Picasso.with(getContext())
            .load(usuario.getFoto())
            .fit()
            .centerCrop()
            .priority(Picasso.Priority.HIGH)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .transform(new CircleTransform(diemen,"#f8cb00"))
            .into(imgSliderPhoto);
        //Glide.with(this)
        //    .load(usuario.getFoto())
        //    .diskCacheStrategy(DiskCacheStrategy.ALL)
        //    .centerCrop()
        //    //.transform(new BitmapTransformation() {
        //    //  @Override
        //    //  protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth,
        //    //      int outHeight) {
        //    //    return null;
        //    //  }
        //    //
        //    //  @Override public String getId() {
        //    //    return null;
        //    //  }
        //    //})
        //    .into(imgSliderPhoto);

        //Picasso.with(imgSliderPhoto.getContext())
        //    .load(usuario.getFoto())
        //     .resize(MAX_WIDTH, MAX_HEIGHT)
        //     .onlyScaleDown()
        //    .priority(Picasso.Priority.HIGH)
        //    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        //    .into(imgSliderPhoto);
        //.into(new Target() {
        //  @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        //    log("onBitmapLoaded");
        //    if (bitmap != null && imgSliderPhoto != null ) {
        //      imgSliderPhoto.setImageBitmap(bitmap);
        //    } else {
        //      log("bitmap null");
        //    }
        //  }
        //
        //  @Override public void onBitmapFailed(Drawable errorDrawable) {
        //    log("onBitmapFailed");
        //  }
        //
        //  @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
        //    log("onPrepareLoad");
        //  }
        //});
      }
      assert txtNamePerson != null;
      txtNamePerson.setText(usuario.getFullName());
    }
  }

  private void getCategorias(@NonNull Usuario usuario) {
    REST.getRest()
        .categorias(usuario.getToken(), new HashMap<>())
        .compose(bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(responseCategorias -> {
          succesCategoria(responseCategorias, usuario);
        }, throwable -> {
          log(throwable.getMessage());
        });
  }

  private void succesCategoria(@NonNull ResponseCategorias responseCategorias, Usuario usuario) {
    log(responseCategorias.toJson());
    if (responseCategorias.getEstado().equalsIgnoreCase("1")
        && responseCategorias.getCategorias().size() > 1) {
      setupList(responseCategorias.getCategorias(), usuario);
    }
  }

  @Override public void onEvent(List<Categoria> categorias) {
    super.onEvent(categorias);
    log("onevent categorias");
    setupList(categorias, base.getUserSync());
  }

  private void setupList(List<Categoria> categorias, Usuario usuario) {
    if (usuario != null) {
      REST.getRest()
          .eventos(usuario.getToken())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(eventoCategoria -> {
            Categoria eventos = new Categoria();
            eventos.setId(
                eventoCategoria.getEstado() == 1 ? Integer.parseInt(eventoCategoria.getCategoria())
                    : -1);
            eventos.setNombre("Eventos");
            Categoria showcase = new Categoria();
            showcase.setId(0);
            showcase.setNombre(getString(R.string.msg_sitio_web));
            showcase.setUrl(getString(R.string.url));

            List<Categoria> finalList = new ArrayList<>();
            finalList.addAll(categorias);
            finalList.add(eventos);
            finalList.add(showcase);
            //finalList.addAll(categorias);

            log(AppMain.getGson().toJson(finalList));
            adapter = new SlideAdapter(finalList, getActivity());
            assert menu != null;
            menu.setAdapter(adapter);
          }, throwable -> {
            log(throwable.getMessage());
          });
    }
  }

  @Override public void onEvent(Usuario usuario) {
    super.onEvent(usuario);
    //if (!isOnPause) updateUi(getUserSync());
  }
}
