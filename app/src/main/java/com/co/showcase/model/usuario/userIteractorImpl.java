package com.co.showcase.model.usuario;

import com.co.showcase.BuildConfig;
import com.co.showcase.model.NullIfNoRealmObject;
import com.co.showcase.model.Usuario;

import com.orhanobut.logger.Logger;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

/**
 * Created by home on 29/07/16.
 */

public class userIteractorImpl implements userInterator {

  @Override public Observable<Void> createOrUpdateUser(Usuario user) {
    if (BuildConfig.DEBUG) Logger.e("onSavePre " + user.getCorreo());
    return Observable.just(user).map(user1 -> {
      if (BuildConfig.DEBUG) Logger.e("onSave " + user1.getCorreo());

      final Realm realm = Realm.getDefaultInstance();
      realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(user1));
      realm.close();

      return null;
    });
  }

  @Override public Observable<Usuario> getUser() {

    return getManagedRealm().concatMap(realm -> realm.where(Usuario.class)
        .findAllAsync()
        .asObservable()
        .compose(new NullIfNoRealmObject<Usuario>()));
        //.map(usuario -> realm.copyFromRealm(usuario)));
  }

  @Override public Observable<Usuario> getLiveUser() {
    return getManagedRealm().concatMap(realm -> realm.where(Usuario.class)
        .findAllAsync()
        .asObservable()
        .compose(new NullIfNoRealmObject<Usuario>()));
  }

  @Override public Observable<Void> deleteUser() {

    return getUser().map(registroResponse -> {
      if (registroResponse != null) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> registroResponse.removeFromRealm());
        realm.close();
      }
      return null;
    });
  }

  public static Observable<Realm> getManagedRealm() {
    return Observable.create(new Observable.OnSubscribe<Realm>() {
      @Override public void call(final Subscriber<? super Realm> subscriber) {
        final Realm realm = Realm.getDefaultInstance();
        subscriber.add(Subscriptions.create(realm::close));
        subscriber.onNext(realm);
      }
    });
  }
}
