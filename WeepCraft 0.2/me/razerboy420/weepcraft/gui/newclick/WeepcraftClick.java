package me.razerboy420.weepcraft.gui.newclick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.newclick.extras.CategoryButton;
import me.razerboy420.weepcraft.gui.newclick.extras.ModuleButton;
import me.razerboy420.weepcraft.gui.newclick.extras.ValueButton;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class WeepcraftClick extends GuiScreen {

   public static ArrayList catbs = new ArrayList();
   public static ArrayList modbs = new ArrayList();
   public static ArrayList valuebs = new ArrayList();
   int screennum = 0;


   public void initGui() {
      catbs.clear();
      int catcount = 0;
      Module.Category[] var5;
      int var4 = (var5 = Module.Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Module.Category c = var5[var3];
         float y = (float)(this.height / 2 - Module.Category.values().length * 12 / 2 + 12 * catcount);
         float x = (float)(this.width / 2 - 20);
         catbs.add(new CategoryButton(c, x, y, 40.0F, 12.0F));
         ++catcount;
      }

      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      Wrapper.drawBorderRect((float)(this.width / 2 - 20), (float)(this.height / 2 - Module.Category.values().length / 2), (float)(this.width / 2 - 20 + 40), (float)(this.height / 2 - Module.Category.values().length / 2 + 12 * Module.Category.values().length), ColorUtil.getHexColor(Weepcraft.borderColor), ColorUtil.getHexColor(Weepcraft.panlColor) & 1358954495, 1.0F);
      if(this.screennum == 0) {
         Iterator var5 = catbs.iterator();

         while(var5.hasNext()) {
            CategoryButton b = (CategoryButton)var5.next();
            b.draw();
         }
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
   }

   public void onResize(Minecraft mcIn, int w, int h) {
      super.onResize(mcIn, w, h);
   }

   public ArrayList getSubValues(Value v) {
      ArrayList vs = new ArrayList();
      Iterator var4 = Value.modes.iterator();

      while(var4.hasNext()) {
         Value va = (Value)var4.next();
         if(va.needsvalopen && va.neededval == v) {
            vs.add(va);
         }
      }

      return vs;
   }

   public CategoryButton getCatButtonInSlot(int slot) {
      return (CategoryButton)catbs.get(slot);
   }

   public ModuleButton getModButtonInSlot(int slot) {
      return (ModuleButton)modbs.get(slot);
   }

   public ValueButton getValButtonInSlot(int slot) {
      return (ValueButton)valuebs.get(slot);
   }
}
