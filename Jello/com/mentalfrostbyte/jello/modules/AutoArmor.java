package com.mentalfrostbyte.jello.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.event.events.EventPreMotionUpdates;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.util.ArmorUtil;
import com.mentalfrostbyte.jello.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class AutoArmor extends Module {

	public Random rand = new Random();
	private final int[] chestplate = new int[]{311, 307, 315, 303, 299};
    private final int[] leggings = new int[]{312, 308, 316, 304, 300};
    private final int[] boots = new int[]{313, 309, 317, 305, 301};
    private final int[] helmet = new int[]{310, 306, 314, 302, 298};
    int delay = 0;
    public boolean bestarmor = true;
	public TimerUtil timer = new TimerUtil();
	
	public AutoArmor() {
        super("Auto Armor", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(3);
    }
   
	public void onEnable(){
	}
	
	public void onDisable(){
	}
	
	
    public void AutoArmoring() {
        if (this.bestarmor) {
            return;
        }
        int item = -1;
        ++this.delay;
        if (this.delay >= 10) {
            int id;
            if (Jello.core.player().inventory.armorInventory[0] == null) {
                int[] boots = this.boots;
                int length = boots.length;
                int i = 0;
                //int slotID = -1;
                while (i < length) {
                    id = boots[i];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++i;
                }
            }
            if (Jello.core.player().inventory.armorInventory[1] == null) {
                int[] leggings = this.leggings;
                int length2 = leggings.length;
                int j = 0;
                while (j < length2) {
                    id = leggings[j];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++j;
                }
            }
            if (Jello.core.player().inventory.armorInventory[2] == null) {
                int[] chestplate = this.chestplate;
                int length3 = chestplate.length;
                int k = 0;
                while (k < length3) {
                    id = chestplate[k];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++k;
                }
            }
            if (Jello.core.player().inventory.armorInventory[3] == null) {
                int[] helmet = this.helmet;
                int length4 = helmet.length;
                int l = 0;
                while (l < length4) {
                    id = helmet[l];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++l;
                }
            }
            if (item != -1) {
                mc.playerController.windowClick(0, item, 0, 1, Jello.core.player());
                this.delay = 0;
            }
        }
    }

    public void SwitchToBetterArmor() {
        if (!this.bestarmor) {
            return;
        }
        ++this.delay;
        if (this.delay >= 10 && (Jello.core.player().openContainer == null || Jello.core.player().openContainer.windowId == 0)) {
            int n;
            int n2;
            int[] arrn;
            int id;
            boolean switcharmor = false;
            int item = -1;
            if (Jello.core.player().inventory.armorInventory[0] == null) {
                arrn = this.boots;
                n2 = arrn.length;
                n = 0;
                while (n < n2) {
                    id = arrn[n];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++n;
                }
            }
            if (ArmorUtil.IsBetterArmor(0, this.boots)) {
                item = 8;
                switcharmor = true;
            }
            if (Jello.core.player().inventory.armorInventory[3] == null) {
                arrn = this.helmet;
                n2 = arrn.length;
                n = 0;
                while (n < n2) {
                    id = arrn[n];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++n;
                }
            }
            if (ArmorUtil.IsBetterArmor(3, this.helmet)) {
                item = 5;
                switcharmor = true;
            }
            if (Jello.core.player().inventory.armorInventory[1] == null) {
                arrn = this.leggings;
                n2 = arrn.length;
                n = 0;
                while (n < n2) {
                    id = arrn[n];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++n;
                }
            }
            if (ArmorUtil.IsBetterArmor(1, this.leggings)) {
                item = 7;
                switcharmor = true;
            }
            if (Jello.core.player().inventory.armorInventory[2] == null) {
                arrn = this.chestplate;
                n2 = arrn.length;
                n = 0;
                while (n < n2) {
                    id = arrn[n];
                    if (ArmorUtil.getItem(id) != -1) {
                        item = ArmorUtil.getItem(id);
                        break;
                    }
                    ++n;
                }
            }
            if (ArmorUtil.IsBetterArmor(2, this.chestplate)) {
                item = 6;
                switcharmor = true;
            }
            boolean var7 = false;
            ItemStack[] arritemStack = Jello.core.player().inventory.mainInventory;
            int n3 = arritemStack.length;
            n2 = 0;
            while (n2 < n3) {
                ItemStack stack = arritemStack[n2];
                if (stack == null) {
                    var7 = true;
                    break;
                }
                ++n2;
            }
            boolean bl = switcharmor = switcharmor && !var7;
            if (item != -1) {
                mc.playerController.windowClick(0, item, 0, switcharmor ? 4 : 1, Jello.core.player());
                this.delay = 0;
            }
        }
    }
    private double getProtectionValue(final ItemStack stack) {
        return ((ItemArmor)stack.getItem()).damageReduceAmount + (100 - ((ItemArmor)stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack) * 4 * 0.0075;
    }

    public void onUpdate() {
    	if(!this.isToggled())
    		return;
    	if(mc.currentScreen instanceof GuiInventory){
    	
        this.AutoArmoring();
        this.SwitchToBetterArmor();
    	}
    }
}
