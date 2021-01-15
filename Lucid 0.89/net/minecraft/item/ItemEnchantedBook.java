package net.minecraft.item;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandomChestContent;

public class ItemEnchantedBook extends Item
{

    @Override
	public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    @Override
	public boolean isItemTool(ItemStack stack)
    {
        return false;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    @Override
	public EnumRarity getRarity(ItemStack stack)
    {
        return this.getEnchantments(stack).tagCount() > 0 ? EnumRarity.UNCOMMON : super.getRarity(stack);
    }

    public NBTTagList getEnchantments(ItemStack stack)
    {
        NBTTagCompound var2 = stack.getTagCompound();
        return var2 != null && var2.hasKey("StoredEnchantments", 9) ? (NBTTagList)var2.getTag("StoredEnchantments") : new NBTTagList();
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    @Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        NBTTagList var5 = this.getEnchantments(stack);

        if (var5 != null)
        {
            for (int var6 = 0; var6 < var5.tagCount(); ++var6)
            {
                short var7 = var5.getCompoundTagAt(var6).getShort("id");
                short var8 = var5.getCompoundTagAt(var6).getShort("lvl");

                if (Enchantment.getEnchantmentById(var7) != null)
                {
                    tooltip.add(Enchantment.getEnchantmentById(var7).getTranslatedName(var8));
                }
            }
        }
    }

    /**
     * Adds an stored enchantment to an enchanted book ItemStack
     */
    public void addEnchantment(ItemStack stack, EnchantmentData enchantment)
    {
        NBTTagList var3 = this.getEnchantments(stack);
        boolean var4 = true;

        for (int var5 = 0; var5 < var3.tagCount(); ++var5)
        {
            NBTTagCompound var6 = var3.getCompoundTagAt(var5);

            if (var6.getShort("id") == enchantment.enchantmentobj.effectId)
            {
                if (var6.getShort("lvl") < enchantment.enchantmentLevel)
                {
                    var6.setShort("lvl", (short)enchantment.enchantmentLevel);
                }

                var4 = false;
                break;
            }
        }

        if (var4)
        {
            NBTTagCompound var7 = new NBTTagCompound();
            var7.setShort("id", (short)enchantment.enchantmentobj.effectId);
            var7.setShort("lvl", (short)enchantment.enchantmentLevel);
            var3.appendTag(var7);
        }

        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setTag("StoredEnchantments", var3);
    }

    /**
     * Returns the ItemStack of an enchanted version of this item.
     */
    public ItemStack getEnchantedItemStack(EnchantmentData data)
    {
        ItemStack var2 = new ItemStack(this);
        this.addEnchantment(var2, data);
        return var2;
    }

    public void getAll(Enchantment enchantment, List list)
    {
        for (int var3 = enchantment.getMinLevel(); var3 <= enchantment.getMaxLevel(); ++var3)
        {
            list.add(this.getEnchantedItemStack(new EnchantmentData(enchantment, var3)));
        }
    }

    public WeightedRandomChestContent getRandom(Random rand)
    {
        return this.getRandom(rand, 1, 1, 1);
    }

    public WeightedRandomChestContent getRandom(Random rand, int minChance, int maxChance, int weight)
    {
        ItemStack var5 = new ItemStack(Items.book, 1, 0);
        EnchantmentHelper.addRandomEnchantment(rand, var5, 30);
        return new WeightedRandomChestContent(var5, minChance, maxChance, weight);
    }
}
