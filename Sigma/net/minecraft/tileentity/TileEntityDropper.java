package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {
    private static final String __OBFID = "CL_00000353";

    /**
     * Gets the name of this command sender (usually username, but possibly
     * "Rcon")
     */
    @Override
    public String getName() {
        return hasCustomName() ? field_146020_a : "container.dropper";
    }

    @Override
    public String getGuiID() {
        return "minecraft:dropper";
    }
}
