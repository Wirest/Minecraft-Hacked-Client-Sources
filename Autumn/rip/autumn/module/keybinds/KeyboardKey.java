package rip.autumn.module.keybinds;

import org.lwjgl.input.Keyboard;

public class KeyboardKey {
   private String key;

   public KeyboardKey(String key) {
      this.key = key;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void setKeyCode(int keyCode) {
      this.key = Keyboard.getKeyName(keyCode);
   }

   public int getKeyCode() {
      return Keyboard.getKeyIndex(this.key.toUpperCase());
   }
}
