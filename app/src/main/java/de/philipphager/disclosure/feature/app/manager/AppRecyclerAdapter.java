package de.philipphager.disclosure.feature.app.manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.util.ui.TypeSafeViewHolder;
import de.philipphager.disclosure.util.ui.components.AppListItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class AppRecyclerAdapter extends RecyclerView.Adapter<TypeSafeViewHolder<AppListItem>> {
  private final List<AppReport> appReports;
  private final Context context;
  private AppListItem.OnAppClickListener listener;
  private AppListItem.OnAppLongClickListener longClickListener;

  @Inject public AppRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.appReports = new ArrayList<>();
  }

  @Override
  public TypeSafeViewHolder<AppListItem> onCreateViewHolder(ViewGroup parent, int viewType) {
    AppListItem appListItem = AppListItem.create(parent, LayoutInflater.from(context));
    return new TypeSafeViewHolder<>(appListItem);
  }

  @Override public void onBindViewHolder(TypeSafeViewHolder<AppListItem> holder, int position) {
    AppReport appReport = appReports.get(position);
    AppListItem appListItem = holder.getView();
    appListItem.bind(appReport, listener, longClickListener);
  }

  @Override public int getItemCount() {
    return appReports.size();
  }

  public void setAppReports(List<AppReport> appReports) {
    clear();
    this.appReports.addAll(appReports);
    notifyDataSetChanged();
  }

  public void clear() {
    this.appReports.clear();
    notifyDataSetChanged();
  }

  public void setOnAppClickListener(AppListItem.OnAppClickListener listener) {
    this.listener = listener;
  }

  public void setOnAppLongClickListener(AppListItem.OnAppLongClickListener listener) {
    this.longClickListener = listener;
  }
}
