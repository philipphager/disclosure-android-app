package de.philipphager.disclosure.api.library;

import de.philipphager.disclosure.database.library.model.Library;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface LibraryApi {
  @GET("/libraries")
  Observable<List<Library>> all();

  @GET("/libraries")
  Observable<List<Library>> all(@Query("updatedSince") OffsetDateTime lastUpdate);
}
