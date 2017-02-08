package de.philipphager.disclosure.database.app.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue public abstract class App implements AppModel, Parcelable {
  public static final Factory<App> FACTORY = new Factory<>(
      (id, label, packageName, process, sourceDir, flags, isTrusted) -> builder().id(id)
          .label(label)
          .packageName(packageName)
          .process(process)
          .sourceDir(sourceDir)
          .flags(flags)
          .isTrusted(isTrusted)
          .build());

  public static Builder builder() {
    return new AutoValue_App.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName") @Nullable public abstract Long id();

  public abstract String label();

  public abstract String packageName();

  public abstract String process();

  public abstract String sourceDir();

  public abstract Integer flags();

  public abstract Boolean isTrusted();

  public boolean hasLabel() {
    return label() != null;
  }

  public App editTrust(App app, boolean isTrusted) {
    return App.builder()
        .id(app.id())
        .packageName(app.packageName())
        .label(app.label())
        .process(app.process())
        .sourceDir(app.sourceDir())
        .flags(app.flags())
        .isTrusted(isTrusted)
        .build();
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(Long id);

    Builder label(String label);

    Builder packageName(String name);

    Builder process(String name);

    Builder sourceDir(String path);

    Builder flags(Integer flags);

    Builder isTrusted(Boolean isTrusted);

    App build();
  }

  @AutoValue public static abstract class Info implements SelectAllInfosModel {
    public static final RowMapper<Info> MAPPER =
        FACTORY.selectAllInfosMapper(AutoValue_App_Info::create);

    public static Info create(String packageName, int versionCode) {
      return new AutoValue_App_Info(packageName, versionCode);
    }
  }
}
