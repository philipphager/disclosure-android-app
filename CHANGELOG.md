# Changelog

- Latest production version: -
- Latest beta version: v0.0.4.0

## Disclosure [v0.0.4.0] 03.04.2017
### Miscellaneous
- Added: Travis CI build support for unit testing and instrumentation tests.
- Added: Github issue tracker and changelog in settings.
- Added: Instrumentation tests using Espresso.
- Improved: Housekeeping, removed unused features and files.

## Disclosure [v0.0.3.0] 16.03.2017
### Analyzer
- Improved: Libraries are not only detected by the exact package name, but also if their package name starts with a known library package name.
- Improved: Removed all dependencies on [APKParser](https://github.com/clearthesky/apk-parsers) for library detection.
- Improved: Don't allow an app to be added multiple times to the analytics queue.

### AppManager
- Added: Descriptions for all system permissions.
- Fix: Crash caused by missing error handling.
- Fix: Selection bug, where apps would be falsely marked as selected.

### LibraryExplorer
- Added: Custom libraries can now be added locally and analyzed.
- Added: Manual sync button to sync with the Disclosure API and to rerun library detection (Before only done on app start).

### Miscellaneous
- Added: Show current app version in settings.
- Added: List all licenses for all used libraries in the settings.
- Added: Add personal contact information in the settings.
- Improved: Updated Retrofit, OkHttp to latest version.

## Disclosure [v0.0.2.0] 06.03.2017
### Analyzer
- Added: Analyzer that detects permission usages inside libraries.
- Added: PScout permission mappings for Android API 22 that detects permission usages inside libraries.
- Added: Ability to analyze multiple apps in a queue.
- Added: Pending apps can be removed from the analytics queue.
- Improved: Filter methods for permission analysis that do not call the Android API.

### AppManager
- Added: Search for apps by name, package name and names of used libraries
- Added: Sort apps by alphabet, library count, permission count and date of last analysis.
- Added: Link to open an app.
- Added: Show an explanation for used permissions in a dialog explaining the permission and it's current status on the system.
- Removed: Trust app feature.

### LibraryExplorer
- Added: Tabs for library categories.
- Added: Show/hide only used libraries.
- Added: Show link to library website.

### Miscellaneous
- Added: New app icon.
- Added: New layout (Dark mode).
- Added: Setting to show/hide only actively used permissions of libraries.
- Improved: Changed server endpoint to disclosure-api.herokuapp.com
