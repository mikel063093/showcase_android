package com.co.showcase.ui.slide;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
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
import com.pkmmte.view.CircularImageView;
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
  @Nullable @Bind(R.id.img_slider_photo) CircularImageView imgSliderPhoto;
  @Nullable @Bind(R.id.txt_name_person) AppCompatTextView txtNamePerson;
  @Nullable @Bind(R.id.menu) ListView menu;
  private SlideAdapter adapter;
  private BaseActivity base;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.slide_menu, container, false);
    ButterKnife.bind(this, view);
    base = (BaseActivity) getActivity();
    updateUi(base.getUserSync());

    if (BuildConfig.DEBUG) setupList(new ArrayList<>(), base.getUserSync());
    return view;
  }

  private void updateUi(@Nullable Usuario usuario) {
    if (usuario != null && usuario.getToken() != null) {
      if (usuario.getFoto() != null && usuario.getFoto().length() > 0) {
        float diemen = getResources().getDimension(R.dimen._3sdp);
        Picasso.with(getContext())
            .load(usuario.getFoto())
            .transform(new CircleTransform(diemen))
            .into(imgSliderPhoto);
      }
      assert txtNamePerson != null;
      txtNamePerson.setText(usuario.getFullName());
      getCategorias(usuario);
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
            showcase.setNombre(getString(R.string.app_name));
            showcase.setUrl(getString(R.string.url));
            List<Categoria> finalList = new ArrayList<>();
            finalList.add(0, eventos);
            finalList.add(1, showcase);
            finalList.addAll(categorias);
            log(AppMain.getGson().toJson(finalList));
            adapter = new SlideAdapter(finalList, getActivity());
            assert menu != null;
            menu.setAdapter(adapter);
          }, throwable -> {
            log(throwable.getMessage());
          });
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
