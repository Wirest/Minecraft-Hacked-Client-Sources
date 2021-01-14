package store.shadowclient.client.module.render;

import org.lwjgl.opengl.GL11;

import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.RenderEvent;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

public class Chams2 extends Module {
    public Chams2() {
		super("Chams2", 0, Category.RENDER);
	}

    public void onEnable() {
        EventManager.register(this);
    }
    
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    private boolean isValid(final Entity ent) {
        return ent instanceof EntityPlayer && ent != this.mc.thePlayer && !ent.isInvisible();
    }
    
    @EventTarget
    public void beforeRenderPlayer(final RenderEvent event) {
            this.offsetPolygon();
        }
    
    @EventTarget
    public void afterRenderPlayer(final RenderEvent event) {
            this.resetPolygon();
        }
    
    private void offsetPolygon() {
        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0f, -1.0E7f);
    }
    
    private void resetPolygon() {
        GL11.glPolygonOffset(1.0f, 1000000.0f);
        GL11.glDisable(32823);
    }
}
