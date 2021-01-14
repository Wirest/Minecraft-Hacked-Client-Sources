package moonx.ohare.client.gui.tab.component.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.gui.tab.component.impl.value.*;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.impl.*;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class ModuleComponent extends Component {
    private Module module;
    private CategoryComponent parentComponent;
    private ArrayList<Component> components = new ArrayList<>();
    private Value selectedValue;

    public ModuleComponent(CategoryComponent parentComponent, Module module, float posX, float posY, float width, float height) {
        super(module.getLabel(), posX, posY, width, height);
        this.module = module;
        this.parentComponent = parentComponent;
        selectedValue = module.getValues().isEmpty() ? null : module.getValues().get(0);
    }

    @Override
    public void init() {
        float y = getPosY();
        for (Value value : module.getValues()) {
            if (value instanceof BooleanValue)
                components.add(new BooleanComponent(this, (BooleanValue) value, getPosX() + getWidth() + 2, y, module.getLongestValueInModule() + 15, 12));
            else if (value instanceof NumberValue)
                components.add(new NumberComponent(this, (NumberValue) value, getPosX() + getWidth() + 2, y, module.getLongestValueInModule() + 15, 12));
            else if (value instanceof EnumValue)
                components.add(new EnumComponent(this, (EnumValue) value, getPosX() + getWidth() + 2, y, module.getLongestValueInModule() + 15, 12));
            else if (value instanceof ColorValue)
                components.add(new ColorComponent(this, (ColorValue) value, getPosX() + getWidth() + 2, y, module.getLongestValueInModule() + 15, 12));
            else if (value instanceof RangedValue)
                components.add(new RangedNumberComponent(this, (RangedValue) value, getPosX() + getWidth() + 2, y, module.getLongestValueInModule() + 15, 12));
            if (!(value instanceof FontValue || value instanceof StringValue))
                y += 12;
        }
        components.forEach(Component::init);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (Component component : components) {
            component.setPosX(getPosX() + getWidth() + 2);
            component.setWidth(module.getLongestValueInModule() + 15);
        }
        int colorWaterMark = 0;
        switch (hud.MODECOLORS.getValue()) {
            case DEV:
                colorWaterMark = Color.getHSBColor(0, 0, 1f).getRGB();
                break;
            case LIGHTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.55f);
                break;
            case ASTOLFO:
                colorWaterMark = hud.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case VALENTINE:
                colorWaterMark = hud.getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case NORMALRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.85f);
                break;
            case FASTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(3000, 0, 0.90f);
                break;
            case WEIRD:
                colorWaterMark = hud.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case TESTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(2000, 0, 0.4f);
                break;
            case RANDOM:
            case MODULECOLOR:
            case CLIENTCOLOR:
                colorWaterMark = hud.colorValue.getValue();
                break;
            case WAVE:
                colorWaterMark = hud.color(0, 100);
                break;
        }
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), parentComponent.getSelectedModule() == getModule() ? colorWaterMark : new Color(0, 0, 0, 120).getRGB());
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedModule() == getModule() ? 5 : 2), getPosY() + 2.5f, module.isEnabled() ? -1 : 0xff808080);
        else
            hud.getMc().fontRendererObj.drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (parentComponent.getSelectedModule() == getModule() ? 5 : 2), getPosY() + 2.5f, module.isEnabled() ? -1 : 0xff808080);
        if (parentComponent.getMainTab().isExtendedValue() && parentComponent.getSelectedModule() == getModule()) {
            Keyboard.enableRepeatEvents(true);
            components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onDraw(scaledResolution));
            RenderUtil.drawBorderedRect(getPosX() + getWidth() + 2, getPosY(), module.getLongestValueInModule() + 15, 12 * (int) module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count(), 0.5f, 0xff000000, 0x00000000);
            resetPositions();
        }
    }

    @Override
    public void onKeyPress(int key) {
        if (parentComponent.getMainTab().isExtendedValue() && parentComponent.getSelectedModule() == getModule()) {
            components.stream().filter(component -> !component.isHidden()).forEach(component -> component.onKeyPress(key));
            switch (key) {
                case Keyboard.KEY_DOWN:
                    if (getParentComponent().getMainTab().isExtendedValue() && !getParentComponent().getMainTab().isExtendedValueDynamic())
                        setSelectedValue(module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).get((module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(getSelectedValue()) + 1) % (int) module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null  && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count()));
                    break;
                case Keyboard.KEY_UP:
                    if (getParentComponent().getMainTab().isExtendedValue() && !getParentComponent().getMainTab().isExtendedValueDynamic())
                        setSelectedValue(module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).get((module.getValues().stream().filter(value ->!(value instanceof FontValue) && !(value instanceof StringValue) && !(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(getSelectedValue()) - 1) < 0 ? (int) module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null  && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).count() - 1 : (module.getValues().stream().filter(value -> !(value instanceof FontValue) && !(value instanceof StringValue) &&!(value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))).collect(Collectors.toList()).indexOf(getSelectedValue()) - 1)));
                    break;
                default:
                    break;
            }
        }
    }

    private void resetPositions() {
        float y = getPosY();
        for (Component subComponent : components) {
            if (subComponent instanceof BooleanComponent) {
                final BooleanComponent booleanComponent = (BooleanComponent) subComponent;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                booleanComponent.setPosY(y);
                y += 12;
            }
            if (subComponent instanceof NumberComponent) {
                final NumberComponent numberComponent = (NumberComponent) subComponent;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                numberComponent.setPosY(y);
                y += 12;
            }
            if (subComponent instanceof ColorComponent) {
                final ColorComponent colorComponent = (ColorComponent) subComponent;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                colorComponent.setPosY(y);
                y += 12;
            }
            if (subComponent instanceof RangedNumberComponent) {
                final RangedNumberComponent rangedNumberComponent = (RangedNumberComponent) subComponent;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                rangedNumberComponent.setPosY(y);
                y += 12;
            }
            if (subComponent instanceof EnumComponent) {
                final EnumComponent enumComponent = (EnumComponent) subComponent;
                if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                    enumComponent.setHidden(true);
                    continue;
                }
                enumComponent.setHidden(false);
                enumComponent.setPosY(y);
                y += 12;
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public CategoryComponent getParentComponent() {
        return parentComponent;
    }

    public Value getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Value selectedValue) {
        this.selectedValue = selectedValue;
    }

}
