package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.LibraryRepository;
import de.philipphager.disclosure.database.library.model.Library;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class LibraryService {
  private final DatabaseManager databaseManager;
  private final LibraryRepository libraryRepository;

  @Inject public LibraryService(DatabaseManager databaseManager,
      LibraryRepository libraryRepository) {
    this.databaseManager = databaseManager;
    this.libraryRepository = libraryRepository;
  }

  public void put(List<Library> libraries) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Library library : libraries) {
        libraryRepository.put(db, library);
      }

      transaction.markSuccessful();
    }
  }

  public void putForApp(App app, List<Library> libraries) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Library library : libraries) {
        libraryRepository.putForApp(db, library.id(), app.id());
      }

      transaction.markSuccessful();
    }
  }

  public Observable<List<Library>> all() {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.all(db);
  }

  public Observable<List<Library>> byApp(App app) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byApp(db, app.id());
  }
}
