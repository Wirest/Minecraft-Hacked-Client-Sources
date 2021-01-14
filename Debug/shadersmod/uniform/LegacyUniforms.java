package shadersmod.uniform;

import java.util.HashMap;
import java.util.Map;

public class LegacyUniforms
{
    private static Map<String, Number> map = new HashMap();
    private static Map<String, String> mapKeysX = new HashMap();
    private static Map<String, String> mapKeysY = new HashMap();
    private static Map<String, String> mapKeysZ = new HashMap();
    private static Map<String, String> mapKeysR = new HashMap();
    private static Map<String, String> mapKeysG = new HashMap();
    private static Map<String, String> mapKeysB = new HashMap();

    public static Number getNumber(String name)
    {
        return (Number)map.get(name);
    }

    public static void setFloat(String name, float val)
    {
        map.put(name, Float.valueOf(val));
    }

    public static void setInt(String name, int val)
    {
        map.put(name, Integer.valueOf(val));
    }

    public static void setIntXy(String name, int x, int y)
    {
        setInt(getCompoundKey(name, "x", mapKeysX), x);
        setInt(getCompoundKey(name, "y", mapKeysY), y);
    }

    public static void setFloatXyz(String name, float x, float y, float z)
    {
        setFloat(getCompoundKey(name, "x", mapKeysX), x);
        setFloat(getCompoundKey(name, "y", mapKeysY), y);
        setFloat(getCompoundKey(name, "z", mapKeysZ), z);
    }

    public static void setFloatRgb(String name, float x, float y, float z)
    {
        setFloat(getCompoundKey(name, "r", mapKeysR), x);
        setFloat(getCompoundKey(name, "g", mapKeysG), y);
        setFloat(getCompoundKey(name, "b", mapKeysB), z);
    }

    private static String getCompoundKey(String name, String suffix, Map<String, String> mapKeys)
    {
        String s = (String)mapKeys.get(name);

        if (s != null)
        {
            return s;
        }
        else
        {
            s = name + "." + suffix;
            mapKeys.put(name, s);
            return s;
        }
    }

    public static void reset()
    {
        map.clear();
    }
}
