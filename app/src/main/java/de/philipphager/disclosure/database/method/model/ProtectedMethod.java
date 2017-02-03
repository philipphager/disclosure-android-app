package de.philipphager.disclosure.database.method.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class ProtectedMethod implements ProtectedMethodModel {
  public static final ProtectedMethod.Factory<ProtectedMethod> FACTORY =
      new ProtectedMethod.Factory<>((permissionId, declaringType, returnType, name, argTypes) ->
          builder().permissionId(permissionId)
              .declaringType(declaringType)
              .returnType(returnType)
              .name(name)
              .argTypes(argTypes)
              .build());

  public static Builder builder() {
    return new AutoValue_ProtectedMethod.Builder();
  }

  public abstract String permissionId();

  public abstract String declaringType();

  public abstract String returnType();

  public abstract String name();

  public abstract String argTypes();

  @AutoValue.Builder public interface Builder {
    Builder permissionId(String permissionId);

    Builder declaringType(String declaringType);

    Builder returnType(String returnType);

    Builder name(String name);

    Builder argTypes(String argTypes);

    ProtectedMethod build();
  }
}
