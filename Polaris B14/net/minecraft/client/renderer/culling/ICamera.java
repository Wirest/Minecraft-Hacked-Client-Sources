package net.minecraft.client.renderer.culling;

import net.minecraft.util.AxisAlignedBB;

public abstract interface ICamera
{
  public abstract boolean isBoundingBoxInFrustum(AxisAlignedBB paramAxisAlignedBB);
  
  public abstract void setPosition(double paramDouble1, double paramDouble2, double paramDouble3);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\culling\ICamera.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */