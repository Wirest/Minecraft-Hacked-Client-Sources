/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ChatAllowedCharacters
 */
package delta;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class Class101
extends Gui {
    private int towers$;
    public boolean lenders$;
    private final int anxiety$;
    private boolean swiss$;
    private int plans$;
    private int jonathan$ = 234 - 378 + 373 - 260 + 81;
    private final FontRenderer industry$;
    private final int nissan$;
    private int hearing$;
    private boolean unlike$ = 72 - 136 + 44 + 21;
    private int nuclear$;
    private int pressed$;
    private String suite$ = "";
    private boolean activity$;
    private final int qualify$;
    private int annually$;
    private final int points$;
    private boolean province$;

    public void gXNh(boolean bl) {
        if (bl && !this.lenders$) {
            this.nuclear$ = 95 - 130 + 88 - 35 + -18;
        }
        this.lenders$ = bl;
    }

    public void T4kK(int n) {
        if (this.suite$.length() != 0) {
            if (this.hearing$ != this.annually$) {
                this.y81b("");
            } else {
                int n2 = n < 0 ? 38 - 69 + 14 - 1 + 19 : 198 - 211 + 105 + -92;
                int n3 = n2 != 0 ? this.annually$ + n : this.annually$;
                int n4 = n2 != 0 ? this.annually$ : this.annually$ + n;
                String string = "";
                if (n3 >= 0) {
                    string = this.suite$.substring(230 - 324 + 235 - 45 + -96, n3);
                }
                if (n4 < this.suite$.length()) {
                    string = String.valueOf(String.valueOf(string)) + this.suite$.substring(n4);
                }
                this.suite$ = string;
                if (n2 != 0) {
                    this.YwD7(n);
                }
            }
        }
    }

    public void 2obs(boolean bl) {
        this.province$ = bl;
    }

    public boolean Cna1(char c, int n) {
        if (!this.activity$ || !this.lenders$) {
            return 198 - 342 + 115 - 42 + 71;
        }
        switch (c) {
            case '\u0001': {
                this.edoR();
                this.F6f2(175 - 222 + 48 + -1);
                return 186 - 195 + 85 - 69 + -6;
            }
            case '\u0003': {
                GuiScreen.setClipboardString((String)this.DDR6());
                return 97 - 140 + 102 - 92 + 34;
            }
            case '\u0016': {
                this.y81b(GuiScreen.getClipboardString());
                return 111 - 204 + 134 - 117 + 77;
            }
            case '\u0018': {
                GuiScreen.setClipboardString((String)this.DDR6());
                this.y81b("");
                return 166 - 205 + 22 + 18;
            }
        }
        switch (n) {
            case 14: {
                if (GuiScreen.isCtrlKeyDown()) {
                    this.MVLx(234 - 401 + 273 + -107);
                } else {
                    this.T4kK(146 - 169 + 57 - 36 + 1);
                }
                return 121 - 211 + 54 + 37;
            }
            case 199: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.F6f2(48 - 89 + 49 - 40 + 32);
                } else {
                    this.EGRR();
                }
                return 56 - 59 + 53 + -49;
            }
            case 203: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.F6f2(this.mRnI(232 - 445 + 87 - 20 + 145, this.V8pf()));
                    } else {
                        this.F6f2(this.V8pf() - (176 - 192 + 137 - 7 + -113));
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.Zvdp(this.nCFs(241 - 268 + 36 + -10));
                } else {
                    this.YwD7(100 - 153 + 147 - 57 + -38);
                }
                return 116 - 207 + 56 + 36;
            }
            case 205: {
                if (GuiScreen.isShiftKeyDown()) {
                    if (GuiScreen.isCtrlKeyDown()) {
                        this.F6f2(this.mRnI(176 - 317 + 96 + 46, this.V8pf()));
                    } else {
                        this.F6f2(this.V8pf() + (163 - 219 + 84 + -27));
                    }
                } else if (GuiScreen.isCtrlKeyDown()) {
                    this.Zvdp(this.nCFs(143 - 182 + 106 + -66));
                } else {
                    this.YwD7(192 - 278 + 175 - 172 + 84);
                }
                return 151 - 158 + 48 - 10 + -30;
            }
            case 207: {
                if (GuiScreen.isShiftKeyDown()) {
                    this.F6f2(this.suite$.length());
                } else {
                    this.edoR();
                }
                return 206 - 318 + 163 - 159 + 109;
            }
            case 211: {
                if (GuiScreen.isCtrlKeyDown()) {
                    this.MVLx(193 - 327 + 70 - 24 + 89);
                } else {
                    this.T4kK(81 - 134 + 12 - 6 + 48);
                }
                return 230 - 306 + 298 - 142 + -79;
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter((char)c)) {
            this.y81b(Character.toString(c));
            return 48 - 85 + 66 + -28;
        }
        return 187 - 369 + 87 - 83 + 178;
    }

    public void awMx(int n) {
        this.towers$ = n;
    }

    public void MVLx(int n) {
        if (this.suite$.length() != 0) {
            if (this.hearing$ != this.annually$) {
                this.y81b("");
            } else {
                this.T4kK(this.nCFs(n) - this.annually$);
            }
        }
    }

    public void YwD7(int n) {
        this.Zvdp(this.hearing$ + n);
    }

    public void Sk63() {
        this.nuclear$ += 125 - 219 + 151 - 56 + 0;
    }

    public void EGRR() {
        this.Zvdp(167 - 245 + 74 + 4);
    }

    public void oIir(int n, int n2, int n3) {
        int n4;
        int n5 = n4 = n >= this.anxiety$ && n < this.anxiety$ + this.nissan$ && n2 >= this.points$ && n2 < this.points$ + this.qualify$ ? 220 - 352 + 84 - 26 + 75 : 118 - 168 + 101 + -51;
        if (this.swiss$) {
            this.gXNh(this.activity$ && n4 != 0 ? 274 - 410 + 240 + -103 : 117 - 175 + 23 - 2 + 37);
        }
        if (this.lenders$ && n3 == 0) {
            int n6 = n - this.anxiety$;
            if (this.unlike$) {
                n6 -= 4;
            }
            String string = this.industry$.trimStringToWidth(this.suite$.substring(this.plans$), this.x0Se());
            this.Zvdp(this.industry$.trimStringToWidth(string, n6).length() + this.plans$);
        }
    }

    public int qZQH() {
        return this.annually$;
    }

    public int nCFs(int n) {
        return this.mRnI(n, this.qZQH());
    }

    public boolean LnRW() {
        return this.province$;
    }

    public int mRnI(int n, int n2) {
        return this.eZj8(n, this.qZQH(), 70 - 112 + 80 + -37);
    }

    public void DZwN(int n) {
        this.jonathan$ = n;
        if (this.suite$.length() > n) {
            this.suite$ = this.suite$.substring(269 - 520 + 212 + 39, n);
        }
    }

    public void TBH0(boolean bl) {
        this.unlike$ = bl;
    }

    public int x0Se() {
        return this.dAfU() ? this.nissan$ - (272 - 441 + 370 + -193) : this.nissan$;
    }

    public void y81b(String string) {
        int n;
        String string2 = "";
        String string3 = ChatAllowedCharacters.filerAllowedCharacters((String)string);
        int n2 = this.annually$ < this.hearing$ ? this.annually$ : this.hearing$;
        int n3 = this.annually$ < this.hearing$ ? this.hearing$ : this.annually$;
        int n4 = this.jonathan$ - this.suite$.length() - (n2 - this.hearing$);
        int n5 = 97 - 165 + 126 - 87 + 29;
        if (this.suite$.length() > 0) {
            string2 = String.valueOf(String.valueOf(string2)) + this.suite$.substring(91 - 94 + 74 - 27 + -44, n2);
        }
        if (n4 < string3.length()) {
            string2 = String.valueOf(String.valueOf(string2)) + string3.substring(151 - 247 + 135 - 121 + 82, n4);
            n = n4;
        } else {
            string2 = String.valueOf(String.valueOf(string2)) + string3;
            n = string3.length();
        }
        if (this.suite$.length() > 0 && n3 < this.suite$.length()) {
            string2 = String.valueOf(String.valueOf(string2)) + this.suite$.substring(n3);
        }
        this.suite$ = string2.replaceAll(" ", "");
        this.YwD7(n2 - this.hearing$ + n);
    }

    public void Zvdp(int n) {
        this.annually$ = n;
        int n2 = this.suite$.length();
        if (this.annually$ < 0) {
            this.annually$ = 23 - 30 + 20 + -13;
        }
        if (this.annually$ > n2) {
            this.annually$ = n2;
        }
        this.F6f2(this.annually$);
    }

    public String DDR6() {
        int n = this.annually$ < this.hearing$ ? this.annually$ : this.hearing$;
        int n2 = this.annually$ < this.hearing$ ? this.hearing$ : this.annually$;
        return this.suite$.substring(n, n2);
    }

    public void wX1p(boolean bl) {
        this.swiss$ = bl;
    }

    public Class101(FontRenderer fontRenderer, int n, int n2, int n3, int n4) {
        this.swiss$ = 52 - 55 + 17 - 12 + -1;
        this.lenders$ = 252 - 413 + 80 - 4 + 85;
        this.activity$ = 234 - 397 + 209 - 207 + 162;
        this.plans$ = 62 - 85 + 69 + -46;
        this.annually$ = 165 - 170 + 91 - 5 + -81;
        this.hearing$ = 221 - 421 + 78 + 122;
        this.towers$ = 147 - 250 + 102 + 0xE0E0E1;
        this.pressed$ = 78 - 88 + 18 + 7368808;
        this.province$ = 129 - 152 + 144 - 70 + -50;
        this.industry$ = fontRenderer;
        this.anxiety$ = n;
        this.points$ = n2;
        this.nissan$ = n3;
        this.qualify$ = n4;
    }

    public boolean dAfU() {
        return this.unlike$;
    }

    public String U1h1() {
        return this.suite$;
    }

    public int V8pf() {
        return this.hearing$;
    }

    public void edoR() {
        this.Zvdp(this.suite$.length());
    }

    public void F6f2(int n) {
        int n2 = this.suite$.length();
        if (n > n2) {
            n = n2;
        }
        if (n < 0) {
            n = 207 - 376 + 191 - 170 + 148;
        }
        this.hearing$ = n;
        if (this.industry$ != null) {
            if (this.plans$ > n2) {
                this.plans$ = n2;
            }
            int n3 = this.x0Se();
            String string = this.industry$.trimStringToWidth(this.suite$.substring(this.plans$), n3);
            int n4 = string.length() + this.plans$;
            if (n == this.plans$) {
                this.plans$ -= this.industry$.trimStringToWidth(this.suite$, n3, 128 - 225 + 128 + -30).length();
            }
            if (n > n4) {
                this.plans$ += n - n4;
            } else if (n <= this.plans$) {
                this.plans$ -= this.plans$ - n;
            }
            if (this.plans$ < 0) {
                this.plans$ = 126 - 187 + 155 - 16 + -78;
            }
            if (this.plans$ > n2) {
                this.plans$ = n2;
            }
        }
    }

    public void a8aP(String string) {
        this.suite$ = string.length() > this.jonathan$ ? string.substring(163 - 186 + 40 - 18 + 1, this.jonathan$) : string;
        this.edoR();
    }

    public void e5ch() {
        if (this.LnRW()) {
            if (this.dAfU()) {
                Gui.drawRect((int)(this.anxiety$ - (119 - 148 + 73 - 12 + -31)), (int)(this.points$ - (250 - 327 + 57 - 53 + 74)), (int)(this.anxiety$ + this.nissan$ + (248 - 338 + 297 - 234 + 28)), (int)(this.points$ + this.qualify$ + (211 - 234 + 215 + -191)), (int)(32 - 38 + 14 + -6250344));
                Gui.drawRect((int)this.anxiety$, (int)this.points$, (int)(this.anxiety$ + this.nissan$), (int)(this.points$ + this.qualify$), (int)(173 - 316 + 40 - 40 + -16777073));
            }
            int n = this.activity$ ? this.towers$ : this.pressed$;
            int n2 = this.annually$ - this.plans$;
            int n3 = this.hearing$ - this.plans$;
            String string = this.industry$.trimStringToWidth(this.suite$.substring(this.plans$), this.x0Se());
            int n4 = n2 >= 0 && n2 <= string.length() ? 247 - 431 + 38 + 147 : 233 - 251 + 133 + -115;
            int n5 = this.lenders$ && this.nuclear$ / (86 - 93 + 23 - 3 + -7) % (247 - 440 + 128 + 67) == 0 && n4 != 0 ? 170 - 213 + 171 + -127 : 254 - 268 + 109 - 83 + -12;
            int n6 = this.unlike$ ? this.anxiety$ + (232 - 357 + 163 - 149 + 115) : this.anxiety$;
            int n7 = this.unlike$ ? this.points$ + (this.qualify$ - (198 - 353 + 312 + -149)) / (179 - 233 + 162 + -106) : this.points$;
            int n8 = n6;
            if (n3 > string.length()) {
                n3 = string.length();
            }
            if (string.length() > 0) {
                if (n4 != 0) {
                    string.substring(222 - 409 + 144 - 81 + 124, n2);
                }
                n8 = Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(this.suite$.replaceAll("(?s).", "*"), n6, n7, n);
            }
            int n9 = this.annually$ < this.suite$.length() || this.suite$.length() >= this.YOFW() ? 85 - 169 + 66 + 19 : 54 - 55 + 39 - 23 + -15;
            int n10 = n8;
            if (n4 == 0) {
                n10 = n2 > 0 ? n6 + this.nissan$ : n6;
            } else if (n9 != 0) {
                n10 = n8 - (145 - 286 + 16 + 126);
                --n8;
            }
            if (string.length() > 0 && n4 != 0 && n2 < string.length()) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string.substring(n2), n8, n7, n);
            }
            if (n5 != 0) {
                if (n9 != 0) {
                    Gui.drawRect((int)n10, (int)(n7 - (133 - 216 + 201 + -117)), (int)(n10 + (229 - 312 + 158 + -74)), (int)(n7 + (55 - 70 + 51 + -35) + this.industry$.FONT_HEIGHT), (int)(97 - 126 + 6 - 6 + -3092243));
                } else {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("_", n10, n7, n);
                }
            }
            if (n3 != n2) {
                int n11 = n6 + this.industry$.getStringWidth(string.substring(178 - 286 + 271 - 49 + -114, n3));
            }
        }
    }

    public int YOFW() {
        return this.jonathan$;
    }

    public int eZj8(int n, int n2, boolean bl) {
        int n3 = n2;
        int n4 = n < 0 ? 87 - 92 + 67 + -61 : 211 - 357 + 88 + 58;
        int n5 = Math.abs(n);
        for (int i = 202 - 322 + 296 - 35 + -141; i < n5; ++i) {
            if (n4 == 0) {
                int n6 = this.suite$.length();
                if ((n3 = this.suite$.indexOf(164 - 319 + 181 - 82 + 88, n3)) == 153 - 278 + 181 + -57) {
                    n3 = n6;
                    continue;
                }
                while (bl && n3 < n6 && this.suite$.charAt(n3) == 124 - 144 + 126 + -74) {
                    ++n3;
                }
                continue;
            }
            while (bl && n3 > 0 && this.suite$.charAt(n3 - (103 - 171 + 87 + -18)) == 146 - 229 + 75 - 13 + 53) {
                --n3;
            }
            while (n3 > 0 && this.suite$.charAt(n3 - (111 - 210 + 192 - 2 + -90)) != 262 - 318 + 206 + -118) {
                --n3;
            }
        }
        return n3;
    }

    public boolean 03Mg() {
        return this.lenders$;
    }
}

