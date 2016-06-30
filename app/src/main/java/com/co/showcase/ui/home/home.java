package com.co.showcase.ui.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.CustomView.CirclePageIndicator;
import java.util.ArrayList;

public class home extends BaseActivity {

  @Bind(R.id.toolbar_home) Toolbar toolbar;
  @Bind(R.id.view_pager_home) ViewPager viewPagerSlide;
  @Bind(R.id.indicator_home) CirclePageIndicator indicatorSlides;
  private SlideAdapter adapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home);
    ButterKnife.bind(this);
  }

  private void renderSlideImages(ArrayList<String> imgs) {
    adapter = new SlideAdapter(this, imgs);
    viewPagerSlide.setAdapter(adapter);
    indicatorSlides.setViewPager(viewPagerSlide);
  }
}
