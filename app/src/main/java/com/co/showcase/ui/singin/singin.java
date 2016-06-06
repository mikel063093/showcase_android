package com.co.showcase.ui.singin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.ui.BaseActivity;
import com.co.showcase.ui.ingresar.ingresar;
import com.co.showcase.ui.registro.registro;
import java.util.ArrayList;
import java.util.List;

public class singin extends BaseActivity {

  @Bind(R.id.toolbar_singin) Toolbar toolbar;
  @Bind(R.id.tabanim_tabs) TabLayout tabanimTabs;
  @Bind(R.id.view_pager_singin) ViewPager viewPagerSingin;
  private ViewPagerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.root_singin);
    ButterKnife.bind(this);
    adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFrag(new ingresar(), getString(R.string.ingresar));
    adapter.addFrag(new registro(), getString(R.string.registrarse));
    initTabs(adapter);
    renderToolbar();
  }

  private void renderToolbar() {
    final Drawable upArrow = getResources().getDrawable(R.drawable.ico_flecha_blanca);
    toolbar.setNavigationIcon(upArrow);
    toolbar.setNavigationOnClickListener(v -> {
      finish();
      overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    });
  }

  private void initTabs(ViewPagerAdapter adapter) {
    viewPagerSingin.setAdapter(adapter);
    if (viewPagerSingin != null && viewPagerSingin.isActivated()) {
      viewPagerSingin.setCurrentItem(viewPagerSingin.getCurrentItem());
      tabanimTabs.getSelectedTabPosition();
    }
    tabanimTabs.setupWithViewPager(viewPagerSingin);
    tabanimTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(TabLayout.Tab tab) {
        viewPagerSingin.setCurrentItem(tab.getPosition());
      }

      @Override public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override public void onTabReselected(TabLayout.Tab tab) {

      }
    });
  }

  private static class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    @Override public Fragment getItem(int position) {
      return mFragmentList.get(position);
    }

    @Override public int getCount() {
      return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mFragmentTitleList.add(title);
    }

    @Override public CharSequence getPageTitle(int position) {
      return mFragmentTitleList.get(position);
    }
  }
}
