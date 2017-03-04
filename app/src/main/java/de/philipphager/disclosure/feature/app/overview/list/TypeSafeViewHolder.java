package de.philipphager.disclosure.feature.app.overview.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TypeSafeViewHolder<T extends View> extends RecyclerView.ViewHolder {
  private final T view;

  public TypeSafeViewHolder(T view) {
    super(view);
    this.view = view;
  }

  public T getView() {
    return view;
  }
}
