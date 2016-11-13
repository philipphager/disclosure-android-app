package de.philipphager.disclosure.database.feature.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import de.philipphager.disclosure.database.util.adapters.OffsetDateTimeColumnAdapter;
import org.threeten.bp.OffsetDateTime;

@AutoValue public abstract class Feature implements FeatureModel {
  public static final FeatureModel.Factory<Feature> FACTORY =
      new Factory<>(Feature::create, new OffsetDateTimeColumnAdapter(),
          new OffsetDateTimeColumnAdapter());

  public static Feature create(String id, String title, String description,
      OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    return new AutoValue_Feature(id, title, description, createdAt, updatedAt);
  }

  public static TypeAdapter<Feature> typeAdapter(Gson gson) {
    return new AutoValue_Feature.GsonTypeAdapter(gson);
  }

  @SerializedName("_id") @SuppressWarnings("PMD.ShortMethodName") public abstract String id();

  public abstract String title();

  public abstract String description();

  public abstract OffsetDateTime createdAt();

  public abstract OffsetDateTime updatedAt();
}
