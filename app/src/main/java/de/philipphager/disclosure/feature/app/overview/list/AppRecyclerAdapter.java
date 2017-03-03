package de.philipphager.disclosure.feature.app.overview.list;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.format.DateTimeFormatter;
import rx.schedulers.Schedulers;

public class AppRecyclerAdapter extends RecyclerView.Adapter<AppRecyclerAdapter.ViewHolder> {
  private final List<AppReport> appReports;
  private final Context context;
  private OnAppClickListener listener;

  @Inject public AppRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.appReports = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_app_list_item, parent, false);

    return new ViewHolder(v, context);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    AppReport appReport = appReports.get(position);
    holder.bind(appReport, listener);
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

  public void setOnAppClickListener(OnAppClickListener listener) {
    this.listener = listener;
  }

  public interface OnAppClickListener {
    void onItemClick(AppReport app);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView icon;
    private final TextView title;
    private final TextView libraryCount;
    private final TextView analyzedAt;
    private final TextView permissionCount;
    private final Context context;

    ViewHolder(View itemView, Context context) {
      super(itemView);
      this.icon = (ImageView) itemView.findViewById(R.id.icon);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.libraryCount = (TextView) itemView.findViewById(R.id.library_count);
      this.permissionCount = (TextView) itemView.findViewById(R.id.permission_count);
      this.analyzedAt = (TextView) itemView.findViewById(R.id.analyzed_at);
      this.context = context;
    }

    public void bind(final AppReport appReport, final OnAppClickListener listener) {
      title.setText(appReport.App().label());

      libraryCount.setText(getLibraryCountText(appReport));
      libraryCount.setTextColor(
          ContextCompat.getColor(context, getLibraryCountTextColor(appReport)));

      if (appReport.librariesDetected()) {

        if (appReport.wasAnalyzed()) {
          permissionCount.setText(getPermissionCountText(appReport));
          analyzedAt.setText(getAnalyzedAt(appReport));
          analyzedAt.setVisibility(View.VISIBLE);
        } else {
          permissionCount.setText(context.getString(R.string.view_app_list_item_not_yet_analyzed));
        }

        permissionCount.setVisibility(View.VISIBLE);
      }

      permissionCount.setTextColor(
          ContextCompat.getColor(context, getPermissionCountTextColor(appReport)));

      new AppIconLoader.Builder(context)
          .load(appReport.App().packageName())
          .onThread(Schedulers.io())
          .into(icon);

      itemView.setOnClickListener(view -> {
        if (listener != null) {
          listener.onItemClick(appReport);
        }
      });
    }

    private String getLibraryCountText(AppReport appReport) {
      if (appReport.librariesDetected()) {
        int count = (int) appReport.libraryCount();
        return context.getResources()
            .getQuantityString(R.plurals.view_app_list_item_libraries_found, count, count);
      } else {
        return context.getResources().getString(R.string.view_app_list_item_no_libraries_found);
      }
    }

    private @ColorRes int getLibraryCountTextColor(AppReport appReport) {
      return appReport.librariesDetected()
          ? R.color.color_text_warning
          : R.color.color_text_secondary;
    }

    private String getPermissionCountText(AppReport appReport) {
      int count = (int) appReport.permissionCount();
      return context.getResources()
          .getQuantityString(R.plurals.view_app_list_item_permissions_found, count, count);
    }

    private @ColorRes int getPermissionCountTextColor(AppReport appReport) {
      return appReport.wasAnalyzed() && appReport.permissionCount() > 0
          ? R.color.color_accent
          : R.color.color_text_secondary;
    }

    private String getAnalyzedAt(AppReport appReport) {
      String date =
          appReport.App().analyzedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
      return context.getResources().getString(R.string.view_app_list_item_last_analyzed, date);
    }
  }
}
