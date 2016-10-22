package de.philipphager.disclosure.database.version;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionRepository implements Repository<Version> {

  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public VersionRepository() {
    // Needed for dagger injection.
  }

  @Override public long add(SQLiteDatabase db, Version version) {
    synchronized (this) {
      return db.replace(Version.TABLE_NAME, null,
          Version.FACTORY.marshal(version).asContentValues());
    }
  }

  @Override public void add(SQLiteDatabase db, Iterable<Version> versions) {
    // TODO: Implement
  }

  @Override public void update(SQLiteDatabase db, Version version) {
    // TODO: Implement
  }

  @Override public void remove(SQLiteDatabase db, Version version) {
    // TODO: Implement
  }

  @Override public Observable<List<Version>> query(BriteDatabase db, BriteQuery<Version> query) {
    CursorToListMapper<Version> cursorToVersionList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToVersionList);
  }
}
