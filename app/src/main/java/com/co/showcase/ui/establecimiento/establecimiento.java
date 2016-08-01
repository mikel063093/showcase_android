package com.co.showcase.ui.establecimiento;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;

public class establecimiento extends BaseActivity {

  @Bind(R.id.toolbar_home) Toolbar toolbarHome;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorHome;
  @Bind(R.id.view_pager_home) ViewPager viewPagerHome;
  @Bind(R.id.txt_name_company) AppCompatTextView txtNameCompany;
  @Bind(R.id.txt_description) AppCompatTextView txtDescription;
  @Bind(R.id.txt_addres) AppCompatTextView txtAddres;
  @Bind(R.id.txt_phone) AppCompatTextView txtPhone;
  @Bind(R.id.txt_celphone) AppCompatTextView txtCelphone;
  @Bind(R.id.txt_email) AppCompatTextView txtEmail;
  @Bind(R.id.txt_website) AppCompatTextView txtWebsite;
  @Bind(R.id.btn_sahre_fb) ImageView btnSahreFb;
  @Bind(R.id.btn_sahre_tw) ImageView btnSahreTw;
  @Bind(R.id.ratingBar) RatingBar ratingBar;
  @Bind(R.id.rv_home) RecyclerView rvHome;
  @Bind(R.id.drawer) RelativeLayout drawer;
  @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
  @Bind(R.id.share_general) ImageView shareGeneral;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.establecimiento);
    ButterKnife.bind(this);
  }

  @OnClick({ R.id.btn_sahre_fb, R.id.btn_sahre_tw, R.id.share_general })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_sahre_fb:
        break;
      case R.id.btn_sahre_tw:
        break;
      case R.id.share_general:
        break;
    }
  }
}
