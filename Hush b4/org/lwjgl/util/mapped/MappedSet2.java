// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

public class MappedSet2
{
    private final MappedObject a;
    private final MappedObject b;
    public int view;
    
    MappedSet2(final MappedObject a, final MappedObject b) {
        this.a = a;
        this.b = b;
    }
    
    void view(final int view) {
        this.a.setViewAddress(this.a.getViewAddress(view));
        this.b.setViewAddress(this.b.getViewAddress(view));
    }
    
    public void next() {
        this.a.next();
        this.b.next();
    }
}
