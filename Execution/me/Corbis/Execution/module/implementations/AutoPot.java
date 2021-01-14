package me.Corbis.Execution.module.implementations;


import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.RotationUtils;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class AutoPot extends Module {

    public AutoPot(){
        super("AutoPot", Keyboard.KEY_NONE, Category.COMBAT);
    }
    TimeHelper timer = new TimeHelper();
    @EventTarget
    public void onEvent(EventMotionUpdate event){
        if(event.isPre()){

            for(int i = 0; i < 9; i++){
                if(mc.thePlayer.inventory.getStackInSlot(i) == null)
                    continue;
                if(mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemSkull){
                    if(mc.thePlayer.getHealth() < 5){
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(i));
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    }
                }
            }
            final boolean speed = true;
            final boolean regen = true;


            int spoofSlot = getBestSpoofSlot();
            int pots[] = {6,-1,-1};
            if(regen)
                pots[1] = 10;
            if(speed)
                pots[2] = 1;

            for(int i = 0; i < pots.length; i ++){
                if(pots[i] == -1)
                    continue;
                if(pots[i] == 6 || pots[i] == 10){
                    if(timer.hasReached(900) && !mc.thePlayer.isPotionActive(pots[i])){
                        if(mc.thePlayer.getHealth() < 15){
                            getBestPot(spoofSlot, pots[i]);
                        }
                    }
                }else
                if(timer.hasReached(1000) && !mc.thePlayer.isPotionActive(pots[i])){
                    getBestPot(spoofSlot, pots[i]);
                }
            }
        }
    }

    public void swap(int slot1, int hotbarSlot){
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }
    float[] getRotations(){
        double movedPosX = mc.thePlayer.posX + mc.thePlayer.motionX * 26.0D;
        double movedPosY = mc.thePlayer.boundingBox.minY - 3.6D;
        double movedPosZ = mc.thePlayer.posZ + mc.thePlayer.motionZ * 26.0D;
        return RotationUtils.getRotationFromPosition(movedPosX, movedPosZ, movedPosY);
    }
    int getBestSpoofSlot(){
        int spoofSlot = 5;
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            }else if(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemPotion) {
                spoofSlot = i - 36;
                break;
            }
        }
        return spoofSlot;
    }
    void getBestPot(int hotbarSlot, int potID){
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() &&(mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if(pot.getEffects(is).isEmpty())
                        return;
                    PotionEffect effect = (PotionEffect) pot.getEffects(is).get(0);
                    int potionID = effect.getPotionID();
                    if(potionID == potID)
                        if(ItemPotion.isSplash(is.getItemDamage()) && isBestPot(pot, is)){
                            if(36 + hotbarSlot != i)
                                swap(i, hotbarSlot);
                            timer.reset();
                            boolean canpot = true;
                            int oldSlot = mc.thePlayer.inventory.currentItem;
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.thePlayer.onGround));
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));

                            break;
                        }
                }
            }
        }
    }

    boolean isBestPot(ItemPotion potion, ItemStack stack){
        if(potion.getEffects(stack) == null || potion.getEffects(stack).size() != 1)
            return false;
        PotionEffect effect = (PotionEffect) potion.getEffects(stack).get(0);
        int potionID = effect.getPotionID();
        int amplifier = effect.getAmplifier();
        int duration = effect.getDuration();
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(is.getItem() instanceof ItemPotion){
                    ItemPotion pot = (ItemPotion)is.getItem();
                    if (pot.getEffects(is) != null) {
                        for (Object o : pot.getEffects(is)) {
                            PotionEffect effects = (PotionEffect) o;
                            int id = effects.getPotionID();
                            int ampl = effects.getAmplifier();
                            int dur = effects.getDuration();
                            if (id == potionID && ItemPotion.isSplash(is.getItemDamage())){
                                if(ampl > amplifier){
                                    return false;
                                }else if (ampl == amplifier && dur > duration){
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
