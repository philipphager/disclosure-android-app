package de.philipphager.disclosure.feature.library.overview;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.feature.library.overview.usecase.LibraryCategory;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LibraryCategoryRecyclerAdapter
    extends RecyclerView.Adapter<LibraryCategoryRecyclerAdapter.ViewHolder> {
  private final List<LibraryCategory> libraryCategories;
  private final Context context;
  private OnAppClickListener listener;

  @Inject public LibraryCategoryRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.libraryCategories = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_library_category_item, parent, false);

    return new ViewHolder(v, context);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    LibraryCategory item = libraryCategories.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return libraryCategories.size();
  }

  public void setLibraryCategories(List<LibraryCategory> libraryCategories) {
    clear();
    this.libraryCategories.addAll(libraryCategories);
    notifyDataSetChanged();
  }

  public void clear() {
    this.libraryCategories.clear();
    notifyDataSetChanged();
  }

  public void setOnCategoryClickListener(OnAppClickListener listener) {
    this.listener = listener;
  }

  public interface OnAppClickListener {
    void onItemClick(LibraryCategory app);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView libraryCount;
    private final TextView usageCount;
    private final Context context;

    ViewHolder(View itemView, Context context) {
      super(itemView);
      this.title = (TextView) itemView.findViewById(R.id.category_title);
      this.libraryCount = (TextView) itemView.findViewById(R.id.library_count);
      this.usageCount = (TextView) itemView.findViewById(R.id.usage_count);
      this.context = context;
    }

    public void bind(final LibraryCategory typeInfo, final OnAppClickListener listener) {
      Resources resources = context.getResources();

      title.setText(typeInfo.type().name());
      libraryCount.setText(resources.getQuantityString(
          R.plurals.fragment_library_category_library_count,
          typeInfo.allLibraries().intValue(),
          typeInfo.allLibraries()));

      usageCount.setText(resources.getQuantityString(
          R.plurals.fragment_library_category_usage_count,
          typeInfo.usedLibraries().intValue(),
          typeInfo.usedLibraries()));

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(typeInfo);
        }
      });
    }
  }
}
