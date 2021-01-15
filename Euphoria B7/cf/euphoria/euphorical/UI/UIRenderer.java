// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.UI;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;

import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Events.EventRender2D;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

public class UIRenderer extends GuiIngame
{
    private static float hue;
    
    static {
        UIRenderer.hue = 0.0f;
    }
    
    public UIRenderer(final Minecraft mcIn) {
        super(mcIn);
    }
    
    @Override
    public void func_175180_a(final float var1337) {
        super.func_175180_a(var1337);
        this.renderEuphoriaUI();
    }
    
    private void renderEuphoriaUI() {
        GL11.glPushMatrix();
        final Color color = Color.getHSBColor(UIRenderer.hue / 255.0f, 0.9f, 0.9f);
        final int c = color.getRGB();
        int yCount = 2;
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(Euphoria.getName(), 2.0f, 2.0f, c);
        final String timeStamp = new SimpleDateFormat("h:mm:ss a").format(Calendar.getInstance().getTime());
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Time: " + timeStamp, 2.0f, 12.0f, 10066329);
        final Minecraft minecraft = Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        final int displayWidth = Minecraft.displayWidth;
        Minecraft.getMinecraft();
        final ScaledResolution res = new ScaledResolution(minecraft, displayWidth, Minecraft.displayHeight);
        final EventRender2D event = new EventRender2D();
        EventManager.call(event);
        final ArrayList<Mod> sortex = new ArrayList<Mod>();
        for (final Mod mod : Euphoria.getEuphoria().theMods.getMods()) {
            sortex.add(mod);
        }
        Collections.sort(sortex, new Comparator<Mod>() {
            @Override
            public int compare(final Mod mod1, final Mod mod2) {
                String s1 = String.valueOf(mod1.getRenderName().replace("[", "- "));
                String s2 = String.valueOf(mod2.getRenderName().replace("[", "- "));
                s1 = s1.replace("]", "");
                s2 = s2.replace("]", "");
                final int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
                return (cmp != 0) ? cmp : s2.compareTo(s1);
            }
        });
        for (final Mod mod : sortex) {
            if (mod.isEnabled()) {
            	String name = String.valueOf(mod.getRenderName()).replace("[", "§7- ");
            	name = name.replace("]", "");
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(name, res.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name.replace("§7", "")) - 2, yCount, RenderUtils.transparency(c, 1.0f));
                yCount += 10;
            }
        }
        int yBottom = res.getScaledHeight() - 15;
        final int[] pos = { Minecraft.getMinecraft().thePlayer.getPosition().getX(), Minecraft.getMinecraft().thePlayer.getPosition().getY(), Minecraft.getMinecraft().thePlayer.getPosition().getZ() };
        final String[] currPos = { "X: ", "Y: ", "Z: " };
        for (int i = 0; i < 3; ++i) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(String.valueOf(currPos[i]) + pos[i], res.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(currPos[i]) + pos[i]) - 2, yBottom - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, c);
            yBottom -= 10;
        }
        TabGUI.render(c);
        Euphoria.getEuphoria().getGuiMgr().update();
        Euphoria.getEuphoria().getKeybindGuiMgr().update();
        UIRenderer.hue += 0.25;
        if (UIRenderer.hue > 255.0f) {
            UIRenderer.hue = 0.0f;
        }
        GL11.glPopMatrix();
    }
}
