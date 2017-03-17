package de.philipphager.disclosure.api;

import de.philipphager.disclosure.database.library.model.Library;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface DisclosureApi {
  int PAGE_SIZE = 50;

  @GET("/libraries")
  Observable<List<Library>> allLibraries();

  @GET("/libraries")
  Observable<List<Library>> allLibraries(@Query("updatedSince") OffsetDateTime date,
      @Query("page") int page, @Query("limit") int limit);
}
