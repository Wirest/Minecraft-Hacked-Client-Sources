package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser
{
    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.dropper";
    }

    @Override
	public String getGuiID()
    {
        return "minecraft:dropper";
    }
}
