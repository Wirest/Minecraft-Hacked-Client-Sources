// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render.hud;

import java.util.Iterator;
import me.aristhena.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.aristhena.event.events.Render2DEvent;
import me.aristhena.client.module.Module;

public class IndigoTheme extends Theme
{
    private static final int MIN_HEX = -23614;
    private static final int MAX_HEX = -3394561;
    private static final int MAX_FADE = 20;
    private static int currentColor;
    private static int fadeState;
    private static boolean goingUp;
    
    public IndigoTheme(final String name, final boolean value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public boolean onRender(final Render2DEvent event) {
        if (super.onRender(event)) {
            String time = new SimpleDateFormat("hh:mm a").format(new Date());
            if (time.startsWith("0")) {
                time = time.replaceFirst("0", "");
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.7f, 1.7f, 1.7f);
            ClientUtils.clientFont().drawStringWithShadow("I", 1.0, 1.0, 7785430);
            GlStateManager.popMatrix();
            ClientUtils.clientFont().drawStringWithShadow("ndigo", 9.0, 7.0, 7785430);
            ClientUtils.clientFont().drawStringWithShadow(time, 2.0, 17.0, -10063499);
            ClientUtils.clientFont().drawStringWithShadow("FPS: " + Minecraft.debugFPS, 2.0, 28.0, -10063499);
            int y = 2;
            for (final Module mod : ModuleManager.getModulesForRender()) {
                if (mod.drawDisplayName(event.getWidth() - ClientUtils.clientFont().getStringWidth(String.format("%s" + ((mod.getSuffix().length() > 0) ? " §0[%s]" : ""), mod.getDisplayName(), mod.getSuffix())) - 2, y, IndigoTheme.currentColor)) {
                    y += 10;
                }
            }
        }
        return super.onRender(event);
    }
    
    public static void updateFade() {
        if (IndigoTheme.fadeState >= 20 || IndigoTheme.fadeState <= 0) {
            IndigoTheme.goingUp = !IndigoTheme.goingUp;
        }
        if (IndigoTheme.goingUp) {
            ++IndigoTheme.fadeState;
        }
        else {
            --IndigoTheme.fadeState;
        }
        final double ratio = IndigoTheme.fadeState / 20.0;
        IndigoTheme.currentColor = getFadeHex(-23614, -3394561, ratio);
    }
    
    private static int getFadeHex(final int hex1, final int hex2, final double ratio) {
        int r = hex1 >> 16;
        int g = hex1 >> 8 & 0xFF;
        int b = hex1 & 0xFF;
        r += (int)(((hex2 >> 16) - r) * ratio);
        g += (int)(((hex2 >> 8 & 0xFF) - g) * ratio);
        b += (int)(((hex2 & 0xFF) - b) * ratio);
        return r << 16 | g << 8 | b;
    }
}
