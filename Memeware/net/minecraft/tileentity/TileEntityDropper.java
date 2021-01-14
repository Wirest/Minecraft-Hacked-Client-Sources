package net.minecraft.tileentity;

public class TileEntityDropper extends TileEntityDispenser {
    private static final String __OBFID = "CL_00000353";

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return this.hasCustomName() ? this.field_146020_a : "container.dropper";
    }

    public String getGuiID() {
        return "minecraft:dropper";
    }
}
