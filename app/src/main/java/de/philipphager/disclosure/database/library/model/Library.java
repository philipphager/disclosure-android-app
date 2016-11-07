package de.philipphager.disclosure.database.library.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.EnumColumnAdapter;
import de.philipphager.disclosure.database.util.adapters.LocalDateTimeColumnAdapter;
import org.threeten.bp.OffsetDateTime;

@AutoValue public abstract class Library implements LibraryModel, Parcelable {
  private static final ColumnAdapter<Type> TYPE_ADAPTER = EnumColumnAdapter.create(Type.class);
  public static final Factory<Library> FACTORY = new LibraryModel.Factory<>(
      (id, packageName, title, subtitle, description, type, createdAt, updatedAt) -> {
        return builder().id(id)
            .packageName(packageName)
            .title(title)
            .subtitle(subtitle)
            .description(description)
            .type(type)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
      }, TYPE_ADAPTER, new LocalDateTimeColumnAdapter(), new LocalDateTimeColumnAdapter());

  public static Builder builder() {
    return new AutoValue_Library.Builder();
  }

  public static TypeAdapter<Library> typeAdapter(Gson gson) {
    return new AutoValue_Library.GsonTypeAdapter(gson);
  }

  @SuppressWarnings("PMD.ShortMethodName") @Nullable public abstract Long id();

  public abstract String packageName();

  public abstract String title();

  public abstract String subtitle();

  public abstract String description();

  public abstract Type type();

  public abstract OffsetDateTime createdAt();

  public abstract OffsetDateTime updatedAt();

  public enum Type {
    ANALYTICS, ADVERTISMENT, DEVELOPER, SOCIAL
  }

  @AutoValue.Builder public interface Builder {
    @SuppressWarnings("PMD.ShortMethodName") Builder id(@Nullable Long id);

    Builder packageName(String packageName);

    Builder title(String title);

    Builder subtitle(String subtitle);

    Builder description(String description);

    Builder type(Type type);

    Builder createdAt(OffsetDateTime createdAt);

    Builder updatedAt(OffsetDateTime updatedAt);

    Library build();
  }
}
