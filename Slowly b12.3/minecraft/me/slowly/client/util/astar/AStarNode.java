package me.slowly.client.util.astar;

public class AStarNode {
   private double x;
   private double y;
   private double z;
   private double heuristic;
   private AStarNode parent;

   public AStarNode(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public AStarNode getParent() {
      return this.parent;
   }

   public void setParent(AStarNode parent) {
      this.parent = parent;
   }

   public double getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = (double)x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(int y) {
      this.z = (double)y;
   }

   public double getHeuristic() {
      return this.heuristic;
   }

   public void setHeuristic(double heuristic) {
      this.heuristic = heuristic;
   }
}
