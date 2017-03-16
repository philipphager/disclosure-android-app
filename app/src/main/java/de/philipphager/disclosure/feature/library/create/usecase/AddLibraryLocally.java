package de.philipphager.disclosure.feature.library.create.usecase;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.service.LibraryService;
import java.util.Collections;
import java.util.UUID;
import javax.inject.Inject;
import org.threeten.bp.OffsetDateTime;
import rx.Observable;

public class AddLibraryLocally {
  private final LibraryService libraryService;

  @Inject public AddLibraryLocally(LibraryService libraryService) {
    this.libraryService = libraryService;
  }

  public Observable<Library> run(String title, String packageName, String websiteUrl,
      Library.Type type) {

    return Observable.fromCallable(() -> {
      String sourceDir = packageName.replace(".", "/");

      return Library.builder()
          .id(UUID.randomUUID().toString())
          .title(title)
          .subtitle("")
          .description("")
          .packageName(packageName)
          .sourceDir(sourceDir)
          .websiteUrl(websiteUrl)
          .createdAt(OffsetDateTime.now())
          .updatedAt(OffsetDateTime.now())
          .type(type)
          .build();
    }).doOnNext(library -> libraryService.insert(Collections.singletonList(library)));
  }
}
