/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.reflect.TypeToken
 *  cpw.mods.fml.common.FMLCommonHandler
 *  cpw.mods.fml.common.Loader
 *  cpw.mods.fml.common.MinecraftDummyContainer
 *  cpw.mods.fml.common.ModContainer
 *  cpw.mods.fml.common.eventhandler.Event
 *  cpw.mods.fml.common.eventhandler.EventBus
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.common.MinecraftForge
 */
package delta.hooks;

import com.google.common.reflect.TypeToken;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MinecraftDummyContainer;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import delta.Class159;
import delta.Class192;
import delta.Class55;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

public class FMLBusHook {
    private List<Object> marriage$ = new ArrayList<Object>();

    private void _hawaii(EventBus eventBus, Object object) throws ReflectiveOperationException {
        Class159 class159 = Class159._lover((Object)eventBus);
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap)class159._greene("listeners");
        Map map = (Map)class159._greene("listenerOwners");
        if (concurrentHashMap.containsKey(object)) {
            return;
        }
        MinecraftDummyContainer minecraftDummyContainer = Loader.instance().getMinecraftModContainer();
        map.put(object, minecraftDummyContainer);
        class159._glory("listenerOwners", map);
        Set set = TypeToken.of(object.getClass()).getTypes().rawTypes();
        Method[] arrmethod = object.getClass().getMethods();
        int n = arrmethod.length;
        block4: for (int i = 173 - 227 + 162 - 82 + -26; i < n; ++i) {
            Method method = arrmethod[i];
            for (Class class_ : set) {
                Class<?> class_2;
                block8: {
                    Class<?>[] arrclass;
                    block7: {
                        try {
                            Method method2 = class_.getDeclaredMethod(method.getName(), method.getParameterTypes());
                            if (!method2.isAnnotationPresent(SubscribeEvent.class)) continue;
                            arrclass = method.getParameterTypes();
                            if (arrclass.length == 230 - 235 + 26 - 20 + 0) break block7;
                            throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but requires " + arrclass.length + " arguments.  Event handler methods must require a single argument.");
                        }
                        catch (NoSuchMethodException noSuchMethodException) {
                            continue;
                        }
                    }
                    class_2 = arrclass[270 - 536 + 134 - 25 + 157];
                    if (Event.class.isAssignableFrom(class_2)) break block8;
                    throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + class_2);
                }
                Class[] arrclass = new Class[226 - 257 + 155 + -120];
                arrclass[238 - 365 + 321 - 280 + 86] = Class.class;
                arrclass[118 - 187 + 174 + -104] = Object.class;
                arrclass[34 - 67 + 35 + 0] = Method.class;
                arrclass[178 - 229 + 185 + -131] = ModContainer.class;
                Method method3 = class159._steady().getClass().getDeclaredMethod("register", arrclass);
                method3.setAccessible(104 - 110 + 91 - 31 + -53);
                Object[] arrobject = new Object[66 - 98 + 35 + 1];
                arrobject[184 - 355 + 334 - 94 + -69] = class_2;
                arrobject[173 - 242 + 189 + -119] = object;
                arrobject[160 - 296 + 199 - 55 + -6] = method;
                arrobject[186 - 211 + 69 - 2 + -39] = minecraftDummyContainer;
                method3.invoke(class159._steady(), arrobject);
                continue block4;
            }
        }
    }

    public void _campaign(Object object) {
        this.marriage$.add(object);
        try {
            this._hawaii(FMLCommonHandler.instance().bus(), object);
            this._hawaii(MinecraftForge.EVENT_BUS, object);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void _candle(EventBus eventBus) throws ReflectiveOperationException {
        if (!Class55._option()) {
            return;
        }
        Class159 class159 = Class159._lover((Object)eventBus);
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap)class159._greene("listeners");
        for (Object k : concurrentHashMap.keySet()) {
            String string = k.getClass().getName().toLowerCase();
            if (!string.contains(Class192._pattern())) continue;
            Method[] arrmethod = k.getClass().getDeclaredMethods();
            int n = arrmethod.length;
            for (int i = 90 - 120 + 88 + -58; i < n; ++i) {
                Method method = arrmethod[i];
                if (method.getParameterCount() != 112 - 215 + 199 - 13 + -82 || !method.getParameterTypes()[32 - 39 + 35 - 17 + -11].equals(GuiOpenEvent.class)) continue;
                eventBus.unregister(k);
            }
        }
    }

    public void _perfect() {
        for (Object object : this.marriage$) {
            FMLCommonHandler.instance().bus().unregister(object);
            MinecraftForge.EVENT_BUS.unregister(object);
        }
    }
}

