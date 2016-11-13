package de.philipphager.disclosure.database.util.adapters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.threeten.bp.temporal.ChronoUnit.MILLIS;

@RunWith(MockitoJUnitRunner.class) public class OffsetDateTimeColumnAdapterShould {
  @InjectMocks OffsetDateTimeColumnAdapter adapter;

  @Test public void encodeMillisToStartOfAtUtcStandard() {
    OffsetDateTime utcStartOfTime = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    Long millis = adapter.encode(utcStartOfTime);

    assertThat(millis).isEqualTo(0);
  }

  @Test public void encodeMillisWithCorrectDuration() {
    OffsetDateTime start = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime end = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    long duration = start.until(end, MILLIS);

    Long millis = adapter.encode(end);

    assertThat(millis).isEqualTo(duration);
  }

  @Test public void decodeDateFromMillisStartCorrectly() {
    OffsetDateTime utcStartOfTime = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime date = adapter.decode(0L);

    assertThat(date).isEqualTo(utcStartOfTime);
  }

  @Test public void decodeDateFromMillisDurationCorrectly() {
    OffsetDateTime start = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime end = OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    long duration = start.until(end, MILLIS);

    OffsetDateTime date = adapter.decode(duration);
    assertThat(date).isEqualTo(end);
  }
}
