package rip.autumn.utils.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public final class PathfindingUtils {
   private static final Minecraft mc = Minecraft.getMinecraft();

   public static ArrayList computePath(CustomVec3 topFrom, CustomVec3 to) {
      if (!canPassThrow(new BlockPos(topFrom.mc()))) {
         topFrom = topFrom.addVector(0.0D, 1.0D, 0.0D);
      }

      AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
      pathfinder.compute();
      int i = 0;
      CustomVec3 lastLoc = null;
      CustomVec3 lastDashLoc = null;
      ArrayList path = new ArrayList();
      ArrayList pathFinderPath = pathfinder.getPath();

      for(Iterator var8 = pathFinderPath.iterator(); var8.hasNext(); ++i) {
         CustomVec3 pathElm = (CustomVec3)var8.next();
         if (i != 0 && i != pathFinderPath.size() - 1) {
            boolean canContinue = true;
            if (pathElm.squareDistanceTo(lastDashLoc) > 25.0D) {
               canContinue = false;
            } else {
               double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
               double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
               double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
               double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
               double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
               double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());

               label54:
               for(int x = (int)smallX; (double)x <= bigX; ++x) {
                  for(int y = (int)smallY; (double)y <= bigY; ++y) {
                     for(int z = (int)smallZ; (double)z <= bigZ; ++z) {
                        if (!AStarCustomPathFinder.checkPositionValidity(new CustomVec3((double)x, (double)y, (double)z))) {
                           canContinue = false;
                           break label54;
                        }
                     }
                  }
               }
            }

            if (!canContinue) {
               path.add(lastLoc.addVector(0.5D, 0.0D, 0.5D));
               lastDashLoc = lastLoc;
            }
         } else {
            if (lastLoc != null) {
               path.add(lastLoc.addVector(0.5D, 0.0D, 0.5D));
            }

            path.add(pathElm.addVector(0.5D, 0.0D, 0.5D));
            lastDashLoc = pathElm;
         }

         lastLoc = pathElm;
      }

      return path;
   }

   private static boolean canPassThrow(BlockPos pos) {
      Block block = mc.theWorld.getBlockState(pos).getBlock();
      return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
   }
}
