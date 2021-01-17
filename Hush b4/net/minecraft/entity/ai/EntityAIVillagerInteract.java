// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.item.Item;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    private int interactionDelay;
    private EntityVillager villager;
    
    public EntityAIVillagerInteract(final EntityVillager villagerIn) {
        super(villagerIn, EntityVillager.class, 3.0f, 0.02f);
        this.villager = villagerIn;
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
        if (this.villager.canAbondonItems() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).func_175557_cr()) {
            this.interactionDelay = 10;
        }
        else {
            this.interactionDelay = 0;
        }
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        if (this.interactionDelay > 0) {
            --this.interactionDelay;
            if (this.interactionDelay == 0) {
                final InventoryBasic inventorybasic = this.villager.getVillagerInventory();
                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i) {
                    final ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    ItemStack itemstack2 = null;
                    if (itemstack != null) {
                        final Item item = itemstack.getItem();
                        if ((item == Items.bread || item == Items.potato || item == Items.carrot) && itemstack.stackSize > 3) {
                            final int l = itemstack.stackSize / 2;
                            final ItemStack itemStack = itemstack;
                            itemStack.stackSize -= l;
                            itemstack2 = new ItemStack(item, l, itemstack.getMetadata());
                        }
                        else if (item == Items.wheat && itemstack.stackSize > 5) {
                            final int j = itemstack.stackSize / 2 / 3 * 3;
                            final int k = j / 3;
                            final ItemStack itemStack2 = itemstack;
                            itemStack2.stackSize -= j;
                            itemstack2 = new ItemStack(Items.bread, k, 0);
                        }
                        if (itemstack.stackSize <= 0) {
                            inventorybasic.setInventorySlotContents(i, null);
                        }
                    }
                    if (itemstack2 != null) {
                        final double d0 = this.villager.posY - 0.30000001192092896 + this.villager.getEyeHeight();
                        final EntityItem entityitem = new EntityItem(this.villager.worldObj, this.villager.posX, d0, this.villager.posZ, itemstack2);
                        final float f = 0.3f;
                        final float f2 = this.villager.rotationYawHead;
                        final float f3 = this.villager.rotationPitch;
                        entityitem.motionX = -MathHelper.sin(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * f;
                        entityitem.motionZ = MathHelper.cos(f2 / 180.0f * 3.1415927f) * MathHelper.cos(f3 / 180.0f * 3.1415927f) * f;
                        entityitem.motionY = -MathHelper.sin(f3 / 180.0f * 3.1415927f) * f + 0.1f;
                        entityitem.setDefaultPickupDelay();
                        this.villager.worldObj.spawnEntityInWorld(entityitem);
                        break;
                    }
                }
            }
        }
    }
}
