/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class AnimationMetadataSection implements IMetadataSection
/*    */ {
/*    */   private final List<AnimationFrame> animationFrames;
/*    */   private final int frameWidth;
/*    */   private final int frameHeight;
/*    */   private final int frameTime;
/*    */   private final boolean interpolate;
/*    */   
/*    */   public AnimationMetadataSection(List<AnimationFrame> p_i46088_1_, int p_i46088_2_, int p_i46088_3_, int p_i46088_4_, boolean p_i46088_5_)
/*    */   {
/* 17 */     this.animationFrames = p_i46088_1_;
/* 18 */     this.frameWidth = p_i46088_2_;
/* 19 */     this.frameHeight = p_i46088_3_;
/* 20 */     this.frameTime = p_i46088_4_;
/* 21 */     this.interpolate = p_i46088_5_;
/*    */   }
/*    */   
/*    */   public int getFrameHeight()
/*    */   {
/* 26 */     return this.frameHeight;
/*    */   }
/*    */   
/*    */   public int getFrameWidth()
/*    */   {
/* 31 */     return this.frameWidth;
/*    */   }
/*    */   
/*    */   public int getFrameCount()
/*    */   {
/* 36 */     return this.animationFrames.size();
/*    */   }
/*    */   
/*    */   public int getFrameTime()
/*    */   {
/* 41 */     return this.frameTime;
/*    */   }
/*    */   
/*    */   public boolean isInterpolate()
/*    */   {
/* 46 */     return this.interpolate;
/*    */   }
/*    */   
/*    */   private AnimationFrame getAnimationFrame(int p_130072_1_)
/*    */   {
/* 51 */     return (AnimationFrame)this.animationFrames.get(p_130072_1_);
/*    */   }
/*    */   
/*    */   public int getFrameTimeSingle(int p_110472_1_)
/*    */   {
/* 56 */     AnimationFrame animationframe = getAnimationFrame(p_110472_1_);
/* 57 */     return animationframe.hasNoTime() ? this.frameTime : animationframe.getFrameTime();
/*    */   }
/*    */   
/*    */   public boolean frameHasTime(int p_110470_1_)
/*    */   {
/* 62 */     return !((AnimationFrame)this.animationFrames.get(p_110470_1_)).hasNoTime();
/*    */   }
/*    */   
/*    */   public int getFrameIndex(int p_110468_1_)
/*    */   {
/* 67 */     return ((AnimationFrame)this.animationFrames.get(p_110468_1_)).getFrameIndex();
/*    */   }
/*    */   
/*    */   public Set<Integer> getFrameIndexSet()
/*    */   {
/* 72 */     Set<Integer> set = Sets.newHashSet();
/*    */     
/* 74 */     for (AnimationFrame animationframe : this.animationFrames)
/*    */     {
/* 76 */       set.add(Integer.valueOf(animationframe.getFrameIndex()));
/*    */     }
/*    */     
/* 79 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\AnimationMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */