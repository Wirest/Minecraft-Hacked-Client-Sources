package nivia.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.awt.*;

public class Wrapper {
	
	public static Minecraft getMinecraft() {
		return Minecraft.getMinecraft();
	}
	
	public static EntityPlayerSP getPlayer() {
		return getMinecraft().thePlayer;
	}
	
	public static World getWorld() {
		return getMinecraft().theWorld;
	}
	
	public static void addChat(String msg) {
		getPlayer().addChatComponentMessage(new ChatComponentText(msg));
	}
	
	public static FontRenderer getFontRenderer() {
		return getMinecraft().fontRendererObj;
	}
	
	public static ScaledResolution getScaledRes() {
		return new ScaledResolution(getMinecraft(), getMinecraft().displayWidth, getMinecraft().displayHeight);
	}

    public static void drawCenteredString(String s, int x, int y, int colour) {
        x -= Wrapper.getMinecraft().fontRendererObj.getStringWidth(s) / 2;
        Wrapper.getMinecraft().fontRendererObj.drawStringWithShadow(s, x, y, colour);
    }
	public static void drawRect(double x1, double y1, double x2, double y2, int color) {
		Gui.drawRect(x1, y1, x2, y2, color);
	}
    public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
        drawRect(x1, y1, x2, y2, outline);
        drawRect(x1+1, y1+1, x2-1, y2-1, inline);
        drawRect(x1+2, y1+2, x2-2, y2-2, background);
    }
    public static void drawRectWH(double x, double y, double width, double height, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }
    public static void drawBorderedRect(double x, double y, double x2, double y2, double thickness, int inside, int outline) {
        double fix = 0.0;
        if (thickness < 1.0) {
            fix = 1.0;
        }
        drawRect(x + thickness, y + thickness, x2 - thickness, y2 - thickness, inside);
        drawRect(x, y + 1.0 - fix, x + thickness, y2, outline);
        drawRect(x, y, x2 - 1.0 + fix, y + thickness, outline);
        drawRect(x2 - thickness, y, x2, y2 - 1.0 + fix, outline);
        drawRect(x + 1.0 - fix, y2 - thickness, x2, y2, outline);
    }
    public static  void drawRainbowRect(double x, double y, double x2, double y2) {
        double width = (x2 - x);
        for (int i = 0; i <= (width); i++) {
            Color color = new Color(Color.HSBtoRGB((float)((Wrapper.getPlayer().ticksExisted / width)/2 + (Math.sin(i / width * 0.6))) % 1.0f, 0.5f, 1.0f));
            drawRect(x+i, y, x+i+1,y2,color.getRGB());
        }
    }
    public static int getIntFromColor(int r, int g, int b){
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
    public static int getIntFromColor(Color color){
        return color.getRGB();
    }
    public static Color getColorFromHex(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);

        return new Color(r, g, b);
    }

}
