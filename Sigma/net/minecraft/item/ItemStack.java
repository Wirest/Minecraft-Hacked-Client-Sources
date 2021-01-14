package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class ItemStack {
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.###");

    /**
     * Size of the stack.
     */
    public int stackSize;

    /**
     * Number of animation frames to go when receiving an item (by walking into
     * it, for example).
     */
    public int animationsToGo;
    private Item item;

    /**
     * A NBTTagMap containing data about an ItemStack. Can only be used for non
     * stackable items
     */
    private NBTTagCompound stackTagCompound;
    private int itemDamage;

    /**
     * Item frame this stack is on, or null if not on an item frame.
     */
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;
    private static final String __OBFID = "CL_00000043";

    public ItemStack(Block blockIn) {
        this(blockIn, 1);
    }

    public ItemStack(Block blockIn, int amount) {
        this(blockIn, amount, 0);
    }

    public ItemStack(Block blockIn, int amount, int meta) {
        this(Item.getItemFromBlock(blockIn), amount, meta);
    }

    public ItemStack(Item itemIn) {
        this(itemIn, 1);
    }

    public ItemStack(Item itemIn, int amount) {
        this(itemIn, amount, 0);
    }

    public ItemStack(Item itemIn, int amount, int meta) {
        canDestroyCacheBlock = null;
        canDestroyCacheResult = false;
        canPlaceOnCacheBlock = null;
        canPlaceOnCacheResult = false;
        item = itemIn;
        stackSize = amount;
        itemDamage = meta;

        if (itemDamage < 0) {
            itemDamage = 0;
        }
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt) {
        ItemStack var1 = new ItemStack();
        var1.readFromNBT(nbt);
        return var1.getItem() != null ? var1 : null;
    }

    private ItemStack() {
        canDestroyCacheBlock = null;
        canDestroyCacheResult = false;
        canPlaceOnCacheBlock = null;
        canPlaceOnCacheResult = false;
    }

    /**
     * Splits off a stack of the given amount of this stack and reduces this
     * stack by the amount.
     */
    public ItemStack splitStack(int amount) {
        ItemStack var2 = new ItemStack(item, amount, itemDamage);

        if (stackTagCompound != null) {
            var2.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
        }

        stackSize -= amount;
        return var2;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Called when the player uses this ItemStack on a Block (right-click).
     * Places blocks, etc. (Legacy name: tryPlaceItemIntoWorld)
     */
    public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean var8 = getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);

        if (var8) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(item)]);
        }

        return var8;
    }

    public float getStrVsBlock(Block p_150997_1_) {
        return getItem().getStrVsBlock(this, p_150997_1_);
    }

    /**
     * Called whenever this item stack is equipped and right clicked. Returns
     * the new item stack to put in the position where this item is. Args:
     * world, player
     */
    public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn) {
        return getItem().onItemRightClick(this, worldIn, playerIn);
    }

    /**
     * Called when the item in use count reach 0, e.g. item food eaten. Return
     * the new ItemStack. Args : world, entity
     */
    public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn) {
        return getItem().onItemUseFinish(this, worldIn, playerIn);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        ResourceLocation var2 = (ResourceLocation) Item.itemRegistry.getNameForObject(item);
        nbt.setString("id", var2 == null ? "minecraft:air" : var2.toString());
        nbt.setByte("Count", (byte) stackSize);
        nbt.setShort("Damage", (short) itemDamage);

        if (stackTagCompound != null) {
            nbt.setTag("tag", stackTagCompound);
        }

        return nbt;
    }

    /**
     * Read the stack fields from a NBT object.
     */
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("id", 8)) {
            item = Item.getByNameOrId(nbt.getString("id"));
        } else {
            item = Item.getItemById(nbt.getShort("id"));
        }

        stackSize = nbt.getByte("Count");
        itemDamage = nbt.getShort("Damage");

        if (itemDamage < 0) {
            itemDamage = 0;
        }

        if (nbt.hasKey("tag", 10)) {
            stackTagCompound = nbt.getCompoundTag("tag");

            if (item != null) {
                item.updateItemStackNBT(stackTagCompound);
            }
        }
    }

    /**
     * Returns maximum size of the stack.
     */
    public int getMaxStackSize() {
        return getItem().getItemStackLimit();
    }

    /**
     * Returns true if the ItemStack can hold 2 or more units of the item.
     */
    public boolean isStackable() {
        return getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged());
    }

    /**
     * true if this itemStack is damageable
     */
    public boolean isItemStackDamageable() {
        return item == null ? false : (item.getMaxDamage() <= 0 ? false : !hasTagCompound() || !getTagCompound().getBoolean("Unbreakable"));
    }

    public boolean getHasSubtypes() {
        return item.getHasSubtypes();
    }

    /**
     * returns true when a damageable item is damaged
     */
    public boolean isItemDamaged() {
        return isItemStackDamageable() && itemDamage > 0;
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public int getMetadata() {
        return itemDamage;
    }

    public void setItemDamage(int meta) {
        itemDamage = meta;

        if (itemDamage < 0) {
            itemDamage = 0;
        }
    }

    /**
     * Returns the max damage an item in the stack can take.
     */
    public int getMaxDamage() {
        return item.getMaxDamage();
    }

    /**
     * Attempts to damage the ItemStack with par1 amount of damage, If the
     * ItemStack has the Unbreaking enchantment there is a chance for each point
     * of damage to be negated. Returns true if it takes more damage than
     * getMaxDamage(). Returns false otherwise or if the ItemStack can't be
     * damaged or if all points of damage are negated.
     */
    public boolean attemptDamageItem(int amount, Random rand) {
        if (!isItemStackDamageable()) {
            return false;
        } else {
            if (amount > 0) {
                int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
                int var4 = 0;

                for (int var5 = 0; var3 > 0 && var5 < amount; ++var5) {
                    if (EnchantmentDurability.negateDamage(this, var3, rand)) {
                        ++var4;
                    }
                }

                amount -= var4;

                if (amount <= 0) {
                    return false;
                }
            }

            itemDamage += amount;
            return itemDamage > getMaxDamage();
        }
    }

    /**
     * Damages the item in the ItemStack
     */
    public void damageItem(int amount, EntityLivingBase entityIn) {
        if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).capabilities.isCreativeMode) {
            if (isItemStackDamageable()) {
                if (attemptDamageItem(amount, entityIn.getRNG())) {
                    entityIn.renderBrokenItemStack(this);
                    --stackSize;

                    if (entityIn instanceof EntityPlayer) {
                        EntityPlayer var3 = (EntityPlayer) entityIn;
                        var3.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(item)]);

                        if (stackSize == 0 && getItem() instanceof ItemBow) {
                            var3.destroyCurrentEquippedItem();
                        }
                    }

                    if (stackSize < 0) {
                        stackSize = 0;
                    }

                    itemDamage = 0;
                }
            }
        }
    }

    /**
     * Calls the corresponding fct in di
     */
    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
        boolean var3 = item.hitEntity(this, entityIn, playerIn);

        if (var3) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(item)]);
        }
    }

    /**
     * Called when a Block is destroyed using this ItemStack
     *
     * @param blockIn The block being destroyed
     */
    public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn) {
        boolean var5 = item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);

        if (var5) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(item)]);
        }
    }

    /**
     * Check whether the given Block can be harvested using this ItemStack.
     */
    public boolean canHarvestBlock(Block p_150998_1_) {
        return item.canHarvestBlock(p_150998_1_);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn) {
        return item.itemInteractionForEntity(this, playerIn, entityIn);
    }

    /**
     * Returns a new stack with the same properties.
     */
    public ItemStack copy() {
        ItemStack var1 = new ItemStack(item, stackSize, itemDamage);

        if (stackTagCompound != null) {
            var1.stackTagCompound = (NBTTagCompound) stackTagCompound.copy();
        }

        return var1;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? (stackA.stackTagCompound == null && stackB.stackTagCompound != null ? false : stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)) : false);
    }

    /**
     * compares ItemStack argument1 with ItemStack argument2; returns true if
     * both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemStackEqual(stackB) : false);
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if
     * both ItemStacks are equal
     */
    private boolean isItemStackEqual(ItemStack other) {
        return stackSize != other.stackSize ? false : (item != other.item ? false : (itemDamage != other.itemDamage ? false : (stackTagCompound == null && other.stackTagCompound != null ? false : stackTagCompound == null || stackTagCompound.equals(other.stackTagCompound))));
    }

    /**
     * Compares Item and damage value of the two stacks
     */
    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemEqual(stackB) : false);
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if
     * the Items contained in both ItemStacks are equal
     */
    public boolean isItemEqual(ItemStack other) {
        return other != null && item == other.item && itemDamage == other.itemDamage;
    }

    public String getUnlocalizedName() {
        return item.getUnlocalizedName(this);
    }

    /**
     * Creates a copy of a ItemStack, a null parameters will return a null.
     */
    public static ItemStack copyItemStack(ItemStack stack) {
        return stack == null ? null : stack.copy();
    }

    @Override
    public String toString() {
        return stackSize + "x" + item.getUnlocalizedName() + "@" + itemDamage;
    }

    /**
     * Called each tick as long the ItemStack in on player inventory. Used to
     * progress the pickup animation and update maps.
     */
    public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if (animationsToGo > 0) {
            --animationsToGo;
        }

        item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
        playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(item)], amount);
        item.onCreated(this, worldIn, playerIn);
    }

    public boolean getIsItemStackEqual(ItemStack p_179549_1_) {
        return isItemStackEqual(p_179549_1_);
    }

    public int getMaxItemUseDuration() {
        return getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction() {
        return getItem().getItemUseAction(this);
    }

    /**
     * Called when the player releases the use item button. Args: world,
     * entityplayer, itemInUseCount
     *
     * @param timeLeft The amount of ticks left before the using would have been
     *                 complete
     */
    public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft) {
        getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
    }

    /**
     * Returns true if the ItemStack has an NBTTagCompound. Currently used to
     * store enchantments.
     */
    public boolean hasTagCompound() {
        return stackTagCompound != null;
    }

    /**
     * Returns the NBTTagCompound of the ItemStack.
     */
    public NBTTagCompound getTagCompound() {
        return stackTagCompound;
    }

    /**
     * Get an NBTTagCompound from this stack's NBT data.
     *
     * @param key    The NBTTagCompound to get
     * @param create Whether a new, empty compound should be created if none is
     *               present yet
     */
    public NBTTagCompound getSubCompound(String key, boolean create) {
        if (stackTagCompound != null && stackTagCompound.hasKey(key, 10)) {
            return stackTagCompound.getCompoundTag(key);
        } else if (create) {
            NBTTagCompound var3 = new NBTTagCompound();
            setTagInfo(key, var3);
            return var3;
        } else {
            return null;
        }
    }

    public NBTTagList getEnchantmentTagList() {
        return stackTagCompound == null ? null : stackTagCompound.getTagList("ench", 10);
    }

    /**
     * Assigns a NBTTagCompound to the ItemStack, minecraft validates that only
     * non-stackable items can have it.
     */
    public void setTagCompound(NBTTagCompound nbt) {
        stackTagCompound = nbt;
    }

    /**
     * returns the display name of the itemstack
     */
    public String getDisplayName() {
        String var1 = getItem().getItemStackDisplayName(this);

        if (stackTagCompound != null && stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound var2 = stackTagCompound.getCompoundTag("display");

            if (var2.hasKey("Name", 8)) {
                var1 = var2.getString("Name");
            }
        }

        return var1;
    }

    public ItemStack setStackDisplayName(String p_151001_1_) {
        if (stackTagCompound == null) {
            stackTagCompound = new NBTTagCompound();
        }

        if (!stackTagCompound.hasKey("display", 10)) {
            stackTagCompound.setTag("display", new NBTTagCompound());
        }

        stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
        return this;
    }

    /**
     * Clear any custom name set for this ItemStack
     */
    public void clearCustomName() {
        if (stackTagCompound != null) {
            if (stackTagCompound.hasKey("display", 10)) {
                NBTTagCompound var1 = stackTagCompound.getCompoundTag("display");
                var1.removeTag("Name");

                if (var1.hasNoTags()) {
                    stackTagCompound.removeTag("display");

                    if (stackTagCompound.hasNoTags()) {
                        setTagCompound((NBTTagCompound) null);
                    }
                }
            }
        }
    }

    /**
     * Returns true if the itemstack has a display name
     */
    public boolean hasDisplayName() {
        return stackTagCompound == null ? false : (!stackTagCompound.hasKey("display", 10) ? false : stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
    }

    /**
     * Return a list of strings containing information about the item
     *
     * @param advanced Whether the "Advanced Tooltips" setting is active
     */
    public List getTooltip(EntityPlayer playerIn, boolean advanced) {
        ArrayList var3 = Lists.newArrayList();
        String var4 = getDisplayName();

        if (hasDisplayName()) {
            var4 = EnumChatFormatting.ITALIC + var4;
        }

        var4 = var4 + EnumChatFormatting.RESET;

        if (advanced) {
            String var5 = "";

            if (var4.length() > 0) {
                var4 = var4 + " (";
                var5 = ")";
            }

            int var6 = Item.getIdFromItem(item);

            if (getHasSubtypes()) {
                var4 = var4 + String.format("#%04d/%d%s", new Object[]{Integer.valueOf(var6), Integer.valueOf(itemDamage), var5});
            } else {
                var4 = var4 + String.format("#%04d%s", new Object[]{Integer.valueOf(var6), var5});
            }
        } else if (!hasDisplayName() && item == Items.filled_map) {
            var4 = var4 + " #" + itemDamage;
        }

        var3.add(var4);
        int var14 = 0;

        if (hasTagCompound() && stackTagCompound.hasKey("HideFlags", 99)) {
            var14 = stackTagCompound.getInteger("HideFlags");
        }

        if ((var14 & 32) == 0) {
            item.addInformation(this, playerIn, var3, advanced);
        }

        NBTTagList var18;
        int var20;

        if (hasTagCompound()) {
            if ((var14 & 1) == 0) {
                NBTTagList var15 = getEnchantmentTagList();

                if (var15 != null) {
                    for (int var7 = 0; var7 < var15.tagCount(); ++var7) {
                        short var8 = var15.getCompoundTagAt(var7).getShort("id");
                        short var9 = var15.getCompoundTagAt(var7).getShort("lvl");

                        if (Enchantment.func_180306_c(var8) != null) {
                            var3.add(Enchantment.func_180306_c(var8).getTranslatedName(var9));
                        }
                    }
                }
            }

            if (stackTagCompound.hasKey("display", 10)) {
                NBTTagCompound var16 = stackTagCompound.getCompoundTag("display");

                if (var16.hasKey("color", 3)) {
                    if (advanced) {
                        var3.add("Color: #" + Integer.toHexString(var16.getInteger("color")).toUpperCase());
                    } else {
                        var3.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
                    }
                }

                if (var16.getTagType("Lore") == 9) {
                    var18 = var16.getTagList("Lore", 8);

                    if (var18.tagCount() > 0) {
                        for (var20 = 0; var20 < var18.tagCount(); ++var20) {
                            var3.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + var18.getStringTagAt(var20));
                        }
                    }
                }
            }
        }

        Multimap var17 = getAttributeModifiers();

        if (!var17.isEmpty() && (var14 & 2) == 0) {
            var3.add("");
            Iterator var19 = var17.entries().iterator();

            while (var19.hasNext()) {
                Entry var21 = (Entry) var19.next();
                AttributeModifier var22 = (AttributeModifier) var21.getValue();
                double var10 = var22.getAmount();

                if (var22.getID() == Item.itemModifierUUID) {
                    var10 += EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }

                double var12;

                if (var22.getOperation() != 1 && var22.getOperation() != 2) {
                    var12 = var10;
                } else {
                    var12 = var10 * 100.0D;
                }

                if (var10 > 0.0D) {
                    var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var22.getOperation(), new Object[]{ItemStack.DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String) var21.getKey())}));
                } else if (var10 < 0.0D) {
                    var12 *= -1.0D;
                    var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var22.getOperation(), new Object[]{ItemStack.DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String) var21.getKey())}));
                }
            }
        }

        if (hasTagCompound() && getTagCompound().getBoolean("Unbreakable") && (var14 & 4) == 0) {
            var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
        }

        Block var23;

        if (hasTagCompound() && stackTagCompound.hasKey("CanDestroy", 9) && (var14 & 8) == 0) {
            var18 = stackTagCompound.getTagList("CanDestroy", 8);

            if (var18.tagCount() > 0) {
                var3.add("");
                var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));

                for (var20 = 0; var20 < var18.tagCount(); ++var20) {
                    var23 = Block.getBlockFromName(var18.getStringTagAt(var20));

                    if (var23 != null) {
                        var3.add(EnumChatFormatting.DARK_GRAY + var23.getLocalizedName());
                    } else {
                        var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (hasTagCompound() && stackTagCompound.hasKey("CanPlaceOn", 9) && (var14 & 16) == 0) {
            var18 = stackTagCompound.getTagList("CanPlaceOn", 8);

            if (var18.tagCount() > 0) {
                var3.add("");
                var3.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));

                for (var20 = 0; var20 < var18.tagCount(); ++var20) {
                    var23 = Block.getBlockFromName(var18.getStringTagAt(var20));

                    if (var23 != null) {
                        var3.add(EnumChatFormatting.DARK_GRAY + var23.getLocalizedName());
                    } else {
                        var3.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }

        if (advanced) {
            if (isItemDamaged()) {
                var3.add("Durability: " + (getMaxDamage() - getItemDamage()) + " / " + getMaxDamage());
            }

            var3.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation) Item.itemRegistry.getNameForObject(item)).toString());

            if (hasTagCompound()) {
                var3.add(EnumChatFormatting.DARK_GRAY + "NBT: " + getTagCompound().getKeySet().size() + " tag(s)");
            }
        }

        return var3;
    }

    public boolean hasEffect() {
        return getItem().hasEffect(this);
    }

    public EnumRarity getRarity() {
        return getItem().getRarity(this);
    }

    /**
     * True if it is a tool and has no enchantments to begin with
     */
    public boolean isItemEnchantable() {
        return !getItem().isItemTool(this) ? false : !isItemEnchanted();
    }

    /**
     * Adds an enchantment with a desired level on the ItemStack.
     */
    public void addEnchantment(Enchantment ench, int level) {
        if (stackTagCompound == null) {
            setTagCompound(new NBTTagCompound());
        }

        if (!stackTagCompound.hasKey("ench", 9)) {
            stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 = stackTagCompound.getTagList("ench", 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short) ench.effectId);
        var4.setShort("lvl", ((byte) level));
        var3.appendTag(var4);
    }

    /**
     * True if the item has enchantment data
     */
    public boolean isItemEnchanted() {
        return stackTagCompound != null && stackTagCompound.hasKey("ench", 9);
    }

    public void setTagInfo(String key, NBTBase value) {
        if (stackTagCompound == null) {
            setTagCompound(new NBTTagCompound());
        }

        stackTagCompound.setTag(key, value);
    }

    public boolean canEditBlocks() {
        return getItem().canItemEditBlocks();
    }

    /**
     * Return whether this stack is on an item frame.
     */
    public boolean isOnItemFrame() {
        return itemFrame != null;
    }

    /**
     * Set the item frame this stack is on.
     */
    public void setItemFrame(EntityItemFrame frame) {
        itemFrame = frame;
    }

    /**
     * Return the item frame this stack is on. Returns null if not on an item
     * frame.
     */
    public EntityItemFrame getItemFrame() {
        return itemFrame;
    }

    /**
     * Get this stack's repair cost, or 0 if no repair cost is defined.
     */
    public int getRepairCost() {
        return hasTagCompound() && stackTagCompound.hasKey("RepairCost", 3) ? stackTagCompound.getInteger("RepairCost") : 0;
    }

    /**
     * Set this stack's repair cost.
     */
    public void setRepairCost(int cost) {
        if (!hasTagCompound()) {
            stackTagCompound = new NBTTagCompound();
        }

        stackTagCompound.setInteger("RepairCost", cost);
    }

    /**
     * Gets the attribute modifiers for this ItemStack. Will check for an NBT
     * tag list containing modifiers for the stack.
     */
    public Multimap getAttributeModifiers() {
        Object var1;

        if (hasTagCompound() && stackTagCompound.hasKey("AttributeModifiers", 9)) {
            var1 = HashMultimap.create();
            NBTTagList var2 = stackTagCompound.getTagList("AttributeModifiers", 10);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);

                if (var5 != null && var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L) {
                    ((Multimap) var1).put(var4.getString("AttributeName"), var5);
                }
            }
        } else {
            var1 = getItem().getItemAttributeModifiers();
        }

        return (Multimap) var1;
    }

    public void setItem(Item p_150996_1_) {
        item = p_150996_1_;
    }

    /**
     * Get a ChatComponent for this Item's display name that shows this Item on
     * hover
     */
    public IChatComponent getChatComponent() {
        ChatComponentText var1 = new ChatComponentText(getDisplayName());

        if (hasDisplayName()) {
            var1.getChatStyle().setItalic(Boolean.valueOf(true));
        }

        IChatComponent var2 = (new ChatComponentText("[")).appendSibling(var1).appendText("]");

        if (item != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            writeToNBT(var3);
            var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
            var2.getChatStyle().setColor(getRarity().rarityColor);
        }

        return var2;
    }

    public boolean canDestroy(Block blockIn) {
        if (blockIn == canDestroyCacheBlock) {
            return canDestroyCacheResult;
        } else {
            canDestroyCacheBlock = blockIn;

            if (hasTagCompound() && stackTagCompound.hasKey("CanDestroy", 9)) {
                NBTTagList var2 = stackTagCompound.getTagList("CanDestroy", 8);

                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));

                    if (var4 == blockIn) {
                        canDestroyCacheResult = true;
                        return true;
                    }
                }
            }

            canDestroyCacheResult = false;
            return false;
        }
    }

    public boolean canPlaceOn(Block blockIn) {
        if (blockIn == canPlaceOnCacheBlock) {
            return canPlaceOnCacheResult;
        } else {
            canPlaceOnCacheBlock = blockIn;

            if (hasTagCompound() && stackTagCompound.hasKey("CanPlaceOn", 9)) {
                NBTTagList var2 = stackTagCompound.getTagList("CanPlaceOn", 8);

                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));

                    if (var4 == blockIn) {
                        canPlaceOnCacheResult = true;
                        return true;
                    }
                }
            }

            canPlaceOnCacheResult = false;
            return false;
        }
    }
}
