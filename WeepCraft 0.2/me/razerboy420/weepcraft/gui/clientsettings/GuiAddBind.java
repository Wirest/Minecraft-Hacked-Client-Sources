package me.razerboy420.weepcraft.gui.clientsettings;

import java.io.IOException;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.Keybinds;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiAddBind extends GuiScreen {

   public GuiTextField key;
   public GuiTextField command;
   public GuiButton add;
   public GuiScreen parent;
   public String text;
   public boolean onbutton;


   public GuiAddBind(GuiScreen parent) {
      this.key = new GuiTextField(0, Wrapper.fr(), this.width / 2 - 100, this.height / 2 - 60, 200, 20);
      this.command = new GuiTextField(1, Wrapper.fr(), this.width / 2 - 100, this.height / 2 - 40, 200, 20);
      this.add = new GuiButton(0, this.width / 2 - 30, this.height / 2 - 10, 60, 20, "Add");
      this.text = "";
      this.onbutton = false;
      this.parent = parent;
   }

   public void initGui() {
      this.key = new GuiTextField(0, Wrapper.fr(), this.width / 2 - 90, this.height / 2 - 90, 140, 20);
      this.command = new GuiTextField(1, Wrapper.fr(), this.width / 2 - 90, this.height / 2 - 50, 180, 20);
      this.key.setFocused(true);
      this.key.setCanLoseFocus(true);
      this.command.setCanLoseFocus(true);
      this.add = new GuiButton(0, this.width / 2 - 50, this.height / 2 - 20, 100, 20, "Add");
      this.buttonList.add(this.add);
      this.buttonList.add(new GuiButton(1, this.width / 2 - 56, this.height / 2 + 30, 114, 20, "Back"));
      this.buttonList.add(new GuiButton(3, this.key.xPosition + 142, this.key.yPosition, 38, 20, "..."));
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      String weepcraftString = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Key", (float)(this.width / 2), (float)(this.height / 2 - 100), -16777216);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Command", (float)(this.width / 2), (float)(this.height / 2 - 60), -16777216);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.disabledColor) + this.text, (float)(this.width / 2), (float)(this.height / 2 + 80), -1);
      this.key.drawTextBox();
      this.command.drawTextBox();
      this.key.updateCursorCounter();
      this.command.updateCursorCounter();
      this.add.enabled = !this.key.getText().isEmpty() && !this.command.getText().isEmpty();
      Iterator var6 = this.buttonList.iterator();

      while(var6.hasNext()) {
         GuiButton b = (GuiButton)var6.next();
         if(b.id == 3) {
            b.displayString = this.onbutton?"> <":"...";
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      boolean hitit = false;
      if(this.onbutton) {
         if(keyCode == 1) {
            hitit = true;
            this.onbutton = false;
         } else {
            this.key.setText(Keyboard.getKeyName(keyCode));
            this.onbutton = false;
         }
      } else {
         if(this.key.isFocused()) {
            this.key.textboxKeyTyped(typedChar, keyCode);
         }

         if(this.command.isFocused()) {
            this.command.textboxKeyTyped(typedChar, keyCode);
         }

         if(!hitit) {
            super.keyTyped(typedChar, keyCode);
         }

      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.key.mouseClicked(mouseX, mouseY, mouseButton);
      this.command.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         if(Keyboard.getKeyIndex(this.key.getText()) == 0) {
            this.text = "Key not found.";
            return;
         }

         Iterator var3 = Weepcraft.getMods().iterator();

         while(var3.hasNext()) {
            Module mod = (Module)var3.next();
            if(mod.getName().equalsIgnoreCase(this.command.getText())) {
               Weepcraft.keybinds.add(new Keybind(mod, Keyboard.getKeyIndex(this.key.getText())));
               Keybinds.save();
               Wrapper.mc().displayGuiScreen(this.parent);
               return;
            }
         }

         Keybinds.save();
         this.text = "Mod not found.";
      }

      if(button.id == 1) {
         Wrapper.mc().displayGuiScreen(this.parent);
      }

      if(button.id == 3) {
         this.onbutton = true;
      }

      super.actionPerformed(button);
   }
}
