package cheatware.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventPreMotionUpdate;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.utils.BlockUtil;
import cheatware.utils.MoveUtils;
import de.Hero.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

public class Speed extends Module {
    private float speed;
    private Setting speedvalue;
    private boolean doSlow;
    double moveSpeed;
    int hops;
    double dist;
    double lastDist;

	public Speed() {
        super("Speed", Keyboard.KEY_U, Category.MOVEMENT);
    }

    @Override
    public void setup() {
	    ArrayList<String> options = new ArrayList<>();
	    ArrayList<String> options2 = new ArrayList<>();
	    options.add("Mineplex");
	    options.add("BunnyHop");
	    options.add("Viper");
	    
	    options2.add("OnGround");
	    options2.add("Bhop");
	    Cheatware.instance.settingsManager.rSetting(speedvalue = new Setting("Speed", this, 1, 1, 2, false));
	    Cheatware.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Mineplex", options));
	    Cheatware.instance.settingsManager.rSetting(new Setting("Mineplex Mode", this, "OnGround", options2));
	    Cheatware.instance.settingsManager.rSetting(new Setting("Speed", this, 1, 0, 1, false));
    }
    
    @Override
    public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1;
		this.speed = 0;
		this.moveSpeed = 0.024;
		this.lastDist = 0.0;
		this.moveSpeed = 0.0;
		this.doSlow = true;
		this.hops = 1;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
        final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
        this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
    	String mpmode = Cheatware.instance.settingsManager.getSettingByName("Mineplex Mode").getValString();
    	String mode = Cheatware.instance.settingsManager.getSettingByName("Speed Mode").getValString();
    	if(mode.equalsIgnoreCase("Mineplex")) {
	    	this.setDisplayName("Speed §7Mineplex");
	    	
    		if(mpmode.equalsIgnoreCase("Bhop")) {
    			 final Entity player = this.mc.thePlayer;
    	            final BlockPos pos = new BlockPos(player.posX, player.posY - 1.0, player.posZ);
    	            final Block block = Minecraft.theWorld.getBlockState(pos).getBlock();
    	            this.mc.timer.timerSpeed = 1.0f;
    	            if (this.mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
    	                this.mc.timer.timerSpeed = 1.0f;
    	                event.setY(this.mc.thePlayer.motionY = 0.4000000059604645);
    	                this.doSlow = true;
    	                this.dist = this.moveSpeed;
    	                this.moveSpeed = 0.0;
    	            }
    	            else {
    	                this.mc.timer.timerSpeed = 1.0f;
    	                if (this.doSlow) {
    	                    this.moveSpeed = this.dist + 0.5600000023841858;
    	                    this.doSlow = false;
    	                }
    	                else {
    	                    this.moveSpeed = this.lastDist * ((this.moveSpeed > 2.2) ? 0.975 : ((this.moveSpeed >= 1.5) ? 0.98 : 0.985));
    	                }
    	            }
    	            final double max = this.speedvalue.getValDouble();
    	            MoveUtils.setMotion(Math.max(Math.min(this.moveSpeed, max), this.doSlow ? 0.0 : 0.42));
    		} else if(mpmode.equalsIgnoreCase("OnGround")) {
    			if (Fly.airSlot() == -10){
		            return;
		        }
		        if (mc.thePlayer.isMoving()) {
		            mc.getNetHandler().getNetworkManager().sendPacket((new C09PacketHeldItemChange(Fly.airSlot())));
		            BlockUtil.placeHeldItemUnderPlayer();
		            speed = 0.45F;
		        }
		        else {
		            mc.getNetHandler().getNetworkManager().sendPacket((new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem)));
		            speed = 0.45F;
		        }
		       
		        MoveUtils.setMotion(speed);
    		}
    	} else if(mode.equalsIgnoreCase("BunnyHop")) {
	    	this.setDisplayName("Speed §7BunnyHop");
    		MoveUtils.setMotion(Cheatware.instance.settingsManager.getSettingByName(this, "Speed").getValDouble() / 3);
    		if(mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		}
    	} else if(mode.equalsIgnoreCase("Viper")) {
	    	this.setDisplayName("Speed §7Viper");
    		mc.thePlayer.setSpeed(0.4F);
    		if(mc.thePlayer.onGround) {
    			mc.thePlayer.jump();
    		} else {
    			mc.thePlayer.jumpMovementFactor = 0.035F;
    		}
    	}
    }
}
