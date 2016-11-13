package de.philipphager.disclosure.database.feature;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.feature.model.Feature;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.util.time.Date;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class FeatureRepository {
  private final Feature.InsertFeature insertFeature;
  private final Feature.UpdateFeature updateFeature;

  @Inject public FeatureRepository(Feature.InsertFeature insertFeature,
      Feature.UpdateFeature updateFeature) {
    this.insertFeature = insertFeature;
    this.updateFeature = updateFeature;
  }

  public long insert(BriteDatabase db, Feature feature) {
    synchronized (this) {
      insertFeature.bind(feature.id(),
          feature.title(),
          feature.description(),
          feature.createdAt(),
          feature.updatedAt());
      return db.executeInsert(insertFeature.table, insertFeature.program);
    }
  }

  public int update(BriteDatabase db, Feature feature) {
    synchronized (this) {
      updateFeature.bind(feature.title(),
          feature.description(),
          feature.createdAt(),
          feature.updatedAt(),
          feature.id());
      return db.executeUpdateDelete(updateFeature.table, updateFeature.program);
    }
  }

  public Observable<List<Feature>> all(BriteDatabase db) {
    CursorToListMapper<Feature> cursorToList =
        new CursorToListMapper<>(Feature.FACTORY.selectAllMapper());

    return db.createQuery(Feature.TABLE_NAME, Feature.SELECTALL)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Feature>> byLibrary(BriteDatabase db, String libraryId) {
    SqlDelightStatement selectByLibrary = Feature.FACTORY.selectByLibrary(libraryId);
    CursorToListMapper<Feature> cursorToList =
        new CursorToListMapper<>(Feature.FACTORY.selectByLibraryMapper());

    return db.createQuery(selectByLibrary.tables, selectByLibrary.statement, selectByLibrary.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<OffsetDateTime> lastUpdated(BriteDatabase db) {
    return db.createQuery(Feature.TABLE_NAME, Feature.SELECTLASTUPDATED)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return Feature.FACTORY.selectLastUpdatedMapper().map(cursor);
          }
          return Date.MIN;
        });
  }
}
