package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializer;

public abstract interface IMetadataSectionSerializer<T extends IMetadataSection>
  extends JsonDeserializer<T>
{
  public abstract String getSectionName();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\data\IMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */