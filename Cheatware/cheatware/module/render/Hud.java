package cheatware.module.render;

import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.Event2D;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.module.ModuleManager;
import cheatware.utils.RainbowUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class Hud extends Module {

    public Hud() {
        super("Hud", Keyboard.KEY_NONE, Category.RENDER);
    }
    
    @Override
    public void setup() {
    	
    }
    
	public static class ModuleComparator implements Comparator<Module> {
		public int compare(Module arg0, Module arg1) {
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.fontRendererObj.getStringWidth(arg0.getDisplayName()) < mc.fontRendererObj.getStringWidth(arg1.getDisplayName())) {
				return -1;
			}
			if(mc.fontRendererObj.getStringWidth(arg0.getDisplayName()) < mc.fontRendererObj.getStringWidth(arg1.getDisplayName())) {
				return 1;
			}
			return 0;
		}
	}
	

    @EventTarget
    public void onUpdate(Event2D event) {
    	if(mc.gameSettings.showDebugInfo) {
			return;
		}
		int count = 0;
		ScaledResolution sr = new ScaledResolution(mc);
		FontRenderer fr = mc.fontRendererObj;
			mc.fontRendererObj.drawStringWithShadow("Cheatware", 5, 5, RainbowUtil.rainbow());
			Collections.sort(Cheatware.instance.moduleManager.getModules(), new ModuleComparator().reversed());
			for(Module m : Cheatware.instance.moduleManager.getModules()) {
				if(!m.isToggled())
					continue;
					mc.fontRendererObj.drawStringWithShadow(m.getDisplayName(), sr.getScaledWidth() - fr.getStringWidth(m.getDisplayName()) - 5, 1 + count* (fr.FONT_HEIGHT), RainbowUtil.rainbow());
					
					count++;
		}
    }
}
