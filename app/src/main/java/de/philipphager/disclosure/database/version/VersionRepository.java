package de.philipphager.disclosure.database.version;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.util.query.SQLQuery;
import de.philipphager.disclosure.database.util.query.SQLSelector;
import de.philipphager.disclosure.database.util.repository.Repository;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionRepository implements Repository<Version> {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public VersionRepository() {
    // Needed for dagger injection.
  }

  @Override public long add(BriteDatabase db, Version version) {
    synchronized (this) {
      ContentValues versionContent = Version.FACTORY.marshal(version).asContentValues();
      return db.insert(Version.TABLE_NAME, versionContent, SQLiteDatabase.CONFLICT_REPLACE);
    }
  }

  @Override public int update(BriteDatabase db, Version version) {
    synchronized (this) {
      ContentValues content = Version.FACTORY.marshal(version).asContentValues();
      String where = String.format("%s = %s AND %s = %s",
          Version.APPID, version.appId(),
          Version.VERSIONCODE, version.versionCode());
      return db.update(Version.TABLE_NAME, content, where);
    }
  }

  @Override public int remove(BriteDatabase db, SQLSelector selector) {
    return db.delete(Version.TABLE_NAME, selector.create());
  }

  @Override public List<Version> query(BriteDatabase db, SQLQuery<Version> query) {
    CursorToListMapper<Version> cursorToList = new CursorToListMapper<>(query.rowMapper());
    Cursor cursor = db.query(query.create());
    return cursorToList.call(cursor);
  }

  @Override public Observable<List<Version>> query(BriteDatabase db, BriteQuery<Version> query) {
    CursorToListMapper<Version> cursorToList = new CursorToListMapper<>(query.rowMapper());
    return query.create(db).map(SqlBrite.Query::run).map(cursorToList);
  }
}
