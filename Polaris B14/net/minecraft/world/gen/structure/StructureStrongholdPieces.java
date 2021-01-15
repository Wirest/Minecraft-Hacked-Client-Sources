/*      */ package net.minecraft.world.gen.structure;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockDynamicLiquid;
/*      */ import net.minecraft.block.BlockEndPortalFrame;
/*      */ import net.minecraft.block.BlockSlab;
/*      */ import net.minecraft.block.BlockStoneSlab.EnumType;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.ItemEnchantedBook;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class StructureStrongholdPieces
/*      */ {
/*   23 */   private static final PieceWeight[] pieceWeightArray = { new PieceWeight(Straight.class, 40, 0), new PieceWeight(Prison.class, 5, 5), new PieceWeight(LeftTurn.class, 20, 0), new PieceWeight(RightTurn.class, 20, 0), new PieceWeight(RoomCrossing.class, 10, 6), new PieceWeight(StairsStraight.class, 5, 5), new PieceWeight(Stairs.class, 5, 5), new PieceWeight(Crossing.class, 5, 4), new PieceWeight(ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2)
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   29 */     new PieceWeight
/*      */   {
/*      */     public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
/*      */     {
/*   27 */       return (super.canSpawnMoreStructuresOfType(p_75189_1_)) && (p_75189_1_ > 4);
/*      */     }
/*   23 */   }, 
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   29 */     new PieceWeight(PortalRoom.class, 20, 1)
/*      */   {
/*      */     public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
/*      */     {
/*   33 */       return (super.canSpawnMoreStructuresOfType(p_75189_1_)) && (p_75189_1_ > 5);
/*      */     }
/*   23 */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   29 */      };
/*      */   
/*      */ 
/*      */   private static List<PieceWeight> structurePieceList;
/*      */   
/*      */ 
/*      */   private static Class<? extends Stronghold> strongComponentType;
/*      */   
/*      */ 
/*      */   static int totalWeight;
/*      */   
/*   40 */   private static final Stones strongholdStones = new Stones(null);
/*      */   
/*      */   public static void registerStrongholdPieces()
/*      */   {
/*   44 */     MapGenStructureIO.registerStructureComponent(ChestCorridor.class, "SHCC");
/*   45 */     MapGenStructureIO.registerStructureComponent(Corridor.class, "SHFC");
/*   46 */     MapGenStructureIO.registerStructureComponent(Crossing.class, "SH5C");
/*   47 */     MapGenStructureIO.registerStructureComponent(LeftTurn.class, "SHLT");
/*   48 */     MapGenStructureIO.registerStructureComponent(Library.class, "SHLi");
/*   49 */     MapGenStructureIO.registerStructureComponent(PortalRoom.class, "SHPR");
/*   50 */     MapGenStructureIO.registerStructureComponent(Prison.class, "SHPH");
/*   51 */     MapGenStructureIO.registerStructureComponent(RightTurn.class, "SHRT");
/*   52 */     MapGenStructureIO.registerStructureComponent(RoomCrossing.class, "SHRC");
/*   53 */     MapGenStructureIO.registerStructureComponent(Stairs.class, "SHSD");
/*   54 */     MapGenStructureIO.registerStructureComponent(Stairs2.class, "SHStart");
/*   55 */     MapGenStructureIO.registerStructureComponent(Straight.class, "SHS");
/*   56 */     MapGenStructureIO.registerStructureComponent(StairsStraight.class, "SHSSD");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void prepareStructurePieces()
/*      */   {
/*   64 */     structurePieceList = Lists.newArrayList();
/*      */     PieceWeight[] arrayOfPieceWeight;
/*   66 */     int j = (arrayOfPieceWeight = pieceWeightArray).length; for (int i = 0; i < j; i++) { PieceWeight structurestrongholdpieces$pieceweight = arrayOfPieceWeight[i];
/*      */       
/*   68 */       structurestrongholdpieces$pieceweight.instancesSpawned = 0;
/*   69 */       structurePieceList.add(structurestrongholdpieces$pieceweight);
/*      */     }
/*      */     
/*   72 */     strongComponentType = null;
/*      */   }
/*      */   
/*      */   private static boolean canAddStructurePieces()
/*      */   {
/*   77 */     boolean flag = false;
/*   78 */     totalWeight = 0;
/*      */     
/*   80 */     for (PieceWeight structurestrongholdpieces$pieceweight : structurePieceList)
/*      */     {
/*   82 */       if ((structurestrongholdpieces$pieceweight.instancesLimit > 0) && (structurestrongholdpieces$pieceweight.instancesSpawned < structurestrongholdpieces$pieceweight.instancesLimit))
/*      */       {
/*   84 */         flag = true;
/*      */       }
/*      */       
/*   87 */       totalWeight += structurestrongholdpieces$pieceweight.pieceWeight;
/*      */     }
/*      */     
/*   90 */     return flag;
/*      */   }
/*      */   
/*      */   private static Stronghold func_175954_a(Class<? extends Stronghold> p_175954_0_, List<StructureComponent> p_175954_1_, Random p_175954_2_, int p_175954_3_, int p_175954_4_, int p_175954_5_, EnumFacing p_175954_6_, int p_175954_7_)
/*      */   {
/*   95 */     Stronghold structurestrongholdpieces$stronghold = null;
/*      */     
/*   97 */     if (p_175954_0_ == Straight.class)
/*      */     {
/*   99 */       structurestrongholdpieces$stronghold = Straight.func_175862_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  101 */     else if (p_175954_0_ == Prison.class)
/*      */     {
/*  103 */       structurestrongholdpieces$stronghold = Prison.func_175860_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  105 */     else if (p_175954_0_ == LeftTurn.class)
/*      */     {
/*  107 */       structurestrongholdpieces$stronghold = LeftTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  109 */     else if (p_175954_0_ == RightTurn.class)
/*      */     {
/*  111 */       structurestrongholdpieces$stronghold = RightTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  113 */     else if (p_175954_0_ == RoomCrossing.class)
/*      */     {
/*  115 */       structurestrongholdpieces$stronghold = RoomCrossing.func_175859_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  117 */     else if (p_175954_0_ == StairsStraight.class)
/*      */     {
/*  119 */       structurestrongholdpieces$stronghold = StairsStraight.func_175861_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  121 */     else if (p_175954_0_ == Stairs.class)
/*      */     {
/*  123 */       structurestrongholdpieces$stronghold = Stairs.func_175863_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  125 */     else if (p_175954_0_ == Crossing.class)
/*      */     {
/*  127 */       structurestrongholdpieces$stronghold = Crossing.func_175866_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  129 */     else if (p_175954_0_ == ChestCorridor.class)
/*      */     {
/*  131 */       structurestrongholdpieces$stronghold = ChestCorridor.func_175868_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  133 */     else if (p_175954_0_ == Library.class)
/*      */     {
/*  135 */       structurestrongholdpieces$stronghold = Library.func_175864_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  137 */     else if (p_175954_0_ == PortalRoom.class)
/*      */     {
/*  139 */       structurestrongholdpieces$stronghold = PortalRoom.func_175865_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*      */     
/*  142 */     return structurestrongholdpieces$stronghold;
/*      */   }
/*      */   
/*      */   private static Stronghold func_175955_b(Stairs2 p_175955_0_, List<StructureComponent> p_175955_1_, Random p_175955_2_, int p_175955_3_, int p_175955_4_, int p_175955_5_, EnumFacing p_175955_6_, int p_175955_7_)
/*      */   {
/*  147 */     if (!canAddStructurePieces())
/*      */     {
/*  149 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  153 */     if (strongComponentType != null)
/*      */     {
/*  155 */       Stronghold structurestrongholdpieces$stronghold = func_175954_a(strongComponentType, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
/*  156 */       strongComponentType = null;
/*      */       
/*  158 */       if (structurestrongholdpieces$stronghold != null)
/*      */       {
/*  160 */         return structurestrongholdpieces$stronghold;
/*      */       }
/*      */     }
/*      */     
/*  164 */     int j = 0;
/*      */     java.util.Iterator localIterator;
/*  166 */     label200: for (; j < 5; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  171 */         localIterator.hasNext())
/*      */     {
/*  168 */       j++;
/*  169 */       int i = p_175955_2_.nextInt(totalWeight);
/*      */       
/*  171 */       localIterator = structurePieceList.iterator(); continue;PieceWeight structurestrongholdpieces$pieceweight = (PieceWeight)localIterator.next();
/*      */       
/*  173 */       i -= structurestrongholdpieces$pieceweight.pieceWeight;
/*      */       
/*  175 */       if (i < 0)
/*      */       {
/*  177 */         if ((!structurestrongholdpieces$pieceweight.canSpawnMoreStructuresOfType(p_175955_7_)) || (structurestrongholdpieces$pieceweight == p_175955_0_.strongholdPieceWeight)) {
/*      */           break label200;
/*      */         }
/*      */         
/*      */ 
/*  182 */         Stronghold structurestrongholdpieces$stronghold1 = func_175954_a(structurestrongholdpieces$pieceweight.pieceClass, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
/*      */         
/*  184 */         if (structurestrongholdpieces$stronghold1 != null)
/*      */         {
/*  186 */           structurestrongholdpieces$pieceweight.instancesSpawned += 1;
/*  187 */           p_175955_0_.strongholdPieceWeight = structurestrongholdpieces$pieceweight;
/*      */           
/*  189 */           if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructures())
/*      */           {
/*  191 */             structurePieceList.remove(structurestrongholdpieces$pieceweight);
/*      */           }
/*      */           
/*  194 */           return structurestrongholdpieces$stronghold1;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  200 */     StructureBoundingBox structureboundingbox = Corridor.func_175869_a(p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_);
/*      */     
/*  202 */     if ((structureboundingbox != null) && (structureboundingbox.minY > 1))
/*      */     {
/*  204 */       return new Corridor(p_175955_7_, p_175955_2_, structureboundingbox, p_175955_6_);
/*      */     }
/*      */     
/*      */ 
/*  208 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static StructureComponent func_175953_c(Stairs2 p_175953_0_, List<StructureComponent> p_175953_1_, Random p_175953_2_, int p_175953_3_, int p_175953_4_, int p_175953_5_, EnumFacing p_175953_6_, int p_175953_7_)
/*      */   {
/*  215 */     if (p_175953_7_ > 50)
/*      */     {
/*  217 */       return null;
/*      */     }
/*  219 */     if ((Math.abs(p_175953_3_ - p_175953_0_.getBoundingBox().minX) <= 112) && (Math.abs(p_175953_5_ - p_175953_0_.getBoundingBox().minZ) <= 112))
/*      */     {
/*  221 */       StructureComponent structurecomponent = func_175955_b(p_175953_0_, p_175953_1_, p_175953_2_, p_175953_3_, p_175953_4_, p_175953_5_, p_175953_6_, p_175953_7_ + 1);
/*      */       
/*  223 */       if (structurecomponent != null)
/*      */       {
/*  225 */         p_175953_1_.add(structurecomponent);
/*  226 */         p_175953_0_.field_75026_c.add(structurecomponent);
/*      */       }
/*      */       
/*  229 */       return structurecomponent;
/*      */     }
/*      */     
/*      */ 
/*  233 */     return null;
/*      */   }
/*      */   
/*      */   public static class ChestCorridor
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*  239 */     private static final List<WeightedRandomChestContent> strongholdChestContents = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
/*      */     
/*      */     private boolean hasMadeChest;
/*      */     
/*      */ 
/*      */     public ChestCorridor() {}
/*      */     
/*      */     public ChestCorridor(int p_i45582_1_, Random p_i45582_2_, StructureBoundingBox p_i45582_3_, EnumFacing p_i45582_4_)
/*      */     {
/*  248 */       super();
/*  249 */       this.coordBaseMode = p_i45582_4_;
/*  250 */       this.field_143013_d = getRandomDoor(p_i45582_2_);
/*  251 */       this.boundingBox = p_i45582_3_;
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  256 */       super.writeStructureToNBT(tagCompound);
/*  257 */       tagCompound.setBoolean("Chest", this.hasMadeChest);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  262 */       super.readStructureFromNBT(tagCompound);
/*  263 */       this.hasMadeChest = tagCompound.getBoolean("Chest");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  268 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */     
/*      */     public static ChestCorridor func_175868_a(List<StructureComponent> p_175868_0_, Random p_175868_1_, int p_175868_2_, int p_175868_3_, int p_175868_4_, EnumFacing p_175868_5_, int p_175868_6_)
/*      */     {
/*  273 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175868_2_, p_175868_3_, p_175868_4_, -1, -1, 0, 5, 5, 7, p_175868_5_);
/*  274 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175868_0_, structureboundingbox) == null) ? new ChestCorridor(p_175868_6_, p_175868_1_, structureboundingbox, p_175868_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  279 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  281 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  285 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  286 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*  287 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
/*  288 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
/*  289 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, structureBoundingBoxIn);
/*  290 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, structureBoundingBoxIn);
/*  291 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, structureBoundingBoxIn);
/*  292 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, structureBoundingBoxIn);
/*      */       
/*  294 */       for (int i = 2; i <= 4; i++)
/*      */       {
/*  296 */         setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, i, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  299 */       if ((!this.hasMadeChest) && (structureBoundingBoxIn.isVecInside(new BlockPos(getXWithOffset(3, 3), getYWithOffset(2), getZWithOffset(3, 3)))))
/*      */       {
/*  301 */         this.hasMadeChest = true;
/*  302 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, WeightedRandomChestContent.func_177629_a(strongholdChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 2 + randomIn.nextInt(2));
/*      */       }
/*      */       
/*  305 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Corridor
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     private int field_74993_a;
/*      */     
/*      */ 
/*      */     public Corridor() {}
/*      */     
/*      */     public Corridor(int p_i45581_1_, Random p_i45581_2_, StructureBoundingBox p_i45581_3_, EnumFacing p_i45581_4_)
/*      */     {
/*  320 */       super();
/*  321 */       this.coordBaseMode = p_i45581_4_;
/*  322 */       this.boundingBox = p_i45581_3_;
/*  323 */       this.field_74993_a = ((p_i45581_4_ != EnumFacing.NORTH) && (p_i45581_4_ != EnumFacing.SOUTH) ? p_i45581_3_.getXSize() : p_i45581_3_.getZSize());
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  328 */       super.writeStructureToNBT(tagCompound);
/*  329 */       tagCompound.setInteger("Steps", this.field_74993_a);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  334 */       super.readStructureFromNBT(tagCompound);
/*  335 */       this.field_74993_a = tagCompound.getInteger("Steps");
/*      */     }
/*      */     
/*      */     public static StructureBoundingBox func_175869_a(List<StructureComponent> p_175869_0_, Random p_175869_1_, int p_175869_2_, int p_175869_3_, int p_175869_4_, EnumFacing p_175869_5_)
/*      */     {
/*  340 */       int i = 3;
/*  341 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, 4, p_175869_5_);
/*  342 */       StructureComponent structurecomponent = StructureComponent.findIntersecting(p_175869_0_, structureboundingbox);
/*      */       
/*  344 */       if (structurecomponent == null)
/*      */       {
/*  346 */         return null;
/*      */       }
/*      */       
/*      */ 
/*  350 */       if (structurecomponent.getBoundingBox().minY == structureboundingbox.minY)
/*      */       {
/*  352 */         for (int j = 3; j >= 1; j--)
/*      */         {
/*  354 */           structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, j - 1, p_175869_5_);
/*      */           
/*  356 */           if (!structurecomponent.getBoundingBox().intersectsWith(structureboundingbox))
/*      */           {
/*  358 */             return StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, j, p_175869_5_);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  363 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  369 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  371 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  375 */       for (int i = 0; i < this.field_74993_a; i++)
/*      */       {
/*  377 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 0, i, structureBoundingBoxIn);
/*  378 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 0, i, structureBoundingBoxIn);
/*  379 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 0, i, structureBoundingBoxIn);
/*  380 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 0, i, structureBoundingBoxIn);
/*  381 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 0, i, structureBoundingBoxIn);
/*      */         
/*  383 */         for (int j = 1; j <= 3; j++)
/*      */         {
/*  385 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, j, i, structureBoundingBoxIn);
/*  386 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 1, j, i, structureBoundingBoxIn);
/*  387 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 2, j, i, structureBoundingBoxIn);
/*  388 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 3, j, i, structureBoundingBoxIn);
/*  389 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, j, i, structureBoundingBoxIn);
/*      */         }
/*      */         
/*  392 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 4, i, structureBoundingBoxIn);
/*  393 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, i, structureBoundingBoxIn);
/*  394 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, i, structureBoundingBoxIn);
/*  395 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 4, i, structureBoundingBoxIn);
/*  396 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 4, i, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  399 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Crossing
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     private boolean field_74996_b;
/*      */     
/*      */     private boolean field_74997_c;
/*      */     private boolean field_74995_d;
/*      */     private boolean field_74999_h;
/*      */     
/*      */     public Crossing() {}
/*      */     
/*      */     public Crossing(int p_i45580_1_, Random p_i45580_2_, StructureBoundingBox p_i45580_3_, EnumFacing p_i45580_4_)
/*      */     {
/*  417 */       super();
/*  418 */       this.coordBaseMode = p_i45580_4_;
/*  419 */       this.field_143013_d = getRandomDoor(p_i45580_2_);
/*  420 */       this.boundingBox = p_i45580_3_;
/*  421 */       this.field_74996_b = p_i45580_2_.nextBoolean();
/*  422 */       this.field_74997_c = p_i45580_2_.nextBoolean();
/*  423 */       this.field_74995_d = p_i45580_2_.nextBoolean();
/*  424 */       this.field_74999_h = (p_i45580_2_.nextInt(3) > 0);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  429 */       super.writeStructureToNBT(tagCompound);
/*  430 */       tagCompound.setBoolean("leftLow", this.field_74996_b);
/*  431 */       tagCompound.setBoolean("leftHigh", this.field_74997_c);
/*  432 */       tagCompound.setBoolean("rightLow", this.field_74995_d);
/*  433 */       tagCompound.setBoolean("rightHigh", this.field_74999_h);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  438 */       super.readStructureFromNBT(tagCompound);
/*  439 */       this.field_74996_b = tagCompound.getBoolean("leftLow");
/*  440 */       this.field_74997_c = tagCompound.getBoolean("leftHigh");
/*  441 */       this.field_74995_d = tagCompound.getBoolean("rightLow");
/*  442 */       this.field_74999_h = tagCompound.getBoolean("rightHigh");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  447 */       int i = 3;
/*  448 */       int j = 5;
/*      */       
/*  450 */       if ((this.coordBaseMode == EnumFacing.WEST) || (this.coordBaseMode == EnumFacing.NORTH))
/*      */       {
/*  452 */         i = 8 - i;
/*  453 */         j = 8 - j;
/*      */       }
/*      */       
/*  456 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 5, 1);
/*      */       
/*  458 */       if (this.field_74996_b)
/*      */       {
/*  460 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
/*      */       }
/*      */       
/*  463 */       if (this.field_74997_c)
/*      */       {
/*  465 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
/*      */       }
/*      */       
/*  468 */       if (this.field_74995_d)
/*      */       {
/*  470 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
/*      */       }
/*      */       
/*  473 */       if (this.field_74999_h)
/*      */       {
/*  475 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
/*      */       }
/*      */     }
/*      */     
/*      */     public static Crossing func_175866_a(List<StructureComponent> p_175866_0_, Random p_175866_1_, int p_175866_2_, int p_175866_3_, int p_175866_4_, EnumFacing p_175866_5_, int p_175866_6_)
/*      */     {
/*  481 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175866_2_, p_175866_3_, p_175866_4_, -4, -3, 0, 10, 9, 11, p_175866_5_);
/*  482 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175866_0_, structureboundingbox) == null) ? new Crossing(p_175866_6_, p_175866_1_, structureboundingbox, p_175866_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  487 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  489 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  493 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 9, 8, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  494 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 3, 0);
/*      */       
/*  496 */       if (this.field_74996_b)
/*      */       {
/*  498 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  501 */       if (this.field_74995_d)
/*      */       {
/*  503 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  506 */       if (this.field_74997_c)
/*      */       {
/*  508 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  511 */       if (this.field_74999_h)
/*      */       {
/*  513 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  516 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  517 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 8, 2, 6, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  518 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 4, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  519 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 5, 8, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  520 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 3, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  521 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 3, 3, 6, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  522 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  523 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  524 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 1, 7, 7, 1, 8, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  525 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  526 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  527 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  528 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  529 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
/*  530 */       setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 5, 6, structureBoundingBoxIn);
/*  531 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class LeftTurn
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     public LeftTurn() {}
/*      */     
/*      */ 
/*      */     public LeftTurn(int p_i45579_1_, Random p_i45579_2_, StructureBoundingBox p_i45579_3_, EnumFacing p_i45579_4_)
/*      */     {
/*  544 */       super();
/*  545 */       this.coordBaseMode = p_i45579_4_;
/*  546 */       this.field_143013_d = getRandomDoor(p_i45579_2_);
/*  547 */       this.boundingBox = p_i45579_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  552 */       if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.EAST))
/*      */       {
/*  554 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */       else
/*      */       {
/*  558 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */     }
/*      */     
/*      */     public static LeftTurn func_175867_a(List<StructureComponent> p_175867_0_, Random p_175867_1_, int p_175867_2_, int p_175867_3_, int p_175867_4_, EnumFacing p_175867_5_, int p_175867_6_)
/*      */     {
/*  564 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175867_2_, p_175867_3_, p_175867_4_, -1, -1, 0, 5, 5, 5, p_175867_5_);
/*  565 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175867_0_, structureboundingbox) == null) ? new LeftTurn(p_175867_6_, p_175867_1_, structureboundingbox, p_175867_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  570 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  572 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  576 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  577 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*      */       
/*  579 */       if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.EAST))
/*      */       {
/*  581 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       else
/*      */       {
/*  585 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  588 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Library
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*  595 */     private static final List<WeightedRandomChestContent> strongholdLibraryChestContents = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.book, 0, 1, 3, 20), new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20), new WeightedRandomChestContent(Items.map, 0, 1, 1, 1), new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1) });
/*      */     
/*      */     private boolean isLargeRoom;
/*      */     
/*      */ 
/*      */     public Library() {}
/*      */     
/*      */     public Library(int p_i45578_1_, Random p_i45578_2_, StructureBoundingBox p_i45578_3_, EnumFacing p_i45578_4_)
/*      */     {
/*  604 */       super();
/*  605 */       this.coordBaseMode = p_i45578_4_;
/*  606 */       this.field_143013_d = getRandomDoor(p_i45578_2_);
/*  607 */       this.boundingBox = p_i45578_3_;
/*  608 */       this.isLargeRoom = (p_i45578_3_.getYSize() > 6);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  613 */       super.writeStructureToNBT(tagCompound);
/*  614 */       tagCompound.setBoolean("Tall", this.isLargeRoom);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  619 */       super.readStructureFromNBT(tagCompound);
/*  620 */       this.isLargeRoom = tagCompound.getBoolean("Tall");
/*      */     }
/*      */     
/*      */     public static Library func_175864_a(List<StructureComponent> p_175864_0_, Random p_175864_1_, int p_175864_2_, int p_175864_3_, int p_175864_4_, EnumFacing p_175864_5_, int p_175864_6_)
/*      */     {
/*  625 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 11, 15, p_175864_5_);
/*      */       
/*  627 */       if ((!canStrongholdGoDeeper(structureboundingbox)) || (StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null))
/*      */       {
/*  629 */         structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 6, 15, p_175864_5_);
/*      */         
/*  631 */         if ((!canStrongholdGoDeeper(structureboundingbox)) || (StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null))
/*      */         {
/*  633 */           return null;
/*      */         }
/*      */       }
/*      */       
/*  637 */       return new Library(p_175864_6_, p_175864_1_, structureboundingbox, p_175864_5_);
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  642 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  644 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  648 */       int i = 11;
/*      */       
/*  650 */       if (!this.isLargeRoom)
/*      */       {
/*  652 */         i = 6;
/*      */       }
/*      */       
/*  655 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 13, i - 1, 14, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  656 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 1, 0);
/*  657 */       func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
/*  658 */       int j = 1;
/*  659 */       int k = 12;
/*      */       
/*  661 */       for (int l = 1; l <= 13; l++)
/*      */       {
/*  663 */         if ((l - 1) % 4 == 0)
/*      */         {
/*  665 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, l, 1, 4, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  666 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, l, 12, 4, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  667 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 2, 3, l, structureBoundingBoxIn);
/*  668 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 11, 3, l, structureBoundingBoxIn);
/*      */           
/*  670 */           if (this.isLargeRoom)
/*      */           {
/*  672 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, l, 1, 9, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  673 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 6, l, 12, 9, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  678 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, l, 1, 4, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  679 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, l, 12, 4, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */           
/*  681 */           if (this.isLargeRoom)
/*      */           {
/*  683 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, l, 1, 9, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  684 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 6, l, 12, 9, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  689 */       for (int k1 = 3; k1 < 12; k1 += 2)
/*      */       {
/*  691 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, k1, 4, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  692 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, k1, 7, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  693 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, k1, 10, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */       }
/*      */       
/*  696 */       if (this.isLargeRoom)
/*      */       {
/*  698 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  699 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 1, 12, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  700 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  701 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 12, 9, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  702 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 9, 5, 11, structureBoundingBoxIn);
/*  703 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 5, 11, structureBoundingBoxIn);
/*  704 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 9, 5, 10, structureBoundingBoxIn);
/*  705 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 2, 3, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  706 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 6, 2, 10, 6, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  707 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 6, 2, 9, 6, 2, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  708 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 6, 12, 8, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  709 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 11, structureBoundingBoxIn);
/*  710 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 8, 6, 11, structureBoundingBoxIn);
/*  711 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 10, structureBoundingBoxIn);
/*  712 */         int l1 = getMetadataWithOffset(Blocks.ladder, 3);
/*  713 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 1, 13, structureBoundingBoxIn);
/*  714 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 2, 13, structureBoundingBoxIn);
/*  715 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 3, 13, structureBoundingBoxIn);
/*  716 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 4, 13, structureBoundingBoxIn);
/*  717 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 5, 13, structureBoundingBoxIn);
/*  718 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 6, 13, structureBoundingBoxIn);
/*  719 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 7, 13, structureBoundingBoxIn);
/*  720 */         int i1 = 7;
/*  721 */         int j1 = 7;
/*  722 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 9, j1, structureBoundingBoxIn);
/*  723 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 9, j1, structureBoundingBoxIn);
/*  724 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 8, j1, structureBoundingBoxIn);
/*  725 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 8, j1, structureBoundingBoxIn);
/*  726 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1, structureBoundingBoxIn);
/*  727 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1, structureBoundingBoxIn);
/*  728 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 2, 7, j1, structureBoundingBoxIn);
/*  729 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 + 1, 7, j1, structureBoundingBoxIn);
/*  730 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1 - 1, structureBoundingBoxIn);
/*  731 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1 + 1, structureBoundingBoxIn);
/*  732 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1 - 1, structureBoundingBoxIn);
/*  733 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1 + 1, structureBoundingBoxIn);
/*  734 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 2, 8, j1, structureBoundingBoxIn);
/*  735 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 + 1, 8, j1, structureBoundingBoxIn);
/*  736 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 1, 8, j1 - 1, structureBoundingBoxIn);
/*  737 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 1, 8, j1 + 1, structureBoundingBoxIn);
/*  738 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1, 8, j1 - 1, structureBoundingBoxIn);
/*  739 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1, 8, j1 + 1, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  742 */       generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 3, 5, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn, 1, 5, 2) }), 1 + randomIn.nextInt(4));
/*      */       
/*  744 */       if (this.isLargeRoom)
/*      */       {
/*  746 */         setBlockState(worldIn, Blocks.air.getDefaultState(), 12, 9, 1, structureBoundingBoxIn);
/*  747 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 12, 8, 1, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn, 1, 5, 2) }), 1 + randomIn.nextInt(4));
/*      */       }
/*      */       
/*  750 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static class PieceWeight
/*      */   {
/*      */     public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
/*      */     public final int pieceWeight;
/*      */     public int instancesSpawned;
/*      */     public int instancesLimit;
/*      */     
/*      */     public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> p_i2076_1_, int p_i2076_2_, int p_i2076_3_)
/*      */     {
/*  764 */       this.pieceClass = p_i2076_1_;
/*  765 */       this.pieceWeight = p_i2076_2_;
/*  766 */       this.instancesLimit = p_i2076_3_;
/*      */     }
/*      */     
/*      */     public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
/*      */     {
/*  771 */       return (this.instancesLimit == 0) || (this.instancesSpawned < this.instancesLimit);
/*      */     }
/*      */     
/*      */     public boolean canSpawnMoreStructures()
/*      */     {
/*  776 */       return (this.instancesLimit == 0) || (this.instancesSpawned < this.instancesLimit);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class PortalRoom
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     private boolean hasSpawner;
/*      */     
/*      */     public PortalRoom() {}
/*      */     
/*      */     public PortalRoom(int p_i45577_1_, Random p_i45577_2_, StructureBoundingBox p_i45577_3_, EnumFacing p_i45577_4_)
/*      */     {
/*  790 */       super();
/*  791 */       this.coordBaseMode = p_i45577_4_;
/*  792 */       this.boundingBox = p_i45577_3_;
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/*  797 */       super.writeStructureToNBT(tagCompound);
/*  798 */       tagCompound.setBoolean("Mob", this.hasSpawner);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/*  803 */       super.readStructureFromNBT(tagCompound);
/*  804 */       this.hasSpawner = tagCompound.getBoolean("Mob");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  809 */       if (componentIn != null)
/*      */       {
/*  811 */         ((StructureStrongholdPieces.Stairs2)componentIn).strongholdPortalRoom = this;
/*      */       }
/*      */     }
/*      */     
/*      */     public static PortalRoom func_175865_a(List<StructureComponent> p_175865_0_, Random p_175865_1_, int p_175865_2_, int p_175865_3_, int p_175865_4_, EnumFacing p_175865_5_, int p_175865_6_)
/*      */     {
/*  817 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175865_2_, p_175865_3_, p_175865_4_, -4, -1, 0, 11, 8, 16, p_175865_5_);
/*  818 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175865_0_, structureboundingbox) == null) ? new PortalRoom(p_175865_6_, p_175865_1_, structureboundingbox, p_175865_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  823 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 7, 15, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  824 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
/*  825 */       int i = 6;
/*  826 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, i, 1, 1, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  827 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, i, 1, 9, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  828 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 1, 8, i, 2, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  829 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 14, 8, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  830 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 2, 1, 4, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  831 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 1, 9, 1, 4, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  832 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*  833 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*  834 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 1, 8, 7, 1, 12, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  835 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*      */       
/*  837 */       for (int j = 3; j < 14; j += 2)
/*      */       {
/*  839 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, j, 0, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  840 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 3, j, 10, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*      */       }
/*      */       
/*  843 */       for (int k1 = 2; k1 < 9; k1 += 2)
/*      */       {
/*  845 */         fillWithBlocks(worldIn, structureBoundingBoxIn, k1, 3, 15, k1, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*      */       }
/*      */       
/*  848 */       int l1 = getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
/*  849 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 6, 1, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  850 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 2, 6, 6, 2, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  851 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 3, 7, 6, 3, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*      */       
/*  853 */       for (int k = 4; k <= 6; k++)
/*      */       {
/*  855 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 1, 4, structureBoundingBoxIn);
/*  856 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 2, 5, structureBoundingBoxIn);
/*  857 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 3, 6, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  860 */       int i2 = EnumFacing.NORTH.getHorizontalIndex();
/*  861 */       int l = EnumFacing.SOUTH.getHorizontalIndex();
/*  862 */       int i1 = EnumFacing.EAST.getHorizontalIndex();
/*  863 */       int j1 = EnumFacing.WEST.getHorizontalIndex();
/*      */       
/*  865 */       if (this.coordBaseMode != null)
/*      */       {
/*  867 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case SOUTH: 
/*  870 */           i2 = EnumFacing.SOUTH.getHorizontalIndex();
/*  871 */           l = EnumFacing.NORTH.getHorizontalIndex();
/*  872 */           break;
/*      */         
/*      */         case UP: 
/*  875 */           i2 = EnumFacing.WEST.getHorizontalIndex();
/*  876 */           l = EnumFacing.EAST.getHorizontalIndex();
/*  877 */           i1 = EnumFacing.SOUTH.getHorizontalIndex();
/*  878 */           j1 = EnumFacing.NORTH.getHorizontalIndex();
/*  879 */           break;
/*      */         
/*      */         case WEST: 
/*  882 */           i2 = EnumFacing.EAST.getHorizontalIndex();
/*  883 */           l = EnumFacing.WEST.getHorizontalIndex();
/*  884 */           i1 = EnumFacing.SOUTH.getHorizontalIndex();
/*  885 */           j1 = EnumFacing.NORTH.getHorizontalIndex();
/*      */         }
/*      */         
/*      */       }
/*  889 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 4, 3, 8, structureBoundingBoxIn);
/*  890 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 5, 3, 8, structureBoundingBoxIn);
/*  891 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 6, 3, 8, structureBoundingBoxIn);
/*  892 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 4, 3, 12, structureBoundingBoxIn);
/*  893 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 5, 3, 12, structureBoundingBoxIn);
/*  894 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 6, 3, 12, structureBoundingBoxIn);
/*  895 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 3, 3, 9, structureBoundingBoxIn);
/*  896 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 3, 3, 10, structureBoundingBoxIn);
/*  897 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 3, 3, 11, structureBoundingBoxIn);
/*  898 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 7, 3, 9, structureBoundingBoxIn);
/*  899 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 7, 3, 10, structureBoundingBoxIn);
/*  900 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty(BlockEndPortalFrame.EYE, Boolean.valueOf(randomIn.nextFloat() > 0.9F)), 7, 3, 11, structureBoundingBoxIn);
/*      */       
/*  902 */       if (!this.hasSpawner)
/*      */       {
/*  904 */         i = getYWithOffset(3);
/*  905 */         BlockPos blockpos = new BlockPos(getXWithOffset(5, 6), i, getZWithOffset(5, 6));
/*      */         
/*  907 */         if (structureBoundingBoxIn.isVecInside(blockpos))
/*      */         {
/*  909 */           this.hasSpawner = true;
/*  910 */           worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
/*  911 */           net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*      */           
/*  913 */           if ((tileentity instanceof net.minecraft.tileentity.TileEntityMobSpawner))
/*      */           {
/*  915 */             ((net.minecraft.tileentity.TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Silverfish");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  920 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Prison
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     public Prison() {}
/*      */     
/*      */     public Prison(int p_i45576_1_, Random p_i45576_2_, StructureBoundingBox p_i45576_3_, EnumFacing p_i45576_4_)
/*      */     {
/*  932 */       super();
/*  933 */       this.coordBaseMode = p_i45576_4_;
/*  934 */       this.field_143013_d = getRandomDoor(p_i45576_2_);
/*  935 */       this.boundingBox = p_i45576_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  940 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */     
/*      */     public static Prison func_175860_a(List<StructureComponent> p_175860_0_, Random p_175860_1_, int p_175860_2_, int p_175860_3_, int p_175860_4_, EnumFacing p_175860_5_, int p_175860_6_)
/*      */     {
/*  945 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175860_2_, p_175860_3_, p_175860_4_, -1, -1, 0, 9, 5, 11, p_175860_5_);
/*  946 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175860_0_, structureboundingbox) == null) ? new Prison(p_175860_6_, p_175860_1_, structureboundingbox, p_175860_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  951 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  953 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  957 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 4, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  958 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*  959 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  960 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 1, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  961 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 3, 3, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  962 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 3, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  963 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 3, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  964 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  965 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  966 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
/*  967 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 8, structureBoundingBoxIn);
/*  968 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, structureBoundingBoxIn);
/*  969 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, structureBoundingBoxIn);
/*  970 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, structureBoundingBoxIn);
/*  971 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, structureBoundingBoxIn);
/*  972 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RightTurn
/*      */     extends StructureStrongholdPieces.LeftTurn
/*      */   {
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/*  981 */       if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.EAST))
/*      */       {
/*  983 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */       else
/*      */       {
/*  987 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/*  993 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  995 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  999 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1000 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*      */       
/* 1002 */       if ((this.coordBaseMode != EnumFacing.NORTH) && (this.coordBaseMode != EnumFacing.EAST))
/*      */       {
/* 1004 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       else
/*      */       {
/* 1008 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/* 1011 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/* 1018 */     private static final List<WeightedRandomChestContent> strongholdRoomCrossingChestContents = Lists.newArrayList(new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1) });
/*      */     
/*      */     protected int roomType;
/*      */     
/*      */ 
/*      */     public RoomCrossing() {}
/*      */     
/*      */     public RoomCrossing(int p_i45575_1_, Random p_i45575_2_, StructureBoundingBox p_i45575_3_, EnumFacing p_i45575_4_)
/*      */     {
/* 1027 */       super();
/* 1028 */       this.coordBaseMode = p_i45575_4_;
/* 1029 */       this.field_143013_d = getRandomDoor(p_i45575_2_);
/* 1030 */       this.boundingBox = p_i45575_3_;
/* 1031 */       this.roomType = p_i45575_2_.nextInt(5);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1036 */       super.writeStructureToNBT(tagCompound);
/* 1037 */       tagCompound.setInteger("Type", this.roomType);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1042 */       super.readStructureFromNBT(tagCompound);
/* 1043 */       this.roomType = tagCompound.getInteger("Type");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1048 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 4, 1);
/* 1049 */       getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
/* 1050 */       getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
/*      */     }
/*      */     
/*      */     public static RoomCrossing func_175859_a(List<StructureComponent> p_175859_0_, Random p_175859_1_, int p_175859_2_, int p_175859_3_, int p_175859_4_, EnumFacing p_175859_5_, int p_175859_6_)
/*      */     {
/* 1055 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175859_2_, p_175859_3_, p_175859_4_, -4, -1, 0, 11, 7, 11, p_175859_5_);
/* 1056 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175859_0_, structureboundingbox) == null) ? new RoomCrossing(p_175859_6_, p_175859_1_, structureboundingbox, p_175859_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1061 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1063 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1067 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 6, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1068 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 1, 0);
/* 1069 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1070 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1071 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       
/* 1073 */       switch (this.roomType)
/*      */       {
/*      */       case 0: 
/* 1076 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
/* 1077 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/* 1078 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/* 1079 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
/* 1080 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
/* 1081 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
/* 1082 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
/* 1083 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
/* 1084 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
/* 1085 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 6, structureBoundingBoxIn);
/* 1086 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 4, structureBoundingBoxIn);
/* 1087 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
/* 1088 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 6, structureBoundingBoxIn);
/* 1089 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
/* 1090 */         setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
/* 1091 */         break;
/*      */       
/*      */       case 1: 
/* 1094 */         for (int i1 = 0; i1 < 5; i1++)
/*      */         {
/* 1096 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + i1, structureBoundingBoxIn);
/* 1097 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + i1, structureBoundingBoxIn);
/* 1098 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 3, structureBoundingBoxIn);
/* 1099 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 7, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 1102 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
/* 1103 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/* 1104 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/* 1105 */         setBlockState(worldIn, Blocks.flowing_water.getDefaultState(), 5, 4, 5, structureBoundingBoxIn);
/* 1106 */         break;
/*      */       
/*      */       case 2: 
/* 1109 */         for (int i = 1; i <= 9; i++)
/*      */         {
/* 1111 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 3, i, structureBoundingBoxIn);
/* 1112 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 9, 3, i, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 1115 */         for (int j = 1; j <= 9; j++)
/*      */         {
/* 1117 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 1, structureBoundingBoxIn);
/* 1118 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 9, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 1121 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
/* 1122 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
/* 1123 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
/* 1124 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
/* 1125 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
/* 1126 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
/* 1127 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
/* 1128 */         setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
/*      */         
/* 1130 */         for (int k = 1; k <= 3; k++)
/*      */         {
/* 1132 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 4, structureBoundingBoxIn);
/* 1133 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 4, structureBoundingBoxIn);
/* 1134 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 6, structureBoundingBoxIn);
/* 1135 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 6, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 1138 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/*      */         
/* 1140 */         for (int l = 2; l <= 8; l++)
/*      */         {
/* 1142 */           setBlockState(worldIn, Blocks.planks.getDefaultState(), 2, 3, l, structureBoundingBoxIn);
/* 1143 */           setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, l, structureBoundingBoxIn);
/*      */           
/* 1145 */           if ((l <= 3) || (l >= 7))
/*      */           {
/* 1147 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 4, 3, l, structureBoundingBoxIn);
/* 1148 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 5, 3, l, structureBoundingBoxIn);
/* 1149 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 6, 3, l, structureBoundingBoxIn);
/*      */           }
/*      */           
/* 1152 */           setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 3, l, structureBoundingBoxIn);
/* 1153 */           setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 3, l, structureBoundingBoxIn);
/*      */         }
/*      */         
/* 1156 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, structureBoundingBoxIn);
/* 1157 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, structureBoundingBoxIn);
/* 1158 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, structureBoundingBoxIn);
/* 1159 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 4, 8, WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 1 + randomIn.nextInt(4));
/*      */       }
/*      */       
/* 1162 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Stairs
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     private boolean field_75024_a;
/*      */     
/*      */ 
/*      */     public Stairs() {}
/*      */     
/*      */     public Stairs(int p_i2081_1_, Random p_i2081_2_, int p_i2081_3_, int p_i2081_4_)
/*      */     {
/* 1177 */       super();
/* 1178 */       this.field_75024_a = true;
/* 1179 */       this.coordBaseMode = net.minecraft.util.EnumFacing.Plane.HORIZONTAL.random(p_i2081_2_);
/* 1180 */       this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
/*      */       
/* 1182 */       switch (this.coordBaseMode)
/*      */       {
/*      */       case NORTH: 
/*      */       case SOUTH: 
/* 1186 */         this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
/* 1187 */         break;
/*      */       
/*      */       default: 
/* 1190 */         this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
/*      */       }
/*      */     }
/*      */     
/*      */     public Stairs(int p_i45574_1_, Random p_i45574_2_, StructureBoundingBox p_i45574_3_, EnumFacing p_i45574_4_)
/*      */     {
/* 1196 */       super();
/* 1197 */       this.field_75024_a = false;
/* 1198 */       this.coordBaseMode = p_i45574_4_;
/* 1199 */       this.field_143013_d = getRandomDoor(p_i45574_2_);
/* 1200 */       this.boundingBox = p_i45574_3_;
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1205 */       super.writeStructureToNBT(tagCompound);
/* 1206 */       tagCompound.setBoolean("Source", this.field_75024_a);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1211 */       super.readStructureFromNBT(tagCompound);
/* 1212 */       this.field_75024_a = tagCompound.getBoolean("Source");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1217 */       if (this.field_75024_a)
/*      */       {
/* 1219 */         StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.Crossing.class;
/*      */       }
/*      */       
/* 1222 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */     
/*      */     public static Stairs func_175863_a(List<StructureComponent> p_175863_0_, Random p_175863_1_, int p_175863_2_, int p_175863_3_, int p_175863_4_, EnumFacing p_175863_5_, int p_175863_6_)
/*      */     {
/* 1227 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175863_2_, p_175863_3_, p_175863_4_, -1, -7, 0, 5, 11, 5, p_175863_5_);
/* 1228 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175863_0_, structureboundingbox) == null) ? new Stairs(p_175863_6_, p_175863_1_, structureboundingbox, p_175863_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1233 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1235 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1239 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1240 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
/* 1241 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
/* 1242 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 6, 1, structureBoundingBoxIn);
/* 1243 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 1, structureBoundingBoxIn);
/* 1244 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, structureBoundingBoxIn);
/* 1245 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 2, structureBoundingBoxIn);
/* 1246 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, 3, structureBoundingBoxIn);
/* 1247 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, structureBoundingBoxIn);
/* 1248 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, 3, structureBoundingBoxIn);
/* 1249 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 3, structureBoundingBoxIn);
/* 1250 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, structureBoundingBoxIn);
/* 1251 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 2, structureBoundingBoxIn);
/* 1252 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 2, 1, structureBoundingBoxIn);
/* 1253 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, structureBoundingBoxIn);
/* 1254 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 2, 1, structureBoundingBoxIn);
/* 1255 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 1, structureBoundingBoxIn);
/* 1256 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, structureBoundingBoxIn);
/* 1257 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
/* 1258 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, structureBoundingBoxIn);
/* 1259 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Stairs2
/*      */     extends StructureStrongholdPieces.Stairs
/*      */   {
/*      */     public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
/*      */     public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
/* 1268 */     public List<StructureComponent> field_75026_c = Lists.newArrayList();
/*      */     
/*      */ 
/*      */     public Stairs2() {}
/*      */     
/*      */ 
/*      */     public Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_, int p_i2083_4_)
/*      */     {
/* 1276 */       super(p_i2083_2_, p_i2083_3_, p_i2083_4_);
/*      */     }
/*      */     
/*      */     public BlockPos getBoundingBoxCenter()
/*      */     {
/* 1281 */       return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.getBoundingBoxCenter() : super.getBoundingBoxCenter();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class StairsStraight
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     public StairsStraight() {}
/*      */     
/*      */     public StairsStraight(int p_i45572_1_, Random p_i45572_2_, StructureBoundingBox p_i45572_3_, EnumFacing p_i45572_4_)
/*      */     {
/* 1293 */       super();
/* 1294 */       this.coordBaseMode = p_i45572_4_;
/* 1295 */       this.field_143013_d = getRandomDoor(p_i45572_2_);
/* 1296 */       this.boundingBox = p_i45572_3_;
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1301 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */     
/*      */     public static StairsStraight func_175861_a(List<StructureComponent> p_175861_0_, Random p_175861_1_, int p_175861_2_, int p_175861_3_, int p_175861_4_, EnumFacing p_175861_5_, int p_175861_6_)
/*      */     {
/* 1306 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175861_2_, p_175861_3_, p_175861_4_, -1, -7, 0, 5, 11, 8, p_175861_5_);
/* 1307 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175861_0_, structureboundingbox) == null) ? new StairsStraight(p_175861_6_, p_175861_1_, structureboundingbox, p_175861_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1312 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1314 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1318 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 7, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1319 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
/* 1320 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
/* 1321 */       int i = getMetadataWithOffset(Blocks.stone_stairs, 2);
/*      */       
/* 1323 */       for (int j = 0; j < 6; j++)
/*      */       {
/* 1325 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 1, 6 - j, 1 + j, structureBoundingBoxIn);
/* 1326 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 2, 6 - j, 1 + j, structureBoundingBoxIn);
/* 1327 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 3, 6 - j, 1 + j, structureBoundingBoxIn);
/*      */         
/* 1329 */         if (j < 5)
/*      */         {
/* 1331 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5 - j, 1 + j, structureBoundingBoxIn);
/* 1332 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 5 - j, 1 + j, structureBoundingBoxIn);
/* 1333 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 5 - j, 1 + j, structureBoundingBoxIn);
/*      */         }
/*      */       }
/*      */       
/* 1337 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class Stones
/*      */     extends StructureComponent.BlockSelector
/*      */   {
/*      */     public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_)
/*      */     {
/* 1350 */       if (p_75062_5_)
/*      */       {
/* 1352 */         float f = rand.nextFloat();
/*      */         
/* 1354 */         if (f < 0.2F)
/*      */         {
/* 1356 */           this.blockstate = Blocks.stonebrick.getStateFromMeta(net.minecraft.block.BlockStoneBrick.CRACKED_META);
/*      */         }
/* 1358 */         else if (f < 0.5F)
/*      */         {
/* 1360 */           this.blockstate = Blocks.stonebrick.getStateFromMeta(net.minecraft.block.BlockStoneBrick.MOSSY_META);
/*      */         }
/* 1362 */         else if (f < 0.55F)
/*      */         {
/* 1364 */           this.blockstate = Blocks.monster_egg.getStateFromMeta(net.minecraft.block.BlockSilverfish.EnumType.STONEBRICK.getMetadata());
/*      */         }
/*      */         else
/*      */         {
/* 1368 */           this.blockstate = Blocks.stonebrick.getDefaultState();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1373 */         this.blockstate = Blocks.air.getDefaultState();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class Straight
/*      */     extends StructureStrongholdPieces.Stronghold
/*      */   {
/*      */     private boolean expandsX;
/*      */     private boolean expandsZ;
/*      */     
/*      */     public Straight() {}
/*      */     
/*      */     public Straight(int p_i45573_1_, Random p_i45573_2_, StructureBoundingBox p_i45573_3_, EnumFacing p_i45573_4_)
/*      */     {
/* 1389 */       super();
/* 1390 */       this.coordBaseMode = p_i45573_4_;
/* 1391 */       this.field_143013_d = getRandomDoor(p_i45573_2_);
/* 1392 */       this.boundingBox = p_i45573_3_;
/* 1393 */       this.expandsX = (p_i45573_2_.nextInt(2) == 0);
/* 1394 */       this.expandsZ = (p_i45573_2_.nextInt(2) == 0);
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1399 */       super.writeStructureToNBT(tagCompound);
/* 1400 */       tagCompound.setBoolean("Left", this.expandsX);
/* 1401 */       tagCompound.setBoolean("Right", this.expandsZ);
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1406 */       super.readStructureFromNBT(tagCompound);
/* 1407 */       this.expandsX = tagCompound.getBoolean("Left");
/* 1408 */       this.expandsZ = tagCompound.getBoolean("Right");
/*      */     }
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
/*      */     {
/* 1413 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       
/* 1415 */       if (this.expandsX)
/*      */       {
/* 1417 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
/*      */       }
/*      */       
/* 1420 */       if (this.expandsZ)
/*      */       {
/* 1422 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
/*      */       }
/*      */     }
/*      */     
/*      */     public static Straight func_175862_a(List<StructureComponent> p_175862_0_, Random p_175862_1_, int p_175862_2_, int p_175862_3_, int p_175862_4_, EnumFacing p_175862_5_, int p_175862_6_)
/*      */     {
/* 1428 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175862_2_, p_175862_3_, p_175862_4_, -1, -1, 0, 5, 5, 7, p_175862_5_);
/* 1429 */       return (canStrongholdGoDeeper(structureboundingbox)) && (StructureComponent.findIntersecting(p_175862_0_, structureboundingbox) == null) ? new Straight(p_175862_6_, p_175862_1_, structureboundingbox, p_175862_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
/*      */     {
/* 1434 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1436 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1440 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1441 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/* 1442 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
/* 1443 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 1, 2, 1, Blocks.torch.getDefaultState());
/* 1444 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 3, 2, 1, Blocks.torch.getDefaultState());
/* 1445 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 1, 2, 5, Blocks.torch.getDefaultState());
/* 1446 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 3, 2, 5, Blocks.torch.getDefaultState());
/*      */       
/* 1448 */       if (this.expandsX)
/*      */       {
/* 1450 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/* 1453 */       if (this.expandsZ)
/*      */       {
/* 1455 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/* 1458 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Stronghold
/*      */     extends StructureComponent
/*      */   {
/* 1465 */     protected Door field_143013_d = Door.OPENING;
/*      */     
/*      */ 
/*      */     public Stronghold() {}
/*      */     
/*      */ 
/*      */     protected Stronghold(int p_i2087_1_)
/*      */     {
/* 1473 */       super();
/*      */     }
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1478 */       tagCompound.setString("EntryDoor", this.field_143013_d.name());
/*      */     }
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound)
/*      */     {
/* 1483 */       this.field_143013_d = Door.valueOf(tagCompound.getString("EntryDoor"));
/*      */     }
/*      */     
/*      */     protected void placeDoor(World worldIn, Random p_74990_2_, StructureBoundingBox p_74990_3_, Door p_74990_4_, int p_74990_5_, int p_74990_6_, int p_74990_7_)
/*      */     {
/* 1488 */       switch (p_74990_4_)
/*      */       {
/*      */       case GRATES: 
/*      */       default: 
/* 1492 */         fillWithBlocks(worldIn, p_74990_3_, p_74990_5_, p_74990_6_, p_74990_7_, p_74990_5_ + 3 - 1, p_74990_6_ + 3 - 1, p_74990_7_, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1493 */         break;
/*      */       
/*      */       case IRON_DOOR: 
/* 1496 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1497 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1498 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1499 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1500 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1501 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1502 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1503 */         setBlockState(worldIn, Blocks.oak_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1504 */         setBlockState(worldIn, Blocks.oak_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1505 */         break;
/*      */       
/*      */       case OPENING: 
/* 1508 */         setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1509 */         setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1510 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1511 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1512 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1513 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1514 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1515 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1516 */         setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1517 */         break;
/*      */       
/*      */       case WOOD_DOOR: 
/* 1520 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1521 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1522 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1523 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1524 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1525 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1526 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1527 */         setBlockState(worldIn, Blocks.iron_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1528 */         setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1529 */         setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(getMetadataWithOffset(Blocks.stone_button, 4)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ + 1, p_74990_3_);
/* 1530 */         setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(getMetadataWithOffset(Blocks.stone_button, 3)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ - 1, p_74990_3_);
/*      */       }
/*      */     }
/*      */     
/*      */     protected Door getRandomDoor(Random p_74988_1_)
/*      */     {
/* 1536 */       int i = p_74988_1_.nextInt(5);
/*      */       
/* 1538 */       switch (i)
/*      */       {
/*      */       case 0: 
/*      */       case 1: 
/*      */       default: 
/* 1543 */         return Door.OPENING;
/*      */       
/*      */       case 2: 
/* 1546 */         return Door.WOOD_DOOR;
/*      */       
/*      */       case 3: 
/* 1549 */         return Door.GRATES;
/*      */       }
/*      */       
/* 1552 */       return Door.IRON_DOOR;
/*      */     }
/*      */     
/*      */ 
/*      */     protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 p_74986_1_, List<StructureComponent> p_74986_2_, Random p_74986_3_, int p_74986_4_, int p_74986_5_)
/*      */     {
/* 1558 */       if (this.coordBaseMode != null)
/*      */       {
/* 1560 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1563 */           return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ - 1, this.coordBaseMode, getComponentType());
/*      */         
/*      */         case SOUTH: 
/* 1566 */           return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.maxZ + 1, this.coordBaseMode, getComponentType());
/*      */         
/*      */         case UP: 
/* 1569 */           return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, getComponentType());
/*      */         
/*      */         case WEST: 
/* 1572 */           return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1576 */       return null;
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 p_74989_1_, List<StructureComponent> p_74989_2_, Random p_74989_3_, int p_74989_4_, int p_74989_5_)
/*      */     {
/* 1581 */       if (this.coordBaseMode != null)
/*      */       {
/* 1583 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1586 */           return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, getComponentType());
/*      */         
/*      */         case SOUTH: 
/* 1589 */           return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, getComponentType());
/*      */         
/*      */         case UP: 
/* 1592 */           return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         
/*      */         case WEST: 
/* 1595 */           return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1599 */       return null;
/*      */     }
/*      */     
/*      */     protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 p_74987_1_, List<StructureComponent> p_74987_2_, Random p_74987_3_, int p_74987_4_, int p_74987_5_)
/*      */     {
/* 1604 */       if (this.coordBaseMode != null)
/*      */       {
/* 1606 */         switch (this.coordBaseMode)
/*      */         {
/*      */         case NORTH: 
/* 1609 */           return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, getComponentType());
/*      */         
/*      */         case SOUTH: 
/* 1612 */           return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, getComponentType());
/*      */         
/*      */         case UP: 
/* 1615 */           return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         
/*      */         case WEST: 
/* 1618 */           return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         }
/*      */         
/*      */       }
/* 1622 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean canStrongholdGoDeeper(StructureBoundingBox p_74991_0_)
/*      */     {
/* 1627 */       return (p_74991_0_ != null) && (p_74991_0_.minY > 10);
/*      */     }
/*      */     
/*      */     public static enum Door
/*      */     {
/* 1632 */       OPENING, 
/* 1633 */       WOOD_DOOR, 
/* 1634 */       GRATES, 
/* 1635 */       IRON_DOOR;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureStrongholdPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */