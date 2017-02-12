package de.philipphager.disclosure.feature.analyser.app.usecase;

import de.philipphager.disclosure.database.app.model.App;
import java.io.File;
import javax.inject.Inject;
import org.jf.baksmali.baksmali;
import org.jf.baksmali.baksmaliOptions;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.analysis.InlineMethodResolver;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedOdexFile;
import rx.Observable;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

/**
 * Closely follows closely the implementation used in APKTool by iBotPeaches.
 *
 * @see <a href="https://github.com/iBotPeaches/Apktool/blob/master/brut
 * .apktool/apktool-lib/src/main/java/brut/androlib/src/
 * SmaliDecoder.java">SmaliDecoder.java</a>
 */
public class DecompileApp {
  @SuppressWarnings("PMD.UnnecessaryConstructor") @Inject public DecompileApp() {
    // Needed for dagger injection.
  }

  public Observable<File> run(App app, File outputDir) {
    return Observable.fromCallable(() -> {
      File apkFile = new File(app.sourceDir());
      check(apkFile.exists(), "must provide valid .apk file for analysis");

      if (!outputDir.exists()) {
        check(outputDir.mkdirs(), "could not create .smali output directory");
      }

      baksmaliOptions options = getDefaultOptions();
      options.outputDirectory = outputDir.getPath();

      // Limit the max. used threads.
      options.jobs = Runtime.getRuntime().availableProcessors();
      if (options.jobs > 6) {
        options.jobs = 6;
      }

      // TODO: Replace 15 with actual targetSDKNumber of apk.
      DexBackedDexFile dexFile = DexFileFactory.loadDexFile(apkFile, "classes.dex", 15, false);

      if (dexFile instanceof DexBackedOdexFile) {
        options.inlineResolver = InlineMethodResolver.createInlineMethodResolver(
            ((DexBackedOdexFile) dexFile).getOdexVersion());
      }

      check(baksmali.disassembleDexFile(dexFile, options), "disassembling apk to smali failed");
      return outputDir;
    });
  }

  public baksmaliOptions getDefaultOptions() {
    baksmaliOptions options = new baksmaliOptions();
    options.deodex = false;
    options.noParameterRegisters = false;
    options.useLocalsDirective = true;
    options.useSequentialLabels = true;
    options.outputDebugInfo = true;
    options.addCodeOffsets = false;
    options.jobs = -1;
    options.noAccessorComments = false;
    options.registerInfo = 0;
    options.ignoreErrors = false;
    options.inlineResolver = null;
    options.checkPackagePrivateAccess = false;
    return options;
  }
}
