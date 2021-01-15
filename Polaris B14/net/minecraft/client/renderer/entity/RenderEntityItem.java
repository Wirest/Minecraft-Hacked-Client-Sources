/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*     */ import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.resources.model.IBakedModel;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ 
/*     */ public class RenderEntityItem extends Render<EntityItem>
/*     */ {
/*     */   private final RenderItem itemRenderer;
/*  17 */   private Random field_177079_e = new Random();
/*     */   
/*     */   public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_)
/*     */   {
/*  21 */     super(renderManagerIn);
/*  22 */     this.itemRenderer = p_i46167_2_;
/*  23 */     this.shadowSize = 0.15F;
/*  24 */     this.shadowOpaque = 0.75F;
/*     */   }
/*     */   
/*     */   private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
/*     */   {
/*  29 */     ItemStack itemstack = itemIn.getEntityItem();
/*  30 */     net.minecraft.item.Item item = itemstack.getItem();
/*     */     
/*  32 */     if (item == null)
/*     */     {
/*  34 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  38 */     boolean flag = p_177077_9_.isGui3d();
/*  39 */     int i = func_177078_a(itemstack);
/*  40 */     float f = 0.25F;
/*  41 */     float f1 = net.minecraft.util.MathHelper.sin((itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
/*  42 */     float f2 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
/*  43 */     GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f1 + 0.25F * f2, (float)p_177077_6_);
/*     */     
/*  45 */     if ((flag) || (this.renderManager.options != null))
/*     */     {
/*  47 */       float f3 = ((itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * 57.295776F;
/*  48 */       GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
/*     */     }
/*     */     
/*  51 */     if (!flag)
/*     */     {
/*  53 */       float f6 = -0.0F * (i - 1) * 0.5F;
/*  54 */       float f4 = -0.0F * (i - 1) * 0.5F;
/*  55 */       float f5 = -0.046875F * (i - 1) * 0.5F;
/*  56 */       GlStateManager.translate(f6, f4, f5);
/*     */     }
/*     */     
/*  59 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  60 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   private int func_177078_a(ItemStack stack)
/*     */   {
/*  66 */     int i = 1;
/*     */     
/*  68 */     if (stack.stackSize > 48)
/*     */     {
/*  70 */       i = 5;
/*     */     }
/*  72 */     else if (stack.stackSize > 32)
/*     */     {
/*  74 */       i = 4;
/*     */     }
/*  76 */     else if (stack.stackSize > 16)
/*     */     {
/*  78 */       i = 3;
/*     */     }
/*  80 */     else if (stack.stackSize > 1)
/*     */     {
/*  82 */       i = 2;
/*     */     }
/*     */     
/*  85 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  96 */     ItemStack itemstack = entity.getEntityItem();
/*  97 */     this.field_177079_e.setSeed(187L);
/*  98 */     boolean flag = false;
/*     */     
/* 100 */     if (bindEntityTexture(entity))
/*     */     {
/* 102 */       this.renderManager.renderEngine.getTexture(getEntityTexture(entity)).setBlurMipmap(false, false);
/* 103 */       flag = true;
/*     */     }
/*     */     
/* 106 */     GlStateManager.enableRescaleNormal();
/* 107 */     GlStateManager.alphaFunc(516, 0.1F);
/* 108 */     GlStateManager.enableBlend();
/* 109 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 110 */     GlStateManager.pushMatrix();
/* 111 */     IBakedModel ibakedmodel = this.itemRenderer.getItemModelMesher().getItemModel(itemstack);
/* 112 */     int i = func_177077_a(entity, x, y, z, partialTicks, ibakedmodel);
/*     */     
/* 114 */     for (int j = 0; j < i; j++)
/*     */     {
/* 116 */       if (ibakedmodel.isGui3d())
/*     */       {
/* 118 */         GlStateManager.pushMatrix();
/*     */         
/* 120 */         if (j > 0)
/*     */         {
/* 122 */           float f = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 123 */           float f1 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 124 */           float f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
/* 125 */           GlStateManager.translate(f, f1, f2);
/*     */         }
/*     */         
/* 128 */         GlStateManager.scale(0.5F, 0.5F, 0.5F);
/* 129 */         ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
/* 130 */         this.itemRenderer.renderItem(itemstack, ibakedmodel);
/* 131 */         GlStateManager.popMatrix();
/*     */       }
/*     */       else
/*     */       {
/* 135 */         GlStateManager.pushMatrix();
/* 136 */         ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
/* 137 */         this.itemRenderer.renderItem(itemstack, ibakedmodel);
/* 138 */         GlStateManager.popMatrix();
/* 139 */         float f3 = ibakedmodel.getItemCameraTransforms().ground.scale.x;
/* 140 */         float f4 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
/* 141 */         float f5 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
/* 142 */         GlStateManager.translate(0.0F * f3, 0.0F * f4, 0.046875F * f5);
/*     */       }
/*     */     }
/*     */     
/* 146 */     GlStateManager.popMatrix();
/* 147 */     GlStateManager.disableRescaleNormal();
/* 148 */     GlStateManager.disableBlend();
/* 149 */     bindEntityTexture(entity);
/*     */     
/* 151 */     if (flag)
/*     */     {
/* 153 */       this.renderManager.renderEngine.getTexture(getEntityTexture(entity)).restoreLastBlurMipmap();
/*     */     }
/*     */     
/* 156 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected net.minecraft.util.ResourceLocation getEntityTexture(EntityItem entity)
/*     */   {
/* 164 */     return net.minecraft.client.renderer.texture.TextureMap.locationBlocksTexture;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderEntityItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */