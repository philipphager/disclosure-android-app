package de.philipphager.disclosure.util.ui;

import android.content.Context;
import de.philipphager.disclosure.R;
import javax.inject.Inject;

public class GridColumnProvider {
  private final Context context;

  @Inject public GridColumnProvider(Context context) {
    this.context = context;
  }

  public int get() {
    return context.getResources().getInteger(R.integer.grid_columns);
  }
}
