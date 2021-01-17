// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class Tuple<A, B>
{
    private A a;
    private B b;
    
    public Tuple(final A aIn, final B bIn) {
        this.a = aIn;
        this.b = bIn;
    }
    
    public A getFirst() {
        return this.a;
    }
    
    public B getSecond() {
        return this.b;
    }
}
