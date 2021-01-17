/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta;

import delta.Class11;
import delta.Class22;
import delta.Class80;
import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;

public abstract class Class54
implements Class80 {
    private int clearly$;
    private int staying$;
    private int expects$;
    private int counter$;
    private final float highly$;

    public void _seeking(int n) {
        this.clearly$ = n;
    }

    protected void _children(int n) {
        this.counter$ = n;
    }

    int _chargers() {
        return this.expects$;
    }

    public abstract void _cabin(Class11 var1);

    public float _album() {
        return this.highly$;
    }

    public Class54(float f) {
        this.highly$ = f;
        Random random = new Random();
        ScaledResolution scaledResolution = Class22._remedy();
        this.staying$ = random.nextBoolean() ? 250 - 368 + 133 + -16 : scaledResolution.getScaledWidth();
        this.clearly$ = random.nextBoolean() ? 249 - 287 + 57 + -20 : scaledResolution.getScaledHeight();
    }

    public int _necklace() {
        return this.staying$;
    }

    public void _inserted(int n) {
        this.staying$ = n;
    }

    public int _patent() {
        return this.clearly$;
    }

    protected void _bikini(int n) {
        this.expects$ = n;
    }

    int _stereo() {
        return this.counter$;
    }
}

