package de.philipphager.disclosure.database.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.util.query.SQLQuery;
import de.philipphager.disclosure.database.util.query.SQLSelector;
import de.philipphager.disclosure.database.util.repository.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class LibraryRepository implements Repository<Library> {
  @Inject public LibraryRepository() {
  }

  @Override public long add(BriteDatabase db, Library library) {
    synchronized (this) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      return db.insert(Library.TABLE_NAME, content, SQLiteDatabase.CONFLICT_IGNORE);
    }
  }

  @Override public int update(BriteDatabase db, Library library) {
    synchronized (this) {
      ContentValues content = Library.FACTORY.marshal(library).asContentValues();
      return db.update(Library.TABLE_NAME, content,
          String.format("%s=%s", Library.ID, library.id()));
    }
  }

  @Override public int remove(BriteDatabase db, SQLSelector selector) {
    return db.delete(Library.TABLE_NAME, selector.create());
  }

  @Override public List<Library> query(BriteDatabase db, SQLQuery<Library> query) {
    CursorToListMapper<Library> cursorToLibraryList = new CursorToListMapper<>(query.rowMapper());
    Cursor cursor = db.query(query.create());
    return cursorToLibraryList.call(cursor);
  }

  @Override public Observable<List<Library>> query(BriteDatabase db, BriteQuery<Library> query) {
    CursorToListMapper<Library> cursorToLibraryList = new CursorToListMapper<>(query.rowMapper());
    return query.create(db).map(SqlBrite.Query::run).map(cursorToLibraryList);
  }
}
