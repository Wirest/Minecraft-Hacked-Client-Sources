package splash.client.themes.virtue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import splash.Splash;
import splash.api.module.Module;
import splash.api.theme.Theme;
import splash.api.theme.type.FontType;
import splash.client.modules.visual.UI;
import splash.client.themes.virtue.tab.VirtueTabGUI;
import splash.utilities.animation.AnimationUtility;
import splash.utilities.visual.ColorUtilities;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Ice
 * Created: 15:18, 31-May-20
 * Project: Client
 */
public class VirtueTheme extends Theme {


    public VirtueTheme() {
        super("Gay");
    }

    @Override
    public void drawWatermark() {
        Splash.getInstance().getFontRenderer().drawStringWithShadow("virtue " + EnumChatFormatting.GRAY + getTime(), 4, 2, -1);
    }

    public String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    @Override
    public void drawTabGUI() {
        new VirtueTabGUI().drawTabGui();
    }

    @Override
    public void onKey(int keyCode) {
        new VirtueTabGUI().onKey(keyCode);
    }

    @Override
    public void drawArraylist() {
        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
        int yStart = 2;
        UI ui = ((UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class));
        
        ui.color = Splash.getInstance().getClientColor();
        List<Module> mods = Splash.getInstance().getModuleManager().getModulesForRender();
        mods.sort((o1, o2) -> (int) (Splash.getInstance().getFontRenderer().getStringWidthCustom(o2.getFullModuleDisplayName()) - Splash.getInstance().getFontRenderer().getStringWidthCustom(o1.getFullModuleDisplayName())));
        for (Module module : mods) {
            int startX = (int) (s.getScaledWidth() - Splash.getInstance().getFontRenderer().getStringWidthCustom(module.getFullModuleDisplayName()) - 5) + 2;
            AnimationUtility.animate(module);
            if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.CUSTOM) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation()), yStart, module.getColor());
            } else if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.MINEMAN) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation()), yStart + 2, module.getColor());   
            }
            yStart += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 4;
        }
    }
}