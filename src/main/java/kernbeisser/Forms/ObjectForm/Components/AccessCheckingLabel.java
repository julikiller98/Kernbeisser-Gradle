package kernbeisser.Forms.ObjectForm.Components;

import javax.swing.JLabel;
import kernbeisser.Exeptions.PermissionKeyRequiredException;
import kernbeisser.Forms.ObjectForm.ObjectFormComponents.ObjectFormComponent;
import kernbeisser.Forms.ObjectForm.Properties.BoundedReadProperty;
import kernbeisser.Forms.ObjectForm.Properties.PredictableModifiable;
import kernbeisser.Security.Utils.Getter;

public class AccessCheckingLabel<T> extends JLabel
    implements BoundedReadProperty<T, String>, PredictableModifiable<T>, ObjectFormComponent<T> {

  private final Getter<T, String> getter;

  public AccessCheckingLabel(Getter<T, String> getter) {
    this.getter = getter;
  }

  private String original;

  @Override
  public void setReadable(boolean b) {
    super.setText(b ? original : "[Keine Leseberechtigung]");
  }

  @Override
  public void setText(String text) {
    original = text;
    super.setText(original);
  }

  @Override
  public String get(T t) throws PermissionKeyRequiredException {
    return getter.get(t);
  }

  @Override
  public boolean isPropertyModifiable(T parent) {
    return false;
  }

  @Override
  public void setData(String s) {
    setText(s);
  }
}
