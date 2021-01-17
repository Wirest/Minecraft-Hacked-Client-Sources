/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package delta;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Class108 {
    public final double premier$;
    public final double hebrew$;
    public final double resume$;

    public Class108 _brandon(Vec3 vec3) {
        return this._palace(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public Class108 _briefs(Class108 class108) {
        return this._within(class108.premier$, class108.hebrew$, class108.resume$);
    }

    public Class108 _eleven(float f) {
        float f2 = MathHelper.cos((float)f);
        float f3 = MathHelper.sin((float)f);
        double d = this.premier$ * (double)f2 + this.resume$ * (double)f3;
        double d2 = this.hebrew$;
        double d3 = this.resume$ * (double)f2 - this.premier$ * (double)f3;
        return new Class108(d, d2, d3);
    }

    public Class108 _merit(Class108 class108) {
        return new Class108(class108.premier$ - this.premier$, class108.hebrew$ - this.hebrew$, class108.resume$ - this.resume$);
    }

    public double _batman(Class108 class108) {
        double d = class108.premier$ - this.premier$;
        double d2 = class108.hebrew$ - this.hebrew$;
        double d3 = class108.resume$ - this.resume$;
        return MathHelper.sqrt_double((double)(d * d + d2 * d2 + d3 * d3));
    }

    public String toString() {
        return "(" + this.premier$ + ", " + this.hebrew$ + ", " + this.resume$ + ")";
    }

    public Class108 _smell(Class108 class108, double d) {
        double d2 = class108.premier$ - this.premier$;
        double d3 = class108.hebrew$ - this.hebrew$;
        double d4 = class108.resume$ - this.resume$;
        if (d2 * d2 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.premier$) / d2;
        return d5 >= 0.0 && d5 <= 1.0 ? new Class108(this.premier$ + d2 * d5, this.hebrew$ + d3 * d5, this.resume$ + d4 * d5) : null;
    }

    public double _stated(Class108 class108) {
        return this.premier$ * class108.premier$ + this.hebrew$ * class108.hebrew$ + this.resume$ * class108.resume$;
    }

    public Class108(double d, double d2, double d3) {
        if (d == -0.0) {
            d = 0.0;
        }
        if (d2 == -0.0) {
            d2 = 0.0;
        }
        if (d3 == -0.0) {
            d3 = 0.0;
        }
        this.premier$ = d;
        this.hebrew$ = d2;
        this.resume$ = d3;
    }

    public Class108 _diana(Class108 class108) {
        return new Class108(this.hebrew$ * class108.resume$ - this.resume$ * class108.hebrew$, this.resume$ * class108.premier$ - this.premier$ * class108.resume$, this.premier$ * class108.hebrew$ - this.hebrew$ * class108.premier$);
    }

    public double _medium(Class108 class108) {
        double d = class108.premier$ - this.premier$;
        double d2 = class108.hebrew$ - this.hebrew$;
        double d3 = class108.resume$ - this.resume$;
        return d * d + d2 * d2 + d3 * d3;
    }

    public Class108 _within(double d, double d2, double d3) {
        return this._palace(-d, -d2, -d3);
    }

    public Class108 _helen(float f) {
        float f2 = MathHelper.cos((float)f);
        float f3 = MathHelper.sin((float)f);
        double d = this.premier$;
        double d2 = this.hebrew$ * (double)f2 + this.resume$ * (double)f3;
        double d3 = this.resume$ * (double)f2 - this.hebrew$ * (double)f3;
        return new Class108(d, d2, d3);
    }

    public Class108 _cemetery() {
        double d = MathHelper.sqrt_double((double)(this.premier$ * this.premier$ + this.hebrew$ * this.hebrew$ + this.resume$ * this.resume$));
        return d < 1.0E-4 ? new Class108(0.0, 0.0, 0.0) : new Class108(this.premier$ / d, this.hebrew$ / d, this.resume$ / d);
    }

    public Class108 _washing(Class108 class108, double d) {
        double d2 = class108.premier$ - this.premier$;
        double d3 = class108.hebrew$ - this.hebrew$;
        double d4 = class108.resume$ - this.resume$;
        if (d4 * d4 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.resume$) / d4;
        return d5 >= 0.0 && d5 <= 1.0 ? new Class108(this.premier$ + d2 * d5, this.hebrew$ + d3 * d5, this.resume$ + d4 * d5) : null;
    }

    public Class108 _apart(Class108 class108, double d) {
        double d2 = class108.premier$ - this.premier$;
        double d3 = class108.hebrew$ - this.hebrew$;
        double d4 = class108.resume$ - this.resume$;
        if (d3 * d3 < (double)1.0E-7f) {
            return null;
        }
        double d5 = (d - this.hebrew$) / d3;
        return d5 >= 0.0 && d5 <= 1.0 ? new Class108(this.premier$ + d2 * d5, this.hebrew$ + d3 * d5, this.resume$ + d4 * d5) : null;
    }

    public Class108 _palace(double d, double d2, double d3) {
        return new Class108(this.premier$ + d, this.hebrew$ + d2, this.resume$ + d3);
    }

    public double _smoke() {
        return MathHelper.sqrt_double((double)(this.premier$ * this.premier$ + this.hebrew$ * this.hebrew$ + this.resume$ * this.resume$));
    }
}

