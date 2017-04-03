package de.philipphager.disclosure.database.version;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqldelight.SqlDelightStatement;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.database.version.model.VersionModel;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionRepository {
  private final VersionModel.InsertVersion insertVersion;
  private final VersionModel.UpdateVersion updateVersion;

  @Inject public VersionRepository(Version.InsertVersion insertVersion,
      Version.UpdateVersion updateVersion) {
    this.insertVersion = insertVersion;
    this.updateVersion = updateVersion;
  }

  public long insert(BriteDatabase db, Version version) {
    synchronized (this) {
      insertVersion.bind(
          version.appId(),
          version.versionCode(),
          version.versionName(),
          version.createdAt());

      return db.executeInsert(insertVersion.table, insertVersion.program);
    }
  }

  public int update(BriteDatabase db, Version version) {
    synchronized (this) {
      updateVersion.bind(
          version.versionName(),
          version.createdAt(),
          version.appId(),
          version.versionCode());

      return db.executeUpdateDelete(insertVersion.table, insertVersion.program);
    }
  }

  public long insertOrUpdate(BriteDatabase db, Version version) {
    synchronized (this) {
      int updatedRows = update(db, version);

      if (updatedRows == 0) {
        return insert(db, version);
      }
      return updatedRows;
    }
  }

  public Observable<List<Version>> all(BriteDatabase db) {
    CursorToListMapper<Version> cursorToList =
        new CursorToListMapper<>(Version.FACTORY.selectAllMapper());
    SqlDelightStatement selectAll = Version.FACTORY.selectAll();

    return db.createQuery(selectAll.tables, selectAll.statement)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Version>> byAppId(BriteDatabase db, long appId) {
    SqlDelightStatement selectByApp = Version.FACTORY.selectByApp(appId);
    CursorToListMapper<Version> cursorToList =
        new CursorToListMapper<>(Version.FACTORY.selectByAppMapper());

    return db.createQuery(selectByApp.tables, selectByApp.statement, selectByApp.args)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }
}
