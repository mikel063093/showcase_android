package com.co.showcase.ui.slide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.Categoria;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseFragment;
import com.co.showcase.ui.util.CircleTransform;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by home on 7/07/16.
 */

public class slide extends BaseFragment {
  @Bind(R.id.img_slider_photo) CircularImageView imgSliderPhoto;
  @Bind(R.id.txt_name_person) AppCompatTextView txtNamePerson;
  @Bind(R.id.menu) ListView menu;
  private SlideAdapter adapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.slide_menu, container, false);
    ButterKnife.bind(this, view);
    Usuario.getItem()
        .compose(bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::updateUi);
    return view;
  }

  private void updateUi(Usuario usuario) {
    if (usuario.getFoto() != null && usuario.getFoto().length() > 4) {
      Picasso.with(getContext())
          .load(usuario.getFoto())
          .transform(new CircleTransform())
          .into(imgSliderPhoto);
    }
    if (usuario.getFullName() != null) {
      txtNamePerson.setText(usuario.getFullName());
    }
    if (BuildConfig.DEBUG) {
      List<Categoria> sections = new ArrayList<>();
      sections.add(new Categoria(0, "Almacenes de ropa"));
      sections.add(new Categoria(2, "Almacenes de ropa"));
      setupList(sections);
    }
  }

  @Override public void onEvent(List<Categoria> categorias) {
    super.onEvent(categorias);
    log("onevent categorias");
    setupList(categorias);
  }

  private void setupList(List<Categoria> categorias) {
    adapter = new SlideAdapter(categorias, getActivity());
    menu.setAdapter(adapter);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}
