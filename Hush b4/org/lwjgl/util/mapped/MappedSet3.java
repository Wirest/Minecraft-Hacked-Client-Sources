// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

public class MappedSet3
{
    private final MappedObject a;
    private final MappedObject b;
    private final MappedObject c;
    public int view;
    
    MappedSet3(final MappedObject a, final MappedObject b, final MappedObject c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    void view(final int view) {
        this.a.setViewAddress(this.a.getViewAddress(view));
        this.b.setViewAddress(this.b.getViewAddress(view));
        this.c.setViewAddress(this.c.getViewAddress(view));
    }
    
    public void next() {
        this.a.next();
        this.b.next();
        this.c.next();
    }
}
