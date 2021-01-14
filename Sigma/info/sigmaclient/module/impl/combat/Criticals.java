package info.sigmaclient.module.impl.combat;

import tv.twitch.chat.Chat;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.misc.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2APacketParticles;

public class Criticals extends Module {

    public static String PACKET = "MODE";
    static String HURTTIME = "HURTTIME";
    public static String MODE = "";
    Timer lastStep = new Timer();
    Timer timer = new Timer();
    int groundTicks, stage, count;
    double y;
    //0.0625101D
    
    public Criticals(ModuleData data) {
        super(data);
        settings.put(PACKET, new Setting<>(PACKET, new Options("Mode", "Packet", new String[]{"Packet", "HPacket", "Minis", "HMinis", "Hover", "Jump"}), "Critical attack method."));
        settings.put(HURTTIME, new Setting<>(HURTTIME, 15, "The hurtTime tick to crit at.", 1, 0, 20));
    }
    @Override
    public void onEnable() {
    	stage = 0;
    	String mm = ((Options) settings.get(PACKET).getValue()).getSelected();
    	count = 0;
    	if(mm.equalsIgnoreCase("Packet") || mm.equalsIgnoreCase("Minis")){
   		 if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")){
   			 Notifications.getManager().post("Warning", mm + " criticals doesn't bypass on Hypixel.", Notifications.Type.WARNING);
   		 }
   	}
    }
    @Override
    @RegisterEvent(events = {EventPacket.class, EventUpdate.class, EventJump.class, EventAttack.class, EventStep.class})
    public void onEvent(Event event) {
        if(event instanceof EventStep){
        	EventStep es = (EventStep)event;
        
        	if(!es.isPre()){
        		lastStep.reset();
        		if(!mc.thePlayer.isCollidedHorizontally){
        			y = mc.thePlayer.boundingBox.minY;
        			stage = 0;
        		}
        	}
        }
        if (event instanceof EventUpdate) {
        	EventUpdate em = (EventUpdate)event;
            setSuffix(((Options) settings.get(PACKET).getValue()).getSelected() + " " + ((Number) settings.get(HURTTIME).getValue()).intValue());
            if(MoveUtils.isOnGround(0.001)){
            	groundTicks ++;
            }else if(!mc.thePlayer.onGround){
            	groundTicks = 0;
            }
           
            if(!MODE.equalsIgnoreCase(((Options) settings.get(PACKET).getValue()).getSelected())){          	
            	MODE = ((Options) settings.get(PACKET).getValue()).getSelected();
            	if(MODE.equalsIgnoreCase("Packet") || MODE.equalsIgnoreCase("Minis")){
            		 if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")){
            			 Notifications.getManager().post("Warning", MODE + " criticals doesn't bypass on Hypixel.", Notifications.Type.WARNING);
            		 }
            	}
        	}	
            if(!MODE.equalsIgnoreCase("Packet") && !MODE.equalsIgnoreCase("HPacket")){
            	setSuffix(((Options) settings.get(PACKET).getValue()).getSelected());
            }
            if(em.isPre() && MODE.equalsIgnoreCase("Hover")){
        		mc.thePlayer.lastReportedPosY = 0;
    			double ypos = mc.thePlayer.posY;
        		if(MoveUtils.isOnGround(0.001)){
        			em.setOnGround(false);
        			if(stage == 0){
        				y = ypos + 1E-8;
        				em.setOnGround(true);
        			}else if(stage == 1)
        				y-= 5E-15;
        			else
        				y-= 4E-15;
        			
        			if(y <= mc.thePlayer.posY){
        				stage = 0;
        				y = mc.thePlayer.posY;
        				em.setOnGround(true);
        			}
        			em.setY(y);
        			stage ++;
        		}else
        			stage = 0;
        		
            }
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet p = ep.getPacket();
         
            if (p instanceof S2APacketParticles ) {
            
                return;
            }
            if(p instanceof S08PacketPlayerPosLook){
		    	stage = 0;
            }
            if(p instanceof C0FPacketConfirmTransaction){
            	C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction)p;
            	boolean accepted = packet.isAccepted();
            	int uid = packet.getId();
            	if(accepted && uid == 0){
        			count ++;
        			if(Killaura.target != null){
        			//	ep.setCancelled(true);
        			}
            	}		
            }
            if(p instanceof C03PacketPlayer && ep.isPre()){
            	C03PacketPlayer pa = (C03PacketPlayer)p;
            	double y = pa.y;
            	if(timer.getTime() == 0)
            	if(MODE.equalsIgnoreCase("HPacket")){
                	//ep.setPacket(new C03PacketPlayer(true));
            	}
            	
            }
        }
        if(event instanceof EventJump){
        	EventJump ej = (EventJump)event;
        	if(ej.isPre()){
        		 if (Killaura.isSetupTick() && Client.getModuleManager().isEnabled(Killaura.class)) {
        	        	if(MODE.equalsIgnoreCase("minis") || MODE.equalsIgnoreCase("HMinis")){
        	        		//if minis criticals is not on ground, send packet onground to be able to jump
        	        		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(
        	        				mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        	        	}
        		 }
        	}
        }
        if(event instanceof EventAttack){
        	EventAttack ea = (EventAttack)event;
        	if(ea.isPreAttack()){
        	      if( ea.getEntity() != null){        	        
        	        	if(mc.thePlayer.onGround && MoveUtils.isOnGround(0.001) && !(Client.getModuleManager().isEnabled(LongJump.class) || Client.getModuleManager().isEnabled(Bhop.class))){
        	        		 if(MODE.equalsIgnoreCase("Packet") || MODE.equalsIgnoreCase("HPacket")){
        	                     if( ea.getEntity().hurtResistantTime <= ((Number) settings.get(HURTTIME).getValue()).doubleValue() && lastStep.check(20)
        	                    		 && (timer.check(200) || ea.getEntity().hurtResistantTime > 0) && mc.thePlayer.isCollidedVertically){
        	                    	 if(groundTicks > 1){
        	                    		 doCrits(); 
        	                    	 }
        	                    	
        	                     }
        	        		 }else if (!mc.thePlayer.isJumping && ((Options) settings.get(PACKET).getValue()).getSelected().equalsIgnoreCase("Jump")){
        	        			 mc.thePlayer.jump();
        	        		 }
        	        		 timer.reset();
        	        		 
        	        	}
        	        }  
        	}
        }
    }

    private boolean hurtTimeCheck(Entity entity) {
        return entity != null && entity.hurtResistantTime <= ((Number) settings.get(HURTTIME).getValue()).intValue();
    }

    public static void doCrits() {
    	//0.0625 , 17.64e-8 
    	double off = 0.0626;
		double x = mc.thePlayer.posX; double y = mc.thePlayer.posY; double z = mc.thePlayer.posZ;
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y+off, z, false));
		if(MODE.equalsIgnoreCase("HPacket")){
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y+off+0.00000000001, z, false));
		}
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
    	
    	
    } 

}
