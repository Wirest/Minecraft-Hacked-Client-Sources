package saint.screens;

import net.java.games.input.Version;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import saint.Saint;

public class GuiMainMenuHook extends GuiMainMenu {
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/saintbg.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
      String var10 = "§7Saint for Minecraft 1.8: Version #" + (Saint.isLatestVersion() ? "§a" : "§c") + Version.getVersion();
      this.drawString(this.fontRendererObj, var10, 2, this.height - 10, -1);
      String var11 = "§7Made by §fAndrew§7.";
      this.drawString(this.fontRendererObj, var11, this.width - this.fontRendererObj.getStringWidth(var11) - 2, this.height - 10, -1);
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
