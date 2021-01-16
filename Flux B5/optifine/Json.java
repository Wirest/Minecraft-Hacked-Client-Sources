package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Json
{
    public static float getFloat(JsonObject obj, String field, float def)
    {
        JsonElement elem = obj.get(field);
        return elem == null ? def : elem.getAsFloat();
    }

    public static boolean getBoolean(JsonObject obj, String field, boolean def)
    {
        JsonElement elem = obj.get(field);
        return elem == null ? def : elem.getAsBoolean();
    }

    public static String getString(JsonObject jsonObj, String field)
    {
        return getString(jsonObj, field, (String)null);
    }

    public static String getString(JsonObject jsonObj, String field, String def)
    {
        JsonElement jsonElement = jsonObj.get(field);
        return jsonElement == null ? def : jsonElement.getAsString();
    }

    public static float[] parseFloatArray(JsonElement jsonElement, int len)
    {
        return parseFloatArray(jsonElement, len, (float[])null);
    }

    public static float[] parseFloatArray(JsonElement jsonElement, int len, float[] def)
    {
        if (jsonElement == null)
        {
            return def;
        }
        else
        {
            JsonArray arr = jsonElement.getAsJsonArray();

            if (arr.size() != len)
            {
                throw new JsonParseException("Wrong array length: " + arr.size() + ", should be: " + len + ", array: " + arr);
            }
            else
            {
                float[] floatArr = new float[arr.size()];

                for (int i = 0; i < floatArr.length; ++i)
                {
                    floatArr[i] = arr.get(i).getAsFloat();
                }

                return floatArr;
            }
        }
    }

    public static int[] parseIntArray(JsonElement jsonElement, int len)
    {
        return parseIntArray(jsonElement, len, (int[])null);
    }

    public static int[] parseIntArray(JsonElement jsonElement, int len, int[] def)
    {
        if (jsonElement == null)
        {
            return def;
        }
        else
        {
            JsonArray arr = jsonElement.getAsJsonArray();

            if (arr.size() != len)
            {
                throw new JsonParseException("Wrong array length: " + arr.size() + ", should be: " + len + ", array: " + arr);
            }
            else
            {
                int[] intArr = new int[arr.size()];

                for (int i = 0; i < intArr.length; ++i)
                {
                    intArr[i] = arr.get(i).getAsInt();
                }

                return intArr;
            }
        }
    }
}
