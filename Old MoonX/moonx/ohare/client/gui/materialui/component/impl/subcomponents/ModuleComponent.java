package moonx.ohare.client.gui.materialui.component.impl.subcomponents;

import moonx.ohare.client.gui.materialui.component.Component;
import moonx.ohare.client.gui.materialui.component.impl.CategoryComponent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.Fonts;
import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.impl.*;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class ModuleComponent extends Component {
    private Module module;
    private ArrayList<Component> components = new ArrayList<>();
    private boolean extended;
    private CategoryComponent categoryComponent;
    private int scrollY;

    public ModuleComponent(CategoryComponent categoryComponent, Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
        this.categoryComponent = categoryComponent;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (!module.getValues().isEmpty()) {
            float valueOffsetY = 0;
            for (Value value : module.getValues()) {
                if (value instanceof BooleanValue) {
                    components.add(new BooleanComponent((BooleanValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof EnumValue) {
                    components.add(new EnumComponent((EnumValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof NumberValue) {
                    components.add(new NumberComponent((NumberValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof RangedValue) {
                    components.add(new RangedNumberComponent((RangedValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof ColorValue) {
                    components.add(new ColorComponent((ColorValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
            }
        }
        components.forEach(Component::initializeComponent);
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
        if (isExtended()) {
            for (Component component : getComponents()) {
                component.componentMoved(getPosX(), getCategoryComponent().getPosY());
            }
        }
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        double scrollbarHeight = (getCategoryComponent().getHeight() / getComponentHeight()) * getCategoryComponent().getHeight();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getCategoryComponent().getPosX() + 100, getCategoryComponent().getPosY(), 175, getCategoryComponent().getHeight()) && getComponentHeight() >= getCategoryComponent().getHeight()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (getScrollY() - 6 < -(getComponentHeight() - getCategoryComponent().getHeight()))
                    setScrollY((int) -(getComponentHeight() - getCategoryComponent().getHeight()));
                else setScrollY(getScrollY() - 6);
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 6);
            }
        }
        if (getScrollY() > 0) setScrollY(0);
        if (getComponentHeight() >= getCategoryComponent().getHeight()) {
            if (getScrollY() - 6 < -(getComponentHeight() - getCategoryComponent().getHeight()))
                setScrollY((int) -(getComponentHeight() - getCategoryComponent().getHeight()));
        } else if (getScrollY() < 0) setScrollY(0);
        Fonts.clickGuiFont.drawStringWithShadow(getLabel(), getPosX(), getPosY(), module.isEnabled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        if (!getComponents().isEmpty())
            Fonts.clickGuiFont.drawStringWithShadow("...", getPosX() + getWidth() - Fonts.clickGuiFont.getStringWidth("..."), getPosY() - 2, module.isEnabled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        if (isExtended()) {
            for (Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onDrawScreen(mouseX, mouseY, partialTicks);
                component.setOffsetY(component.getOriginalOffsetY() + getScrollY());
                component.componentMoved(getPosX(), getCategoryComponent().getPosY());
            }
            if (getComponentHeight() >= getCategoryComponent().getHeight()) {
                RenderUtil.drawRect(getCategoryComponent().getPosX() + 275, getCategoryComponent().getPosY() - 6, 2, getCategoryComponent().getHeight() + 6, new Color(55, 55, 55, 255).getRGB());
                RenderUtil.drawRect(getCategoryComponent().getPosX() + 275, getCategoryComponent().getPosY() - 6 - (((getCategoryComponent().getHeight() - (scrollbarHeight - 4)) / (getComponentHeight() - (getCategoryComponent().getHeight()))) * getScrollY()), 2, scrollbarHeight, new Color(40, 40, 40, 255).getRGB());
            }
            setupHeight();
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() - 4, getWidth(), getHeight() - 8)) {
            getModule().setEnabled(!getModule().isEnabled());
        }
        if (button == 1 && !getComponents().isEmpty() && MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() - 4, getWidth(), getHeight() - 8)) {
            getCategoryComponent().getComponents().stream().filter(component -> component instanceof ModuleComponent && component != this).forEach(component -> ((ModuleComponent) component).setExtended(false));
            setExtended(!isExtended());
        }
        if (isExtended()) {
            for (Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (isExtended()) {
            for (Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        if (isExtended()) {
            for (Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onKeyTyped(character, keyCode);
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public CategoryComponent getCategoryComponent() {
        return categoryComponent;
    }

    public int getComponentHeight() {
        int h = 0;
        for (Component component : getComponents()) {
            if (component.isHidden()) continue;
            h += component.getHeight();
        }
        return h;
    }

    public void setupHeight() {
        int h = getScrollY();
        for (Component component : getComponents()) {
            if (component instanceof BooleanComponent) {
                final BooleanComponent booleanComponent = (BooleanComponent) component;
                if (booleanComponent.getBooleanValue().getParentValueObject() != null && !booleanComponent.getBooleanValue().getParentValueObject().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParentValue())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof NumberComponent) {
                final NumberComponent numberComponent = (NumberComponent) component;
                if (numberComponent.getNumberValue().getParentValueObject() != null && !numberComponent.getNumberValue().getParentValueObject().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParentValue())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof ColorComponent) {
                final ColorComponent colorComponent = (ColorComponent) component;
                if (colorComponent.getColorValue().getParentValueObject() != null && !colorComponent.getColorValue().getParentValueObject().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParentValue())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof RangedNumberComponent) {
                final RangedNumberComponent rangedNumberComponent = (RangedNumberComponent) component;
                if (rangedNumberComponent.getRangedValue().getParentValueObject() != null && !rangedNumberComponent.getRangedValue().getParentValueObject().getValueAsString().equalsIgnoreCase(rangedNumberComponent.getRangedValue().getParentValue())) {
                    rangedNumberComponent.setHidden(true);
                    continue;
                }
                rangedNumberComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof EnumComponent) {
                final EnumComponent enumComponent = (EnumComponent) component;
                if (enumComponent.getEnumValue().getParentValueObject() != null && !enumComponent.getEnumValue().getParentValueObject().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParentValue())) {
                    enumComponent.setHidden(true);
                    continue;
                }
                enumComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
        }
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}
