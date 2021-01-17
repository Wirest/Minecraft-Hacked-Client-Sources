/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.api.types.IBasePlugin
 *  me.xtrm.atlaspluginloader.api.types.PluginInfo
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraft.launchwrapper.LaunchClassLoader
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package delta;

import delta.Class148;
import delta.Class159;
import delta.Class193;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import me.xtrm.atlaspluginloader.api.types.IBasePlugin;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@PluginInfo(name="Aion", version="5.0", author="xTrM_")
public class Class35
implements IBasePlugin {
    private static Class35 fight$;
    private List<String> happens$;
    private Logger ships$;

    public static Class35 zXeo() {
        return fight$;
    }

    public List<String> qG8F() {
        return this.happens$;
    }

    public Class35() {
        fight$ = this;
        this.happens$ = new ArrayList<String>();
        this.happens$.add("me.xtrm.");
        this.happens$.add("delta.");
        this.happens$.add("xdelta.");
        this.ships$ = LogManager.getLogger((String)"Aion");
        this.ships$.info("Bootstrap > Starting @ Aion " + this.getClass().getDeclaredAnnotation(PluginInfo.class).version());
        LaunchClassLoader launchClassLoader = Launch.classLoader;
        Class159 class159 = Class159._lover((Object)launchClassLoader);
        try {
            class159._glory("cachedClasses", new Class148((Map)class159._greene("cachedClasses")));
            class159._glory("classLoaderExceptions", new Class193((Set)class159._greene("classLoaderExceptions")));
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
        this.ships$.info("Hooked.");
    }
}

