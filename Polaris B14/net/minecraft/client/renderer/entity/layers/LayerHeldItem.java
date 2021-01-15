/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.ItemRenderer;
/*    */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class LayerHeldItem implements LayerRenderer<EntityLivingBase>
/*    */ {
/*    */   private final RendererLivingEntity<?> livingEntityRenderer;
/*    */   
/*    */   public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn)
/*    */   {
/* 22 */     this.livingEntityRenderer = livingEntityRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 27 */     ItemStack itemstack = entitylivingbaseIn.getHeldItem();
/*    */     
/* 29 */     if (itemstack != null)
/*    */     {
/* 31 */       GlStateManager.pushMatrix();
/*    */       
/* 33 */       if (this.livingEntityRenderer.getMainModel().isChild)
/*    */       {
/* 35 */         float f = 0.5F;
/* 36 */         GlStateManager.translate(0.0F, 0.625F, 0.0F);
/* 37 */         GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
/* 38 */         GlStateManager.scale(f, f, f);
/*    */       }
/*    */       
/* 41 */       ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F);
/* 42 */       GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);
/*    */       
/* 44 */       if (((entitylivingbaseIn instanceof EntityPlayer)) && (((EntityPlayer)entitylivingbaseIn).fishEntity != null))
/*    */       {
/* 46 */         itemstack = new ItemStack(Items.fishing_rod, 0);
/*    */       }
/*    */       
/* 49 */       Item item = itemstack.getItem();
/* 50 */       Minecraft minecraft = Minecraft.getMinecraft();
/*    */       
/* 52 */       if (((item instanceof net.minecraft.item.ItemBlock)) && (Block.getBlockFromItem(item).getRenderType() == 2))
/*    */       {
/* 54 */         GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
/* 55 */         GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
/* 56 */         GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 57 */         float f1 = 0.375F;
/* 58 */         GlStateManager.scale(-f1, -f1, f1);
/*    */       }
/*    */       
/* 61 */       if (entitylivingbaseIn.isSneaking())
/*    */       {
/* 63 */         GlStateManager.translate(0.0F, 0.203125F, 0.0F);
/*    */       }
/*    */       
/* 66 */       minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON);
/* 67 */       GlStateManager.popMatrix();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerHeldItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */