package de.philipphager.disclosure.feature.detail;

import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.util.ui.components.ScoreView;
import java.util.List;

public interface DetailView {
  void notify(String message);

  void setToolbarTitle(String title);

  void setAppIcon(String packageName);

  void setLibraries(List<Library> libraries);

  void setScore(ScoreView.Score score);
}
