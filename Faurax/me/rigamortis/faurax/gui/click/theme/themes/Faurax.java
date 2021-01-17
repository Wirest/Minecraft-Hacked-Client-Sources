package me.rigamortis.faurax.gui.click.theme.themes;

import me.rigamortis.faurax.gui.click.theme.*;
import me.rigamortis.faurax.module.helpers.*;
import net.minecraft.client.*;
import me.rigamortis.faurax.gui.font.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.module.modules.movement.*;
import me.rigamortis.faurax.module.modules.player.*;
import me.rigamortis.faurax.module.modules.render.*;
import me.rigamortis.faurax.module.modules.combat.*;
import java.util.*;
import me.rigamortis.faurax.utils.*;
import org.lwjgl.opengl.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.gui.click.components.*;
import java.text.*;
import me.rigamortis.faurax.gui.click.*;

public class Faurax extends Theme implements RenderHelper
{
    private Minecraft mc;
    public CustomFont font;
    
    public Faurax() {
        this.mc = Minecraft.getMinecraft();
        this.setName("Faurax");
        this.setVisible(true);
        this.font = new CustomFont("Arial", 19.0f);
    }
    
    @Override
    public void panelContructor(final Panel panel, final float mouseX, final float mouseY) {
        int height = 0;
        for (final Module mod : Client.getModules().mods) {
            final int space = 13 * height;
            if (mod.getType() != ModuleType.UI && panel.name.equalsIgnoreCase(mod.getType().name())) {
                panel.buttons.add(new Button(panel, mod.getName(), mouseX, mouseY + 14.0f + space));
                ++height;
            }
        }
        if (panel.name.equalsIgnoreCase("GUI")) {
            panel.scrollBars.add(new ScrollBar(panel, "GUI", mouseX, mouseY + 14.0f, 130.0f));
            panel.setVisible(true);
        }
        if (panel.name.equalsIgnoreCase("Theme")) {
            panel.dropDowns.add(new DropDown(panel, "Theme", mouseX, mouseY + 14.0f, Client.t.getOptions(), Client.t));
        }
        if (panel.name.equalsIgnoreCase("Triggerbot")) {
            panel.dropDowns.add(new DropDown(panel, "Delay Mode", mouseX, mouseY + 14.0f, Triggerbot.delayMode.getOptions(), Triggerbot.delayMode));
            panel.checkBoxes.add(new CheckBox(panel, "Players", mouseX, mouseY + 14.0f + 13.0f, Triggerbot.players));
            panel.checkBoxes.add(new CheckBox(panel, "Animals", mouseX, mouseY + 14.0f + 26.0f, Triggerbot.animals));
            panel.checkBoxes.add(new CheckBox(panel, "Mobs", mouseX, mouseY + 14.0f + 39.0f, Triggerbot.mobs));
            panel.checkBoxes.add(new CheckBox(panel, "Invisibles", mouseX, mouseY + 14.0f + 52.0f, Triggerbot.invisibles));
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f + 65.0f, Triggerbot.triggerMode.getOptions(), Triggerbot.triggerMode));
            panel.sliders.add(new Slider(panel, "Delay", mouseX, mouseY + 14.0f + 78.0f, Triggerbot.attackDelay));
            panel.sliders.add(new Slider(panel, "FOV", mouseX, mouseY + 14.0f + 91.0f, Triggerbot.FOV));
        }
        if (panel.name.equalsIgnoreCase("Speed")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, Speed.mode.getOptions(), Speed.mode));
        }
        if (panel.name.equalsIgnoreCase("Aura")) {
            panel.sliders.add(new Slider(panel, "Attack Delay", mouseX, mouseY + 14.0f, KillAura.attackDelay));
            panel.sliders.add(new Slider(panel, "Range", mouseX, mouseY + 14.0f + 13.0f, KillAura.range));
            panel.sliders.add(new Slider(panel, "FOV", mouseX, mouseY + 14.0f + 26.0f, KillAura.fov));
            panel.sliders.add(new Slider(panel, "Ticks Existed", mouseX, mouseY + 14.0f + 39.0f, KillAura.ticksExisted));
            panel.dropDowns.add(new DropDown(panel, "Aim Type", mouseX, mouseY + 14.0f + 52.0f, KillAura.aimType.getOptions(), KillAura.aimType));
            panel.checkBoxes.add(new CheckBox(panel, "Players", mouseX, mouseY + 14.0f + 65.0f, KillAura.players));
            panel.checkBoxes.add(new CheckBox(panel, "Animals", mouseX, mouseY + 14.0f + 78.0f, KillAura.animals));
            panel.checkBoxes.add(new CheckBox(panel, "Mobs", mouseX, mouseY + 14.0f + 91.0f, KillAura.mobs));
            panel.checkBoxes.add(new CheckBox(panel, "Invisibles", mouseX, mouseY + 14.0f + 104.0f, KillAura.invisibles));
            panel.dropDowns.add(new DropDown(panel, "Target Priority", mouseX, mouseY + 14.0f + 117.0f, KillAura.targetPriority.getOptions(), KillAura.targetPriority));
            panel.checkBoxes.add(new CheckBox(panel, "Teleport", mouseX, mouseY + 14.0f + 130.0f, KillAura.tpAura));
            panel.checkBoxes.add(new CheckBox(panel, "Silent", mouseX, mouseY + 14.0f + 143.0f, KillAura.silent));
            panel.checkBoxes.add(new CheckBox(panel, "RayTrace", mouseX, mouseY + 14.0f + 156.0f, KillAura.rayTrace));
            panel.checkBoxes.add(new CheckBox(panel, "AutoBlock", mouseX, mouseY + 14.0f + 169.0f, KillAura.autoBlock));
            panel.checkBoxes.add(new CheckBox(panel, "AutoAttack", mouseX, mouseY + 14.0f + 182.0f, KillAura.autoAttack));
            panel.sliders.add(new Slider(panel, "Aim Speed", mouseX, mouseY + 14.0f + 195.0f, KillAura.aimSpeed));
            panel.dropDowns.add(new DropDown(panel, "Target", mouseX, mouseY + 14.0f + 208.0f, KillAura.target.getOptions(), KillAura.target));
        }
        if (panel.name.equalsIgnoreCase("ESP")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, ESP.mode.getOptions(), ESP.mode));
            panel.checkBoxes.add(new CheckBox(panel, "Players", mouseX, mouseY + 14.0f + 13.0f, ESP.players));
            panel.checkBoxes.add(new CheckBox(panel, "Animals", mouseX, mouseY + 14.0f + 26.0f, ESP.animals));
            panel.checkBoxes.add(new CheckBox(panel, "Mobs", mouseX, mouseY + 14.0f + 39.0f, ESP.mobs));
            panel.sliders.add(new Slider(panel, "Red", mouseX, mouseY + 14.0f + 52.0f, ESP.colorRed));
            panel.sliders.add(new Slider(panel, "Blue", mouseX, mouseY + 14.0f + 65.0f, ESP.colorBlue));
            panel.sliders.add(new Slider(panel, "Green", mouseX, mouseY + 14.0f + 78.0f, ESP.colorGreen));
            panel.sliders.add(new Slider(panel, "Alpha", mouseX, mouseY + 14.0f + 91.0f, ESP.colorAlpha));
            panel.sliders.add(new Slider(panel, "Width", mouseX, mouseY + 14.0f + 104.0f, ESP.width));
            panel.sliders.add(new Slider(panel, "Inner Width", mouseX, mouseY + 14.0f + 117.0f, ESP.innerWidth));
            panel.checkBoxes.add(new CheckBox(panel, "Use Cross", mouseX, mouseY + 14.0f + 130.0f, ESP.cross));
            panel.dropDowns.add(new DropDown(panel, "Color", mouseX, mouseY + 14.0f + 143.0f, ESP.color.getOptions(), ESP.color));
        }
        if (panel.name.equalsIgnoreCase("ChestStealer")) {
            panel.checkBoxes.add(new CheckBox(panel, "AutoClose", mouseX, mouseY + 14.0f, ChestStealer.autoClose));
            panel.checkBoxes.add(new CheckBox(panel, "AutoDrop", mouseX, mouseY + 14.0f + 13.0f, ChestStealer.autoDrop));
            panel.checkBoxes.add(new CheckBox(panel, "AutoOpen", mouseX, mouseY + 14.0f + 26.0f, ChestStealer.autoOpen));
            panel.checkBoxes.add(new CheckBox(panel, "Silent", mouseX, mouseY + 14.0f + 39.0f, ChestStealer.silent));
            panel.sliders.add(new Slider(panel, "Steal Delay", mouseX, mouseY + 14.0f + 52.0f, ChestStealer.stealDelayValue));
        }
        if (panel.name.equalsIgnoreCase("AutoFarm")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, AutoFarm.mode.getOptions(), AutoFarm.mode));
            panel.sliders.add(new Slider(panel, "Delay", mouseX, mouseY + 14.0f + 13.0f, AutoFarm.delay));
            panel.sliders.add(new Slider(panel, "Radius", mouseX, mouseY + 14.0f + 26.0f, AutoFarm.radius));
        }
        if (panel.name.equalsIgnoreCase("Chams")) {
            panel.checkBoxes.add(new CheckBox(panel, "Players", mouseX, mouseY + 14.0f, Chams.players));
            panel.checkBoxes.add(new CheckBox(panel, "Animals", mouseX, mouseY + 14.0f + 13.0f, Chams.animals));
            panel.checkBoxes.add(new CheckBox(panel, "Mobs", mouseX, mouseY + 14.0f + 26.0f, Chams.mobs));
            panel.checkBoxes.add(new CheckBox(panel, "Items", mouseX, mouseY + 14.0f + 39.0f, Chams.items));
            panel.checkBoxes.add(new CheckBox(panel, "Chests", mouseX, mouseY + 14.0f + 52.0f, Chams.chests));
        }
        if (panel.name.equalsIgnoreCase("Criticals")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, Criticals.mode.getOptions(), Criticals.mode));
        }
        if (panel.name.equalsIgnoreCase("Flight")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, Flight.types.getOptions(), Flight.types));
            panel.checkBoxes.add(new CheckBox(panel, "Anti Kick", mouseX, mouseY + 14.0f + 13.0f, Flight.antiKick));
            panel.sliders.add(new Slider(panel, "Speed", mouseX, mouseY + 14.0f + 26.0f, Flight.speed));
        }
        if (panel.name.equalsIgnoreCase("Jesus")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, Jesus.mode.getOptions(), Jesus.mode));
        }
        if (panel.name.equalsIgnoreCase("Regen")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, Regen.mode.getOptions(), Regen.mode));
            panel.sliders.add(new Slider(panel, "Health", mouseX, mouseY + 14.0f + 13.0f, Regen.health));
        }
        if (panel.name.equalsIgnoreCase("Phase")) {
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, NoClip.mode.getOptions(), NoClip.mode));
        }
        if (panel.name.equalsIgnoreCase("Speedmine")) {
            final String[] items = { "Block Damage", "Packet", "Instant", "New" };
            panel.dropDowns.add(new DropDown(panel, "Mode", mouseX, mouseY + 14.0f, SpeedMine.mode.getOptions(), SpeedMine.mode));
            panel.sliders.add(new Slider(panel, "Speed", mouseX, mouseY + 14.0f + 13.0f, SpeedMine.speed));
        }
        if (panel.name.equalsIgnoreCase("Tracers")) {
            panel.checkBoxes.add(new CheckBox(panel, "Players", mouseX, mouseY + 14.0f, Tracers.players));
            panel.checkBoxes.add(new CheckBox(panel, "Animals", mouseX, mouseY + 14.0f + 13.0f, Tracers.animals));
            panel.checkBoxes.add(new CheckBox(panel, "Mobs", mouseX, mouseY + 14.0f + 26.0f, Tracers.mobs));
            panel.checkBoxes.add(new CheckBox(panel, "Chests", mouseX, mouseY + 14.0f + 39.0f, Tracers.chests));
            panel.checkBoxes.add(new CheckBox(panel, "Spine", mouseX, mouseY + 14.0f + 52.0f, Tracers.spine));
            panel.dropDowns.add(new DropDown(panel, "Color", mouseX, mouseY + 14.0f + 65.0f, Tracers.color.getOptions(), Tracers.color));
            panel.sliders.add(new Slider(panel, "Red", mouseX, mouseY + 14.0f + 78.0f, Tracers.colorRed));
            panel.sliders.add(new Slider(panel, "Blue", mouseX, mouseY + 14.0f + 91.0f, Tracers.colorBlue));
            panel.sliders.add(new Slider(panel, "Green", mouseX, mouseY + 14.0f + 104.0f, Tracers.colorGreen));
            panel.sliders.add(new Slider(panel, "Alpha", mouseX, mouseY + 14.0f + 117.0f, Tracers.colorAlpha));
            panel.sliders.add(new Slider(panel, "Width", mouseX, mouseY + 14.0f + 130.0f, Tracers.width));
        }
        if (panel.name.equalsIgnoreCase("NameTags")) {
            panel.checkBoxes.add(new CheckBox(panel, "Armor", mouseX, mouseY + 14.0f, NameTags.armor));
            panel.sliders.add(new Slider(panel, "Scale", mouseX, mouseY + 14.0f + 13.0f, NameTags.scale));
        }
        if (panel.name.equalsIgnoreCase("CopsAndCrims")) {
            panel.checkBoxes.add(new CheckBox(panel, "AutoShoot", mouseX, mouseY + 14.0f, CopsAndCrims.autoShoot));
            panel.checkBoxes.add(new CheckBox(panel, "Silent", mouseX, mouseY + 14.0f + 13.0f, CopsAndCrims.silent));
            panel.checkBoxes.add(new CheckBox(panel, "NoSpread", mouseX, mouseY + 14.0f + 26.0f, CopsAndCrims.noSpread));
            panel.checkBoxes.add(new CheckBox(panel, "RCS", mouseX, mouseY + 14.0f + 39.0f, CopsAndCrims.RCS));
            panel.sliders.add(new Slider(panel, "Delay", mouseX, mouseY + 14.0f + 52.0f, CopsAndCrims.delay));
            panel.sliders.add(new Slider(panel, "FOV", mouseX, mouseY + 14.0f + 65.0f, CopsAndCrims.FOV));
            panel.dropDowns.add(new DropDown(panel, "Bone", mouseX, mouseY + 14.0f + 78.0f, CopsAndCrims.bone.getOptions(), CopsAndCrims.bone));
            panel.sliders.add(new Slider(panel, "H Recoil", mouseX, mouseY + 14.0f + 91.0f, CopsAndCrims.rcsHorizontal));
            panel.sliders.add(new Slider(panel, "V Recoil", mouseX, mouseY + 14.0f + 104.0f, CopsAndCrims.rcsVertical));
        }
    }
    
    @Override
    public void panelMouseClicked(final Panel panel, final int mouseX, final int mouseY, final int button) {
        if (panel.isVisible()) {
            for (final Button b : panel.buttons) {
                b.mouseClicked(mouseX, mouseY, button);
            }
            for (final Slider s : panel.sliders) {
                s.mouseClicked(mouseX, mouseY, button);
            }
            for (final CheckBox cb : panel.checkBoxes) {
                cb.mouseClicked(mouseX, mouseY, button);
            }
            for (final DropDown dd : panel.dropDowns) {
                dd.mouseClicked(mouseX, mouseY, button);
            }
            for (final ScrollBar sb : panel.scrollBars) {
                sb.mouseClicked(mouseX, mouseY, button);
            }
            if (mouseX >= panel.x + panel.dragX && mouseY >= panel.dragY + panel.y && mouseX <= panel.dragX + panel.x + 110.0f + 11 * panel.scrollBars.size() && mouseY <= panel.dragY + panel.y + 12.0f && button == 0) {
                panel.dragging = true;
                Client.getGUI().getTopPanel(panel);
                panel.lastDragX = mouseX - panel.dragX;
                panel.lastDragY = mouseY - panel.dragY;
            }
        }
    }
    
    @Override
    public void panelMouseMovedOrUp(final Panel panel, final int mouseX, final int mouseY, final int button) {
        if (panel.isVisible()) {
            for (final Slider s : panel.sliders) {
                s.mouseReleased(mouseX, mouseY, button);
            }
            for (final ScrollBar sb : panel.scrollBars) {
                sb.mouseReleased(mouseX, mouseY, button);
            }
        }
        if (button == 0) {
            panel.dragging = false;
        }
    }
    
    @Override
    public void panelDraw(final Panel panel, final int mouseX, final int mouseY) {
        if (panel.isVisible()) {
            final GuiUtils guiUtils = Faurax.guiUtils;
            GuiUtils.drawGradientRect(panel.x + panel.dragX, panel.y + panel.dragY, panel.dragX + panel.x + 110.0f + 11 * panel.scrollBars.size(), panel.dragY + panel.y + 14.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size() + 100 * panel.radar.size(), -584636633, -584570840);
            this.font.drawString(panel.name, panel.x + panel.dragX + 3.0f, panel.y + panel.dragY - 1.0f, CustomFont.FontType.SHADOW_THIN, -1);
            GL11.glEnable(3089);
            GuiUtils.scissorBox((int)(panel.x + panel.dragX), (int)(panel.y + panel.dragY + 14.0f), (int)(panel.dragX + panel.x + 110.0f + 11 * panel.scrollBars.size()), (int)(panel.dragY + panel.y + 14.0f + 13 * panel.buttons.size() + 13 * panel.sliders.size() + 13 * panel.checkBoxes.size() + 13 * panel.dropDowns.size() + 170 * panel.scrollBars.size()) + 100 * panel.radar.size());
            for (final ScrollBar sb : panel.scrollBars) {
                sb.draw(mouseX, mouseY);
            }
            GL11.glDisable(3089);
            for (final Button b : panel.buttons) {
                b.draw(mouseX, mouseY);
            }
            for (final Slider s : panel.sliders) {
                s.draw(mouseX, mouseY);
            }
            for (final CheckBox cb : panel.checkBoxes) {
                cb.draw(mouseX, mouseY);
            }
            for (final DropDown dd : panel.dropDowns) {
                dd.draw(mouseX, mouseY);
            }
        }
        if (panel.dragging) {
            panel.dragX = mouseX - panel.lastDragX;
            panel.dragY = mouseY - panel.lastDragY;
        }
    }
    
    @Override
    public void buttonContructor(final Button button, final Panel panel) {
    }
    
    @Override
    public void buttonMouseClicked(final Button button, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + button.x && mouseY >= panel.dragY + panel.y + button.y && mouseX <= panel.dragX + panel.x + button.x + 110.0f && mouseY <= panel.dragY + panel.y + button.y + 12.0f && mouse == 0) {
            for (final Module mod : Client.getModules().mods) {
                if (mod.getName().equalsIgnoreCase(button.name)) {
                    mod.toggle();
                    Client.getConfig().saveMods();
                }
            }
        }
    }
    
    @Override
    public void buttonDraw(final Button button, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(button.x + panel.dragX + panel.x + 1.0f, button.y + panel.dragY + panel.y, button.x + panel.dragX + panel.x + 109.0f, button.y + panel.dragY + panel.y + 12.0f, 1999975733, 1999646768);
        this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -6710887);
        if (x >= panel.x + panel.dragX + button.x && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 110.0f && y <= panel.dragY + panel.y + button.y + 12.0f) {
            final GuiUtils guiUtils2 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(button.x + panel.dragX + panel.x + 1.0f, button.y + panel.dragY + panel.y, button.x + panel.dragX + panel.x + 109.0f, button.y + panel.dragY + panel.y + 12.0f, 2000699456, 2000831042);
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (button.enabled) {
            final GuiUtils guiUtils3 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(button.x + panel.dragX + panel.x + 1.0f, button.y + panel.dragY + panel.y, button.x + panel.dragX + panel.x + 109.0f, button.y + panel.dragY + panel.y + 12.0f, 1997616448, 1997616448);
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (x >= panel.x + panel.dragX + button.x && y >= panel.dragY + panel.y + button.y && x <= panel.dragX + panel.x + button.x + 110.0f && y <= panel.dragY + panel.y + button.y + 12.0f && button.enabled) {
            final GuiUtils guiUtils4 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(button.x + panel.dragX + panel.x + 1.0f, button.y + panel.dragY + panel.y, button.x + panel.dragX + panel.x + 109.0f, button.y + panel.dragY + panel.y + 12.0f, 1997619269, 1997619269);
            this.font.drawString(button.name, button.x + panel.dragX + panel.x + 3.0f, button.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        for (final Module mod : Client.getModules().mods) {
            if (mod.getName().equalsIgnoreCase(button.name)) {
                if (mod.isToggled()) {
                    button.enabled = true;
                }
                else {
                    button.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void checkBoxContructor(final CheckBox checkBox, final Panel panel) {
    }
    
    @Override
    public void checkBoxMouseClicked(final CheckBox checkBox, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + checkBox.x + 95.0f && mouseY >= panel.dragY + panel.y + checkBox.y && mouseX <= panel.dragX + panel.x + checkBox.x + 110.0f && mouseY <= panel.dragY + panel.y + checkBox.y + 12.0f && mouse == 0) {
            checkBox.value.setBooleanValue(!checkBox.value.getBooleanValue());
        }
    }
    
    @Override
    public void checkBoxDraw(final CheckBox checkBox, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1999975733, 1999646768);
        this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -6710887);
        if (x >= panel.x + panel.dragX + checkBox.x && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 110.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f) {
            final GuiUtils guiUtils2 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1715486784, 1715618370);
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (checkBox.enabled) {
            final GuiUtils guiUtils3 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1712403776, 1712403776);
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
            final GuiUtils guiUtils4 = Faurax.guiUtils;
            GuiUtils.drawCheck((int)checkBox.x + (int)panel.dragX + (int)panel.x + (int)99.0f, (int)checkBox.y + (int)panel.dragY + (int)panel.y + (int)1.5f, -1);
        }
        if (x >= panel.x + panel.dragX + checkBox.x && y >= panel.dragY + panel.y + checkBox.y && x <= panel.dragX + panel.x + checkBox.x + 110.0f && y <= panel.dragY + panel.y + checkBox.y + 12.0f && checkBox.enabled) {
            final GuiUtils guiUtils5 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(checkBox.x + panel.dragX + panel.x + 95.0f, checkBox.y + panel.dragY + panel.y, checkBox.x + panel.dragX + panel.x + 109.0f, checkBox.y + panel.dragY + panel.y + 12.0f, 1712406597, 1712406597);
            this.font.drawString(checkBox.name, checkBox.x + panel.dragX + panel.x + 2.0f, checkBox.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
            final GuiUtils guiUtils6 = Faurax.guiUtils;
            GuiUtils.drawCheck((int)checkBox.x + (int)panel.dragX + (int)panel.x + (int)99.0f, (int)checkBox.y + (int)panel.dragY + (int)panel.y + (int)1.5f, -1);
        }
        if (checkBox.value.getBooleanValue()) {
            checkBox.enabled = true;
        }
        else {
            checkBox.enabled = false;
        }
    }
    
    @Override
    public void dropDownContructor(final DropDown dropDown, final Panel panel, final float x, final float y) {
        int height = 0;
        String[] items;
        for (int length = (items = dropDown.items).length, i = 0; i < length; ++i) {
            final String s = items[i];
            final int space = height * 13;
            dropDown.buttons.add(new DropDownButton(panel, dropDown, s, x, y + 13.0f + space, dropDown.value));
            ++height;
        }
    }
    
    @Override
    public void dropDownMouseClicked(final DropDown dropDown, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (dropDown.open) {
            for (final DropDownButton b : dropDown.buttons) {
                b.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.x + panel.dragX + dropDown.x && mouseY >= panel.dragY + panel.y + dropDown.y && mouseX <= panel.dragX + panel.x + dropDown.x + 110.0f && mouseY <= panel.dragY + panel.y + dropDown.y + 12.0f && mouse == 0) {
            dropDown.open = !dropDown.open;
            Client.getGUI().getTopPanel(panel);
        }
    }
    
    @Override
    public void dropDownDraw(final DropDown dropDown, final Panel panel, final float x, final float y) {
        final int state = dropDown.open ? 0 : 180;
        final float height = dropDown.open ? (dropDown.y + panel.dragY + panel.y + 13.0f + 13 * dropDown.buttons.size()) : (dropDown.y + panel.dragY + panel.y);
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, height, -14211289, -14145496);
        final GuiUtils guiUtils2 = Faurax.guiUtils;
        GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, dropDown.y + panel.dragY + panel.y + 12.0f, 1999975733, 1999646768);
        this.font.drawString(dropDown.name, dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -6710887);
        final GuiUtils guiUtils3 = Faurax.guiUtils;
        GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.5f, dropDown.y + panel.dragY + panel.y + 7.0f, 3.5f, state, 1.0f, -871296751);
        final GuiUtils guiUtils4 = Faurax.guiUtils;
        GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.0f, dropDown.y + panel.dragY + panel.y + 6.5f, 3.5f, state, 1.0f, -6710887);
        if (x >= panel.x + panel.dragX + dropDown.x && y >= panel.dragY + panel.y + dropDown.y && x <= panel.dragX + panel.x + dropDown.x + 110.0f && y <= panel.dragY + panel.y + dropDown.y + 12.0f) {
            final GuiUtils guiUtils5 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(dropDown.x + panel.dragX + panel.x + 1.0f, dropDown.y + panel.dragY + panel.y, dropDown.x + panel.dragX + panel.x + 109.0f, dropDown.y + panel.dragY + panel.y + 12.0f, 2000699456, 2000831042);
            final GuiUtils guiUtils6 = Faurax.guiUtils;
            GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.5f, dropDown.y + panel.dragY + panel.y + 7.0f, 3.5f, state, 1.0f, -871296751);
            final GuiUtils guiUtils7 = Faurax.guiUtils;
            GuiUtils.drawTriangle(dropDown.x + panel.dragX + panel.x + 103.0f, dropDown.y + panel.dragY + panel.y + 6.5f, 3.5f, state, 1.0f, -1);
            this.font.drawString(dropDown.name, dropDown.x + panel.dragX + panel.x + 4.0f, dropDown.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (dropDown.open) {
            for (final DropDownButton b : dropDown.buttons) {
                b.draw(x, y);
            }
        }
    }
    
    @Override
    public void dropDownButtonContructor(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel) {
    }
    
    @Override
    public void dropDownButtonMouseClicked(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + dropDownButton.x && mouseY >= panel.dragY + panel.y + dropDownButton.y && mouseX <= panel.dragX + panel.x + dropDownButton.x + 110.0f && mouseY <= panel.dragY + panel.y + dropDownButton.y + 12.0f && mouse == 0) {
            for (final DropDownButton ddb : dropDown.buttons) {
                if (!ddb.name.equalsIgnoreCase(dropDownButton.name)) {
                    ddb.enabled = false;
                }
            }
            for (final Theme theme : Client.getThemes().themes) {
                if (dropDownButton.name.equalsIgnoreCase(theme.getName())) {
                    for (final Theme t : Client.getThemes().themes) {
                        t.setVisible(false);
                    }
                    theme.setVisible(true);
                }
            }
            dropDownButton.enabled = true;
            dropDownButton.value.setSelectedOption(dropDownButton.name);
        }
    }
    
    @Override
    public void dropDownButtonDraw(final DropDownButton dropDownButton, final DropDown dropDown, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1999975733, 1999646768);
        this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY - 1.5f, CustomFont.FontType.SHADOW_THIN, -6710887);
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 110.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f) {
            final GuiUtils guiUtils2 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 2000699456, 2000831042);
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (x >= panel.x + panel.dragX + dropDownButton.x && y >= panel.dragY + panel.y + dropDownButton.y && x <= panel.dragX + panel.x + dropDownButton.x + 110.0f && y <= panel.dragY + panel.y + dropDownButton.y + 12.0f && dropDownButton.enabled) {
            final GuiUtils guiUtils3 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1997619269, 1997619269);
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (dropDownButton.enabled) {
            final GuiUtils guiUtils4 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(dropDownButton.x + panel.dragX + 2.0f, dropDownButton.y + panel.dragY, dropDownButton.x + panel.dragX + 108.0f, dropDownButton.y + panel.dragY + 12.0f, 1997616448, 1997616448);
            this.font.drawString(dropDownButton.name, dropDownButton.x + panel.dragX + 4.0f, dropDownButton.y + panel.dragY - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        Client.getValues();
        for (final Value val : ValueManager.values) {
            if (dropDownButton.value == val) {
                if (dropDownButton.value.getSelectedOption().equalsIgnoreCase(dropDownButton.name)) {
                    dropDownButton.enabled = true;
                }
                else {
                    dropDownButton.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void ScrollBarContructor(final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Aura", x, y - 13.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "ChestStealer", x, y));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "AutoFarm", x, y + 13.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Chams", x, y + 26.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "ESP", x, y + 39.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Flight", x, y + 52.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Jesus", x, y + 65.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Phase", x, y + 78.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Regen", x, y + 91.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Speedmine", x, y + 104.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Tracers", x, y + 117.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "NameTags", x, y + 130.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "CopsAndCrims", x, y + 143.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Criticals", x, y + 156.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Speed", x, y + 169.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Triggerbot", x, y + 182.0f));
        scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, "Theme", x, y + 195.0f));
        int space = 0;
        ModuleType[] values;
        for (int length = (values = ModuleType.values()).length, i = 0; i < length; ++i) {
            final Enum modType = values[i];
            if (modType != ModuleType.UI) {
                final int h = space * 13;
                final String n = String.valueOf(modType.name().substring(0, 1).toUpperCase()) + modType.name().substring(1).toLowerCase();
                scrollBar.buttons.add(new ScrollBarButton(scrollBar, panel, n, x, y + 208.0f + h));
                ++space;
            }
        }
    }
    
    @Override
    public void ScrollBarMouseClicked(final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + scrollBar.x + 1.0f && mouseY >= panel.dragY + panel.y + scrollBar.y + 1.0f && mouseX <= panel.dragX + panel.x + scrollBar.x + 109.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f) {
            for (final ScrollBarButton sbb : scrollBar.buttons) {
                sbb.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        if (mouseX >= panel.x + panel.dragX + scrollBar.x + 110.0f && mouseY >= panel.dragY + panel.y + 1.0f + scrollBar.y && mouseX <= panel.dragX + panel.x + scrollBar.x + 120.0f && mouseY <= panel.dragY + panel.y + scrollBar.y + 170.0f && mouse == 0) {
            scrollBar.dragging = true;
            scrollBar.lastDragY = mouseY - scrollBar.dragY;
        }
    }
    
    @Override
    public void ScrollBarMouseMovedOrUp(final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouse == 0) {
            scrollBar.dragging = false;
        }
    }
    
    @Override
    public void ScrollBarDraw(final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(scrollBar.x + panel.dragX + panel.x + 110.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 119.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, 1999975733, 1999646768);
        if (x >= panel.x + panel.dragX + scrollBar.x + 110.0f && y >= panel.dragY + panel.y + scrollBar.y + scrollBar.dragY + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 120.0f && y <= panel.dragY + panel.y + scrollBar.y + 39.0f + scrollBar.dragY) {
            final GuiUtils guiUtils2 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(scrollBar.x + panel.dragX + panel.x + 110.0f, scrollBar.y + panel.dragY + panel.y + scrollBar.dragY + 1.0f, scrollBar.x + panel.dragX + panel.x + 119.0f, scrollBar.y + panel.dragY + panel.y + 39.0f + scrollBar.dragY, 1999975733, 1999646768);
        }
        if (scrollBar.dragging) {
            scrollBar.dragY = y - scrollBar.lastDragY;
        }
        if (scrollBar.dragY <= 0.0f) {
            scrollBar.dragY = 0.0f;
        }
        if (scrollBar.dragY >= scrollBar.height) {
            scrollBar.dragY = scrollBar.height;
        }
        for (final ScrollBarButton sbb : scrollBar.buttons) {
            sbb.draw(x, y);
        }
    }
    
    @Override
    public void ScrollBarButtonContructor(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel) {
    }
    
    @Override
    public void ScrollBarButtonMouseClicked(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= scrollBar.x + panel.dragX + button.x && mouseY >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && mouseX <= panel.dragX + scrollBar.x + button.x + 110.0f && mouseY <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && mouse == 0) {
            for (final Panel p : Client.getGUI().panels) {
                if (p.name.equalsIgnoreCase(button.name)) {
                    p.setVisible(!p.isVisible());
                }
            }
        }
    }
    
    @Override
    public void ScrollBarButtonDraw(final ScrollBarButton button, final ScrollBar scrollBar, final Panel panel, final float x, final float y) {
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1999975733, 1999646768);
        this.font.drawString(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY - 1.5f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.SHADOW_THIN, -6710887);
        if (x >= panel.x + panel.dragX + scrollBar.x + 1.0f && y >= panel.dragY + panel.y + scrollBar.y + 1.0f && x <= panel.dragX + panel.x + scrollBar.x + 109.0f && y <= panel.dragY + panel.y + scrollBar.y + 170.0f) {
            if (x >= scrollBar.x + panel.dragX + button.x && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 110.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY) {
                final GuiUtils guiUtils2 = Faurax.guiUtils;
                GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 2000699456, 2000831042);
                this.font.drawString(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY - 1.5f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.SHADOW_THIN, -1);
            }
            if (x >= scrollBar.x + panel.dragX + button.x && y >= panel.dragY + scrollBar.y + button.y - scrollBar.dragY && x <= panel.dragX + scrollBar.x + button.x + 110.0f && y <= panel.dragY + scrollBar.y + button.y + 12.0f - scrollBar.dragY && button.enabled) {
                final GuiUtils guiUtils3 = Faurax.guiUtils;
                GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1997616448, 1997616448);
                this.font.drawString(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY - 1.5f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.SHADOW_THIN, -1);
            }
        }
        if (button.enabled) {
            final GuiUtils guiUtils4 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(button.x + panel.dragX + 2.0f + scrollBar.x, button.y + panel.dragY + scrollBar.y - scrollBar.dragY, button.x + panel.dragX + 108.0f + scrollBar.x, button.y + panel.dragY + 12.0f + scrollBar.y - scrollBar.dragY, 1997616448, 1997616448);
            this.font.drawString(button.name, button.x + panel.dragX + 3.0f + scrollBar.x, button.y + panel.dragY - 1.5f + scrollBar.y - scrollBar.dragY, CustomFont.FontType.SHADOW_THIN, -1);
        }
        for (final Panel p : Client.getGUI().panels) {
            if (p.name.equalsIgnoreCase(button.name)) {
                if (p.isVisible()) {
                    button.enabled = true;
                }
                else {
                    button.enabled = false;
                }
            }
        }
    }
    
    @Override
    public void SliderContructor(final Slider slider, final Panel panel) {
        slider.dragX = slider.value.getFloatValue() * 99.0f / slider.value.getMaxFloat();
    }
    
    @Override
    public void SliderMouseClicked(final Slider slider, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouseX >= panel.x + panel.dragX + slider.x && mouseY >= panel.dragY + panel.y + slider.y && mouseX <= panel.dragX + panel.x + slider.x + 110.0f && mouseY <= panel.dragY + panel.y + slider.y + 12.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }
    
    @Override
    public void SliderMouseMovedOrUp(final Slider slider, final Panel panel, final int mouseX, final int mouseY, final int mouse) {
        if (mouse == 0) {
            slider.dragging = false;
        }
    }
    
    @Override
    public void SliderDraw(final Slider slider, final Panel panel, final float x, final float y) {
        final float fraction = slider.dragX / 99.0f;
        final float value = fraction * slider.value.getMaxFloat();
        final DecimalFormat decimal = new DecimalFormat("#.#");
        final float percent = Float.valueOf(decimal.format(value));
        final GuiUtils guiUtils = Faurax.guiUtils;
        GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + 109.0f, slider.y + panel.dragY + panel.y + 12.0f, 1714763061, 1714434096);
        final GuiUtils guiUtils2 = Faurax.guiUtils;
        GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + slider.dragX + 10.0f, slider.y + panel.dragY + panel.y + 12.0f, 1712403776, 1712403776);
        this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 3.0f, slider.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        this.font.drawString(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 110.0f - this.font.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) - 2.0f, slider.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        if (x >= panel.x + panel.dragX + slider.x && y >= panel.dragY + panel.y + slider.y && x <= panel.dragX + panel.x + slider.x + 110.0f && y <= panel.dragY + panel.y + slider.y + 12.0f) {
            final GuiUtils guiUtils3 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + 109.0f, slider.y + panel.dragY + panel.y + 12.0f, 1715486784, 1715618370);
            final GuiUtils guiUtils4 = Faurax.guiUtils;
            GuiUtils.drawGradientRect(slider.x + panel.dragX + panel.x + 1.0f, slider.y + panel.dragY + panel.y, slider.x + panel.dragX + panel.x + slider.dragX + 10.0f, slider.y + panel.dragY + panel.y + 12.0f, 1712406597, 1712406597);
            this.font.drawString(slider.name, slider.x + panel.dragX + panel.x + 3.0f, slider.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
            this.font.drawString(new StringBuilder(String.valueOf(percent)).toString(), slider.x + panel.dragX + panel.x + 110.0f - this.font.getStringWidth(new StringBuilder(String.valueOf(percent)).toString()) - 2.0f, slider.y + panel.dragY + panel.y - 1.5f, CustomFont.FontType.SHADOW_THIN, -1);
        }
        if (slider.dragging) {
            slider.dragX = x - slider.lastDragX;
            slider.value.setFloatValue(value);
            Client.getConfig().saveConfig();
        }
        if (slider.value.getFloatValue() <= slider.value.getMinFloatValue()) {
            slider.value.setFloatValue(slider.value.getMinFloatValue());
        }
        if (slider.value.getFloatValue() >= slider.value.getMaxFloat()) {
            slider.value.setFloatValue(slider.value.getMaxFloat());
        }
        if (slider.dragX <= 0.0f) {
            slider.dragX = 0.0f;
        }
        if (slider.dragX >= 99.0f) {
            slider.dragX = 99.0f;
        }
    }
    
    @Override
    public void mainConstructor(final ClickUI ui) {
        ui.panels.clear();
        ModuleType[] values;
        for (int length = (values = ModuleType.values()).length, i = 0; i < length; ++i) {
            final Enum modType = values[i];
            if (modType != ModuleType.UI) {
                final String name = String.valueOf(modType.name().substring(0, 1).toUpperCase()) + modType.name().substring(1).toLowerCase();
                ui.panels.add(new Panel(name, 0.0f, 0.0f));
            }
        }
        ui.panels.add(new Panel("Aura", 0.0f, 0.0f));
        ui.panels.add(new Panel("ChestStealer", 0.0f, 0.0f));
        ui.panels.add(new Panel("AutoFarm", 0.0f, 0.0f));
        ui.panels.add(new Panel("Criticals", 0.0f, 0.0f));
        ui.panels.add(new Panel("Chams", 0.0f, 0.0f));
        ui.panels.add(new Panel("ESP", 0.0f, 0.0f));
        ui.panels.add(new Panel("Flight", 0.0f, 0.0f));
        ui.panels.add(new Panel("Jesus", 0.0f, 0.0f));
        ui.panels.add(new Panel("Phase", 0.0f, 0.0f));
        ui.panels.add(new Panel("Regen", 0.0f, 0.0f));
        ui.panels.add(new Panel("Speedmine", 0.0f, 0.0f));
        ui.panels.add(new Panel("Tracers", 0.0f, 0.0f));
        ui.panels.add(new Panel("Speed", 0.0f, 0.0f));
        ui.panels.add(new Panel("NameTags", 0.0f, 0.0f));
        ui.panels.add(new Panel("CopsAndCrims", 0.0f, 0.0f));
        ui.panels.add(new Panel("Triggerbot", 0.0f, 0.0f));
        ui.panels.add(new Panel("Theme", 0.0f, 0.0f));
        ui.panels.add(new Panel("GUI", 0.0f, 0.0f));
    }
}
