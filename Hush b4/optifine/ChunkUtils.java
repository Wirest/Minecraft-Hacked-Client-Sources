// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;
import java.lang.reflect.Field;

public class ChunkUtils
{
    private static Field fieldHasEntities;
    private static boolean fieldHasEntitiesMissing;
    
    static {
        ChunkUtils.fieldHasEntities = null;
        ChunkUtils.fieldHasEntitiesMissing = false;
    }
    
    public static boolean hasEntities(final Chunk p_hasEntities_0_) {
        if (ChunkUtils.fieldHasEntities == null) {
            if (ChunkUtils.fieldHasEntitiesMissing) {
                return true;
            }
            ChunkUtils.fieldHasEntities = findFieldHasEntities(p_hasEntities_0_);
            if (ChunkUtils.fieldHasEntities == null) {
                return ChunkUtils.fieldHasEntitiesMissing = true;
            }
        }
        try {
            return ChunkUtils.fieldHasEntities.getBoolean(p_hasEntities_0_);
        }
        catch (Exception exception) {
            Config.warn("Error calling Chunk.hasEntities");
            Config.warn(String.valueOf(exception.getClass().getName()) + " " + exception.getMessage());
            return ChunkUtils.fieldHasEntitiesMissing = true;
        }
    }
    
    private static Field findFieldHasEntities(final Chunk p_findFieldHasEntities_0_) {
        try {
            final List list = new ArrayList();
            final List list2 = new ArrayList();
            final Field[] afield = Chunk.class.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                final Field field = afield[i];
                if (field.getType() == Boolean.TYPE) {
                    field.setAccessible(true);
                    list.add(field);
                    list2.add(field.get(p_findFieldHasEntities_0_));
                }
            }
            p_findFieldHasEntities_0_.setHasEntities(false);
            final List list3 = new ArrayList();
            for (final Object field2 : list) {
                list3.add(((Field)field2).get(p_findFieldHasEntities_0_));
            }
            p_findFieldHasEntities_0_.setHasEntities(true);
            final List list4 = new ArrayList();
            for (final Object field3 : list) {
                list4.add(((Field)field3).get(p_findFieldHasEntities_0_));
            }
            final List list5 = new ArrayList();
            for (int j = 0; j < list.size(); ++j) {
                final Field field4 = list.get(j);
                final Boolean obool = list3.get(j);
                final Boolean obool2 = list4.get(j);
                if (!obool && obool2) {
                    list5.add(field4);
                    final Boolean obool3 = list2.get(j);
                    field4.set(p_findFieldHasEntities_0_, obool3);
                }
            }
            if (list5.size() == 1) {
                final Field field5 = list5.get(0);
                return field5;
            }
        }
        catch (Exception exception) {
            Config.warn(String.valueOf(exception.getClass().getName()) + " " + exception.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return null;
    }
}
