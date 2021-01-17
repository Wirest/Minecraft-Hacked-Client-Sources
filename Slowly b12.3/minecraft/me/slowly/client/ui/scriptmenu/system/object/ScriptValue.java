package me.slowly.client.ui.scriptmenu.system.object;

import java.util.ArrayList;
import me.slowly.client.ui.scriptmenu.elements.UIElementComboBox;
import me.slowly.client.ui.scriptmenu.elements.UIElementNumeric;
import me.slowly.client.value.Value;

public class ScriptValue {
   private ScriptMod mod;
   private Value value;
   private UIElementComboBox comboList;
   private UIElementNumeric numeric;
   private boolean edit;

   public ScriptValue(ScriptMod scm, Value value) {
      this.mod = scm;
      this.value = value;
      this.edit = true;
      if (value.isValueBoolean) {
         ArrayList falseTrue = new ArrayList();
         falseTrue.add("true");
         falseTrue.add("false");
         this.comboList = new UIElementComboBox(falseTrue);
      } else if (value.isValueMode) {
         this.comboList = new UIElementComboBox(value.mode);
      } else if (value.isValueDouble) {
         this.numeric = new UIElementNumeric(((Double)value.getValueState()).doubleValue(), ((Double)value.getValueMin()).doubleValue(), ((Double)value.getValueMax()).doubleValue(), value.getSteps());
      }

   }

   public UIElementComboBox getComboList() {
      return this.comboList;
   }

   public UIElementNumeric getNumeric() {
      return this.numeric;
   }

   public ScriptMod getMod() {
      return this.mod;
   }

   public void setMod(ScriptMod mod) {
      this.mod = mod;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public boolean isEditable() {
      return this.edit;
   }

   public void setEditable(boolean edit) {
      if (edit) {
         this.mod.setEditable(true);
      }

      this.edit = edit;
   }

   public String getName() {
      return this.value.isValueMode ? this.value.getModeTitle() : this.value.getValueName().split("_")[1];
   }
}
