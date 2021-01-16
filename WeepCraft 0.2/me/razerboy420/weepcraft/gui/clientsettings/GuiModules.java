package me.razerboy420.weepcraft.gui.clientsettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.VisibleMods;
import me.razerboy420.weepcraft.gui.clientsettings.extras.ModuleItem;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class GuiModules extends GuiScreen {

   public GuiScreen parent;
   public ArrayList binds = new ArrayList();
   public int amountScolled;
   public GuiButton toggleshown;
   public GuiButton togglemod;


   public GuiModules(GuiScreen parent) {
      this.parent = parent;
   }

   public void initGui() {
      this.buttonList.clear();
      this.toggleshown = new GuiButton(1, this.width / 2 - 40, this.height - 22, 120, 20, "Toggle Shown");
      this.buttonList.add(this.toggleshown);
      this.buttonList.add(new GuiButton(2, this.width / 2 + 120, this.height - 22, 70, 20, "Back"));
      this.togglemod = new GuiButton(3, this.width / 2 - 40 - 130, this.height - 22, 120, 20, "Toggle Module");
      this.buttonList.add(this.togglemod);
      this.registerBinds();
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.toggleshown.enabled = this.getSelectedBind() != null;
      this.togglemod.enabled = this.getSelectedBind() != null;
      drawRect(0.0F, 0.0F, (float)this.width, (float)this.height, 1610612736);
      Iterator kbi = this.binds.iterator();

      while(kbi.hasNext()) {
         ModuleItem weepcraftString = (ModuleItem)kbi.next();
         weepcraftString.draw();
      }

      this.overlayBackground(0, 30, 255, 255);
      this.overlayBackground(this.height - 25, this.height, 255, 255);
      this.drawGradientRect(0, 30, this.width, 33, -16777216, 0);
      String weepcraftString1 = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString1 + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);

      ModuleItem kbi1;
      for(Iterator var6 = this.binds.iterator(); var6.hasNext(); kbi1.y += (float)this.amountScolled) {
         kbi1 = (ModuleItem)var6.next();
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      int var10000 = button.id;
      if(button.id == 1) {
         this.getSelectedBind().mod.setVisible(!this.getSelectedBind().mod.isVisible());
         VisibleMods.save();
      }

      if(button.id == 2) {
         Wrapper.mc().displayGuiScreen(this.parent);
      }

      if(button.id == 3) {
         this.getSelectedBind().mod.toggle();
      }

      super.actionPerformed(button);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var5 = this.binds.iterator();

      while(var5.hasNext()) {
         ModuleItem kbi = (ModuleItem)var5.next();
         if(kbi.isHovered() && mouseY < this.height - 25) {
            ModuleItem kbi1;
            for(Iterator var7 = this.binds.iterator(); var7.hasNext(); kbi1.selected = false) {
               kbi1 = (ModuleItem)var7.next();
            }

            kbi.selected = true;
            break;
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   public void handleMouseInput() throws IOException {
      if(Mouse.next() && !Mouse.isButtonDown(0)) {
         int var4 = Mouse.getEventDWheel();
         if(var4 != 0) {
            var4 = var4 < 0?-1:1;
            int i;
            ModuleItem kbi;
            Iterator var41;
            if(var4 == 1) {
               for(i = 0; i < 33; ++i) {
                  if(((ModuleItem)this.binds.get(0)).y <= 32.0F) {
                     for(var41 = this.binds.iterator(); var41.hasNext(); ++kbi.y) {
                        kbi = (ModuleItem)var41.next();
                     }
                  }
               }
            }

            if(var4 == -1) {
               for(i = 0; i < 33; ++i) {
                  if(((ModuleItem)this.binds.get(this.binds.size() - 1)).y + ((ModuleItem)this.binds.get(this.binds.size() - 1)).height >= (float)(this.height - 25)) {
                     for(var41 = this.binds.iterator(); var41.hasNext(); --kbi.y) {
                        kbi = (ModuleItem)var41.next();
                     }
                  }
               }
            }
         }
      }

      super.handleMouseInput();
   }

   protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float f = 32.0F;
      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      vertexbuffer.pos(0.0D, (double)endY, 0.0D).tex(0.0D, (double)((float)endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
      vertexbuffer.pos((double)(0 + this.width), (double)endY, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
      vertexbuffer.pos((double)(0 + this.width), (double)startY, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
      vertexbuffer.pos(0.0D, (double)startY, 0.0D).tex(0.0D, (double)((float)startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
      tessellator.draw();
   }

   public void registerBinds() {
      this.binds.clear();
      int count = 0;

      for(Iterator var3 = Weepcraft.getMods().iterator(); var3.hasNext(); ++count) {
         Module mod = (Module)var3.next();
         int y = 33 + 33 * count;
         this.binds.add(new ModuleItem(mod, (float)(this.width / 2 - 110), (float)y, 220.0F, 33.0F));
      }

   }

   public ModuleItem getSelectedBind() {
      Iterator var2 = this.binds.iterator();

      while(var2.hasNext()) {
         ModuleItem kbi = (ModuleItem)var2.next();
         if(kbi.selected) {
            return kbi;
         }
      }

      return null;
   }
}
