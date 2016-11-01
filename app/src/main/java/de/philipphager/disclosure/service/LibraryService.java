package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.LibraryAppRepository;
import de.philipphager.disclosure.database.library.LibraryRepository;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.library.query.QueryAll;
import de.philipphager.disclosure.database.library.query.QueryByApp;
import java.util.List;
import javax.inject.Inject;
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

  public Observable<List<Library>> all() {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.query(db, new QueryAll());
  }

  public Observable<List<Library>> allByApp(App app) {
    BriteDatabase db = databaseManager.get();
    return libraryRepository.query(db, new QueryByApp(app));
  }

  public void add(Library library) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      libraryRepository.add(db, library);
      transaction.markSuccessful();
    }
  }

  public void addForApp(App app, Library library) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      libraryAppRepository.add(db, LibraryApp.create(app.id(), library.id()));
      transaction.markSuccessful();
    }
  }
}
