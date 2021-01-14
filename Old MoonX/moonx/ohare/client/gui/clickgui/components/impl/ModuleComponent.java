package moonx.ohare.client.gui.clickgui.components.impl;

import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.gui.clickgui.frames.impl.CategoryFrame;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ModuleComponent extends Component {
    private final Module module;
    private boolean binding, extended;
    private final CategoryFrame categoryFrame;

    public ModuleComponent(CategoryFrame categoryFrame, Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
        this.categoryFrame = categoryFrame;
    }

    @Override
    public void init() {
        float offsetY = 0;
        for (Value value : module.getValues()) {
            if (value instanceof BooleanValue) {
                getSubComponents().add(new BooleanComponent((BooleanValue) value, getPosX(), categoryFrame.getPosY() + categoryFrame.getHeight(), getWidth() + 2, offsetY, (module.getLongestValueInModule() * 1.25f), getHeight()));
                offsetY += getHeight();
            }
            if (value instanceof NumberValue) {
                getSubComponents().add(new NumberComponent((NumberValue) value, getPosX(), categoryFrame.getPosY() + categoryFrame.getHeight(), getWidth() + 2, offsetY, (module.getLongestValueInModule() * 1.25f), getHeight()));
                offsetY += getHeight();
            }
            if (value instanceof RangedValue) {
                getSubComponents().add(new RangedNumberComponent((RangedValue) value, getPosX(), categoryFrame.getPosY() + categoryFrame.getHeight(), getWidth() + 2, offsetY, (module.getLongestValueInModule() * 1.25f), getHeight()));
                offsetY += getHeight();
            }
            if (value instanceof ColorValue) {
                getSubComponents().add(new ColorComponent((ColorValue) value, getPosX(), categoryFrame.getPosY() + categoryFrame.getHeight(), getWidth() + 2, offsetY, (module.getLongestValueInModule() * 1.25f), getHeight()));
                offsetY += getHeight();
            }
            if (value instanceof EnumValue) {
                getSubComponents().add(new EnumComponent((EnumValue) value, getPosX(), categoryFrame.getPosY() + categoryFrame.getHeight(), getWidth() + 2, offsetY, (module.getLongestValueInModule() * 1.25f), getHeight()));
                offsetY += getHeight();
            }
        }
        super.init();
    }

    @Override
    public void onFrameMoved(float x, float y) {
        setPosX(x + getOffsetX());
        setPosY(y + getOffsetY());
        resetPositions();
        getSubComponents().forEach(subComponents -> subComponents.setPosX(getPosX() + getWidth() + 2));
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        super.onDraw(mouseX, mouseY, partialTicks);
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(isBinding() ? "Press a key..." : getLabel(), getPosX() + 3, getPosY() + 4, getModule().isEnabled() ? 0xffF136DB : -1);
        if (!getSubComponents().isEmpty())
            RenderUtil.drawImage(new ResourceLocation("textures/client/Gear.png"), getPosX() + getWidth() - 12, getPosY() + 2, 10, 10);
        if (isExtended()) {
            getSubComponents().stream().filter(component -> !component.isHidden()).forEach(subComponents -> subComponents.onDraw(mouseX, mouseY, partialTicks));
            resetPositions();
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        switch (button) {
            case 0:
                if (hovered) {
                    getModule().setEnabled(!getModule().isEnabled());
                }
                break;
            case 1:
                if (hovered && !getSubComponents().isEmpty()) {
                    setExtended(!isExtended());
                    if (isExtended()) {
                        categoryFrame.getComponents().stream().filter(component -> component instanceof ModuleComponent && component != this).forEach(component -> ((ModuleComponent) component).setExtended(false));
                    }
                }
                break;
            case 2:
                if (hovered)
                    setBinding(!isBinding());
                break;
            default:
                break;
        }
        if (isExtended())
            getSubComponents().stream().filter(component -> !component.isHidden()).forEach(subComponents -> subComponents.onMouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (isExtended())
            getSubComponents().stream().filter(component -> !component.isHidden()).forEach(subComponents -> subComponents.onMouseReleased(mouseX, mouseY, button));
    }

    @Override
    public void onKeyTyped(char character, int key) {
        super.onKeyTyped(character, key);
        if (isBinding()) {
            getModule().setKeybind(key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_SPACE || key == Keyboard.KEY_DELETE ? Keyboard.KEY_NONE : key);
            Printer.print("Bound " + getLabel() + " to " + Keyboard.getKeyName(getModule().getKeybind()));
            setBinding(false);
        }
        if (isExtended())
            getSubComponents().forEach(subComponents -> subComponents.onKeyTyped(character, key));
    }

    public Module getModule() {
        return module;
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    private void resetPositions() {
        float offset = 0;
        for (Component subComponent : getSubComponents()) {
            if (subComponent instanceof BooleanComponent) {
                final BooleanComponent booleanComponent = (BooleanComponent) subComponent;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                booleanComponent.setPosY(categoryFrame.getPosY() + categoryFrame.getHeight() + offset);
                offset += getHeight();
            }
            if (subComponent instanceof NumberComponent) {
                final NumberComponent numberComponent = (NumberComponent) subComponent;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                numberComponent.setPosY(categoryFrame.getPosY() + categoryFrame.getHeight() + offset);
                offset += getHeight();
            }
            if (subComponent instanceof ColorComponent) {
                final ColorComponent colorComponent = (ColorComponent) subComponent;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                colorComponent.setPosY(categoryFrame.getPosY() + categoryFrame.getHeight() + offset);
                offset += getHeight();
            }
            if (subComponent instanceof RangedNumberComponent) {
                final RangedNumberComponent rangedNumberComponent = (RangedNumberComponent) subComponent;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                rangedNumberComponent.setPosY(categoryFrame.getPosY() + categoryFrame.getHeight() + offset);
                offset += getHeight();
            }
            if (subComponent instanceof EnumComponent) {
                final EnumComponent enumComponent = (EnumComponent) subComponent;
                if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                    enumComponent.setHidden(true);
                    continue;
                }
                enumComponent.setHidden(false);
                enumComponent.setPosY(categoryFrame.getPosY() + categoryFrame.getHeight() + offset);
                offset += getHeight() + (enumComponent.isExtended() ? (enumComponent.getEnumValue().getConstants().length - 1) * getHeight() : 0);
            }
        }
    }
}
