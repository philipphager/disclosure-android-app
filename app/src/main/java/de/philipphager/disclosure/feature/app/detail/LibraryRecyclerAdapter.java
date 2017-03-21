package de.philipphager.disclosure.feature.app.detail;

import android.content.pm.PermissionInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.library.model.LibraryWithPermission;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.gujun.android.taggroup.TagGroup;
import rx.Observable;

public class LibraryRecyclerAdapter
    extends RecyclerView.Adapter<LibraryRecyclerAdapter.ViewHolder> {
  private final List<LibraryWithPermission> libraries;
  private OnClickListener<Library> libraryListener;
  private OnClickListener<Permission> permissionListener;

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
    holder.bind(item, libraryListener, permissionListener);
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

  public void setOnLibraryClickListener(OnClickListener<Library> listener) {
    this.libraryListener = listener;
  }

  public void setOnPermissionClickListener(OnClickListener<Permission> listener) {
    this.permissionListener = listener;
  }

  public interface OnClickListener<T> {
    void onItemClick(T t);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView subtitle;
    private final TagGroup permissionGroupDangerous;
    private final TagGroup permissionGroupNormal;

    ViewHolder(View itemView) {
      super(itemView);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.subtitle = (TextView) itemView.findViewById(R.id.library_count);
      this.permissionGroupDangerous =
          (TagGroup) itemView.findViewById(R.id.permission_group_dangerous);
      this.permissionGroupNormal = (TagGroup) itemView.findViewById(R.id.permission_group_normal);
    }

    public void bind(final LibraryWithPermission libraryWithPermission,
        final OnClickListener<Library> libraryListener,
        final OnClickListener<Permission> permissionListener) {
      Library library = libraryWithPermission.library();
      title.setText(library.title());
      subtitle.setText(library.type().name());

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

      permissionGroupDangerous.setOnTagClickListener(tag -> {
        permissionListener.onItemClick(getByTag(libraryWithPermission.permissions(), tag));
      });

      permissionGroupNormal.setOnTagClickListener(tag -> {
        permissionListener.onItemClick(getByTag(libraryWithPermission.permissions(), tag));
      });

      itemView.setOnClickListener(view -> {
        if (libraryListener != null) {
          libraryListener.onItemClick(library);
        }
      });
    }

    private Permission getByTag(List<Permission> permissions, String title) {
      return Observable.from(permissions)
          .filter(permission -> permission.title().equals(title))
          .toBlocking()
          .first();
    }
  }
}
