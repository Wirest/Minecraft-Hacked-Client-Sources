package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;

public class ItemSaddle extends Item {
    private static final String __OBFID = "CL_00000059";

    public ItemSaddle() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        if (target instanceof EntityPig) {
            EntityPig var4 = (EntityPig) target;

            if (!var4.getSaddled() && !var4.isChild()) {
                var4.setSaddled(true);
                var4.worldObj.playSoundAtEntity(var4, "mob.horse.leather", 0.5F, 1.0F);
                --stack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     *
     * @param target   The Entity being hit
     * @param attacker the attacking entity
     */
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        this.itemInteractionForEntity(stack, (EntityPlayer) null, target);
        return true;
    }
}
