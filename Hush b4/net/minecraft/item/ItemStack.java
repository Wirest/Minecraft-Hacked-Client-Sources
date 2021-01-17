// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.HashMultimap;
import java.util.Iterator;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.Map;
import net.minecraft.util.StatCollector;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import java.util.Random;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.nbt.NBTTagCompound;
import java.text.DecimalFormat;

public final class ItemStack
{
    public static final DecimalFormat DECIMALFORMAT;
    public int stackSize;
    public int animationsToGo;
    private Item item;
    private NBTTagCompound stackTagCompound;
    private int itemDamage;
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;
    
    static {
        DECIMALFORMAT = new DecimalFormat("#.###");
    }
    
    public ItemStack(final Block blockIn) {
        this(blockIn, 1);
    }
    
    public ItemStack(final Block blockIn, final int amount) {
        this(blockIn, amount, 0);
    }
    
    public ItemStack(final Block blockIn, final int amount, final int meta) {
        this(Item.getItemFromBlock(blockIn), amount, meta);
    }
    
    public ItemStack(final Item itemIn) {
        this(itemIn, 1);
    }
    
    public ItemStack(final Item itemIn, final int amount) {
        this(itemIn, amount, 0);
    }
    
    public ItemStack(final Item itemIn, final int amount, final int meta) {
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
    
    public static ItemStack loadItemStackFromNBT(final NBTTagCompound nbt) {
        final ItemStack itemstack = new ItemStack();
        itemstack.readFromNBT(nbt);
        return (itemstack.getItem() != null) ? itemstack : null;
    }
    
    private ItemStack() {
        this.canDestroyCacheBlock = null;
        this.canDestroyCacheResult = false;
        this.canPlaceOnCacheBlock = null;
        this.canPlaceOnCacheResult = false;
    }
    
    public ItemStack splitStack(final int amount) {
        final ItemStack itemstack = new ItemStack(this.item, amount, this.itemDamage);
        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        this.stackSize -= amount;
        return itemstack;
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public boolean onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final boolean flag = this.getItem().onItemUse(this, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        if (flag) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
        return flag;
    }
    
    public float getStrVsBlock(final Block blockIn) {
        return this.getItem().getStrVsBlock(this, blockIn);
    }
    
    public ItemStack useItemRightClick(final World worldIn, final EntityPlayer playerIn) {
        return this.getItem().onItemRightClick(this, worldIn, playerIn);
    }
    
    public ItemStack onItemUseFinish(final World worldIn, final EntityPlayer playerIn) {
        return this.getItem().onItemUseFinish(this, worldIn, playerIn);
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        final ResourceLocation resourcelocation = Item.itemRegistry.getNameForObject(this.item);
        nbt.setString("id", (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString());
        nbt.setByte("Count", (byte)this.stackSize);
        nbt.setShort("Damage", (short)this.itemDamage);
        if (this.stackTagCompound != null) {
            nbt.setTag("tag", this.stackTagCompound);
        }
        return nbt;
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        if (nbt.hasKey("id", 8)) {
            this.item = Item.getByNameOrId(nbt.getString("id"));
        }
        else {
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
    
    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }
    
    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }
    
    public boolean isItemStackDamageable() {
        return this.item != null && this.item.getMaxDamage() > 0 && (!this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }
    
    public boolean getHasSubtypes() {
        return this.item.getHasSubtypes();
    }
    
    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }
    
    public int getItemDamage() {
        return this.itemDamage;
    }
    
    public int getMetadata() {
        return this.itemDamage;
    }
    
    public void setItemDamage(final int meta) {
        this.itemDamage = meta;
        if (this.itemDamage < 0) {
            this.itemDamage = 0;
        }
    }
    
    public int getMaxDamage() {
        return this.item.getMaxDamage();
    }
    
    public boolean attemptDamageItem(int amount, final Random rand) {
        if (!this.isItemStackDamageable()) {
            return false;
        }
        if (amount > 0) {
            final int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
            int j = 0;
            for (int k = 0; i > 0 && k < amount; ++k) {
                if (EnchantmentDurability.negateDamage(this, i, rand)) {
                    ++j;
                }
            }
            amount -= j;
            if (amount <= 0) {
                return false;
            }
        }
        this.itemDamage += amount;
        return this.itemDamage > this.getMaxDamage();
    }
    
    public void damageItem(final int amount, final EntityLivingBase entityIn) {
        if ((!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode) && this.isItemStackDamageable() && this.attemptDamageItem(amount, entityIn.getRNG())) {
            entityIn.renderBrokenItemStack(this);
            --this.stackSize;
            if (entityIn instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)entityIn;
                entityplayer.triggerAchievement(StatList.objectBreakStats[Item.getIdFromItem(this.item)]);
                if (this.stackSize == 0 && this.getItem() instanceof ItemBow) {
                    entityplayer.destroyCurrentEquippedItem();
                }
            }
            if (this.stackSize < 0) {
                this.stackSize = 0;
            }
            this.itemDamage = 0;
        }
    }
    
    public void hitEntity(final EntityLivingBase entityIn, final EntityPlayer playerIn) {
        final boolean flag = this.item.hitEntity(this, entityIn, playerIn);
        if (flag) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }
    
    public void onBlockDestroyed(final World worldIn, final Block blockIn, final BlockPos pos, final EntityPlayer playerIn) {
        final boolean flag = this.item.onBlockDestroyed(this, worldIn, blockIn, pos, playerIn);
        if (flag) {
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this.item)]);
        }
    }
    
    public boolean canHarvestBlock(final Block blockIn) {
        return this.item.canHarvestBlock(blockIn);
    }
    
    public boolean interactWithEntity(final EntityPlayer playerIn, final EntityLivingBase entityIn) {
        return this.item.itemInteractionForEntity(this, playerIn, entityIn);
    }
    
    public ItemStack copy() {
        final ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }
        return itemstack;
    }
    
    public static boolean areItemStackTagsEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && (stackA.stackTagCompound != null || stackB.stackTagCompound == null) && (stackA.stackTagCompound == null || stackA.stackTagCompound.equals(stackB.stackTagCompound)));
    }
    
    public static boolean areItemStacksEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && stackA.isItemStackEqual(stackB));
    }
    
    private boolean isItemStackEqual(final ItemStack other) {
        return this.stackSize == other.stackSize && this.item == other.item && this.itemDamage == other.itemDamage && (this.stackTagCompound != null || other.stackTagCompound == null) && (this.stackTagCompound == null || this.stackTagCompound.equals(other.stackTagCompound));
    }
    
    public static boolean areItemsEqual(final ItemStack stackA, final ItemStack stackB) {
        return (stackA == null && stackB == null) || (stackA != null && stackB != null && stackA.isItemEqual(stackB));
    }
    
    public boolean isItemEqual(final ItemStack other) {
        return other != null && this.item == other.item && this.itemDamage == other.itemDamage;
    }
    
    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName(this);
    }
    
    public static ItemStack copyItemStack(final ItemStack stack) {
        return (stack == null) ? null : stack.copy();
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.stackSize) + "x" + this.item.getUnlocalizedName() + "@" + this.itemDamage;
    }
    
    public void updateAnimation(final World worldIn, final Entity entityIn, final int inventorySlot, final boolean isCurrentItem) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }
        this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem);
    }
    
    public void onCrafting(final World worldIn, final EntityPlayer playerIn, final int amount) {
        playerIn.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.item)], amount);
        this.item.onCreated(this, worldIn, playerIn);
    }
    
    public boolean getIsItemStackEqual(final ItemStack p_179549_1_) {
        return this.isItemStackEqual(p_179549_1_);
    }
    
    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }
    
    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }
    
    public void onPlayerStoppedUsing(final World worldIn, final EntityPlayer playerIn, final int timeLeft) {
        this.getItem().onPlayerStoppedUsing(this, worldIn, playerIn, timeLeft);
    }
    
    public boolean hasTagCompound() {
        return this.stackTagCompound != null;
    }
    
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }
    
    public NBTTagCompound getSubCompound(final String key, final boolean create) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) {
            return this.stackTagCompound.getCompoundTag(key);
        }
        if (create) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            this.setTagInfo(key, nbttagcompound);
            return nbttagcompound;
        }
        return null;
    }
    
    public NBTTagList getEnchantmentTagList() {
        return (this.stackTagCompound == null) ? null : this.stackTagCompound.getTagList("ench", 10);
    }
    
    public void setTagCompound(final NBTTagCompound nbt) {
        this.stackTagCompound = nbt;
    }
    
    public String getDisplayName() {
        String s = this.getItem().getItemStackDisplayName(this);
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            final NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
            if (nbttagcompound.hasKey("Name", 8)) {
                s = nbttagcompound.getString("Name");
            }
        }
        return s;
    }
    
    public ItemStack setStackDisplayName(final String displayName) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }
        if (!this.stackTagCompound.hasKey("display", 10)) {
            this.stackTagCompound.setTag("display", new NBTTagCompound());
        }
        this.stackTagCompound.getCompoundTag("display").setString("Name", displayName);
        return this;
    }
    
    public void clearCustomName() {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10)) {
            final NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
            nbttagcompound.removeTag("Name");
            if (nbttagcompound.hasNoTags()) {
                this.stackTagCompound.removeTag("display");
                if (this.stackTagCompound.hasNoTags()) {
                    this.setTagCompound(null);
                }
            }
        }
    }
    
    public boolean hasDisplayName() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10) && this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8);
    }
    
    public List<String> getTooltip(final EntityPlayer playerIn, final boolean advanced) {
        final List<String> list = (List<String>)Lists.newArrayList();
        String s = this.getDisplayName();
        if (this.hasDisplayName()) {
            s = EnumChatFormatting.ITALIC + s;
        }
        s = String.valueOf(s) + EnumChatFormatting.RESET;
        if (advanced) {
            String s2 = "";
            if (s.length() > 0) {
                s = String.valueOf(s) + " (";
                s2 = ")";
            }
            final int i = Item.getIdFromItem(this.item);
            if (this.getHasSubtypes()) {
                s = String.valueOf(s) + String.format("#%04d/%d%s", i, this.itemDamage, s2);
            }
            else {
                s = String.valueOf(s) + String.format("#%04d%s", i, s2);
            }
        }
        else if (!this.hasDisplayName() && this.item == Items.filled_map) {
            s = String.valueOf(s) + " #" + this.itemDamage;
        }
        list.add(s);
        int i2 = 0;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99)) {
            i2 = this.stackTagCompound.getInteger("HideFlags");
        }
        if ((i2 & 0x20) == 0x0) {
            this.item.addInformation(this, playerIn, list, advanced);
        }
        if (this.hasTagCompound()) {
            if ((i2 & 0x1) == 0x0) {
                final NBTTagList nbttaglist = this.getEnchantmentTagList();
                if (nbttaglist != null) {
                    for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                        final int k = nbttaglist.getCompoundTagAt(j).getShort("id");
                        final int l = nbttaglist.getCompoundTagAt(j).getShort("lvl");
                        if (Enchantment.getEnchantmentById(k) != null) {
                            list.add(Enchantment.getEnchantmentById(k).getTranslatedName(l));
                        }
                    }
                }
            }
            if (this.stackTagCompound.hasKey("display", 10)) {
                final NBTTagCompound nbttagcompound = this.stackTagCompound.getCompoundTag("display");
                if (nbttagcompound.hasKey("color", 3)) {
                    if (advanced) {
                        list.add("Color: #" + Integer.toHexString(nbttagcompound.getInteger("color")).toUpperCase());
                    }
                    else {
                        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
                    }
                }
                if (nbttagcompound.getTagId("Lore") == 9) {
                    final NBTTagList nbttaglist2 = nbttagcompound.getTagList("Lore", 8);
                    if (nbttaglist2.tagCount() > 0) {
                        for (int j2 = 0; j2 < nbttaglist2.tagCount(); ++j2) {
                            list.add(new StringBuilder().append(EnumChatFormatting.DARK_PURPLE).append(EnumChatFormatting.ITALIC).append(nbttaglist2.getStringTagAt(j2)).toString());
                        }
                    }
                }
            }
        }
        final Multimap<String, AttributeModifier> multimap = this.getAttributeModifiers();
        if (!multimap.isEmpty() && (i2 & 0x2) == 0x0) {
            list.add("");
            for (final Map.Entry<String, AttributeModifier> entry : multimap.entries()) {
                final AttributeModifier attributemodifier = entry.getValue();
                double d0 = attributemodifier.getAmount();
                if (attributemodifier.getID() == Item.itemModifierUUID) {
                    d0 += EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }
                double d2;
                if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2) {
                    d2 = d0;
                }
                else {
                    d2 = d0 * 100.0;
                }
                if (d0 > 0.0) {
                    list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + entry.getKey())));
                }
                else {
                    if (d0 >= 0.0) {
                        continue;
                    }
                    d2 *= -1.0;
                    list.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), ItemStack.DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + entry.getKey())));
                }
            }
        }
        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable") && (i2 & 0x4) == 0x0) {
            list.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i2 & 0x8) == 0x0) {
            final NBTTagList nbttaglist3 = this.stackTagCompound.getTagList("CanDestroy", 8);
            if (nbttaglist3.tagCount() > 0) {
                list.add("");
                list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canBreak"));
                for (int k2 = 0; k2 < nbttaglist3.tagCount(); ++k2) {
                    final Block block = Block.getBlockFromName(nbttaglist3.getStringTagAt(k2));
                    if (block != null) {
                        list.add(EnumChatFormatting.DARK_GRAY + block.getLocalizedName());
                    }
                    else {
                        list.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i2 & 0x10) == 0x0) {
            final NBTTagList nbttaglist4 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            if (nbttaglist4.tagCount() > 0) {
                list.add("");
                list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.canPlace"));
                for (int l2 = 0; l2 < nbttaglist4.tagCount(); ++l2) {
                    final Block block2 = Block.getBlockFromName(nbttaglist4.getStringTagAt(l2));
                    if (block2 != null) {
                        list.add(EnumChatFormatting.DARK_GRAY + block2.getLocalizedName());
                    }
                    else {
                        list.add(EnumChatFormatting.DARK_GRAY + "missingno");
                    }
                }
            }
        }
        if (advanced) {
            if (this.isItemDamaged()) {
                list.add("Durability: " + (this.getMaxDamage() - this.getItemDamage()) + " / " + this.getMaxDamage());
            }
            list.add(EnumChatFormatting.DARK_GRAY + Item.itemRegistry.getNameForObject(this.item).toString());
            if (this.hasTagCompound()) {
                list.add(EnumChatFormatting.DARK_GRAY + "NBT: " + this.getTagCompound().getKeySet().size() + " tag(s)");
            }
        }
        return list;
    }
    
    public boolean hasEffect() {
        return this.getItem().hasEffect(this);
    }
    
    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }
    
    public boolean isItemEnchantable() {
        return this.getItem().isItemTool(this) && !this.isItemEnchanted();
    }
    
    public void addEnchantment(final Enchantment ench, final int level) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }
        final NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)ench.effectId);
        nbttagcompound.setShort("lvl", (byte)level);
        nbttaglist.appendTag(nbttagcompound);
    }
    
    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }
    
    public void setTagInfo(final String key, final NBTBase value) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }
        this.stackTagCompound.setTag(key, value);
    }
    
    public boolean canEditBlocks() {
        return this.getItem().canItemEditBlocks();
    }
    
    public boolean isOnItemFrame() {
        return this.itemFrame != null;
    }
    
    public void setItemFrame(final EntityItemFrame frame) {
        this.itemFrame = frame;
    }
    
    public EntityItemFrame getItemFrame() {
        return this.itemFrame;
    }
    
    public int getRepairCost() {
        return (this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3)) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }
    
    public void setRepairCost(final int cost) {
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }
        this.stackTagCompound.setInteger("RepairCost", cost);
    }
    
    public Multimap<String, AttributeModifier> getAttributeModifiers() {
        Multimap<String, AttributeModifier> multimap;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            multimap = (Multimap<String, AttributeModifier>)HashMultimap.create();
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
                if (attributemodifier != null && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L) {
                    multimap.put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        }
        else {
            multimap = this.getItem().getItemAttributeModifiers();
        }
        return multimap;
    }
    
    public void setItem(final Item newItem) {
        this.item = newItem;
    }
    
    public IChatComponent getChatComponent() {
        final ChatComponentText chatcomponenttext = new ChatComponentText(this.getDisplayName());
        if (this.hasDisplayName()) {
            chatcomponenttext.getChatStyle().setItalic(true);
        }
        final IChatComponent ichatcomponent = new ChatComponentText("[").appendSibling(chatcomponenttext).appendText("]");
        if (this.item != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            this.writeToNBT(nbttagcompound);
            ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nbttagcompound.toString())));
            ichatcomponent.getChatStyle().setColor(this.getRarity().rarityColor);
        }
        return ichatcomponent;
    }
    
    public boolean canDestroy(final Block blockIn) {
        if (blockIn == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        }
        this.canDestroyCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block == blockIn) {
                    return this.canDestroyCacheResult = true;
                }
            }
        }
        return this.canDestroyCacheResult = false;
    }
    
    public boolean canPlaceOn(final Block blockIn) {
        if (blockIn == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        }
        this.canPlaceOnCacheBlock = blockIn;
        if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
            final NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
                if (block == blockIn) {
                    return this.canPlaceOnCacheResult = true;
                }
            }
        }
        return this.canPlaceOnCacheResult = false;
    }
}
