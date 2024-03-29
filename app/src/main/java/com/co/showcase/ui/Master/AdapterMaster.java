package com.co.showcase.ui.Master;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.BuildConfig;
import com.co.showcase.R;
import com.co.showcase.model.Entre;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by miguelalegria on 16/5/16 for DemoMike.
 */
public class AdapterMaster extends ArrayAdapter<List<Entre>> {
  @Nullable
  private ViewHolder holder;

  public AdapterMaster(@NonNull Context context, int resource, @NonNull List<List<Entre>> objects) {
    super(context, resource, objects);
  }

  //public AdapterMaster(Context context, int resource, ArrayList<List<Entre>> objects) {
  //  super(context, resource, objects);
  //}

  @Nullable
  @Override public View getView(int position, @Nullable View convertView, ViewGroup parent) {
    if (convertView == null) {
      LayoutInflater inflater =
          (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.row, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    }
    Entre entry = getItem(position).get(0);
    log(entry.toJson());

    holder = new ViewHolder(convertView);
    //Transformation transformation = new RoundedTransformationBuilder().borderColor(
    //    ContextCompat.getColor(getContext(), R.color.divider))
    //    .borderWidthDp(1)
    //    .cornerRadiusDp(5)
    //    .oval(false)
    //    .build();
    Picasso.with(getContext()).setLoggingEnabled(BuildConfig.DEBUG);
    //Picasso.with(getContext())
    //    .load(entry.imImage.get(1).label)
    //    .fit()
    //    .transform(transformation)
    //    .into(holder.icon);
    holder.label.setText(entry.category.attributes.label);
    return convertView;
  }

  private void log(String msg) {
    if (BuildConfig.DEBUG) Logger.e(msg);
  }

  static class ViewHolder {
    @Nullable
    @Bind(R.id.img_icon) ImageView icon;
    @Nullable
    @Bind(R.id.txt_label) TextView label;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
