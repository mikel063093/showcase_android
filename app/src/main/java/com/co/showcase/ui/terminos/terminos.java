package com.co.showcase.ui.terminos;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Usuario;
import com.co.showcase.ui.BaseActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class terminos extends BaseActivity {

  @Bind(R.id.toolbar_perfil) Toolbar toolbarPerfil;
  @Bind(R.id.webview) WebView webview;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.terminos_condiciones);
    ButterKnife.bind(this);
    configBackToolbar(toolbarPerfil);
    WebSettings webSettings = webview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webview.setWebViewClient(new WebViewClient());
    // webview.loadUrl(getString(R.string.url) + "termino");
    getTerminos(getUserSync());
  }

  private void getTerminos(Usuario user) {
    if (user != null) {
      REST.getRest()
          .terminos(user.getToken())
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(terminos -> {
            if (terminos.getEstado() == 1) {
              webview.loadData(terminos.getVista(), "text/html; charset=utf-8", "UTF-8");
            } else {
              showErr(getString(R.string.general_err));
            }
          }, this::errControl);
    }
  }
}
