package skyline.specc.extras.main;

import java.awt.Color;

public class OverlayUtil 
{
    public static int rainbow(int delay, float brightness, float sateration) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), brightness, sateration).getRGB();
        
    }
}
