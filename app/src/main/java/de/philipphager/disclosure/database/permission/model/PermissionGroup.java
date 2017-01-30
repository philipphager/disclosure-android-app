package de.philipphager.disclosure.database.permission.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class PermissionGroup implements PermissionGroupModel {
  public static final PermissionGroupModel.Factory FACTORY = new Factory(PermissionGroup::create);

  public static PermissionGroup create(String id, String title, String description) {
    return new AutoValue_PermissionGroup(id, title, description);
  }

  public abstract String id();

  public abstract String title();

  public abstract String description();
}
