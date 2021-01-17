/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 */
package delta;

import delta.Class149;
import delta.Class172;
import delta.Class98;
import delta.OVYt;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Class69 {
    public static Class98 details$;
    private static String[] poultry$;
    public static Class98 develops$;
    public static FontRenderer somalia$;
    public static Class98 eagles$;
    public static Class98 concepts$;
    public static Class98 centres$;
    public static Class98 existing$;

    static {
        Object object;
        Class69._adipex();
        somalia$ = Minecraft.getMinecraft().fontRenderer;
        Class172 class172 = new Class172(OVYt.968L.FS1x((String)poultry$[0], (int)586314787));
        Font font = null;
        try {
            object = new FileInputStream(class172._horror());
            font = Font.createFont(190 - 260 + 251 + -181, (InputStream)object);
        }
        catch (FontFormatException | IOException exception) {
            exception.printStackTrace();
        }
        if (font == null) {
            font = new Font(OVYt.968L.FS1x((String)poultry$[1], (int)-1278380167), 251 - 370 + 294 + -175, 144 - 221 + 84 - 60 + 153);
            System.out.println(OVYt.968L.FS1x((String)poultry$[2], (int)1942439243));
        }
        object = new char[216 - 351 + 50 - 21 + 362];
        for (int i = 115 - 188 + 120 - 103 + 56; i < ((Object)object).length; ++i) {
            object[i] = (char)i;
        }
        Class149 class149 = new Class149(font.deriveFont(221 - 348 + 288 + -161, 100.0f), 92 - 143 + 129 + -77, 182 - 300 + 296 + -177);
        class149._codes((char[])object);
        class149._cliff();
        Class149 class1492 = new Class149(font.deriveFont(221 - 420 + 291 + -92, 74.0f), 23 - 40 + 9 - 4 + 13, 273 - 487 + 85 - 19 + 149);
        class1492._codes((char[])object);
        class1492._cliff();
        Class149 class1493 = new Class149(font.deriveFont(124 - 144 + 86 - 78 + 12, 60.0f), 256 - 288 + 89 - 70 + 14, 21 - 24 + 1 + 3, 163 - 254 + 179 - 178 + 91);
        class1493._codes((char[])object);
        class1493._cliff();
        Class149 class1494 = new Class149(font.deriveFont(201 - 211 + 115 - 13 + -92, 37.0f), 65 - 88 + 75 + -51, 189 - 310 + 104 + 18);
        class1494._codes((char[])object);
        class1494._cliff();
        Class149 class1495 = new Class149(font.deriveFont(163 - 255 + 122 + -30, 24.0f), 102 - 181 + 154 + -74, 161 - 319 + 223 + -64);
        class1495._codes((char[])object);
        class1495._cliff();
        Class149 class1496 = new Class149(font.deriveFont(134 - 211 + 116 - 58 + 19, 21.0f), 225 - 327 + 199 + -96, 189 - 215 + 27 - 12 + 12);
        class1496._codes((char[])object);
        class1496._cliff();
        centres$ = new Class98(class149, class149, class149, class149);
        eagles$ = new Class98(class1492, class1492, class1492, class1492);
        existing$ = new Class98(class1494, class1494, class1494, class1494);
        develops$ = new Class98(class1495, class1495, class1495, class1495);
        concepts$ = new Class98(class1493, class1493, class1493, class1493);
        details$ = new Class98(class1496, class1496, class1496, class1496);
    }

    private static void _adipex() {
        poultry$ = new String[]{"\u744b\u7457\u7457\u7453\u7450\u7419\u740c\u740c\u744d\u7448\u744c\u7450\u744e\u744c\u7450\u740d\u7444\u744a\u7457\u744b\u7456\u7441\u740d\u744a\u744c\u740c\u7442\u7450\u7450\u7446\u7457\u7450\u740c\u7445\u744c\u744d\u7457\u740c\u7440\u744c\u744e\u7445\u744c\u7451\u7457\u7442\u7442\u740d\u7457\u7457\u7445", "\u771a\u7716\u7714\u771f\u7716\u770b\u770d\u7718\u7718", "\u453c\u452a\u4539\u4525\u4522\u4525\u452c\u4571\u456b\u452d\u4524\u4525\u453f\u456b\u4522\u4538\u456b\u4538\u4523\u4522\u453f"};
    }
}

