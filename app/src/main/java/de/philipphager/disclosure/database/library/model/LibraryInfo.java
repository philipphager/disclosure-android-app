package de.philipphager.disclosure.database.library.model;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue public abstract class LibraryInfo implements Library.SelectByTypeWithAppCountModel {
  public static final RowMapper<LibraryInfo> MAPPER =
      new LibraryModel.SelectByTypeWithAppCountMapper<>(LibraryInfo::create);

  public static LibraryInfo create(String id, String title, long appCount) {
    return new AutoValue_LibraryInfo(id, title, appCount);
  }

  public abstract String id();

  public abstract String title();

  public abstract long appCount();
}
