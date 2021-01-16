package me.razerboy420.weepcraft.gui.servers;

import java.io.IOException;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.servers.serveritem.ServerItem;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public class GuiWCServers extends GuiScreen {

   public GuiButton connect;


   public void initGui() {
      ServerItem.items.clear();
      byte count = 0;
      int y = 32 + 31 * count;
      ServerItem.items.add(new ServerItem("mc-cental.net", "mc-central.net", "rlly shit server easy to get good gear", "1.8-1.12", this.width / 2 - 120, y, 240, 32));
      int count1 = count + 1;
      this.connect = new GuiButton(0, this.width / 2 - 101, this.height - 22, 100, 20, "Connect");
      this.buttonList.add(this.connect);
      this.buttonList.add(new GuiButton(1, this.width / 2 + 1, this.height - 22, 100, 20, "Back"));
      super.initGui();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.connect.enabled = this.getSelectedServer() != null;
      drawRect(0.0F, 0.0F, (float)this.width, (float)this.height, 1610612736);
      Iterator var5 = ServerItem.items.iterator();

      while(var5.hasNext()) {
         ServerItem weepcraftString = (ServerItem)var5.next();
         weepcraftString.draw();
      }

      this.overlayBackground(0, 30, 255, 255);
      this.overlayBackground(this.height - 25, this.height, 255, 255);
      drawCenteredString(Wrapper.fr(), ColorUtil.getColor(Weepcraft.normalColor) + "More will be added soon.", (float)(this.width / 2), 13.0F, -1);
      this.drawGradientRect(0, 30, this.width, 33, -16777216, 0);
      String weepcraftString1 = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
      drawCenteredString(Wrapper.fr(), weepcraftString1 + " " + ColorUtil.getColor(Weepcraft.normalColor) + Weepcraft.version + "v", (float)(this.width / 2), 2.0F, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      Iterator var5 = ServerItem.items.iterator();

      while(var5.hasNext()) {
         ServerItem s = (ServerItem)var5.next();
         if(s.isHovered()) {
            Iterator var7 = ServerItem.items.iterator();

            while(var7.hasNext()) {
               ServerItem s1 = (ServerItem)var7.next();
               if(!s1.isHovered()) {
                  s1.selected = false;
               }
            }

            s.selected = true;
            break;
         }
      }

      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         int port = 25565;

         try {
            port = Integer.parseInt(this.getSelectedServer().ip.split(":")[1]);
         } catch (Exception var4) {
            ;
         }

         String server = this.getSelectedServer().proxy + this.getSelectedServer().ip.split(":")[0];
         if(!server.endsWith(".")) {
            server = server + ".";
         }

         if(server.equalsIgnoreCase("play.weepcraft.xyz.")) {
            server = "107.161.169.13";
            port = 25566;
         }

         Wrapper.mc().displayGuiScreen(new GuiConnecting(this, Wrapper.mc(), server, port));
      }

      if(button.id == 1) {
         Wrapper.mc().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
      }

      super.actionPerformed(button);
   }

   public void handleMouseInput() throws IOException {
      if(Mouse.next() && !Mouse.isButtonDown(0)) {
         int var4 = Mouse.getEventDWheel();
         if(var4 != 0) {
            var4 = var4 < 0?-1:1;
            int i;
            ServerItem kbi;
            Iterator var41;
            if(var4 == 1) {
               for(i = 0; i < 32; ++i) {
                  if(((ServerItem)ServerItem.items.get(0)).y <= 32) {
                     for(var41 = ServerItem.items.iterator(); var41.hasNext(); ++kbi.y) {
                        kbi = (ServerItem)var41.next();
                     }
                  }
               }
            }

            if(var4 == -1) {
               for(i = 0; i < 32; ++i) {
                  if(((ServerItem)ServerItem.items.get(ServerItem.items.size() - 1)).y + ((ServerItem)ServerItem.items.get(ServerItem.items.size() - 1)).height >= this.height - 25) {
                     for(var41 = ServerItem.items.iterator(); var41.hasNext(); --kbi.y) {
                        kbi = (ServerItem)var41.next();
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

   public ServerItem getSelectedServer() {
      Iterator var2 = ServerItem.items.iterator();

      while(var2.hasNext()) {
         ServerItem i = (ServerItem)var2.next();
         if(i.selected) {
            return i;
         }
      }

      return null;
   }
}
