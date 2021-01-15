package saint.screens;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiUnfinished extends GuiScreen {
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/saintbg.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), (float)scaledRes.getScaledWidth(), (float)scaledRes.getScaledHeight());
      GL11.glPushMatrix();
      GL11.glScaled(3.0D, 3.0D, 3.0D);
      this.drawCenteredString(this.fontRendererObj, "WORK IN PROGRESS", scaledRes.getScaledHeight() / 3, 40, -8388608);
      this.drawCenteredString(this.fontRendererObj, "PRESS ESC TO EXIT", scaledRes.getScaledHeight() / 3, 50, -8388608);
      GL11.glPopMatrix();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }
}
