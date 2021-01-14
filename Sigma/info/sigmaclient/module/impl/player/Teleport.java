package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventRender3D;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.misc.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Teleport extends Module {

    private boolean canTP;
    private int delay;
    public BlockPos endPos;
    Timer cooldown = new Timer();
    String MODE = "MODE";
    private info.sigmaclient.util.Timer timer = new info.sigmaclient.util.Timer();

    public Teleport(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Mode", "Hypixel", new String[]{"Hypixel", "Basic"}), "ClickBlink method."));
    }
    @Override
    public void onEnable(){
    	if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("hypixel")){
    		ChatUtil.printChat(Command.chatPrefix + "Try avoiding any moves before teleporting.");
    	}
    }
    public MovingObjectPosition getBlinkBlock() {
        Vec3 var4 = mc.thePlayer.func_174824_e(mc.timer.renderPartialTicks);
        Vec3 var5 = mc.thePlayer.getLook(mc.timer.renderPartialTicks);
        Vec3 var6 = var4.addVector(var5.xCoord * 70, var5.yCoord * 70, var5.zCoord * 70);
        return mc.thePlayer.worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventRender3D.class})
    public void onEvent(Event event) {
    	this.setSuffix(((Options) settings.get(MODE).getValue()).getSelected());

    	if(premiumAddon == null && ((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel")) {
            if (timer.delay(400)) {
                Notifications.getManager().post("Premium Bypass", "Hypixel teleport mode is a premium only bypass", Notifications.Type.WARNING);
                timer.reset();
            }
            toggle();
            return;
        }

        try {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                return;
            }
        } catch (Exception e) {
        }
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
            	if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Basic")){
	                if (canTP && Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && delay == 0 && mc.inGameHasFocus && getBlinkBlock().entityHit == null && !(getBlock(getBlinkBlock().getBlockPos()) instanceof BlockChest)) {
	                	
	                	event.setCancelled(true);
	                    endPos = getBlinkBlock().getBlockPos().add(0,1,0);
	                    final double[] startPos = {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
	                    PlayerUtil.teleport(startPos, endPos);
	                    delay = 5;
	                    event.setCancelled(false);
	                }
            	}else if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Hypixel") && premiumAddon != null){
            		if (cooldown.check(500) && canTP && Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && delay == 0 && mc.inGameHasFocus && getBlinkBlock().entityHit == null && !(getBlock(getBlinkBlock().getBlockPos()) instanceof BlockChest)) {
	                	cooldown.reset();
	                	event.setCancelled(true);
	                    endPos = getBlinkBlock().getBlockPos();

	                    if(endPos.getY()+1 > mc.thePlayer.posY){
	                    	ChatUtil.printChat(Command.chatPrefix + "§CPlease select a valid position.");
	                    	return;
	                    }
	                    endPos = new BlockPos(endPos.getX(), mc.thePlayer.posY, endPos.getZ());
	                    premiumAddon.onEvent(em);
	                    delay = 5;
	                    event.setCancelled(false);
	                }
            	}
                if (delay > 0) {
                    --delay;
                }
            }
        }
        if (event instanceof EventRender3D) {
            EventRender3D er = (EventRender3D) event;
            try {
                final int x = getBlinkBlock().getBlockPos().getX();
                final int y = getBlinkBlock().getBlockPos().getY();
                final int z = getBlinkBlock().getBlockPos().getZ();
                final Block block1 = getBlock(x, y, z);
                final Block block2 = getBlock(x, y + 1, z);
                final Block block3 = getBlock(x, y + 2, z);
                final boolean blockBelow = !(block1 instanceof BlockSign) && block1.getMaterial().isSolid();
                final boolean blockLevel = !(block2 instanceof BlockSign) && block1.getMaterial().isSolid();
                final boolean blockAbove = !(block3 instanceof BlockSign) && block1.getMaterial().isSolid();
                if (getBlock(getBlinkBlock().getBlockPos()).getMaterial() != Material.air && blockBelow && blockLevel && blockAbove && !(getBlock(getBlinkBlock().getBlockPos()) instanceof BlockChest)) {
                    canTP = true;
                    GL11.glPushMatrix();
                    RenderingUtil.pre3D();
                    mc.entityRenderer.setupCameraTransform(er.renderPartialTicks, 2);
                   
                    GL11.glColor4d(0, 0.6, 0, 0.25);
                    if(((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("hypixel") && premiumAddon != null){
                    	if(mc.thePlayer.posY < (y+1)){
                    		 GL11.glColor4d(0.6, 0, 0, 0.25);
                    	}
                    }
                    RenderingUtil.drawBoundingBox(new AxisAlignedBB(x - RenderManager.renderPosX, y - RenderManager.renderPosY, z - RenderManager.renderPosZ, x - RenderManager.renderPosX + 1.0, y + getBlock(getBlinkBlock().getBlockPos()).getBlockBoundsMaxY() - RenderManager.renderPosY, z - RenderManager.renderPosZ + 1.0));
                    GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    RenderingUtil.post3D();
                    GL11.glPopMatrix();
                } else {
                    canTP = false;
                }
            } catch (Exception e) {

            }
        }
    }

    public static Block getBlock(final int x, final int y, final int z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

}
