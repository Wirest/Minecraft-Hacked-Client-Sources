package saint.valuestuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import saint.Saint;

public final class Value {
   private Object value;
   private final Object defaultValue;
   private final String name;
   public boolean isValueBoolean = false;
   public boolean isValueInteger = false;
   public boolean isValueFloat = false;
   public boolean isValueDouble = false;
   public boolean isValueLong = false;
   public boolean isValueByte = false;
   public static final List list = new ArrayList();

   public Value(String name, Object value) {
      this.defaultValue = value;
      this.name = name;
      this.value = value;
      if (value instanceof Boolean) {
         this.isValueBoolean = true;
      } else if (value instanceof Integer) {
         this.isValueInteger = true;
      } else if (value instanceof Float) {
         this.isValueFloat = true;
      } else if (value instanceof Double) {
         this.isValueDouble = true;
      } else if (value instanceof Long) {
         this.isValueLong = true;
      } else if (value instanceof Byte) {
         this.isValueByte = true;
      }

      list.add(this);
      Collections.sort(list, new Comparator() {
         public int compare(Value val1, Value val2) {
            return val1.getValueName().compareTo(val2.getValueName());
         }
      });
      Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
   }

   public final String getValueName() {
      return this.name;
   }

   public final Object getDefaultValue() {
      return this.defaultValue;
   }

   public final Object getValueState() {
      return this.value;
   }

   public final void setValueState(Object value) {
      this.value = value;
      Saint.getFileManager().getFileUsingName("valueconfiguration").saveFile();
   }
}
