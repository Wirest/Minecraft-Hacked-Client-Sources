package saint.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import saint.threads.AutoUpdater;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class GuiUpdating extends GuiScreen {
   private int count = 0;
   private AutoUpdater thread;
   public static boolean isDone = false;
   public static boolean isDownloading = false;
   public static boolean isExtracting = false;
   public static boolean isDeleting = false;

   public void initGui() {
      try {
         this.thread = new AutoUpdater("http://saintclient.grn.cc/Saint.jar");
         this.thread.start();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/saintbg.png"));
      Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, RenderHelper.getScaledRes().getScaledWidth(), RenderHelper.getScaledRes().getScaledHeight(), RenderHelper.getScaledRes().getScaledWidth(), RenderHelper.getScaledRes().getScaledHeight(), (float)RenderHelper.getScaledRes().getScaledWidth(), (float)RenderHelper.getScaledRes().getScaledHeight());
      super.drawScreen(mouseX, mouseY, partialTicks);
      String text = "Downloading - " + this.thread.getPercentage();
      RenderHelper.drawBorderedRect((float)(RenderHelper.getScaledRes().getScaledWidth() / 2 - 1) - RenderHelper.getNahrFont().getStringWidth(text) / 2.0F, 50.0F, (float)(RenderHelper.getScaledRes().getScaledWidth() / 2) - RenderHelper.getNahrFont().getStringWidth(text) / 2.0F + 1.0F + RenderHelper.getNahrFont().getStringWidth(text), 65.0F, 1.0F, -587202560, Integer.MIN_VALUE);
      RenderHelper.getNahrFont().drawString(text, (float)(RenderHelper.getScaledRes().getScaledWidth() / 2) - RenderHelper.getNahrFont().getStringWidth(text) / 2.0F, 50.0F, NahrFont.FontType.SHADOW_THIN, -1, -16777216);
   }

   public static void setup() {
      Minecraft.getMinecraft().displayGuiScreen(new GuiUpdating());
   }
}
