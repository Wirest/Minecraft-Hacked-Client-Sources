package me.xatzdevelopments.xatz.utils;

import net.minecraft.client.renderer.culling.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;

public class RenderUtil
{
    private static final Frustum frustum;
    
    static {
        frustum = new Frustum();
    }
    

    
    public static int withTransparency(final int rgb, final float alpha) {
        final float r = (rgb >> 16 & 0xFF) / 255.0f;
        final float g = (rgb >> 8 & 0xFF) / 255.0f;
        final float b = (rgb >> 0 & 0xFF) / 255.0f;
        return new Color(r, g, b, alpha).getRGB();
    }
    
    public static int getHexRGB(final int hex) {
        return 0xFF000000 | hex;
    }
    
    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }
    
    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }

}
