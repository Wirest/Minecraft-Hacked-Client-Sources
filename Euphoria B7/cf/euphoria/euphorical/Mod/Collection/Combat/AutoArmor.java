// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Utils.TimeHelper;
import cf.euphoria.euphorical.Mod.Mod;

public class AutoArmor extends Mod
{
    private TimeHelper timer;
    private int[] bestArmor;
    
    public AutoArmor() {
        super("AutoArmor", Category.COMBAT);
        this.timer = new TimeHelper();
    }
    
    @Override
    public void onEnable() {
        this.timer = new TimeHelper();
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.mc.thePlayer.capabilities.isCreativeMode || (this.mc.currentScreen instanceof GuiContainer && !(this.mc.currentScreen instanceof GuiInventory))) {
            return;
        }
        if (this.timer.hasPassed(250.0)) {
            this.bestArmor = new int[4];
            for (int i = 0; i < this.bestArmor.length; ++i) {
                this.bestArmor[i] = -1;
            }
            for (int i = 0; i < 36; ++i) {
                final ItemStack itemstack = this.mc.thePlayer.inventory.getStackInSlot(i);
                if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                    final ItemArmor armor = (ItemArmor)itemstack.getItem();
                    if (armor.damageReduceAmount > this.bestArmor[3 - armor.armorType]) {
                        this.bestArmor[3 - armor.armorType] = i;
                    }
                }
            }
            for (int i = 0; i < 4; ++i) {
                final ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(i);
                ItemArmor currentArmor;
                if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                    currentArmor = (ItemArmor)itemstack.getItem();
                }
                else {
                    currentArmor = null;
                }
                ItemArmor bestArmor;
                try {
                    bestArmor = (ItemArmor)this.mc.thePlayer.inventory.getStackInSlot(this.bestArmor[i]).getItem();
                }
                catch (Exception e) {
                    bestArmor = null;
                }
                if (bestArmor != null && (currentArmor == null || bestArmor.damageReduceAmount > currentArmor.damageReduceAmount) && (this.mc.thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null)) {
                    this.mc.playerController.windowClick(0, 8 - i, 0, 1, this.mc.thePlayer);
                    this.mc.playerController.windowClick(0, (this.bestArmor[i] < 9) ? (36 + this.bestArmor[i]) : this.bestArmor[i], 0, 1, Minecraft.getMinecraft().thePlayer);
                }
            }
            this.timer.reset();
        }
    }
}
