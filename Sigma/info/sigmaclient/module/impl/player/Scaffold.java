package info.sigmaclient.module.impl.player;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventPushBlock;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.movement.Highjump;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.movement.Phase;

import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.sun.javafx.event.EventRedirector;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

	
    public static List<Block> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }

    public static float yaw = 999, pitch = 999;
    private static List<Block> blacklistedBlocks;
    private Timer timer = new Timer();
    private String TOWER = "TOWER";
    
    public static final String MODE = "MODE";
    private final String EXPAND = "EXPAND";
    private final String DELAY = "DELAY";
    private final String AACTOWER = "AACTOWER";
    public static final String ROT = "ROTATIONS";
    private final String TOWERMOVE = "TOWERMOVE";
    private final String TOWERMODE = "TOWERMODE";
    private final String PICKER = "PICKER";
    private final String ESP = "ESP";
    private final String SAMEY = "SAMEY";
    private final String LEGIT = "LEGIT";
    private String noswing = "NOSWING";
    Timer tDelay = new Timer();
    public static String currentMode = "";
    BlockData blockdata;
    double y;
    boolean sneaking;
    int cubeSpoof;
    int count;
    public Scaffold(ModuleData data) {
        super(data);
        settings.put(TOWER, new Setting<>(TOWER, true, "Helps you build up faster."));
        settings.put(SAMEY, new Setting<>(SAMEY, false, "Keep same height."));
        settings.put(TOWERMOVE, new Setting<>(TOWERMOVE, false, "Allows you to tower while moving."));
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Normal", new String[]{"Normal", "Hypixel", "Mineplex", "AAC", "Custom", "Legit"}), "Scaffold method."));
        settings.put(TOWERMODE, new Setting<>(TOWERMODE, new Options("TowerMode", "Normal", new String[]{"Normal", "AAC", "Cubecraft", "Hypixel", "AAC2"}), "Tower method."));
        settings.put(DELAY, new Setting<>(DELAY, 100, "Delay for Custom mode.", 25, 100, 1000));
        settings.put(AACTOWER, new Setting<>(AACTOWER, 0.4, "AAC Tower speed", 0.05, 0.2, 1));
        settings.put(LEGIT, new Setting<>(LEGIT, 1, "Legit speed multiplier", 0.01, 1, 3));
        settings.put(EXPAND, new Setting<>(EXPAND, 0.4, "Expand distance", 0.2, 0, 5));
        settings.put(ROT, new Setting<>(ROT, true, "Keep your rotations for Custom mode."));
        settings.put(noswing, new Setting<>(noswing, true, "Remove the swing animation."));
    	settings.put(PICKER, new Setting<>("BlockPicker", true, "New block picker."));
    	settings.put(ESP, new Setting<>(ESP, true, "Draw rectangle where you placed a block."));
        blacklistedBlocks = Arrays.asList(
                Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
                Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars,
                Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore,
                Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt,
                Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
                Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate,
                Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
                Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, 
                Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    }

    @Override
    public void onEnable() {
        tDelay.reset();
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && mc.getIntegratedServer() == null)
            if (!((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel")) {
                Notifications.getManager().post("Warning", ((Options) settings.get(MODE).getValue()).getSelected() + " Scaffold doesn't bypass on Hypixel.", Notifications.Type.WARNING);
            }
        yaw = 999;
        pitch = 999;
        count = 1;
     	blockdata = null;
     	y = mc.thePlayer.posY;
     	sneaking = true;
     	cubeSpoof = -1;
        super.onEnable();
    }

    public void onDisable() {
        yaw = 999;
        pitch = 999;
        blockdata = null;
        mc.timer.timerSpeed = 1f;
        if(sneaking && !mc.thePlayer.isSneaking()){
        	C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
        	mc.thePlayer.sendQueue.addToSendQueue(p);
        }
        if (mc.thePlayer.isSwingInProgress) {
            mc.thePlayer.swingProgress = 0;
            mc.thePlayer.swingProgressInt = 0;
            mc.thePlayer.isSwingInProgress = false;
        }
        if(cubeSpoof != mc.thePlayer.inventory.currentItem && cubeSpoof != -1){
        	C09PacketHeldItemChange p = new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem);
        	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
        	cubeSpoof = -1;
        }
    }

    int slot;


    @Override
    @RegisterEvent(events = { EventUpdate.class, EventRenderGui.class, EventRender3D.class, EventAttack.class, EventPacket.class, EventPushBlock.class})
    public void onEvent(Event event) {
    	currentMode = ((Options) settings.get(MODE).getValue()).getSelected();
    	String towerMode = ((Options) settings.get(TOWERMODE).getValue()).getSelected();

        if(premiumAddon == null && currentMode.equalsIgnoreCase("Hypixel"))
        {
            if (timer.delay(400)) {
                Notifications.getManager().post("Premium Bypass", "Hypixel mode is a premium only bypass", Notifications.Type.WARNING);
                timer.reset();
            }
            toggle();
            return;
        }

        setSuffix(currentMode);
        boolean shouldTower = (Boolean) settings.get(TOWER).getValue();
        boolean samey = (Boolean) settings.get(SAMEY).getValue();
        boolean keepRotations = (Boolean) settings.get(ROT).getValue();
        if(event instanceof EventPushBlock){
        	EventPushBlock epb = (EventPushBlock)event;
        	if(towerMode.equalsIgnoreCase("AAC2")){
                if(getBlockCount()>0)
                if (mc.gameSettings.keyBindJump.getIsKeyPressed() && shouldTower && ((Boolean) settings.get(TOWERMOVE).getValue() || !PlayerUtil.isMoving2()))
                    if(!samey || !PlayerUtil.isMoving2()){
                    	epb.setCancelled(true);
                    }
        	}
        }
        if(event instanceof EventAttack){
        	EventAttack at = (EventAttack)event;
        	if(cubeSpoof != mc.thePlayer.inventory.currentItem && cubeSpoof != -1)
        	if(at.isPreAttack()){
        	//	C09PacketHeldItemChange p = new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem);
        	//	mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
        	//	cubeSpoof = mc.thePlayer.inventory.currentItem;
        	}
        }
        if(event instanceof EventPacket){
        	EventPacket ep = (EventPacket)event;
        	Packet p = ep.getPacket();
        	if(p instanceof C09PacketHeldItemChange){
        		C09PacketHeldItemChange packet = (C09PacketHeldItemChange)p;
        		int slot = packet.getSlotId();
        		if(ep.isPre())
        		if(cubeSpoof != -1){
        			ep.setCancelled(true);
        		}
        	}
        	if(cubeSpoof != -1){
                if(p instanceof C08PacketPlayerBlockPlacement){
                	C08PacketPlayerBlockPlacement pa = (C08PacketPlayerBlockPlacement)p;
                	ItemStack stack =pa.getStack();
                	if(!ItemStack.areItemsEqual(mc.thePlayer.inventory.getStackInSlot(cubeSpoof), stack)){
                		ep.setCancelled(true);
                	}
                }
                if(p instanceof C07PacketPlayerDigging){
                	C07PacketPlayerDigging pa = (C07PacketPlayerDigging)p;
                	net.minecraft.network.play.client.C07PacketPlayerDigging.Action act  = pa.func_180762_c();
                	if(act == net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM){
                		if(!ItemStack.areItemsEqual(mc.thePlayer.inventory.getStackInSlot(cubeSpoof), mc.thePlayer.inventory.getCurrentItem())){
                    		ep.setCancelled(true);

                    	}
                	}
                }
        	}
        }
        if (event instanceof EventRenderGui) {

            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int blockCount = getBlockCount();
            int color = Colors.getColor(255, 0, 0);
            if (blockCount >= 64 && 128 > blockCount) {
                color = Colors.getColor(255, 255, 0);
            } else if (blockCount >= 128) {
                color = Colors.getColor(0, 255, 0);
            }
            if(blockCount == 0){
            	yaw = 999;
            	pitch = 999;
            }
            Client.fm.getFont("SFB 7").drawStringWithShadow(blockCount + "", res.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(blockCount + "") / 2, res.getScaledHeight() / 2 - 25, color);
        }
        if(event instanceof EventRender3D){
        	EventRender3D err = (EventRender3D)event;
        	
            if(blockdata != null && (Boolean) settings.get(ESP).getValue()){
            	RenderingUtil.pre3D();
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
            	RenderingUtil.glColor(Client.cm.getESPColor().getColorInt());
            	BlockPos place = blockdata.position;
            	EnumFacing face = blockdata.face;
            	double x1 = place.getX() - RenderManager.renderPosX;
            	double x2 = place.getX() - RenderManager.renderPosX + 1;
            	double y1 = place.getY() - RenderManager.renderPosY;
            	double y2 = place.getY() - RenderManager.renderPosY + 1;
            	double z1 = place.getZ() - RenderManager.renderPosZ;
            	double z2 = place.getZ() - RenderManager.renderPosZ + 1;
            	y1 += face.getFrontOffsetY();
            	if(face.getFrontOffsetX() < 0){
            		x2 += face.getFrontOffsetX();
            	}else{
            		x1 += face.getFrontOffsetX();
            	}
            	if(face.getFrontOffsetZ() < 0){
                	z2 += face.getFrontOffsetZ();
            	}else{
                	z1 += face.getFrontOffsetZ();
            	}
            	
                RenderingUtil.drawBoundingBox(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                RenderingUtil.post3D();
            }
          
        }
        if (event instanceof EventUpdate) {
            

            EventUpdate em = (EventUpdate) event;

            if(em.isPre()){
            	getBestBlocks();	
            	if(!currentMode.equalsIgnoreCase("Legit") && !currentMode.equalsIgnoreCase("Cubecraft")){
                    if(sneaking && !mc.thePlayer.isSneaking()){
                    	C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
                    	mc.thePlayer.sendQueue.addToSendQueue(p);
                    	sneaking = !sneaking;
                    }
                    if(cubeSpoof != -1){
                    	C09PacketHeldItemChange p = new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem);
                		mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
                		cubeSpoof = -1;
                    }         	
            	}else if(!sneaking){
            		BlockPos ppos = new BlockPos(-1, -1, -1);
            		ItemStack stack;
            		if(cubeSpoof == -1){
            			stack = mc.thePlayer.inventory.getCurrentItem();
            		}else{
            			stack = mc.thePlayer.inventory.getStackInSlot(cubeSpoof);
            		}
        			
        			C08PacketPlayerBlockPlacement pl = new C08PacketPlayerBlockPlacement(ppos, 255, stack, 0, 0, 0);	
        			mc.thePlayer.sendQueue.addToSendQueue(pl);
            	}
            }
            
            if (!hotbarContainBlock()) {
            	blockdata = null;
            	return;
            }
            double x = mc.thePlayer.posX;
            double z = mc.thePlayer.posZ;
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            float YAW = mc.thePlayer.rotationYaw;
            if (!mc.thePlayer.isCollidedHorizontally){
            	double[] coords = getExpandCoords(x,z,forward,strafe,YAW);
                x = coords[0];
                z = coords[1];
            }
                
                
            if (isAirBlock(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock())) {
                x = mc.thePlayer.posX;
                z = mc.thePlayer.posZ;
            }
            if(samey){
            	if(mc.thePlayer.fallDistance > 1.2 + 1* MoveUtils.getJumpEffect() ||(!PlayerUtil.isMoving2() && mc.gameSettings.keyBindJump.getIsKeyPressed())){
            		y = mc.thePlayer.posY;
            	}
            }else{
            	y = mc.thePlayer.posY;
            }
            BlockPos underPos = new BlockPos(x, y-1, z);
            Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();

            BlockData data = getBlockData(underPos);
            if (em.isPre()) {

                if (((keepRotations && currentMode.equalsIgnoreCase("Custom")) || currentMode.equalsIgnoreCase("Legit") || currentMode.equalsIgnoreCase("Cubecraft")) && yaw != 999 && pitch != 999) {
                    em.setYaw(yaw + (float)randomNumber(1, -1));
                    em.setPitch(pitch);   
                }
               
                setSpeed(currentMode, em);
                if(getBlockCount()>0){
                	if (shouldTower && ((Boolean) settings.get(TOWERMOVE).getValue() || !PlayerUtil.isMoving2())) {
                    	if(!samey || !PlayerUtil.isMoving2()){
                    		tower(towerMode, em);
                    	}
                    	
                    } else{
                    	if (!mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.timer.timerSpeed == ((Number) settings.get(AACTOWER).getValue()).floatValue()) {
                      		mc.timer.timerSpeed = 1;
                    	}
                    }
                }
            }
            if (isAirBlock(underBlock) && data != null) {
            	
                if (em.isPre()) {
                	blockdata = data;
                    float[] rot = getRotations(data.position, data.face);
                    if (!currentMode.equalsIgnoreCase("Mineplex")) {
                    	em.setYaw(rot[0]);
                        em.setPitch(rot[1]);
                        Killaura.sYaw = rot[0];
                        Killaura.sPitch = rot[1];
                        yaw = rot[0];
                        pitch = rot[1];
                    }
                    if (currentMode.equalsIgnoreCase("Hypixel") && !mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.onGround && MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                        em.setOnGround(false);                     
                    }
                   
                } else {

                    if (!tDelay.delay(((Number) settings.get(DELAY).getValue()).floatValue()) && currentMode.equalsIgnoreCase("custom") && !mc.gameSettings.keyBindJump.getIsKeyPressed() && MoveUtils.isOnGround(0.001)) {
                        return;
                    }
                    if (!tDelay.delay(((Number) settings.get(DELAY).getValue()).floatValue()) && currentMode.equalsIgnoreCase("Legit") && !mc.gameSettings.keyBindJump.getIsKeyPressed() && MoveUtils.isOnGround(0.001)) {
                    	return;
                    }
                    if (!tDelay.delay(80) && currentMode.equalsIgnoreCase("Hypixel") && MoveUtils.isOnGround(0.01) && !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                        return;
                    }
                    tDelay.reset();
                    int slot = mc.thePlayer.inventory.currentItem;
                    ItemSpoof();    
                  
                  
                    if (currentMode.equalsIgnoreCase("aac") || currentMode.equalsIgnoreCase("Mineplex")) {
                        if (mc.thePlayer.onGround) {
                            if (mc.thePlayer.isSprinting()) {
                                mc.thePlayer.motionX *= 0.5;
                                mc.thePlayer.motionZ *= 0.5;
                            }
                        } else {
                            mc.thePlayer.motionX *= 0.9;
                            mc.thePlayer.motionZ *= 0.9;
                        }
                    }
                    if (currentMode.equalsIgnoreCase("Hypixel") && !mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.onGround && MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
                        em.setOnGround(false);
                    }
                    count ++;
                    PlayerControllerMP.canPlace = true;
                    if((currentMode.equalsIgnoreCase("Legit") || currentMode.equalsIgnoreCase("Cubecraft")) && cubeSpoof != -1){
                    	
                    	mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(cubeSpoof), data.position, data.face, getVec3(data.position, data.face));
                    }else{
                    	mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, getVec3(data.position, data.face));
                    }
                    PlayerControllerMP.canPlace = false;
                    if(timer.delay(250)){
                        timer.reset();
                    }
               
                    if ((Boolean) settings.get(noswing).getValue()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    } else {
                        mc.thePlayer.swingItem();
                    }
                    mc.thePlayer.inventory.currentItem = slot;
                    mc.playerController.updateController();	

                   
                    
                }
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
 
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum,2, mc.thePlayer);

    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (item instanceof ItemBlock && isValid(item)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && isValid(item)) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
                return false;
            }
            return true;
        }

        return false;
    }
    public int getBiggestBlockSlotInv(){
    	int slot = -1;
    	int size = 0;
    	if(getBlockCount() == 0)
    		return - 1;
    	   for (int i = 9; i < 36; i++) {
               if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                   Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                   ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                   if (item instanceof ItemBlock && isValid(item)) {             	 
                	   if(is.stackSize > size){
                		   size = is.stackSize;
                		   slot = i;
                	   }
                   }
               }
           }
    	return slot;
    }
    public int getBiggestBlockSlotHotbar(){
    	int slot = -1;
    	int size = 0;
    	if(getBlockCount() == 0)
    		return - 1;
    	   for (int i = 36; i < 45; i++) {
               if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                   Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                   ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                   if (item instanceof ItemBlock && isValid(item)) {             	 
                	   if(is.stackSize > size){
                		   size = is.stackSize;
                		   slot = i;
                	   }
                   }
               }
           }
    	return slot;
    }
    public void getBestBlocks(){
    	
 	 	if(getBlockCount() == 0)
    		return;
 	 	if((Boolean) settings.get(PICKER).getValue()){
 	 		ItemStack is = new ItemStack(Item.getItemById(261));
 	    	int bestInvSlot = getBiggestBlockSlotInv();
 	    	int bestHotbarSlot = getBiggestBlockSlotHotbar();
 	    	int bestSlot = getBiggestBlockSlotHotbar() > 0 ? getBiggestBlockSlotHotbar() : getBiggestBlockSlotInv();
 	    	int spoofSlot = 42;
 	    	if(bestHotbarSlot > 0 && bestInvSlot > 0){
 	    		if (mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack() && mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack() ) {
 	    			if(mc.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack().stackSize < mc.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack().stackSize){
 	    				bestSlot = bestInvSlot;
 	    			}
 	    		}
 	    	}
 	    	if(hotbarContainBlock()){
 	    		for (int a = 36; a < 45; a++) {       		
 	        		if (mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
 	        			Item item = mc.thePlayer.inventoryContainer.getSlot(a).getStack().getItem();
 	                    if(item instanceof ItemBlock && isValid(item)){
 	                    	spoofSlot = a;
 	                        break;
 	                    }
 	                }
 	            }
 	    	}else{
 	    		for (int a = 36; a < 45; a++) {       		
 	        		if (!mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
 	        			spoofSlot = a;
 	                    break;
 	                }
 	            }
 	    	}
 	    	
 	    	if (mc.thePlayer.inventoryContainer.getSlot(spoofSlot).slotNumber != bestSlot) {
 	    	   	
 	    		swap(bestSlot, spoofSlot - 36);
 	    		mc.playerController.updateController();
 	    		
 	    	
 	    	}
 	 	}else{
 	 		if (invCheck()) {

                ItemStack is = new ItemStack(Item.getItemById(261));
                for (int i = 9; i < 36; i++) {

                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                        int count = 0;
                        if (item instanceof ItemBlock && isValid(item)) {
                            for (int a = 36; a < 45; a++) {
                                if (mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {
                                	
                                	swap(i, a - 36);
                                    count++;
                                    break;
                                }
                            }

                            if (count == 0) {
                            	
                                swap(i, 7);
                            }
                            break;

                        }
                    }
                }
            }
 	 	}     
    }
    public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += (double) face.getFrontOffsetX() / 2;
        z += (double) face.getFrontOffsetZ() / 2;
        y += (double) face.getFrontOffsetY() / 2;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber(0.3, -0.3);
            z += randomNumber(0.3, -0.3);
        } else {
            y += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if ((block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet
                || block instanceof BlockSnow || block instanceof BlockSkull)
                && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
            return true;
        }
        return false;
    }

    private void ItemSpoof() {
      	if(currentMode.equalsIgnoreCase("Legit") || currentMode.equalsIgnoreCase("Cubecraft")){
    		if(Killaura.target != null && cubeSpoof != -1){
    			if(cubeSpoof != mc.thePlayer.inventory.currentItem){
                	//C09PacketHeldItemChange p = new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem);
                	//mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
                	//cubeSpoof = mc.thePlayer.inventory.currentItem;
    			}
    	
            	//return;
    		}
        }
        ItemStack is = new ItemStack(Item.getItemById(261));
        try {
            for (int i = 36; i < 45; i++) {
                int theSlot = i - 36;

                if (!mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), is, true)
                        && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null) {
                    if (isValid(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0) {
                        if (mc.thePlayer.inventory.currentItem != theSlot) {
                    	
                        	if(currentMode.equalsIgnoreCase("Legit") || currentMode.equalsIgnoreCase("Cubecraft")){
                        		if(cubeSpoof != theSlot){
                        			
                              	    C09PacketHeldItemChange p = new C09PacketHeldItemChange(theSlot);
                            	    cubeSpoof = theSlot;
                            	    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(p);
                            	    mc.thePlayer.inventory.currentItem = theSlot;
                                    mc.playerController.updateController();
                        		}else{
                             	    mc.thePlayer.inventory.currentItem = theSlot;
                                    mc.playerController.updateController();
                        		}
                        	}else{
                        	    mc.thePlayer.inventory.currentItem = theSlot;
                                mc.playerController.updateController();
                        	}
                        
                        }
                        break;

                    }
                }
            }
        } catch (Exception e) {
        }
    }


    private BlockData getBlockData(BlockPos pos) {
    	
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
    public static float[] getRotations(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5 - mc.thePlayer.posX +  (double) face.getFrontOffsetX()/2;
        double z = block.getZ() + 0.5 - mc.thePlayer.posZ +  (double) face.getFrontOffsetZ()/2;
        double y = (block.getY() + 0.5);
        if(currentMode.equalsIgnoreCase("Legit")){
        	double dist = mc.thePlayer.getDistance(block.getX()+ 0.5 + (double) face.getFrontOffsetX()/2, block.getY(), block.getZ()+ 0.5+  (double) face.getFrontOffsetZ()/2);
        	//if(dist > 1.5){
        		y += 0.5;
        		//x += (double) face.getFrontOffsetX()/8;
        		//z += (double) face.getFrontOffsetZ()/8;
        	//}
        	
        }
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360f;
        }
        return new float[]{yaw, pitch};
    }

    public static boolean shouldSprint() {
        if (Client.getModuleManager().isEnabled(Scaffold.class) && (currentMode.equalsIgnoreCase("Legit") || currentMode.equalsIgnoreCase("Cubecraft")))
            return false;
        return true;
    }

    private boolean hotbarContainBlock() {
        int i = 36;

        while (i < 45) {
            try {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((stack == null) || (stack.getItem() == null) || !(stack.getItem() instanceof ItemBlock) || !isValid(stack.getItem())) {
                    i++;
                    continue;
                }
                return true;
            } catch (Exception e) {

            }
        }

        return false;

    }

    private boolean isValid(Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        } else {
            ItemBlock iBlock = (ItemBlock) item;
            Block block = iBlock.getBlock();
            if (blacklistedBlocks.contains(block)) {
                return false;
            }
        }
        return true;
    }

    private class BlockData {

        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }

    }

    public void tower(String mode, EventUpdate em) {
        BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        BlockData data = getBlockData(underPos);
        if(!mc.gameSettings.keyBindJump.getIsKeyPressed()){
        	if(((Boolean) settings.get(TOWERMOVE).getValue() && PlayerUtil.isMoving2()) && (mode.equalsIgnoreCase("Hypixel" ) || mode.equalsIgnoreCase("AAC"))){
        		if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                    mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
                }
                if (MoveUtils.isOnGround(0.0001)) {   
                	if(mode.equalsIgnoreCase("AAC")){
                        mc.thePlayer.motionX *= 0.95;
                        mc.thePlayer.motionZ *= 0.95;   
                	}
                }else if(mc.thePlayer.motionY > 0.1 && mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                   
                	mc.thePlayer.motionY = 0;
                }
        	}
        	return;
        }
        if(mode.equalsIgnoreCase("Cubecraft")){
        	count ++;
    		mc.thePlayer.motionX = 0;
    		mc.thePlayer.motionZ = 0;
    		mc.thePlayer.jumpMovementFactor = 0;
        	if(MoveUtils.isOnGround(2))        		
        	if(count == 1){
        		mc.thePlayer.motionY = 0.41;
        	}else{
        		
        		mc.thePlayer.motionY = 0.47;
        		count = 0;
        	}
        }
        if (mode.equalsIgnoreCase("aac")) {
            if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
            }
            if(!PlayerUtil.isMoving2()){
            	mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        		mc.thePlayer.jumpMovementFactor = 0;
            }
            if (MoveUtils.isOnGround(0.0001)) {            	
                mc.thePlayer.motionY = 0.42;
                mc.thePlayer.motionX *= 0.9;
                mc.thePlayer.motionZ *= 0.9;          	
            }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                mc.thePlayer.motionY = 0;
                mc.timer.timerSpeed = ((Number) settings.get(AACTOWER).getValue()).floatValue();
            }
            if (mc.timer.timerSpeed == ((Number) settings.get(AACTOWER).getValue()).floatValue()) {
                mc.timer.timerSpeed = 1;
            }
        } else if(mode.equalsIgnoreCase("Normal")){
            if (isAirBlock(underBlock) && data != null) {
                mc.thePlayer.motionY = 0.4196;
                mc.thePlayer.motionX *= 0.75;
                mc.thePlayer.motionZ *= 0.75;
            }
            if(!PlayerUtil.isMoving2()){
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
            }
        } else if(mode.equalsIgnoreCase("Hypixel")){
        	if(PlayerUtil.isMoving2()){
                if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                    mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
                }
                if (MoveUtils.isOnGround(0.0001)) {            	
                    mc.thePlayer.motionY = 0.42;
                    mc.thePlayer.motionX *= 0.9;
                    mc.thePlayer.motionZ *= 0.9;          	
                }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
                    mc.thePlayer.motionY = 0;
                }
        	}else{
        		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        		mc.thePlayer.jumpMovementFactor = 0;
        		if (isAirBlock(underBlock) && data != null) {
                    mc.thePlayer.motionY = 0.4196;
                    mc.thePlayer.motionX *= 0.75;
                    mc.thePlayer.motionZ *= 0.75;
                }
        	}
        }else if(mode.equalsIgnoreCase("AAC2")){
        	double X = mc.thePlayer.posX; double Y = mc.thePlayer.posY; double Z = mc.thePlayer.posZ;
        	em.setPitch(90);
        	if(Phase.isInsideBlock())
        		mc.thePlayer.motionY = 0.43;
        	if(!PlayerUtil.isMoving2()){
           		mc.thePlayer.motionX = 0;
        		mc.thePlayer.motionZ = 0;
        		mc.thePlayer.jumpMovementFactor = 0;
        	}

        	int slot = mc.thePlayer.inventory.currentItem;
        	ItemSpoof();
        	for(int i = 0; i < 2; i++){
        		BlockPos underp = new BlockPos(X,mc.thePlayer.isCollidedVertically ? Y+i:Y-1.4+i, Z);
            	net.minecraft.block.Block block = mc.theWorld.getBlockState(underp).getBlock();
            	if(block.isTranslucent()){
            		PlayerControllerMP.canPlace = true;
            		mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), underp.add(0, -1, 0), EnumFacing.UP, new Vec3(0, 0, 0));
            		PlayerControllerMP.canPlace = false;
            		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            		break;
            	}	
        	}
        	mc.thePlayer.inventory.currentItem = slot;
        }
    }

    public void setSpeed(String mode, EventUpdate em) {
		 /*Zitter :
		                
                float var1 = mc.thePlayer.rotationYaw * 0.017453292F;
		        zitter++;
                if(zitter >=2){
                	zitter = 0;
                }
                float zitterspeed = 0.1f;
                if(!(mc.gameSettings.keyBindJump.getIsKeyPressed() && (mc.thePlayer.moveForward == 0 || mc.thePlayer.moveStrafing == 0)))
                if(zitter == 0){
                	mc.thePlayer.setPosition(mc.thePlayer.posX - MathHelper.sin(var1 + 1.5717f) * zitterspeed, 
                						 mc.thePlayer.posY,
                						 mc.thePlayer.posZ + MathHelper.cos(var1 + 1.5717f) * zitterspeed);
                }else{
                	mc.thePlayer.setPosition(mc.thePlayer.posX - MathHelper.sin(var1 - 1.5717f) * zitterspeed, 
     						 			 mc.thePlayer.posY,
     						 			 mc.thePlayer.posZ + MathHelper.cos(var1 - 1.5717f) * zitterspeed);
                }
		  */
        double motionx = mc.thePlayer.motionX;
        double motionz = mc.thePlayer.motionZ;
        if(mode.equalsIgnoreCase("Cubecraft")){
        	if(!Client.getModuleManager().isEnabled(LongJump.class) && !Client.getModuleManager().isEnabled(Fly.class)){
        		mc.thePlayer.onGround = false;
        		mc.thePlayer.jumpMovementFactor = 0;
            	double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float YAW = mc.thePlayer.rotationYaw;
                double a = (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)));
                double b = (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)));
                double c = Math.abs((a* b));
                double slow = 1-c*5;
                double speed =0.35 + randomNumber(0.01, -0.05);
                speed *= slow;
            	MoveUtils.setMotion(speed);
            	mc.thePlayer.setSprinting(false);
        	} 	
        }else if (mode.equalsIgnoreCase("Legit")) {
        	if(!Client.getModuleManager().isEnabled(LongJump.class) && !Client.getModuleManager().isEnabled(Fly.class)){

                if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
                	mc.thePlayer.setSprinting(false);
                	if(timer.delay(50)){
                		if(timer.delay(250)){
                			if(!sneaking && !mc.gameSettings.keyBindJump.getIsKeyPressed()){
                				C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.START_SNEAKING);
                        		mc.thePlayer.sendQueue.addToSendQueue(p);
                        		sneaking = !sneaking;
                			}else if(mc.gameSettings.keyBindJump.getIsKeyPressed()){
                				C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
                        		mc.thePlayer.sendQueue.addToSendQueue(p);
                        		sneaking = !sneaking;
                			}
                		}else{
                   			if(sneaking){
                				C0BPacketEntityAction p = new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SNEAKING);
                        		mc.thePlayer.sendQueue.addToSendQueue(p);
                        		sneaking = !sneaking;
                			}
                		}
                	}
                	if(mc.gameSettings.keyBindJump.pressed){
                		mc.thePlayer.jump();
                	}
                	mc.thePlayer.onGround = false;
                	mc.thePlayer.jumpMovementFactor = 0;
                  
                    double speed = sneaking? 0.09 : 0.13;
                  
                    double x = mc.thePlayer.posX;
                    double z = mc.thePlayer.posZ;
                    double forward = mc.thePlayer.movementInput.moveForward;
                    double strafe = mc.thePlayer.movementInput.moveStrafe;
                    float YAW = mc.thePlayer.rotationYaw;
                    double a = (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)));
                    double b = (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)));
                    double c = Math.abs((a* b));
                    double slow = 1-c*5;
                    speed *= slow;
                    if (speed < 0.05)
                        speed = 0.05;
                    speed += randomNumber(0.001, -0.001);
                    double more = ((Number) settings.get(LEGIT).getValue()).doubleValue();
                    speed *= more;
                    MoveUtils.setMotion(speed);
                } else {
                	//mc.thePlayer.jumpMovementFactor = 0;
                	//mc.thePlayer.onGround = false;
                   // double speed = ((Number) settings.get(CUBE).getValue()).doubleValue()*1;
                   // MoveUtils.setMotion(speed);
                } 
        	}
         
            } else if (mode.equalsIgnoreCase("Mineplex") && !Client.getModuleManager().isEnabled(Bhop.class)) {
                double slow = 0;
                if (mc.thePlayer.onGround) {
                    slow = 1 - Math.abs(motionx * motionz) * 80;
                } else {
                    slow = 0.8;
                }
                mc.thePlayer.motionX *= slow;
                mc.thePlayer.motionZ *= slow;
            }
    }
    public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW){
    	BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
        Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        double xCalc = -999, zCalc = -999;
        double dist = 0;
        double expandDist = ((Number) settings.get(EXPAND).getValue()).doubleValue()*2;
        if(currentMode.equalsIgnoreCase("Cubecraft")){
        	expandDist += randomNumber(0.1, -0.3);
        }
        while(!isAirBlock(underBlock)){
        	xCalc = x;
        	zCalc = z;
        	dist ++;
        	if(dist > expandDist){
        		dist = expandDist;
        	}
        	xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
        	zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
        	if(dist == expandDist){
        		break;
        	}
        	underPos = new BlockPos(xCalc, mc.thePlayer.posY - 1, zCalc);
        	underBlock = mc.theWorld.getBlockState(underPos).getBlock();
        }
    	return new double[]{xCalc,zCalc};
    }

    public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
}
