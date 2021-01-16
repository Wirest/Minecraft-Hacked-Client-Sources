package optifine;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
    private static Field fieldHasEntities = null;
    private static boolean fieldHasEntitiesMissing = false;

    public static boolean hasEntities(Chunk chunk)
    {
        if (fieldHasEntities == null)
        {
            if (fieldHasEntitiesMissing)
            {
                return true;
            }

            fieldHasEntities = findFieldHasEntities(chunk);

            if (fieldHasEntities == null)
            {
                fieldHasEntitiesMissing = true;
                return true;
            }
        }

        try
        {
            return fieldHasEntities.getBoolean(chunk);
        }
        catch (Exception var2)
        {
            Config.warn("Error calling Chunk.hasEntities");
            Config.warn(var2.getClass().getName() + " " + var2.getMessage());
            fieldHasEntitiesMissing = true;
            return true;
        }
    }

    private static Field findFieldHasEntities(Chunk chunk)
    {
        try
        {
            ArrayList e = new ArrayList();
            ArrayList listBoolValuesPre = new ArrayList();
            Field[] fields = Chunk.class.getDeclaredFields();

            for (int listBoolValuesFalse = 0; listBoolValuesFalse < fields.length; ++listBoolValuesFalse)
            {
                Field listBoolValuesTrue = fields[listBoolValuesFalse];

                if (listBoolValuesTrue.getType() == Boolean.TYPE)
                {
                    listBoolValuesTrue.setAccessible(true);
                    e.add(listBoolValuesTrue);
                    listBoolValuesPre.add(listBoolValuesTrue.get(chunk));
                }
            }

            chunk.setHasEntities(false);
            ArrayList var13 = new ArrayList();
            Iterator var14 = e.iterator();

            while (var14.hasNext())
            {
                Field listMatchingFields = (Field)var14.next();
                var13.add(listMatchingFields.get(chunk));
            }

            chunk.setHasEntities(true);
            ArrayList var15 = new ArrayList();
            Iterator var16 = e.iterator();
            Field field;

            while (var16.hasNext())
            {
                field = (Field)var16.next();
                var15.add(field.get(chunk));
            }

            ArrayList var17 = new ArrayList();

            for (int var18 = 0; var18 < e.size(); ++var18)
            {
                Field field1 = (Field)e.get(var18);
                Boolean valFalse = (Boolean)var13.get(var18);
                Boolean valTrue = (Boolean)var15.get(var18);

                if (!valFalse.booleanValue() && valTrue.booleanValue())
                {
                    var17.add(field1);
                    Boolean valPre = (Boolean)listBoolValuesPre.get(var18);
                    field1.set(chunk, valPre);
                }
            }

            if (var17.size() == 1)
            {
                field = (Field)var17.get(0);
                return field;
            }
        }
        catch (Exception var12)
        {
            Config.warn(var12.getClass().getName() + " " + var12.getMessage());
        }

        Config.warn("Error finding Chunk.hasEntities");
        return null;
    }
}
