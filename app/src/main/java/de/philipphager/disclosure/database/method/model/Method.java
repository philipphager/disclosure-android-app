package de.philipphager.disclosure.database.method.model;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class Method implements ProtectedMethod.SelectByPermissionModel {
  public static final ProtectedMethodModel.SelectByPermissionMapper MAPPER =
      ProtectedMethod.FACTORY.selectByPermissionMapper(
          (declaringType, returnType, name, argTypes) -> builder().declaringType(declaringType)
              .returnType(returnType)
              .name(name)
              .argTypes(argTypes)
              .build());

  public static Builder builder() {
    return new AutoValue_Method.Builder();
  }

  public abstract String declaringType();

  public abstract String returnType();

  public abstract String name();

  public abstract String argTypes();

  @AutoValue.Builder public interface Builder {
    Builder declaringType(String declaringType);

    Builder returnType(String returnType);

    Builder name(String name);

    Builder argTypes(String argTypes);

    Method build();
  }
}
