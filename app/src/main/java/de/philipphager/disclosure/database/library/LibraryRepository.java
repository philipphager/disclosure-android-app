package de.philipphager.disclosure.database.library;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryApp;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.util.time.Date;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class LibraryRepository {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public LibraryRepository() {
    // Needed for dagger injection.
  }

  public long put(BriteDatabase db, Library library) {
    synchronized (this) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      return db.insert(Library.TABLE_NAME, content);
    }
  }

  public long putForApp(BriteDatabase db, long libraryId, long appId) {
    synchronized (this) {
      ContentValues content = new ContentValues(2);
      content.put("libraryId", libraryId);
      content.put("appId", appId);

      return db.insert(LibraryApp.TABLE_NAME, content, SQLiteDatabase.CONFLICT_IGNORE);
    }
  }

  public Observable<List<Library>> all(BriteDatabase db) {
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectAllMapper());

    return db.createQuery(App.TABLE_NAME, Library.SELECTALL)
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<List<Library>> byApp(BriteDatabase db, long appId) {
    CursorToListMapper<Library> cursorToList =
        new CursorToListMapper<>(Library.FACTORY.selectByAppMapper());

    List<String> tables = Arrays.asList(LibraryApp.TABLE_NAME, Library.TABLE_NAME);
    return db.createQuery(tables, Library.SELECTBYAPP, String.valueOf(appId))
        .map(SqlBrite.Query::run)
        .map(cursorToList);
  }

  public Observable<OffsetDateTime> lastUpdated(BriteDatabase db) {
    return db.createQuery(Library.TABLE_NAME, Library.SELECTLASTUPDATED)
        .map(SqlBrite.Query::run)
        .map(cursor -> {
          if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return Library.FACTORY.selectLastUpdatedMapper().map(cursor);
          }
          return Date.MIN;
        });
  }
}
