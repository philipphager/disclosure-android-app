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
        Library.builder()
            .id(1L)
            .packageName("com.mixpanel.android")
            .title("Mixpanel")
            .subtitle("Tracks app interaction and user behaviour")
            .description("• Tracks actions, like button clicks \n"
                + "• Beta testing, show different versions of an app to different users \n"
                + "• Tracks basic user information (name, email, location)")
            .type(Library.Type.ANALYTICS)
            .build());

    libraries.add(
        Library.builder()
            .id(2L)
            .packageName("com.google.android.gms.analytics")
            .title("Google Analytics")
            .subtitle("Google's flagship analytics platform")
            .description("")
            .type(Library.Type.ANALYTICS)
            .build());

    libraries.add(
        Library.builder()
            .id(3L)
            .packageName("com.facebook.stetho")
            .title("Facebook Stetho")
            .subtitle("Chrome developer tools for Android")
            .description("• Inspect views, layouts \n"
                + "• Inspect and edit SQLite and shared preferences  \n"
                + "• Analyse network traffic")
            .type(Library.Type.DEVELOPER)
            .build());

    for (Library library : libraries) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      db.insert(Library.TABLE_NAME, null, content);
    }
  }
}
