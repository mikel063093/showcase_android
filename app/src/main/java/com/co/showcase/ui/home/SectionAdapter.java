package com.co.showcase.ui.home;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.co.showcase.R;
import com.co.showcase.model.Categoria;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by home on 6/07/16.
 */

public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private static final int SECTION_TYPE = 0;

  private boolean mValid = true;

  private LayoutInflater mLayoutInflater;
  private RecyclerView.Adapter mBaseAdapter;
  private SparseArray<Categoria> mSections = new SparseArray<>();

  public SectionAdapter(Context context, RecyclerView.Adapter baseAdapter) {

    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    mBaseAdapter = baseAdapter;
    mContext = context;

    mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyDataSetChanged();
      }

      @Override public void onItemRangeChanged(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeChanged(positionStart, itemCount);
      }

      @Override public void onItemRangeInserted(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeInserted(positionStart, itemCount);
      }

      @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeRemoved(positionStart, itemCount);
      }
    });
  }

  public static class SectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.txt_section) AppCompatTextView txtSection;
    @Bind(R.id.btn_section) AppCompatButton btnSection;
    @Bind(R.id.root_section) CardView rootSection;

    SectionViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
    if (typeView == SECTION_TYPE) {
      final View view = LayoutInflater.from(mContext).inflate(R.layout.section_item, parent, false);
      return new SectionViewHolder(view);
    } else {
      return mBaseAdapter.onCreateViewHolder(parent, typeView - 1);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
    if (isSectionHeaderPosition(position)) {
      ((SectionViewHolder) sectionViewHolder).txtSection.setText(
          mSections.get(position).getNombre());
    } else {
      mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
    }
  }

  @Override public int getItemViewType(int position) {
    return isSectionHeaderPosition(position) ? SECTION_TYPE
        : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
  }

  public void setSections(Categoria[] sections) {
    mSections.clear();

    Arrays.sort(sections, new Comparator<Categoria>() {
      @Override public int compare(Categoria o, Categoria o1) {
        return (o.firstPosition == o1.firstPosition) ? 0
            : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
      }
    });

    int offset = 0; // offset positions for the headers we're adding
    for (Categoria section : sections) {
      section.sectionedPosition = section.firstPosition + offset;
      mSections.append(section.sectionedPosition, section);
      ++offset;
    }

    notifyDataSetChanged();
  }

  public int positionToSectionedPosition(int position) {
    int offset = 0;
    for (int i = 0; i < mSections.size(); i++) {
      if (mSections.valueAt(i).firstPosition > position) {
        break;
      }
      ++offset;
    }
    return position + offset;
  }

  public int sectionedPositionToPosition(int sectionedPosition) {
    if (isSectionHeaderPosition(sectionedPosition)) {
      return RecyclerView.NO_POSITION;
    }

    int offset = 0;
    for (int i = 0; i < mSections.size(); i++) {
      if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
        break;
      }
      --offset;
    }
    return sectionedPosition + offset;
  }

  public boolean isSectionHeaderPosition(int position) {
    return mSections.get(position) != null;
  }

  @Override public long getItemId(int position) {
    return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - mSections.indexOfKey(position)
        : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
  }

  @Override public int getItemCount() {
    return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
  }
}