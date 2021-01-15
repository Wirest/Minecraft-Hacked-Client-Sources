/*     */ package optfine;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.BlockModelRenderer.AmbientOcclusionFace;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BreakingFour;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ 
/*     */ public class RenderEnv
/*     */ {
/*     */   private IBlockAccess blockAccess;
/*     */   private IBlockState blockState;
/*     */   private BlockPos blockPos;
/*     */   private GameSettings gameSettings;
/*  21 */   private int blockId = -1;
/*  22 */   private int metadata = -1;
/*  23 */   private int breakingAnimation = -1;
/*  24 */   private float[] quadBounds = new float[EnumFacing.VALUES.length * 2];
/*  25 */   private BitSet boundsFlags = new BitSet(3);
/*  26 */   private BlockModelRenderer.AmbientOcclusionFace aoFace = new BlockModelRenderer.AmbientOcclusionFace();
/*  27 */   private BlockPosM colorizerBlockPos = null;
/*  28 */   private boolean[] borderFlags = null;
/*  29 */   private static ThreadLocal threadLocalInstance = new ThreadLocal();
/*     */   
/*     */   private RenderEnv(IBlockAccess p_i61_1_, IBlockState p_i61_2_, BlockPos p_i61_3_)
/*     */   {
/*  33 */     this.blockAccess = p_i61_1_;
/*  34 */     this.blockState = p_i61_2_;
/*  35 */     this.blockPos = p_i61_3_;
/*  36 */     this.gameSettings = Config.getGameSettings();
/*     */   }
/*     */   
/*     */   public static RenderEnv getInstance(IBlockAccess p_getInstance_0_, IBlockState p_getInstance_1_, BlockPos p_getInstance_2_)
/*     */   {
/*  41 */     RenderEnv renderenv = (RenderEnv)threadLocalInstance.get();
/*     */     
/*  43 */     if (renderenv == null)
/*     */     {
/*  45 */       renderenv = new RenderEnv(p_getInstance_0_, p_getInstance_1_, p_getInstance_2_);
/*  46 */       threadLocalInstance.set(renderenv);
/*  47 */       return renderenv;
/*     */     }
/*     */     
/*     */ 
/*  51 */     renderenv.reset(p_getInstance_0_, p_getInstance_1_, p_getInstance_2_);
/*  52 */     return renderenv;
/*     */   }
/*     */   
/*     */ 
/*     */   private void reset(IBlockAccess p_reset_1_, IBlockState p_reset_2_, BlockPos p_reset_3_)
/*     */   {
/*  58 */     this.blockAccess = p_reset_1_;
/*  59 */     this.blockState = p_reset_2_;
/*  60 */     this.blockPos = p_reset_3_;
/*  61 */     this.blockId = -1;
/*  62 */     this.metadata = -1;
/*  63 */     this.breakingAnimation = -1;
/*  64 */     this.boundsFlags.clear();
/*     */   }
/*     */   
/*     */   public int getBlockId()
/*     */   {
/*  69 */     if (this.blockId < 0)
/*     */     {
/*  71 */       this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
/*     */     }
/*     */     
/*  74 */     return this.blockId;
/*     */   }
/*     */   
/*     */   public int getMetadata()
/*     */   {
/*  79 */     if (this.metadata < 0)
/*     */     {
/*  81 */       this.metadata = this.blockState.getBlock().getMetaFromState(this.blockState);
/*     */     }
/*     */     
/*  84 */     return this.metadata;
/*     */   }
/*     */   
/*     */   public float[] getQuadBounds()
/*     */   {
/*  89 */     return this.quadBounds;
/*     */   }
/*     */   
/*     */   public BitSet getBoundsFlags()
/*     */   {
/*  94 */     return this.boundsFlags;
/*     */   }
/*     */   
/*     */   public BlockModelRenderer.AmbientOcclusionFace getAoFace()
/*     */   {
/*  99 */     return this.aoFace;
/*     */   }
/*     */   
/*     */   public boolean isBreakingAnimation(List p_isBreakingAnimation_1_)
/*     */   {
/* 104 */     if ((this.breakingAnimation < 0) && (p_isBreakingAnimation_1_.size() > 0))
/*     */     {
/* 106 */       if ((p_isBreakingAnimation_1_.get(0) instanceof BreakingFour))
/*     */       {
/* 108 */         this.breakingAnimation = 1;
/*     */       }
/*     */       else
/*     */       {
/* 112 */         this.breakingAnimation = 0;
/*     */       }
/*     */     }
/*     */     
/* 116 */     return this.breakingAnimation == 1;
/*     */   }
/*     */   
/*     */   public boolean isBreakingAnimation(BakedQuad p_isBreakingAnimation_1_)
/*     */   {
/* 121 */     if (this.breakingAnimation < 0)
/*     */     {
/* 123 */       if ((p_isBreakingAnimation_1_ instanceof BreakingFour))
/*     */       {
/* 125 */         this.breakingAnimation = 1;
/*     */       }
/*     */       else
/*     */       {
/* 129 */         this.breakingAnimation = 0;
/*     */       }
/*     */     }
/*     */     
/* 133 */     return this.breakingAnimation == 1;
/*     */   }
/*     */   
/*     */   public boolean isBreakingAnimation()
/*     */   {
/* 138 */     return this.breakingAnimation == 1;
/*     */   }
/*     */   
/*     */   public IBlockState getBlockState()
/*     */   {
/* 143 */     return this.blockState;
/*     */   }
/*     */   
/*     */   public BlockPosM getColorizerBlockPos()
/*     */   {
/* 148 */     if (this.colorizerBlockPos == null)
/*     */     {
/* 150 */       this.colorizerBlockPos = new BlockPosM(0, 0, 0);
/*     */     }
/*     */     
/* 153 */     return this.colorizerBlockPos;
/*     */   }
/*     */   
/*     */   public boolean[] getBorderFlags()
/*     */   {
/* 158 */     if (this.borderFlags == null)
/*     */     {
/* 160 */       this.borderFlags = new boolean[4];
/*     */     }
/*     */     
/* 163 */     return this.borderFlags;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RenderEnv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */