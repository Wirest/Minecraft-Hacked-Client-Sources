/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package delta;

import delta.OVYt;
import delta.utils.Wrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Class32 {
    public static boolean zambia$;
    private static String[] approval$;

    private static void _hearts() {
        approval$ = new String[]{"\u4fd4\u4ff6\u4ff3\u4ff3\u4fec\u4fe8\u4ff6\u4feb\u4ffc\u4ff7", "\uaf4a", "\u98e9\u98f5\u98f5\u98f1\u98f2\u98bb\u98ae\u98ae\u98f3\u98e0\u98f6\u98af\u98e6\u98e8\u98f5\u98e9\u98f4\u98e3\u98f4\u98f2\u98e4\u98f3\u98e2\u98ee\u98ef\u98f5\u98e4\u98ef\u98f5\u98af\u98e2\u98ee\u98ec\u98ae\u98ef\u98ea\u98ee\u98f2\u98ec\u98ee\u98f2\u98ae\u98f9\u98e5\u98e4\u98ed\u98f5\u98e0\u98ae\u98ec\u98e0\u98f2\u98f5\u98e4\u98f3\u98ae\u98e3\u98b3\u98af\u98b4\u98ae\u98ea\u98e8\u98ed\u98ed\u98f2\u98f6\u98e8\u98f5\u98e2\u98e9", "\udc9e", "\ud677", "", "\u670e\u670a\u670e", "\u9134\u9132\u9135\u9125", "\uf9c7\uf9e3\uf9f3\uf9e5\uf9b7\uf9e3\uf9b0\uf9f2\uf9e4\uf9e4\uf9f6\uf9ee\uf9f2\uf9e4\uf9b7\uf9e6\uf9e2\uf9f8\uf9fe\uf9b7\uf9fb\uf977\uf9a8", "\u1bc4\u1be6\u1be3\u1be3\u1bfc\u1bf8\u1be6\u1bfb\u1bec\u1be7", "\u64ba\u64a1\u64b8\u64b8\u64b5\u64a7\u64ba\u64bd\u64b3\u64b3\u64b5", "\u75bb\u75a7\u75a7\u75a3\u75a0\u75e9\u75fc\u75fc\u75a1\u75b2\u75a4\u75fd\u75b4\u75ba\u75a7\u75bb\u75a6\u75b1\u75a6\u75a0\u75b6\u75a1\u75b0\u75bc\u75bd\u75a7\u75b6\u75bd\u75a7\u75fd\u75b0\u75bc\u75be\u75fc\u75bd\u75b8\u75bc\u75a0\u75be\u75bc\u75a0\u75fc\u75ab\u75b7\u75b6\u75bf\u75a7\u75b2\u75fc\u75be\u75b2\u75a0\u75a7\u75b6\u75a1\u75fc\u75b1\u75e0\u75fd\u75e4\u75fc\u75b8\u75ba\u75bf\u75bf\u75a0\u75a4\u75ba\u75a7\u75b0\u75bb", "\ue826", "\u0c99\u0c82\u0c9b\u0c9b\u0c96\u0c84\u0c99\u0c9e\u0c90\u0c90\u0c96", "", "\u5be4\u5be0\u5be4", "\ub222\ub225\ub228\ub237\ub221"};
    }

    static {
        Class32._hearts();
        zambia$ = 142 - 192 + 134 + -83;
    }

    private void _vista() {
        StringBuilder stringBuilder;
        BufferedReader bufferedReader;
        Logger logger = LogManager.getLogger((String)OVYt.968L.FS1x((String)approval$[0], (int)1997229983));
        String string = OVYt.968L.FS1x((String)approval$[1], (int)-1446727930);
        try {
            String string2;
            String string3 = new String(OVYt.968L.FS1x((String)approval$[2], (int)1888131201));
            URL uRL = new URL(string3);
            URLConnection uRLConnection = uRL.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            stringBuilder = new StringBuilder();
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuilder.append(string2 + OVYt.968L.FS1x((String)approval$[3], (int)-2081301356));
            }
        }
        catch (Exception exception) {
            zambia$ = 240 - 364 + 193 + -69;
            exception.printStackTrace();
            Wrapper._occurs();
            return;
        }
        string = stringBuilder.toString();
        bufferedReader.close();
        if (string.equalsIgnoreCase(OVYt.968L.FS1x((String)approval$[4], (int)-1573923269)) || string == OVYt.968L.FS1x((String)approval$[5], (int)-66443639) || string.contains(OVYt.968L.FS1x((String)approval$[6], (int)1517578042))) {
            zambia$ = 114 - 198 + 8 + 76;
            Wrapper._occurs();
            return;
        }
        if (!string.toLowerCase().contains(OVYt.968L.FS1x((String)approval$[7], (int)-704605888))) {
            logger.info(OVYt.968L.FS1x((String)approval$[8], (int)-1431242345));
            zambia$ = 109 - 196 + 75 - 12 + 24;
            Wrapper._occurs();
            return;
        }
    }

    public Class32() {
        StringBuilder stringBuilder;
        BufferedReader bufferedReader;
        Logger logger = LogManager.getLogger((String)OVYt.968L.FS1x((String)approval$[9], (int)-2057036913));
        this._vista();
        String string = OVYt.968L.FS1x((String)approval$[10], (int)-134585100);
        try {
            String string2;
            String string3 = new String(OVYt.968L.FS1x((String)approval$[11], (int)-302025261));
            URL uRL = new URL(string3);
            URLConnection uRLConnection = uRL.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            stringBuilder = new StringBuilder();
            while ((string2 = bufferedReader.readLine()) != null) {
                stringBuilder.append(string2 + OVYt.968L.FS1x((String)approval$[12], (int)-1714690004));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            zambia$ = 114 - 191 + 14 - 5 + 68;
            Wrapper._occurs();
            return;
        }
        string = stringBuilder.toString();
        bufferedReader.close();
        if (string.equalsIgnoreCase(OVYt.968L.FS1x((String)approval$[13], (int)-1497232169)) || string == OVYt.968L.FS1x((String)approval$[14], (int)1420783679) || string.contains(OVYt.968L.FS1x((String)approval$[15], (int)-1528341552))) {
            zambia$ = 109 - 185 + 107 - 10 + -21;
            Wrapper._occurs();
            return;
        }
        if (string.toLowerCase().contains(OVYt.968L.FS1x((String)approval$[16], (int)1506652740))) {
            return;
        }
        zambia$ = 80 - 100 + 27 - 23 + 16;
    }

    public static boolean _manor() {
        return (!zambia$ ? 24 - 35 + 25 - 12 + -1 : 140 - 200 + 116 + -56) != 0;
    }
}

