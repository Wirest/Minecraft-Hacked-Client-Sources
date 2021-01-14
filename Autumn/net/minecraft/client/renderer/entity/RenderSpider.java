package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderSpider extends RenderLiving {
   private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");

   public RenderSpider(RenderManager renderManagerIn) {
      super(renderManagerIn, new ModelSpider(), 1.0F);
      this.addLayer(new LayerSpiderEyes(this));
   }

   protected float getDeathMaxRotation(EntitySpider entityLivingBaseIn) {
      return 180.0F;
   }

   protected ResourceLocation getEntityTexture(EntitySpider entity) {
      return spiderTextures;
   }
}
