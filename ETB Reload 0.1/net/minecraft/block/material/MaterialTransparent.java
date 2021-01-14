package net.minecraft.block.material;

public class MaterialTransparent extends Material
{
    public MaterialTransparent(MapColor color)
    {
        super(color);
        this.setReplaceable();
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    @Override
	public boolean isSolid()
    {
        return false;
    }

    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    @Override
	public boolean blocksLight()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    @Override
	public boolean blocksMovement()
    {
        return false;
    }
}
