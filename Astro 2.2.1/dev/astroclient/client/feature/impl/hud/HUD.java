package dev.astroclient.client.feature.impl.hud;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.render.Render2DUtil;
import dev.astroclient.client.util.render.animation.Opacity;
import dev.astroclient.client.util.render.font.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Toggleable(label = "HUD", category = Category.VISUALS, hidden = true)
public class HUD extends ToggleableFeature {
    public MultiSelectableProperty components = new MultiSelectableProperty("Components", true, new String[]{"Feature List", "Watermark", "TabGUI"}, new String[]{"Feature List", "Watermark", "TabGUI"});

    public MultiSelectableProperty info = new MultiSelectableProperty("Info", true, new String[]{"Potions", "Ping", "FPS"}, new String[]{"Potions", "Ping", "FPS"});

    public StringProperty featureListBar = new StringProperty("Feature List Bar", true, "Right", new String[]{"Off", "Left", "Right"});

    public StringProperty watermarkTheme = new StringProperty("Watermark Theme", "Astro", new String[]{"Skeet", "Astro", "Onetap"});

    public StringProperty font = new StringProperty("Font", "Roboto", new String[]{"Consolas", "Verdana", "Tahoma", "Roboto"});

    public StringProperty colorMode = new StringProperty("Color Theme", true, "Color", new String[]{"Color", "Rainbow", "Fade"});

    public StringProperty fadeColor = new StringProperty("Fade Color", "Astro", new String[]{"Blue", "Red", "White", "Green", "Astro"});

    public ColorProperty colorProperty = new ColorProperty("HUD Color", true, new Color(153, 123, 255));

    public NumberProperty<Float> rainbowBrightness = new NumberProperty<>("Rainbow Hue", true, 1F, .05F, .0F, 1.0F);

    public NumberProperty<Float> featureListBackgroundOpacity = new NumberProperty<>("Background Opacity", true, .3F, .05F, .0F, 1.0F);

    private Opacity hue = new Opacity(0);

    public String displayName = Client.INSTANCE.NAME + " " + Client.INSTANCE.VERSION;

    private int cur = 150, min = 150, max = 255;
    private boolean increasing = true;

    private FontRenderer fontRenderer = Client.INSTANCE.hudFontRenderer;

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        switch (font.getValue()) {
            case "Verdana":
                fontRenderer = Client.INSTANCE.hudFontRendererVerdana;
                break;
            case "Tahoma":
                fontRenderer = Client.INSTANCE.hudFontRendererTahoma;
                break;
            case "Roboto":
                fontRenderer = Client.INSTANCE.hudFontRendererRoboto;
                break;
            default:
                fontRenderer = Client.INSTANCE.hudFontRenderer;
                break;
        }

        int y = 2;
        float h = 255 - hue.getColor();

        Color color = null;
        switch (colorMode.getValue()) {
            case "Color":
                color = colorProperty.getColor();
                break;
            case "Rainbow":
                color = Color.getHSBColor(h / 255, .8F, rainbowBrightness.getValue());
                break;
            case "Fade":
                switch (fadeColor.getValue()) {
                    case "Red":
                        color = new Color(cur - min / 2, 32, 32);
                        break;
                    case "Green":
                        color = new Color(0, cur - min / 2, 0);
                        break;
                    case "White":
                        color = new Color(cur - min / 2, cur - min / 2, cur - min / 2);
                        break;
                    case "Astro":
                        color = new Color(cur - min + 20, 70, 180);
                        break;
                    default:
                        color = new Color(0, cur - min / 2, cur);
                }
                break;
        }


    //    if (components.getSelectedObjects().contains("Watermark")) {
            fontRenderer.drawStringWithShadow(Client.INSTANCE.username + "\2477 - " + String.format("%03d", Client.INSTANCE.uid) + " - " + Client.INSTANCE.BUILD,
                    eventRender2D.getScaledResolution().getScaledWidth() -
                            fontRenderer.getStringWidth(Client.INSTANCE.username + "\2477 - " + String.format("%03d", Client.INSTANCE.uid) + " - " + Client.INSTANCE.BUILD) - 2,
                    eventRender2D.getScaledResolution().getScaledHeight() - 8,
                    -1);
            if (watermarkTheme.getValue().equalsIgnoreCase("Skeet")) {
                String server = mc.isSingleplayer() ? "local server" : mc.getCurrentServerData().serverIP.toLowerCase();
                String text = "astroclient.dev | " + mc.getDebugFPS() + " fps | " + server;
                float width = fontRenderer.getStringWidth(text) + 6;
                int height = 20;
                int posX = 2;
                int posY = 2;
                Render2DUtil.drawRect(posX, posY, posX + width + 2, posY + height, new Color(5, 5, 5, 255).getRGB());
                Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width + 1.5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - .5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
                Render2DUtil.drawGradientSideways(4, posY + 3, 4 + (width / 3), posY + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
                Render2DUtil.drawGradientSideways(4 + (width / 3), posY + 3, 4 + ((width / 3) * 2), posY + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
                Render2DUtil.drawGradientSideways(4 + ((width / 3) * 2), posY + 3, ((width / 3) * 3) + 1, posY + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());
                fontRenderer.drawString(text, 4 + posX, 8 + posY, -1);
            } else if (watermarkTheme.getValue().equalsIgnoreCase("Astro")) {
                String firstLetter = displayName.substring(0, 1);
                String restOfName = displayName.substring(1);
                fontRenderer.drawStringWithShadow(firstLetter, 4, 4, color.getRGB());
                fontRenderer.drawStringWithShadow(restOfName, fontRenderer.getStringWidth(firstLetter) + 4.5, 4, new Color(200, 200, 200).getRGB());
            } else if (watermarkTheme.getValue().equals("Onetap")) {
                String server = mc.isSingleplayer() ? "local server" : mc.getCurrentServerData().serverIP.toLowerCase();
                String text = "astroclient.dev | " + Client.INSTANCE.username + " | " + server;
                float width = fontRenderer.getStringWidth(text) + 6;
                Render2DUtil.drawRect(2, 2, width, 3, colorProperty.getColor().getRGB());
                Render2DUtil.drawRect(2, 3, width, 13, new Color(67, 67, 67, 190).getRGB());
                fontRenderer.drawString(text, 4, 4.5, -1);
            }
  //      }
  //      if (components.getSelectedObjects().contains("Feature List")) {
            int curMod = this.cur;
            boolean incMod = this.increasing;

            for (Feature feature : getModulesForDisplay()) {
                Color color1 = new Color(0, 0, 0);
                switch (colorMode.getValue()) {
                    case "Color":
                        color1 = colorProperty.getColor();
                        break;
                    case "Rainbow":
                        if (h > 255.0F)
                            h = 0.0F;

                        if (h < 0.0F)
                            h = 255.0F;
                        color1 = Color.getHSBColor(h / 255, .8F, rainbowBrightness.getValue());
                        break;
                    case "Fade":
                        switch (fadeColor.getValue()) {
                            case "Red":
                                color1 = new Color(curMod - min / 2, 32, 32);
                                break;
                            case "Green":
                                color1 = new Color(0, curMod - min / 2, 0);
                                break;
                            case "White":
                                color1 = new Color(curMod - min / 2, curMod - min / 2, curMod - min / 2);
                                break;
                            case "Astro":
                                color1 = new Color(curMod - min + 20, 70, 180);
                                break;
                            default:
                                color1 = new Color(0, curMod - min / 2, curMod);
                        }
                        break;
                }

                int alphaColor = new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), 255).getRGB();
                Render2DUtil.drawRect(eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(feature.getDisplayLabel()) - 5, y - 2, eventRender2D.getScaledResolution().getScaledWidth(), y + 8, featureListBackgroundOpacity.getValue());
                if (featureListBar.getValue().equalsIgnoreCase("Left")) {
                    Render2DUtil.drawRect(eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(feature.getDisplayLabel()) - 6, y - 2, eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(feature.getDisplayLabel()) - 5, y + 8, alphaColor);
                } else if (featureListBar.getValue().equalsIgnoreCase("Right"))
                    Render2DUtil.drawRect(eventRender2D.getScaledResolution().getScaledWidth() - 1, y - 2, eventRender2D.getScaledResolution().getScaledWidth(), y + 8, alphaColor);
                fontRenderer.drawStringWithShadow(feature.getDisplayLabel(), eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(feature.getDisplayLabel()) - 2.5 - (featureListBar.getValue().equalsIgnoreCase("Right") ? 1 : 0), y, alphaColor);
                switch (colorMode.getValue()) {
                    case "Rainbow":
                        h -= 10;
                        break;
                    case "Fade":
                        curMod += (incMod ? 8 : -8);
                        if (curMod > max) {
                            incMod = false;
                            curMod = max;
                        }
                        if (curMod < min) {
                            incMod = true;
                            curMod = min;
                        }
                        break;
                }
                y += 10;
            }
     //   }

        // literal rushed garbage code avert ur eyes

        int infoY = 0;

        if (info.getSelectedObjects().contains("Ping")) {
            long ping = 0;
            if (!mc.isSingleplayer())
                // I know this way doesnt update but hypixel raped some shit that makes ping not show
                ping = mc.getCurrentServerData().pingToServer;
            fontRenderer.drawStringWithShadow("Ping: " + ping, eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth("Ping: " + ping) - 2, (eventRender2D.getScaledResolution().getScaledHeight() - 17) - infoY, -1);
            infoY += 10;
        }

        if (info.getSelectedObjects().contains("FPS")) {
            fontRenderer.drawStringWithShadow("FPS: " + mc.getDebugFPS(), eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth("FPS: " + mc.getDebugFPS()) - 2, (eventRender2D.getScaledResolution().getScaledHeight() - 17) - infoY, -1);
            infoY += 10;
        }

        if (info.getSelectedObjects().contains("Potions")) {
            List<PotionEffect> potions = new ArrayList<>();

            for (Object o : mc.thePlayer.getActivePotionEffects()) {
                potions.add((PotionEffect) o);
            }

            potions.sort(Comparator.comparingDouble((effect) -> fontRenderer.getStringWidth(I18n.format(Potion.potionTypes[effect.getPotionID()].getName()))));


            int pY = -12 - infoY;

            for (PotionEffect potionEffect : potions) {
                Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                String name = I18n.format(potion.getName());
                switch (potionEffect.getAmplifier()) {
                    case 1:
                        name = name + " 2";
                        break;
                    case 2:
                        name = name + " 3";
                        break;
                    case 3:
                        name = name + " 4";
                        break;
                }

                String duration = "\2477 " + Potion.getDurationString(potionEffect);

                Color c = new Color(potion.getLiquidColor());
                fontRenderer.drawStringWithShadow(name, eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(name + duration) - 2, (eventRender2D.getScaledResolution().getScaledHeight() - 4) + pY, new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB());
                fontRenderer.drawStringWithShadow(duration, eventRender2D.getScaledResolution().getScaledWidth() - fontRenderer.getStringWidth(duration) - 2, (eventRender2D.getScaledResolution().getScaledHeight() - 4) + pY, -1);
                pY -= 9;
            }
        }
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        switch (colorMode.getValue()) {
            case "Rainbow":
                hue.increase(255.0F, 3);
                break;
            case "Fade":
                if (eventMotion.getEventType().equals(EventType.PRE)) {
                    cur += (increasing ? 8 : -8);
                    if (cur > max) {
                        increasing = false;
                        cur = max;
                    }
                    if (cur < min) {
                        increasing = true;
                        cur = min;
                    }
                }
                break;
        }
    }

    private boolean shouldShow(Feature feature) {
        return !feature.isHidden() && feature instanceof ToggleableFeature && ((ToggleableFeature) feature).getState();
    }

    private List<Feature> getModulesForDisplay() {
        List<Feature> features = new ArrayList<>();
        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            if (shouldShow(feature))
                features.add(feature);
            features.sort((f1, f2) -> fontRenderer.getStringWidth(f2.getDisplayLabel()) - fontRenderer.getStringWidth(f1.getDisplayLabel()));
        }
        return features;
    }
}
