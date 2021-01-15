/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.alt.screens.account.component;

import me.aristhena.lucid.management.account.Alt;
import me.aristhena.lucid.management.account.LoginThread;
import me.aristhena.lucid.ui.alt.screens.component.Button;

public class AltButton
extends Button {
    private Alt alt;

    public AltButton(String text, int x1, int x2, int y1, int y2, int minHex, int maxHex, Alt alt) {
        super(text, x1, x2, y1, y2, minHex, maxHex);
        this.alt = alt;
    }

    @Override
    public void onClick(int button) {
        LoginThread thread = new LoginThread(this.alt.email, this.alt.pass);
        thread.start();
    }

    public Alt getAlt() {
        return this.alt;
    }

    public void setAlt(Alt alt) {
        this.alt = alt;
    }
}

