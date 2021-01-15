package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract interface IStateMapper
{
  public abstract Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block paramBlock);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\statemap\IStateMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */