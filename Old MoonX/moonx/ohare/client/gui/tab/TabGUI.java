package moonx.ohare.client.gui.tab;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.gui.tab.component.impl.CategoryComponent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class TabGUI {
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private ArrayList<Component> components = new ArrayList<>();
    private float posX, posY, width;
    private boolean extendedModule, extendedValue, extendedValueDynamic;

    public TabGUI(float posX, float posY, float width) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
    }

    public void init() {
        float y = posY;
        for (Module.Category category : Module.Category.values()) {
            components.add(new CategoryComponent(this, category, posX, y, width, 12));
            y += 12;
        }
        components.forEach(Component::init);
    }

    public void onDraw(ScaledResolution scaledResolution) {
        components.forEach(component -> component.onDraw(scaledResolution));
        RenderUtil.drawBorderedRect(posX,posY,width,12 * components.size(),0.5f,0xff000000,0x00000000);
    }

    public void onKeyPress(int key) {
        if (key == Keyboard.KEY_LEFT) {
            if (isExtendedModule() && !isExtendedValue()) setExtendedModule(false);
        }
        if (isExtendedModule()) components.forEach(component -> component.onKeyPress(key));
        switch (key) {
            case Keyboard.KEY_DOWN:
                if (!isExtendedModule() && !isExtendedValue())
                    setSelectedCategory(Module.Category.values()[(getSelectedCategory().ordinal() + 1) % Module.Category.values().length]);
                break;
            case Keyboard.KEY_UP:
                if (!isExtendedModule() && !isExtendedValue())
                    setSelectedCategory(Module.Category.values()[(getSelectedCategory().ordinal() - 1) < 0 ? Module.Category.values().length - 1 : (getSelectedCategory().ordinal() - 1)]);
                break;
            case Keyboard.KEY_RIGHT:
                if (!isExtendedModule() && !isExtendedValue() && !Moonx.INSTANCE.getModuleManager().getModulesInCategory(selectedCategory).isEmpty()) setExtendedModule(true);
                break;
            default:
                break;
        }
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public Module.Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Module.Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public boolean isExtendedModule() {
        return extendedModule;
    }

    public void setExtendedModule(boolean extendedModule) {
        this.extendedModule = extendedModule;
    }

    public boolean isExtendedValue() {
        return extendedValue;
    }

    public void setExtendedValue(boolean extendedValue) {
        this.extendedValue = extendedValue;
    }

    public boolean isExtendedValueDynamic() {
        return extendedValueDynamic;
    }

    public void setExtendedValueDynamic(boolean extendedValueDynamic) {
        this.extendedValueDynamic = extendedValueDynamic;
    }
}
