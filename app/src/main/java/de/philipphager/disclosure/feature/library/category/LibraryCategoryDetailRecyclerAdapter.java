package de.philipphager.disclosure.feature.library.category;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LibraryCategoryDetailRecyclerAdapter
    extends RecyclerView.Adapter<LibraryCategoryDetailRecyclerAdapter.ViewHolder> {
  private final List<LibraryInfo> libraries;
  private final Context context;
  private OnLibraryClickListener listener;

  @Inject public LibraryCategoryDetailRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.libraries = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_category_library_list_item, parent, false);

    return new ViewHolder(v, context);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    LibraryInfo item = libraries.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return libraries.size();
  }

  public void setLibraries(List<LibraryInfo> libraries) {
    clear();
    this.libraries.addAll(libraries);
    notifyDataSetChanged();
  }

  public void clear() {
    this.libraries.clear();
    notifyDataSetChanged();
  }

  public void setOnCategoryClickListener(OnLibraryClickListener listener) {
    this.listener = listener;
  }

  public interface OnLibraryClickListener {
    void onItemClick(LibraryInfo library);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView subtitle;
    private final Context context;

    ViewHolder(View itemView, Context context) {
      super(itemView);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
      this.context = context;
    }

    public void bind(final LibraryInfo library, final OnLibraryClickListener listener) {
      int textColor = library.appCount() == 0
          ? R.color.color_grey_dark
          : R.color.colorPrimary;

      String usedInApps = library.appCount() == 0
          ? context.getResources().getString(R.string.activity_library_category_no_apps_found)
          : context.getResources().getQuantityString(R.plurals.activity_library_category_library_app_count,
              (int) library.appCount(), library.appCount());

      title.setText(library.title());
      subtitle.setText(usedInApps);
      subtitle.setTextColor(ContextCompat.getColor(context, textColor));
      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(library);
        }
      });
    }
  }
}
