package com.co.showcase.ui.perfil;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.NullIfNoRealmObject;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.usuario.userIteractorImpl;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.slide.slide;
import com.co.showcase.ui.util.Base64Utils;
import com.co.showcase.ui.util.CircleTransform;
import com.fuck_boilerplate.rx_paparazzo.RxPaparazzo;
import com.fuck_boilerplate.rx_paparazzo.entities.Size;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class perfil extends BaseActivity implements SearchView.OnQueryTextListener {

  @Nullable @Bind(R.id.toolbar_perfil) Toolbar toolbar;
  @Nullable @Bind(R.id.img_perfil) ImageView perfil;
  @Nullable @Bind(R.id.edt_nombre) AppCompatEditText edtNombre;
  @Nullable @Bind(R.id.nombreWrapper) TextInputLayout nombreWrapper;
  @Nullable @Bind(R.id.edt_apellido) AppCompatEditText edtApellido;
  @Nullable @Bind(R.id.apellidoWrapper) TextInputLayout apellidoWrapper;
  @Nullable @Bind(R.id.edt_password) AppCompatEditText edtPassword;
  @Nullable @Bind(R.id.passwordWrapper) TextInputLayout passwordWrapper;
  @Nullable @Bind(R.id.edt_email) AppCompatEditText edtEmail;
  @Nullable @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
  @Nullable @Bind(R.id.drawer) RelativeLayout drawer;
  @Nullable @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  private String filePath;
  private SearchView searchView;
  private MenuItem searchItem;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.perfil);
    ButterKnife.bind(this);
    assert toolbar != null;
    //configToolbarChild(toolbar, R.string.perfil);

    Usuario.getItem()
        .compose(bindToLifecycle())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::updateUi);
    setupToolbar();
    setupSlider();
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
    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    searchItem = menu.findItem(R.id.action_search);
    MenuItemCompat.setOnActionExpandListener(searchItem,
        new MenuItemCompat.OnActionExpandListener() {
          @Override public boolean onMenuItemActionCollapse(MenuItem item) {
            Log("closeMenuSearch");
            setItemsVisibility(menu, searchItem, true);
            return true;  // Return true to collapse action view
          }

          @Override public boolean onMenuItemActionExpand(MenuItem item) {
            // Do something when expanded
            return true;  // Return true to expand action view
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
    // Detect SearchView close
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

      case R.id.action_search:
        log("action search");
        //Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        return true;
      case R.id.action_perfil:
       // goActv(perfil.class, false);
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

  private void updateUi(@NonNull Usuario usuario) {
    assert edtNombre != null;
    edtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
    assert edtApellido != null;
    edtApellido.setText(usuario.getApellido() != null ? usuario.getApellido() : "");
    assert edtEmail != null;
    edtEmail.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
    assert edtPassword != null;
    edtPassword.setText(usuario.getTelefono() != null ? usuario.getTelefono() : "");
    if (usuario.getFoto() != null && usuario.getFoto().length() > 4) {
      Picasso.with(getApplicationContext())
          .load(usuario.getFoto())
          .transform(new CircleTransform())
          .into(perfil);
    }
  }

  @OnClick({ R.id.img_perfil, R.id.btn_guardar }) public void onClick(@NonNull View view) {
    switch (view.getId()) {
      case R.id.img_perfil:
        requestImage();
        break;
      case R.id.btn_guardar:
        guardar();
        break;
    }
  }

  private void guardar() {
    if (filePath != null) {
      Base64Utils.rxFile2B64(filePath)
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .onErrorResumeNext(Observable.error(new Throwable("Custom error")))
          .subscribe(s -> {
            guardar(s, Base64Utils.getExtension(filePath));
          }, this::errControl);
    } else {
      Log("noFilePath");
      Usuario usuario = getUserSync();
      if (usuario != null) {

        Map<String, Object> param = new HashMap<>();
        param.put("id", usuario.getId());
        assert edtNombre != null;
        if (validateFirstName(edtNombre.getText().toString())) {
          param.put("nombre", edtNombre.getText().toString());
        } else {
          log("validateNameFail");
        }
        if (validateLastName(edtApellido.getText().toString())) {
          param.put("apellido", edtApellido.getText().toString());
        } else {
          log("validateLastNameFail");
        }
        if (validateEmail(edtEmail.getText().toString())) {
          param.put("correo", edtEmail.getText().toString());
        } else {
          log("validateEmailFail");
        }
        if (!TextUtils.isEmpty(edtPassword.getText().toString())) {
          param.put("telefono", edtPassword.getText().toString());
        } else {
          log("validatePhoneFail");
        }

        REST.getRest()
            .subirFoto(usuario.getToken(), param)
            .compose(bindToLifecycle())
            .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
            .subscribeOn(Schedulers.io())
            .doOnCompleted(this::dismissDialog)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::succesUpPerfil, this::errControl);
      }
    }
  }

  private void guardar(String base64Img, String extencion) {
    Usuario usuario = getUserSync();
    if (usuario != null) {
      Map<String, Object> param = new HashMap<>();
      param.put("contenido", base64Img);
      param.put("tipo", "." + extencion);
      param.put("id", usuario.getId());
      param.put("id", usuario.getId());
      assert edtNombre != null;
      if (validateFirstName(edtNombre.getText().toString())) {
        param.put("nombre", edtNombre.getText().toString());
      } else {
        log("validateNameFail");
      }
      assert edtApellido != null;
      if (validateLastName(edtApellido.getText().toString())) {
        param.put("apellido", edtApellido.getText().toString());
      } else {
        log("validateLastNameFail");
      }
      assert edtEmail != null;
      if (validateEmail(edtEmail.getText().toString())) {
        param.put("correo", edtEmail.getText().toString());
      } else {
        log("validateEmailFail");
      }
      assert edtPassword != null;
      if (!TextUtils.isEmpty(edtPassword.getText().toString())) {
        param.put("telefono", edtPassword.getText().toString());
      } else {
        log("validatePhoneFail");
      }
      REST.getRest()
          .subirFoto(usuario.getToken(), param)
          .compose(bindToLifecycle())
          .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
          .subscribeOn(Schedulers.io())
          .doOnCompleted(this::dismissDialog)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(this::succesUpPerfil, this::errControl);
    }
  }

  private void succesUpPerfil(@NonNull Usuario usuario) {
    dismissDialog();
    if (usuario.getEstado().equalsIgnoreCase("exito")) {
      updateRealmUser(usuario);
    } else {
      showErr(usuario.getMensaje());
    }
  }

  private void requestImage() {
    RxPaparazzo.takeImage(this).size(Size.Screen).usingGallery().subscribe(response -> {
      if (response.resultCode() != RESULT_OK) {
        response.targetUI().showUserCanceled();
        return;
      }
      response.data();
      response.targetUI().loadImage(response.data());
    });
  }

  private void loadImage(String filePath) {
    this.filePath = filePath;
    Picasso.with(getApplicationContext()).setLoggingEnabled(true);
    Picasso.with(getApplicationContext()).invalidate("file://" + filePath);

    Picasso.with(getApplicationContext())
        .load("file://" + filePath)
        .transform(new CircleTransform())
        .into(perfil);
  }

  private void showUserCanceled() {
    Toast.makeText(this, getString(R.string.user_canceled), Toast.LENGTH_SHORT).show();
  }

  @Override public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override public boolean onQueryTextChange(String newText) {
    return false;
  }
}
