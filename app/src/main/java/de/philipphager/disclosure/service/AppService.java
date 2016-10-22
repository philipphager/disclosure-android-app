package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.app.mapper.ToAppMapper;
import de.philipphager.disclosure.database.app.model.App;
import de.philipphager.disclosure.database.app.query.SelectAllAppsQuery;
import de.philipphager.disclosure.database.app.query.SelectAppsByNameQuery;
import de.philipphager.disclosure.database.util.Repository;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AppService {
  private final Repository<App> appRepository;
  private final ToAppMapper toAppMapper;

  @Inject public AppService(Repository<App> appRepository, ToAppMapper toAppMapper) {
    this.appRepository = appRepository;
    this.toAppMapper = toAppMapper;
  }

  public Observable<List<App>> all() {
    return appRepository.query(new SelectAllAppsQuery());
  }

  public Observable<List<App>> byName(String name) {
    return appRepository.query(new SelectAppsByNameQuery(name));
  }

  public long add(PackageInfo packageInfo) {
    App app = toAppMapper.map(packageInfo.applicationInfo);
    return appRepository.add(app);
  }
}
