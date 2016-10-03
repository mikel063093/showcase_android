package com.co.showcase.model;

import android.support.annotation.NonNull;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by home on 29/07/16.
 */
public class NullIfNoRealmObject<RlmObject extends RealmObject>
    implements Observable.Transformer<RealmResults<RlmObject>, RlmObject> {

  @NonNull @Override public Observable<RlmObject> call(@NonNull Observable<RealmResults<RlmObject>> observable) {
    return observable.filter(rs -> rs != null && rs.isLoaded()).map(rs -> {
      if (rs.size() > 0) {
        return rs.first();
      }
      return null;
    });
  }
}
