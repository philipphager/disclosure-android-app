package de.philipphager.disclosure.database.permission.populator;

import android.content.ContentValues;
import android.content.pm.PermissionInfo;
import android.database.sqlite.SQLiteDatabase;
import de.philipphager.disclosure.database.permission.model.Permission;
import de.philipphager.disclosure.database.permission.model.PermissionGroup;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PermissionPopulator {
  @Inject @SuppressWarnings("PMD.UnnecessaryConstructor") public PermissionPopulator() {
    // Needed for dagger injection.
  }

  public void populate(SQLiteDatabase db) {
    PermissionGroup personalInformation =
        PermissionGroup.create("fksjdfhksjdhf", "Personal Information",
            "Abilities to collect personal infos");

    List<Permission> permissions = new ArrayList<>();
    permissions.add(Permission.builder().id("android.permission.INTERNET")
        .title("Internet")
        .protectionLevel(PermissionInfo.PROTECTION_NORMAL)
        .description("Enables unrestrcited network connections")
        .build()
    );

    permissions.add(Permission.builder().id("android.permission.READ_CONTACTS")
        .title("Read Contacts")
        .protectionLevel(PermissionInfo.PROTECTION_DANGEROUS)
        .description("Read personal contacts from your address book")
        .permissionGroup(personalInformation.id())
        .build()
    );

    permissions.add(Permission.builder().id("android.permission.WRITE_CONTACTS")
        .title("Write Contacts")
        .protectionLevel(PermissionInfo.PROTECTION_DANGEROUS)
        .description("Create new contacts in your address book")
        .permissionGroup(personalInformation.id())
        .build()
    );

    ContentValues group = PermissionGroup.FACTORY.marshal(personalInformation).asContentValues();
    db.insert(PermissionGroup.TABLE_NAME, null, group);

    for (Permission permission : permissions) {
      ContentValues content = Permission.FACTORY.marshal(permission).asContentValues();
      db.insert(Permission.TABLE_NAME, null, content);
    }
  }
}
