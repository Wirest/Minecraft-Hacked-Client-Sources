package net.minecraft.client.gui;

import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;

public class GuiKeyBindingList extends GuiListExtended {
   private final GuiControls field_148191_k;
   private final Minecraft mc;
   private final GuiListExtended.IGuiListEntry[] listEntries;
   private int maxListLabelWidth = 0;

   public GuiKeyBindingList(GuiControls controls, Minecraft mcIn) {
      super(mcIn, controls.width, controls.height, 63, controls.height - 32, 20);
      this.field_148191_k = controls;
      this.mc = mcIn;
      KeyBinding[] akeybinding = (KeyBinding[])((KeyBinding[])ArrayUtils.clone(mcIn.gameSettings.keyBindings));
      this.listEntries = new GuiListExtended.IGuiListEntry[akeybinding.length + KeyBinding.getKeybinds().size()];
      Arrays.sort((Object[])akeybinding);
      int i = 0;
      String s = null;
      KeyBinding[] var6 = akeybinding;
      int var7 = akeybinding.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         KeyBinding keybinding = var6[var8];
         String s1 = keybinding.getKeyCategory();
         if (!s1.equals(s)) {
            s = s1;
            this.listEntries[i++] = new GuiKeyBindingList.CategoryEntry(s1);
         }

         int j = mcIn.fontRendererObj.getStringWidth(I18n.format(keybinding.getKeyDescription()));
         if (j > this.maxListLabelWidth) {
            this.maxListLabelWidth = j;
         }

         this.listEntries[i++] = new GuiKeyBindingList.KeyEntry(keybinding);
      }

   }

   protected int getSize() {
      return this.listEntries.length;
   }

   public GuiListExtended.IGuiListEntry getListEntry(int index) {
      return this.listEntries[index];
   }

   protected int getScrollBarX() {
      return super.getScrollBarX() + 15;
   }

   public int getListWidth() {
      return super.getListWidth() + 32;
   }

   public class KeyEntry implements GuiListExtended.IGuiListEntry {
      private final KeyBinding keybinding;
      private final String keyDesc;
      private final GuiButton btnChangeKeyBinding;
      private final GuiButton btnReset;

      private KeyEntry(KeyBinding p_i45029_2_) {
         this.keybinding = p_i45029_2_;
         this.keyDesc = I18n.format(p_i45029_2_.getKeyDescription());
         this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.format(p_i45029_2_.getKeyDescription()));
         this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.format("controls.reset"));
      }

      public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
         boolean flag = GuiKeyBindingList.this.field_148191_k.buttonId == this.keybinding;
         FontRenderer var10000 = GuiKeyBindingList.this.mc.fontRendererObj;
         String var10001 = this.keyDesc;
         int var10002 = x + 90 - GuiKeyBindingList.this.maxListLabelWidth;
         int var10003 = y + slotHeight / 2;
         FontRenderer var10004 = GuiKeyBindingList.this.mc.fontRendererObj;
         var10000.drawString(var10001, var10002, var10003 - 9 / 2, 16777215);
         this.btnReset.xPosition = x + 190;
         this.btnReset.yPosition = y;
         this.btnReset.enabled = this.keybinding.getKeyCode() != this.keybinding.getKeyCodeDefault();
         this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
         this.btnChangeKeyBinding.xPosition = x + 105;
         this.btnChangeKeyBinding.yPosition = y;
         this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.getKeyCode());
         boolean flag1 = false;
         if (this.keybinding.getKeyCode() != 0) {
            KeyBinding[] var11 = GuiKeyBindingList.this.mc.gameSettings.keyBindings;
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               KeyBinding keybinding = var11[var13];
               if (keybinding != this.keybinding && keybinding.getKeyCode() == this.keybinding.getKeyCode()) {
                  flag1 = true;
                  break;
               }
            }
         }

         if (flag) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
         } else if (flag1) {
            this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
         }

         this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
      }

      public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
         if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
            GuiKeyBindingList.this.field_148191_k.buttonId = this.keybinding;
            return true;
         } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_)) {
            GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, this.keybinding.getKeyCodeDefault());
            KeyBinding.resetKeyBindingArrayAndHash();
            return true;
         } else {
            return false;
         }
      }

      public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
         this.btnChangeKeyBinding.mouseReleased(x, y);
         this.btnReset.mouseReleased(x, y);
      }

      public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
      }
   }

   public class CategoryEntry implements GuiListExtended.IGuiListEntry {
      private final String labelText;
      private final int labelWidth;

      public CategoryEntry(String p_i45028_2_) {
         this.labelText = I18n.format(p_i45028_2_);
         this.labelWidth = GuiKeyBindingList.this.mc.fontRendererObj.getStringWidth(this.labelText);
      }

      public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
         FontRenderer var10000 = GuiKeyBindingList.this.mc.fontRendererObj;
         String var10001 = this.labelText;
         int var10002 = GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2;
         int var10003 = y + slotHeight;
         FontRenderer var10004 = GuiKeyBindingList.this.mc.fontRendererObj;
         var10000.drawString(var10001, var10002, var10003 - 9 - 1, 16777215);
      }

      public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
         return false;
      }

      public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
      }

      public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
      }
   }
}
