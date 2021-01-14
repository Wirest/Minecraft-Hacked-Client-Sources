package me.memewaredevs.client.module.player;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.PacketInEvent;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.packet.PacketUtil;
import me.memewaredevs.client.util.blocks.position.Vec3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.input.Keyboard.KEY_K;

public class Stealer extends Module {
    public Stealer() {
        super("Stealer", KEY_K, Category.PLAYER);
        this.addModes("Normal", "Nearby");
        this.addBoolean("Close", true);
        this.addDouble("Delay", 4, 0, 12);
    }
    private final List<BlockPos> emptiedChests = new ArrayList();

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = event -> {
        if (mc.currentScreen instanceof GuiChest) {
            if (!isEmpty(mc.thePlayer.openContainer)) {
                for (Slot slot : (List<Slot>) mc.thePlayer.openContainer.inventorySlots) {
                    if (slot != null && slot.getStack() != null && mc.thePlayer.ticksExisted % Math.round(this.getDouble("Delay")) == 0) {
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
                        return;
                    }
                }
            } else {
                if (this.getBool("Close") || isMode("Nearby")) {
                    mc.displayGuiScreen(null);
                }
            }
            return;
        }
        if (isMode("Nearby")) {
            int maxDist = 3;
            if (mc.currentScreen == null) {
                if (mc.thePlayer.ticksExisted % Math.round(this.getDouble("Delay")) == 0) {
                    for (int y = maxDist; y >= -maxDist; y--) {
                        for (int x = -maxDist; x < maxDist; x++) {
                            for (int z = -maxDist; z < maxDist; z++) {
                                int posX = ((int) Math.floor(mc.thePlayer.posX) + x);
                                int posY = ((int) Math.floor(mc.thePlayer.posY) + y);
                                int posZ = ((int) Math.floor(mc.thePlayer.posZ) + z);
                                if (mc.thePlayer.getDistanceSq(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z) <= 16.0) {
                                    Block block = mc.theWorld.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
                                    if (block instanceof BlockChest) {
                                        BlockPos pos = new BlockPos(posX, posY, posZ);
                                        if (!this.emptiedChests.contains(pos)) {
                                            this.emptiedChests.add(pos);
                                            PacketUtil.sendPacket(new C0APacketAnimation());
                                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), new BlockPos(posX, posY, posZ), EnumFacing.DOWN, new Vec3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };

    @Handler
    public Consumer<PacketInEvent> eventConsumer1 = event -> {
        if (event.getPacket() instanceof S08PacketPlayerPosLook && mc.getNetHandler() != null && mc.getNetHandler().doneLoadingTerrain) {
            emptiedChests.clear();
        }
    };

    @Override
    public void onEnable() {
        this.emptiedChests.clear();
    }

    public boolean isEmpty(final Container container) {
        boolean isEmpty = true;
        int maxSlot = (container.inventorySlots.size() == 90) ? 54 : 27;
        for (int i = 0; i < maxSlot; ++i) {
            if (container.getSlot(i).getHasStack()) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

}
