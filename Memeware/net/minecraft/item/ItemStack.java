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
     * Number of animation frames to go when receiving an item (by walking into it, for example).
     */
    public int animationsToGo;
    private Item item;

    /**
     * A NBTTagMap containing data about an ItemStack. Can only be used for non stackable items
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
        this.canDestroyCacheBlock = null;
        this.canDestroyCacheResult = false;
        this.canPlaceOnCacheBlock = null;
        this.canPlaceOnCacheResult = false;
        this.item = itemIn;
        this.stackSize = amount;
        this.itemDamage = meta;

        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound nbt) {
        ItemStack var1 = new ItemStack();
        var1.readFromNBT(nbt);
        return var1.getItem() != null ? var1 : null;
    }

    private ItemStack() {
        this.canDestroyCacheBlock = null;
        this.canDestroyCacheResult = false;
        this.canPlaceOnCacheBlock = null;
        this.canPlaceOnCacheResult = false;
    }

    /**
     * Splits off a stack of the given amount of this stack and reduces this stack by the amount.
     */
    public ItemStack splitStack(int amount) {
        ItemStack var2 = new ItemStack(this.item, amount, this.itemDamage);

        if (this.stackTagCompound != null) {
            var2.stackTagCompound = (NBTTagCompound) this.stackTagCompound.copy();
        }

        this.stackSize -= amount;
        return var2;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Called when the player uses this ItemStack on a Block (right-click). Places blocks, etc. (Legacy name:
     * tryPlaceItemIntoWorld)
     */
    public boolean onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean var8 = this.getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);

        if (var8) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }

        return var8;
    }

    public float getStrVsBlock(Block p_150997_1_) {
        return this.getItem().getStrVsBlock(this, p_150997_1_);
    }

    /**
     * Called whenever this item stack is equipped and right clicked. Returns the new item stack to put in the position
     * where this item is. Args: world, player
     */
    public ItemStack useItemRightClick(World worldIn, EntityPlayer playerIn) {
        return this.getItem().onItemRightClick(this, worldIn, playerIn);
    }

    /**
     * Called when the item in use count reach 0, e.g. item food eaten. Return the new ItemStack. Args : world, entity
     */
    public ItemStack onItemUseFinish(World worldIn, EntityPlayer playerIn) {
        return this.getItem().onItemUseFinish(this, worldIn, playerIn);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        ResourceLocation var2 = (ResourceLocation) Item.itemRegistry.getNameForObject(this.item);
        nbt.setString("id", var2 == null ? "minecraft:air" : var2.toString());
        nbt.setByte("Count", (byte) this.stackSize);
        nbt.setShort("Damage", (short) this.itemDamage);

        if (this.stackTagCompound != null) {
            nbt.setTag("tag", this.stackTagCompound);
        }

        return nbt;
    }

    /**
     * Read the stack fields from a NBT object.
     */
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("id", 8)) {
            this.item = Item.getByNameOrId(nbt.getString("id"));
        } else {
            this.item = Item.getItemById(nbt.getShort("id"));
        }

        this.stackSize = nbt.getByte("Count");
        this.itemDamage = nbt.getShort("Damage");

        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }

        if (nbt.hasKey("tag", 10)) {
            this.stackTagCompound = nbt.getCompoundTag("tag");

            if (this.item != null) {
                this.item.updateItemStackNBT(this.stackTagCompound);
            }
        }
    }

    /**
     * Returns maximum size of the stack.
     */
    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }

    /**
     * Returns true if the ItemStack can hold 2 or more units of the item.
     */
    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    /**
     * true if this itemStack is damageable
     */
    public boolean isItemStackDamageable() {
        return this.item == null ? false : (this.item.getMaxDamage() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }

    public boolean getHasSubtypes() {
        return this.item.getHasSubtypes();
    }

    /**
     * returns true when a damageable item is damaged
     */
    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }

    public int getItemDamage() {
        return this.itemDamage;
    }

    public int getMetadata() {
        return this.itemDamage;
    }

    public void setItemDamage(int meta) {
        this.itemDamage = meta;

        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }

    /**
     * Returns the max damage an item in the stack can take.
     */
    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }

    /**
     * Attempts to damage the ItemStack with par1 amount of damage, If the ItemStack has the Unbreaking enchantment
     * there is a chance for each point of damage to be negated. Returns true if it takes more damage than
     * getMaxDamage(). Returns false otherwise or if the ItemStack can't be damaged or if all points of damage are
     * negated.
     */
    public boolean attemptDamageItem(int amount, Random rand) {
        if (!this.isItemStackDamageable()) {
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

            this.itemDamage += amount;
            return this.itemDamage > this.getMaxDamage();
        }
    }

    /**
     * Damages the item in the ItemStack
     */
    public void damageItem(int amount, EntityLivingBase entityIn) {
        if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).capabilities.isCreativeMode) {
            if (this.isItemStackDamageable()) {
                if (this.attemptDamageItem(amount, entityIn.getRNG())) {
                    entityIn.renderBrokenItemStack(this);
                    --this.stackSize;

                    if (entityIn instanceof EntityPlayer) {
                        EntityPlayer var3 = (EntityPlayer) entityIn;
                        var3.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);

                        if (this.stackSize == 0 && this.getItem() instanceof ItemBow) {
                            var3.destroyCurrentEquippedItem();
                        }
                    }

                    if (this.stackSize < 0) {
                        this.stackSize = 0;
                    }

                    this.itemDamage = 0;
                }
            }
        }
    }

    /**
     * Calls the corresponding fct in di
     */
    public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
        boolean var3 = this.item.hitEntity(this, entityIn, playerIn);

        if (var3) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    /**
     * Called when a Block is destroyed using this ItemStack
     *
     * @param blockIn The block being destroyed
     */
    public void onBlockDestroyed(World worldIn, Block blockIn, BlockPos pos, EntityPlayer playerIn) {
        boolean var5 = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);

        if (var5) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }

    /**
     * Check whether the given Block can be harvested using this ItemStack.
     */
    public boolean canHarvestBlock(Block p_150998_1_) {
        return this.item.canHarvestBlock(p_150998_1_);
    }

    public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn) {
        return this.item.itemInteractionForEntity(this, playerIn, entityIn);
    }

    /**
     * Returns a new stack with the same properties.
     */
    public ItemStack copy() {
        ItemStack var1 = new ItemStack(this.item, this.stackSize, this.itemDamage);

        if (this.stackTagCompound != null) {
            var1.stackTagCompound = (NBTTagCompound) this.stackTagCompound.copy();
        }

        return var1;
    }

    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? (stackA.stackTagCompound == null && stackB.stackTagCompound != null ? false : stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)) : false);
    }

    /**
     * compares ItemStack argument1 with ItemStack argument2; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemStackEqual(stackB) : false);
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if both ItemStacks are equal
     */
    private boolean isItemStackEqual(ItemStack other) {
        return this.stackSize != other.stackSize ? false : (this.item != other.item ? false : (this.itemDamage != other.itemDamage ? false : (this.stackTagCompound == null && other.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound))));
    }

    /**
     * Compares Item and damage value of the two stacks
     */
    public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
        return stackA == null && stackB == null ? true : (stackA != null && stackB != null ? stackA.isItemEqual(stackB) : false);
    }

    /**
     * compares ItemStack argument to the instance ItemStack; returns true if the Items contained in both ItemStacks are
     * equal
     */
    public boolean isItemEqual(ItemStack other) {
        return other != null && this.item == other.item && this.itemDamage == other.itemDamage;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName(this);
    }

    /**
     * Creates a copy of a ItemStack, a null parameters will return a null.
     */
    public static ItemStack copyItemStack(ItemStack stack) {
        return stack == null ? null : stack.copy();
    }

    public String toString() {
        return this.stackSize + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
    }

    /**
     * Called each tick as long the ItemStack in on player inventory. Used to progress the pickup animation and update
     * maps.
     */
    public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }

        this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
    }

    public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
        playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
        this.item.onCreated(this, worldIn, playerIn);
    }

    public boolean getIsItemStackEqual(ItemStack p_179549_1_) {
        return this.isItemStackEqual(p_179549_1_);
    }

    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }

    /**
     * Called when the player releases the use item button. Args: world, entityplayer, itemInUseCount
     *
     * @param timeLeft The amount of ticks left before the using would have been complete
     */
    public void onPlayerStoppedUsing(World worldIn, EntityPlayer playerIn, int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
    }

    /**
     * Returns true if the ItemStack has an NBTTagCompound. Currently used to store enchantments.
     */
    public boolean hasTagCompound() {
        return this.stackTagCompound != null;
    }

    /**
     * Returns the NBTTagCompound of the ItemStack.
     */
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    /**
     * Get an NBTTagCompound from this stack's NBT data.
     *
     * @param key    The NBTTagCompound to get
     * @param create Whether a new, empty compound should be created if none is present yet
     */
    public NBTTagCompound getSubCompound(String key, boolean create) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            return this.stackTagCompound.getCompoundTag(key);
        } else if (create) {
            NBTTagCompound var3 = new NBTTagCompound();
            this.setTagInfo(key, var3);
            return var3;
        } else {
            return null;
        }
    }

    public NBTTagList getEnchantmentTagList() {
        return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
    }

    /**
     * Assigns a NBTTagCompound to the ItemStack, minecraft validates that only non-stackable items can have it.
     */
    public void setTagCompound(NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }

    /**
     * returns the display name of the itemstack
     */
    public String getDisplayName() {
        String var1 = this.getItem().getItemStackDisplayName(this);

        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("display");

            if (var2.hasKey("Name", 8)) {
                var1 = var2.getString("Name");
            }
        }

        return var1;
    }

    public ItemStack setStackDisplayName(String p_151001_1_) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("display", 10)) {
            this.stackTagCompound.setTag("display", new NBTTagCompound());
        }

        this.stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
        return this;
    }

    /**
     * Clear any custom name set for this ItemStack
     */
    public void clearCustomName() {
        if (this.stackTagCompound != null) {
            if (this.stackTagCompound.hasKey("display", 10)) {
                NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
                var1.removeTag("Name");

                if (var1.hasNoTags()) {
                    this.stackTagCompound.removeTag("display");

                    if (this.stackTagCompound.hasNoTags()) {
                        this.setTagCompound((NBTTagCompound) null);
                    }
                }
            }
        }
    }

    /**
     * Returns true if the itemstack has a display name
     */
    public boolean hasDisplayName() {
        return this.stackTagCompound == null ? false : (!this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
    }

    /**
     * Return a list of strings containing information about the item
     *
     * @param advanced Whether the "Advanced Tooltips" setting is active
     */
    public List getTooltip(EntityPlayer playerIn, boolean advanced) {
        ArrayList var3 = Lists.newArrayList();
        String var4 = this.getDisplayName();

        if (this.hasDisplayName()) {
            var4 = EnumChatFormatting.ITALIC + var4;
        }

        var4 = var4 + EnumChatFormatting.RESET;

        if (advanced) {
            String var5 = "";

            if (var4.length() > 0) {
                var4 = var4 + " (";
                var5 = ")";
            }

            int var6 = Item.getIdFromItem(this.item);

            if (this.getHasSubtypes()) {
                var4 = var4 + String.format("#%04d/%d%s", new Object[]{Integer.valueOf(var6), Integer.valueOf(this.itemDamage), var5});
            } else {
                var4 = var4 + String.format("#%04d%s", new Object[]{Integer.valueOf(var6), var5});
            }
        } else if (!this.hasDisplayName() && this.item == Items.filled_map) {
            var4 = var4 + " #" + this.itemDamage;
        }

        var3.add(var4);
        int var14 = 0;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            var14 = this.stackTagCompound.getInteger("HideFlags");
        }

        if ((var14 & 32) == 0) {
            this.item.addInformation(this, playerIn, var3, advanced);
        }

        NBTTagList var18;
        int var20;

        if (this.hasTagCompound()) {
            if ((var14 & 1) == 0) {
                NBTTagList var15 = this.getEnchantmentTagList();

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

            if (this.stackTagCompound.hasKey("display", 10)) {
                NBTTagCompound var16 = this.stackTagCompound.getCompoundTag("display");

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

        Multimap var17 = this.getAttributeModifiers();

        if (!var17.isEmpty() && (var14 & 2) == 0) {
            var3.add("");
            Iterator var19 = var17.entries().iterator();

            while (var19.hasNext()) {
                Entry var21 = (Entry) var19.next();
                AttributeModifier var22 = (AttributeModifier) var21.getValue();
                double var10 = var22.getAmount();

                if (var22.getID() == Item.itemModifierUUID) {
                    var10 += (double) EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }

                double var12;

                if (var22.getOperation() != 1 && var22.getOperation() != 2) {
                    var12 = var10;
                } else {
                    var12 = var10 * 100.0D;
                }

                if (var10 > 0.0D) {
                    var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var22.getOperation(), new Object[]{DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String) var21.getKey())}));
                } else if (var10 < 0.0D) {
                    var12 *= -1.0D;
                    var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var22.getOperation(), new Object[]{DECIMALFORMAT.format(var12), StatCollector.translateToLocal("attribute.name." + (String) var21.getKey())}));
                }
            }
        }

        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (var14 & 4) == 0) {
            var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
        }

        Block var23;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (var14 & 8) == 0) {
            var18 = this.stackTagCompound.getTagList("CanDestroy", 8);

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

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (var14 & 16) == 0) {
            var18 = this.stackTagCompound.getTagList("CanPlaceOn", 8);

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
            if (this.isItemDamaged()) {
                var3.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
            }

            var3.add(EnumChatFormatting.DARK_GRAY + ((ResourceLocation) Item.itemRegistry.getNameForObject(this.item)).toString());

            if (this.hasTagCompound()) {
                var3.add(EnumChatFormatting.DARK_GRAY + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
            }
        }

        return var3;
    }

    public boolean hasEffect() {
        return this.getItem().hasEffect(this);
    }

    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }

    /**
     * True if it is a tool and has no enchantments to begin with
     */
    public boolean isItemEnchantable() {
        return !this.getItem().isItemTool(this) ? false : !this.isItemEnchanted();
    }

    /**
     * Adds an enchantment with a desired level on the ItemStack.
     */
    public void addEnchantment(Enchantment ench, int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }

        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short) ench.effectId);
        var4.setShort("lvl", (short) ((byte) level));
        var3.appendTag(var4);
    }

    /**
     * True if the item has enchantment data
     */
    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }

    public void setTagInfo(String key, NBTBase value) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }

        this.stackTagCompound.setTag(key, value);
    }

    public boolean canEditBlocks() {
        return this.getItem().canItemEditBlocks();
    }

    /**
     * Return whether this stack is on an item frame.
     */
    public boolean isOnItemFrame() {
        return this.itemFrame != null;
    }

    /**
     * Set the item frame this stack is on.
     */
    public void setItemFrame(EntityItemFrame frame) {
        this.itemFrame = frame;
    }

    /**
     * Return the item frame this stack is on. Returns null if not on an item frame.
     */
    public EntityItemFrame getItemFrame() {
        return this.itemFrame;
    }

    /**
     * Get this stack's repair cost, or 0 if no repair cost is defined.
     */
    public int getRepairCost() {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    /**
     * Set this stack's repair cost.
     */
    public void setRepairCost(int cost) {
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }

        this.stackTagCompound.setInteger("RepairCost", cost);
    }

    /**
     * Gets the attribute modifiers for this ItemStack.
     * Will check for an NBT tag list containing modifiers for the stack.
     */
    public Multimap getAttributeModifiers() {
        Object var1;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            var1 = HashMultimap.create();
            NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);

                if (var5 != null && var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L) {
                    ((Multimap) var1).put(var4.getString("AttributeName"), var5);
                }
            }
        } else {
            var1 = this.getItem().getItemAttributeModifiers();
        }

        return (Multimap) var1;
    }

    public void setItem(Item p_150996_1_) {
        this.item = p_150996_1_;
    }

    /**
     * Get a ChatComponent for this Item's display name that shows this Item on hover
     */
    public IChatComponent getChatComponent() {
        ChatComponentText var1 = new ChatComponentText(this.getDisplayName());

        if (this.hasDisplayName()) {
            var1.getChatStyle().setItalic(Boolean.valueOf(true));
        }

        IChatComponent var2 = (new ChatComponentText("[")).appendSibling(var1).appendText("]");

        if (this.item != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            this.writeToNBT(var3);
            var2.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var3.toString())));
            var2.getChatStyle().setColor(this.getRarity().rarityColor);
        }

        return var2;
    }

    public boolean canDestroy(Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        } else {
            this.canDestroyCacheBlock = blockIn;

            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
                NBTTagList var2 = this.stackTagCompound.getTagList("CanDestroy", 8);

                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));

                    if (var4 == blockIn) {
                        this.canDestroyCacheResult = true;
                        return true;
                    }
                }
            }

            this.canDestroyCacheResult = false;
            return false;
        }
    }

    public boolean canPlaceOn(Block blockIn) {
        if (blockIn == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        } else {
            this.canPlaceOnCacheBlock = blockIn;

            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
                NBTTagList var2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);

                for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                    Block var4 = Block.getBlockFromName(var2.getStringTagAt(var3));

                    if (var4 == blockIn) {
                        this.canPlaceOnCacheResult = true;
                        return true;
                    }
                }
            }

            this.canPlaceOnCacheResult = false;
            return false;
        }
    }
}
