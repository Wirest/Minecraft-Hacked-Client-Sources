package cedo.modules.movement;

import cedo.Fan;
import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventSafeWalk;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.modules.combat.Killaura;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.Timer;
import cedo.util.movement.MovementUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    public static float yaw;
    public static float pitch;
    private final Timer timerTower = new Timer();
    private final List<Block> invalid = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
            Blocks.command_block, Blocks.enchanting_table, Blocks.chest, Blocks.crafting_table, Blocks.furnace, Blocks.noteblock);
    public float lastYaw, lastPitch;
    public Timer timer = new Timer();
    public BlockData lastBlockData;
    public BooleanSetting //safeWalk = new BooleanSetting("Safewalk", true),
            boost = new BooleanSetting("Boost", true);//,
    //noSwing = new BooleanSetting("NoSwing", false),
    //rotations = new BooleanSetting("Rotations", false),
    //keepRotations = new BooleanSetting("Keep Rotations", true),
    //eagle = new BooleanSetting("Eagle", false),
    //disableSprint = new BooleanSetting("Disable Sprint", true);
    //public ModeSetting //spoofMode = new ModeSetting("Spoof", "Switch", "Off", "Switch", "Hold"),
    //towerMode = new ModeSetting("Tower", "Pause", "Off", "Pause", "Full");
    public NumberSetting delay = new NumberSetting("Place Delay", 50, 0, 500, 10),
            boostDuration = new NumberSetting("Boost Duration", 1500, 0, 3000, 50),
    //speed = new NumberSetting("Move Speed", 0.05, 0, 0.2, 0.01),
    placeRandomization = new NumberSetting("Randomization", 50, 0, 100, 10);
    public BooleanSetting toggleFly = new BooleanSetting("Toggle Fly", true);
    public boolean grounded;
    public boolean headTurned;
    public double vectorOne, vectorTwo, vectorThree;
    int returnSlot, packetSlot, switchTicks;
    int itemStackSize;
    int currentSlot;
    int currentItem;
    double groundy = 0;
    Timer timerMotion = new Timer(), boostCap = new Timer();
    private int slot;

    public Scaffold() {
        super("Scaffold", "Places blocks under the player", Keyboard.KEY_G, Category.MOVEMENT);
        this.addSettings(boost, /*rotations, safeWalk, noSwing, eagle, keepRotations, disableSprint, spoofMode, towerMode, speed,*/ delay, /*placeRandomization,*/ boostDuration, toggleFly);

        setScaffoldValues();
    }

    public static double range(final double n, final double n2) {
        return Math.random() * (n - n2) + n2;
    }

    public void setScaffoldValues() {
        if (Wrapper.authorized) {
            vectorOne = 0.3;
            vectorTwo = 0.49;
            vectorThree = 0.5;
        }
    }

    @Override
    public void onEnable() {
        timer.reset();
        boostCap.reset();
        groundy = mc.thePlayer.posY;
        if (toggleFly.isEnabled())
            Fan.getModuleByName("Fly").setToggled(false);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
        timerTower.reset();
        super.onDisable();
        if (!mc.thePlayer.isSwingInProgress) return;
        mc.thePlayer.swingProgress = 0.0f;
        mc.thePlayer.swingProgressInt = 0;
        mc.thePlayer.isSwingInProgress = false;
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double) blockPos.getX() + 0.5 - mc.thePlayer.posX + (double) enumFacing.getFrontOffsetX() / 2.0;
        double d2 = (double) blockPos.getZ() + 0.5 - mc.thePlayer.posZ + (double) enumFacing.getFrontOffsetZ() / 2.0;
        double d3 = mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() - ((double) blockPos.getY() + 0.5);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float) (Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float) (Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f >= 0.0f) return new float[]{f, f2};
        f += 360.0f;
        return new float[]{f, f2};
    }

    protected void swap(int n, int n2) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, n, n2, 2, mc.thePlayer);
    }

    public void onEvent(Event ev) {
        if (ev instanceof EventSafeWalk) {
            if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                ev.setCancelled(true);
            }
        }
        if (ev instanceof EventUpdate) {
            if (mc.thePlayer.swingProgress <= 0.0f && !mc.thePlayer.isEating() && !Fan.getModule(Killaura.class).isToggled()) {
                mc.thePlayer.swingProgressInt = 5;
            }
        }
        if (ev instanceof EventPacket) {
            if (ev.isIncoming() && ev.isPre()) {
                EventPacket e = (EventPacket) ev;

                Packet packet = e.getPacket();
                int k;
                if (packet instanceof S08PacketPlayerPosLook) {
                    boostCap.setTime(0);
                }
                if (packet instanceof S2FPacketSetSlot) {

                    e.setCancelled(true);

                    S2FPacketSetSlot localS2FPacketSetSlot = (S2FPacketSetSlot) packet;
                    k = localS2FPacketSetSlot.func_149173_d();
                    ItemStack localItemStack1 = localS2FPacketSetSlot.func_149174_e();
                    if (k != -1) {
                        this.currentSlot = k;
                        if (localItemStack1 != null) {
                            this.itemStackSize = localItemStack1.stackSize;
                        } else {
                            if (mc.thePlayer.inventoryContainer.getSlot(k).getStack() != null) {
                                mc.thePlayer.inventoryContainer.getSlot(k).putStack(null);
                            }
                            this.itemStackSize = 0;
                        }
                        mc.playerController.updateController();
                    }
                }
            }
        }

        if (!(ev instanceof EventMotion))
            return;

        EventMotion e = (EventMotion) ev;
        e.setPitch(79.44F);

        int k = 0;
        if (hasBlocksInHotbarHypixel()) {
            ItemStack bow = new ItemStack(Item.getItemById(261));
            for (int i = 9; i < 36; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {

                    if ((mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock) && isValidItemHypixel(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) {
                        for (int m = 36; m < 45; m++) {
                            if (Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(m), bow, true)) {
                                swap(i, m - 36);
                                k++;
                                break;
                            }
                        }
                        if (k != 0) {
                            break;
                        }
                        swap(i, 7);
                        break;
                    }
                }
            }
        }

        mc.thePlayer.lastReportedPitch = 999.0F;

        if (!hasBlocksHypixel()) {
            return;
        }
        double d1 = mc.thePlayer.posX;
        double d2 = mc.thePlayer.posZ;
        double d3 = mc.thePlayer.movementInput.moveForward;
        double d4 = mc.thePlayer.movementInput.moveStrafe;
        float f = mc.thePlayer.rotationYaw;

        if (!mc.thePlayer.isCollidedHorizontally) {
            d1 += (d3 * 0.45D * Math.cos(Math.toRadians(f + 90.0F)) + d4 * 0.45D * Math.sin(Math.toRadians(f + 90.0F))) * 1.0D;
            d2 += (d3 * 0.45D * Math.sin(Math.toRadians(f + 90.0F)) - d4 * 0.45D * Math.cos(Math.toRadians(f + 90.0F))) * 1.0D;

        }
        BlockPos localBlockPos = new BlockPos(d1, mc.thePlayer.posY - 1.0D - (mc.thePlayer.movementInput.sneak ? 1 : 0), d2);
        BlockData localBlockData = getBlockDataHypixel(localBlockPos);
        if (e.isPre()) {
            eventMoveHypixel(e);

            if ((mc.gameSettings.keyBindJump.isKeyDown()) && (mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
                eventMotionHypixel(e);

            }
        }
        if ((localBlockData != null)) {
            if (e.isPre()) {
                float[] arrayOfFloat = getRotationsHypixel(localBlockData.position, localBlockData.face);

                headTurned = true;
                yaw = arrayOfFloat[0];
                pitch = arrayOfFloat[1];
                // mc.thePlayer.rotationYaw = arrayOfFloat[0];
                // mc.thePlayer.rotationPitch = arrayOfFloat[1];

                // TODO rotations
                // e.setYaw(arrayOfFloat[0]);
                // e.setPitch(arrayOfFloat[1]);

                f = arrayOfFloat[0];
                if ((!mc.gameSettings.keyBindJump.isKeyDown()) && (mc.thePlayer.onGround)
                        && (isNotCollidingBelow(0.001D)) && (mc.thePlayer.isCollidedVertically)) {
                    // TODO ground spoof
                    e.setOnGround(false);

                    grounded = false;
                } else {
                    grounded = mc.thePlayer.onGround;
                }
            } else {
                if (!this.timerMotion.hasTimeElapsed((long) (delay.getValue() + Math.random() * 4), true)
                        && isNotCollidingBelow(0.01) && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }
                int n = mc.thePlayer.inventory.currentItem;
                updateHotbarHypixel();
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(),
                        localBlockData.position, localBlockData.face, blockDataToVec3(localBlockData));

                //MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed());
                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionX *= 1.3;
                        mc.thePlayer.motionZ *= 1.3;
                    }
                } else {
                    mc.timer.timerSpeed = 1;
                }
                if (boost.isEnabled()) {
                    mc.timer.timerSpeed = boostCap.hasTimeElapsed((long) boostDuration.getValue(), false) ? 1 : 1.5f;
                }

                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                mc.thePlayer.inventory.currentItem = n;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                mc.playerController.updateController();
                headTurned = false;

                Fan.statistics.increment("Scaffold Blocks Placed");

                this.timer.reset();
            }
        }

    }

    public boolean isBlockAccessible(Block block) {
        if (!block.getMaterial().isReplaceable()) return false;
        if (!(block instanceof BlockSnow)) return true;
        return block.getBlockBoundsMaxY() <= 0.125;
    }

    private Vec3 blockDataToVec3(BlockData data) {
        BlockPos pos = data.position;
        EnumFacing face = data.face;
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += (double) face.getFrontOffsetX() / 2;
        z += (double) face.getFrontOffsetZ() / 2;
        y += (double) face.getFrontOffsetY() / 2;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += range(vectorOne, -vectorOne);
            z += range(vectorOne, -vectorOne);
        } else {
            y += range(vectorTwo, vectorThree);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += range(vectorOne, -vectorOne);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += range(vectorOne, -vectorOne);
        }
        return new Vec3(x, y, z);
    }

    public void eventMotion(EventMotion eventMotion) {
        BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        Block block = mc.theWorld.getBlockState(blockPos).getBlock();
        BlockData blockData = this.getBlockData(blockPos);

        if (!this.isBlockAccessible(block)) return;
        if (blockData == null) return;
        mc.thePlayer.motionY = 0.4196;
        mc.thePlayer.motionX *= 0.0;
        mc.thePlayer.motionZ *= 0.0;
        return;

    }

    public void placeBlock() {

        BlockPos at = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);

        // check for block to place at
        if (mc.theWorld.getBlockState(at.add(0, 1, 0)).getBlock() != Blocks.air) {
            return;
        }

        // play arm animation
        mc.thePlayer.swingItem();

        // place block for client
        IBlockState ibl = ((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack().getItem()).getBlock().getDefaultState();
        mc.theWorld.setBlockState(at.add(0, 1, 0), ibl);

        // place block for server
        C08PacketPlayerBlockPlacement bl = new C08PacketPlayerBlockPlacement(at, 1,
                mc.thePlayer.inventoryContainer.getSlot(36 + this.slot).getStack(), 0, 0, 0);
        mc.thePlayer.sendQueue.addToSendQueue(bl);
    }

    public boolean canBeClicked(BlockPos pos) {
        return this.getBlock(pos).canCollideCheck(this.getState(pos), false);
    }

    public IBlockState getState(BlockPos pos) {
        return mc.theWorld.getBlockState(pos);
    }

    public Block getBlock(BlockPos pos) {
        return this.getState(pos).getBlock();
    }

    public Material getMaterial(BlockPos pos) {
        return this.getBlock(pos).getMaterial();
    }

    public void jump() {
        mc.thePlayer.motionY = 0.42f;
    }

    public BlockData getBlockData(BlockPos pos) {
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH);
        }
        return null;
    }

    public Block getBlock(int x, int y, int z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public int getCount() {
        int counter = 0;
        int i = 0;
        while (i < 36) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                Item item = is.getItem();

                if (item instanceof ItemBlock) {
                    ++counter;
                }
            }
            ++i;
        }
        return counter;
    }

    public BlockData checkArea(BlockPos pos) {
        if (isValidBlock(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isValidBlock(pos.add(0, 1, 0))) {
            return new BlockData(pos.add(0, 1, 0), EnumFacing.DOWN);
        }
        if (isValidBlock(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isValidBlock(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isValidBlock(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isValidBlock(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private BlockData getBlockDataHypixel(BlockPos paramBlockPos) {
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

    private boolean isValidBlock(BlockPos paramBlockPos) {
        Block localBlock = mc.theWorld.getBlockState(paramBlockPos).getBlock();
        if ((localBlock.getMaterial().isSolid()) || (!localBlock.isTranslucent()) || (localBlock.isVisuallyOpaque()) || ((localBlock instanceof BlockLadder)) || ((localBlock instanceof BlockCarpet)) || ((localBlock instanceof BlockSnow)) || ((localBlock instanceof BlockSkull))) {
            return !localBlock.getMaterial().isLiquid();
        }
        return false;
    }

    public void scaffoldMove(double speed) {
        float f1 = mc.thePlayer.rotationYaw * 0.017453292F;
        float f2 = mc.thePlayer.rotationYaw * 0.017453292F - 4.712389F;
        float f3 = mc.thePlayer.rotationYaw * 0.017453292F + 4.712389F;
        float f4 = mc.thePlayer.rotationYaw * 0.017453292F + 0.5969026F;
        float f5 = mc.thePlayer.rotationYaw * 0.017453292F + -0.5969026F;
        float f6 = mc.thePlayer.rotationYaw * 0.017453292F - 2.3876104F;
        float f7 = mc.thePlayer.rotationYaw * 0.017453292F - -2.3876104F;
        if ((mc.gameSettings.keyBindForward.pressed) && (!isMoving())) {
            if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed)) {
                mc.thePlayer.motionX -= MathHelper.sin(f5) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(f5) * speed;
            } else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed)) {
                mc.thePlayer.motionX -= MathHelper.sin(f4) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(f4) * speed;
            } else {
                mc.thePlayer.motionX -= MathHelper.sin(f1) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(f1) * speed;
            }
        } else if ((mc.gameSettings.keyBindBack.pressed) && (!isMoving())) {
            if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed)) {
                mc.thePlayer.motionX -= MathHelper.sin(f6) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(f6) * speed;
            } else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed)) {
                mc.thePlayer.motionX -= MathHelper.sin(f7) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(f7) * speed;
            } else {
                mc.thePlayer.motionX += MathHelper.sin(f1) * speed;
                mc.thePlayer.motionZ -= MathHelper.cos(f1) * speed;
            }
        } else if ((mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindForward.pressed) && (!mc.gameSettings.keyBindBack.pressed) && (!isMoving())) {
            mc.thePlayer.motionX += MathHelper.sin(f2) * speed;
            mc.thePlayer.motionZ -= MathHelper.cos(f2) * speed;
        } else if ((mc.gameSettings.keyBindRight.pressed) && (!mc.gameSettings.keyBindLeft.pressed) && (!mc.gameSettings.keyBindForward.pressed) && (!mc.gameSettings.keyBindBack.pressed) && (!isMoving())) {
            mc.thePlayer.motionX += MathHelper.sin(f3) * speed;
            mc.thePlayer.motionZ -= MathHelper.cos(f3) * speed;
        }
    }

    public boolean isMoving() {
        if ((mc.gameSettings.keyBindForward.isPressed()) || (mc.gameSettings.keyBindBack.isPressed()) || (mc.gameSettings.keyBindLeft.isPressed()) || (mc.gameSettings.keyBindRight.isPressed())) {
            return false;
        }
        return false;
    }

    public boolean isNotCollidingBelow(double paramDouble) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -paramDouble, 0.0)).isEmpty();
    }

    private boolean hasBlocksInHotbarHypixel() {
        for (int i = 36; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                Item localItem = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                if (((localItem instanceof ItemBlock)) && (isValidItemHypixel(localItem))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidItemHypixel(Item paramItem) {
        if (!(paramItem instanceof ItemBlock)) {
            return false;
        }
        ItemBlock localItemBlock = (ItemBlock) paramItem;
        Block localBlock = localItemBlock.getBlock();
        return !invalid.contains(localBlock);
    }

    private boolean hasBlocksHypixel() {
        int i = 36;
        while (i < 45) {
            try {
                ItemStack localItemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((localItemStack == null) || (localItemStack.getItem() == null) || (!(localItemStack.getItem() instanceof ItemBlock)) || (!isValidItemHypixel(localItemStack.getItem()))) {
                    i++;
                } else {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public boolean isBlockAccessibleHypixel(Block block) {
        if (block.getMaterial().isReplaceable()) {
            return (!(block instanceof BlockSnow)) || (!(block.getBlockBoundsMaxY() > 0.125D));
        }
        return false;
    }

    private void updateHotbarHypixel() {
        ItemStack localItemStack = new ItemStack(Item.getItemById(261));
        try {
            for (int i = 36; i < 45; i++) {
                int j = i - 36;
                if ((!Container.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(i), localItemStack, true))
                        && ((mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock)) && (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null)
                        && (isValidItemHypixel(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) && (mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize != 0)) {
                    if ((this.currentSlot == j) && (this.itemStackSize == 0)) {
                        this.itemStackSize = 120;
                        return;
                    }
                    if (mc.thePlayer.inventory.currentItem == j) {
                        break;
                    }
                    mc.thePlayer.inventory.currentItem = j;
                    this.currentItem = j;
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    mc.playerController.updateController();
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public float[] getRotationsHypixel(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        double d1 = paramBlockPos.getX() + 0.5D - mc.thePlayer.posX + paramEnumFacing.getFrontOffsetX() / 2.0D;
        double d2 = paramBlockPos.getZ() + 0.5D - mc.thePlayer.posZ + paramEnumFacing.getFrontOffsetZ() / 2.0D;
        double d3 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (paramBlockPos.getY() + 0.5D);
        double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
        float f1 = (float) (Math.atan2(d2, d1) * 180.0D / Math.PI) - 90.0F;
        float f2 = (float) (Math.atan2(d3, d4) * 180.0D / Math.PI);
        if (f1 < 0.0F) {
            f1 += 360.0F;
        }
        return new float[]{f1, f2};
    }

    public void eventMoveHypixel(EventMotion paramEventMotion) {
        if (!Fan.getModule(Speed.class).isToggled()) {
            double value = mc.thePlayer.rotationYaw;
            double start = -180;
            double end = 180;

            double width = end - start;   //
            double offsetValue = value - start;   // value relative to 0

            double newYaw = (offsetValue - (Math.floor(offsetValue / width) * width)) + start;
            if (mc.thePlayer.movementInput.moveStrafe == 0 && mc.thePlayer.movementInput.moveForward > 0 && (((newYaw > 170 && newYaw < 180) || (newYaw < -170 && newYaw > -180)) || (newYaw > -100 && newYaw < -80) || (newYaw > 80 && newYaw < 100) || ((newYaw > -10 && newYaw < 0) || (newYaw < 10 && newYaw > 0)))) {
                //CleanClient.addChatMessage("Faster");
                if (!mc.thePlayer.onGround) {
                    //mc.thePlayer.setSpeed(0.085f);
                    //mc.thePlayer.motionX *= 0.4D;
                    //mc.thePlayer.motionZ *= 0.4D;
                } else {
                    //mc.thePlayer.motionX *= 0.0D;
                    // mc.thePlayer.motionZ *= 0.0D;
                }
            } else {
                //CleanClient.addChatMessage("Slower " + mc.thePlayer.rotationYaw);
                mc.thePlayer.motionX *= 0.0D;
                mc.thePlayer.motionZ *= 0.0D;

                if ((mc.thePlayer.onGround) && (mc.thePlayer.isCollidedVertically) && (isNotCollidingBelow(0.01D))) {
                    scaffoldMove(0.14D);
                } else {
                    scaffoldMove(0.2D);
                }
            }
        }
    }

    public void eventMotionHypixel(EventMotion paramEventMotion) {
        BlockPos localBlockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        Block localBlock = mc.theWorld.getBlockState(localBlockPos).getBlock();
        BlockData localBlockData = getBlockDataHypixel(localBlockPos);

        if ((isBlockAccessibleHypixel(localBlock)) && (localBlockData != null)) {
            //if(tower.getName().equals("Normal")){
            mc.thePlayer.motionY = 0.4196D;
            //}
            mc.thePlayer.motionX *= 0.0D;
            mc.thePlayer.motionZ *= 0.0D;
        }
    }

    public static class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}
