// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib;

public class Agent
{
    public static final Agent MINECRAFT;
    public static final Agent SCROLLS;
    private final String name;
    private final int version;
    
    public Agent(final String name, final int version) {
        this.name = name;
        this.version = version;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    @Override
    public String toString() {
        return "Agent{name='" + this.name + '\'' + ", version=" + this.version + '}';
    }
    
    static {
        MINECRAFT = new Agent("Minecraft", 1);
        SCROLLS = new Agent("Scrolls", 1);
    }
}
