package net.optifine.util;

import java.util.HashMap;
import java.util.Map;

public class TimedEvent
{
    private static Map<String, Long> mapEventTimes = new HashMap();

    public static boolean isActive(String name, long timeIntervalMs)
    {
        synchronized (mapEventTimes)
        {
            long i = System.currentTimeMillis();
            Long olong = mapEventTimes.get(name);

            if (olong == null)
            {
                olong = i;
                mapEventTimes.put(name, olong);
            }

            long j = olong;

            if (i < j + timeIntervalMs)
            {
                return false;
            }
            else
            {
                mapEventTimes.put(name, i);
                return true;
            }
        }
    }
}
