package net.minecraft.block.material;

public class MaterialLogic extends Material
{
    

    public MaterialLogic(MapColor p_i2112_1_)
    {
        super(p_i2112_1_);
        this.setAdventureModeExempt();
    }

    public boolean isSolid()
    {
        return false;
    }

    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    public boolean blocksLight()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }
}
