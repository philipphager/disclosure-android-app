package de.philipphager.disclosure.database.library.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class LibraryCategory implements Parcelable {
  public static LibraryCategory create(Library.Type type, Long allLibraries, Long usedLibraries) {
    return new AutoValue_LibraryCategory(type, allLibraries, usedLibraries);
  }

  public abstract Library.Type type();

  public abstract Long allLibraries();

  public abstract Long usedLibraries();
}
