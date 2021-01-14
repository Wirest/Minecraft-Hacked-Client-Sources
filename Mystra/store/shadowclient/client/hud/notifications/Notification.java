package store.shadowclient.client.hud.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.Color;

public class Notification {
    private NotificationType type;
    private String title;
    private String messsage;
    private long start;

    private long fadedIn;
    private long fadeOut;
    private long end;

    public Notification(NotificationType type, String messsage, int length) {
        this.type = type;
        this.messsage = messsage;

        fadedIn = 220 * length;
        fadeOut = fadedIn + 150 * length;
        end = fadeOut + fadedIn;
    }

    public void show() {
        start = System.currentTimeMillis();
    }

    public boolean isShown() {
        return getTime() <= end;
    }

    private long getTime() {
        return System.currentTimeMillis() - start;
    }

    public void render() {
        double offset = 0;
        int width = 140;
        int height = 28;
        long time = getTime();

        if (time < fadedIn) {
            offset = Math.tanh(time / (double) (fadedIn) * 4.0) * width;
        } else if (time > fadeOut) {
            offset = (Math.tanh(2.3 - (time - fadeOut) / (double) (end - fadeOut) * 2.8) * width);
        } else {
            offset = width;
        }

        Color color = new Color(0, 0, 0, 200);
        Color color1;

        if (type == NotificationType.INFO)
            color1 = new Color(0, 20, 50);
        else if (type == NotificationType.WARNING)
            color1 = new Color(204, 193, 0);
        else {
            color1 = new Color(204, 0, 18);
            int i = Math.max(0, Math.min(255, (int) (Math.sin(time / 100.0) * 255.0 / 2 + 127.5)));
            color = new Color(i, 0, 0, 220);
        }

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        drawRect(GuiScreen.width - offset, GuiScreen.height - 5 - height, GuiScreen.width, GuiScreen.height - 5, color.getRGB());
        drawRect(GuiScreen.width - offset, GuiScreen.height - 6 - height, GuiScreen.width , GuiScreen.height - 33, novoline(1));
        drawRect(GuiScreen.width - offset, GuiScreen.height - -23 - height, GuiScreen.width, GuiScreen.height - 4, novoline(1));
        drawRect(GuiScreen.width - offset, GuiScreen.height - 5 - height, GuiScreen.width - offset + 1, GuiScreen.height - 5, novoline(1));
        //drawRect(GuiScreen.width - offset, GuiScreen.height - 5 - height, GuiScreen.width - offset + 4, GuiScreen.height - 5, -1);

        fontRenderer.drawString(title, (int) (GuiScreen.width - offset + 8), GuiScreen.height - 2 - height, -1);
        fontRenderer.drawString(messsage, (int) (GuiScreen.width - offset + 20), GuiScreen.height - 22, -1);
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(mode, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static int novoline(int delay) {
	      double novolineState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      novolineState %= 360;
	      return Color.getHSBColor((float) (novolineState / 180.0f), 0.5f, 1.0f).getRGB();
	}
    
    public static int rainbow(int delay) {
	      double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	      rainbowState %= 360;
	      return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}
}