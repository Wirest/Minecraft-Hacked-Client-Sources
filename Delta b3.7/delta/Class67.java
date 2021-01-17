/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 */
package delta;

import delta.OVYt;
import java.util.HashMap;
import java.util.Map;

public class Class67 {
    private static Map<String, String> brian$;
    private static String[] zoning$;

    static {
        Class67._indian();
        brian$ = new HashMap<String, String>();
        brian$.put(OVYt.968L.FS1x((String)zoning$[0], (int)-343422182), OVYt.968L.FS1x((String)zoning$[1], (int)-1149123286));
        brian$.put(OVYt.968L.FS1x((String)zoning$[2], (int)1493120335), OVYt.968L.FS1x((String)zoning$[3], (int)-2066298606));
        brian$.put(OVYt.968L.FS1x((String)zoning$[4], (int)331500806), OVYt.968L.FS1x((String)zoning$[5], (int)449192061));
        brian$.put(OVYt.968L.FS1x((String)zoning$[6], (int)362538405), OVYt.968L.FS1x((String)zoning$[7], (int)-2090886735));
        brian$.put(OVYt.968L.FS1x((String)zoning$[8], (int)-1800884938), OVYt.968L.FS1x((String)zoning$[9], (int)-1762114324));
        brian$.put(OVYt.968L.FS1x((String)zoning$[10], (int)234417034), OVYt.968L.FS1x((String)zoning$[11], (int)2107640702));
    }

    private static void _indian() {
        zoning$ = new String[]{"\ucb7f\ucb74\ucb7b\ucb78\ucb76\ucb7f\ucb7e", "\uc55e\uc558\uc55f\uc54f", "\u353d\u3520\u353b\u352e\u353b\u352a", "\uc966\uc960\uc967\uc977", "\u4d6a\u4d69\u4d61\u4d69\u4d49\u4d60\u4d60\u4d75\u4d63\u4d72", "\u204c\u204d", "\ue5c7\ue5c4\ue5c6\ue5ce\ue5c2\ue5d7\ue5ca\ue5d0\ue5cb\ue5c1", "\u9981\u99c9\u99f4\u99f4\u99f4\u99f4\u99f4\u99f4", "\uad54\uad57\uad44\uad74\uad57\uad55\uad5d\uad51\uad44\uad59\uad43\uad58\uad52", "\u44dc\u4494\u44a9\u44a9\u44a9\u44a9\u44a9\u44a9", "\uebec\uebe5\uebf8\uebed\uebef\uebde\uebef\uebf2\uebfe\uebff\uebf8\uebef", "\u0b0a\u0b1b\u0b06\u0b0a\u0b0b\u0b0c\u0b1b\u0b0d\u0b51\u0b19\u0b0b\u0b17\u0b51\u0b1a\u0b1b\u0b12\u0b0a\u0b1f\u0b12\u0b11\u0b19\u0b11\u0b4c\u0b21\u0b17\u0b1d\u0b11\u0b10\u0b50\u0b0e\u0b10\u0b19"};
    }

    public static String _invite(String string, String string2) {
        if (brian$.containsKey(string2)) {
            return brian$.get(string2);
        }
        return string;
    }
}

