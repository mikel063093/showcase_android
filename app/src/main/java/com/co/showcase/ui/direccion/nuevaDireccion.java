package com.co.showcase.ui.direccion;

import android.Manifest;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.AppMain;
import com.co.showcase.R;
import com.co.showcase.api.REST;
import com.co.showcase.model.Usuario;
import com.co.showcase.model.responseAgregarDireccion;
import com.co.showcase.ui.BaseActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class nuevaDireccion extends BaseActivity {

  @Bind(R.id.toolbar_perfil) Toolbar toolbarPerfil;
  @Bind(R.id.edt_direccion) AppCompatEditText edtDireccion;
  @Bind(R.id.addAddresWrapper) TextInputLayout addAddresWrapper;
  @Bind(R.id.edt_numero) AppCompatEditText edtNumero;
  @Bind(R.id.numerWrapper) TextInputLayout numerWrapper;
  @Bind(R.id.edt_numero2) AppCompatEditText edtNumero2;
  @Bind(R.id.numer2Wrapper) TextInputLayout numer2Wrapper;
  @Bind(R.id.edt_numero3) AppCompatEditText edtNumero3;
  @Bind(R.id.numer3Wrapper) TextInputLayout numer3Wrapper;
  @Bind(R.id.edt_info_adicional) AppCompatEditText edtInfoAdicional;
  @Bind(R.id.informacionAdicionalWrapper) TextInputLayout informacionAdicionalWrapper;
  @Bind(R.id.edt_barrio) AppCompatEditText edtBarrio;
  @Bind(R.id.barrioWrapper) TextInputLayout barrioWrapper;
  @Bind(R.id.edt_ciudad) AppCompatEditText edtCiudad;
  @Bind(R.id.ciuadadWrapper) TextInputLayout ciuadadWrapper;
  @Bind(R.id.edt_nombre_direccion) AppCompatEditText edtNombreDireccion;
  @Bind(R.id.emailWrapper) TextInputLayout emailWrapper;
  @Bind(R.id.btn_nueva_direccion) AppCompatButton btnNuevaDireccion;

  private LocationRequest request = LocationRequest.create()
      .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
      .setInterval(5000);
  private static final int REQUEST_CHECK_SETTINGS = 0;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.nuevadireccion);
    ButterKnife.bind(this);
    configBackToolbar(toolbarPerfil);
    rxCheckPermisionLocation().subscribe(aBoolean -> {
      if (aBoolean) getLastLocation();
    });
  }

  @OnClick(R.id.btn_nueva_direccion) public void onClick() {
    if (validarDatos()) {
      Map<String, Object> param = new HashMap<>();
      param.put("tipo",
          !TextUtils.isEmpty(edtDireccion.getText()) ? edtDireccion.getText().toString() : null);

      param.put("numero",
          !TextUtils.isEmpty(edtNumero.getText()) ? edtNumero.getText().toString() : null);

      String nomencaltura = edtNumero2.getText().toString() + "-" + edtNumero3.getText().toString();

      param.put("nomenclatura", nomencaltura.length() >= 2 ? nomencaltura : null);

      param.put("informacionAdicional",
          !TextUtils.isEmpty(edtInfoAdicional.getText()) ? edtInfoAdicional.getText().toString()
              : null);

      param.put("nombre",
          !TextUtils.isEmpty(edtNombreDireccion.getText()) ? edtNombreDireccion.getText().toString()
              : null);

      param.put("barrio",
          !TextUtils.isEmpty(edtBarrio.getText()) ? edtBarrio.getText().toString() : null);

      param.put("ciudad",
          !TextUtils.isEmpty(edtCiudad.getText()) ? edtCiudad.getText().toString() : null);

      addAddress(param, getUserSync());
    } else {
      showMaterialDialog(getString(R.string.datos_invalidos), new onClickCallback() {
        @Override public void onPositive(boolean result) {

        }

        @Override public void onDissmis() {

        }

        @Override public void onNegative(boolean result) {

        }
      });
    }
  }

  private void addAddress(Map<String, Object> param, Usuario usuario) {
    REST.getRest()
        .agregarDireccion(usuario.getToken(), param)
        .compose(bindToLifecycle())
        .doOnSubscribe(() -> showDialog(getString(R.string.loading)))
        .subscribeOn(Schedulers.io())
        .doOnCompleted(this::dismissDialog)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::succesDirecciones, this::errControl);
  }

  private void succesDirecciones(responseAgregarDireccion responseAgregarDireccion) {
    dismissDialog();
  }

  private boolean validarDatos() {
    boolean result = true;

    if (TextUtils.isEmpty(edtDireccion.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtNumero.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtNumero2.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtNumero3.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtBarrio.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtCiudad.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtInfoAdicional.getText())) {
      result = false;
    }
    if (TextUtils.isEmpty(edtNombreDireccion.getText())) {
      result = false;
    }
    return result;
  }

  private void getLastLocation() {
    ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
    locationProvider.checkLocationSettings(
        new LocationSettingsRequest.Builder().addLocationRequest(request)
            .setAlwaysShow(true)
            .build())
        .doOnNext(this::resolveGplayErr)
        .flatMap(locationSettingsResult -> locationProvider.getUpdatedLocation(request))
        .take(1)
        .subscribe(this::onLocation);
  }

  private void onLocation(Location location) {
    reverseLocation(location);
  }

  private void resolveGplayErr(LocationSettingsResult locationSettingsResult) {
    Status status = locationSettingsResult.getStatus();
    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
      try {
        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
      } catch (IntentSender.SendIntentException th) {
        Log.e("MainActivity", "Error opening settings activity.", th);
      }
    }
  }

  @NonNull private Observable<Boolean> rxCheckPermisionLocation() {
    return RxPermissions.getInstance(this)
        .request(Manifest.permission.ACCESS_FINE_LOCATION)
        .asObservable();
  }

  private void reverseLocation(@NonNull Location loc) {
    ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
    Observable<List<Address>> reverseGeocodeObservable =
        locationProvider.getReverseGeocodeObservable(loc.getLatitude(), loc.getLongitude(), 1);
    reverseGeocodeObservable.subscribeOn(Schedulers.io())
        .onErrorResumeNext(Observable.empty())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(addresses -> {
          if (addresses.size() > 0) {
            Address add = addresses.get(0);
            updateAddres(add);
          }
        }, throwable -> {
          log(throwable.getMessage());
        });
  }

  private void updateAddres(@NonNull Address add) {
    if (add != null) {
      log(AppMain.getGson().toJson(add));
      edtCiudad.setText(add.getLocality());
      String carrera = add.getThoroughfare() != null ? add.getThoroughfare().split(" ")[0] : "";
      String numero1 = add.getThoroughfare() != null ? add.getThoroughfare().split(" ")[1] : "";
      String numero2 =
          add.getSubThoroughfare() != null ? add.getSubThoroughfare().split(" ")[0] : "";

      edtDireccion.setText(carrera);
      edtNumero.setText(numero1);
      edtNumero2.setText(numero2);
    }
  }
}
