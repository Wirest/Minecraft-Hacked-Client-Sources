package com.mentalfrostbyte.jello.modules;

import java.util.Iterator;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.mentalfrostbyte.jello.event.EventManager;
import com.mentalfrostbyte.jello.event.EventTarget;
import com.mentalfrostbyte.jello.event.events.EventPacketSent;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
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
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class InvCleaner extends Module {

	public Random rand = new Random();
	
	public TimerUtil timer = new TimerUtil();
	private int slots;
	private double numberIdkWillfigureout;
	private boolean someboolean;
	
	public InvCleaner() {
        super("InvCleaner", Keyboard.KEY_NONE);
        this.jelloCat = Jello.tabgui.cats.get(3);
    }
   
	public void onEnable(){
		EventManager.register(this);
		timer.reset();
		slots = 9;
		numberIdkWillfigureout = getEnchantmentOnSword(mc.thePlayer.getHeldItem());
	}
	
	public void onDisable(){
		EventManager.unregister(this);
	}
	
    public void onUpdate(){
    	if(!this.isToggled())
    		return;
    	if(mc.currentScreen instanceof GuiInventory){
    	if ((slots >= 45) && (!someboolean)) {
			setToggled(false);
			return;
		}
		if (someboolean) {
			if (timer.delay(100)) {
				mc.playerController.windowClick(0, -999, 0, 0, mc.thePlayer);
				mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				mc.playerController.syncCurrentPlayItem();
				someboolean = false;
				
				timer.reset();
			}
			return;
		}
		numberIdkWillfigureout = getEnchantmentOnSword(mc.thePlayer.getHeldItem());
		ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(slots).getStack();
		if ((isItemBad(stack)) && (getEnchantmentOnSword(stack) <= numberIdkWillfigureout)
				&& (stack != mc.thePlayer.getHeldItem())) {
			mc.playerController.windowClick(0, slots, 0, 0, mc.thePlayer);
			timer.reset();
			someboolean = true;
		}
		slots += 1;
    	
    	}
    }
	

	public static boolean isItemBad(ItemStack item) {
		return (item != null) && ((item.getItem().getUnlocalizedName().contains("TNT"))
				|| (item.getItem().getUnlocalizedName().contains("stick"))
				|| (item.getItem().getUnlocalizedName().contains("egg"))
				|| (item.getItem().getUnlocalizedName().contains("string"))
				|| (item.getItem().getUnlocalizedName().contains("flint"))
				|| (item.getItem().getUnlocalizedName().contains("compass"))
				|| (item.getItem().getUnlocalizedName().contains("feather"))
				|| (item.getItem().getUnlocalizedName().contains("bucket"))
				|| (item.getItem().getUnlocalizedName().contains("chest"))
				|| (item.getItem().getUnlocalizedName().contains("snowball"))
				|| (item.getItem().getUnlocalizedName().contains("fish"))
				|| (item.getItem().getUnlocalizedName().contains("enchant"))
				|| (item.getItem().getUnlocalizedName().contains("exp")) || ((item.getItem() instanceof ItemPickaxe))
				|| ((item.getItem() instanceof ItemTool)) || ((item.getItem() instanceof ItemArmor))
				|| ((item.getItem() instanceof ItemSword))
				|| ((item.getItem().getUnlocalizedName().contains("potion")) && (isBadPotion(item))));
	}

	public static boolean isBadPotion(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		if (!(itemStack.getItem() instanceof ItemPotion)) {
			return false;
		}
		ItemPotion itemPotion = (ItemPotion) itemStack.getItem();
		Iterator iterator = itemPotion.getEffects(itemStack).iterator();
		PotionEffect potionEffect;
		do {
			if (!iterator.hasNext()) {
				return false;
			}
			Object pObj = iterator.next();
			potionEffect = (PotionEffect) pObj;
			if (potionEffect.getPotionID() == Potion.poison.getId()) {
				return true;
			}
			if (potionEffect.getPotionID() == Potion.moveSlowdown.getId()) {
				return true;
			}
		} while (potionEffect.getPotionID() != Potion.harm.getId());
		return true;
	}

	private static double getEnchantmentOnSword(ItemStack itemStack) {
		if (itemStack == null) {
			return 0.0D;
		}
		if (!(itemStack.getItem() instanceof ItemSword)) {
			return 0.0D;
		}
		ItemSword itemSword = (ItemSword) itemStack.getItem();
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, itemStack)
				+ itemSword.field_150934_a;
	}
}
