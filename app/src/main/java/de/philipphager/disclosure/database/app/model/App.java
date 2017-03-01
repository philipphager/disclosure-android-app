package de.philipphager.disclosure.database.app.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import de.philipphager.disclosure.database.util.adapters.LocalDateTimeColumnAdapter;
import org.threeten.bp.LocalDateTime;

@AutoValue public abstract class App implements AppModel, Parcelable {
  public static final Factory<App> FACTORY = new Factory<>(
      (id, label, packageName, process, sourceDir, flags, analyzedAt, isTrusted) -> builder().id(id)
          .label(label)
          .packageName(packageName)
          .process(process)
          .sourceDir(sourceDir)
          .flags(flags)
          .analyzedAt(analyzedAt)
          .isTrusted(isTrusted)
          .build(), new LocalDateTimeColumnAdapter());

  public static Builder builder() {
    return new AutoValue_App.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName") @Nullable public abstract Long id();

  public abstract String label();

  public abstract String packageName();

  public abstract String process();

  public abstract String sourceDir();

  public abstract Integer flags();

  @Nullable public abstract LocalDateTime analyzedAt();

  public abstract Boolean isTrusted();

  public abstract App withIsTrusted(Boolean isTrusted);

  public abstract App withAnalyzedAt(LocalDateTime analyzedAt);

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(Long id);

    Builder label(String label);

    Builder packageName(String name);

    Builder process(String name);

    Builder sourceDir(String path);

    Builder flags(Integer flags);

    Builder analyzedAt(@Nullable LocalDateTime analyzedAt);

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
