/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class110;

public class Class33 {
    private long butter$ = 217L - 372L + 365L + -69630L;
    private float barrier$;
    private float rendered$;

    public Class33(float f, float f2) {
        this.rendered$ = f;
        this.barrier$ = f2;
    }

    public boolean _payday(float f, float f2, double d) {
        this._griffin();
        long l = System.currentTimeMillis();
        long l2 = l - this.butter$;
        this.butter$ = l;
        double d2 = 0.0;
        double d3 = 0.0;
        if (d != 0.0) {
            d2 = (double)(Math.abs(f - this.rendered$) * 0.35f) / (10.0 / d);
            d3 = (double)(Math.abs(f2 - this.barrier$) * 0.35f) / (10.0 / d);
        }
        this.rendered$ = Class110._hosting(f, this.rendered$, l2, d2);
        this.barrier$ = Class110._hosting(f2, this.barrier$, l2, d3);
        return (this.rendered$ == f && this.barrier$ == f2 ? 25 - 41 + 14 + 3 : 95 - 188 + 37 - 1 + 57) != 0;
    }

    public float _warnings() {
        return this.rendered$;
    }

    private void _griffin() {
        if (this.butter$ == 239L - 415L + 197L + -69441L) {
            this.butter$ = System.currentTimeMillis();
        }
    }

    public float _trader() {
        return this.barrier$;
    }

    public void _disks(float f) {
        this.barrier$ = f;
    }

    public void _dispute(float f) {
        this.rendered$ = f;
    }

    public boolean _tahoe(float f, float f2, int n, int n2) {
        this._griffin();
        long l = System.currentTimeMillis();
        long l2 = l - this.butter$;
        this.butter$ = l;
        int n3 = (int)(Math.abs(f - this.rendered$) * 0.51f);
        int n4 = (int)(Math.abs(f2 - this.barrier$) * 0.51f);
        this.rendered$ = Class110._hosting(f, this.rendered$, l2, n3);
        this.barrier$ = Class110._hosting(f2, this.barrier$, l2, n4);
        return (this.rendered$ == f && this.barrier$ == f2 ? 138 - 212 + 200 - 97 + -28 : 102 - 169 + 131 + -64) != 0;
    }
}

