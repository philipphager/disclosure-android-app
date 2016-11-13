package de.philipphager.disclosure.api.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OffsetDateTimeTypeAdapterShould {
  @Mock JsonSerializationContext serializationContext;
  @Mock JsonDeserializationContext deserializationContext;
  @InjectMocks OffsetDateTimeTypeAdapter adapter;

  @Test public void serializeDateToJson() {
    OffsetDateTime testDate = OffsetDateTime.of(2016, 1, 1, 12, 0, 30, 0, ZoneOffset.UTC);
    String expectedDate = "2016-01-01T12:00:30Z";

    JsonElement jsonDate = adapter.serialize(testDate, OffsetDateTime.class, serializationContext);
    assertThat(jsonDate.getAsString()).isEqualTo(expectedDate);
  }

  @Test public void deserializeJsonToDate() {
    String testJson = "2016-01-01T01:30:15Z";
    JsonElement json = new JsonPrimitive(testJson);

    OffsetDateTime expectedDate = OffsetDateTime.of(2016, 1, 1, 1, 30, 15, 0, ZoneOffset.UTC);
    OffsetDateTime date = adapter.deserialize(json, OffsetDateTime.class, deserializationContext);

    assertThat(date).isEqualTo(expectedDate);
  }

  @Test(expected = DateTimeParseException.class)
  public void throwErrorIfDateIsNotInIsoFormat() {
    String testJson = "2016-01-0101:30:15";
    JsonElement json = new JsonPrimitive(testJson);

    adapter.deserialize(json, OffsetDateTime.class, deserializationContext);
  }
}
