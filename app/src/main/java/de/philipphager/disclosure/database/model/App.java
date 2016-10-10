package de.philipphager.disclosure.database.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class App implements AppModel {
  public static final Factory<App> FACTORY = new Factory<>(
      (id, label, packageName, process, sourceDir, flags) -> builder().id(id)
          .label(label)
          .packageName(packageName)
          .process(process)
          .sourceDir(sourceDir)
          .flags(flags)
          .build());

  public static Builder builder() {
    return new AutoValue_App.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName")
  public abstract Long id();

  @Nullable public abstract String label();

  public abstract String packageName();

  public abstract String process();

  public abstract String sourceDir();

  public abstract Integer flags();

  public boolean hasLabel() {
    return label() != null;
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName")
    Builder id(Long id);

    Builder label(@Nullable String label);

    Builder packageName(String name);

    Builder process(String name);

    Builder sourceDir(String path);

    Builder flags(Integer flags);

    App build();
  }
}
