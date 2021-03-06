package kernbeisser.Forms.ObjectForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import javax.swing.*;
import kernbeisser.Enums.Mode;
import kernbeisser.Exeptions.PermissionKeyRequiredException;
import kernbeisser.Forms.ObjectForm.Exceptions.AccessNotPredictableException;
import kernbeisser.Forms.ObjectForm.Exceptions.CannotParseException;
import kernbeisser.Forms.ObjectForm.Exceptions.FieldNotUniqueException;
import kernbeisser.Forms.ObjectForm.Exceptions.SilentParseException;
import kernbeisser.Forms.ObjectForm.ObjectFormComponents.ObjectFormComponent;
import kernbeisser.Forms.ObjectForm.Properties.BoundedReadProperty;
import kernbeisser.Forms.ObjectForm.Properties.BoundedWriteProperty;
import kernbeisser.Forms.ObjectForm.Properties.PredictableModifiable;
import kernbeisser.Security.Access.Access;
import kernbeisser.Security.Access.AccessListenerManager;
import kernbeisser.Security.Access.AccessManager;
import kernbeisser.Useful.Tools;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class ObjectForm<P> {
  private final Collection<ObjectValidator<P>> objectValidators = new ArrayList<>();

  private final ObjectFormComponent<P>[] components;

  @Getter @Setter private String objectDistinction = "Das Objekt";

  private P original;

  @SafeVarargs
  public ObjectForm(ObjectFormComponent<P>... boundedFields) {
    for (ObjectFormComponent<P> boundedField : boundedFields) {
      if (boundedField == null)
        throw new NullPointerException("cannot create ObjectForm with null fields");
    }

    this.components = boundedFields;
  }

  public void setSource(P data) {
    if (data == null) throw new NullPointerException("cannot set null as source for ObjectView");
    this.original = data;
    setData(data);
  }

  public P getData(Mode m) throws CannotParseException {
    checkValidSource();
    P originalCopy = Tools.clone(original);
    boolean success = true;
    for (ObjectFormComponent<P> component : components) {
      if (component instanceof BoundedWriteProperty) {
        try {
          ((BoundedWriteProperty<P, ?>) component).set(originalCopy);
        } catch (PermissionKeyRequiredException e) {
          ((BoundedWriteProperty<?, ?>) component).setPropertyModifiable(false);
        } catch (CannotParseException e) {
          if (!(e instanceof SilentParseException)) {
            ((BoundedWriteProperty<?, ?>) component).setInvalidInput();
          }
          success = false;
        }
      }
    }
    if (!success) throw new CannotParseException();
    validateObject(originalCopy, m);
    return originalCopy;
  }

  private <V> V readProperty(BoundedReadProperty<P, V> component, P parent) {
    return component.get(parent);
  }

  private <V> void setDataAndAccess(ObjectFormComponent<P> formComponent, P parent) {
    if (formComponent instanceof BoundedReadProperty) {
      V value;
      BoundedReadProperty<P, V> boundedReadProperty = (BoundedReadProperty<P, V>) formComponent;
      synchronized (Access.ACCESS_LOCK) {
        AccessManager accessManager = Access.getDefaultManager();
        AccessListenerManager listener = new AccessListenerManager(accessManager);
        Access.setDefaultManager(listener);

        value = readProperty(boundedReadProperty, parent);

        boundedReadProperty.setReadable(listener.isSuccess());
        Access.setDefaultManager(accessManager);
      }
      boundedReadProperty.setData(value);
      if (formComponent instanceof BoundedWriteProperty) {
        setAccessWithData(
            (BoundedWriteProperty<? super P, ? super V>) formComponent, parent, value);
      }
      return;
    }
    if (formComponent instanceof BoundedWriteProperty
        && formComponent instanceof PredictableModifiable) {
      predictModifiable(
          (BoundedWriteProperty<P, V> & PredictableModifiable<P>) formComponent, parent);
      return;
    }
    throw new AccessNotPredictableException("There is no way to try the setter function!");
  }

  private <VP, V, T extends BoundedWriteProperty<VP, V> & PredictableModifiable<VP>>
      void predictModifiable(T component, VP parent) {
    component.setPropertyModifiable(component.isPropertyModifiable(parent));
  }

  private <VP, V, T extends BoundedWriteProperty<VP, V>> void setAccessWithData(
      T component, VP valueParent, V getterData) {
    try {
      component.set(valueParent, getterData);
      component.setPropertyModifiable(true);
    } catch (PermissionKeyRequiredException e) {
      component.setPropertyModifiable(false);
    }
  }

  private void setData(@NotNull P data) {
    for (ObjectFormComponent<P> field : components) {
      setDataAndAccess(field, data);
    }
  }

  public boolean persistAsNewEntity() {
    checkValidSource();
    try {
      P data = getData(Mode.ADD);
      Tools.add(data);
      JOptionPane.showMessageDialog(null, objectDistinction + " wurde erfolgreich angelegt");
      return true;
    } catch (CannotParseException e) {
      notifyException(e);
      return false;
    }
  }

  private static void notifyException(CannotParseException e) {
    if (!(e instanceof SilentParseException))
      JOptionPane.showMessageDialog(null, "Die markierten Felder wurden nicht korrekt ausgefüllt");
  }

  public boolean persistChanges() {
    checkValidSource();
    try {
      P data = getData(Mode.EDIT);
      Tools.edit(Tools.getId(original), data);
      JOptionPane.showMessageDialog(null, objectDistinction + " wurde erfolgreich bearbeitet");
      return true;
    } catch (CannotParseException e) {
      notifyException(e);
      return false;
    }
  }

  public boolean applyMode(Mode mode) {
    checkValidSource();
    switch (mode) {
      case REMOVE:
        Tools.delete(original.getClass(), Tools.getId(original));
        return true;
      case EDIT:
        return persistChanges();
      case ADD:
        return persistAsNewEntity();
      default:
        throw new UnsupportedOperationException(mode + " is not supported by applyMode");
    }
  }

  public P getOriginal() {
    return original;
  }

  private void checkValidSource() {
    if (original == null) throw new UnsupportedOperationException("no source specified");
  }

  void validateObject(P t, Mode m) throws CannotParseException {
    for (ObjectValidator<P> objectValidator : objectValidators) {
      try {
        objectValidator.validate(t, m);
      } catch (CannotParseException e) {
        objectValidator.invalidNotifier();
        throw e;
      }
    }
  }

  public void registerObjectValidator(ObjectValidator<P> objectValidator) {
    objectValidators.add(objectValidator);
  }

  public void registerObjectValidator(ComparingObjectValidator<P> objectValidator) {
    objectValidators.add((input, mode) -> objectValidator.validate(original, input, mode));
  }

  public <T, V extends BoundedWriteProperty<P, T> & BoundedReadProperty<P, T>>
      void registerUniqueCheck(V component, Predicate<T> exists) {
    registerUniqueCheck(component, exists, () -> {});
  }

  public <T, V extends BoundedWriteProperty<P, T> & BoundedReadProperty<P, T>>
      void registerUniqueCheck(V component, Predicate<T> exists, Runnable exceptionNotifier) {
    registerObjectValidator(
        new ObjectValidator<P>() {
          @Override
          public void validate(P input, Mode mode) throws CannotParseException {
            switch (mode) {
              case EDIT:
                if (Objects.equals(component.get(original), component.get(input))) return;
              case ADD:
                if (exists.test(component.get(input))) {
                  component.setInvalidInput();
                  throw new FieldNotUniqueException("field: [" + component + "] is not unique");
                }
            }
          }

          @Override
          public void invalidNotifier() {
            exceptionNotifier.run();
          }
        });
  }

  @SafeVarargs
  public final void registerObjectValidators(ObjectValidator<P>... objectValidators) {
    this.objectValidators.addAll(Arrays.asList(objectValidators));
  }
}
