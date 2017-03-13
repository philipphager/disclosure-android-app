package de.philipphager.disclosure.feature.library.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import de.philipphager.disclosure.ApplicationComponent;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.library.model.Library;
import de.philipphager.disclosure.util.ui.BaseActivity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class CreateLibraryActivity extends BaseActivity implements CreateLibraryView {
  @Inject protected CreateLibraryPresenter presenter;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.ic_done) ImageView done;
  @BindView(R.id.library_title) EditText title;
  @BindView(R.id.library_package_name) EditText packageName;
  @BindView(R.id.library_website) EditText websiteUrl;
  @BindView(R.id.library_type) Spinner type;
  @BindView(R.id.library_layout_title) TextInputLayout titleLayout;
  @BindView(R.id.library_layout_package_name) TextInputLayout packageNameLayout;
  @BindView(R.id.library_layout_website) TextInputLayout websiteUrlLayout;
  private ArrayAdapter<String> typeAdapter;

  public static Intent launch(Context context) {
    return new Intent(context, CreateLibraryActivity.class);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_library_create);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear);

    typeAdapter =
        new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new ArrayList<>(4));
    type.setAdapter(typeAdapter);

    presenter.onCreate(this);
  }

  @Override protected void injectActivity(ApplicationComponent appComponent) {
    appComponent.inject(this);
  }

  @Override public void showTypes(List<Library.Type> types) {
    for (Library.Type type : types) {
      typeAdapter.add(type.name());
    }

    typeAdapter.notifyDataSetChanged();
  }

  @Override public void setDoneEnabled(boolean isEnabled) {
    done.setImageResource(isEnabled ? R.drawable.ic_check : R.drawable.ic_check_disabled);
  }

  @Override public void showTitleError(boolean isEnabled) {
    titleLayout.setError(getString(R.string.activity_library_create_title_invalid));
    titleLayout.setErrorEnabled(isEnabled);
  }

  @Override public void showPackageNameError(boolean isEnabled) {
    packageNameLayout.setError(getString(R.string.activity_library_create_package_name_invalid));
    packageNameLayout.setErrorEnabled(isEnabled);
  }

  @Override public void showWebsiteUrlError(boolean isEnabled) {
    websiteUrlLayout.setError(getString(R.string.activity_library_create_url_invalid));
    websiteUrlLayout.setErrorEnabled(isEnabled);
  }

  @OnTextChanged(R.id.library_title) public void onTitleChanged(Editable title) {
    presenter.onTitleChanged(title.toString());
  }

  @OnTextChanged(R.id.library_package_name) public void onPackageNameChanged(Editable packageName) {
    presenter.onPackageNameChanged(packageName.toString());
  }

  @OnTextChanged(R.id.library_website) public void onWebsiteUrlChanged(Editable websiteUrl) {
    presenter.onWebsiteUrlChanged(websiteUrl.toString());
  }

  @Override public void showError() {
    Toast.makeText(this, R.string.activity_library_create_error_duplicate, Toast.LENGTH_LONG)
        .show();
  }

  @OnItemSelected(R.id.library_type) public void onTypeSelected(Spinner spinner, int position) {
    if (position < typeAdapter.getCount()) {
      String type = typeAdapter.getItem(position);
      presenter.onTypeSelected(Library.Type.valueOf(type));
    }
  }

  @OnClick(R.id.ic_done) public void onDoneClicked() {
    presenter.onDoneClicked();
  }
}
