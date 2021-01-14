/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package cn.kody.debug.utils.handler;

public class ClientEventHandler {
    private boolean state;

    public boolean canExcecute(boolean eventBool) {
        if (eventBool) {
            if (!this.state) {
                this.state = true;
                return true;
            }
        } else {
            this.state = false;
        }
        return false;
    }
}

