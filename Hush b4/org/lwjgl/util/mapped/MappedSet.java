// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

public class MappedSet
{
    public static MappedSet2 create(final MappedObject a, final MappedObject b) {
        return new MappedSet2(a, b);
    }
    
    public static MappedSet3 create(final MappedObject a, final MappedObject b, final MappedObject c) {
        return new MappedSet3(a, b, c);
    }
    
    public static MappedSet4 create(final MappedObject a, final MappedObject b, final MappedObject c, final MappedObject d) {
        return new MappedSet4(a, b, c, d);
    }
}
