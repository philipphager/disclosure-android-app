package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.feature.FeatureRepository;
import de.philipphager.disclosure.database.feature.model.Feature;
import de.philipphager.disclosure.database.library.repositories.LibraryFeatureRepository;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class FeatureService {
  private final DatabaseManager databaseManager;
  private final FeatureRepository featureRepository;
  private final LibraryFeatureRepository libraryFeatureRepository;

  @Inject public FeatureService(DatabaseManager databaseManager,
      FeatureRepository featureRepository,
      LibraryFeatureRepository libraryFeatureRepository) {
    this.databaseManager = databaseManager;
    this.featureRepository = featureRepository;
    this.libraryFeatureRepository = libraryFeatureRepository;
  }

  public void insertOrUpdate(List<Feature> features) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (Feature feature : features) {
        int updatedRows = featureRepository.update(db, feature);

        if (updatedRows == 0) {
          featureRepository.insert(db, feature);
        }
      }
      transaction.markSuccessful();
    }
  }

  public void insertOrUpdateForLibrary(List<LibraryFeature> libraryFeatures) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (LibraryFeature libraryFeature : libraryFeatures) {
        int updatedRows = libraryFeatureRepository.update(db, libraryFeature);

        if (updatedRows == 0) {
          libraryFeatureRepository.insert(db, libraryFeature);
        }
      }
      transaction.markSuccessful();
    }
  }

  public Observable<List<Feature>> byLibrary(String libraryId) {
    BriteDatabase db = databaseManager.get();
    return featureRepository.byLibrary(db, libraryId);
  }

  public Observable<OffsetDateTime> lastUpdated() {
    BriteDatabase db = databaseManager.get();
    return featureRepository.lastUpdated(db);
  }

  public Observable<OffsetDateTime> lastUpdatedLibraryFeatures() {
    BriteDatabase db = databaseManager.get();
    return featureRepository.lastUpdated(db);
  }
}
