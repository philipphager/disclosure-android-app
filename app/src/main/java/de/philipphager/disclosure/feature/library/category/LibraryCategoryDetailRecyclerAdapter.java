package de.philipphager.disclosure.feature.library.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LibraryCategoryDetailRecyclerAdapter
    extends RecyclerView.Adapter<LibraryCategoryDetailRecyclerAdapter.ViewHolder> {
  private final List<Library> libraries;
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
    Library item = libraries.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return libraries.size();
  }

  public void setLibraries(List<Library> libraries) {
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
    void onItemClick(Library library);
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

    public void bind(final Library library, final OnLibraryClickListener listener) {
      title.setText(library.title());
      subtitle.setText(library.packageName());

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(library);
        }
      });
    }
  }
}
