package de.philipphager.disclosure.database.library.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;

@AutoValue public abstract class Library implements LibraryModel, Parcelable {
  private static final ColumnAdapter<Type> TYPE_ADAPTER = EnumColumnAdapter.create(Type.class);
  public static final Factory<Library> FACTORY =
      new LibraryModel.Factory<>((id, packageName, title, subtitle, description, type) -> builder()
          .id(id)
          .packageName(packageName)
          .title(title)
          .subtitle(subtitle)
          .description(description)
          .type(type)
          .build(), TYPE_ADAPTER);

  public static Builder builder() {
    return new AutoValue_Library.Builder();
  }

  @SuppressWarnings("PMD.ShortMethodName") public abstract Long id();

  public abstract String packageName();

  public abstract String title();

  public abstract String subtitle();

  public abstract String description();

  public abstract Type type();

  public enum Type {
    ANALYTICS, ADVERTISMENT, DEVELOPER, SOCIAL
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(Long id);

    Builder packageName(String packageName);

    Builder title(String title);

    Builder subtitle(String subtitle);

    Builder description(String description);

    Builder type(Type type);

    Library build();
  }
}
