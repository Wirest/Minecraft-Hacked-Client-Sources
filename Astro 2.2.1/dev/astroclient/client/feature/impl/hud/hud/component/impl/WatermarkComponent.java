package dev.astroclient.client.feature.impl.hud.hud.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.impl.hud.hud.component.Component;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.property.impl.BooleanProperty;

/**
 * @author Xen for Astro
 * @since 11/10/2019
 **/
public class WatermarkComponent extends Component implements JAHfb {
    private BooleanProperty booleanProperty = new BooleanProperty("test", true, false);

    public WatermarkComponent() {
        super("Watermark", 2, 2);
        add(booleanProperty);
        Client.INSTANCE.bus.register(this);
    }

    @Override
    public void renderPreview() {
        super.renderPreview();
        renderReal();
    }

    @Override
    public void renderReal() {
        super.renderReal();
        setWidth(Client.INSTANCE.fontRenderer.getStringWidth("Astro") + 2);
        setHeight(Client.INSTANCE.fontRenderer.getStringHeight("Astro") + 2);
        Client.INSTANCE.fontRenderer.drawString("Astro",getX(),getY() + 2,-1);
    }

    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        renderReal();
    }

    @Override
    public boolean isEnabled() {
        return this.isActive();
    }
}
