package net.minecraft.entity.ai.attributes;

public abstract interface IAttribute
{
  public abstract String getAttributeUnlocalizedName();
  
  public abstract double clampValue(double paramDouble);
  
  public abstract double getDefaultValue();
  
  public abstract boolean getShouldWatch();
  
  public abstract IAttribute func_180372_d();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\IAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */