package info.sigmaclient.module.impl.premium;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.movement.Phase;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.util.MathHelper;

public class LongjumpMineplexAddon extends Module {
    private float air, groundTicks;
    private LongJump longjumpModule;
    private Timer timer = new Timer();
    double motionY;
    int count;
    boolean collided, half;
    public LongjumpMineplexAddon() {
        super(new ModuleData(ModuleData.Type.Other, "", ""));

        ModuleManager m = Client.getModuleManager();
        longjumpModule = (LongJump) m.get(LongJump.class);
        longjumpModule.setMineplexAddon(this);
    }

    @Override
    public void onEvent(Event event) {

        switch (((Options) longjumpModule.getSettings().get("MODE").getValue()).getSelected()) {
            case "Mineplex":
            	if (!mc.thePlayer.onGround && !BlockUtils.isOnGround(0.01) && air > 0) {
                air++;
                if(mc.thePlayer.isCollidedVertically){
                	air = 0;
                }
                if(mc.thePlayer.isCollidedHorizontally && !collided){
                	collided = !collided;
                }
                double speed = half?0.5- air / 100 : 0.658 - air / 100;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                motionY -= 0.04000000000001;
                if(air > 24){
                	motionY -= 0.02;
                }
                if(air == 12){
                	motionY = -0.005;
                }
                if(speed < 0.3)
                	speed = 0.3;
                if(collided)
                	speed = 0.2873;
                mc.thePlayer.motionY = motionY;
                MoveUtils.setMotion(speed);
            } else {
                if (air > 0) {
                    air = 0;
                }
            }
     
            if (mc.thePlayer.onGround && MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
            	Module longjump = Client.getModuleManager().get(LongJump.class);
                double groundspeed = 0;
                collided = mc.thePlayer.isCollidedHorizontally;
                groundTicks ++;
                if((Boolean) longjump.getSetting(LongJump.OFF).getValue() && groundTicks > 1){
                	longjump.toggle();
                	return;
                }
                mc.thePlayer.motionX *= groundspeed;
                mc.thePlayer.motionZ *= groundspeed;
                
                half = mc.thePlayer.posY != (int)mc.thePlayer.posY;
                mc.thePlayer.motionY = 0.4299999;
                air = 1;
                motionY = mc.thePlayer.motionY;
            }
                break;
            case "Cubecraft":          
            	mc.timer.timerSpeed = MoveUtils.isOnGround(0.001) ? 0.75f : 0.6f;
            	if(PlayerUtil.isMoving2()){
            		count ++;
            		if(count == 1){
                    	MoveUtils.setMotion(1.9);
                    }else if(count == 2){
                    	MoveUtils.setMotion(0);
                    	if(!MoveUtils.isOnGround(0.001)){
                    		count = 0;
                    	}else{
                    		mc.timer.timerSpeed = 1;
                    	}
                    }else if(count >= 3){
                    	MoveUtils.setMotion(0);
                    	count = 0;
                    }
            	}else{
            		count = 0;
            		MoveUtils.setMotion(0);
            	}	
            	
                break;
        }
    }

    @Override
    public void onEnable() {
    	motionY = mc.thePlayer.motionY;
    	air = 0;
    	count = 0;
    	half = mc.thePlayer.posY != (int)mc.thePlayer.posY;
    	collided = mc.thePlayer.isCollidedHorizontally;
        groundTicks = 0;
        Module longjump = Client.getModuleManager().get(LongJump.class);
        if(((Options) longjump.getSetting(LongJump.MODE).getValue()).getSelected().equalsIgnoreCase("CubeCraft")){
        	if(mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.001)){
        		mc.timer.timerSpeed = 0.75f;
        	}
        }
    }
    @Override
    public void onDisable(){
    	mc.timer.timerSpeed = 1f;
    	MoveUtils.setMotion(0.2);
    }
    @Override
    public boolean shouldRegister() {
        return false;
    }
}
