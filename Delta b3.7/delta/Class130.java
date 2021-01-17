/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraft.launchwrapper.LaunchClassLoader
 *  net.minecraft.network.Packet
 *  org.apache.logging.log4j.LogManager
 */
package delta;

import delta.Class154;
import delta.Class67;
import delta.Class96;
import delta.Class97;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.network.Packet;
import org.apache.logging.log4j.LogManager;

public class Class130 {
    private static Class<?> estimate$;

    public static Boolean _schema(Object object, Boolean bl) {
        return Class96._tooth((Packet)object, bl);
    }

    public static Class<?> _music() {
        if (estimate$ == null) {
            LaunchClassLoader launchClassLoader = Launch.classLoader;
            URL uRL = launchClassLoader.findResource("loader_data.tg");
            if (uRL == null) {
                uRL = launchClassLoader.getResource("loader_data.tg");
            }
            String string = "";
            InputStream inputStream = uRL.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String string2 = "";
            while ((string2 = bufferedReader.readLine()) != null) {
                if (!string2.contains("externalWrapper: ")) continue;
                string = string2.substring("externalWrapper: ".length()).replace("\n", "");
            }
            try {
                bufferedReader.close();
                estimate$ = Launch.classLoader.findClass(string);
            }
            catch (IOException iOException) {
                LogManager.getLogger((String)"InternalWrapper").fatal("Couldn't get ExternalWrapper from loader_data.tg!");
                iOException.printStackTrace();
            }
            catch (ClassNotFoundException classNotFoundException) {
                LogManager.getLogger((String)"InternalWrapper").fatal("Couldn't find ExternalWrapper class!");
                classNotFoundException.printStackTrace();
            }
        }
        return estimate$;
    }

    public static Boolean _disney(Object object) {
        return Class96._relative(object);
    }

    public static void _bookings(Integer n) {
        Class97._valuable(n);
    }

    public static Boolean _matched(Boolean bl, Object object, Object object2, Integer n, Integer n2, Integer n3, Integer n4) {
        return Class154._banners(bl, object, object2, n, n2, n3, n4);
    }

    public static Boolean _rider(EntityPlayerSP entityPlayerSP, Boolean bl) {
        return Class96._clara(entityPlayerSP, bl);
    }

    private static void _testing() {
        Class130._rider(null, 120 - 135 + 47 - 45 + 13);
        Class130._disney(null);
        Class130._schema(null, 82 - 131 + 47 - 1 + 3);
        Class130._matched(134 - 176 + 150 + -108, null, null, 170 - 314 + 267 - 131 + 8, 262 - 414 + 349 - 121 + -76, 76 - 77 + 23 - 19 + -3, 89 - 160 + 53 + 18);
        Class130._visited("", "");
    }

    public static String _visited(String string, String string2) {
        return Class67._invite(string, string2);
    }
}

