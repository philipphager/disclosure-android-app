package de.philipphager.disclosure.database.library;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.util.query.SQLSelector;
import de.philipphager.disclosure.database.util.repository.Editable;
import javax.inject.Inject;

public class LibraryAppRepository implements Editable<LibraryApp> {
  @Inject public LibraryAppRepository() {
  }

  @Override public long add(BriteDatabase db, LibraryApp item) {
    ContentValues content = LibraryApp.FACTORY.marshal(item).asContentValues();
    return db.insert(LibraryApp.TABLE_NAME, content, SQLiteDatabase.CONFLICT_IGNORE);
  }

  @Override public int update(BriteDatabase db, LibraryApp item) {
    return 0;
  }

  @Override public int remove(BriteDatabase db, SQLSelector s) {
    return 0;
  }
}
