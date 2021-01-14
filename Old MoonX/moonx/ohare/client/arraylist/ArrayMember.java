package moonx.ohare.client.arraylist;

import com.mojang.realmsclient.gui.ChatFormatting;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class ArrayMember {
    private static Minecraft mc = Minecraft.getMinecraft();
    private float x, oldX, y, oldY, width;
    private boolean done, start;
    private Module module;
    private TimerUtil timer = new TimerUtil();

    public ArrayMember(Module module) {
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        this.x = new ScaledResolution(mc).getScaledWidth() - 2;
        this.width = (hud.font.isEnabled() ?  hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)):mc.fontRendererObj.getStringWidth(getModuleRenderString(module)));
        this.module = module;
        done = false;
    }

    public void draw(ArrayList<ArrayMember> arrayMembers, float startY, float prevY) {
        if (!start) {
            y = startY;
            start = true;
        }
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        final float xSpeed = width / (Minecraft.getDebugFPS() / 6);
        final float ySpeed = (new ScaledResolution(mc).getScaledHeight() - prevY) / (Minecraft.getDebugFPS() * 2);
        if (width != (hud.font.isEnabled() ?  hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)):mc.fontRendererObj.getStringWidth(getModuleRenderString(module))))
        width = (hud.font.isEnabled() ?  hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)):mc.fontRendererObj.getStringWidth(getModuleRenderString(module)));
        if (done) {
            oldX = x;
            oldY = y;
            x += xSpeed;
            y += ySpeed;
        }
        if (!done() && !done) {
            oldX = x;
            if (x <= new ScaledResolution(mc).getScaledWidth() - 2 - width + xSpeed)
                x = new ScaledResolution(mc).getScaledWidth() - 2 - width;
            else x -= xSpeed;
        }
        if (x < new ScaledResolution(mc).getScaledWidth() - 2 - width) {
            oldX = x;
            x += xSpeed;
        }
        if (y != prevY) {
            oldY = y;
            if (y <= prevY - ySpeed) {
                y += ySpeed;
            } else {
                y = prevY;
            }
        } else if (y > prevY) {
            oldY = y;
            if (y >= prevY + ySpeed) {
                y -= ySpeed;
            }
        }
        if (timer.sleep(2000)) {
            x = new ScaledResolution(mc).getScaledWidth() - 2 - (hud.font.isEnabled() ?  hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)):mc.fontRendererObj.getStringWidth(getModuleRenderString(module)));
            y = prevY;
        }
        if (!module.isEnabled() || module.isHidden()) done = true;
        float finishedX = oldX + (x - oldX);
        final float finishedY = oldY + (y - oldY);
        int color = 0;
        switch (hud.MODECOLORS.getValue()) {
            case DEV:
                color = Color.getHSBColor(0, 0, 1f).getRGB();
                break;
            case LIGHTRAINBOW:
                color = RenderUtil.getRainbow(6000, (int) (prevY * 30), 0.55f);
                break;
            case NORMALRAINBOW:
                color = RenderUtil.getRainbow(6000, (int) (prevY * 30), 0.85f);
                break;
            case FASTRAINBOW:
                color = RenderUtil.getRainbow(3000, (int) (prevY * 12), 0.90f);
                break;
            case ASTOLFO:
                color = hud.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (finishedY / ((hud.font.isEnabled() ? hud.fontValue.getValue().getHeight() + 6 : mc.fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                break;
            case WEIRD:
                color = hud.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (finishedY / ((hud.font.isEnabled() ? hud.fontValue.getValue().getHeight() + 6 : mc.fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                break;
            case VALENTINE:
                color = hud.getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (finishedY / ((hud.font.isEnabled() ? hud.fontValue.getValue().getHeight() + 6 : mc.fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                break;
            case RANDOM:
                if (!hud.modColors.containsKey(module)) {
                    hud.modColors.put(module, hud.getRandomColor());
                }
                color = hud.modColors.get(module);
                break;
            case MODULECOLOR:
                color = module.getColor();
                break;
            case CLIENTCOLOR:
                color = hud.colorValue.getValue();
                break;
            case WAVE:
                color = hud.color((int) (finishedY / (hud.font.isEnabled() ? hud.fontValue.getValue().getHeight() + 6 : mc.fontRendererObj.FONT_HEIGHT + 4)), (int) Moonx.INSTANCE.getModuleManager().getMap().values().stream().filter(module1 -> module1.isEnabled() && !module1.isHidden()).count());
                break;
            case TESTRAINBOW:
                color = RenderUtil.getRainbow(2500, (int) -(finishedY * 8), 0.4f);
                break;
        }

        switch (hud.BORDERMODE.getValue()) {
            case NONE:
                if (hud.background.isEnabled()) {
                    RenderUtil.drawRect(finishedX - 1, finishedY - 3, width + 4, hud.fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, hud.backgroundAlpha.getValue()).getRGB());
                }
                break;
            case RIGHT:
                RenderUtil.drawRect(finishedX + (hud.font.isEnabled() ? hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)) : mc.fontRendererObj.getStringWidth(getModuleRenderString(module))) + 2 - hud.borderWidth.getValue(), finishedY - 3, hud.borderWidth.getValue(), hud.fontValue.getValue().getHeight() + 4, color);
                finishedX -= hud.font.isEnabled() ? 4 : 2;
                if (hud.background.isEnabled()) {
                    RenderUtil.drawRect(finishedX - hud.borderWidth.getValue(), finishedY - 3, (hud.font.isEnabled() ? hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)) : mc.fontRendererObj.getStringWidth(getModuleRenderString(module))) + (hud.font.isEnabled() ? 6 : 4), hud.fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, hud.backgroundAlpha.getValue()).getRGB());
                }
                break;
            case LEFT:
                if (hud.background.isEnabled()) {
                    RenderUtil.drawRect(finishedX - 2, finishedY - 3, (hud.font.isEnabled() ? hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)) : mc.fontRendererObj.getStringWidth(getModuleRenderString(module))) + 4, hud.fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, hud.backgroundAlpha.getValue()).getRGB());
                }
                RenderUtil.drawRect(finishedX - 2 - hud.borderWidth.getValue(), finishedY - 3, hud.borderWidth.getValue(), hud.fontValue.getValue().getHeight() + 4, color);
                break;
            case WRAPPERLEFT:
                if (hud.background.isEnabled()) {
                    RenderUtil.drawRect(finishedX - 2, finishedY - 3, width + 4, hud.fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, hud.backgroundAlpha.getValue()).getRGB());
                }
                RenderUtil.drawRect(finishedX - 2 - hud.borderWidth.getValue(), finishedY - 3, hud.borderWidth.getValue(), hud.fontValue.getValue().getHeight() + 4, color);
                if (arrayMembers.indexOf(this) == arrayMembers.size() - 1)
                    RenderUtil.drawRect(finishedX - 2 - hud.borderWidth.getValue(), finishedY + hud.fontValue.getValue().getHeight() + 1, (hud.font.isEnabled() ? hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)) : mc.fontRendererObj.getStringWidth(getModuleRenderString(module))) + 4 + hud.borderWidth.getValue(), hud.borderWidth.getValue() + 0.25f, color);
                else {
                    final Module nextMod = arrayMembers.get(arrayMembers.indexOf(this) + 1).getModule();
                    final float dist = (hud.font.isEnabled() ? hud.fontValue.getValue().getStringWidth(getModuleRenderString(module)) - hud.fontValue.getValue().getStringWidth(getModuleRenderString(nextMod)) : mc.fontRendererObj.getStringWidth(getModuleRenderString(module)) - mc.fontRendererObj.getStringWidth(getModuleRenderString(nextMod)));
                    RenderUtil.drawRect(finishedX - 2 - hud.borderWidth.getValue(), finishedY + hud.fontValue.getValue().getHeight() + 1, hud.borderWidth.getValue() + dist, hud.borderWidth.getValue() + 0.25f, color);
                }
                break;
        }
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(getModuleRenderString(module), finishedX - (hud.BORDERMODE.getValue() == HUD.bordermode.RIGHT ? hud.borderWidth.getValue() - 2 : 0), finishedY - 1, color);
        else
            mc.fontRendererObj.drawStringWithShadow(getModuleRenderString(module), finishedX - (hud.BORDERMODE.getValue() == HUD.bordermode.RIGHT ? hud.borderWidth.getValue() - 2 : 0), finishedY - 1, color);
        if (delete()) {
            if (hud.MODECOLORS.getValue() == HUD.colors.RANDOM) {
                if (hud.modColors.get(module) != null) {
                    hud.modColors.remove(module);
                }
            }
            Moonx.INSTANCE.getArraylistManager().getArrayMembers().remove(this);
        }
    }

    private String getModuleRenderString(Module module) {
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        if (hud.MODECASE.getValue() == HUD.casemode.LOWER)
            return (Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""))).toLowerCase();
        else if (hud.MODECASE.getValue() == HUD.casemode.UPPER)
            return (Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""))).toUpperCase();
        else
            return Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""));
    }

    public boolean done() {
        return x <= new ScaledResolution(mc).getScaledWidth() - 2 - width;
    }

    public boolean delete() {
        return x >= new ScaledResolution(mc).getScaledWidth() - 2 && done;
    }

    public Module getModule() {
        return module;
    }
}
