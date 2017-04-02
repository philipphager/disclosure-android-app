package de.philipphager.disclosure.feature.analyzer.library;

import de.philipphager.disclosure.database.method.model.Method;
import de.philipphager.disclosure.feature.analyser.library.LibraryParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class LibraryParserShould {
  @Test(expected = IllegalStateException.class) public void throwErrorIfApkPathIsInvalid() {
    File nonExistingFile = new File("");
    new LibraryParser(nonExistingFile);
  }

  @Test
  public void findAllMethodInvocationsInLocationActivity() {
    File locationActivityFolder = getFolder("de/philipphager/disclosure/smali/apk");
    LibraryParser libraryParser = new LibraryParser(locationActivityFolder);

    TestSubscriber<List<Method>> subscriber = new TestSubscriber<>();

    libraryParser.findMethodInvocations()
        .toBlocking()
        .subscribe(subscriber);

    subscriber.assertReceivedOnNext(Collections.singletonList(LocationActivity.INVOKED_METHODS));
    subscriber.assertNoErrors();
    subscriber.assertCompleted();
  }

  @Test
  public void iterateAllFilesInApkDirectory() {
    File locationActivityFolder = getFolder("de/philipphager/disclosure/smali/apkWithSubdirs");
    LibraryParser libraryParser = new LibraryParser(locationActivityFolder);

    TestSubscriber<List<Method>> subscriber = new TestSubscriber<>();

    libraryParser.findMethodInvocations()
        .toBlocking()
        .subscribe(subscriber);

    // The folder apkWithSubdirs contains the LocationActivity two times,
    // but one copy is in a subdirectory. If the subdir was successfully
    // parsed, all methods should be found twice.
    List<Method> expectedMethods = new ArrayList<>(LocationActivity.INVOKED_METHODS);
    expectedMethods.addAll(LocationActivity.INVOKED_METHODS);

    subscriber.assertReceivedOnNext(Collections.singletonList(expectedMethods));
    subscriber.assertNoErrors();
    subscriber.assertCompleted();
  }

  @Test
  public void findNoMethodsInEmptySmaliFile() {
    File locationActivityFolder = getFolder("de/philipphager/disclosure/smali/empty");
    LibraryParser libraryParser = new LibraryParser(locationActivityFolder);

    TestSubscriber<List<Method>> subscriber = new TestSubscriber<>();

    libraryParser.findMethodInvocations()
        .toBlocking()
        .subscribe(subscriber);

    subscriber.assertReceivedOnNext(Collections.singletonList(Collections.emptyList()));
    subscriber.assertNoErrors();
    subscriber.assertCompleted();
  }

  private File getFolder(String path) {
    ClassLoader classLoader = this.getClass().getClassLoader();
    return new File(classLoader.getResource(path).getFile());
  }
}
