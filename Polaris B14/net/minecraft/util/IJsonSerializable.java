package net.minecraft.util;

import com.google.gson.JsonElement;

public abstract interface IJsonSerializable
{
  public abstract void fromJson(JsonElement paramJsonElement);
  
  public abstract JsonElement getSerializableElement();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IJsonSerializable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */