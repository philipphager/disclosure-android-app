package de.philipphager.disclosure.feature.detail;

public interface DetailView {
  void notify(String message);

  void setToolbarTitle(String title);

  void setAppIcon(String packageName);
}
