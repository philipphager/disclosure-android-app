package de.philipphager.disclosure.database.permission.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

@AutoValue public abstract class Permission implements PermissionModel, Parcelable {
  public static final PermissionModel.Factory<Permission> FACTORY = new Factory<Permission>(
      (id, title, description, protectionLevel, permissionGroup) -> builder()
          .id(id)
          .title(title)
          .description(description)
          .protectionLevel(protectionLevel)
          .permissionGroup(permissionGroup)
          .build());

  public static Builder builder() {
    return new AutoValue_Permission.Builder();
  }

  @SerializedName("_id") @SuppressWarnings("PMD.ShortMethodName")
  public abstract String id();

  @Nullable public abstract String title();

  @Nullable public abstract String description();

  @Nullable public abstract Integer protectionLevel();

  @Nullable public abstract String permissionGroup();

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(String id);

    Builder title(String title);

    Builder description(String description);

    Builder protectionLevel(Integer protectionLevel);

    Builder permissionGroup(String permissionGroup);

    Permission build();
  }
}
