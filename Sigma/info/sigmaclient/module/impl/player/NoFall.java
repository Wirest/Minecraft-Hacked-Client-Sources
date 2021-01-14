/**
 * Made by LeakedPvP
 */
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

import java.util.ArrayList;

public class NoFall extends Module {

    public static String MODE = "MODE";
    double fall;
    public NoFall(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("NoFall Mode", "Vanilla", new String[]{"Vanilla", "Packet", "Hypixel", "Damage", "AAC"}), "NoFall method."));
    }

    @Override
    public void onEnable(){
    	if(mc.theWorld != null && ((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("AAC")){
    		C04PacketPlayerPosition p = new C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, true);
   			mc.thePlayer.sendQueue.addToSendQueue(p);
    	}
    	fall = 0;
    }
    @Override
    @RegisterEvent(events = EventUpdate.class)
    public void onEvent(Event event) {
        String currentNofall = ((Options) settings.get(MODE).getValue()).getSelected();
        this.setSuffix(currentNofall);
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if(em.isPre()){
            	switch(currentNofall){
            	case"Vanilla":
            		if(!MoveUtils.isOnGround(0.001) && mc.thePlayer.motionY < 0){
            			em.setOnGround(true);
            		}
            		break;
            		
            	case"Packet":
               	 	if(!MoveUtils.isOnGround(0.0001) && mc.thePlayer.motionY < 0){
               	 		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));  
               	 	}
            		break;
            	case"Hypixel":
            	
            		if(!MoveUtils.isOnGround(0.001)){
            			if(mc.thePlayer.motionY < -0.08)
            				fall -= mc.thePlayer.motionY;
            			if(fall > 2){
            				fall = 0;
            			
                			em.setOnGround(true);
                		}
            		}else
            			fall = 0;
            		break;
            	case"Cubecraft":
            		if(!MoveUtils.isOnGround(0.001)){
            			if(mc.thePlayer.fallDistance > 2.69){
                			em.setOnGround(true);
                			mc.thePlayer.fallDistance = 0;
                		}
                		if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).isEmpty()) {  
                			if(!em.isOnground() && mc.thePlayer.motionY < -0.6){
                				em.setOnGround(true);
                			}		 
                		}
            		}
            		break;
            	case"Damage":
            		if(mc.thePlayer.fallDistance > 3.5){
            			em.setOnGround(true);
            		}
            		break;
            	case"AAC":
            		if(mc.thePlayer.ticksExisted == 1){
               			C04PacketPlayerPosition p = new C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, true);
               			mc.thePlayer.sendQueue.addToSendQueue(p);
            		}
            		break;
            	}
            }
        }
    }
}
