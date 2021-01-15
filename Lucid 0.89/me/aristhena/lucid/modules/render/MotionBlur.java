package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.management.module.*;
import me.aristhena.lucid.eventapi.events.*;
import net.minecraft.client.renderer.*;
import me.aristhena.lucid.eventapi.*;

@Mod
public class MotionBlur extends Module
{
    @EventTarget
    private void onTick(final TickEvent event) {
        final EntityRenderer er = this.mc.entityRenderer;
        this.mc.entityRenderer.useShader = true;
        if (this.mc.theWorld != null && (this.mc.entityRenderer.theShaderGroup == null || !this.mc.entityRenderer.theShaderGroup.getShaderGroupName().contains("phosphor"))) {
            if (er.theShaderGroup != null) {
                er.theShaderGroup.deleteShaderGroup();
            }
            er.loadShader(EntityRenderer.shaderResourceLocations[12]);
        }
    }
    
    @Override
    public void onDisable() {
        this.mc.entityRenderer.useShader = true;
        if (this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
        }
        super.onDisable();
    }
}
