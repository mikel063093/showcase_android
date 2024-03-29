package com.co.showcase.ui.direccion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by home on 16/09/16.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

  @Nullable private Drawable mDivider;

  /**
   * Default divider will be used
   */
  public DividerItemDecoration(@NonNull Context context) {
    final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
    mDivider = styledAttributes.getDrawable(0);
    styledAttributes.recycle();
  }

  /**
   * Custom divider will be used
   */
  public DividerItemDecoration(@NonNull Context context, int resId) {
    mDivider = ContextCompat.getDrawable(context, resId);
  }

  @Override public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + mDivider.getIntrinsicHeight();

      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }
}
