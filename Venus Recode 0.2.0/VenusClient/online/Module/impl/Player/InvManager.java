package VenusClient.online.Module.impl.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public final class InvManager extends Module {
  public InvManager() {
		super("InvManager", "InvManager", Category.PLAYER, Keyboard.KEY_L);
	}

public static final TimeHelper INV_STOPWATCH = new TimeHelper();
  
  private List<Integer> allSwords = new ArrayList<>();
  
  private List<Integer>[] allArmors = (List<Integer>[])new List[4];
  
  private List<Integer> trash = new ArrayList<>();
  
  private boolean cleaning;
  
  private int[] bestArmorSlot;
  
  
  private boolean equipping;
    
  private int bestSwordSlot;
  
  //private final DoubleOption swordSlot = new DoubleOption("Sword Slot", 1.0D, 1.0D, 9.0D, 1.0D);
  
  //private final DoubleOption delay = new DoubleOption("Delay", 250.0D, 0.0D, 1000.0D, 50.0D);
 
  @Override
	public void setup() {
		Client.instance.setmgr.rSetting(new Setting("InvManager Delay", this, 120, 20, 1000, true));
		Client.instance.setmgr.rSetting(new Setting("InvManager SwordSlot", this, 1, 1, 9, true));
	}
  
  
  @EventTarget
  public void onEvent(EventMotionUpdate event) {
		if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
			EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
	  		return;
		}
    if ((mc.currentScreen == null || mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) && event.isPre()) {
      collectItems();
      collectBestArmor();
      collectTrash();
      AutoArmor();
      int trashSize = this.trash.size();
      boolean trashPresent = (trashSize > 0);
      EntityPlayerSP player = mc.thePlayer;
      int windowId = player.openContainer.windowId;
      int bestSwordSlot = this.bestSwordSlot;
      if (trashPresent) {
        if (!this.cleaning) {
          this.cleaning = true;
          player.sendQueue.addToSendQueueSilent((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
        } 
        for (int i = 0; i < trashSize; i++) {
          int slot = ((Integer)this.trash.get(i)).intValue();
          if (checkDelay())
            break; 
          mc.playerController.windowClick(windowId, (slot < 9) ? (slot + 36) : slot, 1, 4, (EntityPlayer)player);
          INV_STOPWATCH.reset();
        } 
        if (this.cleaning) {
          player.sendQueue.addToSendQueueSilent((Packet)new C0DPacketCloseWindow(windowId));
          this.cleaning = false;
        } 
      } 
      if (bestSwordSlot != -1 && 
        !checkDelay()) {
        mc.playerController.windowClick(windowId, (bestSwordSlot < 9) ? (bestSwordSlot + 36) : bestSwordSlot, (int) (((Double)Client.instance.setmgr.getSettingByName("InvManager SwordSlot").getValDouble()) - 1), 2, (EntityPlayer)player);
        INV_STOPWATCH.reset();
      } 
    } 
  }
  
  private boolean checkDelay() {
	  double delay = Client.instance.setmgr.getSettingByName("InvManager Delay").getValDouble();
    return !INV_STOPWATCH.hasReached(((long)delay));
  }
  
  public void collectItems() {
    this.bestSwordSlot = -1;
    this.allSwords.clear();
    float bestSwordDamage = -1.0F;
    for (int i = 0; i < 36; i++) {
      ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
      if (itemStack != null && itemStack.getItem() != null)
        if (itemStack.getItem() instanceof net.minecraft.item.ItemSword) {
          float damageLevel = getDamageLevel(itemStack);
          this.allSwords.add(Integer.valueOf(i));
          if (bestSwordDamage < damageLevel) {
            bestSwordDamage = damageLevel;
            this.bestSwordSlot = i;
          } 
        }  
    } 
  }
  
  private void collectBestArmor() {
    int[] bestArmorDamageReducement = new int[4];
    this.bestArmorSlot = new int[4];
    Arrays.fill(bestArmorDamageReducement, -1);
    Arrays.fill(this.bestArmorSlot, -1);
    int i;
    for (i = 0; i < this.bestArmorSlot.length; i++) {
      ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
      this.allArmors[i] = new ArrayList<>();
      if (itemStack != null && itemStack.getItem() != null && 
        itemStack.getItem() instanceof ItemArmor) {
        ItemArmor armor = (ItemArmor)itemStack.getItem();
        int currentProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { itemStack }, DamageSource.generic);
        bestArmorDamageReducement[i] = currentProtectionLevel;
      } 
    } 
    for (i = 0; i < 36; i++) {
      ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
      if (itemStack != null && itemStack.getItem() != null)
        if (itemStack.getItem() instanceof ItemArmor) {
          ItemArmor armor = (ItemArmor)itemStack.getItem();
          int armorType = 3 - armor.armorType;
          this.allArmors[armorType].add(Integer.valueOf(i));
          int slotProtectionLevel = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { itemStack }, DamageSource.generic);
          if (bestArmorDamageReducement[armorType] < slotProtectionLevel) {
            bestArmorDamageReducement[armorType] = slotProtectionLevel;
            this.bestArmorSlot[armorType] = i;
          } 
        }  
    } 
  }

  private void collectTrash() {
    this.trash.clear();
    int i;
    for (i = 0; i < 36; i++) {
      ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
      if (itemStack != null && itemStack.getItem() != null)
        if (!isValidItem(itemStack))
          this.trash.add(Integer.valueOf(i));  
    } 
    for (i = 0; i < this.allArmors.length; i++) {
      List<Integer> armorItem = this.allArmors[i];
      if (armorItem != null) {
        List<Integer> list = this.trash;
        for (int i1 = 0, armorItemSize = armorItem.size(); i1 < armorItemSize; i1++) {
          Integer slot = armorItem.get(i1);
          if (slot.intValue() != this.bestArmorSlot[i])
            list.add(slot); 
        } 
      } 
    } 
    List<Integer> integers = this.trash;
    for (int j = 0, allSwordsSize = this.allSwords.size(); j < allSwordsSize; j++) {
      Integer slot = this.allSwords.get(j);
      if (slot.intValue() != this.bestSwordSlot)
        integers.add(slot); 
    } 
  }
  
  private void AutoArmor() {
	  collectBestArmor();
	  EntityPlayerSP player = mc.thePlayer;
      int inventoryId = player.inventoryContainer.windowId;
      for (int i = 0; i < 4; i++) {
        if (this.bestArmorSlot[i] != -1) {
          if (!this.equipping) {
            this.equipping = true;
            player.sendQueue.addToSendQueueSilent((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
          } 
          int bestSlot = this.bestArmorSlot[i];
          ItemStack oldArmor = mc.thePlayer.inventory.armorItemInSlot(i);
          if (checkDelay())
            return; 
          if (oldArmor != null) {
            mc.playerController.windowClick(inventoryId, 8 - i, 0, 4, (EntityPlayer)player);
            INV_STOPWATCH.reset();
          } 
          int slot = (bestSlot < 9) ? (bestSlot + 36) : bestSlot;
          if (checkDelay())
            return; 
          mc.playerController.windowClick(inventoryId, slot, 0, 1, (EntityPlayer)player);
          INV_STOPWATCH.reset();
          if (this.equipping) {
            player.sendQueue.addToSendQueueSilent((Packet)new C0DPacketCloseWindow(inventoryId));
            this.equipping = false;
          } 
        } 
      } 
  }
  
  public static boolean isValidItem(ItemStack itemStack) {
	    if (itemStack.getDisplayName().startsWith("§a"))
	      return true; 
	    return (itemStack.getItem() instanceof net.minecraft.item.ItemArmor || itemStack.getItem() instanceof ItemSword || itemStack
	      .getItem() instanceof net.minecraft.item.ItemTool || itemStack.getItem() instanceof net.minecraft.item.ItemFood || (itemStack
	      .getItem() instanceof ItemPotion && !isBadPotion(itemStack)) || itemStack.getItem() instanceof net.minecraft.item.ItemBlock || itemStack
	      .getDisplayName().contains("Play") || itemStack.getDisplayName().contains("Game") || itemStack
	      .getDisplayName().contains("Right Click"));
	  }
	  
	  public static float getDamageLevel(ItemStack stack) {
	    if (stack.getItem() instanceof ItemSword) {
	      ItemSword sword = (ItemSword)stack.getItem();
	      float sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F;
	      float fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 1.5F;
	      return sword.getDamageVsEntity() + sharpness + fireAspect;
	    } 
	    return 0.0F;
	  }
	  public static boolean isBadPotion(ItemStack stack) {
		    if (stack != null && stack.getItem() instanceof ItemPotion) {
		      ItemPotion potion = (ItemPotion)stack.getItem();
		      if (ItemPotion.isSplash(stack.getItemDamage()))
		        for (Object o : potion.getEffects(stack)) {
		          PotionEffect effect = (PotionEffect)o;
		          if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId())
		            return true; 
		        }  
		    } 
		    return false;
		  }
	  
}
