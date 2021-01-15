/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class PlayerUtils
/*    */ {
/*    */   public static boolean isInLiquid()
/*    */   {
/* 15 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
/* 16 */       for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
/* 17 */         BlockPos pos = new BlockPos(x, (int)Minecraft.getMinecraft().thePlayer.boundingBox.minY, z);
/* 18 */         Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
/* 19 */         if ((block != null) && (!(block instanceof net.minecraft.block.BlockAir)))
/* 20 */           return block instanceof net.minecraft.block.BlockLiquid;
/*    */       }
/*    */     }
/* 23 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isInsideBlock() {
/* 27 */     for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
/* 28 */       for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
/* 29 */         for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
/* 30 */           Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
/* 31 */           if ((block != null) && (!(block instanceof net.minecraft.block.BlockAir))) {
/* 32 */             AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
/* 33 */             if ((block instanceof net.minecraft.block.BlockHopper))
/* 34 */               boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
/* 35 */             if ((boundingBox != null) && (Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)))
/* 36 */               return true;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\PlayerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */