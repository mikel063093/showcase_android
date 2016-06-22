package com.co.showcase.ui.singin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.co.showcase.R;
import com.co.showcase.model.TabPosition;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.ingresar.ingresar;
import com.co.showcase.ui.registro.registro;

import java.util.ArrayList;
import java.util.List;

public class singin extends BaseActivity {

  @Nullable @Bind(R.id.toolbar_singin) Toolbar toolbar;
  @Nullable @Bind(R.id.tabanim_tabs) TabLayout tabanimTabs;
  @Nullable @Bind(R.id.view_pager_singin) ViewPager viewPagerSingin;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.root_singin);
    ButterKnife.bind(this);
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFrag(new ingresar(), getString(R.string.ingresar));
    adapter.addFrag(new registro(), getString(R.string.registrarse));
    initTabs(adapter);
    renderToolbar();
    int position = getIntent().getIntExtra(KEY_POSITION, -1);
    if (position != -1) {
      goPage(position);
    }
  }

  private void renderToolbar() {
    final Drawable upArrow = getResources().getDrawable(R.drawable.btn_flechaizquierda);

    assert toolbar != null;
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  private void initTabs(ViewPagerAdapter adapter) {
    assert viewPagerSingin != null;
    viewPagerSingin.setAdapter(adapter);
    if (viewPagerSingin != null && viewPagerSingin.isActivated()) {
      viewPagerSingin.setCurrentItem(viewPagerSingin.getCurrentItem());
      assert tabanimTabs != null;
      tabanimTabs.getSelectedTabPosition();
    }
    tabanimTabs.setupWithViewPager(viewPagerSingin);
    tabanimTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(@NonNull TabLayout.Tab tab) {
        viewPagerSingin.setCurrentItem(tab.getPosition());
      }

      @Override public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  @Override public void onEvent(@NonNull TabPosition tabPosition) {
    super.onEvent(tabPosition);
    goPage(tabPosition.getPosition());
  }

  private void goPage(int position) {
    assert viewPagerSingin != null;
    log("currentItem --> " + viewPagerSingin.getCurrentItem());
    assert viewPagerSingin != null;
    log("goPage --> " + position);
    viewPagerSingin.setCurrentItem(position);
    assert tabanimTabs != null;
    viewPagerSingin.setCurrentItem(viewPagerSingin.getCurrentItem());
  }

  private static class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override public int getCount() {
      return mFragmentList.size();
    }

    void addFrag(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }
  }
}
