package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob {
    private static final String __OBFID = "CL_00001690";

    public EntityGiantZombie(World worldIn) {
        super(worldIn);
        this.setSize(this.width * 6.0F, this.height * 6.0F);
    }

    public float getEyeHeight() {
        return 10.440001F;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(50.0D);
    }

    public float func_180484_a(BlockPos p_180484_1_) {
        return this.worldObj.getLightBrightness(p_180484_1_) - 0.5F;
    }
}
