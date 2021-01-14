package info.sigmaclient.module.impl.movement;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.player.NoFall;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class Fly extends Module {

	public final static String SPEED = "SPEED";
	public final static String CUBE = "CUBESPEED";
	public final static String MODE = "MODE";
	public final static String HYPIXELF = "HYPIXEL";
    public final static String HYPIXELMODE = "HYPIXELMODE";
    public final static String BOBBING = "BOBBING";
    Timer kickTimer = new Timer();
    private double flyHeight;
    private double startY;
    double count;
    Timer timer = new Timer();
    Timer bowfly = new Timer();
    public static double hypixel = 0;
    private float flytimer = 0;
    public static int fastFlew;
    int stage;
    boolean shouldSpeed;
    public Fly(ModuleData data) {
        super(data);
        settings.put(SPEED, new Setting<>(SPEED, 2.0f, "Movement speed.", 0.25, 0.25, 5));
        settings.put(HYPIXELF, new Setting<>(HYPIXELF, 4.8, "Hypixel fast fly speed .", 0.1, 1, 6.5));
		settings.put(BOBBING, new Setting<>(BOBBING, false, "View bobbing while flying."));
        settings.put(HYPIXELMODE, new Setting<>(HYPIXELMODE, new Options("Hypixel Mode", "Basic", new String[]{"Basic", "OldFast", "Fast1", "Fast2"}), "Hypixel Fly method."));
     
        settings.put(MODE, new Setting<>(MODE, new Options("Fly Mode", "Motion", new String[]{"Vanilla", "Guardian", "AntiKick", "Glide", "Motion", "Hypixel", "BowFly", "Cube1", "Cube2", "Cube3"}), "Fly method."));
    }

    @Override
    public void onEnable() {
        count = 0;
        if (mc.thePlayer == null)
            return;
        if(((Options)settings.get("MODE").getValue()).getSelected().equalsIgnoreCase("Hypixel") &&
        		((Options)settings.get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Fast2")){
        	fastFlew = 0;
        }else{
        	fastFlew = 100;
        }
        mc.timer.timerSpeed = 1f;
        if(((Options)settings.get(MODE).getValue()).getSelected().contains("Cube")){
          	shouldSpeed = false;
          	if(!MoveUtils.isOnGround(0.001)){
          		mc.timer.timerSpeed = 0.3f; 
          	}
          
          	stage = 0;
        }else{
        	  stage = 1;
        }
        startY = mc.thePlayer.posY;
        hypixel = 0;
        if (premiumAddon != null) {
            premiumAddon.onEnable();
        }
        //mc.thePlayer.motionY = -0.1;
      
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("BowFly")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (premiumAddon != null) {
            premiumAddon.onDisable();
        }
        count = 0;
        fastFlew = 100;
        MoveUtils.setMotion(0.2);
        mc.thePlayer.jumpMovementFactor = 0;
        mc.thePlayer.capabilities.isFlying = false;
        mc.timer.timerSpeed = 1f;
        mc.thePlayer.capabilities.allowFlying = false;
        if(((Options) settings.get(MODE).getValue()).getSelected().contains("Cube")){
    		mc.timer.timerSpeed = 1.0F;
        	mc.thePlayer.onGround = false;
        	mc.thePlayer.jumpMovementFactor = 0;
        	mc.thePlayer.motionY = -0.4;
        	MoveUtils.setMotion(0.2);

        }
    }

    @Override
    @RegisterEvent(events = {EventMove.class, EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        String currentmode = ((Options) settings.get(MODE).getValue()).getSelected();
        if (premiumAddon == null && currentmode.equalsIgnoreCase("guardian")) {
            if (timer.delay(400)) {
                Notifications.getManager().post("Premium Bypass", "Guardian fly mode is a premium only bypass", Notifications.Type.WARNING);
                timer.reset();
            }
            toggle();
            return;
        }
        if (premiumAddon == null && !((Options)settings.get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Basic")) {
            if (timer.delay(400)) {
                Notifications.getManager().post("Premium Bypass", "Hypixel fast fly mode is a premium only bypass", Notifications.Type.WARNING);
                timer.reset();
            }
            toggle();
            return;
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet p = ep.getPacket();
            if (p instanceof S08PacketPlayerPosLook) {
            	S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) p;
                hypixel = 0;
            }

            if(!currentmode.equalsIgnoreCase("BowFly")){
            	if (p instanceof S12PacketEntityVelocity) {
                	S12PacketEntityVelocity packet = (S12PacketEntityVelocity) p;
                    if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    	ep.setCancelled(true);                    
                    }
                } 
                if (p instanceof S27PacketExplosion) {
                	ep.setCancelled(true);
                }
            }
           

          
            if(p instanceof C03PacketPlayer){
            	C03PacketPlayer pack = (C03PacketPlayer)p;
            	if(currentmode.equalsIgnoreCase("Hypixel")){
            		pack.onGround = MoveUtils.isOnGround(0.001);
            		if(!PlayerUtil.isMoving2()){
            			if(pack.y == 0 && pack.x == 0){
                    		if(pack.yaw == 0 && pack.pitch == 0){
                    			//ep.setCancelled(true);
                    		}else if(Killaura.target == null){
                    			//ep.setCancelled(true);
                    		}
                    	}
            		}
            	}
            
            }
            if (ep.isOutgoing() && currentmode.equalsIgnoreCase("Guardian") && !Client.getModuleManager().isEnabled(NoFall.class) && premiumAddon != null) {
                premiumAddon.onEvent(event);
            }
        }
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            double speed = Math.max(((Number) settings.get(SPEED).getValue()).floatValue(), getBaseMoveSpeed());
            if (currentmode.equalsIgnoreCase("Hypixel")) {
            	if(!em.isPre())
            		return;
            	fastFlew ++;
                if (premiumAddon != null && ((Options)settings.get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Fast2") ) {
                    if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
                        
                    	if(mc.thePlayer.hurtResistantTime == 19){
                    		MoveUtils.setMotion(0.3 + MoveUtils.getSpeedEffect() * 0.05f);
                    		mc.thePlayer.motionY = 0.41999998688698f + MoveUtils.getJumpEffect()*0.1;
                    		fastFlew = 25;
                    		hypixel = 13+((Number) settings.get(HYPIXELF).getValue()).doubleValue();
                    	}else if(fastFlew < 25){
                    		mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                            mc.thePlayer.jumpMovementFactor = 0;
                            mc.thePlayer.onGround = false;
                    	}
                    	
                    }
                }
                Block block = MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.2);
                if (!MoveUtils.isOnGround(0.0000001) && !block.isFullBlock() && !(block instanceof BlockGlass)) {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                    float speedf = 0.29f + MoveUtils.getSpeedEffect() * 0.06f;
                    if (hypixel > 0 && premiumAddon != null) {
                        if ((mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) || mc.thePlayer.isCollidedHorizontally)
                            hypixel = 0;                        
                        speedf += hypixel / 18;
                        if(((Options)settings.get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("OldFast")){
                        	hypixel-= 1.3;
                        }else if(((Options)settings.get("HYPIXELMODE").getValue()).getSelected().equalsIgnoreCase("Fast3")){
                        	hypixel-= 0.175 + MoveUtils.getSpeedEffect()*0.006; //0.152
                        }else{
                        	hypixel-= 0.155 + MoveUtils.getSpeedEffect()*0.006; //0.152
                        }
                        
                    }
                    setSpecialMotion(speedf);
                    mc.thePlayer.jumpMovementFactor = 0;
                    mc.thePlayer.onGround = false;
                    if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                        mc.thePlayer.motionY = 0.4;
                    }
                    count++;
                    mc.thePlayer.lastReportedPosY = 0;
                    if (count <= 2) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.9999E-8, mc.thePlayer.posZ);
                    } else if (count == 4){
                    	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1E-8, mc.thePlayer.posZ);
                    }else if(count >= 5){
                    	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.99999E-8, mc.thePlayer.posZ);
                    	count = 0;
                    }

                }

            }
            if (em.isPre()) {

                setSuffix(((Options) settings.get(MODE).getValue()).getSelected());
                double X = mc.thePlayer.posX; double Y = mc.thePlayer.posY; double Z = mc.thePlayer.posZ;
                switch (((Options) settings.get(MODE).getValue()).getSelected()) {
               
                	case"Cube1":
                	   
                	    if(!MoveUtils.isOnGround(0.001)){
                    		stage ++;
                    		
                    		mc.thePlayer.onGround = false;
                    		mc.thePlayer.lastReportedPosY = 0;
                    		mc.thePlayer.jumpMovementFactor = 0;
                    		float timer = 0.3f;
                    		double motion = 0;
                    		speed = 0;
                    		double dist = 4;
                    		if(stage == 1){
                    			motion = 0.28;
                    			speed = 2.4;
                    		}else if(stage == 2){
                    			motion = 0.28;
                    		}else if(stage == 3){
                    			motion = 0.28;
                    			speed = 2.4;
                    		}else if(stage == 4){
                    			motion = -0.9;
                    			stage = 0;
                    		}
                    		mc.timer.timerSpeed = timer;
                    		mc.thePlayer.motionY = motion;
                    		MoveUtils.setMotion(speed);
                    	}else{
                    		if(mc.timer.timerSpeed == 0.3f){
                    			mc.timer.timerSpeed = 1;
                    			MoveUtils.setMotion(0);
                    		}
                    	}
                        break;
                	case"Cube2":
                		if(!Client.um.isPremium()){
                            Notifications.getManager().post("Premium Bypass", "This mode is a premium only bypass", Notifications.Type.WARNING);
                		    toggle();
                		    return;
                        }
                		if(!MoveUtils.isOnGround(0.001)){
                    		stage ++;
                    		mc.timer.timerSpeed = 0.27f;	
                            mc.thePlayer.lastReportedPosY = 0;
                            mc.thePlayer.jumpMovementFactor = 0;
                            mc.thePlayer.onGround = false;
                        	double motion = mc.thePlayer.motionY;
                        	speed = 0;
                        	em.setOnGround(false);
                        	if(stage == 1){
                        		boolean a = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.4d, 0.0D)).isEmpty();
                        	
                        		em.setY(Y + (a?0.4:0.2));
                        		motion = 0.4;
                        	}else if(stage == 2){
                        		motion = 0.28;       
                        		boolean a = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.68d, 0.0D)).isEmpty();
                        		em.setY(Y + (a?0.68:0.2));
                          		if(shouldSpeed)
                        			speed = 2.4;
                        		else{
                        			shouldSpeed = true;
                        			speed = 2.2;
                        		}
                        	}else if(stage == 3){
                        		motion = -0.68;
                        	}else if(stage == 4){
                        		motion = 0;
                        		speed = 2.4;
                        		stage = 0;
                        	}
                        	
                        	MoveUtils.setMotion(speed);
                        	mc.thePlayer.motionY = 0;
                    	}else if(shouldSpeed){
                    		MoveUtils.setMotion(0);
                    	
                    		shouldSpeed = !shouldSpeed;
                    	}else if(mc.timer.timerSpeed != 1){
                    		mc.timer.timerSpeed = 1f;
                    	}
                    break;
                    
                	case"Cube3":
                        if(!Client.um.isPremium()){
                            Notifications.getManager().post("Premium Bypass", "This mode is a premium only bypass", Notifications.Type.WARNING);
                            toggle();
                            return;
                        }
                		if(!MoveUtils.isOnGround(0.001)){
                    		stage ++;
                    		
                    		mc.thePlayer.onGround = false;
                    		mc.thePlayer.lastReportedPosY = 0;
                    		mc.thePlayer.jumpMovementFactor = 0;
                    		float timer = 0.2f;
                    		double motion = 0;
                    		speed = 0;
                    		double dist = 4;
                    		boolean are = !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().expand(dist, dist, dist)).isEmpty();
                    		if(stage == 1){
                    			motion = 0.4;
                    			if(shouldSpeed){
                    				speed = 2.3;
                    			}else{
                    				speed = 2;
                    				shouldSpeed = !shouldSpeed;
                    			}
                    		}else if(stage == 2){
                    			motion = 0;
                    			timer = 1f;
                    		}else if(stage == 3){
                    			motion = 0.4;
                    			speed = 2.3;
                    			if(are)
                    			timer = 1f;
                    		}else if(stage == 4){
                    			motion = -0.9;
                    			//speed = 2.4;
                    			stage = 0;
                    			if(are)
                    				timer = 1f;
                    		}
                    		mc.timer.timerSpeed = timer;
                    		mc.thePlayer.motionY = motion;
                    		MoveUtils.setMotion(speed);
                    	}else if(shouldSpeed){
                    			MoveUtils.setMotion(0);
                    			shouldSpeed = !shouldSpeed;
                    	}else if(mc.timer.timerSpeed != 1){
                    		mc.timer.timerSpeed = 1;
                    	}
                	break;
                    case "Guardian": {
                        if (premiumAddon != null) {
                            premiumAddon.onEvent(em);
                        }
                    }
                    break;
                    case "Glide": {

                        final boolean shouldBlock = mc.thePlayer.posY + 0.1 >= startY && mc.gameSettings.keyBindJump.getIsKeyPressed();
                        if (mc.thePlayer.isSneaking()) {
                            mc.thePlayer.motionY = -0.4f;
                        } else if (mc.gameSettings.keyBindJump.getIsKeyPressed() && !shouldBlock) {

                            mc.thePlayer.motionY = 0.4f;
                        } else {
                            mc.thePlayer.motionY = -0.01;
                        }
                        break;
                    }
                    case "BowFly": {

                        if (getBowCount() == 0 && getArrowCount() == 0) {
                            if (bowfly.delay(5000)) {
                                Notifications.getManager().post("BowFly", "Please get a bow and arrows.", Notifications.Type.WARNING);
                                bowfly.reset();
                            }
                            return;
                        } else if (getBowCount() == 0) {
                            if (bowfly.delay(5000)) {
                                Notifications.getManager().post("BowFly", "Please get a bow.", Notifications.Type.WARNING);
                                bowfly.reset();
                            }
                            return;
                        } else if (getArrowCount() == 0) {
                            if (bowfly.delay(5000)) {
                                Notifications.getManager().post("BowFly", "Please get arrows.", Notifications.Type.WARNING);
                                bowfly.reset();
                            }
                            return;
                        }

                        if (invCheck()) {
                            ItemStack is = new ItemStack(Item.getItemById(261));
                            for (int i = 9; i < 36; i++) {

                                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                                    Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                                    int count = 0;
                                    if (item instanceof ItemBow) {
                                        for (int a = 36; a < 45; a++) {
                                            if (mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {
                                                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, a - 36, 2, mc.thePlayer);
                                                count++;
                                                break;
                                            }
                                        }
                                        if (count == 0) {
                                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 7, 2, mc.thePlayer);
                                        }
                                        break;

                                    }
                                }
                            }
                        }
                        getBow();

                        if (mc.thePlayer.getCurrentEquippedItem() != null &&
                                mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                            Packet C07 = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
                            Packet C08 = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
                            float yaw = mc.thePlayer.rotationYaw;
                            float pitch = -90;
                            if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
                                pitch = -80;
                            }
                            if (mc.thePlayer.moveForward < 0) {
                                yaw -= 180f;
                            }
                            if (mc.thePlayer.motionY < -0.1) {
                                pitch = 90f;
                            }
                            em.setPitch(pitch);
                            em.setYaw(yaw);
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.0001)) {
                                mc.thePlayer.jump();
                            } else {
                                if (mc.thePlayer.motionY < 0) {
                                    mc.timer.timerSpeed = 0.1f;
                                } else {
                                    if (mc.timer.timerSpeed == 0.1f) {
                                        mc.timer.timerSpeed = 1f;
                                    }
                                }
                            }
                            count++;
                            if (count >= 4) {
                                mc.thePlayer.sendQueue.addToSendQueue(C07);
                                count = 0;
                            } else if (count == 1) {
                                mc.thePlayer.sendQueue.addToSendQueue(C08);
                            }
                        }
                        break;
                    }
                    case "Vanilla": {

                        mc.thePlayer.capabilities.isFlying = true;
                        mc.thePlayer.capabilities.allowFlying = true;
                        break;
                    }
                    case "Motion": {
                    	mc.thePlayer.onGround= false;
                    	em.setOnGround(MoveUtils.isOnGround(0.001) || Client.getModuleManager().isEnabled(NoFall.class));
                        if (mc.thePlayer.movementInput.jump) {
                            mc.thePlayer.motionY = speed * 0.6;
                        } else if (mc.thePlayer.movementInput.sneak) {
                            mc.thePlayer.motionY = -speed * 0.6;
                        } else {
                            mc.thePlayer.motionY = 0;
                        }
                        break;
                    }
                    case "AntiKick": {

                        if (mc.thePlayer.movementInput.jump) {
                            mc.thePlayer.motionY = speed * 0.6;
                        } else if (mc.thePlayer.movementInput.sneak) {
                            mc.thePlayer.motionY = -speed * 0.6;
                        } else {
                            mc.thePlayer.motionY = 0;
                        }
                        updateFlyHeight();
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                        if (((this.flyHeight <= 290.0D) && (kickTimer.delay(500L)))
                                || ((this.flyHeight > 290.0D) && (kickTimer.delay(100L)))) {
                            goToGround();
                            kickTimer.reset();
                        }
                        break;
                    }

                }
            }
        }

        if (event instanceof EventMove) {
            EventMove em = (EventMove) event;
            String mode = ((Options) settings.get(MODE).getValue()).getSelected();
            if (mode.equalsIgnoreCase("Antikick") || mode.equalsIgnoreCase("Motion") || mode.equalsIgnoreCase("glide")) {

                double speed = ((Number) settings.get(SPEED).getValue()).floatValue();
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yaw = mc.thePlayer.rotationYaw;
                if ((forward == 0.0D) && (strafe == 0.0D)) {
                    em.setX(0.0D);
                    em.setZ(0.0D);
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
                    em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
                            + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                    em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
                            - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
                }
            }
        }
    }


    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }

    public void updateFlyHeight() {
        double h = 1.0D;
        AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
        for (this.flyHeight = 0.0D; this.flyHeight < mc.thePlayer.posY; this.flyHeight += h) {
            AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
            if (mc.theWorld.checkBlockCollision(nextBox)) {
                if (h < 0.0625D) {
                    break;
                }
                this.flyHeight -= h;
                h /= 2.0D;
            }
        }
    }

    public void goToGround() {
        if (this.flyHeight > 300.0D) {
            return;
        }
        double minY = mc.thePlayer.posY - this.flyHeight;
        if (minY <= 0.0D) {
            return;
        }
        for (double y = mc.thePlayer.posY; y > minY; ) {
            y -= 8.0D;
            if (y < minY) {
                y = minY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        for (double y = minY; y < mc.thePlayer.posY; ) {
            y += 8.0D;
            if (y > mc.thePlayer.posY) {
                y = mc.thePlayer.posY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBow) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getArrowCount() {
        int arrowCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                Item arrow = Item.getItemById(262);
                if (item == arrow) {
                    arrowCount += is.stackSize;
                }
            }
        }
        return arrowCount;
    }

    public int getBowCount() {
        int bowCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBow) {
                    bowCount += is.stackSize;
                }
            }
        }
        return bowCount;
    }

    private void getBow() {
        ItemStack is = new ItemStack(Item.getItemById(262));
        try {
            if (mc.thePlayer.inventory.getCurrentItem() != null && !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow))
                for (int i = 36; i < 45; i++) {
                    int theSlot = i - 36;

                    if (!mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), is, true)
                            && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBow && mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null) {
                        mc.thePlayer.inventory.currentItem = theSlot;
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        mc.playerController.updateController();
                        break;
                    }
                }

        } catch (Exception e) {
        }
    }
    private void setSpecialMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = 0;
        	mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
            	if(hypixel <= 0)
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
    public void setHypixelAddon(Module module) {
        this.premiumAddon = module;
    }
}
