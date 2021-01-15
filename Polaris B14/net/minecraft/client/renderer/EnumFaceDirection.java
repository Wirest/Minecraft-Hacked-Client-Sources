/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public enum EnumFaceDirection
/*    */ {
/*  7 */   DOWN(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null) }), 
/*  8 */   UP(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }), 
/*  9 */   NORTH(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }), 
/* 10 */   SOUTH(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }), 
/* 11 */   WEST(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }), 
/* 12 */   EAST(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) });
/*    */   
/*    */   private static final EnumFaceDirection[] facings;
/*    */   private final VertexInformation[] vertexInfos;
/*    */   
/*    */   public static EnumFaceDirection getFacing(EnumFacing facing)
/*    */   {
/* 19 */     return facings[facing.getIndex()];
/*    */   }
/*    */   
/*    */   private EnumFaceDirection(VertexInformation[] vertexInfosIn)
/*    */   {
/* 24 */     this.vertexInfos = vertexInfosIn;
/*    */   }
/*    */   
/*    */   public VertexInformation func_179025_a(int p_179025_1_)
/*    */   {
/* 29 */     return this.vertexInfos[p_179025_1_];
/*    */   }
/*    */   
/*    */   static
/*    */   {
/* 14 */     facings = new EnumFaceDirection[6];
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */     facings[Constants.DOWN_INDEX] = DOWN;
/* 34 */     facings[Constants.UP_INDEX] = UP;
/* 35 */     facings[Constants.NORTH_INDEX] = NORTH;
/* 36 */     facings[Constants.SOUTH_INDEX] = SOUTH;
/* 37 */     facings[Constants.WEST_INDEX] = WEST;
/* 38 */     facings[Constants.EAST_INDEX] = EAST;
/*    */   }
/*    */   
/*    */   public static final class Constants {
/* 42 */     public static final int SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
/* 43 */     public static final int UP_INDEX = EnumFacing.UP.getIndex();
/* 44 */     public static final int EAST_INDEX = EnumFacing.EAST.getIndex();
/* 45 */     public static final int NORTH_INDEX = EnumFacing.NORTH.getIndex();
/* 46 */     public static final int DOWN_INDEX = EnumFacing.DOWN.getIndex();
/* 47 */     public static final int WEST_INDEX = EnumFacing.WEST.getIndex();
/*    */   }
/*    */   
/*    */   public static class VertexInformation
/*    */   {
/*    */     public final int field_179184_a;
/*    */     public final int field_179182_b;
/*    */     public final int field_179183_c;
/*    */     
/*    */     private VertexInformation(int p_i46270_1_, int p_i46270_2_, int p_i46270_3_) {
/* 57 */       this.field_179184_a = p_i46270_1_;
/* 58 */       this.field_179182_b = p_i46270_2_;
/* 59 */       this.field_179183_c = p_i46270_3_;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\EnumFaceDirection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */