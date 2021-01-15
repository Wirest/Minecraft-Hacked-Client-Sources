package net.minecraft.entity.monster;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals
{
    /** Entity selector for IMob types. */
    Predicate mobSelector = new Predicate()
    {
        public boolean isApplicable(Entity entityIn)
        {
            return entityIn instanceof IMob;
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.isApplicable((Entity)p_apply_1_);
        }
    };

    /** Entity selector for IMob types that are not invisible */
    Predicate VISIBLE_MOB_SELECTOR = new Predicate()
    {
        public boolean isApplicable(Entity entityIn)
        {
            return entityIn instanceof IMob && !entityIn.isInvisible();
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.isApplicable((Entity)p_apply_1_);
        }
    };
}
