package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPotion extends RenderSnowball
{

    public RenderPotion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, Items.potionitem, itemRendererIn);
    }

    public ItemStack func_177085_a(EntityPotion potionIn)
    {
        return new ItemStack(this.field_177084_a, 1, potionIn.getPotionDamage());
    }

    @Override
	public ItemStack func_177082_d(Entity entityIn)
    {
        return this.func_177085_a((EntityPotion)entityIn);
    }
}
