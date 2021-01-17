// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.entity.projectile.EntityPotion;

public class RenderPotion extends RenderSnowball<EntityPotion>
{
    public RenderPotion(final RenderManager renderManagerIn, final RenderItem itemRendererIn) {
        super(renderManagerIn, Items.potionitem, itemRendererIn);
    }
    
    @Override
    public ItemStack func_177082_d(final EntityPotion entityIn) {
        return new ItemStack(this.field_177084_a, 1, entityIn.getPotionDamage());
    }
}
