package de.philipphager.disclosure.database.library.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;
import de.philipphager.disclosure.database.util.adapters.OffsetDateTimeColumnAdapter;
import org.threeten.bp.OffsetDateTime;

@AutoValue public abstract class Library implements LibraryModel, Parcelable {
  private static final ColumnAdapter<Type, String> TYPE_ADAPTER =
      EnumColumnAdapter.create(Type.class);
  public static final Factory<Library> FACTORY = new LibraryModel.Factory<>(
      (id, packageName, sourceDir, title, subtitle, description, websiteUrl, type, createdAt, updatedAt) ->
          builder()
              .id(id)
              .packageName(packageName)
              .sourceDir(sourceDir)
              .title(title)
              .subtitle(subtitle)
              .description(description)
              .websiteUrl(websiteUrl)
              .type(type)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .build(), TYPE_ADAPTER, new OffsetDateTimeColumnAdapter(),
      new OffsetDateTimeColumnAdapter());

  public static Builder builder() {
    return new AutoValue_Library.Builder();
  }

  public static TypeAdapter<Library> typeAdapter(Gson gson) {
    return new AutoValue_Library.GsonTypeAdapter(gson);
  }

  @SerializedName("_id") @SuppressWarnings("PMD.ShortMethodName")
  public abstract String id();

  public abstract String packageName();

  public abstract String sourceDir();

  public abstract String title();

  public abstract String subtitle();

  public abstract String description();

  public abstract String websiteUrl();

  public abstract Type type();

  public abstract OffsetDateTime createdAt();

  public abstract OffsetDateTime updatedAt();

  public enum Type {
    ANALYTICS, ADVERTISEMENT, DEVELOPER, SOCIAL
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(@Nullable String id);

    Builder packageName(String packageName);

    Builder title(String title);

    Builder subtitle(String subtitle);

    Builder description(String description);

    Builder sourceDir(String sourceDir);

    Builder websiteUrl(String websiteUrl);

    Builder type(Type type);

    Builder createdAt(OffsetDateTime createdAt);

    Builder updatedAt(OffsetDateTime updatedAt);

    Library build();
  }
}
