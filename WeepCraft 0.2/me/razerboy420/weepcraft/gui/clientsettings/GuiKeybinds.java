package me.razerboy420.weepcraft.gui.clientsettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.files.Keybinds;
import me.razerboy420.weepcraft.gui.clientsettings.extras.KeybindItem;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiKeybinds extends GuiScreen {

   public GuiScreen parent;
   public ArrayList binds = new ArrayList();
   public boolean binding;
   public GuiButton remove;


   public GuiKeybinds(GuiScreen parent) {
      this.remove = new GuiButton(1, this.width / 2 + 80, this.height - 22, 100, 20, "Remove");
      this.parent = parent;
   }

   public void initGui() {
      this.buttonList.clear();
      Iterator var2 = Weepcraft.keybinds.iterator();

      while(var2.hasNext()) {
         Keybind k = (Keybind)var2.next();
         if(k.mod == ModuleManager.gui) {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 170, 5, 120, 20, ColorUtil.getColor(Weepcraft.normalColor) + "Gui: " + Keyboard.getKeyName(k.getKey())));
         }
      }

      this.remove = new GuiButton(1, this.width / 2 - 40, this.height - 22, 120, 20, "Remove");
      this.buttonList.add(this.remove);
      this.buttonList.add(new GuiButton(2, this.width / 2 + 120, this.height - 22, 70, 20, "Back"));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 40 - 130, this.height - 22, 120, 20, "Add"));
      this.registerBinds();
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.remove.enabled = this.getSelectedBind() != null;
      drawRect(0.0F, 0.0F, (float)this.width, (float)this.height, 1610612736);
      Iterator var5 = this.binds.iterator();

      while(var5.hasNext()) {
         KeybindItem weepcraftString = (KeybindItem)var5.next();
         weepcraftString.draw();
      }

      var5 = this.buttonList.iterator();

      while(var5.hasNext()) {
         GuiButton weepcraftString1 = (GuiButton)var5.next();
         if(weepcraftString1.id == 0) {
            if(this.binding) {
               weepcraftString1.displayString = ColorUtil.getColor(Weepcraft.normalColor) + "> <";
            } else {
               Iterator var7 = Weepcraft.keybinds.iterator();

               while(var7.hasNext()) {
                  Keybind k = (Keybind)var7.next();
                  if(k.mod == ModuleManager.gui) {
                     weepcraftString1.displayString = ColorUtil.getColor(Weepcraft.normalColor) + "Gui: " + Keyboard.getKeyName(k.getKey());
                  }
               }
            }
         }
      }

      this.overlayBackground(0, 30, 255, 255);
      this.overlayBackground(this.height - 25, this.height, 255, 255);
      this.drawGradientRect(0, 30, this.width, 33, -16777216, 0);
      String weepcraftString2 = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString2 + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         this.binding = !this.binding;
      }

      if(button.id == 1) {
         Weepcraft.keybinds.remove(this.getSelectedBind().keybind);
         Keybinds.save();
         this.initGui();
      }

      if(button.id == 2) {
         Wrapper.mc().displayGuiScreen(this.parent);
      }

      if(button.id == 3) {
         Wrapper.mc().displayGuiScreen(new GuiAddBind(this));
      }

      super.actionPerformed(button);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      boolean hitit = false;
      if(this.binding) {
         if(keyCode == 1) {
            hitit = true;
            this.binding = false;
         } else {
            Iterator var5 = Weepcraft.keybinds.iterator();

            while(var5.hasNext()) {
               Keybind k = (Keybind)var5.next();
               if(k.mod == ModuleManager.gui) {
                  k.setKey(keyCode);
               }
            }

            this.binding = false;
         }
      }

      if(!hitit) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var5 = this.binds.iterator();

      while(var5.hasNext()) {
         KeybindItem kbi = (KeybindItem)var5.next();
         if(kbi.isHovered() && mouseY < this.height - 25) {
            KeybindItem kbi1;
            for(Iterator var7 = this.binds.iterator(); var7.hasNext(); kbi1.selected = false) {
               kbi1 = (KeybindItem)var7.next();
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
            KeybindItem kbi;
            Iterator var41;
            if(var4 == 1) {
               for(i = 0; i < 22; ++i) {
                  if(((KeybindItem)this.binds.get(0)).y <= 32.0F) {
                     for(var41 = this.binds.iterator(); var41.hasNext(); ++kbi.y) {
                        kbi = (KeybindItem)var41.next();
                     }
                  }
               }
            }

            if(var4 == -1) {
               for(i = 0; i < 22; ++i) {
                  if(((KeybindItem)this.binds.get(this.binds.size() - 1)).y + ((KeybindItem)this.binds.get(this.binds.size() - 1)).height >= (float)(this.height - 25)) {
                     for(var41 = this.binds.iterator(); var41.hasNext(); --kbi.y) {
                        kbi = (KeybindItem)var41.next();
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
      Iterator var3 = Weepcraft.keybinds.iterator();

      while(var3.hasNext()) {
         Keybind k = (Keybind)var3.next();
         if(k.getKey() != 0 && !k.getMod().getName().equalsIgnoreCase("GUI")) {
            int y = 33 + 22 * count;
            this.binds.add(new KeybindItem(k, (float)(this.width / 2 - 110), (float)y, 220.0F, 22.0F));
            ++count;
         }
      }

   }

   public KeybindItem getSelectedBind() {
      Iterator var2 = this.binds.iterator();

      while(var2.hasNext()) {
         KeybindItem kbi = (KeybindItem)var2.next();
         if(kbi.selected) {
            return kbi;
         }
      }

      return null;
   }
}
