package me.slowly.client.ui.scriptmenu.elements;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class UIElementTextField {
   private String text = "";
   private int width = 200;
   private int height = 11;
   private int MAX_LENGTH = 30;
   private boolean focused;
   private MouseInputHandler handler = new MouseInputHandler(0);
   private ArrayList keyList = new ArrayList();

   public UIElementTextField() {
      this.addLetters();
   }

   public void draw(int x, int y, int mouseX, int mouseY) {
      this.width = 100;
      Gui.drawBorderedRect((float)x, (float)y, (float)(x + this.width), (float)(y + this.height), 0.5F, Colors.GREY.c, 0);
      if (this.focused) {
         Gui.drawBorderedRect((float)x, (float)y, (float)(x + this.width), (float)(y + this.height), 0.5F, ClientUtil.reAlpha(-1, 0.1F), 0);
      }

      UnicodeFontRenderer font = Client.getInstance().getFontManager().consolas14;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(x, y, this.width, this.height);
      font.drawString(this.text, (float)(x + 1), (float)y + (float)(this.height - font.getStringHeight(this.text)) / 2.0F, -2894893);
      GL11.glDisable(3089);
      GL11.glPopMatrix();
      boolean hover = mouseX >= x && mouseX <= x + this.width && mouseY >= y && mouseY <= y + this.height;
      if (this.handler.canExcecute()) {
         this.focused = hover;
      }

      String input = this.getInput();
      if (this.focused) {
         if (input == "DELETE") {
            if (this.text.length() > 0) {
               this.text = this.text.substring(0, this.text.length() - 1);
            }
         } else if (this.text.length() < this.MAX_LENGTH) {
            this.text = this.text + input;
         }
      }

   }

   private String getInput() {
      Iterator var2 = this.keyList.iterator();

      while(var2.hasNext()) {
         UIElementTextField.Key key = (UIElementTextField.Key)var2.next();
         if (Keyboard.isKeyDown(key.keyCode)) {
            if (!key.pressed) {
               key.pressed = true;
               if (key.key.equalsIgnoreCase("BACK")) {
                  return "DELETE";
               }

               boolean upperCase = Keyboard.isKeyDown(42);
               return upperCase ? key.key.toUpperCase() : key.key;
            }
         } else {
            key.pressed = false;
         }
      }

      return "";
   }

   public String getText() {
      return this.text;
   }

   private void addLetters() {
      this.keyList.add(new UIElementTextField.Key("BACK"));
      String letters = "abcdefghijklmnopqrstuvwxyz0123456789 ";

      for(int i = 0; i < letters.length(); ++i) {
         this.keyList.add(new UIElementTextField.Key(Character.toString(letters.charAt(i))));
      }

   }

   public void setText(String text) {
      this.text = text;
   }

   class Key {
      private String key;
      private int keyCode;
      private boolean pressed;

      public Key(String key) {
         this.key = key;
         this.keyCode = Keyboard.getKeyIndex(key.toUpperCase());
      }
   }
}
