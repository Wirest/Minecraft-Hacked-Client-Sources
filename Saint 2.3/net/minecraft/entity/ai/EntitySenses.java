package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {
   EntityLiving entityObj;
   List seenEntities = Lists.newArrayList();
   List unseenEntities = Lists.newArrayList();
   private static final String __OBFID = "CL_00001628";

   public EntitySenses(EntityLiving p_i1672_1_) {
      this.entityObj = p_i1672_1_;
   }

   public void clearSensingCache() {
      this.seenEntities.clear();
      this.unseenEntities.clear();
   }

   public boolean canSee(Entity p_75522_1_) {
      if (this.seenEntities.contains(p_75522_1_)) {
         return true;
      } else if (this.unseenEntities.contains(p_75522_1_)) {
         return false;
      } else {
         this.entityObj.worldObj.theProfiler.startSection("canSee");
         boolean var2 = this.entityObj.canEntityBeSeen(p_75522_1_);
         this.entityObj.worldObj.theProfiler.endSection();
         if (var2) {
            this.seenEntities.add(p_75522_1_);
         } else {
            this.unseenEntities.add(p_75522_1_);
         }

         return var2;
      }
   }
}
