/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta;

import delta.Class11;
import delta.Class22;
import delta.Class39;
import delta.Class54;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.ScaledResolution;

public class Class7 {
    private static final int kenny$ = 50;
    private final int spencer$;
    private final Set<Class11> quizzes$;
    private final List<Class54> gender$ = new ArrayList<Class54>();

    public void _camps() {
        if (this.gender$.size() < this.spencer$) {
            this.gender$.add(new Class39(2.0f));
        }
        for (Class54 class54 : this.gender$) {
            Class11 class11 = this._advocacy(class54._necklace(), class54._patent());
            if (class11 != null) {
                class54._cabin(class11);
                if (!this.quizzes$.contains((Object)class11)) {
                    this.quizzes$.add(class11);
                }
            } else {
                class54._inserted(class54._necklace() + class54._stereo());
                class54._seeking(class54._patent() + class54._chargers());
            }
            if (this.gender$.size() == this.spencer$ && this.quizzes$.size() == Class11._baskets().length) {
                this.gender$.stream().filter(arg_0 -> Class7._celebs(class54, arg_0)).forEach(arg_0 -> Class7._annie(class54, arg_0));
            }
            class54._drugs();
        }
    }

    private static boolean _celebs(Class54 class54, Class54 class542) {
        return ((class542._necklace() > class54._necklace() && class542._necklace() - class54._necklace() < 196 - 365 + 9 + 210 || class54._necklace() > class542._necklace() && class54._necklace() - class542._necklace() < 53 - 75 + 39 - 14 + 47) && (class542._patent() > class54._patent() && class542._patent() - class54._patent() < 147 - 199 + 124 + -22 || class54._patent() > class542._patent() && class54._patent() - class542._patent() < 177 - 280 + 133 - 94 + 114) ? 166 - 187 + 92 - 32 + -38 : 121 - 165 + 82 + -38) != 0;
    }

    public Class7(int n) {
        this.quizzes$ = new HashSet<Class11>();
        this.spencer$ = n;
    }

    private static void _annie(Class54 class54, Class54 class542) {
        class54._courts(class542._necklace(), class542._patent());
    }

    private Class11 _advocacy(int n, int n2) {
        ScaledResolution scaledResolution = Class22._remedy();
        if (n < 0) {
            return Class11.captain$;
        }
        if (n2 < 0) {
            return Class11.motel$;
        }
        if (n > scaledResolution.getScaledWidth()) {
            return Class11.failing$;
        }
        if (n2 > scaledResolution.getScaledHeight()) {
            return Class11.alarm$;
        }
        return null;
    }
}

