package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventRenderEntity;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

public class Chams extends Module {
    public Setting colored;
    public Chams(){
        super("Chams", Keyboard.KEY_NONE, Category.RENDER);
        Execution.instance.settingsManager.rSetting(colored = new Setting("Colored Chams", this, true));
    }

    @EventTarget
    public void onRenderEntity(EventRenderEntity event){
        if(colored.getValBoolean())
            return;
        if(event.getState() == Event.State.PRE){
           // GlStateManager.color(mc.thePlayer.canEntityBeSeen(event.getEntity()) ? 0 : 255, mc.thePlayer.canEntityBeSeen(event.getEntity()) ? 255 : 0, 0, 255);
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0F, -1100000.0F);
        }else {
            GlStateManager.doPolygonOffset(1.0F, 1100000.0F);
            GlStateManager.disablePolygonOffset();
           // GlStateManager.resetColor();
        }
    }
}
