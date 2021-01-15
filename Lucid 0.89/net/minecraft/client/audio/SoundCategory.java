package net.minecraft.client.audio;

import java.util.Map;

import com.google.common.collect.Maps;

public enum SoundCategory
{
    MASTER("MASTER", 0, "master", 0),
    MUSIC("MUSIC", 1, "music", 1),
    RECORDS("RECORDS", 2, "record", 2),
    WEATHER("WEATHER", 3, "weather", 3),
    BLOCKS("BLOCKS", 4, "block", 4),
    MOBS("MOBS", 5, "hostile", 5),
    ANIMALS("ANIMALS", 6, "neutral", 6),
    PLAYERS("PLAYERS", 7, "player", 7),
    AMBIENT("AMBIENT", 8, "ambient", 8);
    private static final Map NAME_CATEGORY_MAP = Maps.newHashMap();
    private static final Map ID_CATEGORY_MAP = Maps.newHashMap();
    private final String categoryName;
    private final int categoryId; 

    private SoundCategory(String p_i45126_1_, int p_i45126_2_, String name, int id)
    {
        this.categoryName = name;
        this.categoryId = id;
    }

    public String getCategoryName()
    {
        return this.categoryName;
    }

    public int getCategoryId()
    {
        return this.categoryId;
    }

    public static SoundCategory getCategory(String name)
    {
        return (SoundCategory)NAME_CATEGORY_MAP.get(name);
    }

    static {
        SoundCategory[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            SoundCategory var3 = var0[var2];

            if (NAME_CATEGORY_MAP.containsKey(var3.getCategoryName()) || ID_CATEGORY_MAP.containsKey(Integer.valueOf(var3.getCategoryId())))
            {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + var3);
            }

            NAME_CATEGORY_MAP.put(var3.getCategoryName(), var3);
            ID_CATEGORY_MAP.put(Integer.valueOf(var3.getCategoryId()), var3);
        }
    }
}
