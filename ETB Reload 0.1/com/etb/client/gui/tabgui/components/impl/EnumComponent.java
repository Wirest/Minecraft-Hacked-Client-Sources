package com.etb.client.gui.tabgui.components.impl;

import com.etb.client.gui.tabgui.components.Component;
import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.value.Value;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import com.etb.client.utils.value.parse.NumberHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/28/2019
 **/
public class EnumComponent extends Component {
    private EnumValue value;
    private ModuleComponent modulecomp;

    public EnumComponent(ModuleComponent modulecomp, EnumValue value, String label, float x, float y, float width, float height) {
        super(label, x, y, width, height);
        this.modulecomp = modulecomp;
        this.value = value;
    }

    public void init() {

    }

    @Override
    public void onRender(ScaledResolution sr) {
        super.onRender(sr);
        float largestString = mc.fontRendererObj.getStringWidth(modulecomp.getModule().getValues().get(0).getLabel() + (modulecomp.getModule().getValues().get(0) instanceof BooleanValue ? "" : ": " + modulecomp.getModule().getValues().get(0).getValue().toString()));
        for (int i = 0; i < modulecomp.getModule().getValues().size(); i++) {
            if (mc.fontRendererObj.getStringWidth(modulecomp.getModule().getValues().get(i).getLabel() + (modulecomp.getModule().getValues().get(i) instanceof BooleanValue ? "" : ": " + modulecomp.getModule().getValues().get(i).getValue().toString())) > largestString) {
                largestString = mc.fontRendererObj.getStringWidth(modulecomp.getModule().getValues().get(i).getLabel() + (modulecomp.getModule().getValues().get(i) instanceof BooleanValue ? "" : ": " + modulecomp.getModule().getValues().get(i).getValue().toString()));
            }
        }
        if (modulecomp.getSelectedValue() == value)
            RenderUtil.drawRect(getX(), getY(), largestString + 18, getHeight(), new Color(0xff4d4c).getRGB());
        mc.fontRendererObj.drawStringWithShadow(getLabel() + ": " + ChatFormatting.GRAY + StringUtils.capitalize(value.getValue().toString().toLowerCase()), modulecomp.getSelectedValue() == value ? getX() + 4 : getX() + 2, getY() + 2, -1);
    }

    @Override
    public void onKeyPress(int key) {
        switch (key) {
            case Keyboard.KEY_RIGHT:
                if (modulecomp.getSelectedValue() == value && modulecomp.getCategorycomp().getSelectedModule() == modulecomp.getModule()) {
                    value.increment();
                }
                break;
            case Keyboard.KEY_LEFT:
                if (modulecomp.getSelectedValue() == value && modulecomp.getCategorycomp().getTabMain().getSelectedCategory() == modulecomp.getCategorycomp().getTabMain().getSelectedCategory() && modulecomp.getCategorycomp().getSelectedModule() == modulecomp.getModule()) {
                    value.decrement();
                }
                break;
        }
    }

    public ModuleComponent getModuleComp() {
        return modulecomp;
    }
}
