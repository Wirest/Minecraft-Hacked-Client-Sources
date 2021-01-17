/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.util.vector.Vector2f
 */
package delta;

import delta.Class91;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class Class139 {
    private Vector2f puerto$;
    private static final Random geometry$ = new Random();
    private Vector2f gardens$;
    private float links$;
    private float replied$;

    public void _overhead(float f) {
        this.gardens$.setX(f);
    }

    public static Class139 _fares() {
        Vector2f vector2f = new Vector2f((float)(Math.random() * 2.0 - 1.0), (float)(Math.random() * 2.0 - 1.0));
        float f = geometry$.nextInt(Display.getWidth());
        float f2 = geometry$.nextInt(Display.getHeight());
        float f3 = (float)(Math.random() * 4.0) + 1.0f;
        return new Class139(vector2f, f, f2, f3);
    }

    public void _motors(Vector2f vector2f) {
        this.puerto$ = vector2f;
    }

    public Vector2f _achieved() {
        return this.puerto$;
    }

    public float _genesis() {
        return this.replied$;
    }

    public float _really() {
        return this.gardens$.getX();
    }

    public float _theme() {
        return this.links$;
    }

    public Class139(Vector2f vector2f, float f, float f2, float f3) {
        this.puerto$ = vector2f;
        this.gardens$ = new Vector2f(f, f2);
        this.links$ = f3;
    }

    public float _fastest(Class139 class139) {
        return this._proteins(class139._really(), class139._partner());
    }

    public void _academy(int n, float f) {
        this.gardens$.x += this.puerto$.getX() * (float)n * f;
        this.gardens$.y += this.puerto$.getY() * (float)n * f;
        if (this.replied$ < 255.0f) {
            this.replied$ += 0.05f * (float)n;
        }
        if (this.gardens$.getX() > (float)Display.getWidth()) {
            this.gardens$.setX(0.0f);
        }
        if (this.gardens$.getX() < 0.0f) {
            this.gardens$.setX((float)Display.getWidth());
        }
        if (this.gardens$.getY() > (float)Display.getHeight()) {
            this.gardens$.setY(0.0f);
        }
        if (this.gardens$.getY() < 0.0f) {
            this.gardens$.setY((float)Display.getHeight());
        }
    }

    public float _proteins(float f, float f2) {
        return (float)Class91._adults(this._really(), this._partner(), f, f2);
    }

    public void _crazy(float f) {
        this.gardens$.setY(f);
    }

    public void _erotica(float f) {
        this.links$ = f;
    }

    public float _partner() {
        return this.gardens$.getY();
    }
}

