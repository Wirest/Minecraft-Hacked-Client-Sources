package moonx.ohare.client.gui.materialui.plane.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.materialui.component.Component;
import moonx.ohare.client.gui.materialui.component.impl.CategoryComponent;
import moonx.ohare.client.gui.materialui.plane.Plane;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;

public class MainPlane extends Plane {
    private Module.Category selectedCategory = Module.Category.COMBAT;
    private ArrayList<Component> components = new ArrayList<>();
    public MainPlane(String label, float posX, float posY, float width, float height) {
        super(label, posX, posY, width, height);
    }

    @Override
    public void initializePlane() {
        super.initializePlane();
        for (Module.Category category : Module.Category.values()) {
            components.add(new CategoryComponent(category,getPosX(),getPosY(),46.5f,45f,getWidth() - 46.5f,getHeight() - 45f));
        }
        components.forEach(Component::initializeComponent);
    }

    @Override
    public void planeMoved(float movedX, float movedY) {
        super.planeMoved(movedX, movedY);
        components.forEach(component -> component.componentMoved(movedX, movedY));
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        if (isDragging()) {
            setPosX(mouseX + getLastPosX());
            setPosY(mouseY + getLastPosY());
            planeMoved(getPosX(), getPosY());
        }
        if (getPosX() < 0) {
            setPosX(0);
            planeMoved(getPosX(), getPosY());
        }
        if (getPosX() + getWidth() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            setPosX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - getWidth());
            planeMoved(getPosX(), getPosY());
        }
        if (getPosY() < 0) {
            setPosY(0);
            planeMoved(getPosX(), getPosY());
        }
        if (getPosY() + getHeight() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            setPosY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - getHeight());
            planeMoved(getPosX(), getPosY());
        }
        RenderUtil.drawRoundedRect(getPosX(), getPosY(), getWidth(), getHeight(), 3, new Color(45, 45, 45, 255).getRGB());
        RenderUtil.drawImage(new ResourceLocation("textures/client/logo.png"), getPosX() + 5.5f, getPosY() + 6, 32, 32);
        RenderUtil.drawUnfilledCircle(getPosX() + 5f, getPosY() + 5f, 33, new Color(45, 45, 45, 255).getRGB());
        float categoryOffsetY = getPosY() + 55;
        for (Module.Category category : Module.Category.values()) {
            if (getSelectedCategory() == category) {
                RenderUtil.drawRect(getPosX(), categoryOffsetY - (Fonts.clickGuiIconFont.getHeight() * 2.5f) / 4 - 4, 42.5f, Fonts.clickGuiIconFont.getHeight() * 2.5f, new Color(35, 35, 35, 255).getRGB());
                RenderUtil.drawRect(getPosX(), categoryOffsetY - (Fonts.clickGuiIconFont.getHeight() * 2.5f) / 4 - 4, 2, Fonts.clickGuiIconFont.getHeight() * 2.5f, new Color(0xff689FFF).getRGB());
            }
            Fonts.clickGuiIconFont.drawStringWithShadow(category.getCharacter(), getPosX() + 12, categoryOffsetY, getSelectedCategory() == category ? new Color(0xff689FFF).getRGB() : new Color(229, 229, 223, 255).getRGB());
            categoryOffsetY += Fonts.clickGuiIconFont.getHeight() * 2.5f;
        }
        RenderUtil.drawRect(getPosX() + 42.5, getPosY(), 100, getHeight(), new Color(35, 35, 35, 255).getRGB());
        Fonts.clickGuiTitleFont.drawStringWithShadow(StringUtils.capitalize(getSelectedCategory().name().toLowerCase()) + " (" + Moonx.INSTANCE.getModuleManager().getModulesInCategory(getSelectedCategory()).size() + ")", getPosX() + 45, getPosY() + 24, new Color(229, 229, 223, 255).getRGB());
        RenderUtil.drawRect(getPosX() + 42.5, getPosY() + 36, 100, 1, new Color(45, 45, 45, 255).getRGB());
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onDrawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), 15);
        if (button == 0) {
            if (hovered) {
                setLastPosX(getPosX() - mouseX);
                setLastPosY(getPosY() - mouseY);
                setDragging(true);
            }
            float categoryOffsetY = getPosY() + 55;
            for (Module.Category category : Module.Category.values()) {
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), categoryOffsetY - (Fonts.clickGuiIconFont.getHeight() * 2.5f) / 4 - 4, 42.5f, Fonts.clickGuiIconFont.getHeight() * 2.5f))
                    setSelectedCategory(category);
                categoryOffsetY += Fonts.clickGuiIconFont.getHeight() * 2.5f;
            }
        }
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && isDragging()) {
            setDragging(false);
        }
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onKeyTyped(character, keyCode);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory()) categoryComponent.onGuiClosed();
            }
        }
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
}
