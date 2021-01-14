package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.utils.TimeHelper;
import store.shadowclient.client.utils.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class LongJump extends Module{
	public static Minecraft mc = Minecraft.getMinecraft();
	private static float air;
    private static float groundTicks;
    private static int stage;
    private boolean teleported;
    private TimeHelper timer = new TimeHelper();
    private boolean jump;
    
	public LongJump() {
		super("LongJump", Keyboard.KEY_C, Category.MOVEMENT);
		ArrayList<String> options = new ArrayList<>();
		//HYPIXEL
        options.add("Hypixel");
        
        //SPARTAN
        options.add("Spartan");
        
        //MINEPLEX
        options.add("Mineplex");
        
        //AAC
        options.add("AAC");
        Shadow.instance.settingsManager.rSetting(new Setting("LongJump Mode", this, "Hypixel", options));

        Shadow.instance.settingsManager.rSetting(new Setting("Jump Glide", this, false));
        Shadow.instance.settingsManager.rSetting(new Setting("Jump Disable", this, false));
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(this.isToggled()){
			String mode = Shadow.instance.settingsManager.getSettingByName("LongJump Mode").getValString();

	        if(mode.equalsIgnoreCase("Hypixel")) {
	        	this.setDisplayName("LongJump §7" + "Hypixel");
				float x2 = 0.7f + getSpeedEffect() * 0.45f;
	            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
	                if(Shadow.instance.settingsManager.getSettingByName("Jump Disable").getValBoolean() && groundTicks > 0){
	                    groundTicks = 0;
	                    this.toggle();
	                    return;
	                }
	                groundTicks++;
	                setMotion(0.15);
	                mc.thePlayer.jump();
	                stage = 1;
	            }
	            if (isOnGround(0.001)) {
	                air = 0;
	            } else {
	                if(mc.thePlayer.isCollidedVertically)
	                    stage = 0;
	                if(stage > 0 && stage <= 3 && Shadow.instance.settingsManager.getSettingByName("Jump Glide").getValBoolean()){
	                    mc.thePlayer.onGround = true;
	                }
	                double speed = (0.95 + getSpeedEffect() * 0.2) - air / 25;
	                if(Shadow.instance.settingsManager.getSettingByName("Jump Glide").getValBoolean()){
	                    speed = (1.1 + getSpeedEffect() * 0.2f) - air / 20;
	                }
	                if(speed < defaultSpeed()-0.05){ // + (0.025*MoveUtils.getSpeedEffect())
	                    speed = defaultSpeed()-0.05;
	                }
	                if(stage < 4 && Shadow.instance.settingsManager.getSettingByName("Jump Glide").getValBoolean())
	                    speed = defaultSpeed();
	                setMotion(speed);
	                if(Shadow.instance.settingsManager.getSettingByName("Jump Glide").getValBoolean()){
	                    mc.thePlayer.motionY = getMotion(stage);
	                }else{
	                    mc.thePlayer.motionY = getOldMotion(stage);
	                }
	                if(stage > 0){
	                    stage ++;
	                }
	                air += x2;
	            }
	        }
	        
	        if(mode.equalsIgnoreCase("Spartan")) {
	        	this.setDisplayName("LongJump §7" + "Spartan");
	        	if((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) && mc.gameSettings.keyBindJump.isKeyDown()) {
	                float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0) + ((mc.thePlayer.moveStrafing > 0) ? (-90F * ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);
	                float xDir = (float)Math.cos((dir + 90F) * Math.PI / 180);
	                float zDir = (float)Math.sin((dir + 90F) * Math.PI / 180);
	                if(mc.thePlayer.isCollidedVertically && (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) && mc.gameSettings.keyBindJump.isKeyDown()) {
	                    mc.thePlayer.motionX = xDir * .29F;
	                    mc.thePlayer.motionZ = zDir * .29F;
	                }
	                if(mc.thePlayer.motionY == .33319999363422365 && (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown())) {
	                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	                        mc.thePlayer.motionX = xDir * 1.34;
	                        mc.thePlayer.motionZ = zDir * 1.34;
	                    } else {
	                        mc.thePlayer.motionX = xDir * 1.261;
	                        mc.thePlayer.motionZ = zDir * 1.261;
	                    }
	                }
	            }
	        }
	        
	        if(mode.equalsIgnoreCase("AAC")) {
	        	this.setDisplayName("LongJump §7" + "AAC");
	        	this.mc.gameSettings.keyBindForward.pressed = false;
	            if(this.mc.thePlayer.onGround) {
	               this.jump = true;
	            }

	            if(this.mc.thePlayer.onGround && this.timer.isDelayComplete(500L)) {
	               this.mc.thePlayer.motionY = 0.42D;
	               PlayerUtils.toFwd(2.3D);
	               this.timer.reset();
	            } else if(!this.mc.thePlayer.onGround && this.jump) {
	               this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
	               this.jump = false;
	            }
	        }
	        
	        if(mode.equalsIgnoreCase("Mineplex")) {
	        	this.setDisplayName("LongJump §7" + "Mineplex");
	    		if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()
	    				|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown())
	    				&& mc.gameSettings.keyBindJump.isKeyDown()) {
	    			
	    			float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0)
	    					+ ((mc.thePlayer.moveStrafing > 0) ? (-90F
	    							* ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F)))
	    							: 0);
	    			float xDir = (float) Math.cos((dir + 90F) * Math.PI / 180);
	    			float zDir = (float) Math.sin((dir + 90F) * Math.PI / 180);
	    			if (mc.thePlayer.isCollidedVertically
	    					&& (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()
	    							|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown())
	    					&& mc.gameSettings.keyBindJump.isKeyDown()) {
	    				mc.thePlayer.motionX = xDir * .29F;
	    				mc.thePlayer.motionZ = zDir * .29F;
	    			}
	    			if (mc.thePlayer.motionY == .33319999363422365
	    					&& (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()
	    							|| mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown())) {
	    				if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	    					mc.thePlayer.motionX = xDir * 1.34;
	    					mc.thePlayer.motionZ = zDir * 1.34;
	    				} else {
	    					mc.thePlayer.motionX = xDir * 1.261;
	    					mc.thePlayer.motionZ = zDir * 1.261;
	    				}
	    			}
	    		}
	        }
	    }
	}

	public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed))
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        else
            return 0;
    }
    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public static double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
          //  if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel")){
           //   baseSpeed *= (1.0D + 0.225D * (amplifier + 1));
           // }else
                baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }
    public static void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
    
    double getMotion(int stage){
        boolean isMoving = (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0);
        double[] mot = {0.396,-0.122,-0.1,0.423, 0.35,0.28,0.217,0.15, 0.025,-0.00625,-0.038,-0.0693,-0.102,-0.13,
                -0.018,-0.1,-0.117,-0.14532,-0.1334, -0.1581, -0.183141, -0.170695, -0.195653, -0.221, -0.209, -0.233, -0.25767,
                -0.314917, -0.371019, -0.426};
        stage --;
        if(stage >= 0 && stage < mot.length){
            double motion = mot[stage];
            return motion;
        }else{
            return mc.thePlayer.motionY;
        }
    }
    
    double getOldMotion(int stage){
        boolean isMoving = (mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0);
        double[] mot = {0.345,0.2699,0.183,0.103,0.024,-0.008,-0.04,-0.072,-0.104,-0.13,-0.019,-0.097};
        double[] notMoving = {0.345,0.2699,0.183,0.103,0.024,-0.008,-0.04,-0.072,-0.14,-0.17,-0.019,-0.13};
        stage --;
        if(stage >= 0 && stage < mot.length){
            if((mc.thePlayer.moveStrafing == 0 && mc.thePlayer.moveForward == 0) || mc.thePlayer.isCollidedHorizontally){
                return notMoving[stage];
            }
            return mot[stage];
        }else{
            return mc.thePlayer.motionY;
        }
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }

    @Override
    public void onDisable() {
    	super.onDisable();
    }
}