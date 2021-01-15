/*      */ package net.minecraft.world.gen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumFacing.Plane;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class StructureNetherBridgePieces
/*      */ {
/*   20 */   private static final PieceWeight[] primaryComponents = { new PieceWeight(Straight.class, 30, 0, true), new PieceWeight(Crossing3.class, 10, 4), new PieceWeight(Crossing.class, 10, 4), new PieceWeight(Stairs.class, 10, 3), new PieceWeight(Throne.class, 5, 2), new PieceWeight(Entrance.class, 5, 1) };
/*   21 */   private static final PieceWeight[] secondaryComponents = { new PieceWeight(Corridor5.class, 25, 0, true), new PieceWeight(Crossing2.class, 15, 5), new PieceWeight(Corridor2.class, 5, 10), new PieceWeight(Corridor.class, 5, 10), new PieceWeight(Corridor3.class, 10, 3, true), new PieceWeight(Corridor4.class, 7, 2), new PieceWeight(NetherStalkRoom.class, 5, 2) };
/*      */   
/*      */   public static void registerNetherFortressPieces()
/*      */   {
/*   25 */     MapGenStructureIO.registerStructureComponent(Crossing3.class, "NeBCr");
/*   26 */     MapGenStructureIO.registerStructureComponent(End.class, "NeBEF");
/*   27 */     MapGenStructureIO.registerStructureComponent(Straight.class, "NeBS");
/*   28 */     MapGenStructureIO.registerStructureComponent(Corridor3.class, "NeCCS");
/*   29 */     MapGenStructureIO.registerStructureComponent(Corridor4.class, "NeCTB");
/*   30 */     MapGenStructureIO.registerStructureComponent(Entrance.class, "NeCE");
/*   31 */     MapGenStructureIO.registerStructureComponent(Crossing2.class, "NeSCSC");
/*   32 */     MapGenStructureIO.registerStructureComponent(Corridor.class, "NeSCLT");
/*   33 */     MapGenStructureIO.registerStructureComponent(Corridor5.class, "NeSC");
/*   34 */     MapGenStructureIO.registerStructureComponent(Corridor2.class, "NeSCRT");
/*   35 */     MapGenStructureIO.registerStructureComponent(NetherStalkRoom.class, "NeCSR");
/*   36 */     MapGenStructureIO.registerStructureComponent(Throne.class, "NeMT");
/*   37 */     MapGenStructureIO.registerStructureComponent(Crossing.class, "NeRC");
/*   38 */     MapGenStructureIO.registerStructureComponent(Stairs.class, "NeSR");
/*   39 */     MapGenStructureIO.registerStructureComponent(Start.class, "NeStart");
/*      */   }
/*      */   
/*      */   private static Piece func_175887_b(PieceWeight p_175887_0_, List<StructureComponent> p_175887_1_, Random p_175887_2_, int p_175887_3_, int p_175887_4_, int p_175887_5_, EnumFacing p_175887_6_, int p_175887_7_)
/*      */   {
/*   44 */     Class<? extends Piece> oclass = p_175887_0_.weightClass;
/*   45 */     Piece structurenetherbridgepieces$piece = null;
/*      */     
/*   47 */     if (oclass == Straight.class)
/*      */     {
/*   49 */       structurenetherbridgepieces$piece = Straight.func_175882_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   51 */     else if (oclass == Crossing3.class)
/*      */     {
/*   53 */       structurenetherbridgepieces$piece = Crossing3.func_175885_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   55 */     else if (oclass == Crossing.class)
/*      */     {
/*   57 */       structurenetherbridgepieces$piece = Crossing.func_175873_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   59 */     else if (oclass == Stairs.class)
/*      */     {
/*   61 */       structurenetherbridgepieces$piece = Stairs.func_175872_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_7_, p_175887_6_);
/*      */     }
/*   63 */     else if (oclass == Throne.class)
/*      */     {
/*   65 */       structurenetherbridgepieces$piece = Throne.func_175874_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_7_, p_175887_6_);
/*      */     }
/*   67 */     else if (oclass == Entrance.class)
/*      */     {
/*   69 */       structurenetherbridgepieces$piece = Entrance.func_175881_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   71 */     else if (oclass == Corridor5.class)
/*      */     {
/*   73 */       structurenetherbridgepieces$piece = Corridor5.func_175877_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   75 */     else if (oclass == Corridor2.class)
/*      */     {
/*   77 */       structurenetherbridgepieces$piece = Corridor2.func_175876_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   79 */     else if (oclass == Corridor.class)
/*      */     {
/*   81 */       structurenetherbridgepieces$piece = Corridor.func_175879_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   83 */     else if (oclass == Corridor3.class)
/*      */     {
/*   85 */       structurenetherbridgepieces$piece = Corridor3.func_175883_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   87 */     else if (oclass == Corridor4.class)
/*      */     {
/*   89 */       structurenetherbridgepieces$piece = Corridor4.func_175880_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   91 */     else if (oclass == Crossing2.class)
/*      */     {
/*   93 */       structurenetherbridgepieces$piece = Crossing2.func_175878_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*   95 */     else if (oclass == NetherStalkRoom.class)
/*      */     {
/*   97 */       structurenetherbridgepieces$piece = NetherStalkRoom.func_175875_a(p_175887_1_, p_175887_2_, p_175887_3_, p_175887_4_, p_175887_5_, p_175887_6_, p_175887_7_);
/*      */     }
/*      */     
/*  100 */     return structurenetherbridgepieces$piece;
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     private boolean field_111021_b;
/*      */     
/*      */     public Corridor() {}
/*      */     
/*      */     public Corridor(int p_i45615_1_, Random p_i45615_2_, StructureBoundingBox p_i45615_3_, EnumFacing p_i45615_4_)
/*      */     {
/*  113 */       super();
/*  114 */       this.coordBaseMode = p_i45615_4_;
/*  115 */       this.boundingBox = p_i45615_3_;
/*  116 */       this.field_111021_b = (p_i45615_2_.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  121 */       super.readStructureFromNBT(tagCompound);
/*  122 */       this.field_111021_b = tagCompound.getBoolean("Chest");
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  127 */       super.writeStructureToNBT(tagCompound);
/*  128 */       tagCompound.setBoolean("Chest", this.field_111021_b);
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  133 */       getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static Corridor func_175879_a(List<StructureComponent> p_175879_0_, Random p_175879_1_, int p_175879_2_, int p_175879_3_, int p_175879_4_, EnumFacing p_175879_5_, int p_175879_6_)
/*      */     {
/*  138 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175879_2_, p_175879_3_, p_175879_4_, -1, 0, 0, 5, 7, 5, p_175879_5_);
/*  139 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175879_0_, structureboundingbox) == null) ? new Corridor(p_175879_6_, p_175879_1_, structureboundingbox, p_175879_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  144 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  145 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  146 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  147 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  148 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 3, 4, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  149 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  150 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 3, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  151 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 1, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  152 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 4, 3, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  154 */       if ((this.field_111021_b) && (structureBoundingBoxIn.isVecInside(new BlockPos(getXWithOffset(3, 3), getYWithOffset(2), getZWithOffset(3, 3)))))
/*      */       {
/*  156 */         this.field_111021_b = false;
/*  157 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, field_111019_a, 2 + randomIn.nextInt(4));
/*      */       }
/*      */       
/*  160 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  162 */       for (int i = 0; i <= 4; i++)
/*      */       {
/*  164 */         for (int j = 0; j <= 4; j++)
/*      */         {
/*  166 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  170 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor2
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     private boolean field_111020_b;
/*      */     
/*      */     public Corridor2() {}
/*      */     
/*      */     public Corridor2(int p_i45613_1_, Random p_i45613_2_, StructureBoundingBox p_i45613_3_, EnumFacing p_i45613_4_)
/*      */     {
/*  184 */       super();
/*  185 */       this.coordBaseMode = p_i45613_4_;
/*  186 */       this.boundingBox = p_i45613_3_;
/*  187 */       this.field_111020_b = (p_i45613_2_.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  192 */       super.readStructureFromNBT(tagCompound);
/*  193 */       this.field_111020_b = tagCompound.getBoolean("Chest");
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  198 */       super.writeStructureToNBT(tagCompound);
/*  199 */       tagCompound.setBoolean("Chest", this.field_111020_b);
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  204 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static Corridor2 func_175876_a(List<StructureComponent> p_175876_0_, Random p_175876_1_, int p_175876_2_, int p_175876_3_, int p_175876_4_, EnumFacing p_175876_5_, int p_175876_6_)
/*      */     {
/*  209 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175876_2_, p_175876_3_, p_175876_4_, -1, 0, 0, 5, 7, 5, p_175876_5_);
/*  210 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175876_0_, structureboundingbox) == null) ? new Corridor2(p_175876_6_, p_175876_1_, structureboundingbox, p_175876_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  215 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  216 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  217 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  218 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  219 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  220 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  221 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  222 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 1, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  223 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 4, 3, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  225 */       if ((this.field_111020_b) && (structureBoundingBoxIn.isVecInside(new BlockPos(getXWithOffset(1, 3), getYWithOffset(2), getZWithOffset(1, 3)))))
/*      */       {
/*  227 */         this.field_111020_b = false;
/*  228 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 1, 2, 3, field_111019_a, 2 + randomIn.nextInt(4));
/*      */       }
/*      */       
/*  231 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  233 */       for (int i = 0; i <= 4; i++)
/*      */       {
/*  235 */         for (int j = 0; j <= 4; j++)
/*      */         {
/*  237 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  241 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor3
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Corridor3() {}
/*      */     
/*      */     public Corridor3(int p_i45619_1_, Random p_i45619_2_, StructureBoundingBox p_i45619_3_, EnumFacing p_i45619_4_)
/*      */     {
/*  253 */       super();
/*  254 */       this.coordBaseMode = p_i45619_4_;
/*  255 */       this.boundingBox = p_i45619_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  260 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static Corridor3 func_175883_a(List<StructureComponent> p_175883_0_, Random p_175883_1_, int p_175883_2_, int p_175883_3_, int p_175883_4_, EnumFacing p_175883_5_, int p_175883_6_)
/*      */     {
/*  265 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175883_2_, p_175883_3_, p_175883_4_, -1, -7, 0, 5, 14, 10, p_175883_5_);
/*  266 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175883_0_, structureboundingbox) == null) ? new Corridor3(p_175883_6_, p_175883_1_, structureboundingbox, p_175883_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  271 */       int i = getMetadataWithOffset(Blocks.nether_brick_stairs, 2);
/*      */       
/*  273 */       for (int j = 0; j <= 9; j++)
/*      */       {
/*  275 */         int k = Math.max(1, 7 - j);
/*  276 */         int l = Math.min(Math.max(k + 5, 14 - j), 13);
/*  277 */         int i1 = j;
/*  278 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, j, 4, k, j, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  279 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, k + 1, j, 3, l - 1, j, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */         
/*  281 */         if (j <= 6)
/*      */         {
/*  283 */           setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i), 1, k + 1, j, structureBoundingBoxIn);
/*  284 */           setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i), 2, k + 1, j, structureBoundingBoxIn);
/*  285 */           setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i), 3, k + 1, j, structureBoundingBoxIn);
/*      */         }
/*      */         
/*  288 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, l, j, 4, l, j, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  289 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, k + 1, j, 0, l - 1, j, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  290 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, k + 1, j, 4, l - 1, j, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */         
/*  292 */         if ((j & 0x1) == 0)
/*      */         {
/*  294 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 0, k + 2, j, 0, k + 3, j, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  295 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 4, k + 2, j, 4, k + 3, j, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */         }
/*      */         
/*  298 */         for (int j1 = 0; j1 <= 4; j1++)
/*      */         {
/*  300 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), j1, -1, i1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  304 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor4
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Corridor4() {}
/*      */     
/*      */     public Corridor4(int p_i45618_1_, Random p_i45618_2_, StructureBoundingBox p_i45618_3_, EnumFacing p_i45618_4_)
/*      */     {
/*  316 */       super();
/*  317 */       this.coordBaseMode = p_i45618_4_;
/*  318 */       this.boundingBox = p_i45618_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  323 */       int i = 1;
/*      */       
/*  325 */       if ((this.coordBaseMode == EnumFacing.WEST) || (this.coordBaseMode == EnumFacing.NORTH))
/*      */       {
/*  327 */         i = 5;
/*      */       }
/*      */       
/*  330 */       getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
/*  331 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
/*      */     }
/*      */     
/*      */     public static Corridor4 func_175880_a(List<StructureComponent> p_175880_0_, Random p_175880_1_, int p_175880_2_, int p_175880_3_, int p_175880_4_, EnumFacing p_175880_5_, int p_175880_6_)
/*      */     {
/*  336 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175880_2_, p_175880_3_, p_175880_4_, -3, 0, 0, 9, 7, 9, p_175880_5_);
/*  337 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175880_0_, structureboundingbox) == null) ? new Corridor4(p_175880_6_, p_175880_1_, structureboundingbox, p_175880_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  342 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  343 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 8, 5, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  344 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 8, 6, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  345 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 2, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  346 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 8, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  347 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 1, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  348 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  349 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 8, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  350 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  351 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 7, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  352 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 8, 8, 3, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  353 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 6, 0, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  354 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 6, 8, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  355 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  356 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 4, 8, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  357 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  358 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 5, 7, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  359 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  360 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 5, 7, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       
/*  362 */       for (int i = 0; i <= 5; i++)
/*      */       {
/*  364 */         for (int j = 0; j <= 8; j++)
/*      */         {
/*  366 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), j, -1, i, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  370 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor5
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Corridor5() {}
/*      */     
/*      */     public Corridor5(int p_i45614_1_, Random p_i45614_2_, StructureBoundingBox p_i45614_3_, EnumFacing p_i45614_4_)
/*      */     {
/*  382 */       super();
/*  383 */       this.coordBaseMode = p_i45614_4_;
/*  384 */       this.boundingBox = p_i45614_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  389 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static Corridor5 func_175877_a(List<StructureComponent> p_175877_0_, Random p_175877_1_, int p_175877_2_, int p_175877_3_, int p_175877_4_, EnumFacing p_175877_5_, int p_175877_6_)
/*      */     {
/*  394 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175877_2_, p_175877_3_, p_175877_4_, -1, 0, 0, 5, 7, 5, p_175877_5_);
/*  395 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175877_0_, structureboundingbox) == null) ? new Corridor5(p_175877_6_, p_175877_1_, structureboundingbox, p_175877_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  400 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  401 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  402 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  403 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  404 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  405 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  406 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  407 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 3, 4, 4, 3, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  408 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  410 */       for (int i = 0; i <= 4; i++)
/*      */       {
/*  412 */         for (int j = 0; j <= 4; j++)
/*      */         {
/*  414 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  418 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Crossing
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Crossing() {}
/*      */     
/*      */     public Crossing(int p_i45610_1_, Random p_i45610_2_, StructureBoundingBox p_i45610_3_, EnumFacing p_i45610_4_)
/*      */     {
/*  430 */       super();
/*  431 */       this.coordBaseMode = p_i45610_4_;
/*  432 */       this.boundingBox = p_i45610_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  437 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 2, 0, false);
/*  438 */       getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 2, false);
/*  439 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 2, false);
/*      */     }
/*      */     
/*      */     public static Crossing func_175873_a(List<StructureComponent> p_175873_0_, Random p_175873_1_, int p_175873_2_, int p_175873_3_, int p_175873_4_, EnumFacing p_175873_5_, int p_175873_6_)
/*      */     {
/*  444 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175873_2_, p_175873_3_, p_175873_4_, -2, 0, 0, 7, 9, 7, p_175873_5_);
/*  445 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175873_0_, structureboundingbox) == null) ? new Crossing(p_175873_6_, p_175873_1_, structureboundingbox, p_175873_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  450 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  451 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 7, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  452 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  453 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 6, 1, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  454 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  455 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 6, 6, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  456 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 6, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  457 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  458 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 6, 6, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  459 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 5, 6, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  460 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 0, 4, 6, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  461 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 5, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  462 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 6, 4, 6, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  463 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 6, 4, 5, 6, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  464 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 2, 0, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  465 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 2, 0, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  466 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 2, 6, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  467 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 2, 6, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       
/*  469 */       for (int i = 0; i <= 6; i++)
/*      */       {
/*  471 */         for (int j = 0; j <= 6; j++)
/*      */         {
/*  473 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  477 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Crossing2
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Crossing2() {}
/*      */     
/*      */     public Crossing2(int p_i45616_1_, Random p_i45616_2_, StructureBoundingBox p_i45616_3_, EnumFacing p_i45616_4_)
/*      */     {
/*  489 */       super();
/*  490 */       this.coordBaseMode = p_i45616_4_;
/*  491 */       this.boundingBox = p_i45616_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  496 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 1, 0, true);
/*  497 */       getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 1, true);
/*  498 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static Crossing2 func_175878_a(List<StructureComponent> p_175878_0_, Random p_175878_1_, int p_175878_2_, int p_175878_3_, int p_175878_4_, EnumFacing p_175878_5_, int p_175878_6_)
/*      */     {
/*  503 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175878_2_, p_175878_3_, p_175878_4_, -1, 0, 0, 5, 7, 5, p_175878_5_);
/*  504 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175878_0_, structureboundingbox) == null) ? new Crossing2(p_175878_6_, p_175878_1_, structureboundingbox, p_175878_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  509 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  510 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  511 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  512 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 4, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  513 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  514 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 4, 4, 5, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  515 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 4, 6, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  517 */       for (int i = 0; i <= 4; i++)
/*      */       {
/*  519 */         for (int j = 0; j <= 4; j++)
/*      */         {
/*  521 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  525 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Crossing3
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Crossing3() {}
/*      */     
/*      */     public Crossing3(int p_i45622_1_, Random p_i45622_2_, StructureBoundingBox p_i45622_3_, EnumFacing p_i45622_4_)
/*      */     {
/*  537 */       super();
/*  538 */       this.coordBaseMode = p_i45622_4_;
/*  539 */       this.boundingBox = p_i45622_3_;
/*      */     }
/*      */     
/*      */     protected Crossing3(Random p_i2042_1_, int p_i2042_2_, int p_i2042_3_)
/*      */     {
/*  544 */       super();
/*  545 */       this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2042_1_);
/*      */       
/*  547 */       switch (this.coordBaseMode)
/*      */       {
/*      */       case NORTH: 
/*      */       case SOUTH: 
/*  551 */         this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
/*  552 */         break;
/*      */       
/*      */       default: 
/*  555 */         this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
/*      */       }
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  561 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 8, 3, false);
/*  562 */       getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 3, 8, false);
/*  563 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 3, 8, false);
/*      */     }
/*      */     
/*      */     public static Crossing3 func_175885_a(List<StructureComponent> p_175885_0_, Random p_175885_1_, int p_175885_2_, int p_175885_3_, int p_175885_4_, EnumFacing p_175885_5_, int p_175885_6_)
/*      */     {
/*  568 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175885_2_, p_175885_3_, p_175885_4_, -8, -3, 0, 19, 10, 19, p_175885_5_);
/*  569 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175885_0_, structureboundingbox) == null) ? new Crossing3(p_175885_6_, p_175885_1_, structureboundingbox, p_175885_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  574 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 11, 4, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  575 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 7, 18, 4, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  576 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 7, 18, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  577 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 8, 18, 7, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  578 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 0, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  579 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 11, 7, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  580 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 11, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  581 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 11, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  582 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  583 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 7, 18, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  584 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 11, 7, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  585 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 18, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  586 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 11, 2, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  587 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 13, 11, 2, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  588 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 0, 11, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  589 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 15, 11, 1, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  591 */       for (int i = 7; i <= 11; i++)
/*      */       {
/*  593 */         for (int j = 0; j <= 2; j++)
/*      */         {
/*  595 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*  596 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, 18 - j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  600 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 7, 5, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  601 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 2, 7, 18, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  602 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 3, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  603 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 7, 18, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  605 */       for (int k = 0; k <= 2; k++)
/*      */       {
/*  607 */         for (int l = 7; l <= 11; l++)
/*      */         {
/*  609 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), k, -1, l, structureBoundingBoxIn);
/*  610 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), 18 - k, -1, l, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  614 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class End
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     private int fillSeed;
/*      */     
/*      */     public End() {}
/*      */     
/*      */     public End(int p_i45621_1_, Random p_i45621_2_, StructureBoundingBox p_i45621_3_, EnumFacing p_i45621_4_)
/*      */     {
/*  628 */       super();
/*  629 */       this.coordBaseMode = p_i45621_4_;
/*  630 */       this.boundingBox = p_i45621_3_;
/*  631 */       this.fillSeed = p_i45621_2_.nextInt();
/*      */     }
/*      */     
/*      */     public static End func_175884_a(List<StructureComponent> p_175884_0_, Random p_175884_1_, int p_175884_2_, int p_175884_3_, int p_175884_4_, EnumFacing p_175884_5_, int p_175884_6_)
/*      */     {
/*  636 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175884_2_, p_175884_3_, p_175884_4_, -1, -3, 0, 5, 10, 8, p_175884_5_);
/*  637 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175884_0_, structureboundingbox) == null) ? new End(p_175884_6_, p_175884_1_, structureboundingbox, p_175884_5_) : null;
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  642 */       super.readStructureFromNBT(tagCompound);
/*  643 */       this.fillSeed = tagCompound.getInteger("Seed");
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  648 */       super.writeStructureToNBT(tagCompound);
/*  649 */       tagCompound.setInteger("Seed", this.fillSeed);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  654 */       Random random = new Random(this.fillSeed);
/*      */       
/*  656 */       for (int i = 0; i <= 4; i++)
/*      */       {
/*  658 */         for (int j = 3; j <= 4; j++)
/*      */         {
/*  660 */           int k = random.nextInt(8);
/*  661 */           fillWithBlocks(worldIn, structureBoundingBoxIn, i, j, 0, i, j, k, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */         }
/*      */       }
/*      */       
/*  665 */       int l = random.nextInt(8);
/*  666 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 5, l, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  667 */       l = random.nextInt(8);
/*  668 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 0, 4, 5, l, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  670 */       for (l = 0; l <= 4; l++)
/*      */       {
/*  672 */         int i1 = random.nextInt(5);
/*  673 */         fillWithBlocks(worldIn, structureBoundingBoxIn, l, 2, 0, l, 2, i1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       }
/*      */       
/*  676 */       for (l = 0; l <= 4; l++)
/*      */       {
/*  678 */         for (int j1 = 0; j1 <= 1; j1++)
/*      */         {
/*  680 */           int k1 = random.nextInt(3);
/*  681 */           fillWithBlocks(worldIn, structureBoundingBoxIn, l, j1, 0, l, j1, k1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */         }
/*      */       }
/*      */       
/*  685 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Entrance
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Entrance() {}
/*      */     
/*      */     public Entrance(int p_i45617_1_, Random p_i45617_2_, StructureBoundingBox p_i45617_3_, EnumFacing p_i45617_4_)
/*      */     {
/*  697 */       super();
/*  698 */       this.coordBaseMode = p_i45617_4_;
/*  699 */       this.boundingBox = p_i45617_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  704 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 5, 3, true);
/*      */     }
/*      */     
/*      */     public static Entrance func_175881_a(List<StructureComponent> p_175881_0_, Random p_175881_1_, int p_175881_2_, int p_175881_3_, int p_175881_4_, EnumFacing p_175881_5_, int p_175881_6_)
/*      */     {
/*  709 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175881_2_, p_175881_3_, p_175881_4_, -5, -3, 0, 13, 14, 13, p_175881_5_);
/*  710 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175881_0_, structureboundingbox) == null) ? new Entrance(p_175881_6_, p_175881_1_, structureboundingbox, p_175881_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  715 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  716 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  717 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  718 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  719 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  720 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 11, 10, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  721 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  722 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  723 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  724 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  725 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 11, 2, 10, 12, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  726 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 8, 0, 7, 8, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       
/*  728 */       for (int i = 1; i <= 11; i += 2)
/*      */       {
/*  730 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 0, i, 11, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  731 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 12, i, 11, 12, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  732 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 10, i, 0, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  733 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 10, i, 12, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  734 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 0, structureBoundingBoxIn);
/*  735 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 12, structureBoundingBoxIn);
/*  736 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 0, 13, i, structureBoundingBoxIn);
/*  737 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 12, 13, i, structureBoundingBoxIn);
/*  738 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 0, structureBoundingBoxIn);
/*  739 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 12, structureBoundingBoxIn);
/*  740 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, i + 1, structureBoundingBoxIn);
/*  741 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, i + 1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  744 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
/*  745 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureBoundingBoxIn);
/*  746 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
/*  747 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureBoundingBoxIn);
/*      */       
/*  749 */       for (int k = 3; k <= 9; k += 2)
/*      */       {
/*  751 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, k, 1, 8, k, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  752 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 7, k, 11, 8, k, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       }
/*      */       
/*  755 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  756 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  757 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  758 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  759 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  760 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  762 */       for (int l = 4; l <= 8; l++)
/*      */       {
/*  764 */         for (int j = 0; j <= 2; j++)
/*      */         {
/*  766 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), l, -1, j, structureBoundingBoxIn);
/*  767 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), l, -1, 12 - j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  771 */       for (int i1 = 0; i1 <= 2; i1++)
/*      */       {
/*  773 */         for (int j1 = 4; j1 <= 8; j1++)
/*      */         {
/*  775 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i1, -1, j1, structureBoundingBoxIn);
/*  776 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), 12 - i1, -1, j1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  780 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  781 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 6, 6, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  782 */       setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 6, 0, 6, structureBoundingBoxIn);
/*  783 */       setBlockState(worldIn, Blocks.flowing_lava.getDefaultState(), 6, 5, 6, structureBoundingBoxIn);
/*  784 */       BlockPos blockpos = new BlockPos(getXWithOffset(6, 6), getYWithOffset(5), getZWithOffset(6, 6));
/*      */       
/*  786 */       if (structureBoundingBoxIn.isVecInside(blockpos))
/*      */       {
/*  788 */         worldIn.forceBlockUpdateTick(Blocks.flowing_lava, blockpos, randomIn);
/*      */       }
/*      */       
/*  791 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class NetherStalkRoom
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public NetherStalkRoom() {}
/*      */     
/*      */     public NetherStalkRoom(int p_i45612_1_, Random p_i45612_2_, StructureBoundingBox p_i45612_3_, EnumFacing p_i45612_4_)
/*      */     {
/*  803 */       super();
/*  804 */       this.coordBaseMode = p_i45612_4_;
/*  805 */       this.boundingBox = p_i45612_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  810 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 5, 3, true);
/*  811 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 5, 11, true);
/*      */     }
/*      */     
/*      */     public static NetherStalkRoom func_175875_a(List<StructureComponent> p_175875_0_, Random p_175875_1_, int p_175875_2_, int p_175875_3_, int p_175875_4_, EnumFacing p_175875_5_, int p_175875_6_)
/*      */     {
/*  816 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175875_2_, p_175875_3_, p_175875_4_, -5, -3, 0, 13, 14, 13, p_175875_5_);
/*  817 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175875_0_, structureboundingbox) == null) ? new NetherStalkRoom(p_175875_6_, p_175875_1_, structureboundingbox, p_175875_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  822 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  823 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  824 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  825 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  826 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  827 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 11, 10, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  828 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  829 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  830 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  831 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  832 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 11, 2, 10, 12, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  834 */       for (int i = 1; i <= 11; i += 2)
/*      */       {
/*  836 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 0, i, 11, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  837 */         fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 12, i, 11, 12, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  838 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 10, i, 0, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  839 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 10, i, 12, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  840 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 0, structureBoundingBoxIn);
/*  841 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 12, structureBoundingBoxIn);
/*  842 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 0, 13, i, structureBoundingBoxIn);
/*  843 */         setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 12, 13, i, structureBoundingBoxIn);
/*  844 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 0, structureBoundingBoxIn);
/*  845 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 12, structureBoundingBoxIn);
/*  846 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, i + 1, structureBoundingBoxIn);
/*  847 */         setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, i + 1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  850 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
/*  851 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureBoundingBoxIn);
/*  852 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
/*  853 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureBoundingBoxIn);
/*      */       
/*  855 */       for (int j1 = 3; j1 <= 9; j1 += 2)
/*      */       {
/*  857 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, j1, 1, 8, j1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  858 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 7, j1, 11, 8, j1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       }
/*      */       
/*  861 */       int k1 = getMetadataWithOffset(Blocks.nether_brick_stairs, 3);
/*      */       
/*  863 */       for (int j = 0; j <= 6; j++)
/*      */       {
/*  865 */         int k = j + 4;
/*      */         
/*  867 */         for (int l = 5; l <= 7; l++)
/*      */         {
/*  869 */           setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(k1), l, 5 + j, k, structureBoundingBoxIn);
/*      */         }
/*      */         
/*  872 */         if ((k >= 5) && (k <= 8))
/*      */         {
/*  874 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, k, 7, j + 4, k, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */         }
/*  876 */         else if ((k >= 9) && (k <= 10))
/*      */         {
/*  878 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 8, k, 7, j + 4, k, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */         }
/*      */         
/*  881 */         if (j >= 1)
/*      */         {
/*  883 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6 + j, k, 7, 9 + j, k, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */         }
/*      */       }
/*      */       
/*  887 */       for (int l1 = 5; l1 <= 7; l1++)
/*      */       {
/*  889 */         setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(k1), l1, 12, 11, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  892 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 6, 7, 5, 7, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  893 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 6, 7, 7, 7, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*  894 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 13, 12, 7, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  895 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 2, 3, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  896 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 9, 3, 5, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  897 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 4, 2, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  898 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 2, 10, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  899 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 9, 10, 5, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  900 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 4, 10, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  901 */       int i2 = getMetadataWithOffset(Blocks.nether_brick_stairs, 0);
/*  902 */       int j2 = getMetadataWithOffset(Blocks.nether_brick_stairs, 1);
/*  903 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 2, structureBoundingBoxIn);
/*  904 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 3, structureBoundingBoxIn);
/*  905 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 9, structureBoundingBoxIn);
/*  906 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(j2), 4, 5, 10, structureBoundingBoxIn);
/*  907 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 2, structureBoundingBoxIn);
/*  908 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 3, structureBoundingBoxIn);
/*  909 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 9, structureBoundingBoxIn);
/*  910 */       setBlockState(worldIn, Blocks.nether_brick_stairs.getStateFromMeta(i2), 8, 5, 10, structureBoundingBoxIn);
/*  911 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 4, 4, 4, 8, Blocks.soul_sand.getDefaultState(), Blocks.soul_sand.getDefaultState(), false);
/*  912 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 4, 9, 4, 8, Blocks.soul_sand.getDefaultState(), Blocks.soul_sand.getDefaultState(), false);
/*  913 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 4, 4, 5, 8, Blocks.nether_wart.getDefaultState(), Blocks.nether_wart.getDefaultState(), false);
/*  914 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 4, 9, 5, 8, Blocks.nether_wart.getDefaultState(), Blocks.nether_wart.getDefaultState(), false);
/*  915 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  916 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  917 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  918 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  919 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*  920 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/*  922 */       for (int k2 = 4; k2 <= 8; k2++)
/*      */       {
/*  924 */         for (int i1 = 0; i1 <= 2; i1++)
/*      */         {
/*  926 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), k2, -1, i1, structureBoundingBoxIn);
/*  927 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), k2, -1, 12 - i1, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  931 */       for (int l2 = 0; l2 <= 2; l2++)
/*      */       {
/*  933 */         for (int i3 = 4; i3 <= 8; i3++)
/*      */         {
/*  935 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), l2, -1, i3, structureBoundingBoxIn);
/*  936 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), 12 - l2, -1, i3, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/*  940 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Piece extends StructureComponent
/*      */   {
/*  946 */     protected static final List<WeightedRandomChestContent> field_111019_a = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 5), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 15), new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 5), new WeightedRandomChestContent(Items.nether_wart, 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 8), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 3), new WeightedRandomChestContent(net.minecraft.item.Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2) });
/*      */     
/*      */ 
/*      */     public Piece() {}
/*      */     
/*      */ 
/*      */     protected Piece(int p_i2054_1_)
/*      */     {
/*  954 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {}
/*      */     
/*      */ 
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {}
/*      */     
/*      */ 
/*      */     private int getTotalWeight(List<StructureNetherBridgePieces.PieceWeight> p_74960_1_)
/*      */     {
/*  967 */       boolean flag = false;
/*  968 */       int i = 0;
/*      */       
/*  970 */       for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : p_74960_1_)
/*      */       {
/*  972 */         if ((structurenetherbridgepieces$pieceweight.field_78824_d > 0) && (structurenetherbridgepieces$pieceweight.field_78827_c < structurenetherbridgepieces$pieceweight.field_78824_d))
/*      */         {
/*  974 */           flag = true;
/*      */         }
/*      */         
/*  977 */         i += structurenetherbridgepieces$pieceweight.field_78826_b;
/*      */       }
/*      */       
/*  980 */       return flag ? i : -1;
/*      */     }
/*      */     
/*      */     private Piece func_175871_a(StructureNetherBridgePieces.Start p_175871_1_, List<StructureNetherBridgePieces.PieceWeight> p_175871_2_, List<StructureComponent> p_175871_3_, Random p_175871_4_, int p_175871_5_, int p_175871_6_, int p_175871_7_, EnumFacing p_175871_8_, int p_175871_9_)
/*      */     {
/*  985 */       int i = getTotalWeight(p_175871_2_);
/*  986 */       boolean flag = (i > 0) && (p_175871_9_ <= 30);
/*  987 */       int j = 0;
/*      */       Iterator localIterator;
/*  989 */       label184: for (; (j < 5) && (flag); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  994 */           localIterator.hasNext())
/*      */       {
/*  991 */         j++;
/*  992 */         int k = p_175871_4_.nextInt(i);
/*      */         
/*  994 */         localIterator = p_175871_2_.iterator(); continue;StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight = (StructureNetherBridgePieces.PieceWeight)localIterator.next();
/*      */         
/*  996 */         k -= structurenetherbridgepieces$pieceweight.field_78826_b;
/*      */         
/*  998 */         if (k < 0)
/*      */         {
/* 1000 */           if ((!structurenetherbridgepieces$pieceweight.func_78822_a(p_175871_9_)) || ((structurenetherbridgepieces$pieceweight == p_175871_1_.theNetherBridgePieceWeight) && (!structurenetherbridgepieces$pieceweight.field_78825_e))) {
/*      */             break label184;
/*      */           }
/*      */           
/*      */ 
/* 1005 */           Piece structurenetherbridgepieces$piece = StructureNetherBridgePieces.func_175887_b(structurenetherbridgepieces$pieceweight, p_175871_3_, p_175871_4_, p_175871_5_, p_175871_6_, p_175871_7_, p_175871_8_, p_175871_9_);
/*      */           
/* 1007 */           if (structurenetherbridgepieces$piece != null)
/*      */           {
/* 1009 */             structurenetherbridgepieces$pieceweight.field_78827_c += 1;
/* 1010 */             p_175871_1_.theNetherBridgePieceWeight = structurenetherbridgepieces$pieceweight;
/*      */             
/* 1012 */             if (!structurenetherbridgepieces$pieceweight.func_78823_a())
/*      */             {
/* 1014 */               p_175871_2_.remove(structurenetherbridgepieces$pieceweight);
/*      */             }
/*      */             
/* 1017 */             return structurenetherbridgepieces$piece;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1023 */       return StructureNetherBridgePieces.End.func_175884_a(p_175871_3_, p_175871_4_, p_175871_5_, p_175871_6_, p_175871_7_, p_175871_8_, p_175871_9_);
/*      */     }
/*      */     
/*      */     private StructureComponent func_175870_a(StructureNetherBridgePieces.Start p_175870_1_, List<StructureComponent> p_175870_2_, Random p_175870_3_, int p_175870_4_, int p_175870_5_, int p_175870_6_, EnumFacing p_175870_7_, int p_175870_8_, boolean p_175870_9_)
/*      */     {
/* 1028 */       if ((Math.abs(p_175870_4_ - p_175870_1_.getBoundingBox().minX) <= 112) && (Math.abs(p_175870_6_ - p_175870_1_.getBoundingBox().minZ) <= 112))
/*      */       {
/* 1030 */         List<StructureNetherBridgePieces.PieceWeight> list = p_175870_1_.primaryWeights;
/*      */         
/* 1032 */         if (p_175870_9_)
/*      */         {
/* 1034 */           list = p_175870_1_.secondaryWeights;
/*      */         }
/*      */         
/* 1037 */         StructureComponent structurecomponent = func_175871_a(p_175870_1_, list, p_175870_2_, p_175870_3_, p_175870_4_, p_175870_5_, p_175870_6_, p_175870_7_, p_175870_8_ + 1);
/*      */         
/* 1039 */         if (structurecomponent != null)
/*      */         {
/* 1041 */           p_175870_2_.add(structurecomponent);
/* 1042 */           p_175870_1_.field_74967_d.add(structurecomponent);
/*      */         }
/*      */         
/* 1045 */         return structurecomponent;
/*      */       }
/*      */       
/*      */ 
/* 1049 */       return StructureNetherBridgePieces.End.func_175884_a(p_175870_2_, p_175870_3_, p_175870_4_, p_175870_5_, p_175870_6_, p_175870_7_, p_175870_8_);
/*      */     }
/*      */     
/*      */ 
/*      */     protected StructureComponent getNextComponentNormal(StructureNetherBridgePieces.Start p_74963_1_, List<StructureComponent> p_74963_2_, Random p_74963_3_, int p_74963_4_, int p_74963_5_, boolean p_74963_6_)
/*      */     {
/* 1055 */       if (this.coordBaseMode != null)
/*      */       {
/* 1057 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1060 */           return func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ - 1, this.coordBaseMode, getComponentType(), p_74963_6_);
/*      */         
/*      */         case SOUTH: 
/* 1063 */           return func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX + p_74963_4_, this.boundingBox.minY + p_74963_5_, this.boundingBox.maxZ + 1, this.coordBaseMode, getComponentType(), p_74963_6_);
/*      */         
/*      */         case UP: 
/* 1066 */           return func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, this.coordBaseMode, getComponentType(), p_74963_6_);
/*      */         
/*      */         case WEST: 
/* 1069 */           return func_175870_a(p_74963_1_, p_74963_2_, p_74963_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74963_5_, this.boundingBox.minZ + p_74963_4_, this.coordBaseMode, getComponentType(), p_74963_6_);
/*      */         }
/*      */         
/*      */       }
/* 1073 */       return null;
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentX(StructureNetherBridgePieces.Start p_74961_1_, List<StructureComponent> p_74961_2_, Random p_74961_3_, int p_74961_4_, int p_74961_5_, boolean p_74961_6_)
/*      */     {
/* 1078 */       if (this.coordBaseMode != null)
/*      */       {
/* 1080 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1083 */           return func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, getComponentType(), p_74961_6_);
/*      */         
/*      */         case SOUTH: 
/* 1086 */           return func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ + p_74961_5_, EnumFacing.WEST, getComponentType(), p_74961_6_);
/*      */         
/*      */         case UP: 
/* 1089 */           return func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType(), p_74961_6_);
/*      */         
/*      */         case WEST: 
/* 1092 */           return func_175870_a(p_74961_1_, p_74961_2_, p_74961_3_, this.boundingBox.minX + p_74961_5_, this.boundingBox.minY + p_74961_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType(), p_74961_6_);
/*      */         }
/*      */         
/*      */       }
/* 1096 */       return null;
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentZ(StructureNetherBridgePieces.Start p_74965_1_, List<StructureComponent> p_74965_2_, Random p_74965_3_, int p_74965_4_, int p_74965_5_, boolean p_74965_6_)
/*      */     {
/* 1101 */       if (this.coordBaseMode != null)
/*      */       {
/* 1103 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1106 */           return func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, getComponentType(), p_74965_6_);
/*      */         
/*      */         case SOUTH: 
/* 1109 */           return func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74965_4_, this.boundingBox.minZ + p_74965_5_, EnumFacing.EAST, getComponentType(), p_74965_6_);
/*      */         
/*      */         case UP: 
/* 1112 */           return func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType(), p_74965_6_);
/*      */         
/*      */         case WEST: 
/* 1115 */           return func_175870_a(p_74965_1_, p_74965_2_, p_74965_3_, this.boundingBox.minX + p_74965_5_, this.boundingBox.minY + p_74965_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType(), p_74965_6_);
/*      */         }
/*      */         
/*      */       }
/* 1119 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean isAboveGround(StructureBoundingBox p_74964_0_)
/*      */     {
/* 1124 */       return (p_74964_0_ != null) && (p_74964_0_.minY > 10);
/*      */     }
/*      */   }
/*      */   
/*      */   static class PieceWeight
/*      */   {
/*      */     public Class<? extends StructureNetherBridgePieces.Piece> weightClass;
/*      */     public final int field_78826_b;
/*      */     public int field_78827_c;
/*      */     public int field_78824_d;
/*      */     public boolean field_78825_e;
/*      */     
/*      */     public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> p_i2055_1_, int p_i2055_2_, int p_i2055_3_, boolean p_i2055_4_)
/*      */     {
/* 1138 */       this.weightClass = p_i2055_1_;
/* 1139 */       this.field_78826_b = p_i2055_2_;
/* 1140 */       this.field_78824_d = p_i2055_3_;
/* 1141 */       this.field_78825_e = p_i2055_4_;
/*      */     }
/*      */     
/*      */     public PieceWeight(Class<? extends StructureNetherBridgePieces.Piece> p_i2056_1_, int p_i2056_2_, int p_i2056_3_)
/*      */     {
/* 1146 */       this(p_i2056_1_, p_i2056_2_, p_i2056_3_, false);
/*      */     }
/*      */     
/*      */     public boolean func_78822_a(int p_78822_1_)
/*      */     {
/* 1151 */       return (this.field_78824_d == 0) || (this.field_78827_c < this.field_78824_d);
/*      */     }
/*      */     
/*      */     public boolean func_78823_a()
/*      */     {
/* 1156 */       return (this.field_78824_d == 0) || (this.field_78827_c < this.field_78824_d);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Stairs
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Stairs() {}
/*      */     
/*      */     public Stairs(int p_i45609_1_, Random p_i45609_2_, StructureBoundingBox p_i45609_3_, EnumFacing p_i45609_4_)
/*      */     {
/* 1168 */       super();
/* 1169 */       this.coordBaseMode = p_i45609_4_;
/* 1170 */       this.boundingBox = p_i45609_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1175 */       getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 6, 2, false);
/*      */     }
/*      */     
/*      */     public static Stairs func_175872_a(List<StructureComponent> p_175872_0_, Random p_175872_1_, int p_175872_2_, int p_175872_3_, int p_175872_4_, int p_175872_5_, EnumFacing p_175872_6_)
/*      */     {
/* 1180 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175872_2_, p_175872_3_, p_175872_4_, -2, 0, 0, 7, 11, 7, p_175872_6_);
/* 1181 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175872_0_, structureboundingbox) == null) ? new Stairs(p_175872_5_, p_175872_1_, structureboundingbox, p_175872_6_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1186 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 6, 1, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1187 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 10, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1188 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 1, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1189 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1190 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1191 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 1, 6, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1192 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 6, 5, 8, 6, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1193 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 2, 0, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1194 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 2, 6, 5, 2, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1195 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 4, 6, 5, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1196 */       setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/* 1197 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 5, 4, 3, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1198 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 5, 3, 4, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1199 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1200 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 1, 6, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1201 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 1, 5, 7, 4, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1202 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 8, 2, 6, 8, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1203 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 6, 0, 4, 8, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1204 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 5, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       
/* 1206 */       for (int i = 0; i <= 6; i++)
/*      */       {
/* 1208 */         for (int j = 0; j <= 6; j++)
/*      */         {
/* 1210 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1214 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Start extends StructureNetherBridgePieces.Crossing3
/*      */   {
/*      */     public StructureNetherBridgePieces.PieceWeight theNetherBridgePieceWeight;
/*      */     public List<StructureNetherBridgePieces.PieceWeight> primaryWeights;
/*      */     public List<StructureNetherBridgePieces.PieceWeight> secondaryWeights;
/* 1223 */     public List<StructureComponent> field_74967_d = Lists.newArrayList();
/*      */     
/*      */ 
/*      */     public Start() {}
/*      */     
/*      */ 
/*      */     public Start(Random p_i2059_1_, int p_i2059_2_, int p_i2059_3_)
/*      */     {
/* 1231 */       super(p_i2059_2_, p_i2059_3_);
/* 1232 */       this.primaryWeights = Lists.newArrayList();
/*      */       StructureNetherBridgePieces.PieceWeight[] arrayOfPieceWeight;
/* 1234 */       int j = (arrayOfPieceWeight = StructureNetherBridgePieces.primaryComponents).length; for (int i = 0; i < j; i++) { StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight = arrayOfPieceWeight[i];
/*      */         
/* 1236 */         structurenetherbridgepieces$pieceweight.field_78827_c = 0;
/* 1237 */         this.primaryWeights.add(structurenetherbridgepieces$pieceweight);
/*      */       }
/*      */       
/* 1240 */       this.secondaryWeights = Lists.newArrayList();
/*      */       
/* 1242 */       j = (arrayOfPieceWeight = StructureNetherBridgePieces.secondaryComponents).length; for (i = 0; i < j; i++) { StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight1 = arrayOfPieceWeight[i];
/*      */         
/* 1244 */         structurenetherbridgepieces$pieceweight1.field_78827_c = 0;
/* 1245 */         this.secondaryWeights.add(structurenetherbridgepieces$pieceweight1);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1251 */       super.readStructureFromNBT(tagCompound);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1256 */       super.writeStructureToNBT(tagCompound);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Straight
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     public Straight() {}
/*      */     
/*      */     public Straight(int p_i45620_1_, Random p_i45620_2_, StructureBoundingBox p_i45620_3_, EnumFacing p_i45620_4_)
/*      */     {
/* 1268 */       super();
/* 1269 */       this.coordBaseMode = p_i45620_4_;
/* 1270 */       this.boundingBox = p_i45620_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1275 */       getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 1, 3, false);
/*      */     }
/*      */     
/*      */     public static Straight func_175882_a(List<StructureComponent> p_175882_0_, Random p_175882_1_, int p_175882_2_, int p_175882_3_, int p_175882_4_, EnumFacing p_175882_5_, int p_175882_6_)
/*      */     {
/* 1280 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175882_2_, p_175882_3_, p_175882_4_, -1, -3, 0, 5, 10, 19, p_175882_5_);
/* 1281 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175882_0_, structureboundingbox) == null) ? new Straight(p_175882_6_, p_175882_1_, structureboundingbox, p_175882_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1286 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 4, 4, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1287 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 0, 3, 7, 18, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1288 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 0, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1289 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 0, 4, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1290 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 4, 2, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1291 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 13, 4, 2, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1292 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1293 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 15, 4, 1, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/*      */       
/* 1295 */       for (int i = 0; i <= 4; i++)
/*      */       {
/* 1297 */         for (int j = 0; j <= 2; j++)
/*      */         {
/* 1299 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/* 1300 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, 18 - j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1304 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1305 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1306 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 14, 0, 4, 14, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1307 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 17, 0, 4, 17, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1308 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 4, 1, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1309 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 4, 4, 4, 4, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1310 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 14, 4, 4, 14, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1311 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 17, 4, 4, 17, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1312 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Throne
/*      */     extends StructureNetherBridgePieces.Piece
/*      */   {
/*      */     private boolean hasSpawner;
/*      */     
/*      */     public Throne() {}
/*      */     
/*      */     public Throne(int p_i45611_1_, Random p_i45611_2_, StructureBoundingBox p_i45611_3_, EnumFacing p_i45611_4_)
/*      */     {
/* 1326 */       super();
/* 1327 */       this.coordBaseMode = p_i45611_4_;
/* 1328 */       this.boundingBox = p_i45611_3_;
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1333 */       super.readStructureFromNBT(tagCompound);
/* 1334 */       this.hasSpawner = tagCompound.getBoolean("Mob");
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1339 */       super.writeStructureToNBT(tagCompound);
/* 1340 */       tagCompound.setBoolean("Mob", this.hasSpawner);
/*      */     }
/*      */     
/*      */     public static Throne func_175874_a(List<StructureComponent> p_175874_0_, Random p_175874_1_, int p_175874_2_, int p_175874_3_, int p_175874_4_, int p_175874_5_, EnumFacing p_175874_6_)
/*      */     {
/* 1345 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175874_2_, p_175874_3_, p_175874_4_, -2, 0, 0, 7, 8, 9, p_175874_6_);
/* 1346 */       return (isAboveGround(structureboundingbox)) && (StructureComponent.findIntersecting(p_175874_0_, structureboundingbox) == null) ? new Throne(p_175874_5_, p_175874_1_, structureboundingbox, p_175874_6_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1351 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 6, 7, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1352 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 1, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1353 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 5, 2, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1354 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 5, 3, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1355 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 5, 4, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1356 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 1, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1357 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 5, 4, 2, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1358 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 1, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1359 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 5, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1360 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1361 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 3, 6, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1362 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 8, 5, 5, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
/* 1363 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 1, 6, 3, structureBoundingBoxIn);
/* 1364 */       setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 5, 6, 3, structureBoundingBoxIn);
/* 1365 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 3, 0, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1366 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 3, 6, 6, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1367 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 8, 5, 7, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/* 1368 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 8, 8, 4, 8, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
/*      */       
/* 1370 */       if (!this.hasSpawner)
/*      */       {
/* 1372 */         BlockPos blockpos = new BlockPos(getXWithOffset(3, 5), getYWithOffset(5), getZWithOffset(3, 5));
/*      */         
/* 1374 */         if (structureBoundingBoxIn.isVecInside(blockpos))
/*      */         {
/* 1376 */           this.hasSpawner = true;
/* 1377 */           worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
/* 1378 */           net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*      */           
/* 1380 */           if ((tileentity instanceof TileEntityMobSpawner))
/*      */           {
/* 1382 */             ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Blaze");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1387 */       for (int i = 0; i <= 6; i++)
/*      */       {
/* 1389 */         for (int j = 0; j <= 6; j++)
/*      */         {
/* 1391 */           replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1395 */       return true;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureNetherBridgePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */