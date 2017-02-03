package de.philipphager.disclosure.feature.analyser.app;

import de.philipphager.disclosure.database.app.model.App;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.DexClass;
import org.jf.baksmali.Baksmali;
import org.jf.baksmali.BaksmaliOptions;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.DexFile;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

public class Apk {
  private static final int MIN_INDEX = 0;
  private final App app;
  private final File apkFile;
  private String[] packageNames;

  public Apk(App app) throws IOException {
    this.app = app;
    this.apkFile = new File(app.sourceDir());

    check(apkFile.exists(), "must provide valid path to .apk file");
    load();
  }

  private void load() throws IOException {
    ApkParser apk = new ApkParser(app.sourceDir());
    DexClass[] dexClasses = apk.getDexClasses();
    packageNames = new String[dexClasses.length];

    for (int i = 0; i < dexClasses.length; i++) {
      packageNames[i] = dexClasses[i].getPackageName();
    }
  }

  public boolean containsPackage(String packageName) {
    String currentThread = Thread.currentThread().getName();
    Timber.d("%s : Searching for package %s in app %s", currentThread, packageName, app.label());
    int index = Arrays.binarySearch(packageNames, packageName);
    return index >= MIN_INDEX;
  }

  public File decompileTo(File outputDir) throws IOException {

    if (!outputDir.exists()) {
      check(outputDir.mkdirs(), "could not create .smali output directory");
    }

    DexFile dexFile = DexFileFactory.loadDexFile(apkFile, Opcodes.getDefault());
    Baksmali.disassembleDexFile(dexFile, outputDir, 4, new BaksmaliOptions());
    return outputDir;
  }
}
