package de.philipphager.disclosure.feature.library.create;

import android.text.TextUtils;
import android.webkit.URLUtil;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.feature.library.create.usecase.AddLibraryLocally;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class CreateLibraryPresenter {
  private static final List<Library.Type> TYPES = Arrays.asList(
      Library.Type.ADVERTISEMENT,
      Library.Type.ANALYTICS,
      Library.Type.DEVELOPER,
      Library.Type.SOCIAL
  );
  private final AddLibraryLocally addLibraryLocally;
  private CreateLibraryView view;
  private String title = "";
  private String packageName = "";
  private String websiteUrl = "";
  private Library.Type type;

  @Inject public CreateLibraryPresenter(AddLibraryLocally addLibraryLocally) {
    this.addLibraryLocally = addLibraryLocally;
  }

  public void onCreate(CreateLibraryView view) {
    this.view = view;
    initUi();
  }

  private void initUi() {
    view.showTypes(TYPES);
  }

  public void onDoneClicked() {
    if (validateAll()) {

      addLibraryLocally.run(title, packageName, websiteUrl, type)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(library -> {
            view.finish();
          }, throwable -> {
            Timber.e(throwable, "while adding local library");
            view.showError();
          });
    } else {
      displayErrors();
    }
  }

  public void onTitleChanged(String title) {
    this.title = title;
    checkDoneButtonEnabled();
  }

  public void onPackageNameChanged(String packageName) {
    this.packageName = packageName;
    checkDoneButtonEnabled();
  }

  public void onWebsiteUrlChanged(String websiteUrl) {
    this.websiteUrl = websiteUrl;
    checkDoneButtonEnabled();
  }

  public void onTypeSelected(Library.Type type) {
    this.type = type;
  }

  private void displayErrors() {
    view.showTitleError(!validateTitle());
    view.showPackageNameError(!validatePackageName());
    view.showWebsiteUrlError(!validateWebsite());
  }

  private void resetErrors() {
    view.showTitleError(false);
    view.showPackageNameError(false);
    view.showWebsiteUrlError(false);
  }

  private void checkDoneButtonEnabled() {
    boolean allValid = validateAll();
    view.setDoneEnabled(allValid);

    if (allValid) {
      resetErrors();
    }
  }

  private boolean validateAll() {
    boolean isValid =
        validateTitle() && validatePackageName() && validateWebsite() && validateType();
    view.setDoneEnabled(isValid);

    return isValid;
  }

  private boolean validateTitle() {
    return !TextUtils.isEmpty(title);
  }

  private boolean validatePackageName() {
    return !TextUtils.isEmpty(packageName) && packageName.contains(".");
  }

  private boolean validateWebsite() {
    return TextUtils.isEmpty(websiteUrl) || URLUtil.isValidUrl(websiteUrl);
  }

  private boolean validateType() {
    return type != null;
  }
}
