/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.world;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import java.util.List;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

@Module.Mod(category=Module.Category.WORLD, description="Steal from chests automatically", key=0, name="ChestStealer")
public class ChestStealer
extends Module {
    private final Value delay = new Value("cheststealer_Delay", Float.valueOf(65.0f), Float.valueOf(0.0f), Float.valueOf(125.0f), Float.valueOf(1.0f));
    public static Value smart = new Value("cheststealer_Smart", true);
    Timer timer = new Timer();

    @EventTarget
    public void onEvent(EventPreMotionUpdates event) {
        this.timer.update();
        this.setDisplayName("Chest Stealer [" + this.delay.value.longValue() + "]");
        if (ChestStealer.smart.boolvalue) {
            if (Wrapper.mc().objectMouseOver == null) {
                return;
            }
            if (Wrapper.mc().objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
                return;
            }
            if (!(Wrapper.getBlock(Wrapper.mc().objectMouseOver.getBlockPos()) instanceof BlockChest) && !(Wrapper.getBlock(Wrapper.mc().objectMouseOver.getBlockPos()) instanceof BlockEnderChest)) {
                return;
            }
        }
        if (Wrapper.mc().player.openContainer != null && Wrapper.mc().player.openContainer instanceof ContainerChest) {
            ContainerChest container = (ContainerChest)Wrapper.mc().player.openContainer;
            if (this.isEmpty(container)) {
                Wrapper.getPlayer().closeScreen();
            }
            int i = 0;
            while (i < container.getLowerChestInventory().getSizeInventory()) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null && this.timer.hasPassed(this.delay.value.longValue())) {
                    Wrapper.mc().playerController.windowClick(container.windowId, i, 0, ClickType.QUICK_MOVE, Wrapper.getPlayer());
                    this.timer.reset();
                }
                ++i;
            }
        }
    }

    public boolean isEmpty(Container container) {
        int i = 0;
        int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        while (i < slotAmount) {
            if (container.getSlot(i).getHasStack()) {
                return false;
            }
            ++i;
        }
        return true;
    }
}

