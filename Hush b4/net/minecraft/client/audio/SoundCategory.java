// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;

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
    
    private static final Map<String, SoundCategory> NAME_CATEGORY_MAP;
    private static final Map<Integer, SoundCategory> ID_CATEGORY_MAP;
    private final String categoryName;
    private final int categoryId;
    
    static {
        NAME_CATEGORY_MAP = Maps.newHashMap();
        ID_CATEGORY_MAP = Maps.newHashMap();
        SoundCategory[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final SoundCategory soundcategory = values[i];
            if (SoundCategory.NAME_CATEGORY_MAP.containsKey(soundcategory.getCategoryName()) || SoundCategory.ID_CATEGORY_MAP.containsKey(soundcategory.getCategoryId())) {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + soundcategory);
            }
            SoundCategory.NAME_CATEGORY_MAP.put(soundcategory.getCategoryName(), soundcategory);
            SoundCategory.ID_CATEGORY_MAP.put(soundcategory.getCategoryId(), soundcategory);
        }
    }
    
    private SoundCategory(final String name2, final int ordinal, final String name, final int id) {
        this.categoryName = name;
        this.categoryId = id;
    }
    
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public int getCategoryId() {
        return this.categoryId;
    }
    
    public static SoundCategory getCategory(final String name) {
        return SoundCategory.NAME_CATEGORY_MAP.get(name);
    }
}
