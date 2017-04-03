package de.philipphager.disclosure.feature.analyser.library.usecase;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.method.model.Method;
import de.philipphager.disclosure.feature.analyser.library.LibraryParser;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class AnalyseLibraryMethodInvocations {
  @SuppressWarnings("PMD.UnnecessaryConstructor")
  @Inject public AnalyseLibraryMethodInvocations() {
    // Needed for dagger injection.
  }

  public Observable<List<Method>> run(File decompiledApkDir, Library library) {
    return Observable.just(getLibrarySourceDirectory(decompiledApkDir, library))
        .filter(File::exists)
        .map(LibraryParser::new)
        .flatMap(LibraryParser::findMethodInvocations)
        .flatMap(Observable::from)
        .distinct()
        .filter(new FilterIrrelevantMethods())
        .toList();
  }

  private File getLibrarySourceDirectory(File decompiledApkDir, Library library) {
    return new File(decompiledApkDir.getPath() + File.separator + library.sourceDir());
  }
}
