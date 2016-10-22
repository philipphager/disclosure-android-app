package de.philipphager.disclosure.database.app.info;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.util.BriteQuery;
import de.philipphager.disclosure.database.util.CursorToListMapper;
import de.philipphager.disclosure.database.util.Queryable;
import java.util.List;
import rx.Observable;

public class AppInfoRepository implements Queryable<App.Info> {
  @SuppressWarnings("PMD.UnnecessaryConstructor") public AppInfoRepository() {
    // Needed for dagger injection.
  }

  @Override
  public Observable<List<App.Info>> query(BriteDatabase db, BriteQuery<App.Info> query) {
    CursorToListMapper<App.Info> cursorToAppInfoList = new CursorToListMapper<>(query.rowMapper());
    return query.createQuery(db).map(cursorToAppInfoList);
  }
}
