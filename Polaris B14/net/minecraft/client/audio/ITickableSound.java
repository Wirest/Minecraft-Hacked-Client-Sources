package net.minecraft.client.audio;

import net.minecraft.util.ITickable;

public abstract interface ITickableSound
  extends ISound, ITickable
{
  public abstract boolean isDonePlaying();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\ITickableSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */