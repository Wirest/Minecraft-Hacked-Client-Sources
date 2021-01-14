package optifine;

import net.minecraft.world.chunk.Chunk;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChunkUtils {
    private static Field fieldHasEntities = null;
    private static boolean fieldHasEntitiesMissing = false;

    public static boolean hasEntities(Chunk paramChunk) {
        if (fieldHasEntities == null) {
            if (fieldHasEntitiesMissing) {
                return true;
            }
            fieldHasEntities = findFieldHasEntities(paramChunk);
            if (fieldHasEntities == null) {
                fieldHasEntitiesMissing = true;
                return true;
            }
        }
        try {
            return fieldHasEntities.getBoolean(paramChunk);
        } catch (Exception localException) {
            Config.warn("Error calling Chunk.hasEntities");
            Config.warn(localException.getClass().getName() + " " + localException.getMessage());
            fieldHasEntitiesMissing = true;
        }
        return true;
    }

    private static Field findFieldHasEntities(Chunk paramChunk) {
        try {
            ArrayList localArrayList1 = new ArrayList();
            ArrayList localArrayList2 = new ArrayList();
            Field[] arrayOfField = Chunk.class.getDeclaredFields();
            for (int i = 0; i < arrayOfField.length; i++) {
                localObject1 = arrayOfField[i];
                if (((Field) localObject1).getType() == Boolean.TYPE) {
                    ((Field) localObject1).setAccessible(true);
                    localArrayList1.add(localObject1);
                    localArrayList2.add(((Field) localObject1).get(paramChunk));
                }
            }
            paramChunk.setHasEntities(false);
            ArrayList localArrayList3 = new ArrayList();
            Object localObject1 = localArrayList1.iterator();
            while (((Iterator) localObject1).hasNext()) {
                localObject2 = ((Iterator) localObject1).next();
                localArrayList3.add(((Field) localObject2).get(paramChunk));
            }
            paramChunk.setHasEntities(true);
            localObject1 = new ArrayList();
            Object localObject2 = localArrayList1.iterator();
            while (((Iterator) localObject2).hasNext()) {
                Object localObject3 = ((Iterator) localObject2).next();
                ((List) localObject1).add(((Field) localObject3).get(paramChunk));
            }
            localObject2 = new ArrayList();
            for (int j = 0; j < localArrayList1.size(); j++) {
                Field localField2 = (Field) localArrayList1.get(j);
                Boolean localBoolean1 = (Boolean) localArrayList3.get(j);
                Boolean localBoolean2 = (Boolean) ((List) localObject1).get(j);
                if ((!localBoolean1.booleanValue()) && (localBoolean2.booleanValue())) {
                    ((List) localObject2).add(localField2);
                    Boolean localBoolean3 = (Boolean) localArrayList2.get(j);
                    localField2.set(paramChunk, localBoolean3);
                }
            }
            if (((List) localObject2).size() == 1) {
                Field localField1 = (Field) ((List) localObject2).get(0);
                return localField1;
            }
        } catch (Exception localException) {
            Config.warn(localException.getClass().getName() + " " + localException.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return null;
    }
}




