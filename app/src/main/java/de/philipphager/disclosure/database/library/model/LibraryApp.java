package de.philipphager.disclosure.database.library.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class LibraryApp implements LibraryAppModel {
  public static final Factory<LibraryApp> FACTORY = new Factory<>(LibraryApp::create);

  public static LibraryApp create(Long appId, String libraryId) {
    return new AutoValue_LibraryApp(appId, libraryId);
  }

  public abstract Long appId();

  public abstract String libraryId();
}
