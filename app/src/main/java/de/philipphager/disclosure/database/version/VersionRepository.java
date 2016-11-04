package de.philipphager.disclosure.database.version;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionRepository {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public VersionRepository() {
    // Needed for dagger injection.
  }

  public long insert(BriteDatabase db, Version version) {
    synchronized (this) {
      ContentValues versionContent = Version.FACTORY.marshal(version).asContentValues();
      return db.insert(Version.TABLE_NAME, versionContent, SQLiteDatabase.CONFLICT_REPLACE);
    }
  }

  public int update(BriteDatabase db, Version version) {
    synchronized (this) {
      ContentValues content = Version.FACTORY.marshal(version).asContentValues();
      String where = String.format("%s = %s AND %s = %s",
          Version.APPID, version.appId(),
          Version.VERSIONCODE, version.versionCode());
      return db.update(Version.TABLE_NAME, content, where);
    }
  }

  public int remove(BriteDatabase db, String where) {
    return db.delete(Version.TABLE_NAME, where);
  }

  public Observable<List<Version>> all(BriteDatabase db) {
    CursorToListMapper<Version> cursorToList =
        new CursorToListMapper<>(Version.FACTORY.selectAllMapper());

    return db.createQuery(Version.TABLE_NAME, Version.SELECTALL)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Version>> byAppId(BriteDatabase db, long appId) {
    CursorToListMapper<Version> cursorToList =
        new CursorToListMapper<>(Version.FACTORY.selectByAppMapper());

    return db.createQuery(Version.TABLE_NAME, Version.SELECTBYAPP, String.valueOf(appId))
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }
}
