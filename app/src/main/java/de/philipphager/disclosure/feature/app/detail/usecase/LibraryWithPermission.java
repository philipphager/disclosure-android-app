package de.philipphager.disclosure.feature.app.detail.usecase;

import com.google.auto.value.AutoValue;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.permission.model.Permission;
import java.util.List;

@AutoValue public abstract class LibraryWithPermission {
  public static LibraryWithPermission create(Library library, List<Permission> permissions) {
    return new AutoValue_LibraryWithPermission(library, permissions);
  }

  public abstract Library library();

  public abstract List<Permission> permissions();
}
