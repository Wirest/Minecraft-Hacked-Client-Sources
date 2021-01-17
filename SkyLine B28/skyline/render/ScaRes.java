package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render;

import net.minecraft.client.Mineman;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;

public class ScaRes
{
  private final double scaledWidthD;
  private final double scaledHeightD;
  private int scaledWidth;
  private int scaledHeight;
  private int scaleFactor;
  
  public ScaRes(Mineman minecraftClient)
  {
    this.scaledWidth = minecraftClient.displayWidth;
    this.scaledHeight = minecraftClient.displayHeight;
    this.scaleFactor = 1;
    boolean flag = minecraftClient.isUnicode();
    int i = minecraftClient.gameSettings.guiScale;
    if (i == 0) {
      i = 1000;
    }
    while ((this.scaleFactor < i) && (this.scaledWidth / (this.scaleFactor + 1) >= 320) && (this.scaledHeight / (this.scaleFactor + 1) >= 240)) {
      this.scaleFactor += 1;
    }
    if ((flag) && (this.scaleFactor % 2 != 0) && (this.scaleFactor != 1)) {
      this.scaleFactor -= 1;
    }
    this.scaledWidthD = (this.scaledWidth / this.scaleFactor);
    this.scaledHeightD = (this.scaledHeight / this.scaleFactor);
    this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
    this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
  }
  
  public int getScaledWidth()
  {
    return this.scaledWidth;
  }
  
  public int getScaledHeight()
  {
    return this.scaledHeight;
  }
  
  public double getScaledWidth_double()
  {
    return this.scaledWidthD;
  }
  
  public double getScaledHeight_double()
  {
    return this.scaledHeightD;
  }
  
  public int getScaleFactor()
  {
    return this.scaleFactor;
  }
}
