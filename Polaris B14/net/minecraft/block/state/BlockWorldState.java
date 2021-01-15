/*    */ package net.minecraft.block.state;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockWorldState
/*    */ {
/*    */   private final World world;
/*    */   private final BlockPos pos;
/*    */   private final boolean field_181628_c;
/*    */   private IBlockState state;
/*    */   private TileEntity tileEntity;
/*    */   private boolean tileEntityInitialized;
/*    */   
/*    */   public BlockWorldState(World p_i46451_1_, BlockPos p_i46451_2_, boolean p_i46451_3_)
/*    */   {
/* 19 */     this.world = p_i46451_1_;
/* 20 */     this.pos = p_i46451_2_;
/* 21 */     this.field_181628_c = p_i46451_3_;
/*    */   }
/*    */   
/*    */   public IBlockState getBlockState()
/*    */   {
/* 26 */     if ((this.state == null) && ((this.field_181628_c) || (this.world.isBlockLoaded(this.pos))))
/*    */     {
/* 28 */       this.state = this.world.getBlockState(this.pos);
/*    */     }
/*    */     
/* 31 */     return this.state;
/*    */   }
/*    */   
/*    */   public TileEntity getTileEntity()
/*    */   {
/* 36 */     if ((this.tileEntity == null) && (!this.tileEntityInitialized))
/*    */     {
/* 38 */       this.tileEntity = this.world.getTileEntity(this.pos);
/* 39 */       this.tileEntityInitialized = true;
/*    */     }
/*    */     
/* 42 */     return this.tileEntity;
/*    */   }
/*    */   
/*    */   public BlockPos getPos()
/*    */   {
/* 47 */     return this.pos;
/*    */   }
/*    */   
/*    */   public static Predicate<BlockWorldState> hasState(Predicate<IBlockState> p_177510_0_)
/*    */   {
/* 52 */     new Predicate()
/*    */     {
/*    */       public boolean apply(BlockWorldState p_apply_1_)
/*    */       {
/* 56 */         return (p_apply_1_ != null) && (BlockWorldState.this.apply(p_apply_1_.getBlockState()));
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\BlockWorldState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */