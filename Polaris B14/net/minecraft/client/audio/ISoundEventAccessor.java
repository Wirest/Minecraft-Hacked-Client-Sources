package net.minecraft.client.audio;

public abstract interface ISoundEventAccessor<T>
{
  public abstract int getWeight();
  
  public abstract T cloneEntry();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\ISoundEventAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */