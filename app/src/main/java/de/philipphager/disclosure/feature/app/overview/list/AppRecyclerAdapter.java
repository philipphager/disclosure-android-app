package de.philipphager.disclosure.feature.app.overview.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppWithLibraries;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.schedulers.Schedulers;

public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.ViewHolder> {
  private final List<AppWithLibraries> apps;
  private final Context context;
  private OnAppClickListener listener;

  @Inject public AppRecyclerAdapter(Context context) {
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
    AppWithLibraries item = apps.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return apps.size();
  }

  public void setApps(List<AppWithLibraries> apps) {
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
    void onItemClick(AppWithLibraries app);
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

    public void bind(final AppWithLibraries appWithLibraries, final OnAppClickListener listener) {
      title.setText(appWithLibraries.label());

      String librariesDetected = appWithLibraries.libraryCountInt() == 0
          ? context.getResources().getString(R.string.fragment_app_list_no_libraries_found)
          : context.getResources().getQuantityString(R.plurals.fragment_app_list_library_count,
              appWithLibraries.libraryCountInt(), appWithLibraries.libraryCount());

      subtitle.setText(librariesDetected);

      int textColor = appWithLibraries.libraryCountInt() == 0
          ? R.color.color_text_secondary
          : R.color.color_text_warning;

      subtitle.setTextColor(ContextCompat.getColor(context, textColor));

      new AppIconLoader.Builder(context)
          .load(appWithLibraries.packageName())
          .onThread(Schedulers.io())
          .into(icon);

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(appWithLibraries);
        }
      });
    }
  }
}
