// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;

public enum AuthFlag
{
    TTV_AuthOption_None(0), 
    TTV_AuthOption_Broadcast(1), 
    TTV_AuthOption_Chat(2);
    
    private static Map<Integer, AuthFlag> s_Map;
    private int m_Value;
    
    public static AuthFlag lookupValue(final int i) {
        return AuthFlag.s_Map.get(i);
    }
    
    public static int getNativeValue(final HashSet<AuthFlag> set) {
        if (set == null) {
            return AuthFlag.TTV_AuthOption_None.getValue();
        }
        int n = 0;
        for (final AuthFlag authFlag : set) {
            if (authFlag != null) {
                n |= authFlag.getValue();
            }
        }
        return n;
    }
    
    private AuthFlag(final int value) {
        this.m_Value = value;
    }
    
    public int getValue() {
        return this.m_Value;
    }
    
    static {
        AuthFlag.s_Map = new HashMap<Integer, AuthFlag>();
        for (final AuthFlag authFlag : EnumSet.allOf(AuthFlag.class)) {
            AuthFlag.s_Map.put(authFlag.getValue(), authFlag);
        }
    }
}
