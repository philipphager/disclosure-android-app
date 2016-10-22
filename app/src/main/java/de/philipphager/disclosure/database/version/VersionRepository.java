package de.philipphager.disclosure.database.version;

import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.model.Version;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionRepository implements Repository<Version> {
  private final DatabaseManager databaseManager;

  @Inject public VersionRepository(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  @Override public synchronized long add(Version version) {
    SQLiteDatabase db = databaseManager.openWriteable();
    return db.replace(Version.TABLE_NAME, null, Version.FACTORY.marshal(version).asContentValues());
  }

  @Override public void add(Iterable<Version> versions) {
    // TODO: Implement
  }

  @Override public void update(Version version) {
    // TODO: Implement
  }

  @Override public void remove(Version version) {
    // TODO: Implement
  }

  @Override public Observable<List<Version>> query(BriteQuery<Version> query) {
    try (BriteDatabase db = databaseManager.openReadable()) {

      CursorToListMapper<Version> cursorToVersionList = new CursorToListMapper<>(query.rowMapper());
      return query.createQuery(db).map(cursorToVersionList);
    }
  }
}
