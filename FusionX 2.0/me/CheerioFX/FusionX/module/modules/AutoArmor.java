// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class AutoArmor extends Module
{
    private final int[] boots;
    private final int[] chestplate;
    private final int[] helmet;
    private final int[] leggings;
    int delay;
    public static int speed;
    public boolean bypass;
    
    static {
        AutoArmor.speed = 1;
    }
    
    public AutoArmor() {
        super("AutoArmor", 0, Category.COMBAT);
        this.bypass = false;
        this.boots = new int[] { 313, 309, 317, 305, 301 };
        this.chestplate = new int[] { 311, 307, 315, 303, 299 };
        this.helmet = new int[] { 310, 306, 314, 302, 298 };
        this.leggings = new int[] { 312, 308, 316, 304, 300 };
        this.delay = 0;
    }
    
    private int getItem(final int id) {
        for (int index = 9; index < 45; ++index) {
            final ItemStack item = Wrapper.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (item != null && Item.getIdFromItem(item.getItem()) == id) {
                return index;
            }
        }
        return -1;
    }
    
    @Override
    public void onEnable() {
        if (this.bypass) {
            AutoArmor.speed = 5;
        }
        else {
            AutoArmor.speed = 1;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @EventTarget
    public void onPre(final EventPreMotionUpdates e) {
        int item = -1;
        ++this.delay;
        if (this.delay >= AutoArmor.speed) {
            if (Wrapper.mc.thePlayer.inventory.armorInventory[0] == null) {
                int[] boots;
                for (int length = (boots = this.boots).length, i = 0; i < length; ++i) {
                    final int id = boots[i];
                    if (this.getItem(id) != -1) {
                        item = this.getItem(id);
                        break;
                    }
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[1] == null) {
                int[] leggings;
                for (int length2 = (leggings = this.leggings).length, j = 0; j < length2; ++j) {
                    final int id = leggings[j];
                    if (this.getItem(id) != -1) {
                        item = this.getItem(id);
                        break;
                    }
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[2] == null) {
                int[] chestplate;
                for (int length3 = (chestplate = this.chestplate).length, k = 0; k < length3; ++k) {
                    final int id = chestplate[k];
                    if (this.getItem(id) != -1) {
                        item = this.getItem(id);
                        break;
                    }
                }
            }
            if (Wrapper.mc.thePlayer.inventory.armorInventory[3] == null) {
                int[] helmet;
                for (int length4 = (helmet = this.helmet).length, l = 0; l < length4; ++l) {
                    final int id = helmet[l];
                    if (this.getItem(id) != -1) {
                        item = this.getItem(id);
                        break;
                    }
                }
            }
            if (item != -1) {
                Wrapper.mc.playerController.windowClick(0, item, 0, 1, Wrapper.mc.thePlayer);
                this.delay = 0;
            }
        }
    }
}
