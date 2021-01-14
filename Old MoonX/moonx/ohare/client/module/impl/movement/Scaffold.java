package moonx.ohare.client.module.impl.movement;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.SafewalkEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.MoveUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    private List<Block> invalid;
    private TimerUtil timerMotion = new TimerUtil();
    private BlockData blockData;
    private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.NCP);
    private BooleanValue Switch = new BooleanValue("Switch", true);
    private BooleanValue Hypixel = new BooleanValue("Hypixel", true);
    private BooleanValue tower = new BooleanValue("Tower", true);
    private BooleanValue keepy = new BooleanValue("KeepY", true);
    private int NoigaY;

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, new Color(255, 155, 255, 255).getRGB());
        setDescription("Auto places blocks under you");
        invalid = Arrays.asList(Blocks.anvil, Blocks.wooden_pressure_plate,Blocks.stone_slab,Blocks.wooden_slab,Blocks.stone_slab2, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.sapling,
                Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.gravel);
    }

    public enum mode {
        NCP, CUBECRAFT, DEV
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        if (event.isPre()) {
            if (Mode.getValue() == mode.CUBECRAFT) {
                if (getMc().thePlayer.isMoving()) {
                    getMc().thePlayer.setSprinting(false);
                    MoveUtil.setSpeed(0.1);
                    if (getMc().thePlayer.ticksExisted % 2 == 0) {
                        //event.setY(event.getY() + 0.3333f);
                       // Printer.print("" + event.getY());
                       // event.setOnGround(false);
                    } else {
                       // event.setY(event.getY());
                      //  event.setOnGround(true);
                    }
                }
            }
            if (keepy.isEnabled()) {
                if ((!getMc().thePlayer.isMoving() && getMc().gameSettings.keyBindJump.isKeyDown()) || (getMc().thePlayer.isCollidedVertically || getMc().thePlayer.onGround)) {
                    NoigaY = MathHelper.floor_double(getMc().thePlayer.posY);
                }
            } else {
                NoigaY = MathHelper.floor_double(getMc().thePlayer.posY);
            }
            blockData = null;
            if (!getMc().thePlayer.isSneaking()) {
                BlockPos blockBelow = new BlockPos(getMc().thePlayer.posX, NoigaY - 1, getMc().thePlayer.posZ);
                if (Math.abs(getMc().thePlayer.motionX) > 0 && Math.abs(getMc().thePlayer.motionZ) > 0) {
                    blockBelow = new BlockPos(getMc().thePlayer.posX, NoigaY - 1.0, getMc().thePlayer.posZ);
                }
                if (getMc().theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) {
                    blockData = getBlockData2(blockBelow);
                    if (blockData != null) {
                        float pitch = aimAtLocation(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ())[1];
                        float yaw = aimAtLocation(blockData.position.getX(), blockData.position.getY(), blockData.position.getZ())[0];
                        event.setPitch(pitch);
                        event.setYaw(yaw);
                    }
                }
            }
        } else {
            if (blockData != null) {
                if (getBlockCount() <= 0 || (!Switch.isEnabled() && getMc().thePlayer.getCurrentEquippedItem() != null && !(getMc().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock))) {
                    return;
                }
                final int heldItem = getMc().thePlayer.inventory.currentItem;
                boolean hasBlock = false;
                if (Switch.isEnabled()) {
                    for (int i = 0; i < 9; ++i) {
                        if (getMc().thePlayer.inventory.getStackInSlot(i) != null && getMc().thePlayer.inventory.getStackInSlot(i).stackSize != 0 && getMc().thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) getMc().thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
                            getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem = i));
                            hasBlock = true;
                            break;
                        }
                    }
                    if (!hasBlock) {
                        for (int i = 0; i < 45; ++i) {
                            if (getMc().thePlayer.inventory.getStackInSlot(i) != null && getMc().thePlayer.inventory.getStackInSlot(i).stackSize != 0 && getMc().thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && !invalid.contains(((ItemBlock) getMc().thePlayer.inventory.getStackInSlot(i).getItem()).getBlock())) {
                                getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 8, 2, getMc().thePlayer);
                                break;
                            }
                        }
                    }
                }
                switch (Mode.getValue()) {
                    case NCP:
                        if (tower.isEnabled()) {
                            if (getMc().gameSettings.keyBindJump.isKeyDown() && !getMc().thePlayer.isMoving() && !getMc().thePlayer.isPotionActive(Potion.jump)) {
                                getMc().thePlayer.motionY = 0.42F;
                                getMc().thePlayer.motionX = Minecraft.getMinecraft().thePlayer.motionZ = 0;
                                if (timerMotion.sleep(1500)) {
                                    getMc().thePlayer.motionY = -0.28f;
                                }
                            } else {
                                timerMotion.reset();
                            }
                        }
                        break;
                    case CUBECRAFT:
                        if (tower.isEnabled()) {
                            if (getMc().gameSettings.keyBindJump.isKeyDown() && !getMc().thePlayer.isMoving()) {
                                getMc().thePlayer.motionX = Minecraft.getMinecraft().thePlayer.motionZ = 0;
                                if (timerMotion.sleep(55)) {
                                    getMc().thePlayer.motionY = 0.75F;
                                }
                            } else {
                                timerMotion.reset();
                                if (getMc().thePlayer.motionY > 0.5) {
                                    getMc().thePlayer.motionY = 0F;
                                }
                            }
                        }
                        break;
                    case DEV:
                        break;
                }
                if (Hypixel.getValue()) {
                    getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem(), blockData.position, blockData.face, new Vec3(blockData.position.getX() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, blockData.position.getY() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9, blockData.position.getZ() + MathUtils.getRandom(100000000, 800000000) * 1.0E-9));
                } else {
                    getMc().playerController.onPlayerRightClick(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem(), blockData.position, blockData.face, new Vec3(blockData.position.getX() + Math.random(), blockData.position.getY() + Math.random(), blockData.position.getZ() + Math.random()));
                }
                getMc().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                if (Switch.isEnabled()) {
                    getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(getMc().thePlayer.inventory.currentItem = heldItem));
                }
            }
        }
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || invalid.contains(((ItemBlock) item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        ScaledResolution sr = new ScaledResolution(getMc());
        hud.fontValue.getValue().drawStringWithShadow(Integer.toString(getBlockCount()), sr.getScaledWidth() / 2 + 1 - hud.fontValue.getValue().getStringWidth(Integer.toString(getBlockCount())) / 2, sr.getScaledHeight() / 2 + 24, getBlockColor(getBlockCount()));
    }

    @Handler
    public void onSafewalk(SafewalkEvent event) {
        if (getMc().thePlayer != null)
            event.setCanceled(keepy.isEnabled() ? (!getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.onGround) : getMc().thePlayer.onGround);
    }

    public BlockData getBlockData2(BlockPos pos) {
        if (!invalid.contains(getMc().theWorld.getBlockState((pos.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos1.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos2.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos3.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos4.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos5.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos5.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos5.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos5.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos5.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos6.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos6.add(-1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos6.add(1, 0, 0))).getBlock())) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos6.add(0, 0, 1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState((pos6.add(0, 0, -1))).getBlock())) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (!invalid.contains(getMc().theWorld.getBlockState((pos7.add(0, -1, 0))).getBlock())) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos7.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos7.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos7.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos7.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (!invalid.contains(getMc().theWorld.getBlockState(pos8.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos8.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos8.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos8.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos8.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (!invalid.contains(getMc().theWorld.getBlockState(pos9.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos9.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos9.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos9.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (!invalid.contains(getMc().theWorld.getBlockState(pos9.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private int getBlockColor(int count) {
        float f = count;
        float f1 = 64;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }

    private float[] aimAtLocation(double positionX, double positionY, double positionZ) {
        double x = positionX - getMc().thePlayer.posX;
        double y = positionY - getMc().thePlayer.posY;
        double z = positionZ - getMc().thePlayer.posZ;
        double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[]{(float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f, (float) (-(Math.atan2(y, distance) * 180.0 / 3.141592653589793)), (float) (-(Math.atan2(y, distance) * 180.0 / 3.141592653589793))};
    }

    @Override
    public void onEnable() {
        if (getMc().theWorld != null) {
            timerMotion.reset();
            NoigaY = MathHelper.floor_double(getMc().thePlayer.posY);
        }
    }

    public class BlockData {
        public BlockPos position;
        public EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }
    }
}