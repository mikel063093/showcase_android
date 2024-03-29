package com.co.showcase.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.R;
import com.co.showcase.model.Entre;

import com.squareup.picasso.Picasso;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {

  public static final String DETAIL = DetailActivity.class.getCanonicalName() + ".DETAIL";
  @Nullable @Bind(R.id.image_app) ImageView imageApp;
  @Nullable @Bind(R.id.toolbar_detail) Toolbar toolbar;
  @Nullable @Bind(R.id.text_title) TextView textTitle;

  private Entre entry;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);
    String data = getIntent().getStringExtra(DETAIL);
    if (data != null) {
      RxParseJson(data, Entre.class).observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.io())
          .subscribe(o -> {
            Entre entre = (Entre) o;
            rederDetail(entre);
          });
    }
  }

  private void rederDetail(@NonNull Entre entry) {
    this.entry = entry;
    //configToolbarChild(toolbar, entry.imName.label);
    Picasso.with(DetailActivity.this).load(entry.imImage.get(2).label).fit().into(imageApp);
    textTitle.setText(entry.title.label);

  }

  @OnClick(R.id.image_price) public void onClick() {
    if (entry != null && entry.id.label != null) {
      Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(entry.id.label));
      goActv(intent, false);
    }
  }
}
