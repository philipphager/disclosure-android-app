package de.philipphager.disclosure.database.version;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.util.SQLQuery;
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

  @Override public void add(BriteDatabase db, Iterable<Version> versions) {
    // TODO: Implement
  }

  @Override public void update(BriteDatabase db, Version version) {
    // TODO: Implement
  }

  @Override public void remove(BriteDatabase db, SQLQuery sqlQuery) {
    // TODO: Implement
  }

  @Override public Observable<List<Version>> query(BriteDatabase db, BriteQuery<Version> query) {
    CursorToListMapper<Version> cursorToVersionList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToVersionList);
  }
}
