package net.minecraft.block;

public class BlockYellowFlower extends BlockFlower
{
    /**
     * Get the Type of this flower (Yellow/Red)
     */
    @Override
	public BlockFlower.EnumFlowerColor getBlockType()
    {
        return BlockFlower.EnumFlowerColor.YELLOW;
    }
}
