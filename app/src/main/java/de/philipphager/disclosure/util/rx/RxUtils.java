package de.philipphager.disclosure.util.rx;

import rx.Observable;
import rx.functions.Func2;

public final class RxUtils {
  public static <T1, T2, R> Observable<R> zipMap(
      Observable<T1> o1, Observable<T2> o2, Func2<T1, T2, Observable<R>> mapper) {
    return Observable.zip(o1, o2, mapper).flatMap(v -> v);
  }

  public static Observable<Integer> infRange(int start) {
    int end = Integer.MAX_VALUE - start;
    return Observable.range(start, end);
  }

  private RxUtils() {
    // No instances.
  }
}
