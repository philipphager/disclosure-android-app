package de.philipphager.disclosure.database.library.repositories;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import de.philipphager.disclosure.database.library.model.LibraryFeatureModel;
import de.philipphager.disclosure.util.time.Date;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class LibraryFeatureRepository {
  private final LibraryFeature.InsertLibraryFeature insertLibraryFeature;
  private final LibraryFeatureModel.UpdateLibraryFeature updateLibraryFeature;

  @Inject public LibraryFeatureRepository(LibraryFeature.InsertLibraryFeature insertLibraryFeature,
      LibraryFeature.UpdateLibraryFeature updateLibraryFeature) {
    this.insertLibraryFeature = insertLibraryFeature;
    this.updateLibraryFeature = updateLibraryFeature;
  }

  public long insert(BriteDatabase db, LibraryFeature libraryFeature) {
    synchronized (this) {
      insertLibraryFeature.bind(libraryFeature.id(),
          libraryFeature.libraryId(),
          libraryFeature.featureId(),
          libraryFeature.createdAt(),
          libraryFeature.updatedAt());
      return db.executeInsert(insertLibraryFeature.table, insertLibraryFeature.program);
    }
  }

  public int update(BriteDatabase db, LibraryFeature libraryFeature) {
    synchronized (this) {
      updateLibraryFeature.bind(libraryFeature.libraryId(),
          libraryFeature.featureId(),
          libraryFeature.createdAt(),
          libraryFeature.updatedAt(),
          libraryFeature.id());
      return db.executeUpdateDelete(updateLibraryFeature.table, updateLibraryFeature.program);
    }
  }

  public Observable<OffsetDateTime> lastUpdated(BriteDatabase db) {
    return db.createQuery(LibraryFeature.TABLE_NAME, LibraryFeature.SELECTLASTUPDATED)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return LibraryFeature.FACTORY.selectLastUpdatedMapper().map(cursor);
          }
          return Date.MIN;
        });
  }
}
