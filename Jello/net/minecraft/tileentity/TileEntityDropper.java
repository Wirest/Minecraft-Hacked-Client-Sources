package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser
{
    

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return this.hasCustomName() ? this.field_146020_a : "container.dropper";
    }

    public String getGuiID()
    {
        return "minecraft:dropper";
    }
}
