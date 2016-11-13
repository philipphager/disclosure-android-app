package de.philipphager.disclosure.database.library.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import de.philipphager.disclosure.database.util.adapters.OffsetDateTimeColumnAdapter;
import org.threeten.bp.OffsetDateTime;

@AutoValue public abstract class LibraryFeature implements LibraryFeatureModel {
  public static final LibraryFeature.Factory<LibraryFeature> FACTORY =
      new LibraryFeature.Factory<>(LibraryFeature::create,
          new OffsetDateTimeColumnAdapter(),
          new OffsetDateTimeColumnAdapter());

  public static LibraryFeature create(String id, String libraryId, String featureId,
      OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    return new AutoValue_LibraryFeature(id, libraryId, featureId, createdAt, updatedAt);
  }

  public static TypeAdapter<LibraryFeature> typeAdapter(Gson gson) {
    return new AutoValue_LibraryFeature.GsonTypeAdapter(gson);
  }

  @SerializedName("_id") @SuppressWarnings("PMD.ShortMethodName") public abstract String id();

  public abstract String libraryId();

  public abstract String featureId();

  public abstract OffsetDateTime createdAt();

  public abstract OffsetDateTime updatedAt();
}
