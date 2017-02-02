package de.philipphager.disclosure.database.method.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class ProtectedMethod implements ProtectedMethodModel {
  public static final ProtectedMethod.Factory<ProtectedMethod> FACTORY = new ProtectedMethod.Factory<>(ProtectedMethod::create);

  public static ProtectedMethod create(String permissionId, String declaringType, String returnType,
      String name, String argTypes) {
    return new AutoValue_ProtectedMethod(permissionId, declaringType, returnType, name, argTypes);
  }

  public abstract String permissionId();

  public abstract String declaringType();

  public abstract String returnType();

  public abstract String name();

  public abstract String argTypes();
}
