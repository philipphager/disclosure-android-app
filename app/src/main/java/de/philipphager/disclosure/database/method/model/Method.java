package de.philipphager.disclosure.database.method.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Method implements ProtectedMethod.SelectByPermissionModel {
  public static final ProtectedMethodModel.SelectByPermissionMapper MAPPER =
      ProtectedMethod.FACTORY.selectByPermissionMapper(Method::create);

  public static Method create(String declaringType, String returnType, String name,
      String argTypes) {
    return new AutoValue_Method(declaringType, returnType, name, argTypes);
  }

  public abstract String declaringType();

  public abstract String returnType();

  public abstract String name();

  public abstract String argTypes();
}
