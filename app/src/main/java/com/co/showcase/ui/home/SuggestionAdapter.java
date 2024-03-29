package com.co.showcase.ui.home;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import com.google.common.collect.Lists;
import java.util.List;

/**
 * Created by home on 4/09/16.
 */

public class SuggestionAdapter<T> extends ArrayAdapter<T> {

  private List<T> items;
  private List<T> filteredItems;
  private ArrayFilter mFilter;

  public SuggestionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
    super(context, resource, Lists.<T>newArrayList());
    this.items = objects;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public T getItem(int position) {
    return items.get(position);
  }

  @NonNull @Override
  public Filter getFilter() {
    if (mFilter == null) {
      mFilter = new ArrayFilter();
    }
    return mFilter;
  }

  public int getCount() {
    //todo: change to pattern-size
    return items.size();
  }

  private class ArrayFilter extends Filter {
    @NonNull @Override
    protected FilterResults performFiltering(CharSequence prefix) {
      FilterResults results = new FilterResults();

      //custom-filtering of results
      results.values = items;
      results.count = items.size();

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, @NonNull FilterResults results) {
      filteredItems = (List<T>) results.values;
      if (results.count > 0) {
        notifyDataSetChanged();
      } else {
        notifyDataSetInvalidated();
      }
    }
  }
}