package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Values.Value;

public class Slider extends BasicSlider {
   public Value storage;
   public String setting;

   public Slider(Value storage, Window window, int id, int offX, int offY, String title, String setting, float min, float max, float increment) {
      super(window, id, offX, offY, title, min, max, increment);
      if (storage != null) {
         this.value = storage.getFloatValue();
      }

      this.storage = storage;
      this.setting = setting;
      this.type = "Slider";
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
      if (this.storage != null) {
         if (this.isDragging) {
            this.storage.setFloatValue(this.value);
         } else {
            this.value = this.storage.getFloatValue();
         }
      }

   }
}
