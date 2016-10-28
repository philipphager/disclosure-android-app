package de.philipphager.disclosure.database.info;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.mapper.CursorToListMapper;
import de.philipphager.disclosure.database.util.query.BriteQuery;
import de.philipphager.disclosure.database.util.query.SQLQuery;
import de.philipphager.disclosure.database.util.repository.Queryable;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppInfoRepository implements Queryable<App.Info> {
  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public AppInfoRepository() {
    // Needed for dagger injection.
  }

  @Override public List<App.Info> query(BriteDatabase db, SQLQuery<App.Info> query) {
    CursorToListMapper<App.Info> cursorToList = new CursorToListMapper<>(query.rowMapper());
    Cursor cursor = db.query(query.create());
    return cursorToList.call(cursor);
  }

  @Override
  public Observable<List<App.Info>> query(BriteDatabase db, BriteQuery<App.Info> query) {
    CursorToListMapper<App.Info> cursorToList = new CursorToListMapper<>(query.rowMapper());
    return query.create(db).map(SqlBrite.Query::run).map(cursorToList);
  }
}
