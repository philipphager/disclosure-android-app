package de.philipphager.disclosure.util.time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StopwatchShould {
  @Mock protected Clock clock;
  @InjectMocks protected Stopwatch stopwatch;

  @Test(expected = IllegalStateException.class)
  public void forbidCallingStopBeforeCallStart() {
    stopwatch.stop();
  }

  @Test public void measureDurationTime() {
    LocalDateTime mockStart = LocalDateTime.of(2016, 1, 1, 0, 0, 0);
    LocalDateTime mockEnd = LocalDateTime.of(2016, 1, 1, 0, 1, 0);

    when(clock.now()).thenReturn(mockStart);
    stopwatch.start();

    when(clock.now()).thenReturn(mockEnd);
    stopwatch.stop();

    assertThat(stopwatch.getDuration()).isEqualTo(Duration.between(mockStart, mockEnd));
  }

  @Test @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
  public void resetAfterRestart() {
    LocalDateTime firstMockStart = LocalDateTime.of(2016, 1, 1, 0, 0, 0);
    LocalDateTime firstMockEnd = LocalDateTime.of(2016, 1, 1, 0, 1, 0);

    when(clock.now()).thenReturn(firstMockStart);
    stopwatch.start();
    when(clock.now()).thenReturn(firstMockEnd);
    stopwatch.stop();

    Duration firstActualDuration = Duration.between(firstMockStart, firstMockEnd);
    Duration firstMeasuredDuration = stopwatch.getDuration();
    assertThat(firstMeasuredDuration).isEqualTo(firstActualDuration);

    LocalDateTime secondMockStart = LocalDateTime.of(2016, 1, 1, 0, 5, 0);
    LocalDateTime secondMockEnd = LocalDateTime.of(2016, 1, 1, 0, 10, 0);

    when(clock.now()).thenReturn(secondMockStart);
    stopwatch.start();
    when(clock.now()).thenReturn(secondMockEnd);
    stopwatch.stop();

    Duration secondActualDuration = Duration.between(secondMockStart, secondMockEnd);
    Duration secondMeasuredDuration = stopwatch.getDuration();
    assertThat(secondMeasuredDuration).isEqualTo(secondActualDuration);
  }

  @Test(expected = IllegalStateException.class)
  public void forbidCallingStopTwiceWithoutStart() {
    stopwatch.start();
    stopwatch.stop();
    stopwatch.stop();
  }
}
