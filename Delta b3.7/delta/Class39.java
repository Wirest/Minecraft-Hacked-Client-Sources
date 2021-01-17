/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package delta;

import delta.Class11;
import delta.Class16;
import delta.Class22;
import delta.Class54;
import delta.utils.ColorUtils;
import delta.utils.RenderUtils;
import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;

public class Class39
extends Class54 {
    private int thousand$;
    private int claim$;
    private final Random hosted$ = new Random();

    @Override
    public void _drugs() {
        RenderUtils._realtor(this._necklace(), this._patent(), this._album(), ColorUtils.getColor(this.claim$, 0.85f, 130 - 198 + 4 + 65).getRGB());
    }

    public Class39(float f) {
        super(f);
        this.claim$ = 52 - 92 + 22 + 18;
        Random random = new Random();
        this.claim$ = random.nextInt(69 - 116 + 5 - 2 + 1000044);
        ScaledResolution scaledResolution = Class22._remedy();
        this._inserted(random.nextBoolean() ? 172 - 286 + 266 - 43 + -110 : scaledResolution.getScaledWidth() + (171 - 233 + 88 - 6 + -19));
        this._seeking(random.nextBoolean() ? 129 - 174 + 57 + -13 : scaledResolution.getScaledHeight() + (172 - 282 + 32 - 15 + 94));
        this.thousand$ = 31 - 51 + 28 + -3;
    }

    @Override
    public void _courts(int n, int n2) {
        if (this.thousand$ > 79 - 154 + 115 - 112 + 75) {
            this.thousand$ -= 236 - 345 + 73 - 14 + 51;
        }
        RenderUtils._tower(this._necklace(), this._patent(), n, n2);
    }

    @Override
    public void _cabin(Class11 class11) {
        ScaledResolution scaledResolution = Class22._remedy();
        switch (Class16.calls$[class11.ordinal()]) {
            case 1: {
                this._seeking(254 - 349 + 240 - 136 + -9);
                this._children(this.hosted$.nextBoolean() ? this.hosted$.nextInt(this.thousand$) + (131 - 176 + 38 + 8) : -(this.hosted$.nextInt(this.thousand$) + (229 - 393 + 83 - 36 + 118)));
                this._bikini(this.hosted$.nextInt(this.thousand$) + (54 - 58 + 11 + -6));
                break;
            }
            case 2: {
                this._inserted(134 - 174 + 91 - 49 + -2);
                this._children(this.hosted$.nextInt(this.thousand$) + (267 - 362 + 132 - 47 + 11));
                this._bikini(this.hosted$.nextBoolean() ? this.hosted$.nextInt(this.thousand$) + (33 - 51 + 27 - 4 + -4) : -(this.hosted$.nextInt(this.thousand$) + (161 - 283 + 40 - 18 + 101)));
                break;
            }
            case 3: {
                this._seeking(scaledResolution.getScaledHeight());
                this._children(this.hosted$.nextBoolean() ? this.hosted$.nextInt(this.thousand$) + (203 - 398 + 319 - 121 + -2) : -(this.hosted$.nextInt(this.thousand$) + (85 - 126 + 125 + -83)));
                this._bikini(-(this.hosted$.nextInt(this.thousand$) + (263 - 276 + 228 + -214)));
                break;
            }
            case 4: {
                this._inserted(scaledResolution.getScaledWidth());
                this._children(-(this.hosted$.nextInt(this.thousand$) + (173 - 237 + 3 + 62)));
                this._bikini(this.hosted$.nextBoolean() ? this.hosted$.nextInt(this.thousand$) + (31 - 47 + 12 - 8 + 13) : -(this.hosted$.nextInt(this.thousand$) + (209 - 361 + 116 + 37)));
            }
        }
    }
}

