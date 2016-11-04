package de.philipphager.disclosure.database.library.populator;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.library.model.Library;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LibraryPopulator {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public LibraryPopulator() {
    // Needed for dagger injection.
  }

  public void populate(SQLiteDatabase db) {
    List<Library> libraries = new ArrayList<>();
    libraries.add(
        Library.create(1L,
            "com.mixpanel.android", "Mixpanel",
            "Tracks app interaction and user behaviour",
            "• Tracks actions, like button clicks \n"
            + "• Beta testing, show different versions of an app to different users \n"
            + "• Tracks basic user information (name, email, location)",
            Library.Type.ANALYTICS));

    libraries.add(
        Library.create(2L, "com.google.android.gms.analytics",
            "Google Analytics",
            "Google's flagship analytics platform",
            "",
        Library.Type.ANALYTICS));

    libraries.add(
        Library.create(3L,
            "com.facebook.stetho",
            "Facebook Stetho",
            "Chrome developer tools for Android",
            "• Inspect views, layouts \n"
            + "• Inspect and edit SQLite and shared preferences  \n"
            + "• Analyse network traffic",
            Library.Type.DEVELOPER));

    for (Library library : libraries) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      db.insert(Library.TABLE_NAME, null, content);
    }
  }
}
