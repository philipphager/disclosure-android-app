package de.philipphager.disclosure.feature.library.create;

import de.philipphager.disclosure.database.library.model.Library;
import java.util.List;

public interface CreateLibraryView {
  void setDoneEnabled(boolean isEnabled);

  void showTitleError(boolean isValid);

  void showPackageNameError(boolean isValid);

  void showWebsiteUrlError(boolean isEnabled);

  void showTypes(List<Library.Type> types);

  void finish();

  void showError();
}
