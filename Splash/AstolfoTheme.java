package splash.client.themes.astolfo;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sun.jna.platform.win32.WinDef.UINT_PTR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import splash.Splash;
import splash.api.module.Module;
import splash.api.theme.Theme;
import splash.api.theme.type.FontType;
import splash.client.modules.visual.UI;
import splash.client.themes.virtue.tab.VirtueTabGUI;
import splash.utilities.animation.AnimationUtility;
import splash.utilities.visual.ColorUtilities;

public class AstolfoTheme extends Theme {

    int y = 0;

    public AstolfoTheme() {
        super("Astolfo");
    }

    @Override
    public void drawWatermark() {

    }


    @Override
    public void drawTabGUI() {

    }

    @Override
    public void onKey(int keyCode) {
        new VirtueTabGUI().onKey(keyCode);
    }

    @Override
    public void drawArraylist() {
        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
        int yStart = 1;
        
        int nigga = Splash.getInstance().getFontRenderer().getFontHeightCustom() + 3;
        float speed = 3000f;
        float hue = (float) (System.currentTimeMillis() % (int)speed) + (yStart);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        int color = Color.HSBtoRGB(hue, 0.5F, 1F);
        UI ui = ((UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class));
        
        ui.color = color;
        List<Module> mods = Splash.getInstance().getModuleManager().getModulesForRender();
        mods.sort((o1, o2) -> (int) (Splash.getInstance().getFontRenderer().getStringWidthCustom(o2.getFullModuleDisplayName()) - Splash.getInstance().getFontRenderer().getStringWidthCustom(o1.getFullModuleDisplayName())));
       
        for (Module module : mods) { 
        	int startX = (int) (s.getScaledWidth() - Splash.getInstance().getFontRenderer().getStringWidthCustom(module.getFullModuleDisplayName())) + 2;
        	
        	double topY = yStart - 1;
        	double leftX = startX + module.getSlideAnimation() - 6;
            AnimationUtility.animate(module);
            Gui.drawRect(startX + module.getSlideAnimation() - 6, yStart - 1, s.getScaledWidth(), yStart + Splash.getInstance().getFontRenderer().getFontHeightCustom() + 3, new Color(0,0,0, ((UI)Splash.getInstance().getModuleManager().getModuleByClass(UI.class)).backgroundDarkness.getValue()).getRGB());
            if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.CUSTOM) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation() - 4), yStart + 1, color);
            } else if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.MINEMAN) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation() - 3), yStart + 2, color);
            }
               
            y += 12;
            yStart += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 4;
        }
    }
}
