package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {
    /**
     * Entity selector for IMob types.
     */
    Predicate mobSelector = new Predicate() {
        private static final String __OBFID = "CL_00001688";

        public boolean func_179983_a(Entity p_179983_1_) {
            return p_179983_1_ instanceof IMob;
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_179983_a((Entity) p_apply_1_);
        }
    };
    Predicate field_175450_e = new Predicate() {
        private static final String __OBFID = "CL_00002218";

        public boolean func_179982_a(Entity p_179982_1_) {
            return p_179982_1_ instanceof IMob && !p_179982_1_.isInvisible();
        }

        public boolean apply(Object p_apply_1_) {
            return this.func_179982_a((Entity) p_apply_1_);
        }
    };
}
