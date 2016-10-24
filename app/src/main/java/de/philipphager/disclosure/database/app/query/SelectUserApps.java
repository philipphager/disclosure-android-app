package de.philipphager.disclosure.database.app.query;

import android.content.pm.ApplicationInfo;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.model.AppModel;
import de.philipphager.disclosure.database.util.BriteQuery;

public class SelectUserApps implements BriteQuery<App> {
  @Override public QueryObservable createQuery(BriteDatabase db) {
    return db.createQuery(App.TABLE_NAME, App.SELECTUSERAPPS,
        String.valueOf(ApplicationInfo.FLAG_SYSTEM));
  }

  @Override public AppModel.Mapper<App> rowMapper() {
    return App.FACTORY.selectUserAppsMapper();
  }
}
