/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Event;

import java.lang.reflect.Method;

public class Data {
    public final Object source;
    public final Method target;
    public final byte priority;

    public Data(Object source, Method target, byte priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }
}

