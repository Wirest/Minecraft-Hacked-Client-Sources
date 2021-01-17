// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityMooshroom extends EntityCow
{
    public EntityMooshroom(final World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.3f);
        this.spawnableBlock = Blocks.mycelium;
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        final ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null && itemstack.getItem() == Items.bowl && this.getGrowingAge() >= 0) {
            if (itemstack.stackSize == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.mushroom_stew));
                return true;
            }
            if (player.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !player.capabilities.isCreativeMode) {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                return true;
            }
        }
        if (itemstack != null && itemstack.getItem() == Items.shears && this.getGrowingAge() >= 0) {
            this.setDead();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + this.height / 2.0f, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            if (!this.worldObj.isRemote) {
                final EntityCow entitycow = new EntityCow(this.worldObj);
                entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    entitycow.setCustomNameTag(this.getCustomNameTag());
                }
                this.worldObj.spawnEntityInWorld(entitycow);
                for (int i = 0; i < 5; ++i) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + this.height, this.posZ, new ItemStack(Blocks.red_mushroom)));
                }
                itemstack.damageItem(1, player);
                this.playSound("mob.sheep.shear", 1.0f, 1.0f);
            }
            return true;
        }
        return super.interact(player);
    }
    
    @Override
    public EntityMooshroom createChild(final EntityAgeable ageable) {
        return new EntityMooshroom(this.worldObj);
    }
}
