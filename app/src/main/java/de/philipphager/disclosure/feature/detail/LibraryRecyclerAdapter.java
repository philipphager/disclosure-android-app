package de.philipphager.disclosure.feature.detail;

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
import net.cachapa.expandablelayout.ExpandableLayout;

public class LibraryRecyclerAdapter
    extends RecyclerView.Adapter<LibraryRecyclerAdapter.ViewHolder> {
  private final List<Library> libraries;
  private final Context context;
  private OnLibraryClickListener listener;

  @Inject public LibraryRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.libraries = new ArrayList<>();
  }

  @Override
  public ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_library_list_item, parent, false);

    return new ViewHolder(v);
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

  public void setOnLibraryClickListener(OnLibraryClickListener listener) {
    this.listener = listener;
  }

  public interface OnLibraryClickListener {
    void onItemClick(Library library);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView subtitle;
    private final TextView description;
    private final ExpandableLayout expandableLayout;

    ViewHolder(View itemView) {
      super(itemView);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
      this.description = (TextView) itemView.findViewById(R.id.description);
      this.expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
    }

    public void bind(final Library library, final OnLibraryClickListener listener) {
      title.setText(library.title());
      subtitle.setText(library.subtitle());
      description.setText(library.description());

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          if (!expandableLayout.isExpanded()) {
            expandableLayout.expand();
          } else {
            expandableLayout.collapse();
          }

          listener.onItemClick(library);
        }
      });
    }
  }
}
