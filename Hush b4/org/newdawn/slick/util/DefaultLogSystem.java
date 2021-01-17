// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

import java.util.Date;
import java.io.PrintStream;

public class DefaultLogSystem implements LogSystem
{
    public static PrintStream out;
    
    static {
        DefaultLogSystem.out = System.out;
    }
    
    @Override
    public void error(final String message, final Throwable e) {
        this.error(message);
        this.error(e);
    }
    
    @Override
    public void error(final Throwable e) {
        DefaultLogSystem.out.println(new Date() + " ERROR:" + e.getMessage());
        e.printStackTrace(DefaultLogSystem.out);
    }
    
    @Override
    public void error(final String message) {
        DefaultLogSystem.out.println(new Date() + " ERROR:" + message);
    }
    
    @Override
    public void warn(final String message) {
        DefaultLogSystem.out.println(new Date() + " WARN:" + message);
    }
    
    @Override
    public void info(final String message) {
        DefaultLogSystem.out.println(new Date() + " INFO:" + message);
    }
    
    @Override
    public void debug(final String message) {
        DefaultLogSystem.out.println(new Date() + " DEBUG:" + message);
    }
    
    @Override
    public void warn(final String message, final Throwable e) {
        this.warn(message);
        e.printStackTrace(DefaultLogSystem.out);
    }
}
