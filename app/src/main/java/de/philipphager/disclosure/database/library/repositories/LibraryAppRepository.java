package de.philipphager.disclosure.database.library.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.model.LibraryAppModel;
import javax.inject.Inject;

public class LibraryAppRepository {
  private final LibraryAppModel.InsertLibraryApp insertLibraryApp;

  @Inject public LibraryAppRepository(LibraryApp.InsertLibraryApp insertLibraryApp) {
    this.insertLibraryApp = insertLibraryApp;
  }

  public long insert(BriteDatabase db, String libraryId, long appId) {
    synchronized (this) {
      insertLibraryApp.bind(appId, libraryId);

      return db.executeInsert(insertLibraryApp.table, insertLibraryApp.program);
    }
  }
}
