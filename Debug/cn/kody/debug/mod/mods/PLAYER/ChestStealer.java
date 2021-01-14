package cn.kody.debug.mod.mods.PLAYER;

import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.time.Timer;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ChestStealer
extends Mod {
    private Timer timer = new Timer();
    private Timer stealTimer = new Timer();
    private boolean isStealing;
    public Value<Double> delay = new Value<Double>("Cheststealer_Delay", 150.0, 0.0, 300.0, 10.0);
    public Value<Boolean> close = new Value<Boolean>("Cheststealer_Close", true);
    public Value<Boolean> ignore = new Value<Boolean>("Cheststealer_Ignore", true);
    public Value<Boolean> hypixel = new Value<Boolean>("Cheststealer_Hypixel", true);

    public ChestStealer() {
        super("ChestStealer", "Chest Stealer", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (this.mc.currentScreen instanceof GuiChest) {
            String[] list;
            GuiChest guiChest = (GuiChest)this.mc.currentScreen;
            String name = guiChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
            for (String str : list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor", "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe", "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock"}) {
                if (!name.contains(str)) continue;
                return;
            }
            this.isStealing = true;
            boolean full = true;
            for (ItemStack item : Minecraft.thePlayer.inventory.mainInventory) {
                if (item != null) continue;
                full = false;
                break;
            }
            boolean containsItems = false;
            if (!full) {
                ItemStack stack;
                int index;
                for (index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
                    stack = guiChest.lowerChestInventory.getStackInSlot(index);
                    if (stack == null || this.isBad(stack)) continue;
                    containsItems = true;
                    break;
                }
                if (containsItems) {
                    for (index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); ++index) {
                        stack = guiChest.lowerChestInventory.getStackInSlot(index);
                        if (stack == null || !this.timer.delay(this.delay.getValueState().intValue()) || this.isBad(stack)) continue;
                        Minecraft.playerController.windowClick(guiChest.inventorySlots.windowId, index, 0, 1, Minecraft.thePlayer);
                        if (this.hypixel.getValueState().booleanValue()) {
                            Minecraft.playerController.windowClick(guiChest.inventorySlots.windowId, index, 1, 1, Minecraft.thePlayer);
                        }
                        this.timer.reset();
                    }
                } else if (this.close.getValueState().booleanValue()) {
                    Minecraft.thePlayer.closeScreen();
                    this.isStealing = false;
                }
            } else if (this.close.getValueState().booleanValue()) {
                Minecraft.thePlayer.closeScreen();
                this.isStealing = false;
            }
        } else {
            this.isStealing = false;
        }
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        MovingObjectPosition rayResult;
        EnumFacing direction = null;
        if (!this.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.UP;
        }
        if ((rayResult = this.mc.theWorld.rayTraceBlocks(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ), new Vec3((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5))) != null) {
            return rayResult.sideHit;
        }
        return direction;
    }

    private boolean isBad(ItemStack item) {
        if (!this.ignore.getValueState().booleanValue()) {
            return false;
        }
        ItemStack is = null;
        float lastDamage = -1.0f;
        for (int i = 9; i < 45; ++i) {
            ItemStack is1;
            if (!Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((is1 = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem() instanceof ItemSword) || !(item.getItem() instanceof ItemSword) || lastDamage >= this.getDamage(is1)) continue;
            lastDamage = this.getDamage(is1);
            is = is1;
        }
        if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = this.getDamage(is);
            float itemDamage = this.getDamage(item);
            if (itemDamage > currentDamage) {
                return false;
            }
        }
        return item != null && (item.getItem().getUnlocalizedName().contains("tnt") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("compass") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("fish") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemGlassBottle || item.getItem() instanceof ItemTool || item.getItem().getUnlocalizedName().contains("piston") || item.getItem().getUnlocalizedName().contains("potion") && this.isBadPotion(item));
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect o : potion.getEffects(stack)) {
                    PotionEffect effect = o;
                    if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword)stack.getItem()).getDamageVsEntity();
    }
}

