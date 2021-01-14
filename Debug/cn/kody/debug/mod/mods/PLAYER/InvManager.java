package cn.kody.debug.mod.mods.PLAYER;

import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.Timer;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvManager
extends Mod {
    Timer timer = new Timer();
    ArrayList<Integer> whitelistedItems = new ArrayList();
    private Value<Double> maxblocks = new Value<Double>("InvManager_MaxBlocks", 128.0, 0.0, 256.0, 8.0);
    private Value<Double> delay = new Value<Double>("InvManager_Delay", 200.0, 0.0, 500.0, 50.0);
    private Value<Boolean> openinv = new Value<Boolean>("InvManager_OpenInv", false);
    public static int weaponSlot = 36;
    public static int pickaxeSlot = 37;
    public static int axeSlot = 38;
    public static int shovelSlot = 39;
    private static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);

    public InvManager() {
        super("InvManager", "Inventory Manager", Category.PLAYER);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.openinv.getValueState().booleanValue() && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) {
            if (this.timer.delay(this.delay.getValueState().intValue()) && weaponSlot >= 36) {
                if (!Minecraft.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    this.getBestWeapon(weaponSlot);
                } else if (!this.isBestWeapon(Minecraft.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
                    this.getBestWeapon(weaponSlot);
                }
            }
            if (this.timer.delay(this.delay.getValueState().intValue()) && pickaxeSlot >= 36) {
                this.getBestPickaxe(pickaxeSlot);
            }
            if (this.timer.delay(this.delay.getValueState().intValue()) && shovelSlot >= 36) {
                this.getBestShovel(shovelSlot);
            }
            if (this.timer.delay(this.delay.getValueState().intValue()) && axeSlot >= 36) {
                this.getBestAxe(axeSlot);
            }
            if (this.timer.delay(this.delay.getValueState().intValue())) {
                for (int i = 9; i < 45; ++i) {
                    ItemStack is;
                    if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.shouldDrop(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack(), i)) continue;
                    this.drop(i);
                    this.timer.reset();
                    if (this.delay.getValueState() > 0.0) break;
                }
            }
        }
    }

    public void shiftClick(int slot) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.thePlayer);
    }

    public void swap(int slot1, int hotbarSlot) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, Minecraft.thePlayer);
    }

    public void drop(int slot) {
        Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, 1, 4, Minecraft.thePlayer);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.getDamage(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= damage || !(is.getItem() instanceof ItemSword)) continue;
            return false;
        }
        if (stack.getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) || this.getDamage(is) <= 0.0f || !(is.getItem() instanceof ItemSword)) continue;
            this.swap(i, slot - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool)item;
            damage += tool.getDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getAttackDamage();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("k||")) {
            return false;
        }
        if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemAppleGold) {
            return false;
        }
        if (stack.getItem() instanceof ItemBlock && (double)this.getBlockCount() > this.maxblocks.getValueState() && !(stack.getItem() instanceof ItemSkull)) {
            return true;
        }
        if (stack.getItem() instanceof ItemBlock && (double)this.getBlockCount() <= this.maxblocks.getValueState() || stack.getItem() instanceof ItemSkull) {
            return false;
        }
        if (slot == weaponSlot && this.isBestWeapon(Minecraft.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()) || slot == pickaxeSlot && this.isBestPickaxe(Minecraft.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0 || slot == axeSlot && this.isBestAxe(Minecraft.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0 || slot == shovelSlot && this.isBestShovel(Minecraft.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                ItemStack is;
                if (Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() && InvManager.isBestArmor(is = Minecraft.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type) || !InvManager.isBestArmor(stack, type)) continue;
                return false;
            }
        }
        if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if (stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("compass") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston")) {
            return true;
        }
        return false;
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = InvManager.getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || InvManager.getProtection(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= prot || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return prot;
    }

    public ArrayList<Integer> getWhitelistedItem() {
        return this.whitelistedItems;
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || blacklistedBlocks.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private void getBestPickaxe(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestPickaxe(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) || pickaxeSlot == i || this.isBestWeapon(is)) continue;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                this.swap(i, pickaxeSlot - 36);
                this.timer.reset();
                if (this.delay.getValueState().intValue() <= 0) continue;
                return;
            }
            if (this.isBestPickaxe(Minecraft.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) continue;
            this.swap(i, pickaxeSlot - 36);
            this.timer.reset();
            if (this.delay.getValueState().intValue() <= 0) continue;
            return;
        }
    }

    private void getBestShovel(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestShovel(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) || shovelSlot == i || this.isBestWeapon(is)) continue;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                this.swap(i, shovelSlot - 36);
                this.timer.reset();
                if (this.delay.getValueState().intValue() <= 0) continue;
                return;
            }
            if (this.isBestShovel(Minecraft.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) continue;
            this.swap(i, shovelSlot - 36);
            this.timer.reset();
            if (this.delay.getValueState().intValue() <= 0) continue;
            return;
        }
    }

    private void getBestAxe(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestAxe(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) || axeSlot == i || this.isBestWeapon(is)) continue;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                this.swap(i, axeSlot - 36);
                this.timer.reset();
                if (this.delay.getValueState().intValue() <= 0) continue;
                return;
            }
            if (this.isBestAxe(Minecraft.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) continue;
            this.swap(i, axeSlot - 36);
            this.timer.reset();
            if (this.delay.getValueState().intValue() <= 0) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.getToolEffect(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value || !(is.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.getToolEffect(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value || !(is.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || this.getToolEffect(is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()) <= value || !(is.getItem() instanceof ItemAxe) || this.isBestWeapon(stack)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (potion.getEffects(stack) == null) {
                return true;
            }
            for (PotionEffect o : potion.getEffects(stack)) {
                PotionEffect effect = o;
                if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                return true;
            }
        }
        return false;
    }

    boolean invContainsType(int type) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor)item;
            if (type != armor.armorType) continue;
            return true;
        }
        return false;
    }
}

