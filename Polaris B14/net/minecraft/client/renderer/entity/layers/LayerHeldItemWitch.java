/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.minecraft.client.model.ModelWitch;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.ItemRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderWitch;
/*    */ import net.minecraft.entity.monster.EntityWitch;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class LayerHeldItemWitch implements LayerRenderer<EntityWitch>
/*    */ {
/*    */   private final RenderWitch witchRenderer;
/*    */   
/*    */   public LayerHeldItemWitch(RenderWitch witchRendererIn)
/*    */   {
/* 21 */     this.witchRenderer = witchRendererIn;
/*    */   }
/*    */   
/*    */   public void doRenderLayer(EntityWitch entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
/*    */   {
/* 26 */     ItemStack itemstack = entitylivingbaseIn.getHeldItem();
/*    */     
/* 28 */     if (itemstack != null)
/*    */     {
/* 30 */       GlStateManager.color(1.0F, 1.0F, 1.0F);
/* 31 */       GlStateManager.pushMatrix();
/*    */       
/* 33 */       if (this.witchRenderer.getMainModel().isChild)
/*    */       {
/* 35 */         GlStateManager.translate(0.0F, 0.625F, 0.0F);
/* 36 */         GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
/* 37 */         float f = 0.5F;
/* 38 */         GlStateManager.scale(f, f, f);
/*    */       }
/*    */       
/* 41 */       ((ModelWitch)this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625F);
/* 42 */       GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
/* 43 */       Item item = itemstack.getItem();
/* 44 */       Minecraft minecraft = Minecraft.getMinecraft();
/*    */       
/* 46 */       if (((item instanceof net.minecraft.item.ItemBlock)) && (minecraft.getBlockRendererDispatcher().isRenderTypeChest(net.minecraft.block.Block.getBlockFromItem(item), itemstack.getMetadata())))
/*    */       {
/* 48 */         GlStateManager.translate(0.0F, 0.0625F, -0.25F);
/* 49 */         GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
/* 50 */         GlStateManager.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
/* 51 */         float f4 = 0.375F;
/* 52 */         GlStateManager.scale(f4, -f4, f4);
/*    */       }
/* 54 */       else if (item == net.minecraft.init.Items.bow)
/*    */       {
/* 56 */         GlStateManager.translate(0.0F, 0.125F, -0.125F);
/* 57 */         GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
/* 58 */         float f1 = 0.625F;
/* 59 */         GlStateManager.scale(f1, -f1, f1);
/* 60 */         GlStateManager.rotate(-100.0F, 1.0F, 0.0F, 0.0F);
/* 61 */         GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
/*    */       }
/* 63 */       else if (item.isFull3D())
/*    */       {
/* 65 */         if (item.shouldRotateAroundWhenRendering())
/*    */         {
/* 67 */           GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 68 */           GlStateManager.translate(0.0F, -0.0625F, 0.0F);
/*    */         }
/*    */         
/* 71 */         this.witchRenderer.transformHeldFull3DItemLayer();
/* 72 */         GlStateManager.translate(0.0625F, -0.125F, 0.0F);
/* 73 */         float f2 = 0.625F;
/* 74 */         GlStateManager.scale(f2, -f2, f2);
/* 75 */         GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
/* 76 */         GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
/*    */       }
/*    */       else
/*    */       {
/* 80 */         GlStateManager.translate(0.1875F, 0.1875F, 0.0F);
/* 81 */         float f3 = 0.875F;
/* 82 */         GlStateManager.scale(f3, f3, f3);
/* 83 */         GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
/* 84 */         GlStateManager.rotate(-60.0F, 1.0F, 0.0F, 0.0F);
/* 85 */         GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
/*    */       }
/*    */       
/* 88 */       GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
/* 89 */       GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
/* 90 */       minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.THIRD_PERSON);
/* 91 */       GlStateManager.popMatrix();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean shouldCombineTextures()
/*    */   {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\layers\LayerHeldItemWitch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */