/*      */ package net.minecraft.world.gen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockPrismarine;
/*      */ import net.minecraft.block.BlockStaticLiquid;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.entity.monster.EntityGuardian;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class StructureOceanMonumentPieces
/*      */ {
/*      */   public static void registerOceanMonumentPieces()
/*      */   {
/*   23 */     MapGenStructureIO.registerStructureComponent(MonumentBuilding.class, "OMB");
/*   24 */     MapGenStructureIO.registerStructureComponent(MonumentCoreRoom.class, "OMCR");
/*   25 */     MapGenStructureIO.registerStructureComponent(DoubleXRoom.class, "OMDXR");
/*   26 */     MapGenStructureIO.registerStructureComponent(DoubleXYRoom.class, "OMDXYR");
/*   27 */     MapGenStructureIO.registerStructureComponent(DoubleYRoom.class, "OMDYR");
/*   28 */     MapGenStructureIO.registerStructureComponent(DoubleYZRoom.class, "OMDYZR");
/*   29 */     MapGenStructureIO.registerStructureComponent(DoubleZRoom.class, "OMDZR");
/*   30 */     MapGenStructureIO.registerStructureComponent(EntryRoom.class, "OMEntry");
/*   31 */     MapGenStructureIO.registerStructureComponent(Penthouse.class, "OMPenthouse");
/*   32 */     MapGenStructureIO.registerStructureComponent(SimpleRoom.class, "OMSimple");
/*   33 */     MapGenStructureIO.registerStructureComponent(SimpleTopRoom.class, "OMSimpleT");
/*      */   }
/*      */   
/*      */ 
/*      */   public static class DoubleXRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public DoubleXRoom() {}
/*      */     
/*      */     public DoubleXRoom(EnumFacing p_i45597_1_, StructureOceanMonumentPieces.RoomDefinition p_i45597_2_, Random p_i45597_3_)
/*      */     {
/*   44 */       super(p_i45597_1_, p_i45597_2_, 2, 1, 1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*   49 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
/*   50 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
/*      */       
/*   52 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/*   54 */         func_175821_a(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*   55 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/*   58 */       if (structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*   60 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 6, field_175828_a);
/*      */       }
/*      */       
/*   63 */       if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*   65 */         func_175819_a(worldIn, structureBoundingBoxIn, 8, 4, 1, 14, 4, 6, field_175828_a);
/*      */       }
/*      */       
/*   68 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
/*   69 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 3, 0, 15, 3, 7, field_175826_b, field_175826_b, false);
/*   70 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 15, 3, 0, field_175826_b, field_175826_b, false);
/*   71 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 14, 3, 7, field_175826_b, field_175826_b, false);
/*   72 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, field_175828_a, field_175828_a, false);
/*   73 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 2, 0, 15, 2, 7, field_175828_a, field_175828_a, false);
/*   74 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 15, 2, 0, field_175828_a, field_175828_a, false);
/*   75 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 14, 2, 7, field_175828_a, field_175828_a, false);
/*   76 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
/*   77 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 0, 15, 1, 7, field_175826_b, field_175826_b, false);
/*   78 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 15, 1, 0, field_175826_b, field_175826_b, false);
/*   79 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
/*   80 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 10, 1, 4, field_175826_b, field_175826_b, false);
/*   81 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 9, 2, 3, field_175828_a, field_175828_a, false);
/*   82 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 10, 3, 4, field_175826_b, field_175826_b, false);
/*   83 */       setBlockState(worldIn, field_175825_e, 6, 2, 3, structureBoundingBoxIn);
/*   84 */       setBlockState(worldIn, field_175825_e, 9, 2, 3, structureBoundingBoxIn);
/*      */       
/*   86 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*   88 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */       }
/*      */       
/*   91 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*   93 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
/*      */       }
/*      */       
/*   96 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*   98 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
/*      */       }
/*      */       
/*  101 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  103 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 0, 12, 2, 0, false);
/*      */       }
/*      */       
/*  106 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  108 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 7, 12, 2, 7, false);
/*      */       }
/*      */       
/*  111 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  113 */         func_181655_a(worldIn, structureBoundingBoxIn, 15, 1, 3, 15, 2, 4, false);
/*      */       }
/*      */       
/*  116 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class DoubleXYRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public DoubleXYRoom() {}
/*      */     
/*      */     public DoubleXYRoom(EnumFacing p_i45596_1_, StructureOceanMonumentPieces.RoomDefinition p_i45596_2_, Random p_i45596_3_)
/*      */     {
/*  128 */       super(p_i45596_1_, p_i45596_2_, 2, 2, 1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  133 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
/*  134 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
/*  135 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition2 = structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()];
/*  136 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition3 = structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()];
/*      */       
/*  138 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/*  140 */         func_175821_a(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*  141 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/*  144 */       if (structureoceanmonumentpieces$roomdefinition2.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  146 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 1, 7, 8, 6, field_175828_a);
/*      */       }
/*      */       
/*  149 */       if (structureoceanmonumentpieces$roomdefinition3.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  151 */         func_175819_a(worldIn, structureBoundingBoxIn, 8, 8, 1, 14, 8, 6, field_175828_a);
/*      */       }
/*      */       
/*  154 */       for (int i = 1; i <= 7; i++)
/*      */       {
/*  156 */         IBlockState iblockstate = field_175826_b;
/*      */         
/*  158 */         if ((i == 2) || (i == 6))
/*      */         {
/*  160 */           iblockstate = field_175828_a;
/*      */         }
/*      */         
/*  163 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 7, iblockstate, iblockstate, false);
/*  164 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 15, i, 0, 15, i, 7, iblockstate, iblockstate, false);
/*  165 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 15, i, 0, iblockstate, iblockstate, false);
/*  166 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 7, 14, i, 7, iblockstate, iblockstate, false);
/*      */       }
/*      */       
/*  169 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 7, 4, field_175826_b, field_175826_b, false);
/*  170 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 4, 7, 2, field_175826_b, field_175826_b, false);
/*  171 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 7, 5, field_175826_b, field_175826_b, false);
/*  172 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 3, 13, 7, 4, field_175826_b, field_175826_b, false);
/*  173 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 2, 12, 7, 2, field_175826_b, field_175826_b, false);
/*  174 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 5, 12, 7, 5, field_175826_b, field_175826_b, false);
/*  175 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 3, 5, 3, 4, field_175826_b, field_175826_b, false);
/*  176 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 3, 10, 3, 4, field_175826_b, field_175826_b, false);
/*  177 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 2, 10, 7, 5, field_175826_b, field_175826_b, false);
/*  178 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 7, 2, field_175826_b, field_175826_b, false);
/*  179 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 2, 10, 7, 2, field_175826_b, field_175826_b, false);
/*  180 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 5, 7, 5, field_175826_b, field_175826_b, false);
/*  181 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 5, 10, 7, 5, field_175826_b, field_175826_b, false);
/*  182 */       setBlockState(worldIn, field_175826_b, 6, 6, 2, structureBoundingBoxIn);
/*  183 */       setBlockState(worldIn, field_175826_b, 9, 6, 2, structureBoundingBoxIn);
/*  184 */       setBlockState(worldIn, field_175826_b, 6, 6, 5, structureBoundingBoxIn);
/*  185 */       setBlockState(worldIn, field_175826_b, 9, 6, 5, structureBoundingBoxIn);
/*  186 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 3, 6, 4, 4, field_175826_b, field_175826_b, false);
/*  187 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 3, 10, 4, 4, field_175826_b, field_175826_b, false);
/*  188 */       setBlockState(worldIn, field_175825_e, 5, 4, 2, structureBoundingBoxIn);
/*  189 */       setBlockState(worldIn, field_175825_e, 5, 4, 5, structureBoundingBoxIn);
/*  190 */       setBlockState(worldIn, field_175825_e, 10, 4, 2, structureBoundingBoxIn);
/*  191 */       setBlockState(worldIn, field_175825_e, 10, 4, 5, structureBoundingBoxIn);
/*      */       
/*  193 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  195 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */       }
/*      */       
/*  198 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  200 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
/*      */       }
/*      */       
/*  203 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  205 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
/*      */       }
/*      */       
/*  208 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  210 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 0, 12, 2, 0, false);
/*      */       }
/*      */       
/*  213 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  215 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 7, 12, 2, 7, false);
/*      */       }
/*      */       
/*  218 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  220 */         func_181655_a(worldIn, structureBoundingBoxIn, 15, 1, 3, 15, 2, 4, false);
/*      */       }
/*      */       
/*  223 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  225 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 0, 4, 6, 0, false);
/*      */       }
/*      */       
/*  228 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  230 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 7, 4, 6, 7, false);
/*      */       }
/*      */       
/*  233 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  235 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 6, 4, false);
/*      */       }
/*      */       
/*  238 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  240 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 6, 0, false);
/*      */       }
/*      */       
/*  243 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  245 */         func_181655_a(worldIn, structureBoundingBoxIn, 11, 5, 7, 12, 6, 7, false);
/*      */       }
/*      */       
/*  248 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  250 */         func_181655_a(worldIn, structureBoundingBoxIn, 15, 5, 3, 15, 6, 4, false);
/*      */       }
/*      */       
/*  253 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class DoubleYRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public DoubleYRoom() {}
/*      */     
/*      */     public DoubleYRoom(EnumFacing p_i45595_1_, StructureOceanMonumentPieces.RoomDefinition p_i45595_2_, Random p_i45595_3_)
/*      */     {
/*  265 */       super(p_i45595_1_, p_i45595_2_, 1, 2, 1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  270 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/*  272 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/*  275 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()];
/*      */       
/*  277 */       if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  279 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 1, 6, 8, 6, field_175828_a);
/*      */       }
/*      */       
/*  282 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 0, 4, 7, field_175826_b, field_175826_b, false);
/*  283 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 0, 7, 4, 7, field_175826_b, field_175826_b, false);
/*  284 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 6, 4, 0, field_175826_b, field_175826_b, false);
/*  285 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 6, 4, 7, field_175826_b, field_175826_b, false);
/*  286 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 1, 2, 4, 2, field_175826_b, field_175826_b, false);
/*  287 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 1, 4, 2, field_175826_b, field_175826_b, false);
/*  288 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 1, 5, 4, 2, field_175826_b, field_175826_b, false);
/*  289 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 2, field_175826_b, field_175826_b, false);
/*  290 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 5, 2, 4, 6, field_175826_b, field_175826_b, false);
/*  291 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 4, 5, field_175826_b, field_175826_b, false);
/*  292 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 5, 4, 6, field_175826_b, field_175826_b, false);
/*  293 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 5, 6, 4, 5, field_175826_b, field_175826_b, false);
/*  294 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
/*      */       
/*  296 */       for (int i = 1; i <= 5; i += 4)
/*      */       {
/*  298 */         int j = 0;
/*      */         
/*  300 */         if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */         {
/*  302 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, field_175826_b, field_175826_b, false);
/*  303 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, field_175826_b, field_175826_b, false);
/*  304 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/*  308 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, field_175826_b, field_175826_b, false);
/*  309 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, field_175828_a, field_175828_a, false);
/*      */         }
/*      */         
/*  312 */         j = 7;
/*      */         
/*  314 */         if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */         {
/*  316 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, field_175826_b, field_175826_b, false);
/*  317 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, field_175826_b, field_175826_b, false);
/*  318 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/*  322 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, field_175826_b, field_175826_b, false);
/*  323 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, field_175828_a, field_175828_a, false);
/*      */         }
/*      */         
/*  326 */         int k = 0;
/*      */         
/*  328 */         if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */         {
/*  330 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, field_175826_b, field_175826_b, false);
/*  331 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, field_175826_b, field_175826_b, false);
/*  332 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/*  336 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, field_175826_b, field_175826_b, false);
/*  337 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, field_175828_a, field_175828_a, false);
/*      */         }
/*      */         
/*  340 */         k = 7;
/*      */         
/*  342 */         if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */         {
/*  344 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, field_175826_b, field_175826_b, false);
/*  345 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, field_175826_b, field_175826_b, false);
/*  346 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/*  350 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, field_175826_b, field_175826_b, false);
/*  351 */           fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, field_175828_a, field_175828_a, false);
/*      */         }
/*      */         
/*  354 */         structureoceanmonumentpieces$roomdefinition1 = structureoceanmonumentpieces$roomdefinition;
/*      */       }
/*      */       
/*  357 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class DoubleYZRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public DoubleYZRoom() {}
/*      */     
/*      */     public DoubleYZRoom(EnumFacing p_i45594_1_, StructureOceanMonumentPieces.RoomDefinition p_i45594_2_, Random p_i45594_3_)
/*      */     {
/*  369 */       super(p_i45594_1_, p_i45594_2_, 1, 2, 2);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  374 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
/*  375 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
/*  376 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition2 = structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()];
/*  377 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition3 = structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()];
/*      */       
/*  379 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/*  381 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 8, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*  382 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/*  385 */       if (structureoceanmonumentpieces$roomdefinition3.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  387 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 1, 6, 8, 7, field_175828_a);
/*      */       }
/*      */       
/*  390 */       if (structureoceanmonumentpieces$roomdefinition2.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  392 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 8, 6, 8, 14, field_175828_a);
/*      */       }
/*      */       
/*  395 */       for (int i = 1; i <= 7; i++)
/*      */       {
/*  397 */         IBlockState iblockstate = field_175826_b;
/*      */         
/*  399 */         if ((i == 2) || (i == 6))
/*      */         {
/*  401 */           iblockstate = field_175828_a;
/*      */         }
/*      */         
/*  404 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 15, iblockstate, iblockstate, false);
/*  405 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, i, 0, 7, i, 15, iblockstate, iblockstate, false);
/*  406 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 6, i, 0, iblockstate, iblockstate, false);
/*  407 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 6, i, 15, iblockstate, iblockstate, false);
/*      */       }
/*      */       
/*  410 */       for (int j = 1; j <= 7; j++)
/*      */       {
/*  412 */         IBlockState iblockstate1 = field_175827_c;
/*      */         
/*  414 */         if ((j == 2) || (j == 6))
/*      */         {
/*  416 */           iblockstate1 = field_175825_e;
/*      */         }
/*      */         
/*  419 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, j, 7, 4, j, 8, iblockstate1, iblockstate1, false);
/*      */       }
/*      */       
/*  422 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  424 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */       }
/*      */       
/*  427 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  429 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
/*      */       }
/*      */       
/*  432 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  434 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
/*      */       }
/*      */       
/*  437 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  439 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 15, 4, 2, 15, false);
/*      */       }
/*      */       
/*  442 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  444 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 11, 0, 2, 12, false);
/*      */       }
/*      */       
/*  447 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  449 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 11, 7, 2, 12, false);
/*      */       }
/*      */       
/*  452 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  454 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 0, 4, 6, 0, false);
/*      */       }
/*      */       
/*  457 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  459 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 5, 3, 7, 6, 4, false);
/*  460 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 2, 6, 4, 5, field_175826_b, field_175826_b, false);
/*  461 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 2, field_175826_b, field_175826_b, false);
/*  462 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 5, 6, 3, 5, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       
/*  465 */       if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  467 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 6, 4, false);
/*  468 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 2, 4, 5, field_175826_b, field_175826_b, false);
/*  469 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 2, field_175826_b, field_175826_b, false);
/*  470 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 1, 3, 5, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       
/*  473 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  475 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 15, 4, 6, 15, false);
/*      */       }
/*      */       
/*  478 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  480 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 5, 11, 0, 6, 12, false);
/*  481 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 10, 2, 4, 13, field_175826_b, field_175826_b, false);
/*  482 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 10, 1, 3, 10, field_175826_b, field_175826_b, false);
/*  483 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 13, 1, 3, 13, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       
/*  486 */       if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  488 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 5, 11, 7, 6, 12, false);
/*  489 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 10, 6, 4, 13, field_175826_b, field_175826_b, false);
/*  490 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 10, 6, 3, 10, field_175826_b, field_175826_b, false);
/*  491 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 13, 6, 3, 13, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       
/*  494 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class DoubleZRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public DoubleZRoom() {}
/*      */     
/*      */     public DoubleZRoom(EnumFacing p_i45593_1_, StructureOceanMonumentPieces.RoomDefinition p_i45593_2_, Random p_i45593_3_)
/*      */     {
/*  506 */       super(p_i45593_1_, p_i45593_2_, 1, 1, 2);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  511 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
/*  512 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
/*      */       
/*  514 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/*  516 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 8, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*  517 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/*  520 */       if (structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  522 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 7, field_175828_a);
/*      */       }
/*      */       
/*  525 */       if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/*  527 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 8, 6, 4, 14, field_175828_a);
/*      */       }
/*      */       
/*  530 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 15, field_175826_b, field_175826_b, false);
/*  531 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 15, field_175826_b, field_175826_b, false);
/*  532 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
/*  533 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 15, 6, 3, 15, field_175826_b, field_175826_b, false);
/*  534 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 15, field_175828_a, field_175828_a, false);
/*  535 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 15, field_175828_a, field_175828_a, false);
/*  536 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 2, 0, field_175828_a, field_175828_a, false);
/*  537 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 15, 6, 2, 15, field_175828_a, field_175828_a, false);
/*  538 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 15, field_175826_b, field_175826_b, false);
/*  539 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 15, field_175826_b, field_175826_b, false);
/*  540 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 7, 1, 0, field_175826_b, field_175826_b, false);
/*  541 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 15, 6, 1, 15, field_175826_b, field_175826_b, false);
/*  542 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 2, field_175826_b, field_175826_b, false);
/*  543 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 1, 2, field_175826_b, field_175826_b, false);
/*  544 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 1, 3, 2, field_175826_b, field_175826_b, false);
/*  545 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 2, field_175826_b, field_175826_b, false);
/*  546 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 13, 1, 1, 14, field_175826_b, field_175826_b, false);
/*  547 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 13, 6, 1, 14, field_175826_b, field_175826_b, false);
/*  548 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 1, 3, 14, field_175826_b, field_175826_b, false);
/*  549 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 13, 6, 3, 14, field_175826_b, field_175826_b, false);
/*  550 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 2, 3, 6, field_175826_b, field_175826_b, false);
/*  551 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 3, 6, field_175826_b, field_175826_b, false);
/*  552 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 9, 2, 3, 9, field_175826_b, field_175826_b, false);
/*  553 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 3, 9, field_175826_b, field_175826_b, false);
/*  554 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 6, 4, 2, 6, field_175826_b, field_175826_b, false);
/*  555 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 9, 4, 2, 9, field_175826_b, field_175826_b, false);
/*  556 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 2, 2, 8, field_175826_b, field_175826_b, false);
/*  557 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 5, 2, 8, field_175826_b, field_175826_b, false);
/*  558 */       setBlockState(worldIn, field_175825_e, 2, 2, 5, structureBoundingBoxIn);
/*  559 */       setBlockState(worldIn, field_175825_e, 5, 2, 5, structureBoundingBoxIn);
/*  560 */       setBlockState(worldIn, field_175825_e, 2, 2, 10, structureBoundingBoxIn);
/*  561 */       setBlockState(worldIn, field_175825_e, 5, 2, 10, structureBoundingBoxIn);
/*  562 */       setBlockState(worldIn, field_175826_b, 2, 3, 5, structureBoundingBoxIn);
/*  563 */       setBlockState(worldIn, field_175826_b, 5, 3, 5, structureBoundingBoxIn);
/*  564 */       setBlockState(worldIn, field_175826_b, 2, 3, 10, structureBoundingBoxIn);
/*  565 */       setBlockState(worldIn, field_175826_b, 5, 3, 10, structureBoundingBoxIn);
/*      */       
/*  567 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/*  569 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */       }
/*      */       
/*  572 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  574 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
/*      */       }
/*      */       
/*  577 */       if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  579 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
/*      */       }
/*      */       
/*  582 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  584 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 15, 4, 2, 15, false);
/*      */       }
/*      */       
/*  587 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  589 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 11, 0, 2, 12, false);
/*      */       }
/*      */       
/*  592 */       if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  594 */         func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 11, 7, 2, 12, false);
/*      */       }
/*      */       
/*  597 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class EntryRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public EntryRoom() {}
/*      */     
/*      */     public EntryRoom(EnumFacing p_i45592_1_, StructureOceanMonumentPieces.RoomDefinition p_i45592_2_)
/*      */     {
/*  609 */       super(p_i45592_1_, p_i45592_2_, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  614 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 2, 3, 7, field_175826_b, field_175826_b, false);
/*  615 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
/*  616 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 2, 7, field_175826_b, field_175826_b, false);
/*  617 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 7, 2, 7, field_175826_b, field_175826_b, false);
/*  618 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
/*  619 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
/*  620 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
/*  621 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 0, field_175826_b, field_175826_b, false);
/*  622 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
/*      */       
/*  624 */       if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */       {
/*  626 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
/*      */       }
/*      */       
/*  629 */       if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */       {
/*  631 */         func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 1, 2, 4, false);
/*      */       }
/*      */       
/*  634 */       if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */       {
/*  636 */         func_181655_a(worldIn, structureBoundingBoxIn, 6, 1, 3, 7, 2, 4, false);
/*      */       }
/*      */       
/*  639 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class FitSimpleRoomHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/*  651 */       return true;
/*      */     }
/*      */     
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/*  656 */       p_175968_2_.field_175963_d = true;
/*  657 */       return new StructureOceanMonumentPieces.SimpleRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class FitSimpleRoomTopHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/*  669 */       return (p_175969_1_.field_175966_c[EnumFacing.WEST.getIndex()] == 0) && (p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] == 0) && (p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] == 0) && (p_175969_1_.field_175966_c[EnumFacing.SOUTH.getIndex()] == 0) && (p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] == 0);
/*      */     }
/*      */     
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/*  674 */       p_175968_2_.field_175963_d = true;
/*  675 */       return new StructureOceanMonumentPieces.SimpleTopRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MonumentBuilding extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     private StructureOceanMonumentPieces.RoomDefinition field_175845_o;
/*      */     private StructureOceanMonumentPieces.RoomDefinition field_175844_p;
/*  683 */     private List<StructureOceanMonumentPieces.Piece> field_175843_q = Lists.newArrayList();
/*      */     
/*      */ 
/*      */     public MonumentBuilding() {}
/*      */     
/*      */ 
/*      */     public MonumentBuilding(Random p_i45599_1_, int p_i45599_2_, int p_i45599_3_, EnumFacing p_i45599_4_)
/*      */     {
/*  691 */       super();
/*  692 */       this.coordBaseMode = p_i45599_4_;
/*      */       
/*  694 */       switch (this.coordBaseMode)
/*      */       {
/*      */       case NORTH: 
/*      */       case SOUTH: 
/*  698 */         this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
/*  699 */         break;
/*      */       
/*      */       default: 
/*  702 */         this.boundingBox = new StructureBoundingBox(p_i45599_2_, 39, p_i45599_3_, p_i45599_2_ + 58 - 1, 61, p_i45599_3_ + 58 - 1);
/*      */       }
/*      */       
/*  705 */       List<StructureOceanMonumentPieces.RoomDefinition> list = func_175836_a(p_i45599_1_);
/*  706 */       this.field_175845_o.field_175963_d = true;
/*  707 */       this.field_175843_q.add(new StructureOceanMonumentPieces.EntryRoom(this.coordBaseMode, this.field_175845_o));
/*  708 */       this.field_175843_q.add(new StructureOceanMonumentPieces.MonumentCoreRoom(this.coordBaseMode, this.field_175844_p, p_i45599_1_));
/*  709 */       List<StructureOceanMonumentPieces.MonumentRoomFitHelper> list1 = Lists.newArrayList();
/*  710 */       list1.add(new StructureOceanMonumentPieces.XYDoubleRoomFitHelper(null));
/*  711 */       list1.add(new StructureOceanMonumentPieces.YZDoubleRoomFitHelper(null));
/*  712 */       list1.add(new StructureOceanMonumentPieces.ZDoubleRoomFitHelper(null));
/*  713 */       list1.add(new StructureOceanMonumentPieces.XDoubleRoomFitHelper(null));
/*  714 */       list1.add(new StructureOceanMonumentPieces.YDoubleRoomFitHelper(null));
/*  715 */       list1.add(new StructureOceanMonumentPieces.FitSimpleRoomTopHelper(null));
/*  716 */       list1.add(new StructureOceanMonumentPieces.FitSimpleRoomHelper(null));
/*      */       
/*      */ 
/*  719 */       for (StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition : list)
/*      */       {
/*  721 */         if ((!structureoceanmonumentpieces$roomdefinition.field_175963_d) && (!structureoceanmonumentpieces$roomdefinition.func_175961_b()))
/*      */         {
/*  723 */           Iterator iterator = list1.iterator();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  728 */           while (iterator.hasNext())
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  733 */             StructureOceanMonumentPieces.MonumentRoomFitHelper structureoceanmonumentpieces$monumentroomfithelper = (StructureOceanMonumentPieces.MonumentRoomFitHelper)iterator.next();
/*      */             
/*  735 */             if (structureoceanmonumentpieces$monumentroomfithelper.func_175969_a(structureoceanmonumentpieces$roomdefinition))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  741 */               this.field_175843_q.add(structureoceanmonumentpieces$monumentroomfithelper.func_175968_a(this.coordBaseMode, structureoceanmonumentpieces$roomdefinition, p_i45599_1_)); }
/*      */           }
/*      */         }
/*      */       }
/*  745 */       int j = this.boundingBox.minY;
/*  746 */       int k = getXWithOffset(9, 22);
/*  747 */       int l = getZWithOffset(9, 22);
/*      */       
/*  749 */       for (StructureOceanMonumentPieces.Piece structureoceanmonumentpieces$piece : this.field_175843_q)
/*      */       {
/*  751 */         structureoceanmonumentpieces$piece.getBoundingBox().offset(k, j, l);
/*      */       }
/*      */       
/*  754 */       StructureBoundingBox structureboundingbox1 = StructureBoundingBox.func_175899_a(getXWithOffset(1, 1), getYWithOffset(1), getZWithOffset(1, 1), getXWithOffset(23, 21), getYWithOffset(8), getZWithOffset(23, 21));
/*  755 */       StructureBoundingBox structureboundingbox2 = StructureBoundingBox.func_175899_a(getXWithOffset(34, 1), getYWithOffset(1), getZWithOffset(34, 1), getXWithOffset(56, 21), getYWithOffset(8), getZWithOffset(56, 21));
/*  756 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.func_175899_a(getXWithOffset(22, 22), getYWithOffset(13), getZWithOffset(22, 22), getXWithOffset(35, 35), getYWithOffset(17), getZWithOffset(35, 35));
/*  757 */       int i = p_i45599_1_.nextInt();
/*  758 */       this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, structureboundingbox1, i++));
/*  759 */       this.field_175843_q.add(new StructureOceanMonumentPieces.WingRoom(this.coordBaseMode, structureboundingbox2, i++));
/*  760 */       this.field_175843_q.add(new StructureOceanMonumentPieces.Penthouse(this.coordBaseMode, structureboundingbox));
/*      */     }
/*      */     
/*      */     private List<StructureOceanMonumentPieces.RoomDefinition> func_175836_a(Random p_175836_1_)
/*      */     {
/*  765 */       StructureOceanMonumentPieces.RoomDefinition[] astructureoceanmonumentpieces$roomdefinition = new StructureOceanMonumentPieces.RoomDefinition[75];
/*      */       
/*  767 */       for (int i = 0; i < 5; i++)
/*      */       {
/*  769 */         for (int j = 0; j < 4; j++)
/*      */         {
/*  771 */           int k = 0;
/*  772 */           int l = func_175820_a(i, k, j);
/*  773 */           astructureoceanmonumentpieces$roomdefinition[l] = new StructureOceanMonumentPieces.RoomDefinition(l);
/*      */         }
/*      */       }
/*      */       
/*  777 */       for (int i2 = 0; i2 < 5; i2++)
/*      */       {
/*  779 */         for (int l2 = 0; l2 < 4; l2++)
/*      */         {
/*  781 */           int k3 = 1;
/*  782 */           int j4 = func_175820_a(i2, k3, l2);
/*  783 */           astructureoceanmonumentpieces$roomdefinition[j4] = new StructureOceanMonumentPieces.RoomDefinition(j4);
/*      */         }
/*      */       }
/*      */       
/*  787 */       for (int j2 = 1; j2 < 4; j2++)
/*      */       {
/*  789 */         for (int i3 = 0; i3 < 2; i3++)
/*      */         {
/*  791 */           int l3 = 2;
/*  792 */           int k4 = func_175820_a(j2, l3, i3);
/*  793 */           astructureoceanmonumentpieces$roomdefinition[k4] = new StructureOceanMonumentPieces.RoomDefinition(k4);
/*      */         }
/*      */       }
/*      */       
/*  797 */       this.field_175845_o = astructureoceanmonumentpieces$roomdefinition[field_175823_g];
/*      */       Object localObject;
/*  799 */       for (int k2 = 0; k2 < 5; k2++)
/*      */       {
/*  801 */         for (int j3 = 0; j3 < 5; j3++)
/*      */         {
/*  803 */           for (int i4 = 0; i4 < 3; i4++)
/*      */           {
/*  805 */             int l4 = func_175820_a(k2, i4, j3);
/*      */             
/*  807 */             if (astructureoceanmonumentpieces$roomdefinition[l4] != null)
/*      */             {
/*  809 */               j = (localObject = EnumFacing.values()).length; for (i = 0; i < j; i++) { EnumFacing enumfacing = localObject[i];
/*      */                 
/*  811 */                 int i1 = k2 + enumfacing.getFrontOffsetX();
/*  812 */                 int j1 = i4 + enumfacing.getFrontOffsetY();
/*  813 */                 int k1 = j3 + enumfacing.getFrontOffsetZ();
/*      */                 
/*  815 */                 if ((i1 >= 0) && (i1 < 5) && (k1 >= 0) && (k1 < 5) && (j1 >= 0) && (j1 < 3))
/*      */                 {
/*  817 */                   int l1 = func_175820_a(i1, j1, k1);
/*      */                   
/*  819 */                   if (astructureoceanmonumentpieces$roomdefinition[l1] != null)
/*      */                   {
/*  821 */                     if (k1 != j3)
/*      */                     {
/*  823 */                       astructureoceanmonumentpieces$roomdefinition[l4].func_175957_a(enumfacing.getOpposite(), astructureoceanmonumentpieces$roomdefinition[l1]);
/*      */                     }
/*      */                     else
/*      */                     {
/*  827 */                       astructureoceanmonumentpieces$roomdefinition[l4].func_175957_a(enumfacing, astructureoceanmonumentpieces$roomdefinition[l1]);
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition;
/*  838 */       astructureoceanmonumentpieces$roomdefinition[field_175831_h].func_175957_a(EnumFacing.UP, structureoceanmonumentpieces$roomdefinition = new StructureOceanMonumentPieces.RoomDefinition(1003));
/*      */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1;
/*  840 */       astructureoceanmonumentpieces$roomdefinition[field_175832_i].func_175957_a(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition1 = new StructureOceanMonumentPieces.RoomDefinition(1001));
/*      */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition2;
/*  842 */       astructureoceanmonumentpieces$roomdefinition[field_175829_j].func_175957_a(EnumFacing.SOUTH, structureoceanmonumentpieces$roomdefinition2 = new StructureOceanMonumentPieces.RoomDefinition(1002));
/*  843 */       structureoceanmonumentpieces$roomdefinition.field_175963_d = true;
/*  844 */       structureoceanmonumentpieces$roomdefinition1.field_175963_d = true;
/*  845 */       structureoceanmonumentpieces$roomdefinition2.field_175963_d = true;
/*  846 */       this.field_175845_o.field_175964_e = true;
/*  847 */       this.field_175844_p = astructureoceanmonumentpieces$roomdefinition[func_175820_a(p_175836_1_.nextInt(4), 0, 2)];
/*  848 */       this.field_175844_p.field_175963_d = true;
/*  849 */       this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
/*  850 */       this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
/*  851 */       this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
/*  852 */       this.field_175844_p.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/*  853 */       this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/*  854 */       this.field_175844_p.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/*  855 */       this.field_175844_p.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/*  856 */       List<StructureOceanMonumentPieces.RoomDefinition> list = Lists.newArrayList();
/*      */       
/*  858 */       int j = (localObject = astructureoceanmonumentpieces$roomdefinition).length; for (int i = 0; i < j; i++) { StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition4 = localObject[i];
/*      */         
/*  860 */         if (structureoceanmonumentpieces$roomdefinition4 != null)
/*      */         {
/*  862 */           structureoceanmonumentpieces$roomdefinition4.func_175958_a();
/*  863 */           list.add(structureoceanmonumentpieces$roomdefinition4);
/*      */         }
/*      */       }
/*      */       
/*  867 */       structureoceanmonumentpieces$roomdefinition.func_175958_a();
/*  868 */       java.util.Collections.shuffle(list, p_175836_1_);
/*  869 */       int i5 = 1;
/*      */       int j5;
/*  871 */       int k5; for (Iterator localIterator = list.iterator(); localIterator.hasNext(); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  876 */           (j5 < 2) && (k5 < 5))
/*      */       {
/*  871 */         StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition3 = (StructureOceanMonumentPieces.RoomDefinition)localIterator.next();
/*      */         
/*  873 */         j5 = 0;
/*  874 */         k5 = 0;
/*      */         
/*  876 */         continue;
/*      */         
/*  878 */         k5++;
/*  879 */         int l5 = p_175836_1_.nextInt(6);
/*      */         
/*  881 */         if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5] != 0)
/*      */         {
/*  883 */           int i6 = EnumFacing.getFront(l5).getOpposite().getIndex();
/*  884 */           structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5] = false;
/*  885 */           structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].field_175966_c[i6] = false;
/*      */           
/*  887 */           if ((structureoceanmonumentpieces$roomdefinition3.func_175959_a(i5++)) && (structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].func_175959_a(i5++)))
/*      */           {
/*  889 */             j5++;
/*      */           }
/*      */           else
/*      */           {
/*  893 */             structureoceanmonumentpieces$roomdefinition3.field_175966_c[l5] = true;
/*  894 */             structureoceanmonumentpieces$roomdefinition3.field_175965_b[l5].field_175966_c[i6] = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  900 */       list.add(structureoceanmonumentpieces$roomdefinition);
/*  901 */       list.add(structureoceanmonumentpieces$roomdefinition1);
/*  902 */       list.add(structureoceanmonumentpieces$roomdefinition2);
/*  903 */       return list;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  908 */       int i = Math.max(worldIn.func_181545_F(), 64) - this.boundingBox.minY;
/*  909 */       func_181655_a(worldIn, structureBoundingBoxIn, 0, 0, 0, 58, i, 58, false);
/*  910 */       func_175840_a(false, 0, worldIn, randomIn, structureBoundingBoxIn);
/*  911 */       func_175840_a(true, 33, worldIn, randomIn, structureBoundingBoxIn);
/*  912 */       func_175839_b(worldIn, randomIn, structureBoundingBoxIn);
/*  913 */       func_175837_c(worldIn, randomIn, structureBoundingBoxIn);
/*  914 */       func_175841_d(worldIn, randomIn, structureBoundingBoxIn);
/*  915 */       func_175835_e(worldIn, randomIn, structureBoundingBoxIn);
/*  916 */       func_175842_f(worldIn, randomIn, structureBoundingBoxIn);
/*  917 */       func_175838_g(worldIn, randomIn, structureBoundingBoxIn);
/*      */       int k;
/*  919 */       for (int j = 0; j < 7; j++)
/*      */       {
/*  921 */         k = 0;
/*      */         
/*  923 */         while (k < 7)
/*      */         {
/*  925 */           if ((k == 0) && (j == 3))
/*      */           {
/*  927 */             k = 6;
/*      */           }
/*      */           
/*  930 */           int l = j * 9;
/*  931 */           int i1 = k * 9;
/*      */           
/*  933 */           for (int j1 = 0; j1 < 4; j1++)
/*      */           {
/*  935 */             for (int k1 = 0; k1 < 4; k1++)
/*      */             {
/*  937 */               setBlockState(worldIn, field_175826_b, l + j1, 0, i1 + k1, structureBoundingBoxIn);
/*  938 */               replaceAirAndLiquidDownwards(worldIn, field_175826_b, l + j1, -1, i1 + k1, structureBoundingBoxIn);
/*      */             }
/*      */           }
/*      */           
/*  942 */           if ((j != 0) && (j != 6))
/*      */           {
/*  944 */             k += 6;
/*      */           }
/*      */           else
/*      */           {
/*  948 */             k++;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  953 */       for (int l1 = 0; l1 < 5; l1++)
/*      */       {
/*  955 */         func_181655_a(worldIn, structureBoundingBoxIn, -1 - l1, 0 + l1 * 2, -1 - l1, -1 - l1, 23, 58 + l1, false);
/*  956 */         func_181655_a(worldIn, structureBoundingBoxIn, 58 + l1, 0 + l1 * 2, -1 - l1, 58 + l1, 23, 58 + l1, false);
/*  957 */         func_181655_a(worldIn, structureBoundingBoxIn, 0 - l1, 0 + l1 * 2, -1 - l1, 57 + l1, 23, -1 - l1, false);
/*  958 */         func_181655_a(worldIn, structureBoundingBoxIn, 0 - l1, 0 + l1 * 2, 58 + l1, 57 + l1, 23, 58 + l1, false);
/*      */       }
/*      */       
/*  961 */       for (StructureOceanMonumentPieces.Piece structureoceanmonumentpieces$piece : this.field_175843_q)
/*      */       {
/*  963 */         if (structureoceanmonumentpieces$piece.getBoundingBox().intersectsWith(structureBoundingBoxIn))
/*      */         {
/*  965 */           structureoceanmonumentpieces$piece.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  969 */       return true;
/*      */     }
/*      */     
/*      */     private void func_175840_a(boolean p_175840_1_, int p_175840_2_, World worldIn, Random p_175840_4_, StructureBoundingBox p_175840_5_)
/*      */     {
/*  974 */       int i = 24;
/*      */       
/*  976 */       if (func_175818_a(p_175840_5_, p_175840_2_, 0, p_175840_2_ + 23, 20))
/*      */       {
/*  978 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 0, 0, 0, p_175840_2_ + 24, 0, 20, field_175828_a, field_175828_a, false);
/*  979 */         func_181655_a(worldIn, p_175840_5_, p_175840_2_ + 0, 1, 0, p_175840_2_ + 24, 10, 20, false);
/*      */         
/*  981 */         for (int j = 0; j < 4; j++)
/*      */         {
/*  983 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j, j + 1, j, p_175840_2_ + j, j + 1, 20, field_175826_b, field_175826_b, false);
/*  984 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 7, j + 5, j + 7, p_175840_2_ + j + 7, j + 5, 20, field_175826_b, field_175826_b, false);
/*  985 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 17 - j, j + 5, j + 7, p_175840_2_ + 17 - j, j + 5, 20, field_175826_b, field_175826_b, false);
/*  986 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 24 - j, j + 1, j, p_175840_2_ + 24 - j, j + 1, 20, field_175826_b, field_175826_b, false);
/*  987 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 1, j + 1, j, p_175840_2_ + 23 - j, j + 1, j, field_175826_b, field_175826_b, false);
/*  988 */           fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + j + 8, j + 5, j + 7, p_175840_2_ + 16 - j, j + 5, j + 7, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/*  991 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 4, 4, 4, p_175840_2_ + 6, 4, 20, field_175828_a, field_175828_a, false);
/*  992 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 7, 4, 4, p_175840_2_ + 17, 4, 6, field_175828_a, field_175828_a, false);
/*  993 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 18, 4, 4, p_175840_2_ + 20, 4, 20, field_175828_a, field_175828_a, false);
/*  994 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 8, 11, p_175840_2_ + 13, 8, 20, field_175828_a, field_175828_a, false);
/*  995 */         setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 12, p_175840_5_);
/*  996 */         setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 15, p_175840_5_);
/*  997 */         setBlockState(worldIn, field_175824_d, p_175840_2_ + 12, 9, 18, p_175840_5_);
/*  998 */         int j1 = p_175840_1_ ? p_175840_2_ + 19 : p_175840_2_ + 5;
/*  999 */         int k = p_175840_1_ ? p_175840_2_ + 5 : p_175840_2_ + 19;
/*      */         
/* 1001 */         for (int l = 20; l >= 5; l -= 3)
/*      */         {
/* 1003 */           setBlockState(worldIn, field_175824_d, j1, 5, l, p_175840_5_);
/*      */         }
/*      */         
/* 1006 */         for (int k1 = 19; k1 >= 7; k1 -= 3)
/*      */         {
/* 1008 */           setBlockState(worldIn, field_175824_d, k, 5, k1, p_175840_5_);
/*      */         }
/*      */         
/* 1011 */         for (int l1 = 0; l1 < 4; l1++)
/*      */         {
/* 1013 */           int i1 = p_175840_1_ ? p_175840_2_ + (24 - (17 - l1 * 3)) : p_175840_2_ + 17 - l1 * 3;
/* 1014 */           setBlockState(worldIn, field_175824_d, i1, 5, 5, p_175840_5_);
/*      */         }
/*      */         
/* 1017 */         setBlockState(worldIn, field_175824_d, k, 5, 5, p_175840_5_);
/* 1018 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 11, 1, 12, p_175840_2_ + 13, 7, 12, field_175828_a, field_175828_a, false);
/* 1019 */         fillWithBlocks(worldIn, p_175840_5_, p_175840_2_ + 12, 1, 11, p_175840_2_ + 12, 7, 13, field_175828_a, field_175828_a, false);
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175839_b(World worldIn, Random p_175839_2_, StructureBoundingBox p_175839_3_)
/*      */     {
/* 1025 */       if (func_175818_a(p_175839_3_, 22, 5, 35, 17))
/*      */       {
/* 1027 */         func_181655_a(worldIn, p_175839_3_, 25, 0, 0, 32, 8, 20, false);
/*      */         
/* 1029 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 1031 */           fillWithBlocks(worldIn, p_175839_3_, 24, 2, 5 + i * 4, 24, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
/* 1032 */           fillWithBlocks(worldIn, p_175839_3_, 22, 4, 5 + i * 4, 23, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
/* 1033 */           setBlockState(worldIn, field_175826_b, 25, 5, 5 + i * 4, p_175839_3_);
/* 1034 */           setBlockState(worldIn, field_175826_b, 26, 6, 5 + i * 4, p_175839_3_);
/* 1035 */           setBlockState(worldIn, field_175825_e, 26, 5, 5 + i * 4, p_175839_3_);
/* 1036 */           fillWithBlocks(worldIn, p_175839_3_, 33, 2, 5 + i * 4, 33, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
/* 1037 */           fillWithBlocks(worldIn, p_175839_3_, 34, 4, 5 + i * 4, 35, 4, 5 + i * 4, field_175826_b, field_175826_b, false);
/* 1038 */           setBlockState(worldIn, field_175826_b, 32, 5, 5 + i * 4, p_175839_3_);
/* 1039 */           setBlockState(worldIn, field_175826_b, 31, 6, 5 + i * 4, p_175839_3_);
/* 1040 */           setBlockState(worldIn, field_175825_e, 31, 5, 5 + i * 4, p_175839_3_);
/* 1041 */           fillWithBlocks(worldIn, p_175839_3_, 27, 6, 5 + i * 4, 30, 6, 5 + i * 4, field_175828_a, field_175828_a, false);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175837_c(World worldIn, Random p_175837_2_, StructureBoundingBox p_175837_3_)
/*      */     {
/* 1048 */       if (func_175818_a(p_175837_3_, 15, 20, 42, 21))
/*      */       {
/* 1050 */         fillWithBlocks(worldIn, p_175837_3_, 15, 0, 21, 42, 0, 21, field_175828_a, field_175828_a, false);
/* 1051 */         func_181655_a(worldIn, p_175837_3_, 26, 1, 21, 31, 3, 21, false);
/* 1052 */         fillWithBlocks(worldIn, p_175837_3_, 21, 12, 21, 36, 12, 21, field_175828_a, field_175828_a, false);
/* 1053 */         fillWithBlocks(worldIn, p_175837_3_, 17, 11, 21, 40, 11, 21, field_175828_a, field_175828_a, false);
/* 1054 */         fillWithBlocks(worldIn, p_175837_3_, 16, 10, 21, 41, 10, 21, field_175828_a, field_175828_a, false);
/* 1055 */         fillWithBlocks(worldIn, p_175837_3_, 15, 7, 21, 42, 9, 21, field_175828_a, field_175828_a, false);
/* 1056 */         fillWithBlocks(worldIn, p_175837_3_, 16, 6, 21, 41, 6, 21, field_175828_a, field_175828_a, false);
/* 1057 */         fillWithBlocks(worldIn, p_175837_3_, 17, 5, 21, 40, 5, 21, field_175828_a, field_175828_a, false);
/* 1058 */         fillWithBlocks(worldIn, p_175837_3_, 21, 4, 21, 36, 4, 21, field_175828_a, field_175828_a, false);
/* 1059 */         fillWithBlocks(worldIn, p_175837_3_, 22, 3, 21, 26, 3, 21, field_175828_a, field_175828_a, false);
/* 1060 */         fillWithBlocks(worldIn, p_175837_3_, 31, 3, 21, 35, 3, 21, field_175828_a, field_175828_a, false);
/* 1061 */         fillWithBlocks(worldIn, p_175837_3_, 23, 2, 21, 25, 2, 21, field_175828_a, field_175828_a, false);
/* 1062 */         fillWithBlocks(worldIn, p_175837_3_, 32, 2, 21, 34, 2, 21, field_175828_a, field_175828_a, false);
/* 1063 */         fillWithBlocks(worldIn, p_175837_3_, 28, 4, 20, 29, 4, 21, field_175826_b, field_175826_b, false);
/* 1064 */         setBlockState(worldIn, field_175826_b, 27, 3, 21, p_175837_3_);
/* 1065 */         setBlockState(worldIn, field_175826_b, 30, 3, 21, p_175837_3_);
/* 1066 */         setBlockState(worldIn, field_175826_b, 26, 2, 21, p_175837_3_);
/* 1067 */         setBlockState(worldIn, field_175826_b, 31, 2, 21, p_175837_3_);
/* 1068 */         setBlockState(worldIn, field_175826_b, 25, 1, 21, p_175837_3_);
/* 1069 */         setBlockState(worldIn, field_175826_b, 32, 1, 21, p_175837_3_);
/*      */         
/* 1071 */         for (int i = 0; i < 7; i++)
/*      */         {
/* 1073 */           setBlockState(worldIn, field_175827_c, 28 - i, 6 + i, 21, p_175837_3_);
/* 1074 */           setBlockState(worldIn, field_175827_c, 29 + i, 6 + i, 21, p_175837_3_);
/*      */         }
/*      */         
/* 1077 */         for (int j = 0; j < 4; j++)
/*      */         {
/* 1079 */           setBlockState(worldIn, field_175827_c, 28 - j, 9 + j, 21, p_175837_3_);
/* 1080 */           setBlockState(worldIn, field_175827_c, 29 + j, 9 + j, 21, p_175837_3_);
/*      */         }
/*      */         
/* 1083 */         setBlockState(worldIn, field_175827_c, 28, 12, 21, p_175837_3_);
/* 1084 */         setBlockState(worldIn, field_175827_c, 29, 12, 21, p_175837_3_);
/*      */         
/* 1086 */         for (int k = 0; k < 3; k++)
/*      */         {
/* 1088 */           setBlockState(worldIn, field_175827_c, 22 - k * 2, 8, 21, p_175837_3_);
/* 1089 */           setBlockState(worldIn, field_175827_c, 22 - k * 2, 9, 21, p_175837_3_);
/* 1090 */           setBlockState(worldIn, field_175827_c, 35 + k * 2, 8, 21, p_175837_3_);
/* 1091 */           setBlockState(worldIn, field_175827_c, 35 + k * 2, 9, 21, p_175837_3_);
/*      */         }
/*      */         
/* 1094 */         func_181655_a(worldIn, p_175837_3_, 15, 13, 21, 42, 15, 21, false);
/* 1095 */         func_181655_a(worldIn, p_175837_3_, 15, 1, 21, 15, 6, 21, false);
/* 1096 */         func_181655_a(worldIn, p_175837_3_, 16, 1, 21, 16, 5, 21, false);
/* 1097 */         func_181655_a(worldIn, p_175837_3_, 17, 1, 21, 20, 4, 21, false);
/* 1098 */         func_181655_a(worldIn, p_175837_3_, 21, 1, 21, 21, 3, 21, false);
/* 1099 */         func_181655_a(worldIn, p_175837_3_, 22, 1, 21, 22, 2, 21, false);
/* 1100 */         func_181655_a(worldIn, p_175837_3_, 23, 1, 21, 24, 1, 21, false);
/* 1101 */         func_181655_a(worldIn, p_175837_3_, 42, 1, 21, 42, 6, 21, false);
/* 1102 */         func_181655_a(worldIn, p_175837_3_, 41, 1, 21, 41, 5, 21, false);
/* 1103 */         func_181655_a(worldIn, p_175837_3_, 37, 1, 21, 40, 4, 21, false);
/* 1104 */         func_181655_a(worldIn, p_175837_3_, 36, 1, 21, 36, 3, 21, false);
/* 1105 */         func_181655_a(worldIn, p_175837_3_, 33, 1, 21, 34, 1, 21, false);
/* 1106 */         func_181655_a(worldIn, p_175837_3_, 35, 1, 21, 35, 2, 21, false);
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175841_d(World worldIn, Random p_175841_2_, StructureBoundingBox p_175841_3_)
/*      */     {
/* 1112 */       if (func_175818_a(p_175841_3_, 21, 21, 36, 36))
/*      */       {
/* 1114 */         fillWithBlocks(worldIn, p_175841_3_, 21, 0, 22, 36, 0, 36, field_175828_a, field_175828_a, false);
/* 1115 */         func_181655_a(worldIn, p_175841_3_, 21, 1, 22, 36, 23, 36, false);
/*      */         
/* 1117 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 1119 */           fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 21 + i, 36 - i, 13 + i, 21 + i, field_175826_b, field_175826_b, false);
/* 1120 */           fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 36 - i, 36 - i, 13 + i, 36 - i, field_175826_b, field_175826_b, false);
/* 1121 */           fillWithBlocks(worldIn, p_175841_3_, 21 + i, 13 + i, 22 + i, 21 + i, 13 + i, 35 - i, field_175826_b, field_175826_b, false);
/* 1122 */           fillWithBlocks(worldIn, p_175841_3_, 36 - i, 13 + i, 22 + i, 36 - i, 13 + i, 35 - i, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1125 */         fillWithBlocks(worldIn, p_175841_3_, 25, 16, 25, 32, 16, 32, field_175828_a, field_175828_a, false);
/* 1126 */         fillWithBlocks(worldIn, p_175841_3_, 25, 17, 25, 25, 19, 25, field_175826_b, field_175826_b, false);
/* 1127 */         fillWithBlocks(worldIn, p_175841_3_, 32, 17, 25, 32, 19, 25, field_175826_b, field_175826_b, false);
/* 1128 */         fillWithBlocks(worldIn, p_175841_3_, 25, 17, 32, 25, 19, 32, field_175826_b, field_175826_b, false);
/* 1129 */         fillWithBlocks(worldIn, p_175841_3_, 32, 17, 32, 32, 19, 32, field_175826_b, field_175826_b, false);
/* 1130 */         setBlockState(worldIn, field_175826_b, 26, 20, 26, p_175841_3_);
/* 1131 */         setBlockState(worldIn, field_175826_b, 27, 21, 27, p_175841_3_);
/* 1132 */         setBlockState(worldIn, field_175825_e, 27, 20, 27, p_175841_3_);
/* 1133 */         setBlockState(worldIn, field_175826_b, 26, 20, 31, p_175841_3_);
/* 1134 */         setBlockState(worldIn, field_175826_b, 27, 21, 30, p_175841_3_);
/* 1135 */         setBlockState(worldIn, field_175825_e, 27, 20, 30, p_175841_3_);
/* 1136 */         setBlockState(worldIn, field_175826_b, 31, 20, 31, p_175841_3_);
/* 1137 */         setBlockState(worldIn, field_175826_b, 30, 21, 30, p_175841_3_);
/* 1138 */         setBlockState(worldIn, field_175825_e, 30, 20, 30, p_175841_3_);
/* 1139 */         setBlockState(worldIn, field_175826_b, 31, 20, 26, p_175841_3_);
/* 1140 */         setBlockState(worldIn, field_175826_b, 30, 21, 27, p_175841_3_);
/* 1141 */         setBlockState(worldIn, field_175825_e, 30, 20, 27, p_175841_3_);
/* 1142 */         fillWithBlocks(worldIn, p_175841_3_, 28, 21, 27, 29, 21, 27, field_175828_a, field_175828_a, false);
/* 1143 */         fillWithBlocks(worldIn, p_175841_3_, 27, 21, 28, 27, 21, 29, field_175828_a, field_175828_a, false);
/* 1144 */         fillWithBlocks(worldIn, p_175841_3_, 28, 21, 30, 29, 21, 30, field_175828_a, field_175828_a, false);
/* 1145 */         fillWithBlocks(worldIn, p_175841_3_, 30, 21, 28, 30, 21, 29, field_175828_a, field_175828_a, false);
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175835_e(World worldIn, Random p_175835_2_, StructureBoundingBox p_175835_3_)
/*      */     {
/* 1151 */       if (func_175818_a(p_175835_3_, 0, 21, 6, 58))
/*      */       {
/* 1153 */         fillWithBlocks(worldIn, p_175835_3_, 0, 0, 21, 6, 0, 57, field_175828_a, field_175828_a, false);
/* 1154 */         func_181655_a(worldIn, p_175835_3_, 0, 1, 21, 6, 7, 57, false);
/* 1155 */         fillWithBlocks(worldIn, p_175835_3_, 4, 4, 21, 6, 4, 53, field_175828_a, field_175828_a, false);
/*      */         
/* 1157 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 1159 */           fillWithBlocks(worldIn, p_175835_3_, i, i + 1, 21, i, i + 1, 57 - i, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1162 */         for (int j = 23; j < 53; j += 3)
/*      */         {
/* 1164 */           setBlockState(worldIn, field_175824_d, 5, 5, j, p_175835_3_);
/*      */         }
/*      */         
/* 1167 */         setBlockState(worldIn, field_175824_d, 5, 5, 52, p_175835_3_);
/*      */         
/* 1169 */         for (int k = 0; k < 4; k++)
/*      */         {
/* 1171 */           fillWithBlocks(worldIn, p_175835_3_, k, k + 1, 21, k, k + 1, 57 - k, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1174 */         fillWithBlocks(worldIn, p_175835_3_, 4, 1, 52, 6, 3, 52, field_175828_a, field_175828_a, false);
/* 1175 */         fillWithBlocks(worldIn, p_175835_3_, 5, 1, 51, 5, 3, 53, field_175828_a, field_175828_a, false);
/*      */       }
/*      */       
/* 1178 */       if (func_175818_a(p_175835_3_, 51, 21, 58, 58))
/*      */       {
/* 1180 */         fillWithBlocks(worldIn, p_175835_3_, 51, 0, 21, 57, 0, 57, field_175828_a, field_175828_a, false);
/* 1181 */         func_181655_a(worldIn, p_175835_3_, 51, 1, 21, 57, 7, 57, false);
/* 1182 */         fillWithBlocks(worldIn, p_175835_3_, 51, 4, 21, 53, 4, 53, field_175828_a, field_175828_a, false);
/*      */         
/* 1184 */         for (int l = 0; l < 4; l++)
/*      */         {
/* 1186 */           fillWithBlocks(worldIn, p_175835_3_, 57 - l, l + 1, 21, 57 - l, l + 1, 57 - l, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1189 */         for (int i1 = 23; i1 < 53; i1 += 3)
/*      */         {
/* 1191 */           setBlockState(worldIn, field_175824_d, 52, 5, i1, p_175835_3_);
/*      */         }
/*      */         
/* 1194 */         setBlockState(worldIn, field_175824_d, 52, 5, 52, p_175835_3_);
/* 1195 */         fillWithBlocks(worldIn, p_175835_3_, 51, 1, 52, 53, 3, 52, field_175828_a, field_175828_a, false);
/* 1196 */         fillWithBlocks(worldIn, p_175835_3_, 52, 1, 51, 52, 3, 53, field_175828_a, field_175828_a, false);
/*      */       }
/*      */       
/* 1199 */       if (func_175818_a(p_175835_3_, 0, 51, 57, 57))
/*      */       {
/* 1201 */         fillWithBlocks(worldIn, p_175835_3_, 7, 0, 51, 50, 0, 57, field_175828_a, field_175828_a, false);
/* 1202 */         func_181655_a(worldIn, p_175835_3_, 7, 1, 51, 50, 10, 57, false);
/*      */         
/* 1204 */         for (int j1 = 0; j1 < 4; j1++)
/*      */         {
/* 1206 */           fillWithBlocks(worldIn, p_175835_3_, j1 + 1, j1 + 1, 57 - j1, 56 - j1, j1 + 1, 57 - j1, field_175826_b, field_175826_b, false);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175842_f(World worldIn, Random p_175842_2_, StructureBoundingBox p_175842_3_)
/*      */     {
/* 1213 */       if (func_175818_a(p_175842_3_, 7, 21, 13, 50))
/*      */       {
/* 1215 */         fillWithBlocks(worldIn, p_175842_3_, 7, 0, 21, 13, 0, 50, field_175828_a, field_175828_a, false);
/* 1216 */         func_181655_a(worldIn, p_175842_3_, 7, 1, 21, 13, 10, 50, false);
/* 1217 */         fillWithBlocks(worldIn, p_175842_3_, 11, 8, 21, 13, 8, 53, field_175828_a, field_175828_a, false);
/*      */         
/* 1219 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 1221 */           fillWithBlocks(worldIn, p_175842_3_, i + 7, i + 5, 21, i + 7, i + 5, 54, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1224 */         for (int j = 21; j <= 45; j += 3)
/*      */         {
/* 1226 */           setBlockState(worldIn, field_175824_d, 12, 9, j, p_175842_3_);
/*      */         }
/*      */       }
/*      */       
/* 1230 */       if (func_175818_a(p_175842_3_, 44, 21, 50, 54))
/*      */       {
/* 1232 */         fillWithBlocks(worldIn, p_175842_3_, 44, 0, 21, 50, 0, 50, field_175828_a, field_175828_a, false);
/* 1233 */         func_181655_a(worldIn, p_175842_3_, 44, 1, 21, 50, 10, 50, false);
/* 1234 */         fillWithBlocks(worldIn, p_175842_3_, 44, 8, 21, 46, 8, 53, field_175828_a, field_175828_a, false);
/*      */         
/* 1236 */         for (int k = 0; k < 4; k++)
/*      */         {
/* 1238 */           fillWithBlocks(worldIn, p_175842_3_, 50 - k, k + 5, 21, 50 - k, k + 5, 54, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1241 */         for (int l = 21; l <= 45; l += 3)
/*      */         {
/* 1243 */           setBlockState(worldIn, field_175824_d, 45, 9, l, p_175842_3_);
/*      */         }
/*      */       }
/*      */       
/* 1247 */       if (func_175818_a(p_175842_3_, 8, 44, 49, 54))
/*      */       {
/* 1249 */         fillWithBlocks(worldIn, p_175842_3_, 14, 0, 44, 43, 0, 50, field_175828_a, field_175828_a, false);
/* 1250 */         func_181655_a(worldIn, p_175842_3_, 14, 1, 44, 43, 10, 50, false);
/*      */         
/* 1252 */         for (int i1 = 12; i1 <= 45; i1 += 3)
/*      */         {
/* 1254 */           setBlockState(worldIn, field_175824_d, i1, 9, 45, p_175842_3_);
/* 1255 */           setBlockState(worldIn, field_175824_d, i1, 9, 52, p_175842_3_);
/*      */           
/* 1257 */           if ((i1 == 12) || (i1 == 18) || (i1 == 24) || (i1 == 33) || (i1 == 39) || (i1 == 45))
/*      */           {
/* 1259 */             setBlockState(worldIn, field_175824_d, i1, 9, 47, p_175842_3_);
/* 1260 */             setBlockState(worldIn, field_175824_d, i1, 9, 50, p_175842_3_);
/* 1261 */             setBlockState(worldIn, field_175824_d, i1, 10, 45, p_175842_3_);
/* 1262 */             setBlockState(worldIn, field_175824_d, i1, 10, 46, p_175842_3_);
/* 1263 */             setBlockState(worldIn, field_175824_d, i1, 10, 51, p_175842_3_);
/* 1264 */             setBlockState(worldIn, field_175824_d, i1, 10, 52, p_175842_3_);
/* 1265 */             setBlockState(worldIn, field_175824_d, i1, 11, 47, p_175842_3_);
/* 1266 */             setBlockState(worldIn, field_175824_d, i1, 11, 50, p_175842_3_);
/* 1267 */             setBlockState(worldIn, field_175824_d, i1, 12, 48, p_175842_3_);
/* 1268 */             setBlockState(worldIn, field_175824_d, i1, 12, 49, p_175842_3_);
/*      */           }
/*      */         }
/*      */         
/* 1272 */         for (int j1 = 0; j1 < 3; j1++)
/*      */         {
/* 1274 */           fillWithBlocks(worldIn, p_175842_3_, 8 + j1, 5 + j1, 54, 49 - j1, 5 + j1, 54, field_175828_a, field_175828_a, false);
/*      */         }
/*      */         
/* 1277 */         fillWithBlocks(worldIn, p_175842_3_, 11, 8, 54, 46, 8, 54, field_175826_b, field_175826_b, false);
/* 1278 */         fillWithBlocks(worldIn, p_175842_3_, 14, 8, 44, 43, 8, 53, field_175828_a, field_175828_a, false);
/*      */       }
/*      */     }
/*      */     
/*      */     private void func_175838_g(World worldIn, Random p_175838_2_, StructureBoundingBox p_175838_3_)
/*      */     {
/* 1284 */       if (func_175818_a(p_175838_3_, 14, 21, 20, 43))
/*      */       {
/* 1286 */         fillWithBlocks(worldIn, p_175838_3_, 14, 0, 21, 20, 0, 43, field_175828_a, field_175828_a, false);
/* 1287 */         func_181655_a(worldIn, p_175838_3_, 14, 1, 22, 20, 14, 43, false);
/* 1288 */         fillWithBlocks(worldIn, p_175838_3_, 18, 12, 22, 20, 12, 39, field_175828_a, field_175828_a, false);
/* 1289 */         fillWithBlocks(worldIn, p_175838_3_, 18, 12, 21, 20, 12, 21, field_175826_b, field_175826_b, false);
/*      */         
/* 1291 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 1293 */           fillWithBlocks(worldIn, p_175838_3_, i + 14, i + 9, 21, i + 14, i + 9, 43 - i, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1296 */         for (int j = 23; j <= 39; j += 3)
/*      */         {
/* 1298 */           setBlockState(worldIn, field_175824_d, 19, 13, j, p_175838_3_);
/*      */         }
/*      */       }
/*      */       
/* 1302 */       if (func_175818_a(p_175838_3_, 37, 21, 43, 43))
/*      */       {
/* 1304 */         fillWithBlocks(worldIn, p_175838_3_, 37, 0, 21, 43, 0, 43, field_175828_a, field_175828_a, false);
/* 1305 */         func_181655_a(worldIn, p_175838_3_, 37, 1, 22, 43, 14, 43, false);
/* 1306 */         fillWithBlocks(worldIn, p_175838_3_, 37, 12, 22, 39, 12, 39, field_175828_a, field_175828_a, false);
/* 1307 */         fillWithBlocks(worldIn, p_175838_3_, 37, 12, 21, 39, 12, 21, field_175826_b, field_175826_b, false);
/*      */         
/* 1309 */         for (int k = 0; k < 4; k++)
/*      */         {
/* 1311 */           fillWithBlocks(worldIn, p_175838_3_, 43 - k, k + 9, 21, 43 - k, k + 9, 43 - k, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1314 */         for (int l = 23; l <= 39; l += 3)
/*      */         {
/* 1316 */           setBlockState(worldIn, field_175824_d, 38, 13, l, p_175838_3_);
/*      */         }
/*      */       }
/*      */       
/* 1320 */       if (func_175818_a(p_175838_3_, 15, 37, 42, 43))
/*      */       {
/* 1322 */         fillWithBlocks(worldIn, p_175838_3_, 21, 0, 37, 36, 0, 43, field_175828_a, field_175828_a, false);
/* 1323 */         func_181655_a(worldIn, p_175838_3_, 21, 1, 37, 36, 14, 43, false);
/* 1324 */         fillWithBlocks(worldIn, p_175838_3_, 21, 12, 37, 36, 12, 39, field_175828_a, field_175828_a, false);
/*      */         
/* 1326 */         for (int i1 = 0; i1 < 4; i1++)
/*      */         {
/* 1328 */           fillWithBlocks(worldIn, p_175838_3_, 15 + i1, i1 + 9, 43 - i1, 42 - i1, i1 + 9, 43 - i1, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1331 */         for (int j1 = 21; j1 <= 36; j1 += 3)
/*      */         {
/* 1333 */           setBlockState(worldIn, field_175824_d, j1, 13, 38, p_175838_3_);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class MonumentCoreRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public MonumentCoreRoom() {}
/*      */     
/*      */     public MonumentCoreRoom(EnumFacing p_i45598_1_, StructureOceanMonumentPieces.RoomDefinition p_i45598_2_, Random p_i45598_3_)
/*      */     {
/* 1347 */       super(p_i45598_1_, p_i45598_2_, 2, 2, 2);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1352 */       func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 0, 14, 8, 14, field_175828_a);
/* 1353 */       int i = 7;
/* 1354 */       IBlockState iblockstate = field_175826_b;
/* 1355 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 15, iblockstate, iblockstate, false);
/* 1356 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 15, i, 0, 15, i, 15, iblockstate, iblockstate, false);
/* 1357 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 15, i, 0, iblockstate, iblockstate, false);
/* 1358 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 14, i, 15, iblockstate, iblockstate, false);
/*      */       
/* 1360 */       for (i = 1; i <= 6; i++)
/*      */       {
/* 1362 */         iblockstate = field_175826_b;
/*      */         
/* 1364 */         if ((i == 2) || (i == 6))
/*      */         {
/* 1366 */           iblockstate = field_175828_a;
/*      */         }
/*      */         
/* 1369 */         for (int j = 0; j <= 15; j += 15)
/*      */         {
/* 1371 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 0, j, i, 1, iblockstate, iblockstate, false);
/* 1372 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 6, j, i, 9, iblockstate, iblockstate, false);
/* 1373 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 14, j, i, 15, iblockstate, iblockstate, false);
/*      */         }
/*      */         
/* 1376 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 1, i, 0, iblockstate, iblockstate, false);
/* 1377 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, i, 0, 9, i, 0, iblockstate, iblockstate, false);
/* 1378 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 14, i, 0, 14, i, 0, iblockstate, iblockstate, false);
/* 1379 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 14, i, 15, iblockstate, iblockstate, false);
/*      */       }
/*      */       
/* 1382 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 6, 9, 6, 9, field_175827_c, field_175827_c, false);
/* 1383 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 7, 8, 5, 8, Blocks.gold_block.getDefaultState(), Blocks.gold_block.getDefaultState(), false);
/*      */       
/* 1385 */       for (i = 3; i <= 6; i += 3)
/*      */       {
/* 1387 */         for (int k = 6; k <= 9; k += 3)
/*      */         {
/* 1389 */           setBlockState(worldIn, field_175825_e, k, i, 6, structureBoundingBoxIn);
/* 1390 */           setBlockState(worldIn, field_175825_e, k, i, 9, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1394 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 2, 6, field_175826_b, field_175826_b, false);
/* 1395 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 2, 9, field_175826_b, field_175826_b, false);
/* 1396 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 6, 10, 2, 6, field_175826_b, field_175826_b, false);
/* 1397 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 9, 10, 2, 9, field_175826_b, field_175826_b, false);
/* 1398 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 5, 6, 2, 5, field_175826_b, field_175826_b, false);
/* 1399 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 5, 9, 2, 5, field_175826_b, field_175826_b, false);
/* 1400 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 10, 6, 2, 10, field_175826_b, field_175826_b, false);
/* 1401 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 10, 9, 2, 10, field_175826_b, field_175826_b, false);
/* 1402 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 5, 5, 6, 5, field_175826_b, field_175826_b, false);
/* 1403 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 10, 5, 6, 10, field_175826_b, field_175826_b, false);
/* 1404 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 5, 10, 6, 5, field_175826_b, field_175826_b, false);
/* 1405 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 10, 10, 6, 10, field_175826_b, field_175826_b, false);
/* 1406 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 1, 5, 7, 6, field_175826_b, field_175826_b, false);
/* 1407 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 1, 10, 7, 6, field_175826_b, field_175826_b, false);
/* 1408 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 9, 5, 7, 14, field_175826_b, field_175826_b, false);
/* 1409 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 9, 10, 7, 14, field_175826_b, field_175826_b, false);
/* 1410 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 5, 6, 7, 5, field_175826_b, field_175826_b, false);
/* 1411 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 10, 6, 7, 10, field_175826_b, field_175826_b, false);
/* 1412 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 5, 14, 7, 5, field_175826_b, field_175826_b, false);
/* 1413 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 10, 14, 7, 10, field_175826_b, field_175826_b, false);
/* 1414 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 1, 3, field_175826_b, field_175826_b, false);
/* 1415 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 2, field_175826_b, field_175826_b, false);
/* 1416 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 2, 13, 1, 3, field_175826_b, field_175826_b, false);
/* 1417 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 12, 1, 2, field_175826_b, field_175826_b, false);
/* 1418 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 2, 1, 13, field_175826_b, field_175826_b, false);
/* 1419 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 13, 3, 1, 13, field_175826_b, field_175826_b, false);
/* 1420 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 12, 13, 1, 13, field_175826_b, field_175826_b, false);
/* 1421 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 13, 12, 1, 13, field_175826_b, field_175826_b, false);
/* 1422 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static abstract interface MonumentRoomFitHelper
/*      */   {
/*      */     public abstract boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition paramRoomDefinition);
/*      */     
/*      */     public abstract StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing paramEnumFacing, StructureOceanMonumentPieces.RoomDefinition paramRoomDefinition, Random paramRandom);
/*      */   }
/*      */   
/*      */   public static class Penthouse
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public Penthouse() {}
/*      */     
/*      */     public Penthouse(EnumFacing p_i45591_1_, StructureBoundingBox p_i45591_2_)
/*      */     {
/* 1441 */       super(p_i45591_2_);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1446 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 2, 11, -1, 11, field_175826_b, field_175826_b, false);
/* 1447 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -1, 0, 1, -1, 11, field_175828_a, field_175828_a, false);
/* 1448 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 12, -1, 0, 13, -1, 11, field_175828_a, field_175828_a, false);
/* 1449 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 0, 11, -1, 1, field_175828_a, field_175828_a, false);
/* 1450 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 12, 11, -1, 13, field_175828_a, field_175828_a, false);
/* 1451 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 13, field_175826_b, field_175826_b, false);
/* 1452 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 0, 0, 13, 0, 13, field_175826_b, field_175826_b, false);
/* 1453 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 12, 0, 0, field_175826_b, field_175826_b, false);
/* 1454 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 13, 12, 0, 13, field_175826_b, field_175826_b, false);
/*      */       
/* 1456 */       for (int i = 2; i <= 11; i += 3)
/*      */       {
/* 1458 */         setBlockState(worldIn, field_175825_e, 0, 0, i, structureBoundingBoxIn);
/* 1459 */         setBlockState(worldIn, field_175825_e, 13, 0, i, structureBoundingBoxIn);
/* 1460 */         setBlockState(worldIn, field_175825_e, i, 0, 0, structureBoundingBoxIn);
/*      */       }
/*      */       
/* 1463 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 3, 4, 0, 9, field_175826_b, field_175826_b, false);
/* 1464 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 3, 11, 0, 9, field_175826_b, field_175826_b, false);
/* 1465 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 9, 0, 11, field_175826_b, field_175826_b, false);
/* 1466 */       setBlockState(worldIn, field_175826_b, 5, 0, 8, structureBoundingBoxIn);
/* 1467 */       setBlockState(worldIn, field_175826_b, 8, 0, 8, structureBoundingBoxIn);
/* 1468 */       setBlockState(worldIn, field_175826_b, 10, 0, 10, structureBoundingBoxIn);
/* 1469 */       setBlockState(worldIn, field_175826_b, 3, 0, 10, structureBoundingBoxIn);
/* 1470 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 3, 3, 0, 7, field_175827_c, field_175827_c, false);
/* 1471 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 3, 10, 0, 7, field_175827_c, field_175827_c, false);
/* 1472 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 10, 7, 0, 10, field_175827_c, field_175827_c, false);
/* 1473 */       int l = 3;
/*      */       
/* 1475 */       for (int j = 0; j < 2; j++)
/*      */       {
/* 1477 */         for (int k = 2; k <= 8; k += 3)
/*      */         {
/* 1479 */           fillWithBlocks(worldIn, structureBoundingBoxIn, l, 0, k, l, 2, k, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1482 */         l = 10;
/*      */       }
/*      */       
/* 1485 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 10, 5, 2, 10, field_175826_b, field_175826_b, false);
/* 1486 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 10, 8, 2, 10, field_175826_b, field_175826_b, false);
/* 1487 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, -1, 7, 7, -1, 8, field_175827_c, field_175827_c, false);
/* 1488 */       func_181655_a(worldIn, structureBoundingBoxIn, 6, -1, 3, 7, -1, 4, false);
/* 1489 */       func_175817_a(worldIn, structureBoundingBoxIn, 6, 1, 6);
/* 1490 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class Piece extends StructureComponent
/*      */   {
/* 1496 */     protected static final IBlockState field_175828_a = Blocks.prismarine.getStateFromMeta(BlockPrismarine.ROUGH_META);
/* 1497 */     protected static final IBlockState field_175826_b = Blocks.prismarine.getStateFromMeta(BlockPrismarine.BRICKS_META);
/* 1498 */     protected static final IBlockState field_175827_c = Blocks.prismarine.getStateFromMeta(BlockPrismarine.DARK_META);
/* 1499 */     protected static final IBlockState field_175824_d = field_175826_b;
/* 1500 */     protected static final IBlockState field_175825_e = Blocks.sea_lantern.getDefaultState();
/* 1501 */     protected static final IBlockState field_175822_f = Blocks.water.getDefaultState();
/* 1502 */     protected static final int field_175823_g = func_175820_a(2, 0, 0);
/* 1503 */     protected static final int field_175831_h = func_175820_a(2, 2, 0);
/* 1504 */     protected static final int field_175832_i = func_175820_a(0, 1, 0);
/* 1505 */     protected static final int field_175829_j = func_175820_a(4, 1, 0);
/*      */     protected StructureOceanMonumentPieces.RoomDefinition field_175830_k;
/*      */     
/*      */     protected static final int func_175820_a(int p_175820_0_, int p_175820_1_, int p_175820_2_)
/*      */     {
/* 1510 */       return p_175820_1_ * 25 + p_175820_2_ * 5 + p_175820_0_;
/*      */     }
/*      */     
/*      */     public Piece()
/*      */     {
/* 1515 */       super();
/*      */     }
/*      */     
/*      */     public Piece(int p_i45588_1_)
/*      */     {
/* 1520 */       super();
/*      */     }
/*      */     
/*      */     public Piece(EnumFacing p_i45589_1_, StructureBoundingBox p_i45589_2_)
/*      */     {
/* 1525 */       super();
/* 1526 */       this.coordBaseMode = p_i45589_1_;
/* 1527 */       this.boundingBox = p_i45589_2_;
/*      */     }
/*      */     
/*      */     protected Piece(int p_i45590_1_, EnumFacing p_i45590_2_, StructureOceanMonumentPieces.RoomDefinition p_i45590_3_, int p_i45590_4_, int p_i45590_5_, int p_i45590_6_)
/*      */     {
/* 1532 */       super();
/* 1533 */       this.coordBaseMode = p_i45590_2_;
/* 1534 */       this.field_175830_k = p_i45590_3_;
/* 1535 */       int i = p_i45590_3_.field_175967_a;
/* 1536 */       int j = i % 5;
/* 1537 */       int k = i / 5 % 5;
/* 1538 */       int l = i / 25;
/*      */       
/* 1540 */       if ((p_i45590_2_ != EnumFacing.NORTH) && (p_i45590_2_ != EnumFacing.SOUTH))
/*      */       {
/* 1542 */         this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_6_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_4_ * 8 - 1);
/*      */       }
/*      */       else
/*      */       {
/* 1546 */         this.boundingBox = new StructureBoundingBox(0, 0, 0, p_i45590_4_ * 8 - 1, p_i45590_5_ * 4 - 1, p_i45590_6_ * 8 - 1);
/*      */       }
/*      */       
/* 1549 */       switch (p_i45590_2_)
/*      */       {
/*      */       case NORTH: 
/* 1552 */         this.boundingBox.offset(j * 8, l * 4, -(k + p_i45590_6_) * 8 + 1);
/* 1553 */         break;
/*      */       
/*      */       case SOUTH: 
/* 1556 */         this.boundingBox.offset(j * 8, l * 4, k * 8);
/* 1557 */         break;
/*      */       
/*      */       case UP: 
/* 1560 */         this.boundingBox.offset(-(k + p_i45590_6_) * 8 + 1, l * 4, j * 8);
/* 1561 */         break;
/*      */       
/*      */       default: 
/* 1564 */         this.boundingBox.offset(k * 8, l * 4, j * 8);
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {}
/*      */     
/*      */ 
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {}
/*      */     
/*      */ 
/*      */     protected void func_181655_a(World p_181655_1_, StructureBoundingBox p_181655_2_, int p_181655_3_, int p_181655_4_, int p_181655_5_, int p_181655_6_, int p_181655_7_, int p_181655_8_, boolean p_181655_9_)
/*      */     {
/* 1578 */       for (int i = p_181655_4_; i <= p_181655_7_; i++)
/*      */       {
/* 1580 */         for (int j = p_181655_3_; j <= p_181655_6_; j++)
/*      */         {
/* 1582 */           for (int k = p_181655_5_; k <= p_181655_8_; k++)
/*      */           {
/* 1584 */             if ((!p_181655_9_) || (getBlockStateFromPos(p_181655_1_, j, i, k, p_181655_2_).getBlock().getMaterial() != Material.air))
/*      */             {
/* 1586 */               if (getYWithOffset(i) >= p_181655_1_.func_181545_F())
/*      */               {
/* 1588 */                 setBlockState(p_181655_1_, Blocks.air.getDefaultState(), j, i, k, p_181655_2_);
/*      */               }
/*      */               else
/*      */               {
/* 1592 */                 setBlockState(p_181655_1_, field_175822_f, j, i, k, p_181655_2_);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected void func_175821_a(World worldIn, StructureBoundingBox p_175821_2_, int p_175821_3_, int p_175821_4_, boolean p_175821_5_)
/*      */     {
/* 1602 */       if (p_175821_5_)
/*      */       {
/* 1604 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 2, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
/* 1605 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
/* 1606 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 0, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175828_a, field_175828_a, false);
/* 1607 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
/* 1608 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 2, p_175821_3_ + 4, 0, p_175821_4_ + 2, field_175826_b, field_175826_b, false);
/* 1609 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 3, 0, p_175821_4_ + 5, p_175821_3_ + 4, 0, p_175821_4_ + 5, field_175826_b, field_175826_b, false);
/* 1610 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 2, 0, p_175821_4_ + 3, p_175821_3_ + 2, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
/* 1611 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 5, 0, p_175821_4_ + 3, p_175821_3_ + 5, 0, p_175821_4_ + 4, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       else
/*      */       {
/* 1615 */         fillWithBlocks(worldIn, p_175821_2_, p_175821_3_ + 0, 0, p_175821_4_ + 0, p_175821_3_ + 8 - 1, 0, p_175821_4_ + 8 - 1, field_175828_a, field_175828_a, false);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void func_175819_a(World worldIn, StructureBoundingBox p_175819_2_, int p_175819_3_, int p_175819_4_, int p_175819_5_, int p_175819_6_, int p_175819_7_, int p_175819_8_, IBlockState p_175819_9_)
/*      */     {
/* 1621 */       for (int i = p_175819_4_; i <= p_175819_7_; i++)
/*      */       {
/* 1623 */         for (int j = p_175819_3_; j <= p_175819_6_; j++)
/*      */         {
/* 1625 */           for (int k = p_175819_5_; k <= p_175819_8_; k++)
/*      */           {
/* 1627 */             if (getBlockStateFromPos(worldIn, j, i, k, p_175819_2_) == field_175822_f)
/*      */             {
/* 1629 */               setBlockState(worldIn, p_175819_9_, j, i, k, p_175819_2_);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected boolean func_175818_a(StructureBoundingBox p_175818_1_, int p_175818_2_, int p_175818_3_, int p_175818_4_, int p_175818_5_)
/*      */     {
/* 1638 */       int i = getXWithOffset(p_175818_2_, p_175818_3_);
/* 1639 */       int j = getZWithOffset(p_175818_2_, p_175818_3_);
/* 1640 */       int k = getXWithOffset(p_175818_4_, p_175818_5_);
/* 1641 */       int l = getZWithOffset(p_175818_4_, p_175818_5_);
/* 1642 */       return p_175818_1_.intersectsWith(Math.min(i, k), Math.min(j, l), Math.max(i, k), Math.max(j, l));
/*      */     }
/*      */     
/*      */     protected boolean func_175817_a(World worldIn, StructureBoundingBox p_175817_2_, int p_175817_3_, int p_175817_4_, int p_175817_5_)
/*      */     {
/* 1647 */       int i = getXWithOffset(p_175817_3_, p_175817_5_);
/* 1648 */       int j = getYWithOffset(p_175817_4_);
/* 1649 */       int k = getZWithOffset(p_175817_3_, p_175817_5_);
/*      */       
/* 1651 */       if (p_175817_2_.isVecInside(new BlockPos(i, j, k)))
/*      */       {
/* 1653 */         EntityGuardian entityguardian = new EntityGuardian(worldIn);
/* 1654 */         entityguardian.setElder(true);
/* 1655 */         entityguardian.heal(entityguardian.getMaxHealth());
/* 1656 */         entityguardian.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0.0F, 0.0F);
/* 1657 */         entityguardian.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityguardian)), null);
/* 1658 */         worldIn.spawnEntityInWorld(entityguardian);
/* 1659 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 1663 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static class RoomDefinition
/*      */   {
/*      */     int field_175967_a;
/* 1671 */     RoomDefinition[] field_175965_b = new RoomDefinition[6];
/* 1672 */     boolean[] field_175966_c = new boolean[6];
/*      */     boolean field_175963_d;
/*      */     boolean field_175964_e;
/*      */     int field_175962_f;
/*      */     
/*      */     public RoomDefinition(int p_i45584_1_)
/*      */     {
/* 1679 */       this.field_175967_a = p_i45584_1_;
/*      */     }
/*      */     
/*      */     public void func_175957_a(EnumFacing p_175957_1_, RoomDefinition p_175957_2_)
/*      */     {
/* 1684 */       this.field_175965_b[p_175957_1_.getIndex()] = p_175957_2_;
/* 1685 */       p_175957_2_.field_175965_b[p_175957_1_.getOpposite().getIndex()] = this;
/*      */     }
/*      */     
/*      */     public void func_175958_a()
/*      */     {
/* 1690 */       for (int i = 0; i < 6; i++)
/*      */       {
/* 1692 */         this.field_175966_c[i] = (this.field_175965_b[i] != null ? 1 : false);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean func_175959_a(int p_175959_1_)
/*      */     {
/* 1698 */       if (this.field_175964_e)
/*      */       {
/* 1700 */         return true;
/*      */       }
/*      */       
/*      */ 
/* 1704 */       this.field_175962_f = p_175959_1_;
/*      */       
/* 1706 */       for (int i = 0; i < 6; i++)
/*      */       {
/* 1708 */         if ((this.field_175965_b[i] != null) && (this.field_175966_c[i] != 0) && (this.field_175965_b[i].field_175962_f != p_175959_1_) && (this.field_175965_b[i].func_175959_a(p_175959_1_)))
/*      */         {
/* 1710 */           return true;
/*      */         }
/*      */       }
/*      */       
/* 1714 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean func_175961_b()
/*      */     {
/* 1720 */       return this.field_175967_a >= 75;
/*      */     }
/*      */     
/*      */     public int func_175960_c()
/*      */     {
/* 1725 */       int i = 0;
/*      */       
/* 1727 */       for (int j = 0; j < 6; j++)
/*      */       {
/* 1729 */         if (this.field_175966_c[j] != 0)
/*      */         {
/* 1731 */           i++;
/*      */         }
/*      */       }
/*      */       
/* 1735 */       return i;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class SimpleRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     private int field_175833_o;
/*      */     
/*      */     public SimpleRoom() {}
/*      */     
/*      */     public SimpleRoom(EnumFacing p_i45587_1_, StructureOceanMonumentPieces.RoomDefinition p_i45587_2_, Random p_i45587_3_)
/*      */     {
/* 1749 */       super(p_i45587_1_, p_i45587_2_, 1, 1, 1);
/* 1750 */       this.field_175833_o = p_i45587_3_.nextInt(3);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1755 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/* 1757 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/* 1760 */       if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/* 1762 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 6, field_175828_a);
/*      */       }
/*      */       
/* 1765 */       boolean flag = (this.field_175833_o != 0) && (randomIn.nextBoolean()) && (this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()] == 0) && (this.field_175830_k.field_175966_c[EnumFacing.UP.getIndex()] == 0) && (this.field_175830_k.func_175960_c() > 1);
/*      */       
/* 1767 */       if (this.field_175833_o == 0)
/*      */       {
/* 1769 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 2, 1, 2, field_175826_b, field_175826_b, false);
/* 1770 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 2, 3, 2, field_175826_b, field_175826_b, false);
/* 1771 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 2, field_175828_a, field_175828_a, false);
/* 1772 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 2, 2, 0, field_175828_a, field_175828_a, false);
/* 1773 */         setBlockState(worldIn, field_175825_e, 1, 2, 1, structureBoundingBoxIn);
/* 1774 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 7, 1, 2, field_175826_b, field_175826_b, false);
/* 1775 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 7, 3, 2, field_175826_b, field_175826_b, false);
/* 1776 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 2, field_175828_a, field_175828_a, false);
/* 1777 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
/* 1778 */         setBlockState(worldIn, field_175825_e, 6, 2, 1, structureBoundingBoxIn);
/* 1779 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 5, 2, 1, 7, field_175826_b, field_175826_b, false);
/* 1780 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 5, 2, 3, 7, field_175826_b, field_175826_b, false);
/* 1781 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 2, 7, field_175828_a, field_175828_a, false);
/* 1782 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 2, 2, 7, field_175828_a, field_175828_a, false);
/* 1783 */         setBlockState(worldIn, field_175825_e, 1, 2, 6, structureBoundingBoxIn);
/* 1784 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 1, 7, field_175826_b, field_175826_b, false);
/* 1785 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 5, 7, 3, 7, field_175826_b, field_175826_b, false);
/* 1786 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 5, 7, 2, 7, field_175828_a, field_175828_a, false);
/* 1787 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
/* 1788 */         setBlockState(worldIn, field_175825_e, 6, 2, 6, structureBoundingBoxIn);
/*      */         
/* 1790 */         if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */         {
/* 1792 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 0, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/* 1796 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 1, field_175826_b, field_175826_b, false);
/* 1797 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 0, 4, 2, 0, field_175828_a, field_175828_a, false);
/* 1798 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 1, 1, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1801 */         if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */         {
/* 1803 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 7, 4, 3, 7, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/* 1807 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 6, 4, 3, 7, field_175826_b, field_175826_b, false);
/* 1808 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 7, 4, 2, 7, field_175828_a, field_175828_a, false);
/* 1809 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 6, 4, 1, 7, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1812 */         if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */         {
/* 1814 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 3, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/* 1818 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 1, 3, 4, field_175826_b, field_175826_b, false);
/* 1819 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 3, 0, 2, 4, field_175828_a, field_175828_a, false);
/* 1820 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 1, 1, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1823 */         if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */         {
/* 1825 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         else
/*      */         {
/* 1829 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
/* 1830 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 3, 7, 2, 4, field_175828_a, field_175828_a, false);
/* 1831 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 3, 7, 1, 4, field_175826_b, field_175826_b, false);
/*      */         }
/*      */       }
/* 1834 */       else if (this.field_175833_o == 1)
/*      */       {
/* 1836 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 3, 2, field_175826_b, field_175826_b, false);
/* 1837 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 5, 2, 3, 5, field_175826_b, field_175826_b, false);
/* 1838 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, field_175826_b, field_175826_b, false);
/* 1839 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 2, 5, 3, 2, field_175826_b, field_175826_b, false);
/* 1840 */         setBlockState(worldIn, field_175825_e, 2, 2, 2, structureBoundingBoxIn);
/* 1841 */         setBlockState(worldIn, field_175825_e, 2, 2, 5, structureBoundingBoxIn);
/* 1842 */         setBlockState(worldIn, field_175825_e, 5, 2, 5, structureBoundingBoxIn);
/* 1843 */         setBlockState(worldIn, field_175825_e, 5, 2, 2, structureBoundingBoxIn);
/* 1844 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 1, 3, 0, field_175826_b, field_175826_b, false);
/* 1845 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 1, field_175826_b, field_175826_b, false);
/* 1846 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 1, 3, 7, field_175826_b, field_175826_b, false);
/* 1847 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 6, 0, 3, 6, field_175826_b, field_175826_b, false);
/* 1848 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
/* 1849 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 7, 3, 6, field_175826_b, field_175826_b, false);
/* 1850 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
/* 1851 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 3, 1, field_175826_b, field_175826_b, false);
/* 1852 */         setBlockState(worldIn, field_175828_a, 1, 2, 0, structureBoundingBoxIn);
/* 1853 */         setBlockState(worldIn, field_175828_a, 0, 2, 1, structureBoundingBoxIn);
/* 1854 */         setBlockState(worldIn, field_175828_a, 1, 2, 7, structureBoundingBoxIn);
/* 1855 */         setBlockState(worldIn, field_175828_a, 0, 2, 6, structureBoundingBoxIn);
/* 1856 */         setBlockState(worldIn, field_175828_a, 6, 2, 7, structureBoundingBoxIn);
/* 1857 */         setBlockState(worldIn, field_175828_a, 7, 2, 6, structureBoundingBoxIn);
/* 1858 */         setBlockState(worldIn, field_175828_a, 6, 2, 0, structureBoundingBoxIn);
/* 1859 */         setBlockState(worldIn, field_175828_a, 7, 2, 1, structureBoundingBoxIn);
/*      */         
/* 1861 */         if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()] == 0)
/*      */         {
/* 1863 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
/* 1864 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
/* 1865 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1868 */         if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()] == 0)
/*      */         {
/* 1870 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
/* 1871 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
/* 1872 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1875 */         if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()] == 0)
/*      */         {
/* 1877 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 3, 6, field_175826_b, field_175826_b, false);
/* 1878 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 2, 6, field_175828_a, field_175828_a, false);
/* 1879 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 1, 6, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 1882 */         if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()] == 0)
/*      */         {
/* 1884 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 1, 7, 3, 6, field_175826_b, field_175826_b, false);
/* 1885 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 1, 7, 2, 6, field_175828_a, field_175828_a, false);
/* 1886 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 1, 6, field_175826_b, field_175826_b, false);
/*      */         }
/*      */       }
/* 1889 */       else if (this.field_175833_o == 2)
/*      */       {
/* 1891 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
/* 1892 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
/* 1893 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
/* 1894 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
/* 1895 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
/* 1896 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
/* 1897 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
/* 1898 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
/* 1899 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
/* 1900 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
/* 1901 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
/* 1902 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
/* 1903 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
/* 1904 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
/* 1905 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
/* 1906 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);
/*      */         
/* 1908 */         if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */         {
/* 1910 */           func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */         }
/*      */         
/* 1913 */         if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()] != 0)
/*      */         {
/* 1915 */           func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
/*      */         }
/*      */         
/* 1918 */         if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()] != 0)
/*      */         {
/* 1920 */           func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
/*      */         }
/*      */         
/* 1923 */         if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()] != 0)
/*      */         {
/* 1925 */           func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
/*      */         }
/*      */       }
/*      */       
/* 1929 */       if (flag)
/*      */       {
/* 1931 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, field_175826_b, field_175826_b, false);
/* 1932 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 3, 4, 2, 4, field_175828_a, field_175828_a, false);
/* 1933 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 3, 4, 3, 4, field_175826_b, field_175826_b, false);
/*      */       }
/*      */       
/* 1936 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class SimpleTopRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     public SimpleTopRoom() {}
/*      */     
/*      */     public SimpleTopRoom(EnumFacing p_i45586_1_, StructureOceanMonumentPieces.RoomDefinition p_i45586_2_, Random p_i45586_3_)
/*      */     {
/* 1948 */       super(p_i45586_1_, p_i45586_2_, 1, 1, 1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1953 */       if (this.field_175830_k.field_175967_a / 25 > 0)
/*      */       {
/* 1955 */         func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
/*      */       }
/*      */       
/* 1958 */       if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null)
/*      */       {
/* 1960 */         func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 6, field_175828_a);
/*      */       }
/*      */       
/* 1963 */       for (int i = 1; i <= 6; i++)
/*      */       {
/* 1965 */         for (int j = 1; j <= 6; j++)
/*      */         {
/* 1967 */           if (randomIn.nextInt(3) != 0)
/*      */           {
/* 1969 */             int k = 2 + (randomIn.nextInt(4) == 0 ? 0 : 1);
/* 1970 */             fillWithBlocks(worldIn, structureBoundingBoxIn, i, k, j, i, 3, j, Blocks.sponge.getStateFromMeta(1), Blocks.sponge.getStateFromMeta(1), false);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1975 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
/* 1976 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
/* 1977 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
/* 1978 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
/* 1979 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
/* 1980 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
/* 1981 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
/* 1982 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
/* 1983 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
/* 1984 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
/* 1985 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
/* 1986 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
/* 1987 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
/* 1988 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
/* 1989 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
/* 1990 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);
/*      */       
/* 1992 */       if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()] != 0)
/*      */       {
/* 1994 */         func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
/*      */       }
/*      */       
/* 1997 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class WingRoom
/*      */     extends StructureOceanMonumentPieces.Piece
/*      */   {
/*      */     private int field_175834_o;
/*      */     
/*      */     public WingRoom() {}
/*      */     
/*      */     public WingRoom(EnumFacing p_i45585_1_, StructureBoundingBox p_i45585_2_, int p_i45585_3_)
/*      */     {
/* 2011 */       super(p_i45585_2_);
/* 2012 */       this.field_175834_o = (p_i45585_3_ & 0x1);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 2017 */       if (this.field_175834_o == 0)
/*      */       {
/* 2019 */         for (int i = 0; i < 4; i++)
/*      */         {
/* 2021 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, field_175826_b, field_175826_b, false);
/*      */         }
/*      */         
/* 2024 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 6, 15, 0, 16, field_175826_b, field_175826_b, false);
/* 2025 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 6, 6, 3, 20, field_175826_b, field_175826_b, false);
/* 2026 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 16, 0, 6, 16, 3, 20, field_175826_b, field_175826_b, false);
/* 2027 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 7, 7, 1, 20, field_175826_b, field_175826_b, false);
/* 2028 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 7, 15, 1, 20, field_175826_b, field_175826_b, false);
/* 2029 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 9, 3, 6, field_175826_b, field_175826_b, false);
/* 2030 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 6, 15, 3, 6, field_175826_b, field_175826_b, false);
/* 2031 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 7, 9, 1, 7, field_175826_b, field_175826_b, false);
/* 2032 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
/* 2033 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 5, 13, 0, 5, field_175826_b, field_175826_b, false);
/* 2034 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 7, 12, 0, 7, field_175827_c, field_175827_c, false);
/* 2035 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 10, 8, 0, 12, field_175827_c, field_175827_c, false);
/* 2036 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 0, 10, 14, 0, 12, field_175827_c, field_175827_c, false);
/*      */         
/* 2038 */         for (int i1 = 18; i1 >= 7; i1 -= 3)
/*      */         {
/* 2040 */           setBlockState(worldIn, field_175825_e, 6, 3, i1, structureBoundingBoxIn);
/* 2041 */           setBlockState(worldIn, field_175825_e, 16, 3, i1, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 2044 */         setBlockState(worldIn, field_175825_e, 10, 0, 10, structureBoundingBoxIn);
/* 2045 */         setBlockState(worldIn, field_175825_e, 12, 0, 10, structureBoundingBoxIn);
/* 2046 */         setBlockState(worldIn, field_175825_e, 10, 0, 12, structureBoundingBoxIn);
/* 2047 */         setBlockState(worldIn, field_175825_e, 12, 0, 12, structureBoundingBoxIn);
/* 2048 */         setBlockState(worldIn, field_175825_e, 8, 3, 6, structureBoundingBoxIn);
/* 2049 */         setBlockState(worldIn, field_175825_e, 14, 3, 6, structureBoundingBoxIn);
/* 2050 */         setBlockState(worldIn, field_175826_b, 4, 2, 4, structureBoundingBoxIn);
/* 2051 */         setBlockState(worldIn, field_175825_e, 4, 1, 4, structureBoundingBoxIn);
/* 2052 */         setBlockState(worldIn, field_175826_b, 4, 0, 4, structureBoundingBoxIn);
/* 2053 */         setBlockState(worldIn, field_175826_b, 18, 2, 4, structureBoundingBoxIn);
/* 2054 */         setBlockState(worldIn, field_175825_e, 18, 1, 4, structureBoundingBoxIn);
/* 2055 */         setBlockState(worldIn, field_175826_b, 18, 0, 4, structureBoundingBoxIn);
/* 2056 */         setBlockState(worldIn, field_175826_b, 4, 2, 18, structureBoundingBoxIn);
/* 2057 */         setBlockState(worldIn, field_175825_e, 4, 1, 18, structureBoundingBoxIn);
/* 2058 */         setBlockState(worldIn, field_175826_b, 4, 0, 18, structureBoundingBoxIn);
/* 2059 */         setBlockState(worldIn, field_175826_b, 18, 2, 18, structureBoundingBoxIn);
/* 2060 */         setBlockState(worldIn, field_175825_e, 18, 1, 18, structureBoundingBoxIn);
/* 2061 */         setBlockState(worldIn, field_175826_b, 18, 0, 18, structureBoundingBoxIn);
/* 2062 */         setBlockState(worldIn, field_175826_b, 9, 7, 20, structureBoundingBoxIn);
/* 2063 */         setBlockState(worldIn, field_175826_b, 13, 7, 20, structureBoundingBoxIn);
/* 2064 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 21, 7, 4, 21, field_175826_b, field_175826_b, false);
/* 2065 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 21, 16, 4, 21, field_175826_b, field_175826_b, false);
/* 2066 */         func_175817_a(worldIn, structureBoundingBoxIn, 11, 2, 16);
/*      */       }
/* 2068 */       else if (this.field_175834_o == 1)
/*      */       {
/* 2070 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 18, 13, 3, 20, field_175826_b, field_175826_b, false);
/* 2071 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 18, 9, 2, 18, field_175826_b, field_175826_b, false);
/* 2072 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 0, 18, 13, 2, 18, field_175826_b, field_175826_b, false);
/* 2073 */         int j1 = 9;
/* 2074 */         int j = 20;
/* 2075 */         int k = 5;
/*      */         
/* 2077 */         for (int l = 0; l < 2; l++)
/*      */         {
/* 2079 */           setBlockState(worldIn, field_175826_b, j1, k + 1, j, structureBoundingBoxIn);
/* 2080 */           setBlockState(worldIn, field_175825_e, j1, k, j, structureBoundingBoxIn);
/* 2081 */           setBlockState(worldIn, field_175826_b, j1, k - 1, j, structureBoundingBoxIn);
/* 2082 */           j1 = 13;
/*      */         }
/*      */         
/* 2085 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 7, 15, 3, 14, field_175826_b, field_175826_b, false);
/* 2086 */         j1 = 10;
/*      */         
/* 2088 */         for (int k1 = 0; k1 < 2; k1++)
/*      */         {
/* 2090 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 10, j1, 6, 10, field_175826_b, field_175826_b, false);
/* 2091 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 12, j1, 6, 12, field_175826_b, field_175826_b, false);
/* 2092 */           setBlockState(worldIn, field_175825_e, j1, 0, 10, structureBoundingBoxIn);
/* 2093 */           setBlockState(worldIn, field_175825_e, j1, 0, 12, structureBoundingBoxIn);
/* 2094 */           setBlockState(worldIn, field_175825_e, j1, 4, 10, structureBoundingBoxIn);
/* 2095 */           setBlockState(worldIn, field_175825_e, j1, 4, 12, structureBoundingBoxIn);
/* 2096 */           j1 = 12;
/*      */         }
/*      */         
/* 2099 */         j1 = 8;
/*      */         
/* 2101 */         for (int l1 = 0; l1 < 2; l1++)
/*      */         {
/* 2103 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 7, j1, 2, 7, field_175826_b, field_175826_b, false);
/* 2104 */           fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 14, j1, 2, 14, field_175826_b, field_175826_b, false);
/* 2105 */           j1 = 14;
/*      */         }
/*      */         
/* 2108 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 8, 8, 3, 13, field_175827_c, field_175827_c, false);
/* 2109 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 3, 8, 14, 3, 13, field_175827_c, field_175827_c, false);
/* 2110 */         func_175817_a(worldIn, structureBoundingBoxIn, 11, 5, 13);
/*      */       }
/*      */       
/* 2113 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class XDoubleRoomFitHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/* 2125 */       return (p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d);
/*      */     }
/*      */     
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/* 2130 */       p_175968_2_.field_175963_d = true;
/* 2131 */       p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
/* 2132 */       return new StructureOceanMonumentPieces.DoubleXRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class XYDoubleRoomFitHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/* 2144 */       if ((p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d) && (p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d))
/*      */       {
/* 2146 */         StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175969_1_.field_175965_b[EnumFacing.EAST.getIndex()];
/* 2147 */         return (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.UP.getIndex()] != 0) && (!structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d);
/*      */       }
/*      */       
/*      */ 
/* 2151 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/* 2157 */       p_175968_2_.field_175963_d = true;
/* 2158 */       p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175963_d = true;
/* 2159 */       p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/* 2160 */       p_175968_2_.field_175965_b[EnumFacing.EAST.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/* 2161 */       return new StructureOceanMonumentPieces.DoubleXYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class YDoubleRoomFitHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/* 2173 */       return (p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d);
/*      */     }
/*      */     
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/* 2178 */       p_175968_2_.field_175963_d = true;
/* 2179 */       p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/* 2180 */       return new StructureOceanMonumentPieces.DoubleYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class YZDoubleRoomFitHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/* 2192 */       if ((p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d) && (p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d))
/*      */       {
/* 2194 */         StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()];
/* 2195 */         return (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.UP.getIndex()] != 0) && (!structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d);
/*      */       }
/*      */       
/*      */ 
/* 2199 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/* 2205 */       p_175968_2_.field_175963_d = true;
/* 2206 */       p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
/* 2207 */       p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/* 2208 */       p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
/* 2209 */       return new StructureOceanMonumentPieces.DoubleYZRoom(p_175968_1_, p_175968_2_, p_175968_3_);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class ZDoubleRoomFitHelper
/*      */     implements StructureOceanMonumentPieces.MonumentRoomFitHelper
/*      */   {
/*      */     public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_)
/*      */     {
/* 2221 */       return (p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] != 0) && (!p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d);
/*      */     }
/*      */     
/*      */     public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_)
/*      */     {
/* 2226 */       StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175968_2_;
/*      */       
/* 2228 */       if ((p_175968_2_.field_175966_c[EnumFacing.NORTH.getIndex()] == 0) || (p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d))
/*      */       {
/* 2230 */         structureoceanmonumentpieces$roomdefinition = p_175968_2_.field_175965_b[EnumFacing.SOUTH.getIndex()];
/*      */       }
/*      */       
/* 2233 */       structureoceanmonumentpieces$roomdefinition.field_175963_d = true;
/* 2234 */       structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
/* 2235 */       return new StructureOceanMonumentPieces.DoubleZRoom(p_175968_1_, structureoceanmonumentpieces$roomdefinition, p_175968_3_);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureOceanMonumentPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */