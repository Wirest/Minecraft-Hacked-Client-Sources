package moonx.ohare.client.gui.tab.component.impl;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.tab.TabGUI;
import moonx.ohare.client.gui.tab.component.Component;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.visuals.HUD;
import moonx.ohare.client.utils.RenderUtil;

import moonx.ohare.client.utils.font.Fonts;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * made by oHare for eclipse
 *
 * @since 8/28/2019
 **/
public class CategoryComponent extends Component {
    private ArrayList<ModuleComponent> moduleComponents = new ArrayList<>();
    private Module.Category category;
    private TabGUI mainTab;
    private Module selectedModule;

    public CategoryComponent(TabGUI mainTab, Module.Category category, float posX, float posY, float width, float height) {
        super(StringUtils.capitalize(category.name().toLowerCase()), posX, posY, width, height);
        this.category = category;
        this.mainTab = mainTab;
        selectedModule = Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).isEmpty() ? null : Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).get(0);
    }

    @Override
    public void init() {
        float y = getPosY();
        for (Module module : Moonx.INSTANCE.getModuleManager().getModulesInCategory(category)) {
            moduleComponents.add(new ModuleComponent(this, module, getPosX() + getWidth() + 2, y, Moonx.INSTANCE.getModuleManager().getLongestModInCategory(category) + 22, 12));
            y += 12;
        }
        moduleComponents.forEach(Component::init);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution) {
        for (ModuleComponent moduleComponent : moduleComponents) {
            moduleComponent.setPosX(getPosX() + getWidth() + 2);
            moduleComponent.setWidth(Moonx.INSTANCE.getModuleManager().getLongestModInCategory(category) + 22);
        }
        int colorWaterMark = 0;
        switch (hud.MODECOLORS.getValue()) {
            case DEV:
                colorWaterMark = Color.getHSBColor(0, 0, 1f).getRGB();
                break;
            case LIGHTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.55f);
                break;
            case NORMALRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(6000, 0, 0.85f);
                break;
            case FASTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(3000, 0, 0.90f);
                break;
            case TESTRAINBOW:
                colorWaterMark = RenderUtil.getRainbow(2000, 0, 0.4f);
                break;
            case ASTOLFO:
                colorWaterMark = hud.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case VALENTINE:
                colorWaterMark = hud.getGradientOffset(new Color(255, 129, 202), new Color(255, 15, 0), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
                break;
            case WEIRD:
                colorWaterMark = hud.getGradientOffset(new Color(128, 171, 255), new Color(160, 72, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D)).getRGB();
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
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), mainTab.getSelectedCategory() == getCategory() ? colorWaterMark : new Color(0, 0, 0, 120).getRGB());
        if (mainTab.getSelectedCategory() == getCategory())
            Fonts.tabGuiIconFont.drawStringWithShadow(getCategory().getCharacter(),getPosX() + getWidth() - 2 - Fonts.tabGuiIconFont.getStringWidth(getCategory().getCharacter()), getPosY() + 3.5f,-1);
        if (hud.font.isEnabled())
            hud.fontValue.getValue().drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (mainTab.getSelectedCategory() == getCategory() ? 5 : 2), getPosY() + 2.5f, (mainTab.getSelectedCategory() == getCategory() ? -1 : 0xff808080));
        else
            hud.getMc().fontRendererObj.drawStringWithShadow(hud.MODECASE.getValue() == HUD.casemode.LOWER ? getLabel().toLowerCase() : (hud.MODECASE.getValue() == HUD.casemode.UPPER ? getLabel().toUpperCase() : getLabel()), getPosX() + (mainTab.getSelectedCategory() == getCategory() ? 5 : 2), getPosY() + 2.5f, (mainTab.getSelectedCategory() == getCategory() ? -1 : 0xff808080));
        if (mainTab.isExtendedModule() && mainTab.getSelectedCategory() == getCategory()) {
            moduleComponents.forEach(moduleComponent -> moduleComponent.onDraw(scaledResolution));
            RenderUtil.drawBorderedRect(getPosX() + getWidth() + 2, getPosY(), Moonx.INSTANCE.getModuleManager().getLongestModInCategory(category) + 22, 12 * moduleComponents.size(), 0.5f, 0xff000000, 0x00000000);
        }
    }

    @Override
    public void onKeyPress(int key) {
        if (moduleComponents.isEmpty()) return;
        if (mainTab.isExtendedModule() && mainTab.getSelectedCategory() == getCategory()) {
            switch (key) {
                case Keyboard.KEY_DOWN:
                    if (!mainTab.isExtendedValue() && !mainTab.isExtendedValueDynamic())
                        setSelectedModule(Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).get((Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).indexOf(getSelectedModule()) + 1) % Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).size()));
                    break;
                case Keyboard.KEY_UP:
                    if (!mainTab.isExtendedValue() && !mainTab.isExtendedValueDynamic())
                        setSelectedModule(Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).get((Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).indexOf(getSelectedModule()) - 1) < 0 ? Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).size() - 1 : (Moonx.INSTANCE.getModuleManager().getModulesInCategory(category).indexOf(getSelectedModule()) - 1)));
                    break;
                case Keyboard.KEY_RETURN:
                    if (!mainTab.isExtendedValue() && !mainTab.isExtendedValueDynamic())
                        getSelectedModule().setEnabled(!getSelectedModule().isEnabled());
                    break;
                case Keyboard.KEY_RIGHT:
                    if (!mainTab.isExtendedValue() && !mainTab.isExtendedValueDynamic() && !getSelectedModule().getValues().isEmpty())
                        mainTab.setExtendedValue(true);
                    break;
                case Keyboard.KEY_LEFT:
                    if (mainTab.isExtendedValue() && !mainTab.isExtendedValueDynamic()) mainTab.setExtendedValue(false);
                    break;
            }
            moduleComponents.forEach(moduleComponent -> moduleComponent.onKeyPress(key));
        }
    }

    public Module.Category getCategory() {
        return category;
    }

    public Module getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(Module selectedModule) {
        this.selectedModule = selectedModule;
    }

    public TabGUI getMainTab() {
        return mainTab;
    }
}
