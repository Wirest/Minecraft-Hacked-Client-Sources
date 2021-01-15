package me.aristhena.lucid.eventapi;

public final class Priority
{
    
    public static final byte FIRST = 0, SECOND = 1, THIRD = 2, FOURTH = 3, FIFTH = 4;
    
    public static final byte[] VALUE_ARRAY;
    
    static
    {
	VALUE_ARRAY = new byte[] { FIRST, SECOND, THIRD, FOURTH, FIFTH };
    }
    
}
