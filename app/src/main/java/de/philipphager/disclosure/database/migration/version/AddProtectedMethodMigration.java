package de.philipphager.disclosure.database.migration.version;

import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.method.model.ProtectedMethod;
import de.philipphager.disclosure.database.migration.Migration;
import javax.inject.Inject;

public class AddProtectedMethodMigration implements Migration {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public AddProtectedMethodMigration() {
    // Needed for dagger injection.
  }

  @Override public void update(SQLiteDatabase db) {
    String dropTable = "DROP TABLE IF EXISTS ";
    db.execSQL(dropTable + ProtectedMethod.TABLE_NAME);
    db.execSQL(ProtectedMethod.CREATE_TABLE);
  }
}
