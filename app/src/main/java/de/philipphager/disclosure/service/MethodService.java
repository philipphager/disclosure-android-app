package de.philipphager.disclosure.service;

import com.squareup.sqlbrite.BriteDatabase;
import de.philipphager.disclosure.database.DatabaseManager;
import de.philipphager.disclosure.database.method.model.Method;
import de.philipphager.disclosure.database.method.model.ProtectedMethod;
import de.philipphager.disclosure.database.method.repositories.MethodRepository;
import de.philipphager.disclosure.database.permission.model.Permission;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class MethodService {
  private final DatabaseManager databaseManager;
  private final MethodRepository methodRepository;

  @Inject public MethodService(DatabaseManager databaseManager,
      MethodRepository methodRepository) {
    this.databaseManager = databaseManager;
    this.methodRepository = methodRepository;
  }

  public void insert(List<ProtectedMethod> protectedMethods) {
    BriteDatabase db = databaseManager.get();
    try (BriteDatabase.Transaction transaction = db.newTransaction()) {
      for (ProtectedMethod protectedMethod : protectedMethods) {
        methodRepository.insert(db, protectedMethod);
      }

      transaction.markSuccessful();
    }
  }

  public Observable<List<Method>> byPermission(Permission permission) {
    BriteDatabase db = databaseManager.get();
    return methodRepository.byPermission(db, permission.id());
  }
}
