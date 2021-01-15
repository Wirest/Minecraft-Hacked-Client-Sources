package dev.astroclient.client.feature.impl.hud.hud.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.impl.hud.hud.component.Component;
import dev.astroclient.client.util.render.animation.Opacity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class FeatureListComponent extends Component implements JAHfb {

    private Opacity hue = new Opacity(0);

    public FeatureListComponent() {
        super("Feature List", new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), 2);
        Client.INSTANCE.bus.register(this);
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        hue.increase(255.0F, 2);
    }

    @Override
    public void renderReal() {
        super.renderReal();
    }

    @Override
    public void renderPreview() {
        super.renderPreview();
        renderReal();
    }

    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        renderReal();
    }

    @Override
    public boolean isEnabled() {
        return this.isActive();
    }

    private List<Feature> getModulesForDisplay() {
        List<Feature> features = new ArrayList<>();
        for (Feature feature : Client.INSTANCE.featureManager.getFeatures()) {
            if (!feature.isHidden()) {
                if (feature instanceof ToggleableFeature) {
                    ToggleableFeature toggleableFeature = (ToggleableFeature) feature;
                    if (toggleableFeature.getState())
                        features.add(toggleableFeature);
                }
            }
        }
        features.sort((f1, f2) -> (int) (Client.INSTANCE.fontRenderer.getStringWidth(f2.getDisplayLabel()) - Client.INSTANCE.fontRenderer.getStringWidth(f1.getDisplayLabel())));
        return features;
    }
}
