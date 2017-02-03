package de.philipphager.disclosure.feature.analyser.library;

import de.philipphager.disclosure.database.method.model.Method;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rx.Observable;
import timber.log.Timber;

import static de.philipphager.disclosure.util.assertion.Assertions.check;

public class LibraryParser {
  private static final int ESTIMATED_METHODS_PER_LIBRARY = 500;
  private static final Pattern METHOD_REGEX =
      Pattern.compile("invoke-(direct|virtual|static|super) \\{.*\\}, (.*)->(.*)\\((.*)\\)(.*)");
  private final File directory;
  private List<Method> invokedMethods;

  public LibraryParser(File directory) {
    check(directory != null && directory.exists(), "must provide valid library directory");
    this.directory = directory;
  }

  public Observable<List<Method>> findMethodInvocations() {
    return Observable.fromCallable(() -> {
      if (invokedMethods == null) {
        invokedMethods = new ArrayList<>(ESTIMATED_METHODS_PER_LIBRARY);
        traverseClassesAndFindInvocations(directory, invokedMethods);
      }

      Timber.d("%s method invocations in library %s ", invokedMethods.size(),
          directory.getAbsolutePath());
      return invokedMethods;
    });
  }

  private void traverseClassesAndFindInvocations(File dir, List<Method> methods) {
    File[] smaliFiles = dir.listFiles();

    for (File smaliFile : smaliFiles) {
      if (smaliFile.isFile()) {
        if (smaliFile.getName().endsWith(".smali")) {
          findMethodInvocationsInFile(smaliFile, methods);
        }
      } else if (smaliFile.isDirectory()) {
        traverseClassesAndFindInvocations(smaliFile, methods);
      }
    }
  }

  private void findMethodInvocationsInFile(File file, List<Method> methods) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {

        // Find method invocations in the source code
        Matcher matcher = METHOD_REGEX.matcher(line);

        if (matcher.find()) {
          String declaringType = matcher.group(2);
          String name = matcher.group(3);
          String argTypes = matcher.group(4);
          String returnType = matcher.group(5);

          methods.add(Method.builder()
              .declaringType(declaringType)
              .name(name)
              .argTypes(argTypes)
              .returnType(returnType)
              .build());
        }
      }
    } catch (IOException e) {
      Timber.e(e, "while searching for method invocations in file '%s'", file);
    }
  }
}
