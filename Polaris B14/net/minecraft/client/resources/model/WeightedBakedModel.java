/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandom.Item;
/*     */ 
/*     */ public class WeightedBakedModel implements IBakedModel
/*     */ {
/*     */   private final int totalWeight;
/*     */   private final List<MyWeighedRandomItem> models;
/*     */   private final IBakedModel baseModel;
/*     */   
/*     */   public WeightedBakedModel(List<MyWeighedRandomItem> p_i46073_1_)
/*     */   {
/*  21 */     this.models = p_i46073_1_;
/*  22 */     this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
/*  23 */     this.baseModel = ((MyWeighedRandomItem)p_i46073_1_.get(0)).model;
/*     */   }
/*     */   
/*     */   public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_)
/*     */   {
/*  28 */     return this.baseModel.getFaceQuads(p_177551_1_);
/*     */   }
/*     */   
/*     */   public List<BakedQuad> getGeneralQuads()
/*     */   {
/*  33 */     return this.baseModel.getGeneralQuads();
/*     */   }
/*     */   
/*     */   public boolean isAmbientOcclusion()
/*     */   {
/*  38 */     return this.baseModel.isAmbientOcclusion();
/*     */   }
/*     */   
/*     */   public boolean isGui3d()
/*     */   {
/*  43 */     return this.baseModel.isGui3d();
/*     */   }
/*     */   
/*     */   public boolean isBuiltInRenderer()
/*     */   {
/*  48 */     return this.baseModel.isBuiltInRenderer();
/*     */   }
/*     */   
/*     */   public net.minecraft.client.renderer.texture.TextureAtlasSprite getParticleTexture()
/*     */   {
/*  53 */     return this.baseModel.getParticleTexture();
/*     */   }
/*     */   
/*     */   public ItemCameraTransforms getItemCameraTransforms()
/*     */   {
/*  58 */     return this.baseModel.getItemCameraTransforms();
/*     */   }
/*     */   
/*     */   public IBakedModel getAlternativeModel(long p_177564_1_)
/*     */   {
/*  63 */     return ((MyWeighedRandomItem)WeightedRandom.getRandomItem(this.models, Math.abs((int)p_177564_1_ >> 16) % this.totalWeight)).model;
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*  68 */     private List<WeightedBakedModel.MyWeighedRandomItem> listItems = Lists.newArrayList();
/*     */     
/*     */     public Builder add(IBakedModel p_177677_1_, int p_177677_2_)
/*     */     {
/*  72 */       this.listItems.add(new WeightedBakedModel.MyWeighedRandomItem(p_177677_1_, p_177677_2_));
/*  73 */       return this;
/*     */     }
/*     */     
/*     */     public WeightedBakedModel build()
/*     */     {
/*  78 */       Collections.sort(this.listItems);
/*  79 */       return new WeightedBakedModel(this.listItems);
/*     */     }
/*     */     
/*     */     public IBakedModel first()
/*     */     {
/*  84 */       return ((WeightedBakedModel.MyWeighedRandomItem)this.listItems.get(0)).model;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable<MyWeighedRandomItem>
/*     */   {
/*     */     protected final IBakedModel model;
/*     */     
/*     */     public MyWeighedRandomItem(IBakedModel p_i46072_1_, int p_i46072_2_)
/*     */     {
/*  94 */       super();
/*  95 */       this.model = p_i46072_1_;
/*     */     }
/*     */     
/*     */     public int compareTo(MyWeighedRandomItem p_compareTo_1_)
/*     */     {
/* 100 */       return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).compare(getCountQuads(), p_compareTo_1_.getCountQuads()).result();
/*     */     }
/*     */     
/*     */     protected int getCountQuads()
/*     */     {
/* 105 */       int i = this.model.getGeneralQuads().size();
/*     */       EnumFacing[] arrayOfEnumFacing;
/* 107 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/* 109 */         i += this.model.getFaceQuads(enumfacing).size();
/*     */       }
/*     */       
/* 112 */       return i;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 117 */       return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\model\WeightedBakedModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */