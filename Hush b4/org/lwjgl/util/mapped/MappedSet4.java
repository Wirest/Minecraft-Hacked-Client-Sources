// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

public class MappedSet4
{
    private final MappedObject a;
    private final MappedObject b;
    private final MappedObject c;
    private final MappedObject d;
    public int view;
    
    MappedSet4(final MappedObject a, final MappedObject b, final MappedObject c, final MappedObject d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    void view(final int view) {
        this.a.setViewAddress(this.a.getViewAddress(view));
        this.b.setViewAddress(this.b.getViewAddress(view));
        this.c.setViewAddress(this.c.getViewAddress(view));
        this.d.setViewAddress(this.d.getViewAddress(view));
    }
    
    public void next() {
        this.a.next();
        this.b.next();
        this.c.next();
        this.d.next();
    }
}
