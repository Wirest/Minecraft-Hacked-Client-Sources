package store.shadowclient.client.module.render;

import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.renderer.EntityRenderer;

public class MotionBlur extends Module
{
    public MotionBlur() {
        super("MotionBlur", 0, Category.RENDER);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        this.mc.entityRenderer.useShader = true;
        if (this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
        }
    }
    
    @Override
    public void onEnable() {
        final EntityRenderer er = this.mc.entityRenderer;
        er.activateNextShader();
        EventManager.register(this);
    }
    
    @EventTarget
    public void onTick(final ClientTickEvent event) {
        final EntityRenderer er = this.mc.entityRenderer;
        this.mc.entityRenderer.useShader = true;
        if (this.mc.theWorld != null && (this.mc.entityRenderer.theShaderGroup == null || !this.mc.entityRenderer.theShaderGroup.getShaderGroupName().contains("phosphor"))) {
            if (er.theShaderGroup != null) {
                er.theShaderGroup.deleteShaderGroup();
            }
            er.activateNextShader();
        }
    }
}
