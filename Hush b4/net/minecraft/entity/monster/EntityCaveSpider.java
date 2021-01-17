// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider
{
    public EntityCaveSpider(final World worldIn) {
        super(worldIn);
        this.setSize(0.7f, 0.5f);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0);
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (entityIn instanceof EntityLivingBase) {
                int i = 0;
                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                    i = 7;
                }
                else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    i = 15;
                }
                if (i > 0) {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.poison.id, i * 20, 0));
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, final IEntityLivingData livingdata) {
        return livingdata;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.45f;
    }
}
