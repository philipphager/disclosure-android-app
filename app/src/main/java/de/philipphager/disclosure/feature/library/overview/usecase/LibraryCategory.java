package de.philipphager.disclosure.feature.library.overview.usecase;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import de.philipphager.disclosure.database.library.model.Library;

@AutoValue public abstract class LibraryCategory implements Parcelable {
  public static LibraryCategory create(Library.Type type, Long allLibraries, Long usedLibraries) {
    return new AutoValue_LibraryCategory(type, allLibraries, usedLibraries);
  }

  public abstract Library.Type type();

  public abstract Long allLibraries();

  public abstract Long usedLibraries();
}
