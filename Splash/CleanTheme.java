package splash.client.themes.clean;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import splash.Splash;
import splash.api.module.Module;
import splash.api.theme.Theme;
import splash.api.theme.type.FontType;
import splash.client.managers.cfont.CFontRenderer;
import splash.client.modules.visual.UI;
import splash.utilities.animation.AnimationUtility;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Ice
 * Created: 15:18, 31-May-20
 * Project: Client
 */
public class CleanTheme extends Theme {
	int y = 0;
	
    public CleanTheme() {
        super("Clean");
    }

    @Override
    public void drawWatermark() {
    	ScaledResolution scaledRes = new ScaledResolution(mc);
    	RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/splashPNGLogo.png"),( scaledRes.getScaledWidth() / 2000 - 5), -12, 960,540);

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
        //splashPNGlogo
        UI ui = ((UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class));
        
        ui.color = Splash.getInstance().getClientColor();
        List<Module> mods = Splash.getInstance().getModuleManager().getModulesForRender();
        mods.sort((o1, o2) -> (int) (Splash.getInstance().getFontRenderer().getStringWidthCustom(o2.getFullModuleDisplayName()) - Splash.getInstance().getFontRenderer().getStringWidthCustom(o1.getFullModuleDisplayName())));
        for (Module module : mods) {
            int startX = (int) (s.getScaledWidth() - Splash.getInstance().getFontRenderer().getStringWidthCustom(module.getFullModuleDisplayName()) - 5) + 2;
            AnimationUtility.animate(module);
            if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.CUSTOM) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation()), yStart, Splash.getInstance().getClientColor());
            } else if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font").getValue() == FontType.MINEMAN) {
                Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(), (float) (startX + module.getSlideAnimation()), yStart + 2, Splash.getInstance().getClientColor());            }

            yStart += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 4;
            
//            Gui.drawRect(s.getScaledWidth() / 500, s.getScaledHeight(), 0, 0, Splash.INSTANCE.getClientColor());
//            
//            Gui.drawRect(s.getScaledWidth(), s.getScaledHeight(), s.getScaledWidth() / 1.001, 0, Splash.INSTANCE.getClientColor());
//
//            Gui.drawRect(s.getScaledWidth(), s.getScaledHeight() / 500, 0, 0, Splash.INSTANCE.getClientColor());
//            
//            Gui.drawRect(s.getScaledWidth(), s.getScaledHeight(), 0, s.getScaledHeight() / 1.003, Splash.INSTANCE.getClientColor());

        }
    }
    
}
