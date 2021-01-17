/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class87;

public class Class9
extends Class87<Float> {
    public Class9 _ensemble(Float f) {
        this._notebook(f);
        return this;
    }

    public Float _border() {
        return Float.valueOf(((Number)this._aspects()).floatValue());
    }

    public Class9 _penalty() {
        this._engage(Float.valueOf(this._hidden().floatValue() % 360.0f));
        this._ensemble(Float.valueOf(this._border().floatValue() % 360.0f));
        while (this._hidden().floatValue() <= -180.0f) {
            this._engage(Float.valueOf(this._hidden().floatValue() + 360.0f));
        }
        while (this._border().floatValue() <= -180.0f) {
            this._ensemble(Float.valueOf(this._border().floatValue() + 360.0f));
        }
        while (this._hidden().floatValue() > 180.0f) {
            this._engage(Float.valueOf(this._hidden().floatValue() - 360.0f));
        }
        while (this._border().floatValue() > 180.0f) {
            this._ensemble(Float.valueOf(this._border().floatValue() - 360.0f));
        }
        return this;
    }

    public Float _hidden() {
        return Float.valueOf(((Number)this._poster()).floatValue());
    }

    public Class9 _engage(Float f) {
        this._paradise(f);
        return this;
    }

    public Class9(Float f, Float f2) {
        super(f, f2);
    }
}

