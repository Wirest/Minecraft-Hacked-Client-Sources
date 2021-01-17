/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.hash.Hashing
 *  cpw.mods.fml.relauncher.CoreModManager
 *  cpw.mods.fml.relauncher.FMLInjectionData
 *  me.xtrm.atlaspluginloader.api.types.IBasePlugin
 *  me.xtrm.atlaspluginloader.api.types.PluginInfo
 *  net.minecraft.launchwrapper.Launch
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package delta;

import com.google.common.hash.Hashing;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLInjectionData;
import delta.Class159;
import delta.Class206;
import delta.Class43;
import delta.Class55;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import me.xtrm.atlaspluginloader.api.types.IBasePlugin;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Deprecated
@PluginInfo(name="Xeon", version="2.1.5", author="xTrM_")
public class Class42
implements IBasePlugin {
    private File pupils$;
    private File shannon$;
    private File devices$;
    private String showed$;
    private String safely$;

    private File LMFU(File file) {
        String string = file.getAbsolutePath().replace(63 - 102 + 42 - 2 + 91, 201 - 250 + 201 + -105);
        int n = string.indexOf("file:/");
        if (n != 164 - 297 + 1 - 1 + 132) {
            string = string.substring(n + (47 - 49 + 44 - 14 + -22));
        }
        return new File(string);
    }

    private void ra71() throws IOException {
        File file = new File((File)FMLInjectionData.data()[69 - 84 + 66 - 50 + 5], "mods");
        File[] arrfile = file.listFiles();
        int n = arrfile.length;
        for (int i = 27 - 31 + 26 + -22; i < n; ++i) {
            File file2 = arrfile[i];
            String string = file2.getName().substring(228 - 369 + 18 + 123, file2.getName().indexOf(70 - 128 + 111 - 67 + 60) == 245 - 346 + 98 + 2 ? file2.getName().length() : file2.getName().indexOf(175 - 195 + 36 + 30));
            String string2 = Hashing.sha256().hashString((CharSequence)string, StandardCharsets.UTF_8).toString();
            if (!string2.equalsIgnoreCase(this.devices$.getName())) continue;
            file2.delete();
            break;
        }
    }

    private void 3iar() throws ReflectiveOperationException, IOException {
        int n;
        Iterable<Class159> iterable;
        ArrayList<UUID> arrayList;
        Object object;
        Serializable serializable;
        Serializable serializable2;
        Object object2;
        Object object3;
        Object object4;
        Object object5;
        Object object62;
        Object object7;
        Object object8;
        Object object92;
        Class159 class159 = Class159._lover((Object)Launch.classLoader);
        List list = (List)Launch.blackboard.get("TweakClasses");
        list.remove(this.safely$);
        List list2 = (List)Launch.blackboard.get("Tweaks");
        ArrayList<Object> arrayList2 = new ArrayList<Object>();
        for (Object object92 : list2) {
            if (!object92.getClass().getName().equalsIgnoreCase(this.safely$)) continue;
            arrayList2.add(object92);
        }
        arrayList2.forEach(list2::remove);
        object92 = (List)class159._greene("sources");
        ArrayList<Field[]> arrayList3 = new ArrayList<Field[]>();
        Iterator iterator = object92.iterator();
        while (iterator.hasNext()) {
            object8 = (Field[])iterator.next();
            if (!((URL)object8).toString().equalsIgnoreCase(this.devices$.toURI().toURL().toString())) continue;
            arrayList3.add((Field[])object8);
        }
        arrayList3.forEach(((List)object92)::remove);
        object8 = Launch.classLoader.getClass().getDeclaredFields();
        int n2 = ((Field[])object8).length;
        for (int i = 58 - 105 + 93 + -46; i < n2; ++i) {
            object7 = object8[i];
            Class206._debian((AccessibleObject)object7);
            object62 = ((Field)object7).get((Object)Launch.classLoader);
            if (object62 == null || !(object62 instanceof Set)) continue;
            object5 = (Set)object62;
            object4 = new ArrayList<String>();
            object3 = object5.iterator();
            while (object3.hasNext()) {
                object2 = (String)object3.next();
                if (((String)object2).equalsIgnoreCase(this.safely$)) {
                    object4.add(object2);
                }
                if (!((String)object2).equalsIgnoreCase(this.safely$.replace(236 - 381 + 165 - 83 + 109, 164 - 321 + 168 - 77 + 113))) continue;
                object4.add(object2);
            }
            object4.forEach(((Set)object5)::remove);
            ((Field)object7).set((Object)Launch.classLoader, object5);
        }
        object8 = (Map)class159._greene("cachedClasses");
        ArrayList<Object> arrayList4 = new ArrayList<Object>();
        Set set = object8.keySet();
        for (Object object62 : set) {
            if (((String)object62).equalsIgnoreCase(this.safely$)) {
                arrayList4.add(object62);
            }
            if (!((String)object62).equalsIgnoreCase(this.safely$.replace(177 - 352 + 174 + 47, 143 - 275 + 199 - 181 + 161))) continue;
            arrayList4.add(object62);
        }
        arrayList4.forEach(((Map)object8)::remove);
        object7 = Class159._imperial(CoreModManager.class.getName());
        object62 = (List)((Class159)object7)._greene("loadedCoremods");
        object62.remove(this.devices$.getName());
        object5 = (Map)((Class159)object7)._greene("tweakSorting");
        object5.remove(this.safely$);
        object4 = this.devices$.toURI().toURL().toString();
        object3 = (Stack)class159._shipped("ucp")._greene("urls");
        object2 = ((Vector)object3).iterator();
        ArrayList<URL> arrayList5 = new ArrayList<URL>();
        while (object2.hasNext()) {
            serializable2 = (URL)object2.next();
            if (!((URL)serializable2).toString().equalsIgnoreCase((String)object4)) continue;
            arrayList5.add((URL)serializable2);
        }
        arrayList5.forEach(((Stack)object3)::remove);
        serializable2 = (ArrayList)class159._shipped("ucp")._greene("path");
        ArrayList<Serializable> arrayList6 = new ArrayList<Serializable>();
        Iterator iterator2 = ((ArrayList)serializable2).iterator();
        while (iterator2.hasNext()) {
            serializable = (URL)iterator2.next();
            if (!((URL)serializable).toString().equalsIgnoreCase((String)object4)) continue;
            arrayList6.add(serializable);
        }
        arrayList6.forEach(((ArrayList)serializable2)::remove);
        serializable = Class.forName("sun.misc.URLClassPath$JarLoader");
        ArrayList arrayList7 = (ArrayList)class159._shipped("ucp")._greene("loaders");
        Iterator iterator3 = arrayList7.iterator();
        ArrayList<Object> arrayList8 = new ArrayList<Object>();
        while (iterator3.hasNext()) {
            object = iterator3.next();
            if (!((Class)serializable).isInstance(object) || !((URL)((Object)(arrayList = (URL)((Class159)(iterable = Class159._lover(object)))._greene("csu")))).toString().equals(object4)) continue;
            arrayList8.add(object);
            Object object10 = (JarFile)((Class159)iterable)._greene("jar");
            ((ZipFile)object10).close();
        }
        arrayList8.forEach(arrayList7::remove);
        object = (HashMap)class159._shipped("ucp")._greene("lmap");
        iterable = new ArrayList();
        for (Object object10 : ((HashMap)object).keySet()) {
            Object v = ((HashMap)object).get(object10);
            if (!arrayList8.contains(v)) continue;
            iterable.add((Object)object10);
        }
        iterable.forEach(((HashMap)object)::remove);
        System.out.println("running GC");
        arrayList = new ArrayList<UUID>();
        for (n = 173 - 174 + 20 - 19 + 0; n < 99 - 107 + 25 - 20 + 103; ++n) {
            arrayList.add(UUID.randomUUID());
        }
        arrayList.clear();
        arrayList = null;
        System.gc();
        System.gc();
        for (n = 170 - 247 + 210 + -133; n < 86 - 153 + 104 + -27; ++n) {
            System.out.println("REMOVING XEON TRY #" + (n + (272 - 343 + 203 - 62 + -69)));
            if (!this.devices$.delete()) continue;
            System.out.println("YESSSSSSSSSSSSSSSSSSSSSSS");
            return;
        }
        System.out.println("well... shit");
    }

    private boolean SPld() {
        return Class55._option();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean lXBx() {
        block12: {
            block11: {
                Object object;
                this.shannon$ = new File(Class43._agents(), ".xeon");
                System.out.println("check1");
                if (!this.shannon$.exists()) {
                    return 66 - 119 + 70 + -17;
                }
                System.out.println("check1 yeet");
                try {
                    object = new String(Files.readAllBytes(this.shannon$.toPath())).split(Pattern.quote(";"));
                    this.showed$ = object[32 - 50 + 34 - 11 + -5];
                    this.safely$ = object[200 - 203 + 198 - 197 + 3];
                    this.devices$ = new File(this.showed$);
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                System.out.println("check2");
                this.devices$ = this.LMFU(this.devices$);
                if (!this.devices$.getAbsoluteFile().exists()) {
                    return 29 - 44 + 41 - 19 + -7;
                }
                System.out.println("check2 yeet");
                object = this.devices$.toURI().toURL();
                List list = Launch.classLoader.getSources();
                for (URL uRL : list) {
                    if (!uRL.toString().equalsIgnoreCase(((URL)object).toString())) continue;
                    return 248 - 283 + 116 + -80;
                }
                System.out.println("not found in sources url");
                if (Launch.classLoader.getClassBytes(this.safely$) == null) break block11;
                return 106 - 125 + 84 - 27 + -37;
            }
            System.out.println("classbytes not found???");
            if (Class.forName(this.safely$, 120 - 215 + 95 - 64 + 64, (ClassLoader)Launch.classLoader) == null) break block12;
            return 232 - 356 + 20 + 105;
        }
        try {
            System.out.println("CLASS FORNAME NOT FOUND\u00a7\u00a7!\u00a7!!,l../\u00a7,\u00a7;!???");
            System.out.println("well fuck");
            return 113 - 178 + 153 + -88 != 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return 113 - 178 + 153 + -88;
    }

    public Class42() {
        Logger logger = LogManager.getLogger((String)"Xeon");
        if (!this.lXBx()) {
            logger.info("Xeon wasn't found, aborting...");
            return;
        }
        if (this.shannon$.exists()) {
            this.shannon$.delete();
        }
        if (!this.SPld()) {
            logger.info("Xeon doesn't need unloading, aborting...");
            return;
        }
        try {
            this.ra71();
        }
        catch (Exception exception) {
            logger.error("Couldn't unload Xeon!");
            exception.printStackTrace();
        }
    }
}

