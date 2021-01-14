package moonx.ohare.client.module.impl.player;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.WorldLoadEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.movement.Scaffold;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class ChestStealer extends Module {
    private TileEntityChest tileEntity;
    private List<TileEntityChest> openedChests = new ArrayList<>();
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 100, 0, 1000, 1);
    private BooleanValue aura = new BooleanValue("ChestAura", false);
    private NumberValue<Float> range = new NumberValue<>("AuraRange", 4.0f, 0.0f, 5.0f, 0.1f, aura, "true");
    private final TimerUtil timerUtil = new TimerUtil();
    private final TimerUtil FakeMiss = new TimerUtil();
    private final TimerUtil closedtimer = new TimerUtil();
    private final String[] list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play",
            "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user",
            "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele",
            "port", "map", "kit", "select", "lobby", "vault", "lock", "quick", "travel", "cake", "war", "pvp"};
    public ChestStealer() {
        super("ChestStealer", Category.PLAYER, new Color(255, 75, 170).getRGB());
        setRenderLabel("Chest Stealer");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (aura.isEnabled())
            tileEntity = getClosestChest();
            if (getMc().currentScreen instanceof GuiChest) {
                if (tileEntity != null) openedChests.add(tileEntity);
                GuiChest chest = (GuiChest) getMc().currentScreen;
                String name = chest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
                for (String BadName : list) {
                    if (name.contains(BadName))
                        return;
                }
                int rows = chest.getInventoryRows() * 9;
                for (int i = 0; i < rows; i++) {
                    Slot slot = chest.inventorySlots.getSlot(i);
                    if (slot.getHasStack()) {
                        if (timerUtil.reach(delay.getValue())) {
                            getMc().playerController.windowClick(chest.inventorySlots.windowId, slot.slotNumber, 0, 1, getMc().thePlayer);
                            timerUtil.reset();
                        } else {
                            if (FakeMiss.reach(300)) {
                                FakeMiss.reset();
                                getMc().playerController.windowClick(chest.inventorySlots.windowId, MathUtils.getRandomInRange(0, 25), 0, 1, getMc().thePlayer);
                            }
                        }
                    }
                }
                if (!hasItems(chest) || isInventoryFull()) {
                    if (closedtimer.reach(MathUtils.getRandomInRange(75, 150))) getMc().thePlayer.closeScreen();
                } else closedtimer.reset();
            } else if (aura.isEnabled()) {
                if (tileEntity != null) {
                    final float[] rots = calculateLookAt(tileEntity.getPos().getX() + 0.5f, tileEntity.getPos().getY() - 1, tileEntity.getPos().getZ() + 0.5f, getMc().thePlayer);
                    event.setPitch(rots[1]);
                    event.setYaw(rots[0]);
                }
            }
        } else {
            if (tileEntity != null && !(getMc().currentScreen instanceof GuiChest)) {
                getMc().getNetHandler().getNetworkManager().sendPacket( new C08PacketPlayerBlockPlacement(tileEntity.getPos(), 1, null, 0, 0, 0));
            }
        }
        if (getMc().currentScreen == null) closedtimer.reset();
    }

    @Handler
    public void onWorldLoad(WorldLoadEvent event) {
        openedChests.clear();
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = getMc().thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
    private TileEntityChest getClosestChest() {
        TileEntityChest entity = null;
        double maxDist = range.getValue() * range.getValue();
        for (TileEntity tileEntity : getMc().theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest && !openedChests.contains(tileEntity) && getMc().thePlayer.getDistanceSq(tileEntity.getPos()) < maxDist) {
                entity = (TileEntityChest) tileEntity;
                maxDist = getMc().thePlayer.getDistanceSq(entity.getPos());
            }
        }
        return entity;
    }

    private float[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new float[]{(float) yaw, (float) pitch};
    }

    private boolean hasItems(GuiChest chest) {
        int items = 0;
        int rows = chest.getInventoryRows() * 9;
        for (int i = 0; i < rows; i++) {
            Slot slot = chest.inventorySlots.getSlot(i);
            if (slot.getHasStack()) items++;
        }
        return items > 0;
    }
}
