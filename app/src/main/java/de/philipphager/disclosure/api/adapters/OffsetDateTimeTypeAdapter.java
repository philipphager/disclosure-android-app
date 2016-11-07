package de.philipphager.disclosure.api.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public final class OffsetDateTimeTypeAdapter
    implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

  public static OffsetDateTimeTypeAdapter create() {
    return new OffsetDateTimeTypeAdapter();
  }

  private OffsetDateTimeTypeAdapter() {
    // Use static factory method create().
  }

  @Override public JsonElement serialize(OffsetDateTime src, Type typeOfSrc,
      JsonSerializationContext context) {
    return new JsonPrimitive(FORMATTER.format(src));
  }

  @Override
  public OffsetDateTime deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return FORMATTER.parse(json.getAsString(), OffsetDateTime.FROM);
  }
}
