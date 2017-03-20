package de.philipphager.disclosure.database.app.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.util.adapters.LocalDateTimeColumnAdapter;
import org.threeten.bp.LocalDateTime;

@AutoValue public abstract class App implements AppModel, Parcelable {
  public static final Factory<App> FACTORY = new Factory<>(
      (id, label, packageName, process, sourceDir, targetSdk, flags, analyzedAt) -> builder().id(id)
          .label(label)
          .packageName(packageName)
          .process(process)
          .sourceDir(sourceDir)
          .targetSdk(targetSdk)
          .flags(flags)
          .analyzedAt(analyzedAt)
          .build(), new LocalDateTimeColumnAdapter());

  public static Builder builder() {
    return new AutoValue_App.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName") @Nullable public abstract Long id();

  public abstract String label();

  public abstract String packageName();

  public abstract String process();

  public abstract String sourceDir();

  public abstract Integer targetSdk();

  public abstract Integer flags();

  @Nullable public abstract LocalDateTime analyzedAt();

  public abstract App withAnalyzedAt(LocalDateTime analyzedAt);

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(Long id);

    Builder label(String label);

    Builder packageName(String name);

    Builder process(String name);

    Builder sourceDir(String path);

    Builder targetSdk(Integer targetSdk);

    Builder flags(Integer flags);

    Builder analyzedAt(@Nullable LocalDateTime analyzedAt);

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
