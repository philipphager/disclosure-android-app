package de.philipphager.disclosure.feature.analyzer.app;

import de.philipphager.disclosure.database.mocks.MockApp;
import de.philipphager.disclosure.feature.analyser.app.Apk;
import de.philipphager.disclosure.feature.analyser.app.ApkAnalyzer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApkAnalyzerShould {
  private final String mockClass = "com.facebook.android.sdk";
  private final String mockClassTwo = "com.mixpanel.android";
  private final String mockClassThree = "com.mixpanel.android.MainClass";
  private final List<String> classes = Arrays.asList(mockClass, mockClassTwo, mockClassThree);
  @Mock protected Apk mockApk;

  @Before public void setUp() throws Exception {
    when(mockApk.getApp()).thenReturn(MockApp.TEST);
  }

  @Test @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
  public void loadApkClassesOnInit() {
    when(mockApk.getUsedClasses()).thenReturn(Observable.just(classes));
    ApkAnalyzer apkAnalyzer = new ApkAnalyzer(mockApk);

    verify(mockApk).getUsedClasses();
  }

  @Test public void findLibraryIfApkContainsExactClassName() {
    when(mockApk.getUsedClasses()).thenReturn(Observable.just(classes));
    ApkAnalyzer apkAnalyzer = new ApkAnalyzer(mockApk);

    assertThat(apkAnalyzer.usesLibrary(mockClass)).isTrue();
  }

  @Test public void findLibraryIfApkContainsClassThatStartsWithName() {
    when(mockApk.getUsedClasses()).thenReturn(Observable.just(classes));
    ApkAnalyzer apkAnalyzer = new ApkAnalyzer(mockApk);

    String classPrefix = mockClass.substring(0, 10);

    assertThat(apkAnalyzer.usesLibrary(classPrefix)).isTrue();
  }

  @Test public void notFindLibraryIfClassNameIsNotContainedInApk() {
    when(mockApk.getUsedClasses()).thenReturn(Observable.just(classes));
    ApkAnalyzer apkAnalyzer = new ApkAnalyzer(mockApk);

    String mockClassNameInvalid = "this.class.is.not.in.the.apk";

    assertThat(apkAnalyzer.usesLibrary(mockClassNameInvalid)).isFalse();
  }

  @Test public void notFailIfApkContainsNoClasses() {
    when(mockApk.getUsedClasses()).thenReturn(Observable.just(Collections.EMPTY_LIST));
    ApkAnalyzer apkAnalyzer = new ApkAnalyzer(mockApk);

    assertThat(apkAnalyzer.usesLibrary(mockClass)).isFalse();
  }
}
