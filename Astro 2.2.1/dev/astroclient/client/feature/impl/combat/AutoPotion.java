package dev.astroclient.client.feature.impl.combat;

import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "AutoPotion", category = Category.COMBAT)
public class AutoPotion extends ToggleableFeature {

    public NumberProperty<Integer> health = new NumberProperty<>("Health", true, 6, 1, 1, 10);
    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 200, 1, 0, 1000, Type.MILLISECONDS);

    private Timer timer = new Timer();
    private float prevPitch;

    public boolean potting;

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        this.setSuffix(String.valueOf(getPotionCount()));
        int prevSlot = mc.thePlayer.inventory.currentItem;
        if (!Client.INSTANCE.featureManager.flight.getState() && !Client.INSTANCE.featureManager.scaffold.getState() && mc.thePlayer.onGround)
            if (eventMotion.getEventType().equals(EventType.PRE)) {
                if (!mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (timer.hasReached(delay.getValue())) {
                        if (isSpeedPotsInHotBar()) {
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashSpeedPot(stack)) {
                                    potting = true;
                                    prevPitch = mc.thePlayer.rotationPitch;

                                }
                            }
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashSpeedPot(stack) && potting) {
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 89, mc.thePlayer.onGround));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(index - 36));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, prevPitch, mc.thePlayer.onGround));
                                    break;
                                }
                            }
                            timer.reset();
                            potting = false;
                        } else {
                            getSpeedPotsFromInventory();
                        }
                    }
                }

                if (!mc.thePlayer.isPotionActive(Potion.regeneration) && mc.thePlayer.getHealth() <= health.getValue() * 2) {
                    if (timer.hasReached(delay.getValue().longValue())) {
                        if (isRegenPotsInHotBar()) {
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashRegenPot(stack)) {
                                    potting = true;
                                    prevPitch = mc.thePlayer.rotationPitch;
                                }
                            }
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashRegenPot(stack)) {
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 89, mc.thePlayer.onGround));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(index - 36));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, prevPitch, mc.thePlayer.onGround));
                                    break;
                                }
                            }
                            timer.reset();
                            potting = false;
                        } else {
                            getRegenPotsFromInventory();
                        }
                    }
                }

                if (mc.thePlayer.getHealth() <= health.getValue() * 2) {
                    if (timer.hasReached(delay.getValue())) {
                        if (isHealthPotsInHotBar()) {
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashHealthPot(stack)) {
                                    potting = true;
                                    prevPitch = mc.thePlayer.rotationPitch;
                                }
                            }
                            for (int index = 36; index < 45; index++) {
                                final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
                                if (stack == null)
                                    continue;
                                if (isStackSplashHealthPot(stack)) {
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 89, mc.thePlayer.onGround));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(index - 36));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, prevPitch, mc.thePlayer.onGround));
                                    break;
                                }
                            }
                            timer.reset();
                            potting = false;
                        } else {
                            getPotsFromInventory();
                        }
                    }
                }
            } else if (eventMotion.getEventType().equals(EventType.POST)) {
                potting = false;
            }
    }

    private boolean isSpeedPotsInHotBar() {
        for (int index = 36; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (stack.getDisplayName().contains("Frog")) continue;
            if (isStackSplashSpeedPot(stack))
                return true;
        }
        return false;
    }

    private boolean isHealthPotsInHotBar() {
        for (int index = 36; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isStackSplashHealthPot(stack))
                return true;
        }
        return false;
    }

    private boolean isRegenPotsInHotBar() {
        for (int index = 36; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isStackSplashRegenPot(stack))
                return true;
        }
        return false;
    }

    private void getSpeedPotsFromInventory() {
        if (mc.currentScreen instanceof GuiChest)
            return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (stack.getDisplayName().contains("Frog")) continue;
            if (isStackSplashSpeedPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private int getPotionCount() {
        int count = 0;
        for (int index = 0; index < 45; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isStackSplashHealthPot(stack) || isStackSplashHealthPot(stack) || isStackSplashRegenPot(stack))
                count++;
        }
        return count;
    }

    private void getPotsFromInventory() {
        if (mc.currentScreen instanceof GuiChest)
            return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isStackSplashHealthPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isStackSplashSpeedPot(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (stack.getDisplayName().contains("Frog")) return false;
                    if (effect.getPotionID() == Potion.moveSpeed.id && effect.getPotionID() != Potion.jump.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isStackSplashHealthPot(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.heal.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void getRegenPotsFromInventory() {
        if (mc.currentScreen instanceof GuiChest)
            return;
        for (int index = 9; index < 36; index++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null)
                continue;
            if (isStackSplashRegenPot(stack)) {
                mc.playerController.windowClick(0, index, 6, 2, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isStackSplashRegenPot(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.regeneration.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
