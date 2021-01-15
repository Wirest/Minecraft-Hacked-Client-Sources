// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import java.util.Iterator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import me.CheerioFX.FusionX.events.EventPacketReceive;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimerUtils;
import me.CheerioFX.FusionX.module.Module;

public class AutoPot extends Module
{
    public static long delay;
    private float oldYaw;
    private float oldPitch;
    public static int pots;
    private final TimerUtils timer;
    private boolean needsToPot;
    private static boolean potting;
    
    public AutoPot() {
        super("AutoPot", 0, Category.COMBAT);
        AutoPot.delay = 350L;
        AutoPot.potting = false;
        this.timer = new TimerUtils();
        this.needsToPot = false;
    }
    
    @Override
    public void setup() {
        FusionX.theClient.setmgr.rSetting(new Setting("Pot Health", this, 5.0, 1.0, 10.0, false));
    }
    
    private double getPotHealth() {
        return FusionX.theClient.setmgr.getSetting(this, "Pot Health").getValDouble();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.setExtraInfo("- " + AutoPot.pots);
    }
    
    @EventTarget
    public void EventPreMotionUpdates(final EventPreMotionUpdates event) {
        if (this.updateCounter() == 0) {
            return;
        }
        if (this.timer.hasTimeElapsed(Long.valueOf(AutoPot.delay), false) && this.needsToPot && this.doesHotbarHavePots()) {
            this.oldYaw = Wrapper.mc.thePlayer.rotationYaw;
            this.oldPitch = Wrapper.mc.thePlayer.rotationPitch;
            AutoPot.potting = true;
            Wrapper.mc.thePlayer.rotationYaw = -Wrapper.mc.thePlayer.rotationYaw;
            Wrapper.mc.thePlayer.rotationPitch = 90.0f;
        }
    }
    
    @EventTarget
    public void onPost(final EventPostMotionUpdates event) {
        this.needsToPot = (Wrapper.mc.thePlayer.getHealth() <= this.getPotHealth());
        if (this.timer.hasTimeElapsed(Long.valueOf(AutoPot.delay), false) && this.needsToPot) {
            if (this.doesHotbarHavePots()) {
                if (AutoPot.potting) {
                    if (this.needsToPot) {
                        for (int i = 36; i < 45; ++i) {
                            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            if (stack != null && this.isStackSplashHealthPot(stack)) {
                                final int oldSlot = Wrapper.mc.thePlayer.inventory.currentItem;
                                Wrapper.mc.thePlayer.inventory.currentItem = i - 36;
                                Wrapper.mc.playerController.sendUseItem(Wrapper.mc.thePlayer, Wrapper.mc.theWorld, stack);
                                Wrapper.mc.thePlayer.inventory.currentItem = oldSlot;
                                Wrapper.mc.thePlayer.rotationYaw = this.oldYaw;
                                Wrapper.mc.thePlayer.rotationPitch = this.oldPitch;
                                this.needsToPot = false;
                                AutoPot.potting = false;
                                break;
                            }
                        }
                    }
                    AutoPot.potting = false;
                    this.timer.reset();
                }
            }
            else {
                this.getPotsFromInventory();
            }
        }
        this.setExtraInfo("- " + AutoPot.pots);
    }
    
    @EventTarget
    public void onPacketReceive(final EventPacketReceive e) {
        if (e.getPacket() instanceof S06PacketUpdateHealth) {
            final S06PacketUpdateHealth packetUpdateHealth = (S06PacketUpdateHealth)e.getPacket();
            if (!this.needsToPot) {
                this.needsToPot = (packetUpdateHealth.getHealth() <= this.getPotHealth());
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        AutoPot.potting = false;
        this.needsToPot = false;
    }
    
    private boolean doesHotbarHavePots() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                return true;
            }
        }
        return false;
    }
    
    private void getPotsFromInventory() {
        for (int i = 9; i < 36; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                Wrapper.mc.playerController.windowClick(Wrapper.mc.thePlayer.openContainer.windowId, i, 1, 2, Wrapper.mc.thePlayer);
                break;
            }
        }
    }
    
    private boolean isStackSplashHealthPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.heal.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private int updateCounter() {
        AutoPot.pots = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = Wrapper.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && this.isStackSplashHealthPot(stack)) {
                AutoPot.pots += stack.stackSize;
            }
        }
        return AutoPot.pots;
    }
    
    public static boolean CurrentlyPotting() {
        return AutoPot.potting;
    }
}
