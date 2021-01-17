/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  delta.OVYt$968L
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiLanguage
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSelectWorld
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Project
 */
package delta;

import delta.Class144;
import delta.Class150;
import delta.Class19;
import delta.Class22;
import delta.Class47;
import delta.Class60;
import delta.Class69;
import delta.Class7;
import delta.OVYt;
import delta.guis.AltLogin;
import delta.utils.ColorUtils;
import delta.utils.TimeHelper;
import delta.utils.Wrapper;
import java.util.Random;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class Class135
extends Class19 {
    public static Class7 items$;
    private DynamicTexture routines$;
    private int brooklyn$;
    private ResourceLocation branches$;
    private static String[] papers$;
    private TimeHelper preview$;
    private static final ResourceLocation[] intent$;
    public static Class60 suited$;
    private int schools$;

    private static void _occurred() {
        papers$ = new String[]{"\u25fb\u25e9\u25f5\u25b2\u25fe\u25e9\u25e8\u25e8\u25f3\u25f2\u25b2\u25ec\u25ee\u25f9\u25ef\u25ef", "\u9c85\u9c97\u9c8b\u9ccc\u9c80\u9c97\u9c96\u9c96\u9c8d\u9c8c\u9ccc\u9c92\u9c90\u9c87\u9c91\u9c91", "\u889d\u888f\u8893\u88d4\u8898\u888f\u888e\u888e\u8895\u8894\u88d4\u888a\u8888\u889f\u8889\u8889", "\ud6cd\ud6df\ud6c3\ud684\ud6c8\ud6df\ud6de\ud6de\ud6c5\ud6c4\ud684\ud6da\ud6d8\ud6cf\ud6d9\ud6d9", "\u72f2\u72e0\u72fc\u72bb\u72f7\u72e0\u72e1\u72e1\u72fa\u72fb\u72bb\u72e5\u72e7\u72f0\u72e6\u72e6", "\ub88a\ub898\ub884\ub8c3\ub88f\ub898\ub899\ub899\ub882\ub883\ub8c3\ub89d\ub89f\ub888\ub89e\ub89e", "\ufe81\ufe93\ufe8f\ufec8\ufe84\ufe93\ufe92\ufe92\ufe89\ufe88\ufec8\ufe96\ufe94\ufe83\ufe95\ufe95", "\u39ee\u39ff\u39e2\u39ee\u39ef\u39e8\u39ff\u39e9\u39b5\u39fd\u39ef\u39f3\u39b5\u39ee\u39f3\u39ee\u39f6\u39ff\u39b5\u39f8\u39fb\u39f9\u39f1\u39fd\u39e8\u39f5\u39ef\u39f4\u39fe\u39b5\u39ea\u39fb\u39f4\u39f5\u39e8\u39fb\u39f7\u39fb\u39c5\u39aa\u39b4\u39ea\u39f4\u39fd", "\u9da4\u9db5\u9da8\u9da4\u9da5\u9da2\u9db5\u9da3\u9dff\u9db7\u9da5\u9db9\u9dff\u9da4\u9db9\u9da4\u9dbc\u9db5\u9dff\u9db2\u9db1\u9db3\u9dbb\u9db7\u9da2\u9dbf\u9da5\u9dbe\u9db4\u9dff\u9da0\u9db1\u9dbe\u9dbf\u9da2\u9db1\u9dbd\u9db1\u9d8f\u9de1\u9dfe\u9da0\u9dbe\u9db7", "\u504b\u505a\u5047\u504b\u504a\u504d\u505a\u504c\u5010\u5058\u504a\u5056\u5010\u504b\u5056\u504b\u5053\u505a\u5010\u505d\u505e\u505c\u5054\u5058\u504d\u5050\u504a\u5051\u505b\u5010\u504f\u505e\u5051\u5050\u504d\u505e\u5052\u505e\u5060\u500d\u5011\u504f\u5051\u5058", "\ubc77\ubc66\ubc7b\ubc77\ubc76\ubc71\ubc66\ubc70\ubc2c\ubc64\ubc76\ubc6a\ubc2c\ubc77\ubc6a\ubc77\ubc6f\ubc66\ubc2c\ubc61\ubc62\ubc60\ubc68\ubc64\ubc71\ubc6c\ubc76\ubc6d\ubc67\ubc2c\ubc73\ubc62\ubc6d\ubc6c\ubc71\ubc62\ubc6e\ubc62\ubc5c\ubc30\ubc2d\ubc73\ubc6d\ubc64", "\u9a16\u9a07\u9a1a\u9a16\u9a17\u9a10\u9a07\u9a11\u9a4d\u9a05\u9a17\u9a0b\u9a4d\u9a16\u9a0b\u9a16\u9a0e\u9a07\u9a4d\u9a00\u9a03\u9a01\u9a09\u9a05\u9a10\u9a0d\u9a17\u9a0c\u9a06\u9a4d\u9a12\u9a03\u9a0c\u9a0d\u9a10\u9a03\u9a0f\u9a03\u9a3d\u9a56\u9a4c\u9a12\u9a0c\u9a05", "\ude8b\ude9a\ude87\ude8b\ude8a\ude8d\ude9a\ude8c\uded0\ude98\ude8a\ude96\uded0\ude8b\ude96\ude8b\ude93\ude9a\uded0\ude9d\ude9e\ude9c\ude94\ude98\ude8d\ude90\ude8a\ude91\ude9b\uded0\ude8f\ude9e\ude91\ude90\ude8d\ude9e\ude92\ude9e\udea0\udeca\uded1\ude8f\ude91\ude98", "\u4a01\u4a0d\u4a06\u4a07\u4a58\u4a42", "\ue450\ue45c\ue457\ue456\ue409", "\uda69\uda56\uda41\uda5c", "\uee86\ueea7\ueeae\ueeb6\ueea3", "", "\u922d\u920c\u9205\u921d\u9208", "\u9aa4\u9a85\u9a8c\u9a94\u9a81", "\ufefd\ufedc\ufed5\ufecd\ufed8", "\uf0c4\uf0e5\uf0ec\uf0f4\uf0e1", "\u5455\u5474\u547d\u5465\u5470", "\udd52\udd73\udd7a\udd62\udd77", "\u80d4\u80f5\u80fc\u80e4\u80f1", "\u750c\u753f\u7528\u7529\u7533\u7535\u7534\u757a\u7538", "\u4cce\u4cd3\u4cca", "\u2751\u2762\u2775\u2774\u276e\u2768\u2769\u2727\u2765\u2734\u2729\u2730", "\u0669", "\u6eb4\u6ebc\u6eb7\u6eac\u6ef7\u6eaa\u6eb0\u6eb7\u6ebe\u6eb5\u6ebc\u6ea9\u6eb5\u6eb8\u6ea0\u6ebc\u6eab", "\u8e3c\u8e34\u8e3f\u8e24\u8e7f\u8e3c\u8e24\u8e3d\u8e25\u8e38\u8e21\u8e3d\u8e30\u8e28\u8e34\u8e23", "\u381c\u3838\u3824\u3839", "\u6c28\u6c0c\u6c10\u6c0d", "", "\u7eb4\u7e8b\u7e8f\u7e92\u7e94\u7e95\u7e88", "\u6801\u683e\u683a\u6827\u6821\u6820\u683d", "\u5050\u507d\u5072\u507b\u5069\u507d\u507b\u5079", "\u912e\u9103\u910c\u9105\u9117\u9103\u9105\u9107", "\ufe47\ufe6b\ufe74\ufe7d\ufe76\ufe6d\ufe63\ufe6c\ufe70\ufe24\ufe49\ufe6b\ufe6e\ufe65\ufe6a\ufe63\ufe24\ufe45\ufe46\ufe2a\ufe24\ufe40\ufe6b\ufe24\ufe6a\ufe6b\ufe70\ufe24\ufe60\ufe6d\ufe77\ufe70\ufe76\ufe6d\ufe66\ufe71\ufe70\ufe61\ufe25", "\u48a7\u488b\u4894\u489d\u4896\u488d\u4883\u488c\u4890\u48c4\u48a9\u488b\u488e\u4885\u488a\u4883\u48c4\u48a5\u48a6\u48ca\u48c4\u48a0\u488b\u48c4\u488a\u488b\u4890\u48c4\u4880\u488d\u4897\u4890\u4896\u488d\u4886\u4891\u4890\u4881\u48c5", "\ueb22\ueb03\ueb0a\ueb12\ueb07\ueb46\ueb04\ueb55\ueb48\ueb51\ueb46\ueb04\ueb1f\ueb46\ueb1e\ueb32\ueb14\ueb2b\ueb39", "\u0b20\u0b23\u0b21\u0b29\u0b25\u0b30\u0b2d\u0b37\u0b2c\u0b26", "\ud55c\ud57e\ud575\ud574\ud52b\ud531", "\u6ea9\u6e84\u6e9c\u6e89\u6e9b", "\u1cea\u1ccb\u1cc2\u1cda\u1ccf", "\uff25\uff19\uff1a\uff19", "\u0375\u034d\u0354\u034c\u0351\u0352\u0357\u034d\u035d\u034d\u034a", "\u8bef\u8bce\u8bc7\u8bdf\u8bca", "\ud93e\ud901\ud905\ud918\ud91e\ud91f\ud902", "\u091c\u0938\u0924\u0939\u0939\u0928\u093f"};
    }

    private void _naturals(int n, int n2, float f) {
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        this.func_146276_q_();
        Gui.drawRect((int)(101 - 155 + 57 + -3), (int)(38 - 51 + 14 - 5 + 4), (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (int)(166 - 217 + 217 - 89 + -15790398));
        if (this.preview$.hasReached(203L - 353L + 111L - 1L + 90L)) {
            this.preview$.setLastMS();
            suited$._quotes(255 - 304 + 120 + -21);
        }
        suited$._clone(1.0, n, n2);
        Class47._divided()._faculty(26.0f, scaledResolution.getScaledHeight() / (234 - 429 + 151 + 46) - (164 - 194 + 177 - 139 + 77), scaledResolution.getScaledWidth() - (147 - 222 + 12 + 90), scaledResolution.getScaledHeight() / (87 - 122 + 59 + -22) - (232 - 425 + 31 + 197), 100 - 165 + 43 + -872415210, 118 - 205 + 196 - 73 + -871757338);
        if (this.schools$ == 29 - 38 + 21 - 20 + 77) {
            Class69.centres$._college((Object)EnumChatFormatting.GRAY + OVYt.968L.FS1x((String)papers$[13], (int)546196066), scaledResolution.getScaledWidth() / (79 - 130 + 48 + 5) - Class69.centres$._commit(OVYt.968L.FS1x((String)papers$[14], (int)1574626355)) - (234 - 360 + 1 - 1 + 128), scaledResolution.getScaledHeight() / (23 - 26 + 8 - 8 + 5) + (27 - 48 + 47 - 33 + 16), 22 - 31 + 25 + -17);
            Class69.centres$._college(OVYt.968L.FS1x((String)papers$[15], (int)482990643), scaledResolution.getScaledWidth() / (149 - 187 + 186 + -146) + (240 - 422 + 322 - 223 + 87), scaledResolution.getScaledHeight() / (190 - 297 + 21 + 88) + (127 - 142 + 62 - 53 + 15), 45 - 82 + 75 - 61 + 10621369);
            super.func_73863_a(n, n2, f);
            return;
        }
        String string = OVYt.968L.FS1x((String)papers$[16], (int)-1660227902).substring(227 - 421 + 9 + 185, 142 - 263 + 96 - 30 + 56) + OVYt.968L.FS1x((String)papers$[17], (int)-140299420) + (Object)EnumChatFormatting.WHITE + OVYt.968L.FS1x((String)papers$[18], (int)396333673).substring(24 - 30 + 5 - 2 + 4, OVYt.968L.FS1x((String)papers$[19], (int)-1676371232).length());
        Class69.centres$._college(string, scaledResolution.getScaledWidth() / (100 - 131 + 89 - 43 + -13) - Class69.centres$._commit(OVYt.968L.FS1x((String)papers$[20], (int)-1059258695)) / (130 - 200 + 34 + 38), scaledResolution.getScaledHeight() / (57 - 104 + 44 + 5) + (90 - 125 + 13 + 31), 110 - 142 + 135 + 10621243);
    }

    static {
        Class135._occurred();
        ResourceLocation[] arrresourceLocation = new ResourceLocation[82 - 113 + 16 - 9 + 30];
        arrresourceLocation[24 - 28 + 13 - 9 + 0] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[7], (int)2096511386));
        arrresourceLocation[42 - 52 + 19 - 13 + 5] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[8], (int)302751184));
        arrresourceLocation[121 - 155 + 154 + -118] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[9], (int)1981501503));
        arrresourceLocation[176 - 281 + 246 + -138] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[10], (int)-1832010749));
        arrresourceLocation[150 - 179 + 94 + -61] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[11], (int)-856581534));
        arrresourceLocation[221 - 382 + 140 - 54 + 80] = new ResourceLocation(OVYt.968L.FS1x((String)papers$[12], (int)-1368269057));
        intent$ = arrresourceLocation;
    }

    public Class135() {
        if (items$ == null) {
            items$ = new Class7(210 - 350 + 27 - 23 + 286);
        }
        if (suited$ == null) {
            suited$ = new Class60(127 - 178 + 122 + 229);
        }
    }

    protected void _mention(int n, int n2, int n3) {
        if (!Wrapper.thong$) {
            super.func_73864_a(n, n2, n3);
            return;
        }
        ScaledResolution scaledResolution = Class22._remedy();
        int n4 = 183 - 245 + 245 + -83;
        int n5 = 184 - 355 + 52 - 11 + 170;
        boolean bl = this._north(n, n2, scaledResolution.getScaledWidth() / (21 - 24 + 12 - 2 + -5) - (177 - 270 + 19 + 164), n4 + (104 - 119 + 43 - 34 + 81) + (193 - 220 + 202 + -155), scaledResolution.getScaledWidth() / (272 - 482 + 449 - 442 + 205) + (39 - 57 + 26 - 7 + 89), n4 + (235 - 333 + 137 - 46 + 82) + (262 - 449 + 303 - 185 + 89) + n5);
        boolean bl2 = this._north(n, n2, scaledResolution.getScaledWidth() / (176 - 270 + 196 - 40 + -60) - (81 - 158 + 86 - 54 + 135), n4 + (49 - 86 + 67 - 10 + 55) + (267 - 434 + 246 + -59) + n5 + (214 - 407 + 152 + 46), scaledResolution.getScaledWidth() / (55 - 71 + 1 + 17) + (262 - 337 + 5 + 160), n4 + (47 - 57 + 22 + 63) + (213 - 292 + 72 - 52 + 79) + n5 + n5 + (26 - 36 + 20 - 3 + -2));
        boolean bl3 = this._north(n, n2, 261 - 488 + 292 - 10 + -25, 195 - 242 + 164 + -117, 59 - 92 + 78 + 83, 271 - 419 + 349 + -171);
        boolean bl4 = this._north(n, n2, 144 - 150 + 143 + -9, 186 - 253 + 98 - 19 + -12, 122 - 141 + 18 + 228, 30 - 50 + 46 + 4);
        boolean bl5 = this._north(n, n2, scaledResolution.getScaledWidth() - (127 - 145 + 78 + 140), 90 - 99 + 40 + -31, scaledResolution.getScaledWidth() - (209 - 308 + 246 - 186 + 114), 172 - 231 + 30 + 59);
        boolean bl6 = this._north(n, n2, scaledResolution.getScaledWidth() - (219 - 304 + 103 + 57), 132 - 141 + 82 + -73, scaledResolution.getScaledWidth(), 25 - 33 + 13 + 25);
        if (this._north(n, n2, 37 - 56 + 6 - 5 + 18, 81 - 109 + 27 - 15 + 16, 192 - 264 + 171 + -69, 157 - 234 + 2 + 105) && n3 == 0) {
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[0], (int)-30202468)), (float)1.0f));
        }
        if (bl && n3 == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[1], (int)-349856542)), (float)1.0f));
        }
        if (bl2 && n3 == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[2], (int)-813463302)), (float)1.0f));
        }
        if (bl6 && n3 == 0) {
            this.field_146297_k.shutdown();
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[3], (int)1151915690)), (float)1.0f));
        }
        if (bl5 && n3 == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new AltLogin(this));
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[4], (int)-2141293931)), (float)1.0f));
        }
        if (bl3 && n3 == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.gameSettings));
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[5], (int)1701755117)), (float)1.0f));
        }
        if (bl4 && n3 == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.field_146297_k.gameSettings, this.field_146297_k.getLanguageManager()));
            this.field_146297_k.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation(OVYt.968L.FS1x((String)papers$[6], (int)-138150170)), (float)1.0f));
        }
        super.func_73864_a(n, n2, n3);
    }

    private void _modular(int n, int n2, float f) {
        this.func_146276_q_();
        this.brooklyn$ += 27 - 30 + 16 + -12;
        ScaledResolution scaledResolution = Class22._remedy();
        GL11.glDisable((int)(93 - 126 + 40 + 3001));
        this._cottages(n, n2, f);
        GL11.glEnable((int)(211 - 399 + 74 + 3122));
        items$._camps();
        Gui.drawRect((int)(39 - 52 + 50 - 33 + -4), (int)(44 - 77 + 33 - 6 + 6), (int)scaledResolution.getScaledWidth(), (int)(101 - 169 + 156 + -58), (int)(274 - 525 + 215 + -835768493));
        Gui.drawRect((int)(268 - 408 + 344 + -204), (int)(237 - 349 + 339 - 21 + -206), (int)(196 - 363 + 157 - 127 + 167), (int)(139 - 228 + 162 + -43), (int)(125 - 133 + 81 + -835768602));
        if (this._north(n, n2, 179 - 294 + 40 - 39 + 114, 70 - 115 + 80 - 59 + 24, 37 - 64 + 61 + -4, 182 - 195 + 84 - 11 + -30)) {
            Gui.drawRect((int)(209 - 391 + 177 + 5), (int)(270 - 299 + 238 + -209), (int)(27 - 42 + 8 - 8 + 45), (int)(217 - 243 + 179 - 44 + -79), (int)(242 - 329 + 47 + -298897577));
        }
        int n3 = 119 - 223 + 188 - 20 + 36;
        int n4 = 53 - 70 + 50 + 167;
        int n5 = 42 - 82 + 33 - 14 + 61;
        boolean bl = this._north(n, n2, scaledResolution.getScaledWidth() / (98 - 172 + 119 - 96 + 53) - (170 - 274 + 239 - 66 + 21), n3 + (202 - 213 + 50 - 39 + 75) + (114 - 128 + 10 - 7 + 31), scaledResolution.getScaledWidth() / (230 - 417 + 75 + 114) + (227 - 282 + 136 + 9), n3 + (156 - 165 + 144 + -60) + (163 - 298 + 207 - 97 + 45) + n5);
        boolean bl2 = this._north(n, n2, scaledResolution.getScaledWidth() / (193 - 282 + 10 - 3 + 84) - (156 - 204 + 9 + 129), n3 + (171 - 261 + 174 - 47 + 38) + (69 - 86 + 46 - 23 + 14) + n5 + (255 - 394 + 296 - 240 + 88), scaledResolution.getScaledWidth() / (236 - 401 + 221 + -54) + (142 - 217 + 133 - 57 + 89), n3 + (58 - 67 + 35 + 49) + (93 - 150 + 21 + 56) + n5 + n5 + (99 - 192 + 119 + -21));
        boolean bl3 = this._north(n, n2, 120 - 160 + 93 + -23, 208 - 383 + 90 - 32 + 117, 55 - 64 + 45 - 12 + 104, 182 - 294 + 143 - 113 + 112);
        boolean bl4 = this._north(n, n2, 180 - 348 + 260 + 36, 156 - 166 + 105 - 4 + -91, 48 - 54 + 26 + 207, 93 - 150 + 51 + 36);
        boolean bl5 = this._north(n, n2, scaledResolution.getScaledWidth() - (223 - 366 + 270 - 27 + 100), 117 - 221 + 120 - 80 + 64, scaledResolution.getScaledWidth() - (170 - 221 + 221 + -95), 178 - 241 + 142 - 43 + -6);
        boolean bl6 = this._north(n, n2, scaledResolution.getScaledWidth() - (211 - 260 + 134 - 129 + 119), 194 - 348 + 177 + -23, scaledResolution.getScaledWidth(), 107 - 164 + 59 - 17 + 45);
        Gui.drawRect((int)(scaledResolution.getScaledWidth() - (27 - 50 + 35 - 14 + 202)), (int)(230 - 446 + 369 - 100 + -53), (int)(scaledResolution.getScaledWidth() - (23 - 29 + 27 - 4 + 58)), (int)(235 - 321 + 128 + -12), (int)(22 - 34 + 19 - 17 + -1156969964));
        if (bl5) {
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - (109 - 119 + 82 + 128)), (int)(193 - 257 + 95 + -31), (int)(scaledResolution.getScaledWidth() - (249 - 331 + 314 - 43 + -114)), (int)(214 - 405 + 69 + 152), (int)(36 - 70 + 2 + 1426721322));
        }
        if (bl3) {
            Gui.drawRect((int)(76 - 78 + 1 - 1 + 32), (int)(222 - 247 + 89 + -64), (int)(41 - 45 + 5 + 127), (int)(166 - 227 + 179 - 144 + 56), (int)(187 - 273 + 20 - 6 + 1426063432));
        }
        if (bl4) {
            Gui.drawRect((int)(140 - 271 + 187 + 72), (int)(119 - 224 + 45 - 10 + 70), (int)(102 - 154 + 102 - 11 + 188), (int)(210 - 303 + 276 + -153), (int)(119 - 208 + 162 + 1426063287));
        }
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (231 - 311 + 128 - 51 + 5) - (102 - 174 + 28 + 159), n3 - (246 - 455 + 45 + 169), scaledResolution.getScaledWidth() / (61 - 109 + 4 - 2 + 48) + (160 - 298 + 213 + 40), n4 + n3 + (270 - 455 + 180 - 155 + 165), 266 - 339 + 194 - 132 + -1428102933, 42 - 59 + 21 - 12 + -1428102936);
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (234 - 319 + 253 - 87 + -79) - (53 - 82 + 46 + 93), n3, scaledResolution.getScaledWidth() / (211 - 340 + 37 + 94) + (64 - 65 + 23 + 88), n4 + n3, 25 - 46 + 34 + -1142890285, 264 - 356 + 242 + -1142890422);
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (167 - 326 + 172 - 46 + 35) - (164 - 308 + 15 - 2 + 221), n3 + (70 - 124 + 96 + 33) + (223 - 425 + 237 + -15), scaledResolution.getScaledWidth() / (231 - 253 + 114 + -90) + (161 - 214 + 112 - 17 + 48), n3 + (70 - 135 + 14 - 7 + 133) + (104 - 197 + 7 - 7 + 113) + n5, bl ? 60 - 67 + 40 - 37 + -14211285 : 206 - 242 + 137 + -13816632, bl ? 63 - 115 + 41 - 20 + -14211258 : 258 - 452 + 124 + -13816461);
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (84 - 136 + 28 - 25 + 51) - (249 - 328 + 25 + 144), n3 + (197 - 219 + 180 + -83) + (88 - 159 + 18 + 73) + n5 + (216 - 289 + 14 - 10 + 74), scaledResolution.getScaledWidth() / (165 - 235 + 224 + -152) + (223 - 281 + 16 - 5 + 137), n3 + (203 - 324 + 217 - 64 + 43) + (168 - 247 + 192 + -93) + n5 + n5 + (113 - 136 + 110 + -82), bl2 ? 211 - 308 + 58 + -14211250 : 229 - 311 + 212 - 129 + -13816532, bl2 ? 197 - 335 + 287 + -14211438 : 56 - 88 + 31 + -13816530);
        Class47._divided()._faculty(scaledResolution.getScaledWidth() / (186 - 331 + 127 + 20) - (269 - 381 + 152 - 94 + 58) * Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[21], (int)-924651392)) / (42 - 79 + 54 + -15) - (229 - 376 + 266 - 131 + 27), 157.0f, scaledResolution.getScaledWidth() / (41 - 42 + 13 - 12 + 2) + (218 - 396 + 373 - 91 + -100) * Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[22], (int)254694417)) / (100 - 130 + 83 + -51) + (205 - 306 + 256 - 136 + -4), 161.0f, 267 - 508 + 268 - 152 + -1156969849, 233 - 369 + 258 - 235 + -1156969861);
        GL11.glPushMatrix();
        GL11.glScalef((float)4.0f, (float)4.0f, (float)4.0f);
        this.func_73732_a(Class69.somalia$, (Object)EnumChatFormatting.DARK_PURPLE + OVYt.968L.FS1x((String)papers$[23], (int)980802838).substring(30 - 49 + 11 + 8, 191 - 252 + 164 + -102) + (Object)EnumChatFormatting.GRAY + OVYt.968L.FS1x((String)papers$[24], (int)-1700953968).substring(103 - 175 + 143 + -70), scaledResolution.getScaledWidth() / (190 - 282 + 41 - 35 + 94), 230 - 241 + 34 - 30 + 37, 208 - 213 + 213 + 16728413);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        Class69.somalia$.drawStringWithShadow((Object)EnumChatFormatting.GRAY + OVYt.968L.FS1x((String)papers$[25], (int)234845530) + OVYt.968L.FS1x((String)papers$[26], (int)930761981), scaledResolution.getScaledWidth() / (199 - 280 + 38 - 19 + 66) - Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[27], (int)656811783)) / (198 - 200 + 97 + -93), 181 - 286 + 124 - 90 + 155, 69 - 136 + 129 - 3 + -60);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)3.0f, (float)3.0f, (float)3.0f);
        this.func_73732_a(Class69.somalia$, OVYt.968L.FS1x((String)papers$[28], (int)-856029651), 94 - 144 + 57 + -2, 139 - 237 + 179 + -80, ColorUtils.getColor(265L - 299L + 249L + 999785L, 0.65f, 269 - 461 + 244 - 193 + 10000141).getRGB());
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
        this.func_73732_a(Class69.somalia$, I18n.format((String)OVYt.968L.FS1x((String)papers$[29], (int)1051225817), (Object[])new Object[194 - 301 + 110 + -3]), scaledResolution.getScaledWidth() / (196 - 252 + 226 - 188 + 21), n3 + (223 - 437 + 329 - 98 + 23), 186 - 238 + 196 - 72 + -73);
        this.func_73732_a(Class69.somalia$, I18n.format((String)OVYt.968L.FS1x((String)papers$[30], (int)778800721), (Object[])new Object[147 - 284 + 25 - 18 + 130]), scaledResolution.getScaledWidth() / (80 - 124 + 45 - 16 + 18), n3 + (120 - 172 + 49 + 43) + (267 - 391 + 60 + 94), 163 - 246 + 93 + -11);
        GL11.glPopMatrix();
        if (bl6) {
            Class135.func_73734_a((int)(scaledResolution.getScaledWidth() - (113 - 156 + 9 - 7 + 116)), (int)(31 - 46 + 46 - 6 + -25), (int)scaledResolution.getScaledWidth(), (int)(60 - 100 + 63 + 7), (int)(72 - 85 + 81 - 62 + 1426063354));
        }
        int n6 = 49 - 59 + 2 + 45;
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[31], (int)490747981), scaledResolution.getScaledWidth() - n6 - Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[32], (int)749300857)) / (33 - 63 + 49 + -17), 190 - 340 + 31 + 129, 178 - 215 + 31 + 5);
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[33], (int)-1919400036) + Wrapper.mc.getSession().getUsername(), scaledResolution.getScaledWidth() - (253 - 354 + 71 + 130) - n6 - Class69.somalia$.getStringWidth(Wrapper.mc.getSession().getUsername()) / (46 - 56 + 13 + -1), 212 - 277 + 22 + 53, 133 - 201 + 37 - 24 + 54);
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[34], (int)742424315), 225 - 382 + 114 - 88 + 161 + (77 - 105 + 19 - 18 + 76 - Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[35], (int)-588421042)) / (239 - 338 + 35 + 66)), 29 - 32 + 24 - 7 + -4, 246 - 340 + 215 + -122);
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[36], (int)-2120462308), 194 - 278 + 275 + -63 + (273 - 335 + 2 - 1 + 110 - Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[37], (int)584552802)) / (81 - 118 + 45 - 43 + 37)), 258 - 466 + 57 + 161, 208 - 398 + 147 - 91 + 133);
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[38], (int)-1538327036), scaledResolution.getScaledWidth() - Class69.somalia$.getStringWidth(OVYt.968L.FS1x((String)papers$[39], (int)-1522054940)), scaledResolution.getScaledHeight() - (52 - 60 + 31 - 5 + -8), 93 - 118 + 92 + -68);
        Class69.somalia$.drawStringWithShadow(OVYt.968L.FS1x((String)papers$[40], (int)-740431002), 221 - 233 + 86 - 78 + 6, scaledResolution.getScaledHeight() - (207 - 311 + 237 + -123), 161 - 314 + 14 - 11 + 149);
    }

    private boolean _north(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n > n3 && n < n5 && n2 > n4 && n2 < n6 ? 208 - 331 + 289 + -165 : 233 - 377 + 152 - 147 + 139) != 0;
    }

    protected void _indices(GuiButton guiButton) {
        if (guiButton.id == 74 - 98 + 26 - 13 + 42080) {
            Wrapper.thong$ = !Wrapper.thong$ ? 154 - 192 + 129 - 55 + -35 : 81 - 90 + 19 - 16 + 6;
            this.field_146297_k.displayGuiScreen((GuiScreen)new Class135());
        }
        if (guiButton.id == 53 - 98 + 98 - 95 + 111) {
            // empty if block
        }
        if (guiButton.id == 0) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.gameSettings));
        }
        if (guiButton.id == 156 - 291 + 88 - 74 + 126) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)this, this.field_146297_k.gameSettings, this.field_146297_k.getLanguageManager()));
        }
        if (guiButton.id == 170 - 224 + 63 + -8) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
        }
        if (guiButton.id == 197 - 272 + 217 - 86 + -54) {
            this.field_146297_k.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        }
        if (guiButton.id == 269 - 515 + 134 + 116) {
            this.field_146297_k.shutdown();
        }
        super.func_146284_a(guiButton);
    }

    private void _grass(float f) {
        this.field_146297_k.getTextureManager().bindTexture(this.branches$);
        GL11.glTexParameteri((int)(209 - 241 + 184 + 3401), (int)(48 - 49 + 14 + 10228), (int)(236 - 430 + 199 + 9724));
        GL11.glTexParameteri((int)(37 - 46 + 14 + 3548), (int)(186 - 249 + 214 - 81 + 10170), (int)(29 - 50 + 18 + 9732));
        GL11.glCopyTexSubImage2D((int)(84 - 148 + 72 - 66 + 3611), (int)(187 - 285 + 53 + 45), (int)(61 - 103 + 35 - 29 + 36), (int)(105 - 194 + 139 - 132 + 82), (int)(258 - 307 + 68 - 14 + -5), (int)(119 - 135 + 91 + -75), (int)(70 - 98 + 16 - 13 + 281), (int)(180 - 326 + 84 - 22 + 340));
        GL11.glEnable((int)(71 - 91 + 44 - 1 + 3019));
        OpenGlHelper.glBlendFunc((int)(240 - 375 + 320 + 585), (int)(210 - 288 + 16 + 833), (int)(237 - 461 + 78 - 17 + 164), (int)(55 - 58 + 13 + -10));
        GL11.glColorMask((boolean)(160 - 298 + 196 - 85 + 28), (boolean)(253 - 298 + 280 - 118 + -116), (boolean)(190 - 213 + 89 - 80 + 15), (boolean)(274 - 404 + 26 + 104));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        GL11.glDisable((int)(109 - 135 + 15 - 6 + 3025));
        int n = 106 - 116 + 77 - 54 + -10;
        for (int i = 173 - 328 + 88 + 67; i < n; ++i) {
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f / (float)(i + (77 - 79 + 64 - 6 + -55)));
            int n2 = this.field_146294_l;
            int n3 = this.field_146295_m;
            float f2 = (float)(i - n / (197 - 366 + 264 - 193 + 100)) / 256.0f;
            tessellator.addVertexWithUV((double)n2, (double)n3, (double)this.field_73735_i, (double)(0.0f + f2), 1.0);
            tessellator.addVertexWithUV((double)n2, 0.0, (double)this.field_73735_i, (double)(1.0f + f2), 1.0);
            tessellator.addVertexWithUV(0.0, 0.0, (double)this.field_73735_i, (double)(1.0f + f2), 0.0);
            tessellator.addVertexWithUV(0.0, (double)n3, (double)this.field_73735_i, (double)(0.0f + f2), 0.0);
        }
        tessellator.draw();
        GL11.glEnable((int)(266 - 502 + 13 + 3231));
        GL11.glColorMask((boolean)(218 - 281 + 149 - 105 + 20), (boolean)(140 - 243 + 119 - 118 + 103), (boolean)(191 - 367 + 257 - 149 + 69), (boolean)(259 - 512 + 186 - 133 + 201));
    }

    public void _logging(int n, int n2, float f) {
        if (Wrapper.thong$) {
            this._modular(n, n2, f);
        } else {
            this._naturals(n, n2, f);
        }
        super.func_73863_a(n, n2, f);
    }

    private void _skills(int n, int n2, float f) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode((int)(180 - 213 + 147 + 5775));
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GL11.glMatrixMode((int)(220 - 386 + 262 + 5792));
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glEnable((int)(159 - 171 + 55 + 2999));
        GL11.glDisable((int)(222 - 443 + 20 - 3 + 3212));
        GL11.glDisable((int)(205 - 222 + 83 - 30 + 2848));
        GL11.glDepthMask((boolean)(159 - 174 + 168 - 125 + -28));
        OpenGlHelper.glBlendFunc((int)(166 - 281 + 244 + 641), (int)(60 - 86 + 78 + 719), (int)(63 - 78 + 78 - 68 + 6), (int)(174 - 225 + 15 - 7 + 43));
        int n3 = 66 - 95 + 28 - 6 + 15;
        for (int i = 273 - 352 + 179 - 17 + -83; i < n3 * n3; ++i) {
            GL11.glPushMatrix();
            float f2 = ((float)(i % n3) / (float)n3 - 0.5f) / 64.0f;
            float f3 = ((float)(i / n3) / (float)n3 - 0.5f) / 64.0f;
            float f4 = 0.0f;
            GL11.glTranslatef((float)f2, (float)f3, (float)f4);
            GL11.glRotatef((float)(MathHelper.sin((float)(((float)this.brooklyn$ + f) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)(-((float)this.brooklyn$ + f) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            for (int j = 232 - 323 + 114 - 50 + 27; j < 195 - 266 + 161 - 129 + 45; ++j) {
                GL11.glPushMatrix();
                if (j == 244 - 486 + 407 - 85 + -79) {
                    GL11.glRotatef((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 143 - 207 + 124 - 85 + 27) {
                    GL11.glRotatef((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 98 - 150 + 101 + -46) {
                    GL11.glRotatef((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (j == 36 - 55 + 14 + 9) {
                    GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (j == 73 - 126 + 87 + -29) {
                    GL11.glRotatef((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                this.field_146297_k.getTextureManager().bindTexture(intent$[j]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(82 - 115 + 2 - 1 + 0x100001F, (107 - 170 + 46 + 272) / (i + (198 - 365 + 312 - 240 + 96)));
                float f5 = 0.0f;
                tessellator.addVertexWithUV(-1.0, -1.0, 1.0, (double)(0.0f + f5), (double)(0.0f + f5));
                tessellator.addVertexWithUV(1.0, -1.0, 1.0, (double)(1.0f - f5), (double)(0.0f + f5));
                tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)(1.0f - f5), (double)(1.0f - f5));
                tessellator.addVertexWithUV(-1.0, 1.0, 1.0, (double)(0.0f + f5), (double)(1.0f - f5));
                tessellator.draw();
                GL11.glPopMatrix();
            }
            GL11.glPopMatrix();
            GL11.glColorMask((boolean)(252 - 288 + 44 - 36 + 29), (boolean)(33 - 65 + 64 - 37 + 6), (boolean)(233 - 256 + 228 - 123 + -81), (boolean)(95 - 158 + 83 - 56 + 36));
        }
        tessellator.setTranslation(0.0, 0.0, 0.0);
        GL11.glColorMask((boolean)(201 - 389 + 387 - 345 + 147), (boolean)(115 - 226 + 13 - 2 + 101), (boolean)(268 - 518 + 61 - 12 + 202), (boolean)(207 - 283 + 34 - 24 + 67));
        GL11.glMatrixMode((int)(66 - 75 + 36 - 27 + 5889));
        GL11.glPopMatrix();
        GL11.glMatrixMode((int)(47 - 61 + 23 + 5879));
        GL11.glPopMatrix();
        GL11.glDepthMask((boolean)(263 - 416 + 98 + 56));
        GL11.glEnable((int)(263 - 395 + 334 + 2682));
        GL11.glEnable((int)(187 - 234 + 99 + 2877));
    }

    private void _cottages(int n, int n2, float f) {
        this.field_146297_k.getFramebuffer().unbindFramebuffer();
        GL11.glViewport((int)(184 - 259 + 257 + -182), (int)(52 - 103 + 19 - 1 + 33), (int)(238 - 431 + 159 + 290), (int)(70 - 117 + 97 - 13 + 219));
        this._skills(n, n2, f);
        this._grass(f);
        this._grass(f);
        this._grass(f);
        this._grass(f);
        this._grass(f);
        this._grass(f);
        this._grass(f);
        this.field_146297_k.getFramebuffer().bindFramebuffer(162 - 254 + 242 + -149);
        GL11.glViewport((int)(209 - 365 + 169 + -13), (int)(40 - 62 + 1 - 1 + 22), (int)this.field_146297_k.displayWidth, (int)this.field_146297_k.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f2 = this.field_146294_l > this.field_146295_m ? 120.0f / (float)this.field_146294_l : 120.0f / (float)this.field_146295_m;
        float f3 = (float)this.field_146295_m * f2 / 256.0f;
        float f4 = (float)this.field_146294_l * f2 / 256.0f;
        tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
        int n3 = this.field_146294_l;
        int n4 = this.field_146295_m;
        tessellator.addVertexWithUV(0.0, (double)n4, (double)this.field_73735_i, (double)(0.5f - f3), (double)(0.5f + f4));
        tessellator.addVertexWithUV((double)n3, (double)n4, (double)this.field_73735_i, (double)(0.5f - f3), (double)(0.5f - f4));
        tessellator.addVertexWithUV((double)n3, 0.0, (double)this.field_73735_i, (double)(0.5f + f3), (double)(0.5f - f4));
        tessellator.addVertexWithUV(0.0, 0.0, (double)this.field_73735_i, (double)(0.5f + f3), (double)(0.5f + f4));
        tessellator.draw();
    }

    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.routines$ = new DynamicTexture(225 - 440 + 412 - 229 + 288, 161 - 260 + 32 - 31 + 354);
        this.branches$ = this.field_146297_k.getTextureManager().getDynamicTextureLocation(OVYt.968L.FS1x((String)papers$[41], (int)-263189694), this.routines$);
        this.preview$ = new TimeHelper();
        this.schools$ = new Random().nextInt(49 - 86 + 54 + 283);
        this.field_146292_n.clear();
        ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k, this.field_146297_k.displayWidth, this.field_146297_k.displayHeight);
        int n = (scaledResolution.getScaledWidth() - (111 - 203 + 60 - 7 + 89)) / (244 - 270 + 122 - 98 + 6);
        int n2 = (scaledResolution.getScaledWidth() - (24 - 27 + 6 + 47)) / (254 - 462 + 236 - 72 + 50);
        this.field_146292_n.add(new Class144(235 - 314 + 9 - 8 + 42147, scaledResolution.getScaledWidth() - (191 - 232 + 28 - 16 + 129) - (250 - 326 + 191 - 112 + 7), scaledResolution.getScaledHeight() - (36 - 44 + 38 + 5), 182 - 188 + 154 - 58 + 10, 210 - 316 + 28 - 6 + 104, OVYt.968L.FS1x((String)papers$[42], (int)-1120283375) + (Wrapper.thong$ ? OVYt.968L.FS1x((String)papers$[43], (int)748646120) : OVYt.968L.FS1x((String)papers$[44], (int)290790574))));
        if (Wrapper.thong$) {
            return;
        }
        this.field_146292_n.add(new Class150(69 - 74 + 24 + -18, 21 - 37 + 24 + 18, scaledResolution.getScaledHeight() / (230 - 301 + 20 - 13 + 66) - (108 - 122 + 51 + 48), n, 254 - 463 + 120 - 6 + 145, OVYt.968L.FS1x((String)papers$[45], (int)551550838)));
        this.field_146292_n.add(new Class150(158 - 276 + 124 - 65 + 61, 236 - 239 + 105 + -76 + n, scaledResolution.getScaledHeight() / (265 - 367 + 154 + -50) - (162 - 240 + 49 + 114), n, 136 - 242 + 146 + 10, OVYt.968L.FS1x((String)papers$[46], (int)-1713306824)));
        this.field_146292_n.add(new Class150(94 - 156 + 65 + 66, 242 - 419 + 71 - 58 + 190 + n * (272 - 422 + 253 + -101), scaledResolution.getScaledHeight() / (201 - 234 + 141 + -106) - (130 - 220 + 127 - 69 + 117), n2, 69 - 76 + 70 + -13, OVYt.968L.FS1x((String)papers$[47], (int)-249197653)));
        this.field_146292_n.add(new Class150(45 - 81 + 80 - 53 + 9, 98 - 122 + 117 + -67 + n * (219 - 278 + 77 - 7 + -9) + n2, scaledResolution.getScaledHeight() / (81 - 133 + 73 - 10 + -9) - (59 - 89 + 66 - 19 + 68), n2, 218 - 244 + 6 - 6 + 76, OVYt.968L.FS1x((String)papers$[48], (int)-1125525135)));
        this.field_146292_n.add(new Class150(228 - 407 + 197 - 66 + 52, 61 - 84 + 56 - 54 + 47 + n * (238 - 305 + 292 - 123 + -100) + n2 * (223 - 308 + 133 + -46), scaledResolution.getScaledHeight() / (245 - 408 + 295 + -130) - (217 - 301 + 137 - 75 + 107), n2, 75 - 81 + 53 - 51 + 54, OVYt.968L.FS1x((String)papers$[49], (int)-1060697779)));
    }
}

