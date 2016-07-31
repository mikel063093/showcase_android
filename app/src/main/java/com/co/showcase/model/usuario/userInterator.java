package com.co.showcase.model.usuario;

import com.co.showcase.model.Usuario;

import java.util.List;
import rx.Observable;

/**
 * Created by home on 29/07/16.
 */

public interface userInterator {
  Observable<Void> createOrUpdateUser(Usuario user);

  Observable<Usuario> getUser();

  Observable<Usuario> getLiveUser();

  Observable<Void> deleteUser();
}
