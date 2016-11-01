package de.philipphager.disclosure.feature.device;

import javax.inject.Inject;
import net.dongliu.apk.parser.ApkParser;
import rx.Observable;

public class ApkProvider {
  @Inject public ApkProvider() {
  }

  public Observable<ApkParser> loadApk(String sourceDir) {
    return Observable.fromCallable(() -> new ApkParser(sourceDir));
  }
}
