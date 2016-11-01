package de.philipphager.disclosure.database.library.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;

@AutoValue public abstract class Library implements LibraryModel, Parcelable {
  private static final ColumnAdapter<Type> TYPE_ADAPTER = EnumColumnAdapter.create(Type.class);
  public static final Factory<Library> FACTORY = new LibraryModel.Factory<>(
      (id, title, description, packageName, type) ->
          Library.create(id, title, description, type, packageName), TYPE_ADAPTER);

  public static Library create(long id, String title, String description, @Nullable Type type,
      String packageName) {
    return new AutoValue_Library(id, title, description, packageName, type);
  }

  public abstract Long id();

  public abstract String title();

  public abstract String description();

  public abstract String packageName();

  public abstract Type type();

  public enum Type {
    ANALYTICS, ADVERTISMENT, DEVELOPER, SOCIAL
  }
}
