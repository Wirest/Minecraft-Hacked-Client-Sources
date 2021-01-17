/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.launchwrapper.Launch
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.Session
 *  org.lwjgl.opengl.GL11
 */
package delta.guis;

import com.mojang.authlib.exceptions.AuthenticationException;
import delta.Class101;
import delta.Class144;
import delta.Class146;
import delta.Class159;
import delta.Class19;
import delta.Class69;
import delta.utils.LoginUtils;
import delta.utils.RenderUtils;
import delta.utils.TimeHelper;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;

public class AltLogin
extends Class19 {
    private GuiTextField speak$;
    private TimeHelper aircraft$;
    private Class101 velvet$;
    private GuiScreen feedback$;
    private GuiTextField potato$;

    protected void _offers(char c, int n) {
        this.speak$.textboxKeyTyped(c, n);
        this.velvet$.Cna1(c, n);
        this.potato$.textboxKeyTyped(c, n);
        super.func_73869_a(c, n);
    }

    public void _queen() {
        super.func_73876_c();
    }

    public void _creature(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        this.func_146276_q_();
        Gui.drawRect((int)(103 - 131 + 126 + -98), (int)(62 - 91 + 47 + -18), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)(55 - 79 + 22 + -16448249));
        int n3 = 250 - 452 + 266 - 148 + 84;
        String string = "OfflinePlayer:" + this.field_146297_k.getSession().getUsername();
        UUID uUID = UUID.nameUUIDFromBytes(string.getBytes());
        if (uUID.equals(this.field_146297_k.getSession().func_148256_e().getId())) {
            n3 = 51 - 92 + 17 + 25;
        }
        Class69.develops$._college("Username: " + (Object)EnumChatFormatting.RED + this.field_146297_k.getSession().getUsername(), 2.0, 1.0, 26 - 43 + 20 + -4);
        Class69.develops$._college("Type: " + (Object)EnumChatFormatting.GRAY + (n3 != 0 ? "Cracked" : "Premium"), 2.0, 14.0, 211 - 416 + 84 - 20 + 140);
        GL11.glPushMatrix();
        GL11.glScaled((double)0.2, (double)0.2, (double)0.2);
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        this.field_146297_k.getTextureManager().bindTexture(RenderUtils._dance());
        this.func_73729_b(119 - 176 + 46 - 31 + 62, 230 - 298 + 234 - 58 + 42, 263 - 280 + 67 + -50, 64 - 83 + 44 + -25, 231 - 356 + 222 + 159, 219 - 317 + 166 - 88 + 276);
        GL11.glPopMatrix();
        Gui.drawRect((int)(this.field_146294_l / (239 - 363 + 62 - 2 + 66) - (128 - 211 + 115 + 93)), (int)(scaledResolution.getScaledHeight() / (236 - 238 + 137 + -133) - (110 - 123 + 16 + 77)), (int)(this.field_146294_l / (269 - 410 + 229 - 141 + 55) - (39 - 66 + 21 - 12 + 143) + (187 - 305 + 113 + 255)), (int)(scaledResolution.getScaledHeight() / (143 - 227 + 136 - 46 + -4) - (129 - 184 + 147 + -12) + (94 - 176 + 121 + -19)), (int)(121 - 124 + 16 + -872415245));
        Gui.drawRect((int)(this.field_146294_l / (83 - 133 + 53 + -1) - (210 - 387 + 167 - 158 + 293)), (int)(scaledResolution.getScaledHeight() / (155 - 231 + 170 + -92) - (100 - 183 + 2 - 2 + 123)), (int)(this.field_146294_l / (140 - 224 + 93 - 9 + 2) - (107 - 192 + 2 - 2 + 210) + (196 - 348 + 185 + 217)), (int)(scaledResolution.getScaledHeight() / (195 - 380 + 221 + -34) - (185 - 215 + 102 + -32) + (89 - 147 + 33 + 45)), (int)(64 - 100 + 100 + -872415296));
        Gui.drawRect((int)(this.field_146294_l / (72 - 84 + 81 - 6 + -61) - (224 - 349 + 259 + -9)), (int)(scaledResolution.getScaledHeight() / (259 - 500 + 130 + 113)), (int)(this.field_146294_l / (93 - 141 + 48 + 2) - (265 - 387 + 47 + 200) + (114 - 192 + 4 - 1 + 325)), (int)(scaledResolution.getScaledHeight() / (156 - 264 + 120 + -10) + (191 - 338 + 127 + 40)), (int)(136 - 192 + 8 - 8 + -872415176));
        Gui.drawRect((int)(this.field_146294_l / (21 - 22 + 19 - 5 + -11) - (26 - 45 + 26 - 11 + 129)), (int)(scaledResolution.getScaledHeight() / (64 - 67 + 53 + -48) - (238 - 432 + 174 + 100) + (258 - 501 + 27 + 235)), (int)(this.field_146294_l / (173 - 213 + 161 - 46 + -73) - (172 - 211 + 126 - 63 + 101) + (223 - 388 + 336 - 200 + 279)), (int)(scaledResolution.getScaledHeight() / (148 - 252 + 252 - 102 + -44) - (203 - 266 + 237 + -94) + (68 - 133 + 54 - 54 + 85)), (int)(37 - 56 + 21 - 11 + -6155861));
        Gui.drawRect((int)(this.field_146294_l / (234 - 387 + 73 - 41 + 123) - (73 - 102 + 1 + 153)), (int)(scaledResolution.getScaledHeight() / (75 - 121 + 43 + 5) - (250 - 451 + 140 + 101) + (173 - 217 + 25 - 2 + 40)), (int)(this.field_146294_l / (115 - 125 + 89 - 60 + -17) - (58 - 82 + 12 + 137) + (168 - 308 + 270 + 120)), (int)(scaledResolution.getScaledHeight() / (240 - 455 + 407 + -190) - (46 - 70 + 47 + 17) + (123 - 132 + 98 - 36 + -33)), (int)(253 - 374 + 74 + -6155823));
        Gui.drawRect((int)(this.field_146294_l / (154 - 166 + 17 - 6 + 3) - (167 - 196 + 24 - 22 + 152)), (int)(scaledResolution.getScaledHeight() / (52 - 57 + 24 - 19 + 2) + (144 - 246 + 116 + 5)), (int)(this.field_146294_l / (123 - 200 + 132 + -53) - (240 - 416 + 174 - 113 + 240) + (35 - 51 + 40 - 29 + 255)), (int)(scaledResolution.getScaledHeight() / (256 - 277 + 236 - 7 + -206) + (168 - 309 + 218 + -57)), (int)(140 - 179 + 24 + -6155855));
        Class69.develops$._college("Mail:", this.field_146294_l / (119 - 143 + 94 - 45 + -23) - (150 - 169 + 15 + 129), scaledResolution.getScaledHeight() / (56 - 84 + 33 - 10 + 7) - (116 - 213 + 201 + -11) - (241 - 302 + 72 - 58 + 49), 242 - 272 + 102 + -73);
        this.speak$.drawTextBox();
        Class69.develops$._college("Password:", this.field_146294_l / (214 - 243 + 49 - 23 + 5) - (135 - 269 + 8 - 5 + 256), scaledResolution.getScaledHeight() / (126 - 140 + 41 + -25) - (204 - 268 + 5 - 1 + 153) + (74 - 84 + 9 - 6 + 47) - (245 - 333 + 54 + 36), 64 - 87 + 73 - 30 + -21);
        this.velvet$.e5ch();
        Class69.develops$._college("Combo (mail:pass):", this.field_146294_l / (96 - 101 + 91 - 9 + -75) - (74 - 118 + 47 - 6 + 128), scaledResolution.getScaledHeight() / (168 - 193 + 42 + -15) - (156 - 160 + 45 + 52) + (157 - 198 + 193 - 74 + -38) + (254 - 466 + 82 + 170) - (127 - 249 + 178 - 151 + 97), 113 - 183 + 162 + -93);
        this.potato$.drawTextBox();
        super.func_73863_a(n, n2, f);
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        this.speak$ = new GuiTextField(this.field_146297_k.fontRenderer, this.field_146294_l / (191 - 206 + 175 - 165 + 7) - (31 - 51 + 51 + 94) + (237 - 333 + 200 - 145 + 46), scaledResolution.getScaledHeight() / (143 - 199 + 22 - 4 + 40) - (272 - 492 + 231 - 87 + 156) + (70 - 125 + 37 - 36 + 60), 248 - 452 + 49 + 395, 148 - 259 + 157 + -26);
        this.speak$.setMaxStringLength(250 - 266 + 157 - 145 + 104);
        this.speak$.setEnableBackgroundDrawing(68 - 127 + 70 + -11);
        this.velvet$ = new Class101(this.field_146297_k.fontRenderer, this.field_146294_l / (100 - 163 + 134 + -69) - (191 - 380 + 219 + 95) + (163 - 261 + 207 + -104), scaledResolution.getScaledHeight() / (164 - 247 + 88 - 53 + 50) - (227 - 411 + 105 - 17 + 136) + (88 - 147 + 74 + -9), 113 - 159 + 151 + 145, 45 - 77 + 62 - 32 + 22);
        this.velvet$.DZwN(265 - 407 + 56 - 13 + 199);
        this.velvet$.TBH0(272 - 465 + 178 - 161 + 176);
        this.potato$ = new GuiTextField(this.field_146297_k.fontRenderer, this.field_146294_l / (147 - 157 + 65 + -53) - (109 - 115 + 63 - 46 + 114) + (141 - 232 + 97 - 17 + 16), scaledResolution.getScaledHeight() / (172 - 286 + 6 - 2 + 112) + (84 - 159 + 51 + 30), 137 - 240 + 81 + 272, 56 - 70 + 3 - 2 + 33);
        this.potato$.setMaxStringLength(137 - 265 + 34 + 284);
        this.potato$.setEnableBackgroundDrawing(116 - 155 + 110 - 31 + -40);
        this.field_146292_n.add(new Class144(153 - 214 + 199 - 93 + -42, this.field_146294_l / (99 - 120 + 23 - 8 + 8) - (89 - 113 + 26 - 25 + 148), scaledResolution.getScaledHeight() / (50 - 62 + 42 + -28) + (60 - 102 + 9 + 58), 206 - 376 + 104 + 316, 144 - 222 + 161 + -63, "Login"));
        this.field_146292_n.add(new Class144(26 - 42 + 16 + 4, this.field_146294_l / (225 - 275 + 134 + -82) - (207 - 371 + 191 + 98), scaledResolution.getScaledHeight() / (55 - 101 + 32 + 16) + (104 - 144 + 135 - 93 + 48), 235 - 368 + 22 + 361, 198 - 280 + 25 + 77, "Clipboard"));
        this.field_146292_n.add(new Class144(151 - 277 + 17 + 114, this.field_146294_l / (219 - 429 + 84 + 128) - (87 - 96 + 6 - 4 + 132), scaledResolution.getScaledHeight() / (184 - 240 + 135 + -77) + (253 - 500 + 365 - 33 + -10), 130 - 239 + 113 - 15 + 261, 236 - 295 + 266 - 200 + 13, "Back"));
    }

    protected void _crash(int n, int n2, int n3) {
        this.speak$.mouseClicked(n, n2, n3);
        this.velvet$.oIir(n, n2, n3);
        this.potato$.mouseClicked(n, n2, n3);
        super.func_73864_a(n, n2, n3);
    }

    protected void _safari(GuiButton guiButton) {
        if (guiButton.id == 143 - 229 + 17 + 72) {
            this._closure();
        }
        if (guiButton.id == 186 - 251 + 153 - 31 + -53) {
            String string;
            try {
                string = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            }
            catch (Exception exception) {
                return;
            }
            if (string != null) {
                this.potato$.setText(string);
                this._closure();
                this.potato$.setText("");
            }
        }
        if (guiButton.id == 165 - 323 + 227 - 25 + -39) {
            this.field_146297_k.displayGuiScreen(this.feedback$);
        }
        super.func_146284_a(guiButton);
    }

    private void _closure() {
        if (this.potato$.getText().length() != 0 && this.potato$.getText().contains(":") && !this.potato$.getText().endsWith(":")) {
            this.speak$.setText(this.potato$.getText().split(":")[180 - 225 + 10 + 35]);
            this.velvet$.a8aP(this.potato$.getText().split(":")[268 - 427 + 75 - 63 + 148]);
            this.potato$.setText("");
        }
        if (this.speak$.getText().equalsIgnoreCase("")) {
            return;
        }
        Session session = null;
        if (this.velvet$.U1h1().equalsIgnoreCase("")) {
            if (this.speak$.getText().contains("@")) {
                this.field_146297_k.displayGuiScreen((GuiScreen)new Class146(this, "Login Error", "Invalid login credentials"));
                return;
            }
            session = LoginUtils._again((String)this.speak$.getText());
        } else {
            try {
                session = LoginUtils._expedia((String)this.speak$.getText(), (String)this.velvet$.U1h1());
            }
            catch (AuthenticationException authenticationException) {
                this.field_146297_k.displayGuiScreen((GuiScreen)new Class146(this, "Login Error", authenticationException.getMessage()));
                return;
            }
        }
        try {
            int n = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment") == false ? 182 - 353 + 288 + -116 : 267 - 322 + 146 - 98 + 7;
            Class159 class159 = Class159._lover((Object)Minecraft.getMinecraft());
            class159._glory(n != 0 ? "field_71449_j" : "session", (Object)session);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
    }

    public AltLogin(GuiScreen guiScreen) {
        this.feedback$ = guiScreen;
        this.aircraft$ = new TimeHelper();
    }
}

