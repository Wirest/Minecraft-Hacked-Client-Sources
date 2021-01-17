// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

public class Pair<A, B>
{
    private final A first;
    private final B second;
    
    protected Pair(final A first, final B second) {
        this.first = first;
        this.second = second;
    }
    
    public static <A, B> Pair<A, B> of(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    public A first() {
        return this.first;
    }
    
    public B second() {
        return this.second;
    }
    
    public String mkString(final String separator) {
        return String.format("%s%s%s", this.first, separator, this.second);
    }
}
