// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

public enum DisplayContext
{
    STANDARD_NAMES(Type.DIALECT_HANDLING, 0), 
    DIALECT_NAMES(Type.DIALECT_HANDLING, 1), 
    CAPITALIZATION_NONE(Type.CAPITALIZATION, 0), 
    CAPITALIZATION_FOR_MIDDLE_OF_SENTENCE(Type.CAPITALIZATION, 1), 
    CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE(Type.CAPITALIZATION, 2), 
    CAPITALIZATION_FOR_UI_LIST_OR_MENU(Type.CAPITALIZATION, 3), 
    CAPITALIZATION_FOR_STANDALONE(Type.CAPITALIZATION, 4);
    
    private final Type type;
    private final int value;
    
    private DisplayContext(final Type type, final int value) {
        this.type = type;
        this.value = value;
    }
    
    public Type type() {
        return this.type;
    }
    
    public int value() {
        return this.value;
    }
    
    public enum Type
    {
        DIALECT_HANDLING, 
        CAPITALIZATION;
    }
}
