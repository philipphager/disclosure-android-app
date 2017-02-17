package de.philipphager.disclosure.feature.app.detail;

import android.content.pm.PackageInfo;
import android.content.pm.PermissionInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.app.detail.usecase.LibraryWithPermission;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.gujun.android.taggroup.TagGroup;
import rx.Observable;

public class LibraryRecyclerAdapter
    extends RecyclerView.Adapter<LibraryRecyclerAdapter.ViewHolder> {
  private final List<LibraryWithPermission> libraries;
  private OnLibraryClickListener listener;

  @Inject public LibraryRecyclerAdapter() {
    super();
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
    LibraryWithPermission item = libraries.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return libraries.size();
  }

  public void setLibraries(List<LibraryWithPermission> libraries) {
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
    private final TagGroup permissionGroupDangerous;
    private final TagGroup permissionGroupNormal;

    ViewHolder(View itemView) {
      super(itemView);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
      this.permissionGroupDangerous = (TagGroup) itemView.findViewById(R.id.permission_group_dangerous);
      this.permissionGroupNormal = (TagGroup) itemView.findViewById(R.id.permission_group_normal);
    }

    public void bind(final LibraryWithPermission libraryWithPermission,
        final OnLibraryClickListener listener) {
      Library library = libraryWithPermission.library();
      title.setText(library.title());
      subtitle.setText(library.subtitle());

      permissionGroupDangerous.setTags(Observable.from(libraryWithPermission.permissions())
          .filter(permission -> permission.protectionLevel() == PermissionInfo.PROTECTION_DANGEROUS)
          .map(Permission::title)
          .toList()
          .toBlocking()
          .first());

      permissionGroupNormal.setTags(Observable.from(libraryWithPermission.permissions())
          .filter(permission -> permission.protectionLevel() == PermissionInfo.PROTECTION_NORMAL)
          .map(Permission::title)
          .toList()
          .toBlocking()
          .first());

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(library);
        }
      });
    }
  }
}
