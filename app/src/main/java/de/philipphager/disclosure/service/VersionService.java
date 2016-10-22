package de.philipphager.disclosure.service;

import android.content.pm.PackageInfo;
import de.philipphager.disclosure.database.util.Repository;
import de.philipphager.disclosure.database.version.mapper.ToVersionMapper;
import de.philipphager.disclosure.database.version.model.Version;
import de.philipphager.disclosure.database.version.query.SelectAllVersionsQuery;
import de.philipphager.disclosure.database.version.query.SelectVersionByAppQuery;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class VersionService {
  private final Repository<Version> versionRepository;

  @Inject public VersionService(Repository<Version> versionRepository) {
    this.versionRepository = versionRepository;
  }

  public Observable<List<Version>> all() {
    return versionRepository.query(new SelectAllVersionsQuery());
  }

  public Observable<List<Version>> byApp(long appId) {
    return versionRepository.query(new SelectVersionByAppQuery(appId));
  }

  public long add(long appId, PackageInfo packageInfo) {
    Version version = new ToVersionMapper(appId).map(packageInfo);
    return versionRepository.add(version);
  }
}
