package de.philipphager.disclosure.database.util.adapters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.threeten.bp.temporal.ChronoUnit.MILLIS;

@RunWith(MockitoJUnitRunner.class) public class LocalDateTimeColumnAdapterShould {
  @InjectMocks LocalDateTimeColumnAdapter adapter;

  @Test public void encodeMillisToStartOfAtUtcStandard() {
    LocalDateTime utcStartOfTime = LocalDateTime.of(1970, 1, 1, 1, 0, 0, 0);

    Long millis = adapter.encode(utcStartOfTime);

    assertThat(millis).isEqualTo(0);
  }

  @Test public void encodeMillisWithCorrectDuration() {
    LocalDateTime start = LocalDateTime.of(1970, 1, 1, 1, 0, 0, 0);
    LocalDateTime end = LocalDateTime.of(2016, 1, 1, 1, 0, 0, 0);
    long duration = start.until(end, MILLIS);

    Long millis = adapter.encode(end);

    assertThat(millis).isEqualTo(duration);
  }

  @Test public void decodeDateFromMillisStartCorrectly() {
    LocalDateTime utcStartOfTime = LocalDateTime.of(1970, 1, 1, 1, 0, 0, 0);
    LocalDateTime date = adapter.decode(0L);

    assertThat(date).isEqualTo(utcStartOfTime);
  }

  @Test public void decodeDateFromMillisDurationCorrectly() {
    LocalDateTime start = LocalDateTime.of(1970, 1, 1, 1, 0, 0, 0);
    LocalDateTime end = LocalDateTime.of(2016, 1, 1, 1, 0, 0, 0);

    long duration = start.until(end, MILLIS);

    LocalDateTime date = adapter.decode(duration);
    assertThat(date).isEqualTo(end);
  }
}
