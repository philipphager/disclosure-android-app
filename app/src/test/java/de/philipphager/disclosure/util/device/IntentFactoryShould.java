package de.philipphager.disclosure.util.device;

import android.content.Intent;
import de.philipphager.disclosure.database.mocks.MockApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class IntentFactoryShould {
  @InjectMocks protected IntentFactory intentFactory;

  @Test public void uninstallPackageIntentShouldContainUninstallIntentAction() {
    String mockPackageName = MockApp.TEST.packageName();
    Intent intent = intentFactory.uninstallPackage(mockPackageName);

    assertThat(intent.getAction()).isEqualTo("android.intent.action.UNINSTALL_PACKAGE");
  }

  @Test public void uninstallPackageIntentShouldContainNameOfUninstalledApp() {
    String mockPackageName = MockApp.TEST.packageName();
    Intent intent = intentFactory.uninstallPackage(mockPackageName);

    assertThat(intent.getData().toString()).isEqualTo("package:" + mockPackageName);
  }
}
