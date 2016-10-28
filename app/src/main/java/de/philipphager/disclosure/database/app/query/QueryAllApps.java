package de.philipphager.disclosure.database.app.query;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppModel;
import de.philipphager.disclosure.database.util.query.BriteQuery;

public class QueryAllApps implements BriteQuery<App> {
  @Override public QueryObservable create(BriteDatabase db) {
    return db.createQuery(App.TABLE_NAME, App.SELECTALL);
  }

  @Override public AppModel.Mapper<App> rowMapper() {
    return App.FACTORY.selectAllMapper();
  }
}
