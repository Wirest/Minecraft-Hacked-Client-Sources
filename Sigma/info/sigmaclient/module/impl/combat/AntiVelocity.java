package info.sigmaclient.module.impl.combat;

import tv.twitch.chat.Chat;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.MathHelper;
/*
 * Made by LeakedPvP
 */
public class AntiVelocity extends Module {
    private String VELOCITY = "MODE";
    public AntiVelocity(ModuleData data) {
        super(data);
        settings.put(VELOCITY, new Setting<>(VELOCITY, new Options("Mode", "Basic", new String[]{"Basic", "AACPush", "AACFlag"}), "AntiVelocity method."));
        settings.put(HORIZONTAL, new Setting<>(HORIZONTAL, 0, "Horizontal velocity factor.", 10, 0, 100));
        settings.put(VERTICAL, new Setting<>(VERTICAL, 0, "Vertical velocity factor.", 10, 0, 100));
    }
    double count = 0;
    public static String HORIZONTAL = "HORIZONTAL";
    public static String VERTICAL = "VERTICAL";
    double xmot, zmot, ymot = 0;
    @Override
    public void onEnable() {
        super.onEnable();
        count = 0;
    }
    @Override
    @RegisterEvent(events = {EventPacket.class,EventUpdate.class})
    public void onEvent(Event event) {
    	String currentMode = ((Options) settings.get(VELOCITY).getValue()).getSelected();
        setSuffix(currentMode);
    	if(event instanceof EventPacket){
        EventPacket ep = (EventPacket) event;
        Packet p = ep.getPacket();
        if (ep.isOutgoing()) {
            if (p instanceof C03PacketPlayer)
                return;
        }    
        if(p instanceof S08PacketPlayerPosLook){
        	S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
        	if(currentMode.equalsIgnoreCase("AACFlag") && mc.thePlayer.hurtResistantTime > 0){
        		pac.yaw = mc.thePlayer.rotationYaw;
            	pac.pitch = mc.thePlayer.rotationPitch;
        	}
           
        }
        if (p instanceof S12PacketEntityVelocity) {
        	
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
        	int entId = packet.getEntityID();
			int x = packet.getX();
			int y = packet.getY();
			int z = packet.getZ();
            if (entId == mc.thePlayer.getEntityId() && !(Client.getModuleManager().isEnabled(Fly.class) && ((Options)Client.getModuleManager().get(Fly.class).getSetting("MODE").getValue()).getSelected().equalsIgnoreCase("BowFly"))) {
            	switch(currentMode){
            	case"AACPush":
            		if(x != 0 && z != 0){
                		count = 10;
                		xmot = x/1000;
                		zmot = z/1000;	
            		}
            		break;
            	case"Basic":
                    int vertical = ((Number) settings.get(VERTICAL).getValue()).intValue();
                    int horizontal = ((Number) settings.get(HORIZONTAL).getValue()).intValue();
                    if (vertical != 0 || horizontal != 0) {
                        packet.setMotX(horizontal * packet.getX() / 100);
                        packet.setMotY(vertical * packet.getY() / 100);
                        packet.setMotZ(horizontal * packet.getZ() / 100);
                    } else {
                        ep.setCancelled(true);
                    }
            		break;
            	case"AACFlag":
            		if(x != 0 && z != 0){
            			C04PacketPlayerPosition pack = new C04PacketPlayerPosition(mc.thePlayer.posX, Double.NaN, mc.thePlayer.posZ, true);
        	   			mc.thePlayer.sendQueue.addToSendQueue(pack);
        	   			ep.setCancelled(true);
            		}
            		break;
            	}
            } 
        }
        if (p instanceof S27PacketExplosion && currentMode.equalsIgnoreCase("basic")) {
        	if(ep.isPre()){
                ep.setCancelled(true);
        	}
        
        }
    	}else if(event instanceof EventUpdate && mc.thePlayer.hurtResistantTime > 0 && currentMode.equalsIgnoreCase("aacpush")){
               
    		if(count > 0){
    			double boost = 0.05 - count/200;
    			count --;
    			if(count <=10){
    				double speedcheck = Math.sqrt(xmot * xmot + zmot * zmot);
    				
    				MoveUtils.strafe(boost);
    				double speed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    				if(speed > 0.35){
    					count = 0;
    					mc.thePlayer.motionX *= 0;
    					mc.thePlayer.motionZ *= 0;
    					MoveUtils.strafe(0.2);
    				}
    				if(mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0){
    					double value = 0.8-boost;
    					mc.thePlayer.motionX *= value;
    					mc.thePlayer.motionZ *= value;
    				}			
    			}
    		}
    	}
    }
}
