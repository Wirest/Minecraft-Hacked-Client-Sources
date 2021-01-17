/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLCommonHandler
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  delta.OVYt$968L
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Timer
 */
package delta.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import delta.OVYt;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class Wrapper {
    public static Timer timer;
    public static boolean thong$;
    private static String[] jamaica$;
    public static Minecraft mc;

    private static void _cents() {
        jamaica$ = new String[]{"\u6722\u673f\u673b\u6733\u6724", "\ue3ff\ue3f0\ue3fc\ue3f5\ue3fd\ue3c6\ue3ae\ue3a8\ue3ad\ue3ab\ue3a1\ue3c6\ue3cd"};
    }

    public static void _occurs() {
        FMLCommonHandler.instance().exitJava(236 - 418 + 37 + 145, 25 - 30 + 3 - 2 + 5);
    }

    static {
        Wrapper._cents();
        mc = Minecraft.getMinecraft();
        String[] arrstring = new String[185 - 294 + 255 + -144];
        arrstring[77 - 144 + 71 + -4] = OVYt.968L.FS1x((String)jamaica$[0], (int)-993433770);
        arrstring[230 - 295 + 33 + 33] = OVYt.968L.FS1x((String)jamaica$[1], (int)1617748889);
        timer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, (Object)Minecraft.getMinecraft(), (String[])arrstring);
        thong$ = 108 - 185 + 179 - 161 + 59;
    }
}

