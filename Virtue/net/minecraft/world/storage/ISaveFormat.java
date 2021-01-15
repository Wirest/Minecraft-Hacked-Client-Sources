package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat
{
    String func_154333_a();

    /**
     * Returns back a loader for the specified save directory
     */
    ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_);

    List getSaveList() throws AnvilConverterException;

    void flushCache();

    /**
     * gets the world info
     */
    WorldInfo getWorldInfo(String p_75803_1_);

    boolean func_154335_d(String p_154335_1_);

    /**
     * @args: Takes one argument - the name of the directory of the world to delete. @desc: Delete the world by deleting
     * the associated directory recursively.
     */
    boolean deleteWorldDirectory(String p_75802_1_);

    /**
     * @args: Takes two arguments - first the name of the directory containing the world and second the new name for
     * that world. @desc: Renames the world by storing the new name in level.dat. It does *not* rename the directory
     * containing the world data.
     */
    void renameWorld(String p_75806_1_, String p_75806_2_);

    boolean func_154334_a(String p_154334_1_);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    boolean isOldMapFormat(String p_75801_1_);

    /**
     * converts the map to mcRegion
     */
    boolean convertMapFormat(String p_75805_1_, IProgressUpdate p_75805_2_);

    /**
     * Return whether the given world can be loaded.
     */
    boolean canLoadWorld(String p_90033_1_);
}
