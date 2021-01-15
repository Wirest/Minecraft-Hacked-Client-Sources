package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.ResourceLocation;

public class RenderBlaze extends RenderLiving {
   private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
   private static final String __OBFID = "CL_00000980";

   public RenderBlaze(RenderManager p_i46191_1_) {
      super(p_i46191_1_, new ModelBlaze(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(EntityBlaze p_110775_1_) {
      return blazeTextures;
   }

   protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
      return this.getEntityTexture((EntityBlaze)p_110775_1_);
   }
}
