/*    */ package net.minecraft.world.gen.structure;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class StructureMineshaftStart
/*    */   extends StructureStart
/*    */ {
/*    */   public StructureMineshaftStart() {}
/*    */   
/*    */   public StructureMineshaftStart(World worldIn, Random rand, int chunkX, int chunkZ)
/*    */   {
/* 14 */     super(chunkX, chunkZ);
/* 15 */     StructureMineshaftPieces.Room structuremineshaftpieces$room = new StructureMineshaftPieces.Room(0, rand, (chunkX << 4) + 2, (chunkZ << 4) + 2);
/* 16 */     this.components.add(structuremineshaftpieces$room);
/* 17 */     structuremineshaftpieces$room.buildComponent(structuremineshaftpieces$room, this.components, rand);
/* 18 */     updateBoundingBox();
/* 19 */     markAvailableHeight(worldIn, rand, 10);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\StructureMineshaftStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */