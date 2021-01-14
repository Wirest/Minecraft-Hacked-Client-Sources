package moonx.ohare.client.gui.clickgui.components.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import moonx.ohare.client.gui.clickgui.components.Component;
import moonx.ohare.client.utils.MouseUtil;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class EnumComponent extends Component {
    private final EnumValue enumValue;
    private boolean extended = false;
    public EnumComponent(EnumValue enumValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(enumValue.getLabel(), posX, posY, offsetX, offsetY, width, height);
        this.enumValue = enumValue;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onFrameMoved(float x, float y) {
        super.onFrameMoved(x, y);
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        super.onDraw(mouseX, mouseY, partialTicks);
        RenderUtil.drawRect(getPosX(), getPosY(), getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString(getLabel() + ": " + ChatFormatting.LIGHT_PURPLE + StringUtils.capitalize(enumValue.getValue().toString().toLowerCase()), getPosX() + 3, getPosY() + 6, -1);
        if (isExtended()) {
            float y = getPosY() + getHeight();
            for (Enum enoom : getEnumValue().getConstants()) {
                if (enoom != getEnumValue().getValue()) {
                    RenderUtil.drawRect(getPosX(), y, getWidth(), getHeight(), new Color(10, 10, 10, 200).getRGB());
                    Minecraft.getMinecraft().fontRendererObj.drawString(StringUtils.capitalize(enoom.name().toLowerCase()), getPosX() +3, y + 6, -1);
                    y+= getHeight();
                }
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), getHeight());
        if (button == 0) {
            if (hovered) {
                setExtended(!isExtended());
            } else if (isExtended()) {
                float y = getPosY() + getHeight();
                for (Enum enoom : getEnumValue().getConstants()) {
                    if (enoom != getEnumValue().getValue()) {
                        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), y, getWidth(), getHeight())) {
                            getEnumValue().setValue(enoom);
                            setExtended(false);
                        }
                        y+= getHeight();
                    }
                }
            }
        }
    }


    public EnumValue getEnumValue() {
        return enumValue;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }
}
