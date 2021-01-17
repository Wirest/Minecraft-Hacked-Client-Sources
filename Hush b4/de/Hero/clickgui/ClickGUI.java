// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.clickgui;

import de.Hero.clickgui.elements.menu.ElementSlider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.Gui;
import de.Hero.clickgui.elements.Element;
import java.awt.Color;
import de.Hero.clickgui.util.ColorUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import me.nico.hush.modules.Module;
import me.nico.hush.modules.Category;
import de.Hero.clickgui.util.FontUtil;
import me.nico.hush.Client;
import de.Hero.settings.SettingsManager;
import de.Hero.clickgui.elements.ModuleButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen
{
    public static ArrayList<Panel> panels;
    public static ArrayList<Panel> rpanels;
    private ModuleButton mb;
    public SettingsManager setmgr;
    
    public ClickGUI() {
        this.mb = null;
        this.setmgr = Client.instance.settingManager;
        FontUtil.setupFontUtils();
        ClickGUI.panels = new ArrayList<Panel>();
        final double pwidth = 80.0;
        final double pheight = 15.0;
        final double px = 10.0;
        double py = 10.0;
        final double pyplus = pheight + 10.0;
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category c = values[i];
            final String title = String.valueOf(Character.toUpperCase(c.name().toLowerCase().charAt(0))) + c.name().toLowerCase().substring(1);
            ClickGUI.panels.add(new Panel(title, px, py, pwidth, pheight, false, this) {
                @Override
                public void setup() {
                    for (final Module m : Client.instance.moduleManager.getModules()) {
                        if (!m.getCategory().equals(c)) {
                            continue;
                        }
                        this.Elements.add(new ModuleButton(m, this));
                    }
                }
            });
            py += pyplus;
        }
        ClickGUI.rpanels = new ArrayList<Panel>();
        for (final Panel p : ClickGUI.panels) {
            ClickGUI.rpanels.add(p);
        }
        Collections.reverse(ClickGUI.rpanels);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final Panel p : ClickGUI.panels) {
            p.drawScreen(mouseX, mouseY, partialTicks);
        }
        final ScaledResolution s = new ScaledResolution(this.mc);
        GL11.glPushMatrix();
        GL11.glTranslated(s.getScaledWidth(), s.getScaledHeight(), 0.0);
        GL11.glScaled(0.5, 0.5, 0.5);
        GL11.glPopMatrix();
        this.mb = null;
    Label_0218:
        for (final Panel p2 : ClickGUI.panels) {
            if (p2 != null && p2.visible && p2.extended && p2.Elements != null && p2.Elements.size() > 0) {
                for (final ModuleButton e : p2.Elements) {
                    if (e.listening) {
                        this.mb = e;
                        break Label_0218;
                    }
                }
            }
        }
        for (final Panel panel : ClickGUI.panels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended && b.menuelements != null && !b.menuelements.isEmpty()) {
                        double off = 0.0;
                        final Color temp = ColorUtil.getClickGUIColor().darker();
                        final int outlineColor = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 170).getRGB();
                        for (final Element e2 : b.menuelements) {
                            e2.offset = off;
                            e2.update();
                            if (Client.instance.settingManager.getSettingByName("Design").getValString().equalsIgnoreCase("Hero")) {
                                Gui.drawRect(e2.x, e2.y, e2.x + e2.width + 2.0, e2.y + e2.height, outlineColor);
                            }
                            e2.drawScreen(mouseX, mouseY, partialTicks);
                            off += e2.height;
                        }
                    }
                }
            }
        }
        if (this.mb != null) {
            Gui.drawRect(0.0, 0.0, this.width, this.height, -2012213232);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(s.getScaledWidth() / 2), (float)(s.getScaledHeight() / 2), 0.0f);
            GL11.glScalef(4.0f, 4.0f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Binding...", 0.0, -10.0, -1);
            GL11.glScalef(0.5f, 0.5f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("Press 'ESCAPE' to unbind " + this.mb.mod.getName() + ((this.mb.mod.getKeyBind() > -1) ? (" (" + Keyboard.getKeyName(this.mb.mod.getKeyBind()) + ")") : ""), 0.0, 0.0, -1);
            GL11.glScalef(0.25f, 0.25f, 0.0f);
            FontUtil.drawTotalCenteredStringWithShadow("by HeroCode", 0.0, 20.0, -1);
            GL11.glPopMatrix();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.mb != null) {
            return;
        }
        for (final Panel panel : ClickGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            if (e.mouseClicked(mouseX, mouseY, mouseButton)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        for (final Panel p : ClickGUI.rpanels) {
            if (p.mouseClicked(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.mb != null) {
            return;
        }
        for (final Panel panel : ClickGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            e.mouseReleased(mouseX, mouseY, state);
                        }
                    }
                }
            }
        }
        for (final Panel p : ClickGUI.rpanels) {
            p.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Panel p : ClickGUI.rpanels) {
            if (p != null && p.visible && p.extended && p.Elements != null && p.Elements.size() > 0) {
                for (final ModuleButton e : p.Elements) {
                    try {
                        if (e.keyTyped(typedChar, keyCode)) {
                            return;
                        }
                        continue;
                    }
                    catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        try {
            super.keyTyped(typedChar, keyCode);
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    @Override
    public void initGui() {
        if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (this.mc.entityRenderer.theShaderGroup != null) {
                this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            this.mc.entityRenderer.theShaderGroup = null;
        }
        for (final Panel panel : ClickGUI.rpanels) {
            if (panel.extended && panel.visible && panel.Elements != null) {
                for (final ModuleButton b : panel.Elements) {
                    if (b.extended) {
                        for (final Element e : b.menuelements) {
                            if (e instanceof ElementSlider) {
                                ((ElementSlider)e).dragging = false;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void closeAllSettings() {
        for (final Panel p : ClickGUI.rpanels) {
            if (p != null && p.visible && p.extended && p.Elements != null && p.Elements.size() > 0) {
                for (final ModuleButton e : p.Elements) {
                    e.extended = false;
                }
            }
        }
    }
}
