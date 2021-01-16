package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityHusk extends EntityZombie
{
    public EntityHusk(World p_i47286_1_)
    {
        super(p_i47286_1_);
    }

    public static void func_190740_b(DataFixer p_190740_0_)
    {
        EntityLiving.registerFixesMob(p_190740_0_, EntityHusk.class);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() && this.world.canSeeSky(new BlockPos(this));
    }

    protected boolean func_190730_o()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    protected SoundEvent func_190731_di()
    {
        return SoundEvents.ENTITY_HUSK_STEP;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.field_191182_ar;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        boolean flag = super.attackEntityAsMob(entityIn);

        if (flag && this.getHeldItemMainhand().func_190926_b() && entityIn instanceof EntityLivingBase)
        {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.HUNGER, 140 * (int)f));
        }

        return flag;
    }

    protected ItemStack func_190732_dj()
    {
        return ItemStack.field_190927_a;
    }
}
