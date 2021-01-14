package cedo.modules.render;

import cedo.Wrapper;
import cedo.events.Event;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class FullBright extends Module {
    public NumberSetting brightness = new NumberSetting("Brightness", 10, 1, 10, 1);

    public FullBright() {
        super("FullBright", Keyboard.KEY_O, Category.RENDER);
        addSettings(brightness);
    }

    public void onEvent(Event e) {
        if (Wrapper.getFuckedPrinceKin == 1438E-4) {
            GlStateManager.pushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.rotate(10, 10, 10, 10);
        }
        mc.gameSettings.gammaSetting = (float) brightness.getValue();
    }

    public void onDisable() {
        mc.gameSettings.gammaSetting = 1;
        super.onDisable();
    }
}
