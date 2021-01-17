package me.rigamortis.faurax.hooks;

import com.darkmagician6.eventapi.EventManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import me.rigamortis.faurax.Client;
import me.rigamortis.faurax.events.EventRenderHud;
import me.rigamortis.faurax.gui.tabui.TabUI;
import me.rigamortis.faurax.module.Module;
import me.rigamortis.faurax.module.ModuleList;
import me.rigamortis.faurax.module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class GuiIngameHook
extends GuiIngame {
    private boolean[] keys = new boolean[256];
    public static boolean showInfo = true;
    public TabUI tabUI = new TabUI();

    public GuiIngameHook(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void func_175180_a(float p_175180_1_) {
        super.func_175180_a(p_175180_1_);
        this.setupKeys();
        EventRenderHud event = new EventRenderHud();
        EventManager.call(event);
        if (event.isCancelled()) {
            return;
        }
        ScaledResolution var2 = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int var3 = var2.getScaledWidth();
        int var4 = var2.getScaledHeight();
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("Faurax", 2.0f, 2.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("v3.6, Rel 2", 2.0f, 12.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("\u00a77X\u00a7f " + (int)Minecraft.getMinecraft().thePlayer.posX, 2.0f, 90.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("\u00a77Y\u00a7f " + (int)Minecraft.getMinecraft().thePlayer.posY, 2.0f, 102.0f, -1);
        Minecraft.getMinecraft().fontRendererObj.func_175063_a("\u00a77Z\u00a7f " + (int)Minecraft.getMinecraft().thePlayer.posZ, 2.0f, 113.0f, -1);
        int height = 0;
        for (Module mod : GuiIngameHook.getSortedModuleArray()) {
            int space = 10 * height;
            if (!mod.isToggled() || mod.getType() == ModuleType.UI || !mod.isVisible()) continue;
            if (showInfo) {
                Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(mod.getName()) + mod.getModInfo(), 2 + var3 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(mod.getName()) + mod.getModInfo()) - 3, 2.5f + (float)space, mod.getColor());
            } else {
                Minecraft.getMinecraft().fontRendererObj.func_175063_a(mod.getName(), 2 + var3 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod.getName()) - 3, 2.5f + (float)space, mod.getColor());
            }
            ++height;
        }
        this.drawPotionEffects(var2);
        this.tabUI.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    private void drawPotionEffects(ScaledResolution scaledRes) {
        int offset = 0;
        for (Object var4 : Minecraft.getMinecraft().thePlayer.getActivePotionEffects()) {
            int posY = 11 * offset;
            PotionEffect effect = (PotionEffect)var4;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName(), new Object[0]);
            name = effect.getAmplifier() == 1 ? String.valueOf(name) + " II" : (effect.getAmplifier() == 2 ? String.valueOf(name) + " III" : (effect.getAmplifier() == 3 ? String.valueOf(name) + " IV" : (effect.getAmplifier() == 4 ? String.valueOf(name) + " V" : (effect.getAmplifier() == 5 ? String.valueOf(name) + " VI" : (effect.getAmplifier() == 6 ? String.valueOf(name) + " VII" : (effect.getAmplifier() == 7 ? String.valueOf(name) + " VIII" : (effect.getAmplifier() == 8 ? String.valueOf(name) + " IX" : (effect.getAmplifier() == 9 ? String.valueOf(name) + " X" : (effect.getAmplifier() > 10 ? String.valueOf(name) + " X+" : String.valueOf(name) + " I")))))))));
            name = String.valueOf(name) + " \u00a77(\u00a7f" + Potion.getDurationString(effect) + "\u00a77)";
            int color = Integer.MIN_VALUE;
            if (effect.getEffectName() == "potion.weither") {
                color = -16777246;
            } else if (effect.getEffectName() == "potion.weakness") {
                color = -9864951;
            } else if (effect.getEffectName() == "potion.waterBreathing") {
                color = -16758065;
            } else if (effect.getEffectName() == "potion.saturation") {
                color = -11159217;
            } else if (effect.getEffectName() == "potion.resistance") {
                color = -5655199;
            } else if (effect.getEffectName() == "potion.regeneration") {
                color = -1145130;
            } else if (effect.getEffectName() == "potion.poison") {
                color = -14553374;
            } else if (effect.getEffectName() == "potion.nightVision") {
                color = -6735204;
            } else if (effect.getEffectName() == "potion.moveSpeed") {
                color = -7875870;
            } else if (effect.getEffectName() == "potion.moveSlowdown") {
                color = -16751493;
            } else if (effect.getEffectName() == "potion.jump") {
                color = -5375161;
            } else if (effect.getEffectName() == "potion.invisibility") {
                color = -9405272;
            } else if (effect.getEffectName() == "potion.hunger") {
                color = -16754448;
            } else if (effect.getEffectName() == "potion.heal") {
                color = -65556;
            } else if (effect.getEffectName() == "potion.harm") {
                color = -3735043;
            } else if (effect.getEffectName() == "potion.fireResistance") {
                color = -29656;
            } else if (effect.getEffectName() == "potion.healthBoost") {
                color = -40151;
            } else if (effect.getEffectName() == "potion.digSpeed") {
                color = -989556;
            } else if (effect.getEffectName() == "potion.digSlowdown") {
                color = -5655199;
            } else if (effect.getEffectName() == "potion.damageBoost") {
                color = -7665712;
            } else if (effect.getEffectName() == "potion.confusion") {
                color = -5195482;
            } else if (effect.getEffectName() == "potion.blindness") {
                color = -8355712;
            } else if (effect.getEffectName() == "potion.absorption") {
                color = -23256;
            }
            Minecraft.getMinecraft().fontRendererObj.func_175063_a(name, scaledRes.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) - 1, scaledRes.getScaledHeight() - 11 - posY, color);
            ++offset;
        }
    }

    private static List<Module> getSortedModuleArray() {
        ArrayList<Module> list = new ArrayList<Module>();
        for (Module mod : Client.getModules().mods) {
            if (!mod.isToggled() || !mod.isVisible() || mod.getType() == ModuleType.UI) continue;
            list.add(mod);
        }
        list.sort(new Comparator(){

            public int compare(Module m1, Module m2) {
                String s1 = m1.getName();
                String s2 = m2.getName();
                int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
                return cmp != 0 ? cmp : s1.compareTo(s2);
            }

            public int compare(Object o1, Object o2) {
                Module m1 = (Module)o1;
                Module m2 = (Module)o2;
                String s1 = String.valueOf(m1.getName()) + (m1.getName() == null ? "" : m1.getName());
                String s2 = String.valueOf(m2.getName()) + (m2.getName() == null ? "" : m2.getName());
                int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
                return cmp != 0 ? cmp : s2.compareTo(s1);
            }
        });
        return list;
    }

    public boolean isKeyPressed(int key) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            return false;
        }
        if (Keyboard.isKeyDown((int)key) != this.keys[key]) {
            this.keys[key] = !this.keys[key];
            return this.keys[key];
        }
        return false;
    }

    public void setupKeys() {
        for (Module mod : Client.modules.mods) {
            if (!this.isKeyPressed(mod.getKey())) continue;
            mod.toggle();
            Client.getConfig().saveMods();
        }
    }

}

