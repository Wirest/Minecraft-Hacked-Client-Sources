package moonx.ohare.client.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.arraylist.ArrayMember;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.input.KeyInputEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.gui.tab.TabGUI;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class HUD extends Module {
    private TabGUI tabGUI;
    public ColorValue colorValue = new ColorValue("Color", new Color(0x3030ff).getRGB());
    private BooleanValue notifications = new BooleanValue("Notifications", "Hud Notifications", true);
    private BooleanValue tabgui = new BooleanValue("Tab GUI", "Show TabGUI", true);
    private BooleanValue xyz = new BooleanValue("XYZ", "Coordinates", true);
    private BooleanValue fps = new BooleanValue("FPS", "FPS Display", true);
    private BooleanValue ping = new BooleanValue("Ping", "Show Ping", true);
    private BooleanValue armor = new BooleanValue("Armor", "Show Armor", true);
    private BooleanValue healingpotions = new BooleanValue("Healing Potions", "Show Healing Pots", true);
    public BooleanValue background = new BooleanValue("Background", "Arraylist Background", true);
    private BooleanValue animations = new BooleanValue("Animations", "Arraylist Animations", true);
    public BooleanValue font = new BooleanValue("Font", "Enable Custom Font", true);
    public EnumValue<casemode> MODECASE = new EnumValue<>("Case Mode", casemode.NORMAL);
    public EnumValue<colors> MODECOLORS = new EnumValue<>("Color Mode", colors.RANDOM);
    public EnumValue<bordermode> BORDERMODE = new EnumValue<>("Border Mode", bordermode.RIGHT);
    public HashMap<Module, Integer> modColors = new HashMap<>();
    public NumberValue<Integer> backgroundAlpha = new NumberValue<>("BackgroundAlpha", 75, 1, 255, 1);
    public NumberValue<Float> borderWidth = new NumberValue<>("BorderWidth", 2.0f, 0.25f, 5.0f, 0.25f);
    public FontValue fontValue = new FontValue("HudFont", new MCFontRenderer(new Font("Arial", Font.PLAIN, 18), true, true));

    public HUD() {
        super("HUD", Category.VISUALS, new Color(0xE5E5E5).getRGB());
        setHidden(true);
    }

    public enum colors {
        CLIENTCOLOR, LIGHTRAINBOW, DEV, NORMALRAINBOW, FASTRAINBOW, RANDOM, MODULECOLOR, TESTRAINBOW, WAVE, ASTOLFO, WEIRD, VALENTINE
    }

    public enum casemode {
        NORMAL, LOWER, UPPER
    }

    public enum bordermode {
        RIGHT, LEFT, WRAPPERLEFT, NONE
    }


    @Override
    public void onEnable() {
        tabGUI = new TabGUI(3, 14, 80);
        tabGUI.init();
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        if (getMc().gameSettings.showDebugInfo) return;
        if (tabgui.isEnabled()) tabGUI.onDraw(event.getScaledResolution());
        int colorWaterMark = 0;
        switch (MODECOLORS.getValue()) {
            case DEV:
                colorWaterMark = Color.getHSBColor(0, 0, 1f).getRGB();
                break;
            case LIGHTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.55f);
                break;
            case NORMALRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.85f);
                break;
            case FASTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(3000, 0, 0.9f);
                break;
            case TESTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(2500, 0, 0.4f);
                break;
            case ASTOLFO:
                colorWaterMark = getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case WEIRD:
                colorWaterMark = getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case VALENTINE:
                colorWaterMark = getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case RANDOM:
            case MODULECOLOR:
            case CLIENTCOLOR:
                colorWaterMark = colorValue.getValue();
                break;
            case WAVE:
                colorWaterMark = color(2, 100);
                break;
        }
        String watermark = "M" + ChatFormatting.GRAY + "oon X epik gangsta edition";
        switch (MODECASE.getValue()) {
            case UPPER:
                watermark = watermark.toUpperCase();
                break;
            case NORMAL:
                break;
            case LOWER:
                watermark = watermark.toLowerCase();
                break;
        }
        if (font.isEnabled())
            fontValue.getValue().drawStringWithShadow(watermark, 3f, 3, colorWaterMark);
        else
            getMc().fontRendererObj.drawStringWithShadow(watermark, 3f, 3, colorWaterMark);
        if (!animations.isEnabled()) {
            float posY = 3;
            final ArrayList<Module> mods = new ArrayList<>(Moonx.INSTANCE.getModuleManager().getModuleMap().values());
            mods.sort(Comparator.comparingDouble(module -> -(font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module)))));
            for (Module module : mods) {
                if (!module.isEnabled() || module.isHidden()) {
                    if (MODECOLORS.getValue() == HUD.colors.RANDOM) {
                        if (modColors.get(module) != null) {
                            modColors.remove(module);
                        }
                    }
                }
            }
            mods.removeIf(module->!module.isEnabled() || module.isHidden());
            for (Module module : mods) {
                int color = 0;
                switch (MODECOLORS.getValue()) {
                    case DEV:
                        color = Color.getHSBColor(0, 0, 1f).getRGB();
                        break;
                    case LIGHTRAINBOW:
                        color = RenderUtil.getRainbow(6000, (int) (posY * 30), 0.55f);
                        break;
                    case NORMALRAINBOW:
                        color = RenderUtil.getRainbow(6000, (int) (posY * 30), 0.85f);
                        break;
                    case FASTRAINBOW:
                        color = RenderUtil.getRainbow(3000, (int) (posY * 12), 0.90f);
                        break;
                    case TESTRAINBOW:
                        color = RenderUtil.getRainbow(2500, (int) -(posY * 8), 0.4f);
                        break;
                    case ASTOLFO:
                        color = getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (posY / ((font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                        break;
                    case WEIRD:
                        color = getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (posY / ((font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                        break;
                    case VALENTINE:
                        color = getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + (posY / ((font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT + 4) / 2))).getRGB();
                        break;
                    case RANDOM:
                        if (!modColors.containsKey(module)) {
                            modColors.put(module, getRandomColor());
                        }
                        color = modColors.get(module);
                        break;
                    case MODULECOLOR:
                        color = module.getColor();
                        break;
                    case CLIENTCOLOR:
                        color = colorValue.getValue();
                        break;
                    case WAVE:
                        color = color((int) (posY / (font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT + 4)), (int) Moonx.INSTANCE.getModuleManager().getMap().values().stream().filter(module1 -> module1.isEnabled() && !module1.isHidden()).count());
                        break;
                }
                float Xpos = event.getScaledResolution().getScaledWidth() - 2 - (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module)));
                switch (BORDERMODE.getValue()) {
                    case NONE:
                        if (background.isEnabled()) {
                            RenderUtil.drawRect(Xpos - 2, posY - 3, (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + 5, fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, backgroundAlpha.getValue()).getRGB());
                        }
                        break;
                    case RIGHT:
                        RenderUtil.drawRect(Xpos + (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + 2 - borderWidth.getValue(), posY - 3, borderWidth.getValue(), fontValue.getValue().getHeight() + 4, color);
                        Xpos -= font.isEnabled() ? 4 : 2;
                        if (background.isEnabled()) {
                            RenderUtil.drawRect(Xpos - borderWidth.getValue(), posY - 3, (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + (font.isEnabled() ? 6 : 4), fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, backgroundAlpha.getValue()).getRGB());
                        }
                        break;
                    case LEFT:
                        if (background.isEnabled()) {
                            RenderUtil.drawRect(Xpos - 2, posY - 3, (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + 4, fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, backgroundAlpha.getValue()).getRGB());
                        }
                        RenderUtil.drawRect(Xpos - 2 - borderWidth.getValue(), posY - 3, borderWidth.getValue(), fontValue.getValue().getHeight() + 4, color);
                        break;
                    case WRAPPERLEFT:
                        if (background.isEnabled()) {
                            RenderUtil.drawRect(Xpos - 2, posY - 3, (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + 4, fontValue.getValue().getHeight() + 4, new Color(0, 0, 0, backgroundAlpha.getValue()).getRGB());
                        }
                        RenderUtil.drawRect(Xpos - 2 - borderWidth.getValue(), posY - 3, borderWidth.getValue(), fontValue.getValue().getHeight() + 4, color);
                        if (mods.indexOf(module) == mods.size() - 1)
                            RenderUtil.drawRect(Xpos - 2 - borderWidth.getValue(), posY + fontValue.getValue().getHeight() + 1, (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(module))) + 4 + borderWidth.getValue(), borderWidth.getValue() + 0.25f, color);
                        else {
                            final Module nextMod = mods.get(mods.indexOf(module) + 1);
                            final float dist = (font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(module)) - fontValue.getValue().getStringWidth(getModuleRenderString(nextMod)): getMc().fontRendererObj.getStringWidth(getModuleRenderString(module)) - getMc().fontRendererObj.getStringWidth(getModuleRenderString(nextMod)));
                            RenderUtil.drawRect(Xpos - 2 - borderWidth.getValue(), posY + fontValue.getValue().getHeight() + 1, borderWidth.getValue() + dist, borderWidth.getValue() + 0.25f, color);
                        }
                        break;
                }
                if (font.isEnabled())
                    fontValue.getValue().drawStringWithShadow(getModuleRenderString(module), Xpos - (BORDERMODE.getValue() == bordermode.RIGHT ? borderWidth.getValue() - 2 : 0), posY - 1, color);
                else
                    getMc().fontRendererObj.drawStringWithShadow(getModuleRenderString(module), Xpos- (BORDERMODE.getValue() == bordermode.RIGHT ? borderWidth.getValue() - 2 : 0), posY - 1, color);
                posY += (font.isEnabled() ? fontValue.getValue().getHeight() + 4 : getMc().fontRendererObj.FONT_HEIGHT + 2);
            }
        } else drawArraylist();
        if (notifications.isEnabled()) drawNotifications(event.getScaledResolution());
        if (xyz.isEnabled()) {
            if (font.isEnabled())
                fontValue.getValue().drawStringWithShadow((int) getMc().thePlayer.posX + "," + (int) getMc().thePlayer.posY + "," + (int) getMc().thePlayer.posZ, event.getScaledResolution().getScaledWidth() - 2 - fontValue.getValue().getStringWidth((int) getMc().thePlayer.posX + "," + (int) getMc().thePlayer.posY + "," + (int) getMc().thePlayer.posZ), event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 24 : 12), colorWaterMark);
            else
                getMc().fontRendererObj.drawStringWithShadow((int) getMc().thePlayer.posX + "," + (int) getMc().thePlayer.posY + "," + (int) getMc().thePlayer.posZ, event.getScaledResolution().getScaledWidth() - 2 - getMc().fontRendererObj.getStringWidth((int) getMc().thePlayer.posX + "," + (int) getMc().thePlayer.posY + "," + (int) getMc().thePlayer.posZ), event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 24 : 12), colorWaterMark);
        }
        if (ping.isEnabled()) {
            if (font.isEnabled())
                fontValue.getValue().drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toLowerCase() : ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer))), event.getScaledResolution().getScaledWidth() - 2 - fontValue.getValue().getStringWidth(MODECASE.getValue() == casemode.UPPER ? ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toLowerCase() : ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)))), event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 36 : 24), colorWaterMark);
            else
                getMc().fontRendererObj.drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toLowerCase() : ("Ping: " + ChatFormatting.GRAY + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer))), event.getScaledResolution().getScaledWidth() - 2 - getMc().fontRendererObj.getStringWidth(MODECASE.getValue() == casemode.UPPER ? ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)).toLowerCase() : ("Ping: " + (getMc().isSingleplayer() ? "0" : getMc().getCurrentServerData().pingToServer)))), event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 36 : 24), colorWaterMark);
        }
        if (fps.isEnabled()) {
            if (font.isEnabled())
                fontValue.getValue().drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS()).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS()).toLowerCase() : ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS())), 3, tabgui.isEnabled() ? 100 : 15, colorWaterMark);
            else
                getMc().fontRendererObj.drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS()).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS()).toLowerCase() : ("F" + ChatFormatting.GRAY + "PS: " + Minecraft.getDebugFPS())), 3, tabgui.isEnabled() ? 100 : 15, colorWaterMark);
        }
        if (armor.isEnabled())
            drawArmor((event.getScaledResolution().getScaledWidth() / 2) + 105, event.getScaledResolution().getScaledHeight() - 18);
        if (healingpotions.isEnabled()) {
            if (font.isEnabled())
                fontValue.getValue().drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("Healing Pots: " + ChatFormatting.GRAY + getPotions()).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Healing Pots: " + ChatFormatting.GRAY + getPotions()).toLowerCase() : ("Healing Pots: " + ChatFormatting.GRAY + getPotions())), 3, event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 24 : 12), colorWaterMark);
            else
                getMc().fontRendererObj.drawStringWithShadow(MODECASE.getValue() == casemode.UPPER ? ("Healing Pots: " + ChatFormatting.GRAY + getPotions()).toUpperCase() : (MODECASE.getValue() == casemode.LOWER ? ("Healing Pots: " + ChatFormatting.GRAY + getPotions()).toLowerCase() : ("Healing Pots: " + ChatFormatting.GRAY + getPotions())), 3, event.getScaledResolution().getScaledHeight() - (getMc().ingameGUI.getChatGUI().getChatOpen() ? 24 : 12), colorWaterMark);
        }
    }

    @Handler
    public void onKeyPress(KeyInputEvent event) {
        if (tabgui.isEnabled())
            tabGUI.onKeyPress(event.getKey());
    }

    private int getPotions() {
        int healing = 0;
        for (Slot s : getMc().thePlayer.inventoryContainer.inventorySlots)
            if (s.getHasStack()) {
                ItemStack is = s.getStack();
                if ((is.getItem() instanceof ItemPotion)) {
                    ItemPotion ip = (ItemPotion) is.getItem();
                    if (ItemPotion.isSplash(is.getMetadata())) {
                        for (PotionEffect pe : ip.getEffects(is))
                            if (pe.getPotionID() == Potion.heal.getId()) {
                                healing++;
                                break;
                            }
                    }
                }
            }
        return healing;
    }

    private void drawNotifications(ScaledResolution scaledResolution) {
        float y = scaledResolution.getScaledHeight() - 26;
        if (ping.isEnabled()) y -= 14;
        if (xyz.isEnabled()) y -= 14;
        if (getMc().ingameGUI.getChatGUI().getChatOpen()) y -= 12;
        for (int i = 0; i < Moonx.INSTANCE.getNotificationManager().getNotifications().size(); i++) {
            Moonx.INSTANCE.getNotificationManager().getNotifications().get(i).draw(y);
            y -= 26;
        }
    }

    private void drawArraylist() {
        for (Module module : Moonx.INSTANCE.getModuleManager().getModuleMap().values()) {
            if (!module.isEnabled() || module.isHidden() || Moonx.INSTANCE.getArraylistManager().isArrayMember(module))
                continue;
            Moonx.INSTANCE.getArraylistManager().addArray(module);
        }
        float posY = 3;
        final ArrayList<ArrayMember> arrayMembers = new ArrayList<>(Moonx.INSTANCE.getArraylistManager().getArrayMembers());
        arrayMembers.sort(Comparator.comparingDouble(arrayMember -> -(font.isEnabled() ? fontValue.getValue().getStringWidth(getModuleRenderString(arrayMember.getModule())) : getMc().fontRendererObj.getStringWidth(getModuleRenderString(arrayMember.getModule())))));
        for (ArrayMember arrayMember : arrayMembers) {
            arrayMember.draw(arrayMembers,posY - (font.isEnabled() ? fontValue.getValue().getHeight() + 4 : getMc().fontRendererObj.FONT_HEIGHT + 2), posY);
            posY += (font.isEnabled() ? fontValue.getValue().getHeight() + 4 : getMc().fontRendererObj.FONT_HEIGHT + 2);
        }
    }

    private void drawArmor(int x, int y) {
        if (getMc().thePlayer.inventory.armorInventory.length > 0) {
            List<ItemStack> items = new ArrayList<>();
            if (getMc().thePlayer.getHeldItem() != null) {
                items.add(getMc().thePlayer.getHeldItem());
            }
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = getMc().thePlayer.inventory.armorInventory[index];
                if (stack != null) {
                    items.add(stack);
                }
            }
            for (ItemStack stack : items) {
                GlStateManager.pushMatrix();
                GlStateManager.enableLighting();
                getMc().getRenderItem().renderItemIntoGUI(stack, x, y);
                getMc().getRenderItem().renderItemOverlayIntoGUI(getMc().fontRendererObj, stack, x, y, "");
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                if (stack.isStackable() && stack.stackSize > 0) {
                    if (font.isEnabled())
                        fontValue.getValue().drawStringWithShadow(String.valueOf(stack.stackSize), x + 4, y + 8, 0xDDD1E6);
                    else
                        getMc().fontRendererObj.drawStringWithShadow(String.valueOf(stack.stackSize), x + 4, y + 8, 0xDDD1E6);
                }
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
                x += 18;
            }
        }
    }

    public int color(int index, int count) {
        float[] hsb = new float[3];
        final Color clr = colorValue.getColor();
        Color.RGBtoHSB(clr.getRed(), clr.getGreen(), clr.getBlue(), hsb);
        float brightness = Math.abs(((getOffset() + (index / (float) count) * 4) % 2) - 1);
        brightness = 0.4f + (0.4f * brightness);

        hsb[2] = brightness % 1f;
        return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }

    private float getOffset() {
        return (System.currentTimeMillis() % 2000) / 1000f;
    }

    public int getRandomColor() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255), 255).getRGB();
    }

    public Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    private String getModuleRenderString(Module module) {
        if (MODECASE.getValue() == casemode.LOWER)
            return (Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""))).toLowerCase();
        else if (MODECASE.getValue() == casemode.UPPER)
            return (Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""))).toUpperCase();
        else
            return Objects.nonNull(module.getRenderLabel()) ? (module.getRenderLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : "")) : (module.getLabel() + (Objects.nonNull(module.getSuffix()) ? ChatFormatting.GRAY + " " + module.getSuffix() : ""));
    }
}
