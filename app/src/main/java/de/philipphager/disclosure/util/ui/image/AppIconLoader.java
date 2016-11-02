package de.philipphager.disclosure.util.ui.image;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.widget.ImageView;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class AppIconLoader {
  private final String packageName;
  private final Integer placeholderRes;
  private final Context context;
  private final ImageView view;
  private final Scheduler scheduler;

  public AppIconLoader(Builder builder) {
    this.packageName = builder.packageName;
    this.placeholderRes = builder.placeholderRes;
    this.scheduler = builder.scheduler;
    this.context = builder.context;
    view = builder.view;
  }

  public static Builder with(Context context) {
    return new Builder(context);
  }

  public void load() {
    PackageManager packageManager = context.getPackageManager();

    if (placeholderRes != null) {
      Drawable placeholder = context.getResources().getDrawable(placeholderRes, null);
      view.setImageDrawable(placeholder);
    }

    Observable.fromCallable(() -> packageManager.getApplicationIcon(packageName))
        .subscribeOn(scheduler)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(view::setImageDrawable, Timber::e);
  }

  public static final class Builder {
    private final Context context;
    private ImageView view;
    private Integer placeholderRes;
    private String packageName;
    private Scheduler scheduler;

    public Builder(Context context) {
      this.context = context;
      this.packageName = "";
      this.scheduler = Schedulers.io();
    }

    public Builder load(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder withPlaceholder(@IntegerRes Integer placeholderRes) {
      this.placeholderRes = placeholderRes;
      return this;
    }

    public Builder onThread(Scheduler scheduler) {
      this.scheduler = scheduler;
      return this;
    }

    public void into(ImageView view) {
      this.view = view;
      new AppIconLoader(this).load();
    }
  }
}

