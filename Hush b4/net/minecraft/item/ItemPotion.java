// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import com.google.common.collect.HashMultimap;
import net.minecraft.util.StatCollector;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;
import com.google.common.collect.Lists;
import net.minecraft.creativetab.CreativeTabs;
import com.google.common.collect.Maps;
import net.minecraft.potion.PotionEffect;
import java.util.List;
import java.util.Map;

public class ItemPotion extends Item
{
    private Map<Integer, List<PotionEffect>> effectCache;
    private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE;
    
    static {
        SUB_ITEMS_CACHE = Maps.newLinkedHashMap();
    }
    
    public ItemPotion() {
        this.effectCache = (Map<Integer, List<PotionEffect>>)Maps.newHashMap();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }
    
    public List<PotionEffect> getEffects(final ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            final List<PotionEffect> list1 = (List<PotionEffect>)Lists.newArrayList();
            final NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                if (potioneffect != null) {
                    list1.add(potioneffect);
                }
            }
            return list1;
        }
        List<PotionEffect> list2 = this.effectCache.get(stack.getMetadata());
        if (list2 == null) {
            list2 = (List<PotionEffect>)PotionHelper.getPotionEffects(stack.getMetadata(), false);
            this.effectCache.put(stack.getMetadata(), list2);
        }
        return list2;
    }
    
    public List<PotionEffect> getEffects(final int meta) {
        List<PotionEffect> list = this.effectCache.get(meta);
        if (list == null) {
            list = (List<PotionEffect>)PotionHelper.getPotionEffects(meta, false);
            this.effectCache.put(meta, list);
        }
        return list;
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        if (!worldIn.isRemote) {
            final List<PotionEffect> list = this.getEffects(stack);
            if (list != null) {
                for (final PotionEffect potioneffect : list) {
                    playerIn.addPotionEffect(new PotionEffect(potioneffect));
                }
            }
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        if (!playerIn.capabilities.isCreativeMode) {
            if (stack.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }
            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }
        return stack;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (isSplash(itemStackIn.getMetadata())) {
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (ItemPotion.itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(new EntityPotion(worldIn, playerIn, itemStackIn));
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return itemStackIn;
        }
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
    
    public static boolean isSplash(final int meta) {
        return (meta & 0x4000) != 0x0;
    }
    
    public int getColorFromDamage(final int meta) {
        return PotionHelper.getLiquidColor(meta, false);
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return (renderPass > 0) ? 16777215 : this.getColorFromDamage(stack.getMetadata());
    }
    
    public boolean isEffectInstant(final int meta) {
        final List<PotionEffect> list = this.getEffects(meta);
        if (list != null && !list.isEmpty()) {
            for (final PotionEffect potioneffect : list) {
                if (Potion.potionTypes[potioneffect.getPotionID()].isInstant()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 0) {
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
        }
        String s = "";
        if (isSplash(stack.getMetadata())) {
            s = String.valueOf(StatCollector.translateToLocal("potion.prefix.grenade").trim()) + " ";
        }
        final List<PotionEffect> list = Items.potionitem.getEffects(stack);
        if (list != null && !list.isEmpty()) {
            String s2 = list.get(0).getEffectName();
            s2 = String.valueOf(s2) + ".postfix";
            return String.valueOf(s) + StatCollector.translateToLocal(s2).trim();
        }
        final String s3 = PotionHelper.getPotionPrefix(stack.getMetadata());
        return String.valueOf(StatCollector.translateToLocal(s3).trim()) + " " + super.getItemStackDisplayName(stack);
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        if (stack.getMetadata() != 0) {
            final List<PotionEffect> list = Items.potionitem.getEffects(stack);
            final Multimap<String, AttributeModifier> multimap = (Multimap<String, AttributeModifier>)HashMultimap.create();
            if (list != null && !list.isEmpty()) {
                for (final PotionEffect potioneffect : list) {
                    String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
                    final Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    final Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
                    if (map != null && map.size() > 0) {
                        for (final Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                            final AttributeModifier attributemodifier = entry.getValue();
                            final AttributeModifier attributemodifier2 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                            multimap.put(entry.getKey().getAttributeUnlocalizedName(), attributemodifier2);
                        }
                    }
                    if (potioneffect.getAmplifier() > 0) {
                        s1 = String.valueOf(s1) + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                    }
                    if (potioneffect.getDuration() > 20) {
                        s1 = String.valueOf(s1) + " (" + Potion.getDurationString(potioneffect) + ")";
                    }
                    if (potion.isBadEffect()) {
                        tooltip.add(EnumChatFormatting.RED + s1);
                    }
                    else {
                        tooltip.add(EnumChatFormatting.GRAY + s1);
                    }
                }
            }
            else {
                final String s2 = StatCollector.translateToLocal("potion.empty").trim();
                tooltip.add(EnumChatFormatting.GRAY + s2);
            }
            if (!multimap.isEmpty()) {
                tooltip.add("");
                tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
                for (final Map.Entry<String, AttributeModifier> entry2 : multimap.entries()) {
                    final AttributeModifier attributemodifier3 = entry2.getValue();
                    final double d0 = attributemodifier3.getAmount();
                    double d2;
                    if (attributemodifier3.getOperation() != 1 && attributemodifier3.getOperation() != 2) {
                        d2 = attributemodifier3.getAmount();
                    }
                    else {
                        d2 = attributemodifier3.getAmount() * 100.0;
                    }
                    if (d0 > 0.0) {
                        tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier3.getOperation(), ItemStack.DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + entry2.getKey())));
                    }
                    else {
                        if (d0 >= 0.0) {
                            continue;
                        }
                        d2 *= -1.0;
                        tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier3.getOperation(), ItemStack.DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + entry2.getKey())));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        final List<PotionEffect> list = this.getEffects(stack);
        return list != null && !list.isEmpty();
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        if (ItemPotion.SUB_ITEMS_CACHE.isEmpty()) {
            for (int i = 0; i <= 15; ++i) {
                for (int j = 0; j <= 1; ++j) {
                    int lvt_6_1_;
                    if (j == 0) {
                        lvt_6_1_ = (i | 0x2000);
                    }
                    else {
                        lvt_6_1_ = (i | 0x4000);
                    }
                    for (int l = 0; l <= 2; ++l) {
                        int i2 = lvt_6_1_;
                        if (l != 0) {
                            if (l == 1) {
                                i2 = (lvt_6_1_ | 0x20);
                            }
                            else if (l == 2) {
                                i2 = (lvt_6_1_ | 0x40);
                            }
                        }
                        final List<PotionEffect> list = (List<PotionEffect>)PotionHelper.getPotionEffects(i2, false);
                        if (list != null && !list.isEmpty()) {
                            ItemPotion.SUB_ITEMS_CACHE.put(list, i2);
                        }
                    }
                }
            }
        }
        for (final int j2 : ItemPotion.SUB_ITEMS_CACHE.values()) {
            subItems.add(new ItemStack(itemIn, 1, j2));
        }
    }
}
