package shadersmod.uniform;

import java.util.HashMap;
import java.util.Map;

import optifine.SmoothFloat;

public class Smoother
{
    private static Map<Integer, SmoothFloat> mapSmoothValues = new HashMap();

    public static float getSmoothValue(int id, float value, float timeFadeUpSec, float timeFadeDownSec)
    {
        synchronized (mapSmoothValues)
        {
            Integer integer = Integer.valueOf(id);
            SmoothFloat smoothfloat = (SmoothFloat)mapSmoothValues.get(integer);

            if (smoothfloat == null)
            {
                smoothfloat = new SmoothFloat(value, timeFadeUpSec, timeFadeDownSec);
                mapSmoothValues.put(integer, smoothfloat);
            }

            float f = smoothfloat.getSmoothValue(value);
            return f;
        }
    }

    public static void reset()
    {
        synchronized (mapSmoothValues)
        {
            mapSmoothValues.clear();
        }
    }
}
