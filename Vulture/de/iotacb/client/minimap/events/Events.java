package de.iotacb.client.minimap.events;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import de.iotacb.client.Client;
import de.iotacb.client.events.render.RenderEvent;
import de.iotacb.client.events.states.RenderState;
import de.iotacb.client.minimap.animation.Animation;
import de.iotacb.client.minimap.interfaces.InterfaceHandler;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.render.Render2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Events
{
    
    public Events() {
        EventManager.register(this);
    }
    
    private final ResourceLocation SHADOW = new ResourceLocation("client/textures/shadow_minimap.png");
    
    @EventTarget
    public void drawPre(RenderEvent event) {
    	if (event.getState() == RenderState.TWOD) {
    		HUD hud = (HUD) Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class);
    		if (!hud.isEnabled() || !hud.getValueByName("HUDMinimap").getBooleanValue()) return;
			Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			Client.RENDER2D.push();
			Client.RENDER2D.translate(5, hud.getValueByName("HUDLogo").getComboValue().equalsIgnoreCase("Simple") ? 10 : 73);
			Client.RENDER2D.image(SHADOW, 1.5, 1.5, 127.5, 127.5);
			InterfaceHandler.drawInterfaces(Minecraft.getMinecraft().timer.renderPartialTicks);
			Animation.tick();
			Client.RENDER2D.pop();
    	}
    }
}
