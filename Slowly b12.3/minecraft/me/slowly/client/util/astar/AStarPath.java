package me.slowly.client.util.astar;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.BlockPos;

public class AStarPath {
   private BlockPos blockPosFrom;
   private BlockPos blockPosTo;
   private ArrayList path = new ArrayList();
   private ArrayList openList = new ArrayList();
   private ArrayList closedList = new ArrayList();
   private AStarUtil aStarUtil = new AStarUtil();
   private AStarNode lastNode;
   private long timeLeft;

   public AStarPath(BlockPos from, BlockPos to) {
      this.blockPosFrom = from;
      this.blockPosTo = to;
   }

   private void prepare() {
      AStarNode startField = new AStarNode((double)this.blockPosFrom.getX(), (double)this.blockPosFrom.getY(), (double)this.blockPosFrom.getZ());
      startField.setHeuristic(-1.0D);
      this.openList.add(startField);
      this.timeLeft = System.currentTimeMillis();
   }

   private boolean step() {
      if (this.blockPosFrom.getX() == this.blockPosTo.getX() && this.blockPosFrom.getY() == this.blockPosTo.getY() && this.blockPosFrom.getZ() == this.blockPosTo.getZ()) {
         return true;
      } else if (System.currentTimeMillis() - this.timeLeft >= 3000L) {
         return true;
      } else {
         int nodeId = -1;

         for(int i = 0; i < this.openList.size(); ++i) {
            if (nodeId == -1 || ((AStarNode)this.openList.get(i)).getHeuristic() < ((AStarNode)this.openList.get(nodeId)).getHeuristic()) {
               nodeId = i;
            }
         }

         this.closedList.add((AStarNode)this.openList.get(nodeId));
         this.openList.remove(nodeId);
         AStarNode node = (AStarNode)this.closedList.get(this.closedList.size() - 1);

         for(double x = node.getX() - 1.0D; x <= node.getX() + 1.0D; ++x) {
            for(double z = node.getZ() - 1.0D; z <= node.getZ() + 1.0D; ++z) {
               for(double y = node.getY() - 1.0D; y <= node.getY() + 1.0D; ++y) {
                  AStarNode currentNode = new AStarNode(x, y, z);
                  if (this.aStarUtil.isWalkableBlock(new BlockPos(node.getX(), node.getY(), node.getZ()), new BlockPos(x, y, z)) && !this.isInClosedList(currentNode)) {
                     currentNode.setHeuristic(this.aStarUtil.getCost(new BlockPos(this.blockPosTo.getX(), this.blockPosTo.getY(), this.blockPosTo.getZ()), new BlockPos(x, y, z)));
                     currentNode.setParent(node);
                     this.openList.add(currentNode);
                     if ((double)this.blockPosTo.getX() == currentNode.getX() && (double)this.blockPosTo.getY() == currentNode.getY() && (double)this.blockPosTo.getZ() == currentNode.getZ()) {
                        this.lastNode = currentNode;
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public void doAstar() {
      this.prepare();

      while(!this.openList.isEmpty() && !this.step()) {
         ;
      }

      this.fillPathList();
   }

   private void fillPathList() {
      if (this.lastNode != null) {
         AStarNode startPoint = this.lastNode;
         this.path.add(startPoint);

         while((startPoint = startPoint.getParent()) != null) {
            this.path.add(startPoint);
         }
      }

   }

   private boolean isInClosedList(AStarNode node) {
      Iterator var3 = this.closedList.iterator();

      AStarNode iNode;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         iNode = (AStarNode)var3.next();
      } while(node.getX() != iNode.getX() || node.getY() != iNode.getY() || node.getZ() != iNode.getZ());

      return true;
   }

   public ArrayList getPath() {
      return this.path;
   }

   public BlockPos getBlockPosFrom() {
      return this.blockPosFrom;
   }

   public void setBlockPosFrom(BlockPos blockPosFrom) {
      this.blockPosFrom = blockPosFrom;
   }

   public BlockPos getBlockPosTo() {
      return this.blockPosTo;
   }

   public void setBlockPosTo(BlockPos blockPosTo) {
      this.blockPosTo = blockPosTo;
   }
}
