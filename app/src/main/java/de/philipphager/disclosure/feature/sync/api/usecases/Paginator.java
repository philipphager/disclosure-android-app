package de.philipphager.disclosure.feature.sync.api.usecases;

import de.philipphager.disclosure.api.DisclosureApi;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

import static de.philipphager.disclosure.util.rx.RxUtils.infRange;

public abstract class Paginator<T> {
  public Observable<List<T>> join() {
    return getPages()
        .concatMap(new Func1<List<T>, Observable<T>>() {
          @Override public Observable<T> call(List<T> items) {
            return Observable.from(items);
          }
        }).toList();
  }

  public Observable<List<T>> getPages() {
    return infRange(1)
        .concatMap(pageId -> queryPage(pageId, getPageSize(pageId)))
        .takeWhile(list -> !list.isEmpty());
  }

  protected abstract Observable<List<T>> queryPage(int page, int limit);

  /**
   * Override in anonymous inner class to adjust response size for special cases,
   * otherwise the default value will be used defined in DisclosureApi#PAGE_SIZE.
   */
  protected int getPageSize(int page) {
    return DisclosureApi.PAGE_SIZE;
  }
}
