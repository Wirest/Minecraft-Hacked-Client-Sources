package VenusClient.online.Module.impl.Combat;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

public class Antibot extends Module {
    public Antibot() {
        super("Antibot", "Antibot", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
    		toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
    		return;
    	}
        if (event.getType() == EventMotionUpdate.Type.PRE) {
            for (Object entity : mc.theWorld.loadedEntityList) {
                if (((Entity) entity).isInvisible() && entity != mc.thePlayer) {
                    mc.theWorld.removeEntity((Entity) entity);
                }
            }
        }
    }
    
    private boolean isOnTab(Entity entity) {
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (info.getGameProfile().getName().equals(entity.getName()))
                return true;
        }
        return false;
    }


}
