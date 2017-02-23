package de.philipphager.disclosure.feature.library.detail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithPermissions;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class AppPermissionRecyclerAdapter
    extends RecyclerView.Adapter<AppPermissionRecyclerAdapter.ViewHolder> {
  private final List<AppWithPermissions> apps;
  private final Context context;
  private OnAppClickListener listener;

  @Inject public AppPermissionRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.apps = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_app_list_item, parent, false);

    return new ViewHolder(v, context);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    AppWithPermissions item = apps.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return apps.size();
  }

  public void setApps(List<AppWithPermissions> apps) {
    clear();
    this.apps.addAll(apps);
    notifyDataSetChanged();
  }

  public void clear() {
    this.apps.clear();
    notifyDataSetChanged();
  }

  public void setOnAppClickListener(OnAppClickListener listener) {
    this.listener = listener;
  }

  public interface OnAppClickListener {
    void onItemClick(AppWithPermissions app);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView icon;
    private final TextView title;
    private final TextView subtitle;
    private final Context context;

    ViewHolder(View itemView, Context context) {
      super(itemView);
      this.icon = (ImageView) itemView.findViewById(R.id.icon);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
      this.context = context;
    }

    public void bind(final AppWithPermissions appWithPermissions,
        final OnAppClickListener listener) {
      title.setText(appWithPermissions.label());

      String librariesDetected = appWithPermissions.permissionCountInt() == 0
          ? context.getResources().getString(R.string.activity_library_detail_no_permissions_found)
          : context.getResources().getQuantityString(R.plurals.activity_library_detail_no_permission_count,
              appWithPermissions.permissionCountInt(), appWithPermissions.permissionCount());

      subtitle.setText(librariesDetected);

      int textColor = appWithPermissions.permissionCountInt() == 0
          ? R.color.color_grey_dark
          : R.color.colorPrimary;

      subtitle.setTextColor(ContextCompat.getColor(context, textColor));

      new AppIconLoader.Builder(context)
          .load(appWithPermissions.packageName())
          .onThread(Schedulers.io())
          .into(icon);

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(appWithPermissions);
        }
      });
    }
  }
}
