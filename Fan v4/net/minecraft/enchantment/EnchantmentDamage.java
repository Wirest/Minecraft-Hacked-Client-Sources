package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDamage extends Enchantment
{
    /** Holds the name to be translated of each protection type. */
    private static final String[] protectionName = new String[] {"all", "undead", "arthropods"};

    /**
     * Holds the base factor of enchantability needed to be able to use the enchant.
     */
    private static final int[] baseEnchantability = new int[] {1, 5, 5};

    /**
     * Holds how much each level increased the enchantability factor to be able to use this enchant.
     */
    private static final int[] levelEnchantability = new int[] {11, 8, 8};

    /**
     * Used on the formula of base enchantability, this is the 'window' factor of values to be able to use thing
     * enchant.
     */
    private static final int[] thresholdEnchantability = new int[] {20, 20, 20};

    /**
     * Defines the type of damage of the enchantment, 0 = all, 1 = undead, 3 = arthropods
     */
    public final int damageType;

    public EnchantmentDamage(int enchID, ResourceLocation enchName, int enchWeight, int classification)
    {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
        this.damageType = classification;
    }

    /**
     * Returns the minimal value of enchantability needed on the enchantment level passed.
     */
    public int getMinEnchantability(int enchantmentLevel)
    {
        return baseEnchantability[this.damageType] + (enchantmentLevel - 1) * levelEnchantability[this.damageType];
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return this.getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.damageType];
    }

    /**
     * Returns the maximum level that the enchantment can have.
     */
    public int getMaxLevel()
    {
        return 5;
    }

    /**
     * Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
     * calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
     *  
     * @param level The level of this specific enchantment.
     * @param creatureType The EnumCreatureAttribute which represents the target entity. This can be used to have an
     * effect only apply to a specific group of creatures such as Undead or Arthropods.
     */
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
    {
        return this.damageType == 0 ? (float)level * 1.25F : (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD ? (float)level * 2.5F : (this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD ? (float)level * 2.5F : 0.0F));
    }

    /**
     * Return the name of key in translation table of this enchantment.
     */
    public String getName()
    {
        return "enchantment.damage." + protectionName[this.damageType];
    }

    /**
     * Determines if the enchantment passed can be applyied together with this enchantment.
     *  
     * @param ench A possible enchantment that may be applied along side this enchantment, depending on the results.
     */
    public boolean canApplyTogether(Enchantment ench)
    {
        return !(ench instanceof EnchantmentDamage);
    }

    /**
     * Determines if this enchantment can be applied to a specific ItemStack.
     *  
     * @param stack The ItemStack that is attempting to become enchanted with with enchantment.
     */
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
    }

    /**
     * Called whenever a mob is damaged with an item that has this enchantment on it.
     *  
     * @param user An instance of the entity which used the enchantment. This is normally an EntityPlayer.
     * @param target An instance of the damaged entity.
     * @param level The level of the enchantment used.
     */
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
    {
        if (target instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)target;

            if (this.damageType == 2 && entitylivingbase.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD)
            {
                int i = 20 + user.getRNG().nextInt(10 * level);
                entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, i, 3));
            }
        }
    }
}
