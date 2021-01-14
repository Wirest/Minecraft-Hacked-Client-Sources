package store.shadowclient.client.module.combat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot", Keyboard.KEY_M, Category.COMBAT);
        
        ArrayList<String> options = new ArrayList<>();
        options.add("Watchdog");
        
        Shadow.instance.settingsManager.rSetting(new Setting("AntiBot Mode", this, "Watchdog", options));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
    	
    	if(mode.equalsIgnoreCase("Watchdog")) {
    		this.setDisplayName("AntiBot §7| Watchdog");
    		List playerEntities = mc.theWorld.playerEntities;
    		int i = 0;
    		for(int playerEntitiesSize = playerEntities.size(); i < playerEntitiesSize; ++i) {
	            EntityPlayer player = (EntityPlayer)playerEntities.get(i);
	            if (player.getName().startsWith("§") && player.getName().contains("§c") || this.isEntityBot(player) && !player.getDisplayName().getFormattedText().contains("NPC")) {
	            	mc.theWorld.removeEntity(player);
	            }
    		}
    	}
    }
    
    private boolean isOnTab(Entity entity) {
        Iterator var2 = mc.getNetHandler().getPlayerInfoMap().iterator();

        NetworkPlayerInfo info;
        do {
           if (!var2.hasNext()) {
              return false;
           }

           info = (NetworkPlayerInfo)var2.next();
        } while(!info.getGameProfile().getName().equals(entity.getName()));

        return true;
     }
    
    private boolean isEntityBot(Entity entity) {
        double distance = entity.getDistanceSqToEntity(mc.thePlayer);
        if (!(entity instanceof EntityPlayer)) {
           return false;
        } else if (mc.getCurrentServerData() == null) {
           return false;
        } else {
           return mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && entity.getDisplayName().getFormattedText().startsWith("&") || !this.isOnTab(entity) && mc.thePlayer.ticksExisted > 100;
        }
     }
    
    /*for(Object entity : mc.theWorld.loadedEntityList)
    if(((Entity)entity).isInvisible() && entity != mc.thePlayer)
        mc.theWorld.removeEntity((Entity)entity);*/
}
