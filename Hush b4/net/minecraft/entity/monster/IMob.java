// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals
{
    public static final Predicate<Entity> mobSelector = new Predicate<Entity>() {
        @Override
        public boolean apply(final Entity p_apply_1_) {
            return p_apply_1_ instanceof IMob;
        }
    };
    public static final Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate<Entity>() {
        @Override
        public boolean apply(final Entity p_apply_1_) {
            return p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible();
        }
    };
}
