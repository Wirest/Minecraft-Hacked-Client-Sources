package splash.client.modules.player;


import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.math.MathUtils;
import splash.utilities.player.InventoryUtils;
import splash.utilities.time.Stopwatch;

public class InventoryManager extends Module {
	public BooleanValue<Boolean> clean = new BooleanValue<>("Clean", true, this);
	public BooleanValue<Boolean> autoArmor = new BooleanValue<>("Armor", true, this);
    private Stopwatch dropStopwatch;
    private Stopwatch equipStopwatch;
    private Stopwatch swordEquipStopwatch;
    public boolean cleaning;
    public boolean equipping;
    public boolean swappingSword;
    public boolean opened;
    public int exploitTime;
    private boolean guiOpenedByMod;
    private final ArrayList<Action> clickQueue;

    public InventoryManager() {
        super("InventoryManager", "Automatically manages your inventory", ModuleCategory.PLAYER);
        clickQueue = new ArrayList<>();
        dropStopwatch = new Stopwatch();
        equipStopwatch = new Stopwatch();
        swordEquipStopwatch = new Stopwatch();
        clickQueue.clear();
    }
	@Override
	public void onEnable() {
        cleaning = false;
        equipping = false;
        guiOpenedByMod = false;
        swappingSword = false;
		swordEquipStopwatch.reset();
		equipStopwatch.reset();
		dropStopwatch.reset();
		super.onEnable();
	}


	@Collect
	public void onUpate(EventPlayerUpdate event) {
        if (onServer("cubecraft")) {
            if (!(mc.currentScreen instanceof GuiInventory)) return;
        } else {
        	if ((mc.currentScreen instanceof GuiInventory) || (mc.currentScreen instanceof GuiContainerCreative) || mc.currentScreen != null) return;
        }
		if (event.getStage().equals(Stage.PRE)) {
	        ChestStealer cs = (ChestStealer)Splash.INSTANCE.getModuleManager().getModuleByClass(ChestStealer.class);
	        if (cs.isModuleActive() && cs.stealing || mc.thePlayer.isUsingItem())
	            return;
	        
	        if (!clickQueue.isEmpty()) {
	            clickQueue.get(0).execute();
	            clickQueue.remove(clickQueue.get(0));
	        } else {
	        	if (!equipArmor()) {
	        		equipping = false;
	        		if (!clean()) {
	        			cleaning = false;
	        		} else {
	        			swordEquipStopwatch.reset();
	        			cleaning = true;
	        		}
	        	} else {
	        		swordEquipStopwatch.reset();
	        		equipping = true;
	        	}
	        	
	        	if (!equipping && !cleaning && swordEquipStopwatch.delay(200)) {
	        		if (!swapSword()) {
	                    swappingSword = false;
	        		}
	        		swordEquipStopwatch.reset();
	        	}
	        }
		} else {
            if (exploitTime > 4) {
                exploitTime = 0; 
            }
		}
	}
	
	public boolean swapSword() {

        for (int i = 9; i < 45; i++) {
            if (i == 35 + 1)
                continue;

            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemSword))
                continue;

            ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (!mc.thePlayer.inventoryContainer.getSlot(35 + 1).getHasStack()) {
                int finalI1 = i;
                swappingSword = true;
                clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0,  mc.thePlayer));
                clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + 1, 0, 0, mc.thePlayer));
                exploitTime++;
                return true;
            } else {
                ItemStack stackInWantedSlot = mc.thePlayer.inventoryContainer.getSlot(35 + 1).getStack();
                if (!(stackInWantedSlot.getItem() instanceof ItemSword)) return (swappingSword = false);
                if (compareDamage(stackInSlot, stackInWantedSlot) == stackInSlot) {
                    int finalI = i;
                    swappingSword = true;

                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35 + 1, 0, 0, mc.thePlayer));
                    clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                    exploitTime++;
                    return true;
                }
            }
        }        
        return false;
	}
	
	public boolean clean() {
		if (clean.getValue()) {
            if (mc.thePlayer == null)
                return false;
 
            ArrayList<Integer> uselessItem = new ArrayList<Integer>();
            for (int i = 0; i < 45; i++) {

                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (mc.thePlayer.inventory.armorItemInSlot(0) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(1) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(2) == stackInSlot
                        || mc.thePlayer.inventory.armorItemInSlot(3) == stackInSlot)
                    continue;

                if (isGarbo(i))
                    uselessItem.add(i);

            } 
            if (uselessItem.size() > 0) {
                cleaning = true;
 
                if (dropStopwatch.delay(300)) {
                	if (!(mc.thePlayer.inventory.currentItem == uselessItem.get(0))) {
                		
                        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, uselessItem.get(0), 1, 4, mc.thePlayer);
                        exploitTime++;
                	}
                    uselessItem.remove(0);
                    dropStopwatch.reset();
                }
                return true;
            }
 
		}
		
        return false;
    }
	
    private boolean equipArmor() {
    	if (autoArmor.getValue()) {
            for (int i = 9; i < 45; i++) {
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack())
                    continue;

                ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                if (!(stackInSlot.getItem() instanceof ItemArmor))
                    continue;

                if (getArmorType(stackInSlot, false) == -1)
                    continue;

                if (mc.thePlayer.getEquipmentInSlot(getArmorType(stackInSlot, true)) == null) {
                    System.out.println("No stack in slot : " + stackInSlot.getUnlocalizedName());
                    if (equipStopwatch.delay(300)) {
                        int finalI = i;
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI, 0, 0, mc.thePlayer));
                        clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, getArmorType(stackInSlot, false), 0, 0, mc.thePlayer));
                        exploitTime++;
    					equipStopwatch.reset();
                        return true;
                    }
                } else {
                    ItemStack stackInEquipmentSlot = mc.thePlayer.getEquipmentInSlot(getArmorType(stackInSlot, true));
                    if (compareProtection(stackInSlot, stackInEquipmentSlot) == stackInSlot) {
                        System.out.println("Stack in slot : " + stackInSlot.getUnlocalizedName());
                        if (equipStopwatch.delay(300)) {
                            int finalI1 = i; 
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, getArmorType(stackInSlot, false), 0, 0, mc.thePlayer));
                            clickQueue.add(() -> mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, finalI1, 0, 0, mc.thePlayer));
                            exploitTime++;
                            equipStopwatch.reset();
                            return true;
                        }
                    }
                }   
            }
    	}
        return false;
    }
    
    private boolean isGarbo(int slot) {
        ItemStack stackInSlot = mc.thePlayer.inventoryContainer.getSlot(slot).getStack();

        if (stackInSlot.hasDisplayName())
        	return false;
 
        if (stackInSlot.getItem() instanceof ItemSword) {
        	for (int i = 0; i < 44; i++) {
        		if (i == slot) continue;
        		if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        			ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        			if (stackAtIndex.getItem() instanceof ItemSword) {
        				if (compareDamage(stackInSlot, stackAtIndex) == stackAtIndex) {
        					return true;
        				}
        			}
        		}
        	}
        	return false;
        } 
         	
        if (stackInSlot.getItem() instanceof ItemAxe || stackInSlot.getItem() instanceof ItemBow || stackInSlot.getItem() instanceof ItemFishingRod || stackInSlot.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackInSlot.getItem()) == 346) {
        	for (int i = 44; i > 0; i--) {
        		if (i == slot) continue;
        		if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        			ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        			if ((stackAtIndex.getItem() instanceof ItemSword || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemBow || stackAtIndex.getItem() instanceof ItemFishingRod || stackAtIndex.getItem() instanceof ItemAxe || stackAtIndex.getItem() instanceof ItemPickaxe || Item.getIdFromItem(stackAtIndex.getItem()) == 346)) {
        				if (Item.getIdFromItem(stackAtIndex.getItem()) == Item.getIdFromItem(stackInSlot.getItem())) {
        					return true;
        				}	
        			}
        		}
        	}
        	return false;
        } 
        if (stackInSlot.getItem() instanceof ItemArmor) {
        	int equipmentType = getArmorType(stackInSlot, true);
        	if (equipmentType != -1) {
        		if (mc.thePlayer.getEquipmentInSlot(equipmentType) != null) {
        			if (compareProtection(stackInSlot, mc.thePlayer.getEquipmentInSlot(equipmentType)) == mc.thePlayer.getEquipmentInSlot(equipmentType)) {
        				return true;
        			}
        		} else {
        			for (int i = 44; i > 0; i--) {
        				if (i == slot) continue;
        				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
        					ItemStack stackAtIndex = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
        					if (stackAtIndex.getItem() instanceof ItemArmor) {
        						if (getArmorType(stackAtIndex, true) == equipmentType) {
        							if (compareProtection(stackInSlot, stackAtIndex) == stackAtIndex) {
        								return true;
        							}
        						}
        					}
        				}
        			}
        		}
        	}
        	return false;
        }
        if (stackInSlot.getItem() instanceof ItemFood)
        	return false;
        if (!isPotionNegative(stackInSlot))
            return false;

        return true;
    }
    
    public boolean isPotionNegative(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) itemStack.getItem();

            List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

            return potionEffectList.stream()
                    .map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
                    .anyMatch(Potion::isBadEffect);
        }
        return false;
    }
    
    public ItemStack compareDamage(ItemStack item1, ItemStack item2) {
        if (getItemDamage(item1) > getItemDamage(item2)) {
            return item1;
        } else if (getItemDamage(item2) > getItemDamage(item1)) {
            return item2;
        }
        return null;
    }
    
    public ItemStack compareProtection(ItemStack item1, ItemStack item2) {
        if (!(item1.getItem() instanceof ItemArmor) && !(item2.getItem() instanceof ItemArmor))
            return null;

        if (!(item1.getItem() instanceof ItemArmor))
            return item2;

        if (!(item2.getItem() instanceof ItemArmor))
            return item1;

        if (getArmorProtection(item1) > getArmorProtection(item2)) {
            return item1;
        } else if (getArmorProtection(item2) > getArmorProtection(item1)) {
            return item2;
        }

        return null;
    }

    public boolean isContainerFull(Container container) {
        boolean full = true;
        for (Slot slot : container.inventorySlots) {
            if (!slot.getHasStack()) {
                full = false;
                break;
            }
        }
        return full;
    }

    public boolean isContainerEmpty(Container container) {
        boolean empty = true;
        for (Slot slot : container.inventorySlots) {
            if (slot.getHasStack()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public float getSharpnessLevel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
    }

    public float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }

    public int getArmorType(ItemStack stack, boolean equipmentSlot) {
        if (stack.getUnlocalizedName().contains("helmet"))
            return equipmentSlot ? 4 : 5;
        if (stack.getUnlocalizedName().contains("chestplate"))
            return equipmentSlot ? 3 : 6;
        if (stack.getUnlocalizedName().contains("leggings"))
            return equipmentSlot ? 2 : 7;
        if (stack.getUnlocalizedName().contains("boots"))
            return equipmentSlot ? 1 : 8;
        return -1;
    }

    public double getArmorProtection(ItemStack armorStack) {
        if (!(armorStack.getItem() instanceof ItemArmor))
            return 0.0;

        final ItemArmor armorItem = (ItemArmor) armorStack.getItem();
        final double protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, armorStack);

        return armorItem.damageReduceAmount + ((6 + protectionLevel * protectionLevel) * 0.75 / 3);

    }
	public interface Action {
	    void execute();
	}
}
