/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class6;
import delta.Class9;
import delta.Class91;
import java.util.Random;

public class Class202 {
    private float swift$;
    private Class6<Float> depth$;
    private final Random searches$;
    private float concerns$;
    private float choir$;
    private float capacity$;
    private float ensures$ = Class91._turns(1.1f, 1.8f);
    private Class9 thehun$;

    public Class202(float f, float f2, float f3, float f4) {
        this.swift$ = f;
        this.choir$ = f2;
        this.concerns$ = f3;
        this.capacity$ = f4;
        this.searches$ = new Random();
        this.depth$ = new Class6<Float>(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(0.0f));
        this.thehun$ = new Class9(Float.valueOf(0.0f), Float.valueOf(0.0f));
    }

    public Class9 _draws(Class9 class9, Class9 class92, float f, float f2) {
        return this.thehun$._engage(Float.valueOf(class92._hidden().floatValue() - class9._hidden().floatValue() - (Math.abs(class92._hidden().floatValue() - class9._hidden().floatValue()) > 5.0f ? Math.abs(class92._hidden().floatValue() - class9._hidden().floatValue()) / Math.abs(class92._hidden().floatValue() - class9._hidden().floatValue()) * 2.0f / 2.0f : 0.0f)))._ensemble(Float.valueOf(class92._border().floatValue() - class9._border().floatValue()))._penalty()._engage(Float.valueOf(class92._hidden().floatValue() - this.thehun$._hidden().floatValue() / f2))._penalty()._ensemble(Float.valueOf(class92._border().floatValue() - this.thehun$._border().floatValue() / f))._penalty();
    }

    public float _handle(float f, float f2) {
        return f + this.searches$.nextFloat() * (f2 - f);
    }

    public Class9 _acids(Class6<Double> class6, Class6<Double> class62) {
        Class9 class9 = new Class9(Float.valueOf(0.0f), Float.valueOf(0.0f));
        this.depth$._paradise(Float.valueOf(((Number)class6._poster()).floatValue() - ((Number)class62._poster()).floatValue()))._notebook(Float.valueOf(((Number)class6._aspects()).floatValue() + this.ensures$ - (((Number)class62._aspects()).floatValue() + this.ensures$)))._sitemap(Float.valueOf(((Number)class6._nervous()).floatValue() - ((Number)class62._nervous()).floatValue()));
        double d = Math.hypot(((Number)this.depth$._poster()).doubleValue(), ((Number)this.depth$._nervous()).doubleValue());
        float f = (float)Math.atan2(((Number)this.depth$._nervous()).floatValue(), ((Number)this.depth$._poster()).floatValue());
        float f2 = (float)Math.atan2(((Number)this.depth$._aspects()).floatValue(), d);
        float f3 = 57.29578f;
        float f4 = f * f3 - 90.0f;
        float f5 = -(f2 * f3);
        return class9._engage(Float.valueOf(f4))._ensemble(Float.valueOf(f5));
    }

    public void _olympus(float f) {
        this.ensures$ = f;
    }
}

