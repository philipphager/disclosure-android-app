package de.philipphager.disclosure.database.app.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import static de.philipphager.disclosure.database.app.model.App.FACTORY;

@AutoValue public abstract class AppWithPermissions
    implements App.SelectAllWithPermissionCountModel {
  public static final RowMapper<AppWithPermissions> MAPPER =
      FACTORY.selectAllWithPermissionCountMapper(
          (id, label, packageName, process, sourceDir, flags, isTrusted, permissionCount) -> builder()
              .id(id)
              .label(label)
              .packageName(packageName)
              .process(process)
              .sourceDir(sourceDir)
              .flags(flags)
              .isTrusted(isTrusted)
              .permissionCount(permissionCount)
              .build());

  public static Builder builder() {
    return new AutoValue_AppWithPermissions.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName") @Nullable public abstract Long id();

  public abstract String label();

  public abstract String packageName();

  public abstract String process();

  public abstract String sourceDir();

  public abstract Integer flags();

  public abstract Boolean isTrusted();

  public abstract long permissionCount();

  @SuppressWarnings("PMD.UnnecessaryWrapperObjectCreation")
  public int permissionCountInt() {
    return Long.valueOf(permissionCount()).intValue();
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(Long id);

    Builder label(String label);

    Builder packageName(String name);

    Builder process(String name);

    Builder sourceDir(String path);

    Builder flags(Integer flags);

    Builder isTrusted(Boolean isTrusted);

    Builder permissionCount(long permissionCount);

    AppWithPermissions build();
  }
}
