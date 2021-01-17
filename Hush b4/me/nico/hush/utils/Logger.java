// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import net.minecraft.client.Minecraft;

public class Logger
{
    private Minecraft mc;
    
    public void Loading(final String text) {
        System.out.println("Loading > " + text);
    }
    
    public void Info(final String text) {
        System.out.println("Info > " + text);
    }
    
    public void Error(final String text) {
        System.out.println("Error > " + text);
    }
    
    public void Downloading(final String text) {
        System.out.println("Downloading > " + text);
    }
    
    public void Creating(final String text) {
        System.out.println("Creating > " + text);
    }
}
