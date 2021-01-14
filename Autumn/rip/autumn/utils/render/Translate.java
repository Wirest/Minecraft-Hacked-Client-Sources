package rip.autumn.utils.render;

public final class Translate {
   private double x;
   private double y;

   public Translate(float x, float y) {
      this.x = (double)x;
      this.y = (double)y;
   }

   public final void interpolate(double targetX, double targetY, double smoothing) {
      this.x = AnimationUtils.animate(targetX, this.x, smoothing);
      this.y = AnimationUtils.animate(targetY, this.y, smoothing);
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }
}
