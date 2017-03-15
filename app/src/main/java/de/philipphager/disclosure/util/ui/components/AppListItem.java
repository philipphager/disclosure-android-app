package de.philipphager.disclosure.util.ui.components;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.AppReport;
import de.philipphager.disclosure.util.ui.image.AppIconLoader;
import org.threeten.bp.format.DateTimeFormatter;
import rx.schedulers.Schedulers;

public class AppListItem extends FrameLayout implements Checkable {
  private static final int[] CHECKED_STATE = {android.R.attr.state_checked};
  @BindView(R.id.icon) protected ImageView icon;
  @BindView(R.id.title) protected TextView title;
  @BindView(R.id.library_count) protected TextView libraryCount;
  @BindView(R.id.permission_count) protected TextView analyzedAt;
  @BindView(R.id.analyzed_at) protected TextView permissionCount;
  private boolean checked;

  public AppListItem(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public static AppListItem create(ViewGroup parent, LayoutInflater layoutInflater) {
    return (AppListItem) layoutInflater.inflate(R.layout.view_app_list_item, parent, false);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    setBackgroundResource(R.drawable.app_list_item_checkable_background);
  }

  public void bind(AppReport appReport,
      OnAppClickListener listener,
      OnAppLongClickListener longClickListener) {
    title.setText(appReport.App().label());

    libraryCount.setText(getLibraryCountText(appReport));
    libraryCount.setTextColor(
        ContextCompat.getColor(getContext(), getLibraryCountTextColor(appReport)));

    if (appReport.librariesDetected()) {

      if (appReport.wasAnalyzed()) {
        permissionCount.setText(getPermissionCountText(appReport));
        analyzedAt.setText(getAnalyzedAt(appReport));
        analyzedAt.setVisibility(View.VISIBLE);
      } else {
        permissionCount.setText(
            getContext().getString(R.string.app_list_item_not_yet_analyzed));
        analyzedAt.setVisibility(GONE);
      }

      permissionCount.setVisibility(View.VISIBLE);
    } else {
      permissionCount.setVisibility(View.GONE);
    }

    permissionCount.setTextColor(
        ContextCompat.getColor(getContext(), getPermissionCountTextColor(appReport)));

    new AppIconLoader.Builder(getContext())
        .load(appReport.App().packageName())
        .onThread(Schedulers.io())
        .into(icon);

    setOnClickListener(view -> {
      if (listener != null) {
        listener.onItemClick(appReport);
      }
    });

    setOnLongClickListener(view -> {
      if (longClickListener != null) {
        longClickListener.onItemClick(appReport);
      }
      return true;
    });
  }

  @Override public boolean isChecked() {
    return checked;
  }

  @Override public void setChecked(boolean checked) {
    this.checked = checked;
    refreshDrawableState();
  }

  @Override public void toggle() {
    setChecked(!isChecked());
  }

  @Override protected int[] onCreateDrawableState(int extraSpace) {
    int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
    if (isChecked()) {
      mergeDrawableStates(drawableState, CHECKED_STATE);
    }
    return drawableState;
  }

  private String getLibraryCountText(AppReport appReport) {
    if (appReport.librariesDetected()) {
      int count = (int) appReport.libraryCount();
      return getContext().getResources()
          .getQuantityString(R.plurals.app_list_item_libraries_found, count, count);
    } else {
      return getContext().getResources().getString(R.string.app_list_item_no_libraries_found);
    }
  }

  private @ColorRes int getLibraryCountTextColor(AppReport appReport) {
    return appReport.librariesDetected()
        ? R.color.color_text_warning
        : R.color.color_text_secondary;
  }

  private String getPermissionCountText(AppReport appReport) {
    int count = (int) appReport.permissionCount();
    return getContext().getResources()
        .getQuantityString(R.plurals.app_list_item_permissions_found, count, count);
  }

  private @ColorRes int getPermissionCountTextColor(AppReport appReport) {
    return appReport.wasAnalyzed() && appReport.permissionCount() > 0
        ? R.color.color_accent
        : R.color.color_text_secondary;
  }

  private String getAnalyzedAt(AppReport appReport) {
    String date =
        appReport.App().analyzedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    return getContext().getResources().getString(R.string.app_list_item_last_analyzed, date);
  }

  public interface OnAppClickListener {
    void onItemClick(AppReport app);
  }

  public interface OnAppLongClickListener {
    void onItemClick(AppReport app);
  }
}
