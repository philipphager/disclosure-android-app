package de.philipphager.disclosure.database.library.model;

import com.google.auto.value.AutoValue;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.feature.app.detail.usecase.AutoValue_LibraryWithPermission;
import java.util.List;

@AutoValue public abstract class LibraryWithPermission {
  public static LibraryWithPermission create(Library library, List<Permission> permissions) {
    return new AutoValue_LibraryWithPermission(library, permissions);
  }

  public abstract Library library();

  public abstract List<Permission> permissions();
}
