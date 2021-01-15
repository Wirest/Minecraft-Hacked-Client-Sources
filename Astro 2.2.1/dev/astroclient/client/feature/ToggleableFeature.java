package dev.astroclient.client.feature;

import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import com.google.gson.JsonObject;
import dev.astroclient.client.Client;
import dev.astroclient.client.configuration.impl.BasicConfiguration;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.render.animation.Translate;

import java.util.Arrays;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */
public class ToggleableFeature implements Feature, JAHfb {

    private String label = getClass().getAnnotation(Toggleable.class).label();

    private String description = getClass().getAnnotation(Toggleable.class).description();

    private Category category = getClass().getAnnotation(Toggleable.class).category();

    private int bind = getClass().getAnnotation(Toggleable.class).bind();

    private boolean state, hidden = getClass().getAnnotation(Toggleable.class).hidden();

    public Translate translate = new Translate(0, 0);

    public void setLabel(String label) {
        this.label = label;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private String suffix = "";

    public ToggleableFeature() {
        Client.INSTANCE.bus.register(this);
        Client.INSTANCE.configManager.register(new BasicConfiguration(this));
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    public void toggle() {
        setState(!state);
    }

    public void setState(boolean state) {
        if (this.state != state) {
            this.state = state;
            if (state) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDisplayLabel() {
        String spiltName = getLabel().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
        if (suffix.equalsIgnoreCase(""))
            return spiltName;
        return spiltName + "\2477 " + suffix;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    public int getBind() {
        return bind;
    }

    public boolean getState() {
        return state;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public boolean isEnabled() {
        return state && mc.thePlayer != null;
    }

    public void save(JsonObject directory) {
        directory.addProperty("key", getBind());
        directory.addProperty("enabled", isEnabled());
        directory.addProperty("hidden", isHidden());
        Client.INSTANCE.propertyManager.getPropertiesForFeature(this).forEach(val -> {
            if (val instanceof BooleanProperty) {
                directory.addProperty(val.getName(), ((BooleanProperty) val).getValue());
            }

            if (val instanceof ColorProperty) {
                int[] rgb = ((ColorProperty) val).getRGB();
                directory.addProperty(val.getName(), Arrays.toString(rgb).replace("[", "").replace("]", ""));
            }

            if (val instanceof MultiSelectableProperty) {
                String[] selected = ((MultiSelectableProperty) val).getSelectedObjects().toArray(new String[0]);
                directory.addProperty(val.getName(), Arrays.toString(selected).replace("[", "").replace("]", ""));
            }

            if (val instanceof StringProperty) {
                directory.addProperty(val.getName(), ((StringProperty) val).getValue());
            }

            if (val instanceof NumberProperty) {
                directory.addProperty(val.getName(), ((NumberProperty) val).getValue().toString());
            }
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name":
                    return;
                case "key":
                    setBind(data.getValue().getAsInt());
                    return;
                case "enabled":
                    if (!(isEnabled() && data.getValue().getAsBoolean()) && !(!isEnabled() && !data.getValue().getAsBoolean()))
                        setState(data.getValue().getAsBoolean());
                    return;
                case "hidden":
                    setHidden(data.getValue().getAsBoolean());
                    return;
            }
        });
    }
}

