package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {
   Predicate mobSelector = new Predicate() {
      public boolean apply(Entity p_apply_1_) {
         return p_apply_1_ instanceof IMob;
      }
   };
   Predicate VISIBLE_MOB_SELECTOR = new Predicate() {
      public boolean apply(Entity p_apply_1_) {
         return p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible();
      }
   };
}
