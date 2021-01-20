package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.*;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.ColorManager;
import cedo.util.SpeedUtil;
import cedo.util.Timer;
import cedo.util.font.FontUtil;
import cedo.util.movement.MovementUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static net.minecraft.util.EnumFacing.*;

public class BadScaffold extends Module {
    //TODO: auto slot and inventory management (Have any slot selected initially)
    //TODO: Player is in way detection (don't attempt to place blocks on player)
    //TODO: Make this not place blocks ON snow, water

    public float lastYaw, lastPitch;
    public Timer timer = new Timer();
    public BlockData lastBlockData;
    public BooleanSetting safeWalk = new BooleanSetting("Safewalk", true),
            noSwing = new BooleanSetting("NoSwing", false),
            rotations = new BooleanSetting("Rotations", false),
            keepRotations = new BooleanSetting("Keep Rotations", true),
            eagle = new BooleanSetting("Eagle", false),
            disableSprint = new BooleanSetting("Disable Sprint", true);
    public ModeSetting spoofMode = new ModeSetting("Spoof", "Switch", "Off", "Switch", "Hold"),
            towerMode = new ModeSetting("Tower", "Pause", "Off", "Pause", "Full");
    public NumberSetting delay = new NumberSetting("Place Delay", 50, 0, 500, 10),
            speed = new NumberSetting("Move Speed", 0.05, 0, 0.2, 0.01),
            placeRandomization = new NumberSetting("Randomization", 50, 0, 100, 10);
    int returnSlot, packetSlot, switchTicks;

    public BadScaffold() {
        super("Scaffold", "Places blocks under the player", Keyboard.KEY_G, Category.MOVEMENT);
        this.addSettings(rotations, safeWalk, noSwing, eagle, keepRotations, disableSprint, spoofMode, towerMode, speed, delay, placeRandomization);
    }

    public void onEnable() {
        packetSlot = returnSlot = mc.thePlayer.inventory.currentItem;
        switchTicks = 0;

        if (lastYaw == 0)
            lastYaw = mc.thePlayer.rotationYaw;

        if (lastPitch == 0)
            lastPitch = mc.thePlayer.rotationPitch;
        super.onEnable();
    }

    public void onDisable() {
        if (mc.thePlayer.inventory.currentItem != packetSlot) {
            mc.thePlayer.sendQueue.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }

        if (!towerMode.is("Off") && mc.thePlayer.motionY > 0)
            SpeedUtil.setSpeed(0);
        mc.thePlayer.motionY = -0.28;
        mc.thePlayer.setPosition(mc.thePlayer.posX, (int) mc.thePlayer.posY, mc.thePlayer.posZ);
        super.onDisable();
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {
            EventRenderGUI event = (EventRenderGUI) e;
            ScaledResolution sr = event.sr;

            int amount = countHotbarBlocks();

            if (amount > 0) {
                ItemStack stack = spoofMode.is("Off") ? mc.thePlayer.getCurrentEquippedItem() : getHotbarStack();


                GL11.glPushMatrix();
                GL11.glDepthMask(true);
                GlStateManager.clear(256);
                RenderHelper.enableGUIStandardItemLighting();
                Minecraft.getMinecraft().getRenderItem().zLevel = -100.0f;
                GlStateManager.scale(1.0f, 1.0f, 0.7f);
                GlStateManager.enableDepth();
                Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (sr.getScaledWidth() / 2f - 8), (int) (sr.getScaledHeight() / 2f - 12 - 26) + 8);
                //mc.getRenderItem().renderItemOverlayIntoGUINameTags(mc.fontRendererObj, stack, x - 1, y + 10, null);
                Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableCull();
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.scale(0.5, 0.5, 0.5);
                GlStateManager.disableDepth();
                //NameTags.renderEnchantText(stack, x, y);
                GlStateManager.enableDepth();
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GL11.glPopMatrix();
            }

            FontUtil.clean.drawCenteredStringWithShadow(amount + " blocks", sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f - 12, ColorManager.rainbow((0), 10, 0.5f, 1, 0.8f).getRGB());
        }
        if (e instanceof EventRenderWorld) {
    		/*if(lastBlockData != null) {
    			RenderUtil.blockEsp(lastBlockData.getPos(), 1, 0.5, 0, 2);
    			BlockPos newBlock = lastBlockData.getPos().add(lastBlockData.getFace().getFrontOffsetX(), lastBlockData.getFace().getFrontOffsetY(), lastBlockData.getFace().getFrontOffsetZ()),
    					 predictedNextBlock = lastBlockData.getPos().add(lastBlockData.getFace().getFrontOffsetX()*2, lastBlockData.getFace().getFrontOffsetY()*2, lastBlockData.getFace().getFrontOffsetZ()*2);
    			
    			boolean cannotPlace = (mc.thePlayer.movementInput.jump && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, new AxisAlignedBB(lastBlockData.getPos(), newBlock.add(1,1,1))).size() < 2);
    			RenderUtil.blockEsp(newBlock, 1, 0.7, cannotPlace ? 1 : 0, 2);
    		}*/
        }
        if (e instanceof EventSafeWalk) {
            if (safeWalk.isEnabled())
                e.setCancelled(true);
        }
        if (e instanceof EventPacket)
            if (e.isPre()) {
                EventPacket event = (EventPacket) e;
                if (event.isOutgoing() && event.isPre() && event.getPacket() instanceof C09PacketHeldItemChange && !spoofMode.is("Off")) {
                    event.setCancelled(true);
                }
            }

        if (e instanceof EventMotion) {
            EventMotion event = (EventMotion) e;

            this.setSuffix(spoofMode.is("Off") ? null : spoofMode.getSelected());

            if (disableSprint.isEnabled())
                mc.thePlayer.setSprinting(false);

            if (e.isPre()) {
                if (keepRotations.isEnabled()) {
                    event.setYaw(lastYaw);
                    event.setPitch(lastPitch);
                    mc.thePlayer.prevRotationYawHead = mc.thePlayer.rotationYawHead = event.getYaw();
                }
                BlockPos player = new BlockPos(mc.thePlayer.posX - (eagle.isEnabled() ? mc.thePlayer.motionX : 0), mc.thePlayer.posY - 1, mc.thePlayer.posZ - (eagle.isEnabled() ? mc.thePlayer.motionZ : 0));
                BlockData blockData = getBlockDataTest(new BlockPos(player));
                ItemStack stack = spoofMode.is("Off") ? mc.thePlayer.getCurrentEquippedItem() : getHotbarStack();
                MovementInput input = mc.thePlayer.movementInput;
                boolean tower = !towerMode.is("Off") && (input.jump && !input.sneak && input.moveStrafe == 0 && input.moveForward == 0);

                if (stack == null)
                    return;

                Item item = stack.getItem();

                if (!(item instanceof ItemBlock))
                    return;

                if (spoofMode.is("Hold")) {
                    int targetSlot = getHotbarSlot();
                    if (packetSlot != targetSlot) {
                        packetSlot = targetSlot;
                        mc.thePlayer.sendQueue.sendPacketNoEvent(new C09PacketHeldItemChange(targetSlot));
                    }
                }

                if (blockData == null)
                    return;

                lastBlockData = blockData;

                if (mc.thePlayer.isCollidedHorizontally)
                    return;


                if (((ItemBlock) item).canPlaceBlockOnSide(mc.theWorld, blockData.getPos(), blockData.getFace(), mc.thePlayer, stack)) {
                    unableToScaffold();
                    return;
                }

                if (!tower && delay.getValue() != 0 && !timer.hasTimeElapsed((long) Math.max(delay.getMin(), (delay.getValue() + (Math.random() - 0.5) * placeRandomization.getValue())), true)) {
                    unableToScaffold();
                    return;
                }

                if (spoofMode.is("Switch")) {
                    int targetSlot = getHotbarSlot();
                    if (packetSlot != targetSlot) {
                        packetSlot = targetSlot;
                        mc.thePlayer.sendQueue.sendPacketNoEvent(new C09PacketHeldItemChange(packetSlot));
                        switchTicks = 0;
                    }
                }

                float[] rotations = getFaceRotation(blockData);

                if (this.rotations.isEnabled()) {
                    event.setYaw(rotations[0]);
                    event.setPitch(rotations[1]);
                }

                mc.thePlayer.prevRotationYawHead = mc.thePlayer.rotationYawHead = event.getYaw();
                lastYaw = event.getYaw();
                lastPitch = event.getPitch();

                //mc.thePlayer.rotationYaw = event.getYaw();
                // mc.thePlayer.rotationPitch = event.getPitch();

                if (!noSwing.isEnabled())
                    mc.thePlayer.swingItem();
                else
                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());

                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, blockData.getPos(), blockData.getFace(), blockData.getHitVec());
                if (speed.getValue() > 0 && MovementUtil.isMoving()) {
                    MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + speed.getValue());
                }

                mc.playerController.updateController();


                if (tower) {
                    SpeedUtil.setSpeed(0);
                    mc.thePlayer.motionY = -0.28;
                    if (towerMode.is("Pause"))
                        mc.thePlayer.setPosition(mc.thePlayer.posX, (int) mc.thePlayer.posY, mc.thePlayer.posZ);

                    mc.thePlayer.jump();
                }

            }
        }
    }

    public void unableToScaffold() {
        if (spoofMode.is("Switch")) {
            if (mc.thePlayer.inventory.currentItem != packetSlot) {
                mc.thePlayer.sendQueue.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                packetSlot = mc.thePlayer.inventory.currentItem;
            }
        }
    }

    public int getHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            Slot stack = mc.thePlayer.inventoryContainer.getSlot(36 + i);

            if (stack.getHasStack()) {
                if (stack.getStack().getItem() instanceof ItemBlock)
                    return i;
            }
        }

        return -1;
    }

    public ItemStack getHotbarStack() {
        for (int i = 0; i < 9; i++) {
            Slot stack = mc.thePlayer.inventoryContainer.getSlot(36 + i);

            if (stack.getHasStack()) {
                if (stack.getStack().getItem() instanceof ItemBlock)
                    return stack.getStack();
            }
        }

        return null;
    }

    public int countHotbarBlocks() {
        int size = 0;
        for (int i = 0; i < 9; i++) {
            Slot stack = mc.thePlayer.inventoryContainer.getSlot(36 + i);

            if (stack.getHasStack()) {
                if (stack.getStack().getItem() instanceof ItemBlock)
                    size += stack.getStack().stackSize;
            }
        }

        return size;
    }

    public BlockData getBlockData(BlockPos pos) {
        BlockPos east = new BlockPos(-1, 0, 0),
                west = new BlockPos(1, 0, 0),
                north = new BlockPos(0, 0, 1),
                south = new BlockPos(0, 0, -1);

        if (canPlaceHere(pos.add(east)))
            return new BlockData(pos.add(east), EAST);

        if (canPlaceHere(pos.add(west)))
            return new BlockData(pos.add(west), WEST);

        if (canPlaceHere(pos.add(south)))
            return new BlockData(pos.add(south), SOUTH);

        if (canPlaceHere(pos.add(north)))
            return new BlockData(pos.add(north), NORTH);


        return null;
    }

    public BlockData getDiagonalBlockData(BlockPos pos) {
        BlockPos up = new BlockPos(0, -1, 0),
                east = new BlockPos(-1, 0, 0),
                west = new BlockPos(1, 0, 0),
                north = new BlockPos(0, 0, 1),
                south = new BlockPos(0, 0, -1);

        if (canPlaceHere(pos.add(up)))
            return new BlockData(pos.add(up), UP);

        if (canPlaceHere(pos.add(east)))
            return new BlockData(pos.add(east), EAST);

        if (canPlaceHere(pos.add(west)))
            return new BlockData(pos.add(west), WEST);

        if (canPlaceHere(pos.add(south)))
            return new BlockData(pos.add(south), SOUTH);

        if (canPlaceHere(pos.add(north)))
            return new BlockData(pos.add(north), NORTH);

        BlockPos[] positions = {east, west, south, north};

        BlockData data = null;

        for (BlockPos offset : positions) {
            if ((data = getBlockData(pos.add(offset))) != null)
                return data;
        }

        for (BlockPos offset1 : positions)
            for (BlockPos offset2 : positions)
                if ((data = getBlockData(pos.add(offset1).add(offset2))) != null)
                    return data;

    	/*for(BlockPos offset1 : positions)
	    	for(BlockPos offset2 : positions)
		    	for(BlockPos offset3 : positions)
		        	if((data = getBlockData(pos.add(offset1).add(offset2).add(offset3))) != null)
		        		return data;*/


        return null;
    }

    public BlockData checkArea(BlockPos pos) {
        if (canPlaceHere(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), UP);
        }
        if (canPlaceHere(pos.add(0, 1, 0))) {
            return new BlockData(pos.add(0, 1, 0), DOWN);
        }
        if (canPlaceHere(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EAST);
        }
        if (canPlaceHere(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), WEST);
        }
        if (canPlaceHere(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), NORTH);
        }
        if (canPlaceHere(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), SOUTH);
        }
        return null;
    }

    private BlockData getBlockDataTest(BlockPos paramBlockPos) {
        if (checkArea(paramBlockPos) != null) {
            return checkArea(paramBlockPos);
        }
        if (checkArea(paramBlockPos.add(-1, 0, 0)) != null) {
            return checkArea(paramBlockPos.add(-1, 0, 0));
        }
        if (checkArea(paramBlockPos.add(1, 0, 0)) != null) {
            return checkArea(paramBlockPos.add(1, 0, 0));
        }
        if (checkArea(paramBlockPos.add(0, 0, 1)) != null) {
            return checkArea(paramBlockPos.add(0, 0, 1));
        }
        if (checkArea(paramBlockPos.add(0, 0, -1)) != null) {
            return checkArea(paramBlockPos.add(0, 0, -1));
        }
        if (checkArea(paramBlockPos.add(0, -1, 0)) != null) {
            return checkArea(paramBlockPos.add(0, -1, 0));
        }
        if (checkArea(paramBlockPos.add(0, -1, 0).add(1, 0, 0)) != null) {
            return checkArea(paramBlockPos.add(0, -1, 0).add(1, 0, 0));
        }
        if (checkArea(paramBlockPos.add(0, -1, 0).add(-1, 0, 0)) != null) {
            return checkArea(paramBlockPos.add(0, -1, 0).add(-1, 0, 0));
        }
        if (checkArea(paramBlockPos.add(0, -1, 0).add(0, 0, 1)) != null) {
            return checkArea(paramBlockPos.add(0, -1, 0).add(0, 0, 1));
        }
        if (checkArea(paramBlockPos.add(0, -1, 0).add(0, 0, -1)) != null) {
            return checkArea(paramBlockPos.add(0, -1, 0).add(0, 0, -1));
        }
        return null;
    }

    public boolean canPlaceHere(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return /*block.getMaterial().isSolid() && block.isOpaqueCube() && !block.getMaterial().isLiquid() && block.isVisuallyOpaque() && */block != Blocks.air;
    }

    private float[] getFaceRotation(BlockData blockData) {
        BlockPos pos = blockData.getPos();
        EnumFacing facing = blockData.getFace();

        double posX = pos.getX() + 0.5 - mc.thePlayer.posX + facing.getFrontOffsetX() / 2D,
                posY = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (pos.getY() + 0.5),
                posZ = pos.getZ() + 0.5 - mc.thePlayer.posZ + facing.getFrontOffsetZ() / 2D,
                distance = MathHelper.sqrt_double(posX * posX + posZ * posZ);

        float yaw = (float) (Math.atan2(posZ, posX) * 180D / Math.PI) - 90 + (float) (Math.random() * 3 - 1.5),
                pitch = (float) (Math.atan2(posY, distance) * 180D / Math.PI) + (float) (Math.random() * 3 - 1.5);

        if (yaw < 0)
            yaw += 360;

        return new float[]{yaw, /*80*/(float) (Math.random() * 2D + 85)};
    }

    public static class BlockData {
        BlockPos pos;
        EnumFacing face;

        public BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }

        public BlockPos getPos() {
            return pos;
        }

        public void setPos(BlockPos pos) {
            this.pos = pos;
        }

        public EnumFacing getFace() {
            return face;
        }

        public void setFace(EnumFacing face) {
            this.face = face;
        }

        public Vec3 getHitVec() {
            double x = pos.getX() + 0.5 + face.getFrontOffsetX() / 2D,
                    y = pos.getY() + 0.5 + face.getFrontOffsetZ() / 2D,
                    z = pos.getZ() + 0.5 + face.getFrontOffsetY() / 2D;

            if (face == UP || face == DOWN) {
                x += Math.random() * 0.5 - 0.25;
                z += Math.random() * 0.5 - 0.25;
            } else
                y += Math.random() * 0.5 - 0.25;

            if (face == NORTH || face == SOUTH)
                x += Math.random() * 0.5 - 0.25;

            if (face == EAST || face == WEST)
                z += Math.random() * 0.5 - 0.25;

            return new Vec3(x, y, z);
        }
    }
}
