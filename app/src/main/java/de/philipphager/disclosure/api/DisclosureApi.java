package de.philipphager.disclosure.api;

import de.philipphager.disclosure.database.feature.model.Feature;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.database.library.model.LibraryFeature;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface DisclosureApi {
  @GET("/libraries")
  Observable<List<Library>> allLibraries();

  @GET("/libraries")
  Observable<List<Library>> allLibraries(@Query("updatedSince") OffsetDateTime lastUpdate);

  @GET("/features")
  Observable<List<Feature>> allFeatures();

  @GET("/features")
  Observable<List<Feature>> allFeatures(@Query("updatedSince") OffsetDateTime lastUpdate);

  @GET("/libraryFeatures")
  Observable<List<LibraryFeature>> allLibraryFeatures();

  @GET("/libraryFeatures")
  Observable<List<LibraryFeature>> allLibraryFeatures(@Query("updatedSince") OffsetDateTime lastUpdate);
}
