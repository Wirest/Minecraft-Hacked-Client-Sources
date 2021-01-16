package org.m0jang.crystal.GUI.click.window;

import java.security.SecureRandom;
import java.util.ArrayList;

public class WindowPreset extends ArrayList {
   public String title;
   public int id;
   private boolean editable = true;

   public WindowPreset(String title) {
      this.title = title;
      this.id = (new SecureRandom()).nextInt() & Integer.MAX_VALUE;
   }

   public boolean add(Window e) {
      return this.isEditable() ? super.add(e) : false;
   }

   public Window remove(int index) {
      return this.isEditable() ? (Window)super.remove(index) : null;
   }

   public void clear() {
      if (this.isEditable()) {
         super.clear();
      }

   }

   public boolean isEditable() {
      return this.editable;
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }
}
