/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender2D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.EnumChatFormatting
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 */
package delta.module.modules;

import delta.Class172;
import delta.Class198;
import delta.Class36;
import delta.Class44;
import delta.Class63;
import delta.Class69;
import delta.client.DeltaClient;
import delta.utils.ColorUtils;
import delta.utils.HudArraySizeUtils;
import delta.utils.RenderUtils;
import delta.utils.TimeHelper;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender2D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class HUD
extends Module {
    private float retreat$ = 0.0f;
    Class172 firewire$ = new Class172("https://nkosmos.github.io/assets/deltalogo2_title.png");
    private Class36 borough$;
    private TimeHelper vessel$ = new TimeHelper();

    public void 8rY2() {
        RenderUtils._trade(this.firewire$);
        GL11.glPushMatrix();
        GL11.glEnable((int)(35 - 67 + 12 - 4 + 3066));
        double d = 0.3;
        GL11.glScaled((double)d, (double)d, (double)d);
        Gui.func_146110_a((int)(32 - 45 + 20 - 8 + -19), (int)(60 - 80 + 42 - 38 + -14), (float)0.0f, (float)0.0f, (int)(104 - 185 + 73 + 341), (int)(189 - 195 + 91 - 53 + 301), (float)333.0f, (float)333.0f);
        GL11.glPopMatrix();
    }

    public void eCsi() {
        Class69.eagles$._college("Delta".substring(230 - 364 + 254 + -120, 266 - 429 + 73 + 91) + "" + (Object)EnumChatFormatting.WHITE + "Delta".substring(160 - 316 + 307 - 215 + 65, "Delta".length()), 4.0, 2.0, 35 - 66 + 56 - 32 + 10621353);
    }

    private static boolean RZ6x(IModule iModule) {
        return (iModule.isVisible() && iModule.getAnim() != 89 - 149 + 144 + -85 ? 45 - 52 + 20 - 9 + -3 : 47 - 64 + 46 - 19 + -10) != 0;
    }

    public void vL3P() {
        ArrayList arrayList = new ArrayList();
        DeltaClient.instance.managers.modulesManager.getModules().forEach(arrayList::add);
        Collections.sort(arrayList, new Class198(this));
        ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.retreat$;
        if (this.vessel$.hasReached(130L - 237L + 139L + -16L)) {
            this.retreat$ += 0.005f;
            this.vessel$.setLastMS();
        }
        if (this.retreat$ >= 255.0f) {
            this.retreat$ = 0.0f;
        }
        int n = 217 - 429 + 6 + 207;
        int n2 = Class69.somalia$.FONT_HEIGHT + (55 - 86 + 59 + -27);
        for (IModule iModule : arrayList) {
            if (iModule.getAnim() == 156 - 222 + 63 + 2 || iModule.getCategory() == Category.Gui || !iModule.isVisible()) continue;
            Class69.somalia$.drawStringWithShadow(iModule.getDisplayName(), scaledResolution.getScaledWidth() - iModule.getAnim(), n, Color.HSBtoRGB(f, 0.5f, 1.0f));
            for (int i = 177 - 196 + 135 + -116; i < 163 - 194 + 165 + -131; ++i) {
                if (iModule.isEnabled()) {
                    if (iModule.getAnim() < Class69.somalia$.getStringWidth(iModule.getDisplayName()) + (272 - 350 + 171 - 77 + -14)) {
                        iModule.setAnim(iModule.getAnim() + (46 - 57 + 26 + -14));
                    }
                    if (iModule.getAnim() <= Class69.somalia$.getStringWidth(iModule.getDisplayName()) + (26 - 37 + 30 - 6 + -10)) continue;
                    iModule.setAnim(Class69.somalia$.getStringWidth(iModule.getDisplayName()));
                    continue;
                }
                if (iModule.getAnim() <= 205 - 247 + 123 - 94 + 12) continue;
                iModule.setAnim(iModule.getAnim() - (146 - 172 + 69 - 18 + -24));
            }
            if ((f += 0.05f) >= 255.0f) {
                f = 0.0f;
            }
            n += Math.min(n2, iModule.getAnim());
        }
    }

    public HUD() {
        super("HUD", Category.Render, 243 - 431 + 44 + 145);
        this.setDescription("L'interface");
        this.addSetting(new Setting("Logo", (IModule)this, 26 - 45 + 41 + -21));
        this.addSetting(new Setting("ArrayList", (IModule)this, 247 - 460 + 5 + 209));
        this.addSetting(new Setting("TabGui", (IModule)this, 41 - 72 + 69 + -38));
        this.addSetting(new Setting("Spacer", (IModule)this));
        String[] arrstring = new String[249 - 414 + 295 + -127];
        arrstring[168 - 298 + 111 - 87 + 106] = "Delta";
        arrstring[210 - 329 + 256 + -136] = "Ancien";
        arrstring[256 - 412 + 330 + -172] = "Altas";
        this.addSetting(new Setting("Mode", (IModule)this, "Delta", Class63._dressing(arrstring)));
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void u9Y5(EventRender2D var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$TooOptimisticMatchException
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.getInt(SwitchStringRewriter.java:392)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.access$300(SwitchStringRewriter.java:50)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter$SwitchStringMatchResultCollector.collectMatches(SwitchStringRewriter.java:343)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:24)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.KleeneN.match(KleeneN.java:24)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.MatchSequence.match(MatchSequence.java:26)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.matchutil.ResetAfterTest.match(ResetAfterTest.java:23)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewriteComplex(SwitchStringRewriter.java:197)
         * org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.SwitchStringRewriter.rewrite(SwitchStringRewriter.java:70)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:837)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
         * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
         * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
         * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
         * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
         * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
         * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
         * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
         * org.benf.cfr.reader.Main.main(Main.java:49)
         */
        throw new IllegalStateException(Decompilation failed);
    }

    public void onEnable() {
        if (this.borough$ == null) {
            this.borough$ = new Class36();
        }
        DeltaClient.instance.managers.eventManager.register((Object)this.borough$);
        if (this.mc.theWorld != null) {
            Display.setTitle((String)"Delta b3.7");
        }
        super.onEnable();
    }

    public void onDisable() {
        DeltaClient.instance.managers.eventManager.unregister((Object)this.borough$);
        Display.setTitle((String)DeltaClient.instance.displayTitle.replace("%PLAYER_USERNAME%", this.mc.getSession().getUsername()));
        super.onDisable();
    }

    public void 2quB() {
        ArrayList arrayList = new ArrayList();
        DeltaClient.instance.managers.modulesManager.getModules().forEach(arrayList::add);
        Collections.sort(arrayList, new Class44(this));
        ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int n = 93 - 180 + 173 - 117 + 31;
        int n2 = Class69.develops$._rwanda();
        float f = this.retreat$;
        if (this.vessel$.hasReached(227L - 363L + 354L + -202L)) {
            this.retreat$ += 0.005f;
            this.vessel$.setLastMS();
        }
        if (this.retreat$ >= 255.0f) {
            this.retreat$ = 0.0f;
        }
        for (IModule iModule : arrayList) {
            if (!iModule.isVisible() || iModule.getAnim() == 92 - 176 + 169 + -86) continue;
            String string = iModule.getDisplayName();
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - iModule.getAnim() - (95 - 149 + 31 - 14 + 39)), (int)n, (int)scaledResolution.getScaledWidth(), (int)(n + n2), (int)(202 - 385 + 192 + -1442840585));
            Class69.develops$._college(string, scaledResolution.getScaledWidth() - iModule.getAnim() - (167 - 233 + 161 + -94), n, Color.HSBtoRGB(f, 0.6f, 1.0f));
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - (234 - 262 + 59 + -29)), (int)n, (int)scaledResolution.getScaledWidth(), (int)(n + n2), (int)Color.HSBtoRGB(f, 0.6f, 1.0f));
            for (int i = 213 - 313 + 89 - 51 + 62; i < 148 - 170 + 121 + -96; ++i) {
                if (iModule.isEnabled()) {
                    if (iModule.getAnim() < Class69.develops$._commit(string) + (81 - 105 + 50 - 12 + -10)) {
                        iModule.setAnim(iModule.getAnim() + (207 - 369 + 307 + -144));
                    }
                    if (iModule.getAnim() <= Class69.develops$._commit(string) + (190 - 284 + 131 - 52 + 20)) continue;
                    iModule.setAnim(Class69.develops$._commit(string));
                    continue;
                }
                if (iModule.getAnim() <= 246 - 249 + 9 - 9 + 2) continue;
                iModule.setAnim(iModule.getAnim() - (179 - 235 + 161 + -104));
            }
            if ((f += 0.05f) >= 255.0f) {
                f = 0.0f;
            }
            n += Math.min(n2, iModule.getAnim());
        }
    }

    public void 3Efo(String string, int n, int n2, float f) {
        int n3 = n;
        for (int i = 219 - 334 + 230 + -115; i < string.length(); ++i) {
            String string2 = string.charAt(i) + "";
            Class69.somalia$.drawStringWithShadow(string2, n3, n2, ColorUtils.getColor((long)i * (190L - 240L + 197L - 55L + 3499908L), f, 168 - 328 + 236 - 60 + 84).getRGB());
            n3 += Class69.somalia$.getStringWidth(string2);
        }
    }

    public void eSoI() {
        GL11.glPushMatrix();
        GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
        this.3Efo("Delta", 180 - 307 + 182 + -52, 190 - 360 + 265 + -93, 0.9f);
        GL11.glPopMatrix();
        Class69.somalia$.drawStringWithShadow((Object)EnumChatFormatting.GRAY + "b3.7", (int)((float)Class69.somalia$.getStringWidth("Delta") * 1.5f) + (274 - 494 + 457 - 445 + 216), 113 - 157 + 65 - 3 + -16, 157 - 187 + 167 + -138);
    }

    public void 868P() {
        ArrayList arrayList = new ArrayList();
        DeltaClient.instance.managers.modulesManager.getModules().stream().filter(HUD::RZ6x).forEach(arrayList::add);
        Collections.sort(arrayList, new HudArraySizeUtils(this));
        ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int n = 259 - 352 + 247 - 218 + 64;
        int n2 = Class69.details$._rwanda();
        float f = this.retreat$;
        if (this.vessel$.hasReached(76L - 110L + 32L + 18L)) {
            this.retreat$ += 0.005f;
            this.vessel$.setLastMS();
        }
        if (this.retreat$ >= 255.0f) {
            this.retreat$ = 0.0f;
        }
        int n3 = 56 - 95 + 33 + 6;
        for (IModule iModule : arrayList) {
            String string = iModule.getDisplayName();
            int n4 = 132 - 157 + 86 + -61;
            int n5 = 201 - 369 + 34 + 134;
            int n6 = n3 + (263 - 280 + 177 - 125 + -34);
            if (n6 < arrayList.size()) {
                IModule iModule2 = (IModule)arrayList.get(n6);
                n4 = iModule2.getAnim() + (42 - 61 + 19 + 2);
                n5 = iModule2.getAnim() > iModule.getAnim() ? 145 - 206 + 65 + -3 : 48 - 94 + 42 + 4;
            }
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - iModule.getAnim() - (108 - 152 + 130 - 29 + -55)), (int)n, (int)scaledResolution.getScaledWidth(), (int)(n + n2), (int)(98 - 171 + 148 - 2 + -1442840649));
            Class69.details$._college(string, scaledResolution.getScaledWidth() - iModule.getAnim() - (204 - 296 + 178 - 129 + 44), n, Color.HSBtoRGB(f, 0.6f, 1.0f));
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - (164 - 302 + 237 + -97)), (int)n, (int)scaledResolution.getScaledWidth(), (int)(n + n2), (int)Color.HSBtoRGB(f, 0.6f, 1.0f));
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - iModule.getAnim() - (167 - 189 + 28 - 4 + 0) - (71 - 119 + 51 + -2)), (int)n, (int)(scaledResolution.getScaledWidth() - iModule.getAnim() - (241 - 346 + 119 + -12)), (int)(n + n2), (int)Color.HSBtoRGB(f, 0.6f, 1.0f));
            Gui.drawRect((int)(scaledResolution.getScaledWidth() - iModule.getAnim() - (32 - 63 + 1 + 32) - (273 - 450 + 292 - 43 + -71)), (int)(n + n2 - (n5 != 0 ? 163 - 303 + 157 + -16 : 265 - 309 + 253 - 226 + 17)), (int)(scaledResolution.getScaledWidth() - n4 - (n5 != 0 ? 134 - 168 + 57 - 54 + 32 : 34 - 58 + 28 + -4)), (int)(n + n2 + (252 - 495 + 169 + 75) - (n5 != 0 ? 34 - 48 + 12 - 1 + 4 : 173 - 181 + 110 + -102)), (int)Color.HSBtoRGB(f, 0.6f, 1.0f));
            for (int i = 179 - 259 + 219 + -139; i < 177 - 317 + 252 - 28 + -81; ++i) {
                if (iModule.isEnabled()) {
                    if (iModule.getAnim() < Class69.details$._commit(string) + (52 - 103 + 7 - 5 + 53)) {
                        iModule.setAnim(iModule.getAnim() + (103 - 156 + 46 - 29 + 37));
                    }
                    if (iModule.getAnim() <= Class69.details$._commit(string) + (162 - 303 + 196 + -50)) continue;
                    iModule.setAnim(Class69.develops$._commit(string));
                    continue;
                }
                if (iModule.getAnim() <= 139 - 258 + 127 + -9) continue;
                iModule.setAnim(iModule.getAnim() - (153 - 180 + 109 + -81));
            }
            if ((f += 0.05f) >= 255.0f) {
                f = 0.0f;
            }
            n += Math.min(n2, iModule.getAnim());
            ++n3;
        }
    }
}

