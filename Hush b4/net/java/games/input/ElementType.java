// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class ElementType
{
    private static final ElementType[] map;
    public static final ElementType INPUT_MISC;
    public static final ElementType INPUT_BUTTON;
    public static final ElementType INPUT_AXIS;
    public static final ElementType INPUT_SCANCODES;
    public static final ElementType OUTPUT;
    public static final ElementType FEATURE;
    public static final ElementType COLLECTION;
    private final int type_id;
    
    public static final ElementType map(final int type_id) {
        if (type_id < 0 || type_id >= ElementType.map.length) {
            return null;
        }
        return ElementType.map[type_id];
    }
    
    private ElementType(final int type_id) {
        ElementType.map[type_id] = this;
        this.type_id = type_id;
    }
    
    public final String toString() {
        return "ElementType (0x" + Integer.toHexString(this.type_id) + ")";
    }
    
    static {
        map = new ElementType[514];
        INPUT_MISC = new ElementType(1);
        INPUT_BUTTON = new ElementType(2);
        INPUT_AXIS = new ElementType(3);
        INPUT_SCANCODES = new ElementType(4);
        OUTPUT = new ElementType(129);
        FEATURE = new ElementType(257);
        COLLECTION = new ElementType(513);
    }
}
