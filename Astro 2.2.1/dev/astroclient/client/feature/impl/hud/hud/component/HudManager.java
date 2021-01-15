package dev.astroclient.client.feature.impl.hud.hud.component;

import dev.astroclient.client.feature.impl.hud.hud.component.impl.FeatureListComponent;
import dev.astroclient.client.feature.impl.hud.hud.component.impl.WatermarkComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xen for Astro
 * @since 11/10/2019
 **/
public class HudManager {
    private List<Component> components;

    public HudManager() {
        components = new ArrayList<>();
        collectComps();
    }

    private void collectComps() {
        components.add(new WatermarkComponent());
        components.add(new FeatureListComponent());
    }

    public List<Component> getComponents() {
        return components;
    }
}
