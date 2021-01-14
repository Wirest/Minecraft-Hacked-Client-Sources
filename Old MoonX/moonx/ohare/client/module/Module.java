package moonx.ohare.client.module;

import com.google.gson.JsonObject;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.impl.*;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class Module {
    private String label, renderLabel, suffix, description;
    private boolean enabled, hidden;
    private int color, keybind;
    private Category category;
    private Minecraft mc = Minecraft.getMinecraft();
    private List<Value> values = new ArrayList<>();

    public Module(String label, Category category, int color) {
        this.label = label;
        this.category = category;
        this.color = color;
    }

    public Value findValue(String term) {
        for (Value value : values) {
            if (value.getLabel().equalsIgnoreCase(term)) {
                return value;
            }
        }
        return null;
    }

    public List<Value> getValues() {
        return this.values;
    }

    public float getLongestValueInModule() {
        final HUD hud = (HUD) Moonx.INSTANCE.getModuleManager().getModule("hud");
        float width = hud.fontValue.getValue().getStringWidth(getValues().get(0).getLabel() + ": " + getValues().get(0).getValue());
        for (Value value : getValues()) {
            if (value instanceof NumberValue) {
                if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMaximum()) > width) {
                    width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMaximum());
                } else if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMinimum()) > width) {
                    width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((NumberValue) value).getMinimum());
                }
            }
            if (value instanceof RangedValue) {
                if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum()) > width) {
                    width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum());
                } else if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum()) > width) {
                    width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + ((RangedValue) value).getMaximum() + " - " + ((RangedValue) value).getMaximum());
                }
            } else if (value instanceof EnumValue) {
                for (Enum enoom : ((EnumValue) value).getConstants()) {
                    if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + enoom.name()) > width) {
                        width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + enoom.name());
                    }
                }
            } else if (value instanceof BooleanValue) {
                if (hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + value.getValue()) > width) {
                    width = hud.fontValue.getValue().getStringWidth(value.getLabel() + ": " + value.getValue());
                }
            }
        }
        return width;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setRenderLabel(String renderLabel) {
        this.renderLabel = renderLabel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            Moonx.INSTANCE.getEventBus().bind(this);
            onEnable();
        } else {
            Moonx.INSTANCE.getEventBus().unbind(this);
            onDisable();
        }
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public String getLabel() {
        return label;
    }

    public String getRenderLabel() {
        return renderLabel;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public int getColor() {
        return color;
    }

    public int getKeybind() {
        return keybind;
    }

    public Category getCategory() {
        return category;
    }

    public Minecraft getMc() {
        return mc;
    }

    public void save(JsonObject directory, boolean key) {
        if (key) directory.addProperty("key", getKeybind());
        directory.addProperty("enabled", isEnabled());
        directory.addProperty("hidden", isHidden());
        values.forEach(val -> {
            if (val instanceof RangedValue) {
                directory.addProperty(val.getLabel(), ((RangedValue) val).getLeftVal() + "-" + ((RangedValue) val).getRightVal());
            } else if (val instanceof FontValue)
                directory.addProperty(val.getLabel(), ((FontValue) val).getValue().getFont().getName().replace(" ","_") + "-" + ((FontValue) val).getValue().getFont().getStyle() + "-" + ((FontValue) val).getValue().getFont().getSize() + "-" + ((FontValue) val).getValue().isAntiAlias() + "-" + ((FontValue) val).getValue().isFractionalMetrics());
            else
                directory.addProperty(val.getLabel(), val.getValue().toString());
        });
    }

    public void load(JsonObject directory) {
        directory.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name":
                    return;
                case "key":
                    setKeybind(data.getValue().getAsInt());
                    return;
                case "enabled":
                    if (!(isEnabled() && data.getValue().getAsBoolean()) && !(!isEnabled() && !data.getValue().getAsBoolean()))
                        setEnabled(data.getValue().getAsBoolean());
                    return;
                case "hidden":
                    setHidden(data.getValue().getAsBoolean());
                    return;
            }
            Value val = findValue(data.getKey());
            if (val != null) {
                if (val instanceof RangedValue) {
                    String[] strings = data.getValue().getAsString().split("-");
                    if (((RangedValue) val).getInc() instanceof Float) {
                        ((RangedValue) val).setLeftVal(Float.parseFloat(strings[0]));
                        ((RangedValue) val).setRightVal(Float.parseFloat(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Integer) {
                        ((RangedValue) val).setLeftVal(Integer.parseInt(strings[0]));
                        ((RangedValue) val).setRightVal(Integer.parseInt(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Double) {
                        ((RangedValue) val).setLeftVal(Double.parseDouble(strings[0]));
                        ((RangedValue) val).setRightVal(Double.parseDouble(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Byte) {
                        ((RangedValue) val).setLeftVal(Byte.parseByte(strings[0]));
                        ((RangedValue) val).setRightVal(Byte.parseByte(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Short) {
                        ((RangedValue) val).setLeftVal(Short.parseShort(strings[0]));
                        ((RangedValue) val).setRightVal(Short.parseShort(strings[1]));
                    }
                    if (((RangedValue) val).getInc() instanceof Long) {
                        ((RangedValue) val).setLeftVal(Long.parseLong(strings[0]));
                        ((RangedValue) val).setRightVal(Long.parseLong(strings[1]));
                    }
                } else if (val instanceof FontValue) {
                    final FontValue fontValue = (FontValue)val;
                    final String[] strings = data.getValue().getAsString().split("-");
                    final int style = (int) MathUtils.clamp(Integer.parseInt(strings[1]), 2, 0);
                    final int size = Integer.parseInt(strings[2]);
                    final boolean aa = Boolean.parseBoolean(strings[3]);
                    final boolean fractionalmetrics = Boolean.parseBoolean(strings[4]);
                    final MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(strings[0].replace("_"," "), style,size),aa,fractionalmetrics);
                    fontValue.setValue(mcFontRenderer);
                } else if (val instanceof ColorValue) val.setValue(data.getValue().getAsInt());else val.setValue(data.getValue().getAsString());
            }
        });
    }

    public enum Category {
        COMBAT("a"),
        MOVEMENT("b"),
        PLAYER("c"),
        EXPLOITS("d"),
        OTHER("e"),
        GHOST("f"),
        VISUALS("g");

        private final String character;

        Category(String character) {
            this.character = character;
        }

        public String getCharacter() {
            return character;
        }
    }
}
