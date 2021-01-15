package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    @Override
	public String getDimensionName()
    {
        return "Overworld";
    }

    @Override
	public String getInternalNameSuffix()
    {
        return "";
    }
}
