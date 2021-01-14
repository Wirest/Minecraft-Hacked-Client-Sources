package net.minecraft.world.storage;

import java.util.List;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
    String func_154333_a();

    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String var1, boolean var2);

    List getSaveList() throws AnvilConverterException;

    void flushCache();

    /**
     * gets the world info
     */
    WorldInfo getWorldInfo(String var1);

    boolean func_154335_d(String var1);

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    boolean deleteWorldDirectory(String var1);

    /**
     * @args: Takes two arguments - first the name of the directory containing the world and second the new name for
     * that world. @desc: Renames the world by storing the new name in level.dat. It does *not* rename the directory
     * containing the world data.
     */
    void renameWorld(String var1, String var2);

    boolean func_154334_a(String var1);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    boolean isOldMapFormat(String var1);

    /**
     * converts the map to mcRegion
     */
    boolean convertMapFormat(String var1, IProgressUpdate var2);

    /**
     * Return whether the given world can be loaded.
     */
    boolean canLoadWorld(String var1);
}
