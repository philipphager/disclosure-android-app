package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.repositories.LibraryAppRepository;
import de.philipphager.disclosure.database.library.repositories.LibraryRepository;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class LibraryService {
  private final DatabaseManager databaseManager;
  private final LibraryRepository libraryRepository;
  private final LibraryAppRepository libraryAppRepository;

  @Inject public LibraryService(DatabaseManager databaseManager,
      LibraryRepository libraryRepository,
      LibraryAppRepository libraryAppRepository) {
    this.databaseManager = databaseManager;
    this.libraryRepository = libraryRepository;
    this.libraryAppRepository = libraryAppRepository;
  }

  public void insert(List<Library> libraries) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Library library : libraries) {
        libraryRepository.insert(db, library);
      }

      transaction.markSuccessful();
    }
  }

  public void insertOrUpdate(List<Library> libraries) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Library library : libraries) {
        int updatedRows = libraryRepository.update(db, library);

        if (updatedRows == 0) {
          libraryRepository.insert(db, library);
        }
      }
      transaction.markSuccessful();
    }
  }

  public void insertForApp(App app, List<Library> libraries) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Library library : libraries) {
        libraryAppRepository.insert(db, library.id(), app.id());
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

  public Observable<OffsetDateTime> lastUpdated() {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.lastUpdated(db);
  }
}
