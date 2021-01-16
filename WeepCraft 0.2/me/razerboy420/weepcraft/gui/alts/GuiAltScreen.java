package me.razerboy420.weepcraft.gui.alts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.alts.YggdrasilLoginBridge;
import me.razerboy420.weepcraft.files.AltsFile;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.MouseUtils;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class GuiAltScreen extends GuiScreen {

   public static String helptext = "Logged in as " + Wrapper.mc().getSession().getUsername();
   public ArrayList altbuttons = new ArrayList();
   public GuiButton loginbutton = new GuiButton(2, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 110, RenderUtils2D.newScaledResolution().getScaledHeight() - 61, 150, 20, "Login");
   public GuiButton random = new GuiButton(3, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 + 41, RenderUtils2D.newScaledResolution().getScaledHeight() - 61, 90, 20, "Random Alt");
   public GuiTextField search = new GuiTextField(500, Wrapper.fr(), RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 60, RenderUtils2D.newScaledResolution().getScaledHeight() - 30, 120, 15);
   public int scrollY;


   public void initGui() {
      Alt.alts.clear();
      AltsFile.load();
      this.altbuttons.clear();
      int count = 0;

      for(Iterator var3 = Alt.alts.iterator(); var3.hasNext(); ++count) {
         Alt width = (Alt)var3.next();
         int y = 40 + 41 * count;
         this.altbuttons.add(new AltButton((float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100), (float)y, 200.0F, 40.0F, width, width.isCracked()));
      }

      this.buttonList.clear();
      byte var5 = 80;
      this.loginbutton = new GuiButton(2, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 123, RenderUtils2D.newScaledResolution().getScaledHeight() - 61, var5, 20, "Login");
      this.random = new GuiButton(3, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 + 37, RenderUtils2D.newScaledResolution().getScaledHeight() - 61, var5 + 1, 20, "Random Alt");
      this.buttonList.add(new GuiButton(0, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 123, RenderUtils2D.newScaledResolution().getScaledHeight() - 40, 120, 20, "Cancel"));
      this.buttonList.add(new GuiButton(4, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 43, RenderUtils2D.newScaledResolution().getScaledHeight() - 61, var5 + 1, 20, "Remove"));
      this.buttonList.add(new GuiButton(1, RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 3, RenderUtils2D.newScaledResolution().getScaledHeight() - 40, 120, 20, "Add Alt"));
      this.buttonList.add(this.loginbutton);
      this.buttonList.add(this.random);
      this.search = new GuiTextField(500, Wrapper.fr(), RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 60, 22, 120, 15);
      this.search.setCanLoseFocus(true);
      this.search.setMaxStringLength(16);
      this.scrollY = 0;
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      Gui.drawRect(0.0F, 0.0F, (float)RenderUtils2D.newScaledResolution().getScaledWidth(), (float)RenderUtils2D.newScaledResolution().getScaledHeight(), 1879048192);
      this.loginbutton.enabled = this.getSelectedAlt() != null;
      this.random.enabled = !Alt.alts.isEmpty();
      int count = 0;

      for(Iterator var6 = this.altbuttons.iterator(); var6.hasNext(); ++count) {
         AltButton b = (AltButton)var6.next();
         if(b.y < (float)(RenderUtils2D.newScaledResolution().getScaledHeight() - 72) && b.y + b.height > 41.0F) {
            if(this.search.getText().isEmpty()) {
               b.draw();
               Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + (count + 1), b.x + b.width - 1.0F - (float)Wrapper.fr().getStringWidth(String.valueOf(count + 1)), b.y + 2.0F, -1);
            } else if(b.alt.name.toLowerCase().contains(this.search.getText().toLowerCase())) {
               b.draw();
               Gui.drawString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + (count + 1), b.x + b.width - 1.0F - (float)Wrapper.fr().getStringWidth(String.valueOf(count + 1)), b.y + 2.0F, -1);
            }
         }
      }

      this.overlayBackground(this.height, this.height - 70, 255, 255);
      this.drawGradientRect(0, this.height - 70, this.width, this.height - 73, -16777216, 0);
      this.overlayBackground(0, 38, 255, 255);
      this.drawGradientRect(0, 38, this.width, 41, -16777216, 0);
      Weepcraft.drawWeepString();
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.enabledColor) + helptext, (float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2), (float)(this.height - 15), -16711936);
      Gui.drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "Alts: " + Alt.alts.size(), (float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2), 12.0F, -1);
      this.search.drawTextBox();
      this.search.updateCursorCounter();
      super.drawScreen(mouseX, mouseY, partialTicks);
      Mouse.isButtonDown(0);
   }

   public void handleMouseInput() throws IOException {
      if(!this.altbuttons.isEmpty()) {
         int var4;
         AltButton i;
         Iterator mod;
         for(var4 = 0; var4 < 80; ++var4) {
            if(((AltButton)this.altbuttons.get(0)).y > 40.0F) {
               for(mod = this.altbuttons.iterator(); mod.hasNext(); --i.y) {
                  i = (AltButton)mod.next();
               }
            }

            if(((AltButton)this.altbuttons.get(this.altbuttons.size() - 1)).y + ((AltButton)this.altbuttons.get(this.altbuttons.size() - 1)).height < (float)(RenderUtils2D.newScaledResolution().getScaledHeight() - 71)) {
               for(mod = this.altbuttons.iterator(); mod.hasNext(); ++i.y) {
                  i = (AltButton)mod.next();
               }
            }
         }

         if(!Mouse.isButtonDown(0)) {
            while(Mouse.next()) {
               var4 = Mouse.getEventDWheel();
               if(var4 != 0) {
                  var4 = var4 < 0?-1:1;
                  Iterator var41;
                  int var5;
                  AltButton var6;
                  if(var4 == 1) {
                     for(var5 = 0; var5 < 41; ++var5) {
                        if(((AltButton)this.altbuttons.get(0)).y < 40.0F) {
                           for(var41 = this.altbuttons.iterator(); var41.hasNext(); --this.scrollY) {
                              var6 = (AltButton)var41.next();
                              ++var6.y;
                           }
                        }
                     }
                  }

                  if(var4 == -1) {
                     for(var5 = 0; var5 < 41; ++var5) {
                        if(((AltButton)this.altbuttons.get(this.altbuttons.size() - 1)).y + ((AltButton)this.altbuttons.get(this.altbuttons.size() - 1)).height > (float)(RenderUtils2D.newScaledResolution().getScaledHeight() - 71)) {
                           for(var41 = this.altbuttons.iterator(); var41.hasNext(); ++this.scrollY) {
                              var6 = (AltButton)var41.next();
                              --var6.y;
                           }
                        }
                     }
                  }

                  for(mod = this.altbuttons.iterator(); mod.hasNext(); i = (AltButton)mod.next()) {
                     ;
                  }
               }
            }
         }
      }

      super.handleMouseInput();
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         Wrapper.mc().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
      }

      if(button.id == 1) {
         Wrapper.mc().displayGuiScreen(new GuiAddAltScreen(this));
      }

      if(button.id == 2) {
         if(Alt.savedsession == null) {
            Alt.savedsession = Wrapper.mc().getSession();
         }

         helptext = ColorUtil.getColor(Weepcraft.normalColor) + "Signing in...";

         try {
            if(YggdrasilLoginBridge.loginWithAlt(this.getSelectedAlt().alt) != null) {
               helptext = ColorUtil.getColor(Weepcraft.enabledColor) + "Logged in as " + Wrapper.mc().getSession().getUsername();
               if(!this.getSelectedAlt().alt.isCracked()) {
                  this.getSelectedAlt().alt.name = Wrapper.mc().getSession().getUsername();
                  AltsFile.save();
               }
            } else {
               helptext = ColorUtil.getColor(Weepcraft.disabledColor) + "Login failed";
            }
         } catch (Exception var7) {
            helptext = ColorUtil.getColor(Weepcraft.disabledColor) + "§4Login failed";
         }
      }

      if(button.id == 3) {
         helptext = "§cSigning in...";
         if(Alt.savedsession == null) {
            Alt.savedsession = Wrapper.mc().getSession();
         }

         if(YggdrasilLoginBridge.loginWithAlt(this.getRandomAlt()) != null) {
            helptext = "Logged in as " + Wrapper.mc().getSession().getUsername();
         }
      }

      if(button.id == 4) {
         if(this.getSelectedAlt() != null) {
            Alt alt1 = this.getSelectedAlt().alt;
            this.altbuttons.remove(this.getSelectedAlt());
            Alt.alts.remove(alt1);
            AltsFile.save();
            this.altbuttons.clear();
            int count = 0;
            Iterator var5 = Alt.alts.iterator();

            while(var5.hasNext()) {
               Alt alt = (Alt)var5.next();
               int y;
               if(!this.search.getText().isEmpty()) {
                  if(alt.name.toLowerCase().contains(this.search.getText().toLowerCase())) {
                     y = 40 + 41 * count;
                     this.altbuttons.add(new AltButton((float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100), (float)y, 200.0F, 40.0F, alt, alt.isCracked()));
                     ++count;
                  }
               } else if(alt.name.toLowerCase().contains(this.search.getText().toLowerCase())) {
                  y = 40 + 41 * count;
                  alt.name = alt.name.replace("§l", "").replace("§r", "");
                  this.altbuttons.add(new AltButton((float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100), (float)y, 200.0F, 40.0F, alt, alt.isCracked()));
                  ++count;
               }
            }
         }

      }
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if(mouseButton == 0 && MouseUtils.getMouseY() < RenderUtils2D.newScaledResolution().getScaledHeight() - 70 && this.getHoveredAlt() != null) {
         Iterator var5 = this.altbuttons.iterator();

         while(var5.hasNext()) {
            AltButton b = (AltButton)var5.next();
            if(b.selected) {
               b.selected = false;
            }
         }

         this.getHoveredAlt().selected = true;
      }

      this.search.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if(this.search.isFocused()) {
         this.search.textboxKeyTyped(typedChar, keyCode);
         this.altbuttons.clear();
         int count = 0;
         Iterator var5 = Alt.alts.iterator();

         while(var5.hasNext()) {
            Alt alt = (Alt)var5.next();
            int y;
            if(!this.search.getText().isEmpty()) {
               if(alt.name.toLowerCase().contains(this.search.getText().toLowerCase())) {
                  y = 40 + 41 * count;
                  this.altbuttons.add(new AltButton((float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100), (float)y, 200.0F, 40.0F, alt, alt.isCracked()));
                  ++count;
               }
            } else if(alt.name.toLowerCase().contains(this.search.getText().toLowerCase())) {
               y = 40 + 41 * count;
               alt.name = alt.name.replace("§l", "").replace("§r", "");
               this.altbuttons.add(new AltButton((float)(RenderUtils2D.newScaledResolution().getScaledWidth() / 2 - 100), (float)y, 200.0F, 40.0F, alt, alt.isCracked()));
               ++count;
            }
         }

         if(keyCode == 28 || keyCode == 156) {
            this.search.setFocused(false);
         }
      }

      super.keyTyped(typedChar, keyCode);
   }

   public void onGuiClosed() {
      super.onGuiClosed();
   }

   public AltButton getHoveredAlt() {
      Iterator var2 = this.altbuttons.iterator();

      while(var2.hasNext()) {
         AltButton b = (AltButton)var2.next();
         if(b.isHovered()) {
            return b;
         }
      }

      return null;
   }

   public AltButton getSelectedAlt() {
      Iterator var2 = this.altbuttons.iterator();

      while(var2.hasNext()) {
         AltButton b = (AltButton)var2.next();
         if(b.selected) {
            return b;
         }
      }

      return null;
   }

   public Alt getRandomAlt() {
      return (Alt)Alt.alts.get((int)(Math.random() * (double)(Alt.alts.size() - 1)));
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
}
