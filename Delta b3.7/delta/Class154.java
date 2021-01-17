/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Module
 */
package delta;

import delta.module.modules.Xray;
import me.xtrm.delta.api.module.Module;

public class Class154 {
    public static Module climate$ = null;

    public static boolean _banners(boolean bl, Object object, Object object2, int n, int n2, int n3, int n4) {
        if (climate$ == null) {
            return bl;
        }
        if (!climate$.isEnabled()) {
            return bl;
        }
        return Xray.ONAX(bl, object, object2, n, n2, n3, n4);
    }
}

