package de.philipphager.disclosure.database.mocks;

import de.philipphager.disclosure.database.library.model.Library;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public final class MockLibrary {
  public static final Library TEST = Library.builder()
      .id("jfhsakdfhlasdjkf")
      .packageName("com.mixpanel.android")
      .sourceDir("com/mixpanel/android/")
      .title("Mixpanel")
      .subtitle("Tracks app interaction and user behaviour")
      .description("• Tracks actions, like button clicks \n"
          + "• Beta testing, show different versions of an app to different users \n"
          + "• Tracks basic user information (name, email, location)")
      .websiteUrl("http://www.mixpanel.com")
      .type(Library.Type.ANALYTICS)
      .createdAt(OffsetDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC))
      .updatedAt(OffsetDateTime.of(2016, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC))
      .build();

  @SuppressWarnings("PMD.UnnecessaryConstructor") private MockLibrary() {
    //No instances of helper classes.
  }
}
