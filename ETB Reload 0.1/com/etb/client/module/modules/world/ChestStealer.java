package com.etb.client.module.modules.world;

import java.awt.Color;
import java.security.SecureRandom;
import java.util.Objects;

import com.etb.client.utils.value.impl.NumberValue;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class ChestStealer extends Module {
    private TimerUtil timer = new TimerUtil();
    private boolean doneSteal;
    private NumberValue<Integer> delay = new NumberValue("Delay", 80, 50, 500, 10);

    public ChestStealer() {
        super("ChestStealer", Category.WORLD, new Color(0, 25, 255, 255).getRGB());
        setDescription("Be a nigger and steal shit");
        setRenderlabel("Chest Stealer");
        addValues(delay);
    }

    @Override
    public void onDisable() {
        doneSteal = true;
    }

    @Override
    public void onEnable() {
        doneSteal = true;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            KeyBinding[] moveKeys = {mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint,};
            if (mc.currentScreen instanceof GuiChest) {
                KeyBinding[] array;
                int length = (array = moveKeys).length;
                for (int i = 0; i < length; i++) {
                    KeyBinding key = array[i];
                    key.pressed = Keyboard.isKeyDown(key.getKeyCode());
                }
            } else if (Objects.isNull(mc.currentScreen)) {
                KeyBinding[] array2;
                int length2 = (array2 = moveKeys).length;
                for (int j = 0; j < length2; j++) {
                    KeyBinding bind = array2[j];
                    if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                        KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                    }
                }
            }
            if (mc.currentScreen instanceof GuiChest) {
                final GuiChest chest = (GuiChest) mc.currentScreen;
                if (isChestEmpty(chest) || isInventoryFull()) {
                    mc.thePlayer.closeScreen();
                    doneSteal = true;
                }
                for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                    final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                    if ((stack != null) && timer.reach((long) secRanFloat(delay.getValue(), delay.getValue() * 2))) {
                        mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                        timer.reset();
                    }
                }
            } else if (timer.sleep(120)) {
                doneSteal = true;
            }
        }
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index <= chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null) {
                return false;
            }
        }
        return true;
    }

    private float secRanFloat(float min, float max) {
        return new SecureRandom().nextFloat() * (max - min) + min;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    private double getDistanceToBlock(BlockPos pos) {
        Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
            return eyesPos.distanceTo(hitVec);
        }
        return 0;
    }
}