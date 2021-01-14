package com.mentalfrostbyte.jello.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventMotion;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.types.EventType2;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.AimUtil;
import com.mentalfrostbyte.jello.util.BooleanValue;
import com.mentalfrostbyte.jello.util.NumberValue;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.MathHelper;

public class KillAura extends Module {

	public static boolean players = true;
	public static boolean monsters = false;
	public static boolean animals = false;
	public static boolean neutrals = false;
	public static boolean invisibles = false;
	public Random rand = new Random();
	private List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	public NumberValue range;

    public BooleanValue autoblock;
    public BooleanValue pl;
    public BooleanValue gm;
    public BooleanValue an;
    public BooleanValue mo;
    public BooleanValue kb;
    public BooleanValue team;
    public static boolean enabled;
	public static boolean attacking = false;
	public static EntityLivingBase closestE;
	public static boolean blocking;
	public static float yaw;
	public static float pitch;
	
	public static float progressYaw;
	public static float progressPitch;
	
	public TimerUtil timer = new TimerUtil();
	
	public KillAura() {
        super("Kill Aura", Keyboard.KEY_X);
        this.jelloCat = Jello.tabgui.cats.get(2);
        range = new NumberValue("Reach", 3.6, 1.0, 7.0, 0.1);
        addValue(range);
        autoblock = new BooleanValue("BlockHit", false);
        addValue(autoblock);
        kb = new BooleanValue("Extra Knockback", false);
        addValue(kb);
        pl = new BooleanValue("Attack Players", true);
        addValue(pl);
        an = new BooleanValue("Attack Mobs", false);
        addValue(an);
        mo = new BooleanValue("Attack Passives", false);
        addValue(mo);
        team = new BooleanValue("Team", false);
        addValue(team);

    }
	double normalise( double value,  double start,  double end ) 
	{
	   double width       = end - start   ;   // 
	   double offsetValue = value - start ;   // value relative to 0

	  return ( offsetValue - ( Math.floor( offsetValue / width ) * width ) ) + start ;
	}
	public void onEnable(){
		EventManager.register(this);
		enabled = true;
		progressYaw = mc.thePlayer.rotationYaw;
		yaw = mc.thePlayer.rotationYaw;
		progressYaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
		yaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
		progressPitch = mc.thePlayer.rotationPitch;
		pitch = mc.thePlayer.rotationPitch;
	}
	
	public void onDisable(){
		EventManager.unregister(this);
		enabled = false;
	}
	
    @Override
    public void onUpdate() {
       
        if(!this.isToggled())
            return;
        
        if(mc.thePlayer.ticksExisted < 20){
        	this.toggle();
        }

        
       for(Object entity : mc.theWorld.loadedEntityList) {
    	   
        	Entity ent = (Entity) entity;
                if(ent instanceof EntityLivingBase){
                	EntityLivingBase e = (EntityLivingBase)ent;
                	if(isValidHypixel(e)){
                		if(this.isOnTeam(e)){
                		this.setClosestTarget();
                		if(isValidHypixel(closestE)){
                		
                if(timer.hasTimeElapsed(100, true)) {
                	if(gm.getValue() ? true:closestE.getHealth() > 0.0f) {
                		if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && kb.getValue()){
            				mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                	}
                		Jello.core.attack(closestE);
                		 if (autoblock.getValue() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && !mc.thePlayer.isBlocking()) {
                			    //mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 1);
                	           if(mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0){
                	            //Jello.core.player().setSpeed(0.25f);
                	           }
                	        }
                		if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && kb.getValue()){
            				mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                	}
                    }
                }
                		}
                }
            }
                }
                if(isValidHypixel(closestE)){
                	if(this.isOnTeam(closestE)){
                	attacking = true;
                	if(this.autoblock.getValue()){
                		blocking = true;
                	}
                	int speed = 3;
                	if(progressYaw > yaw){
                		progressYaw += (((yaw)-progressYaw)/(speed))+0.1;
            		}else if(progressYaw < yaw){
            			progressYaw += (((yaw)-progressYaw)/(speed))+0.1;
            		}
            		
            		if(progressPitch > pitch){
            			progressPitch += (((pitch)-progressPitch)/(speed))+0.1;
            		}else if(progressPitch < pitch){
            			progressPitch += (((pitch)-progressPitch)/(speed))+0.1;
            		}
                	}
                }else{
                	attacking = false;
                		blocking = false;
                	
                }
            
        }
       
        super.onUpdate();
    }
    @EventTarget
	public void autoBlock(EventMotion event)
	  {
		   boolean isSword = autoblock.getValue() && mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
		   if (event.getType() == EventType2.PRE) {
			  
	        	EntityLivingBase entity;
	            if (this.entities.isEmpty()) {
	                for (Object object : Jello.core.world().loadedEntityList) {
	                    if (!(object instanceof EntityLivingBase) || !isValidHypixel(entity = (EntityLivingBase)object)) continue;
	                    if(this.isOnTeam(entity)){
	                    this.entities.add(entity);
	                    }
	                }
	            }
	            if (!this.entities.isEmpty()) {
	                double distance = Double.MAX_VALUE;
	                entity = null;
	                int i = 0;
	                while (i < this.entities.size()) {
	                    EntityLivingBase e = this.entities.get(i);
	                    if (!isValidHypixel(e)) {
	                        this.entities.remove(e);
	                    }
	                    if ((double)e.getDistanceToEntity(Jello.core.player()) < distance && (double)e.getDistanceToEntity(Jello.core.player()) < range.getValue()) {
	                        entity = e;
	                        distance = e.getDistanceToEntity(Jello.core.player());
	                    }
	                    ++i;
	                }
	                this.closestE = entity;
	            }
	        }
	        else if (isValidHypixel(closestE)) {
	        	if(this.isOnTeam(closestE)){
	            if (isSword && mc.thePlayer.getDistanceToEntity(closestE) < range.getValue()) {
	                PlayerControllerMP playerController = mc.playerController;
	                EntityPlayerSP p = mc.thePlayer;
	                }
	        	
	        	if (this.closestE != null && timer.hasTimeElapsed(100, true)) {
	        		attacking = true;
	        		if(this.autoblock.getValue()){
                		blocking = true;
                	}
               		if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && kb.getValue()){
           				mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
               	}
               		Jello.core.attack(this.closestE);
               		
               		if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 && kb.getValue()){
           				mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
               		}
	                this.entities.remove(this.closestE);
	                this.closestE = null;
	                this.timer.reset();
	            }
	        }
	        }
		
	    }
    public void setClosestTarget() {
    	for(Object entity : mc.theWorld.loadedEntityList) {
        	Entity ent = (Entity) entity;
                if(ent instanceof EntityLivingBase){
                	EntityLivingBase e = (EntityLivingBase)ent;
                
				if(isValidHypixel((EntityLivingBase)e) && e.getDistanceToEntity(mc.thePlayer) <= this.range.getValue() && !e.isDead && ((EntityLivingBase) e).getHealth() != 0 && (this.invisibles ? true:!e.isInvisible())){
					if(this.isOnTeam(e)){
					if(closestE == null){
						closestE = (EntityLivingBase)e;
					}
					if(e.getDistanceToEntity(mc.thePlayer) < closestE.getDistanceToEntity(mc.thePlayer)){
						closestE = (EntityLivingBase)e;
					}
					}
				}
			}
    	}
		}
    @EventTarget
    public void onPacket(EventPacketSent event){
    	if(attacking){
    		
    		
       	 double 	posX 		= closestE.posX - mc.thePlayer.posX,
   	  				posZ 		= closestE.posZ - mc.thePlayer.posZ,
   	  				posY 		= (closestE.posY - 3.5 + Jello.core.findClosestEntity().getEyeHeight() - mc.thePlayer.posY + (mc.thePlayer.getEyeHeight())),
   	  				helper 		= MathHelper.sqrt_double((posX * posX) + (posZ * posZ));
   	  		float 	newYaw 		= (float) Math.toDegrees(-Math.atan(posX / posZ)),
   	  				newPitch 	= (float) -Math.toDegrees(Math.atan(posY / helper));
   	  		
   	  		if ((posZ < 0) && (posX < 0)) {
   	  			newYaw = (float) (90 + Math.toDegrees(Math.atan(posZ / posX)));
   	  		} else if ((posZ < 0) && (posX > 0)) {
   	  			newYaw = (float) (-90 + Math.toDegrees(Math.atan(posZ / posX)));
   	  		}
   	  	if(event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C05PacketPlayerLook || event.getPacket() instanceof C06PacketPlayerPosLook) {
   	  		event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, progressYaw, progressPitch, mc.thePlayer.onGround));
   	 
   	  		yaw = newYaw;
   	  		pitch = newPitch;
   	  		
   	  	}
   	  		}else{
   	  			progressYaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
   	  			yaw = (float)this.normalise(mc.thePlayer.rotationYaw, -180, 180);
   	  			progressPitch = mc.thePlayer.rotationPitch;
   				pitch = mc.thePlayer.rotationPitch;
   	  		}
    }
    
    public boolean isOnTeam(EntityLivingBase entity){
    	String namewithcolorcode = Jello.core.player().getDisplayName().getUnformattedText();
    	String colorcode = namewithcolorcode.replace(Jello.core.player().getName(), "");
    	String enamewithcolorcode = entity.getDisplayName().getUnformattedText();//.getUnformattedText();
    	String ecolorcode = enamewithcolorcode.replace(entity.getName(), "");
        return (team.getValue() ? !(colorcode != "" && colorcode.equalsIgnoreCase(ecolorcode)):true)/*&& (!FriendManager.isFriend(entity.getName())) && (Team.isEnabled ? !(colorcode != "" && colorcode.equalsIgnoreCase(ecolorcode)):true*/;
   
    }
    
    public boolean isValidHypixel(EntityLivingBase entity) {
    	 return entity != null &&  
    			 entity != Jello.core.player() && 
    			 ((entity instanceof EntityOtherPlayerMP && pl.getValue()) || 
    					 (entity instanceof EntityAnimal && an.getValue()) || 
    					 entity instanceof EntityMob && mo.getValue()) && 
    			 entity.getDistanceToEntity(mc.thePlayer) <= range.getValue() && 
    			 (!entity.isInvisible()) && /*(entity.ticksExisted > 10) &&*/ 
    			 (double)entity.getDistanceToEntity(Jello.core.player()) <= (!Jello.core.player().canEntityBeSeen(entity) ? 3.0 : 
    				 this.range.getValue());// && (team.getValue() ? !(colorcode != "" && colorcode.equalsIgnoreCase(ecolorcode)):true)/*&& (!FriendManager.isFriend(entity.getName())) && (Team.isEnabled ? !(colorcode != "" && colorcode.equalsIgnoreCase(ecolorcode)):true*/;
    }
    
    public boolean isEntityInFov(EntityLivingBase entity, double angle) {
        double angleDifference = this.getAngleDifference(Jello.core.player().rotationYaw, AimUtil.getRotations(entity)[0]);
        if (!(angleDifference > 0.0 && angleDifference < angle || - (angle *= 0.5) < angleDifference && angleDifference < 0.0)) {
            return false;
        }
        return true;
    }
    
    public static float getAngleDifference(float direction, float rotationYaw) {
        float phi = Math.abs(rotationYaw - direction) % 360.0f;
        float distance = phi > 180.0f ? 360.0f - phi : phi;
        return distance;
    }
	
}
