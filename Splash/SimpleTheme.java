package splash.client.themes.simple;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import splash.Splash;
import splash.api.module.Module;
import splash.api.theme.Theme;
import splash.api.theme.type.FontType;
import splash.client.modules.visual.UI;
import splash.utilities.animation.AnimationUtility;
import splash.utilities.visual.ColorUtilities;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lwjgl.opengl.GL11;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Ice
 * Created: 15:18, 31-May-20
 * Project: Client
 */
public class SimpleTheme extends Theme {

    public static int y = 0;

    public SimpleTheme() {
        super("Simple");
    }

    @Override
    public void drawWatermark() {
    }

    public String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    @Override
    public void drawTabGUI() {
    }

    @Override
    public void onKey(int keyCode) {

    }

    @Override
    public void drawArraylist() {
        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
        int yStart = 2;

        List<Module> mods = Splash.getInstance().getModuleManager().getModulesForRender();
        mods.sort((o1, o2) -> (int) (Splash.getInstance().getFontRenderer().getStringWidthCustom(o2.getFullModuleDisplayName()) - Splash.getInstance().getFontRenderer().getStringWidthCustom(o1.getFullModuleDisplayName())));

        for (Module module : mods) {
            AnimationUtility.animate(module);
            if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.CUSTOM) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (2), yStart + 1, module.getColor());
            } else if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.MINEMAN) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (2), yStart + 2, module.getColor());
            }

            y += 12;
            yStart += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 4;
        }
    }
}
