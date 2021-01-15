package jaco.mp3.resources;

public interface Control {
   void start();

   void stop();

   boolean isPlaying();

   void pause();

   boolean isRandomAccess();

   double getPosition();

   void setPosition(double var1);
}
