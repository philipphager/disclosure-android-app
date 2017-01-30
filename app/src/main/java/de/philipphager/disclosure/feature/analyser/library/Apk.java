package de.philipphager.disclosure.feature.analyser.library;

import com.jakewharton.dex.DexMethods;
import de.philipphager.disclosure.database.app.model.App;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.DexClass;
import net.dongliu.apk.parser.struct.AndroidConstants;
import timber.log.Timber;

public class Apk {
  private static final int MIN_INDEX = 0;
  private final App app;
  private String[] packageNames;
  private List<String> methodNames;

  public Apk(App app) throws IOException {
    this.app = app;
    load();
  }

  private void load() throws IOException {
    ApkParser apk = new ApkParser(app.sourceDir());
    DexClass[] dexClasses = apk.getDexClasses();
    packageNames = new String[dexClasses.length];

    for (int i = 0; i < dexClasses.length; i++) {
      packageNames[i] = dexClasses[i].getPackageName();
    }
    
    methodNames = DexMethods.list(apk.getFileData(AndroidConstants.DEX_FILE));
  }

  public boolean containsPackage(String packageName) {
    String currentThread = Thread.currentThread().getName();
    Timber.d("%s : Searching for package %s in app %s", currentThread, packageName, app.label());
    int index = Arrays.binarySearch(packageNames, packageName);
    return index >= MIN_INDEX;
  }

  public boolean containsMethod(String method) {
    return methodNames.contains(method);
  }
}
