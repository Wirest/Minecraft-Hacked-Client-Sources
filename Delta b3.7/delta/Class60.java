/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL11
 */
package delta;

import delta.Class139;
import delta.Class22;
import delta.Class47;
import delta.Class91;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Class60 {
    private List<Class139> trick$ = new ArrayList<Class139>();
    private int samuel$;
    private static final float mailing$ = 0.15f;

    public void _prevent(int n) {
        for (int i = 228 - 358 + 177 - 4 + -43; i < n; ++i) {
            this.trick$.add(Class139._fares());
        }
    }

    public Class60(int n) {
        this._prevent(n);
        this.samuel$ = 209 - 304 + 180 + -5;
    }

    public void _quotes(int n) {
        for (Class139 class139 : this.trick$) {
            class139._academy(n, 0.15f);
        }
    }

    public void _clone(double d, float f, float f2) {
        ScaledResolution scaledResolution = Class22._remedy();
        for (Class139 class139 : this.trick$) {
            float f3;
            if (!(class139._really() >= 0.0f) || !(class139._partner() >= 0.0f) || !((double)class139._really() <= (double)scaledResolution.getScaledWidth() * d) || !((double)class139._partner() <= (double)scaledResolution.getScaledHeight() * d)) continue;
            Class47._divided()._believe(class139._really(), class139._partner(), 1.0f, 186 - 211 + 141 - 109 + 3, 218 - 390 + 255 - 169 + 85);
            float f4 = (float)Class91._adults(class139._really(), class139._partner(), f, f2);
            if (f4 < (float)this.samuel$) {
                f3 = Math.min(1.0f, Math.min(1.0f, 1.0f - f4 / (float)this.samuel$));
                this._blowing(class139._really(), class139._partner(), f, f2, f3);
            }
            f3 = 0.0f;
            Class139 class1392 = null;
            for (Class139 class1393 : this.trick$) {
                f4 = class139._fastest(class1393);
                if (!(f4 <= (float)this.samuel$) || !(Class91._adults(f, f2, class139._really(), class139._partner()) <= (double)this.samuel$) && !(Class91._adults(f, f2, class1393._really(), class1393._partner()) <= (double)this.samuel$) || !(f3 <= 0.0f) && !(f4 <= f3)) continue;
                f3 = f4;
                class1392 = class1393;
            }
            if (class1392 == null) continue;
            float f5 = Math.min(1.0f, Math.min(1.0f, 1.0f - f3 / (float)this.samuel$));
            this._blowing(class139._really(), class139._partner(), class1392._really(), class1392._partner(), f5);
        }
    }

    private void _blowing(float f, float f2, float f3, float f4, float f5) {
        GL11.glPushMatrix();
        GL11.glEnable((int)(230 - 278 + 46 - 31 + 2881));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)f5);
        GL11.glDisable((int)(103 - 151 + 29 + 3572));
        GL11.glBlendFunc((int)(51 - 70 + 10 + 779), (int)(172 - 257 + 170 - 108 + 794));
        GL11.glEnable((int)(111 - 176 + 112 + 2995));
        GL11.glLineWidth((float)0.5f);
        GL11.glBegin((int)(43 - 46 + 23 - 18 + -1));
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glVertex2f((float)f3, (float)f4);
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)f5);
        GL11.glDisable((int)(34 - 49 + 3 + 3054));
        GL11.glDisable((int)(95 - 119 + 84 - 74 + 2862));
        GL11.glEnable((int)(188 - 194 + 19 - 12 + 3552));
        GL11.glPopMatrix();
    }
}

