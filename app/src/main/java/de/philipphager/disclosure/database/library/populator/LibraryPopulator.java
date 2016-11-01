package de.philipphager.disclosure.database.library.populator;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.library.model.Library;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LibraryPopulator {
  @Inject public LibraryPopulator() {
  }

  public void populate(SQLiteDatabase db) {
    List<Library> libraries = new ArrayList<>();
    libraries.add(Library.create(1L, "Mixpanel", "", Library.Type.ANALYTICS, "com.mixpanel.android"));
    libraries.add(Library.create(2L, "Google Analytics", "", Library.Type.ANALYTICS, "com.google.android.gms.analytics"));
    libraries.add(Library.create(3L, "Facebook Stetho", "", Library.Type.DEVELOPER, "com.facebook.stetho"));

    for (Library library : libraries) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      db.insert(Library.TABLE_NAME, null, content);
    }
  }
}
