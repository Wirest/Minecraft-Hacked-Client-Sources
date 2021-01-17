// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

import java.util.ArrayList;
import java.io.IOException;

public class CompositeIOException extends IOException
{
    private ArrayList exceptions;
    
    public CompositeIOException() {
        this.exceptions = new ArrayList();
    }
    
    public void addException(final Exception e) {
        this.exceptions.add(e);
    }
    
    @Override
    public String getMessage() {
        String msg = "Composite Exception: \n";
        for (int i = 0; i < this.exceptions.size(); ++i) {
            msg = String.valueOf(msg) + "\t" + this.exceptions.get(i).getMessage() + "\n";
        }
        return msg;
    }
}
