package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.ItemUtils;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.*;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public class Stealer extends Module {
    private double xPos, yPos, zPos, minx;

    TimeHelper timer = new TimeHelper();
    public Setting delaySet;
    private Setting autoclose;
    private Setting baditems;
    private Setting silent;
    private double delay;
    public Stealer(){
        super("Stealer", Keyboard.KEY_H, Category.WORLD);
        Execution.instance.settingsManager.rSetting(delaySet = new Setting("ChestStealer Delay", this, 100, 50, 500, true));
        Execution.instance.settingsManager.rSetting(baditems = new Setting("Ignore Bad Items", this, true));

        Execution.instance.settingsManager.rSetting(autoclose = new Setting("AutoClose", this, true));
        Execution.instance.settingsManager.rSetting(silent = new Setting("Silent", this, true));
    }
    GuiChest silentCurrent;
    @EventTarget
    public void onUpdate(EventUpdate event){
        if (this.isChestEmpty()) {
            this.setDelay();
        }

        if(!silent.getValBoolean()) {

            if (mc.currentScreen instanceof GuiChest) {

                final GuiChest chest = (GuiChest) mc.currentScreen;
                boolean close = autoclose.getValBoolean();
                if (isValidChest(chest)) {
                    if ((this.isChestEmpty() || ItemUtils.isInventoryFull()) && close && timer.hasReached((long) delay)) {
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        timer.reset();
                        return;
                    }

                    if (timer.hasReached((long) delay)) {
                        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                            final ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                            if (stack != null && timer.hasReached((long) delay) && (!ItemUtils.isBad(stack) || !baditems.getValBoolean())) {
                                mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                                this.setDelay();
                                this.timer.reset();
                                continue;
                            }
                        }
                        timer.reset();
                    }
                }
            }
        }else {
            if(mc.currentScreen instanceof GuiChest){
                silentCurrent = (GuiChest)mc.currentScreen;
                if(isValidChest(silentCurrent)) {
                    mc.setIngameFocus();
                    mc.currentScreen = null;
                }
            }
            if(silentCurrent != null){
                final GuiChest chest = silentCurrent;
                boolean close = autoclose.getValBoolean();
                if (isValidChest(chest)) {
                    if ((this.isChestEmpty(chest) || ItemUtils.isInventoryFull()) && close && timer.hasReached((long) delay)) {
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        silentCurrent = null;
                        timer.reset();
                        return;
                    }

                    if (timer.hasReached((long) delay)) {
                        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                            final ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                            if (stack != null && timer.hasReached((long) delay) && (!ItemUtils.isBad(stack) || !baditems.getValBoolean())) {
                                mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                                this.setDelay();
                                this.timer.reset();
                                continue;
                            }
                        }
                        timer.reset();
                    }
                }
            }
        }
    }

    private boolean isChestEmpty() {
        if (mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest)mc.currentScreen;
            for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
                final ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
                if (stack != null && (!ItemUtils.isBad(stack) || !baditems.getValBoolean())) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setDelay() {
        if (delaySet.getValDouble() <= 5) {
            this.delay = delaySet.getValDouble();
        } else {
            this.delay = delaySet.getValDouble() + ThreadLocalRandom.current().nextDouble(-40, 40);
        }

    }


    private boolean isValidChest(GuiChest chest) {

        int radius = 5;
        for(int x = -radius; x < radius; x++){
            for(int y = radius; y > -radius; y--){
                for(int z = -radius; z < radius; z++){
                    this.xPos = mc.thePlayer.posX + x;
                    this.yPos = mc.thePlayer.posY + y;
                    this.zPos = mc.thePlayer.posZ + z;

                    BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                    if(block instanceof BlockChest){
                        return true;

                    }
                }
            }
        }
        return false;
    }

    private boolean isValidItem(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemSword ||
                itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemFood ||
                itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemBlock;
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); ++index) {
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
            if (stack != null) {
                if (isValidItem(stack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }

        return true;
    }
}
