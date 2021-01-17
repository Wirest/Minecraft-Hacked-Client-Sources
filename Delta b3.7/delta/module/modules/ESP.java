/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender3D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.player.EntityPlayer
 */
package delta.module.modules;

import delta.Class47;
import delta.Class63;
import delta.client.DeltaClient;
import delta.utils.ColorUtils;
import delta.utils.RenderUtils;
import java.awt.Color;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender3D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class ESP
extends Module {
    public void 2l7X(float f) {
        RenderUtils._hired();
        for (Object e : this.mc.theWorld.field_72996_f) {
            if (!(e instanceof EntityLivingBase) || e == null || e == this.mc.thePlayer || !this.sjoL((EntityLivingBase)e)) continue;
            int n = 238 - 449 + 2 - 2 + 211;
            if (e instanceof EntityPlayer && DeltaClient.instance.managers.friendsManager.contains(((EntityPlayer)e).getCommandSenderName())) {
                n = 250 - 359 + 251 + -141;
            }
            if (((EntityLivingBase)e).hurtTime > 0) {
                RenderUtils._donated(f, (EntityLivingBase)e, Color.RED.getRGB());
                continue;
            }
            RenderUtils._donated(f, (EntityLivingBase)e, n != 0 ? Color.GREEN.getRGB() : ColorUtils.getColor(168L - 245L + 82L - 11L + 6L, 0.7f, 96 - 159 + 47 - 34 + 51).getRGB());
        }
        RenderUtils._leading();
    }

    public void kJng(EntityLivingBase entityLivingBase, int n, float f) {
        RenderUtils._terror(entityLivingBase, f);
        Class47._divided()._paris(31.0, -79.0, 26.0, 11.0, 156 - 306 + 178 - 72 + -16777172);
        Class47._divided()._paris(-31.0, -79.0, -26.0, 11.0, 202 - 233 + 81 - 66 + -16777200);
        Class47._divided()._paris(-30.0, 6.0, 30.0, 11.0, 94 - 185 + 104 + -16777229);
        Class47._divided()._paris(-30.0, -79.0, 30.0, -74.0, 264 - 336 + 137 - 127 + -16777154);
        Class47._divided()._paris(30.0, -75.0, 27.0, 10.0, n);
        Class47._divided()._paris(-30.0, -75.0, -27.0, 10.0, n);
        Class47._divided()._paris(-30.0, 7.0, 30.0, 10.0, n);
        Class47._divided()._paris(-30.0, -78.0, 30.0, -75.0, n);
        RenderUtils._brake();
    }

    public void HhnL(float f) {
        for (Object e : this.mc.theWorld.field_72996_f) {
            if (!(e instanceof EntityLivingBase) || e == null || e == this.mc.thePlayer || !this.sjoL((EntityLivingBase)e)) continue;
            int n = 180 - 236 + 10 + 46;
            if (e instanceof EntityPlayer && DeltaClient.instance.managers.friendsManager.contains(((EntityPlayer)e).getCommandSenderName())) {
                n = 65 - 125 + 47 - 30 + 44;
            }
            if (((EntityLivingBase)e).hurtTime > 0) {
                this.kJng((EntityLivingBase)e, Color.RED.getRGB(), f);
                continue;
            }
            this.kJng((EntityLivingBase)e, n != 0 ? Color.GREEN.getRGB() : ColorUtils.getColor(258L - 327L + 88L + -19L, 0.7f, 257 - 364 + 167 - 121 + 62).getRGB(), f);
        }
    }

    /*
     * Exception decompiling
     */
    @EventTarget
    public void oVvw(EventRender3D var1_1) {
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

    public boolean sjoL(EntityLivingBase entityLivingBase) {
        if (!this.getSetting("Players").getCheckValue() && entityLivingBase instanceof EntityPlayer) {
            return 195 - 328 + 131 - 37 + 39;
        }
        if (!this.getSetting("Monsters").getCheckValue() && (entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntitySlime)) {
            return 176 - 340 + 135 + 29;
        }
        if (!this.getSetting("Animals").getCheckValue() && (entityLivingBase instanceof EntityAnimal || entityLivingBase instanceof EntitySquid || entityLivingBase instanceof EntityBat)) {
            return 25 - 26 + 4 + -3;
        }
        if (!this.getSetting("Invisibles").getCheckValue() && entityLivingBase.func_82150_aj()) {
            return 194 - 264 + 253 - 85 + -98;
        }
        return 255 - 425 + 217 - 5 + -41;
    }

    public ESP() {
        super("ESP", Category.Render);
        this.setDescription("Dessine une boite autour de certaines entit\u00e9es");
        String[] arrstring = new String[236 - 471 + 170 - 117 + 185];
        arrstring[240 - 321 + 203 - 172 + 50] = "Outline";
        arrstring[212 - 401 + 247 - 110 + 53] = "Box";
        arrstring[144 - 257 + 150 + -35] = "2D";
        this.addSetting(new Setting("Mode", (IModule)this, "Box", Class63._dressing(arrstring)));
        this.addSetting(new Setting("Players", (IModule)this, 192 - 377 + 184 - 173 + 175));
        this.addSetting(new Setting("Monsters", (IModule)this, 76 - 151 + 127 + -51));
        this.addSetting(new Setting("Animals", (IModule)this, 80 - 123 + 2 + 42));
        this.addSetting(new Setting("Invisibles", (IModule)this, 99 - 135 + 1 - 1 + 37));
    }
}

