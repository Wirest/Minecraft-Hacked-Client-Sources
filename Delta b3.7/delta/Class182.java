/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 */
package delta;

import delta.utils.TimeHelper;
import net.minecraft.client.gui.GuiButton;

public abstract class Class182
extends GuiButton {
    private long diverse$;
    private boolean contain$;
    private TimeHelper posted$;
    private boolean amend$;

    public Class182(int n, int n2, int n3, int n4, int n5, String string, long l) {
        super(n, n2, n3, n4, n5, string);
        this.diverse$ = l;
    }

    public Class182(int n, int n2, int n3, String string, long l) {
        this(n, n2, n3, 109 - 161 + 84 - 44 + 112, 48 - 78 + 45 + 85, string, l);
    }

    protected abstract void H3tH();

    protected abstract void NkAX();

    public void rr9w() {
        if (this.amend$) {
            return;
        }
        if (this.posted$ == null) {
            this.posted$ = new TimeHelper();
            this.posted$.setLastMS();
        }
        if (!this.posted$.hasReached(this.contain$ ? 96L - 134L + 70L - 22L + -10L : this.diverse$)) {
            return;
        }
        this.amend$ = 235 - 427 + 317 + -124;
        this.H3tH();
    }

    public void iYIG() {
        this.contain$ = 108 - 121 + 78 + -64;
        this.NkAX();
    }
}

