/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.slowly.client.util.handler;

import org.lwjgl.input.Keyboard;

public class KeyInputHandler {
    public boolean clicked;
    private int key;

    public KeyInputHandler(int key) {
        this.key = key;
    }

    public boolean canExcecute() {
        if (Keyboard.isKeyDown((int)this.key)) {
            if (!this.clicked) {
                this.clicked = true;
                return true;
            }
        } else {
            this.clicked = false;
        }
        return false;
    }
}

