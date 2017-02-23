package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryInfo;
import de.philipphager.disclosure.database.library.repositories.LibraryAppRepository;
import de.philipphager.disclosure.database.library.repositories.LibraryRepository;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

import static de.philipphager.disclosure.util.assertion.Assertions.ensureNotNull;

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

  public Observable<Library> byId(String id) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byId(db, id);
  }

  public Observable<List<Library>> byApp(App app) {
    ensureNotNull(app, "must provide app");
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byApp(db, app.id());
  }

  public Observable<List<Library>> byAppUpdateOnPermissionChange(App app) {
    ensureNotNull(app, "must provide app");
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byAppWithPermissionUpdate(db, app.id());
  }

  public Observable<List<Library>> byFeature(String featureId) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byFeature(db, featureId);
  }

  public Observable<List<Library>> byType(Library.Type type) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.byType(db, type);
  }

  public Observable<List<LibraryInfo>> infoByType(Library.Type type) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.infoByType(db, type);
  }

  public Observable<OffsetDateTime> lastUpdated() {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.lastUpdated(db);
  }

  public Observable<Long> countByType(Library.Type type) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.countByType(db, type);
  }

  public Observable<Long> countUsedLibrariesByType(Library.Type type) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.countUsedLibrariesByType(db, type);
  }
}
