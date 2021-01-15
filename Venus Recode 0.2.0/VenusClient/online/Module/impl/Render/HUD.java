package VenusClient.online.Module.impl.Render;

import VenusClient.online.Client;
import VenusClient.online.Auth.VenusAuthAPI;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.Event2D;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Module.ModuleManager;
import VenusClient.online.Ui.font.GlyphPageFontRenderer;
import VenusClient.online.Utils.MathUtils;

import com.mojang.realmsclient.gui.ChatFormatting;
import de.Hero.settings.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HUD extends Module {
    private GlyphPageFontRenderer font = mc.clientFont;

    public HUD() {
        super("HUD", "HUD", Keyboard.KEY_NONE, Category.RENDER);
        toggle();
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("VenusClient");
        options.add("Genesis");
        Client.instance.setmgr.rSetting(new Setting("HUD Style", this, "VenusClient", options));
        Client.instance.setmgr.rSetting(new Setting("HUD Show Suffix (Bug)", this, false));
    }

    @EventTarget
    public void onRender2D(Event2D event){
    	
    	//ScaledResolution scaledResolution = new ScaledResolution(mc);
    	    	
        double bruh = (MathUtils.square(mc.thePlayer.motionX) + MathUtils.square(mc.thePlayer.motionZ) + MathUtils.square(mc.thePlayer.motionY));
        double bps = MathUtils.round((Math.sqrt(bruh) * 20) * mc.timer.timerSpeed, 2);
        
        String bpsText = "BPS: \247f";

    	
        String hudStyleSelected = Client.instance.setmgr.getSettingByName("HUD Style").getValString();

        boolean showSuffix = Client.instance.setmgr.getSettingByName("HUD Show Suffix (Bug)").getValBoolean();

        if (showSuffix) {
            ModuleManager.modules.sort(Comparator.comparingInt(m -> mc.clientFont.getStringWidth(((Module) m).getDisplayName())).reversed());
        }else {
            ModuleManager.modules.sort(Comparator.comparingInt(m -> mc.clientFont.getStringWidth(((Module) m).getName())).reversed());
        }

        if (hudStyleSelected.equalsIgnoreCase("VenusClient")){

        	
            try {
                mc.clientFont.drawString(Client.client_Build + " - §7" + VenusAuthAPI.getUserName(), event.getWidth() - mc.clientFont.getStringWidth(Client.client_Build + " - §7" + VenusAuthAPI.getUserName()) - 10, event.getHeight() - mc.clientFont.getFontHeight() - 2, Color.WHITE.getRGB(), true);
                mc.clientFont.drawString(Client.client_Name, 1, 3, astofloc(1000), false);
                mc.clientFont.drawString(bpsText + bps, 1, 20, -1, false);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //Array
            ScaledResolution sr = new ScaledResolution(mc);

            int y = 0;
            for(Module m : ModuleManager.getModules()) {
                if (!m.isEnabled())
                    continue;

                	Gui.drawRect(sr.getScaledWidth() - mc.clientFont.getStringWidth(m.name) - 5, y, sr.getScaledWidth(), mc.clientFont.getFontHeight() + y, 0x70000000);
                    mc.clientFont.drawString(m.getName(), sr.getScaledWidth() - mc.clientFont.getStringWidth(m.getName()) - 4, y - 1, astofloc((y * 25)), true);
                

                y += mc.clientFont.getFontHeight();
            }
        }else if (hudStyleSelected.equalsIgnoreCase("Genesis")){
            mc.watermarkFont.drawString("Genesis", 2, 4, Color.CYAN.getRGB(), true);
        }
    }

    public static int astofloc(int delay) {
	    float speed = 3200.0F;
	    float hue = (float)(System.currentTimeMillis() % (int)speed) + (delay / 2);
	    while (hue > speed)
	      hue -= speed; 
	    hue /= speed;
	    if (hue > 0.5D)
	      hue = 0.5F - hue - 0.5F; 
	    hue += 0.5F;
	    return Color.HSBtoRGB(hue, 0.5F, 1.0F);
    }
}


