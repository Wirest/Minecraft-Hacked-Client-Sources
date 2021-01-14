package com.etb.client.gui.clickui;

import com.etb.client.Client;
import com.etb.client.gui.clickui.component.impl.ModButton;
import com.etb.client.gui.clickui.component.impl.impl.BooleanComponent;
import com.etb.client.gui.clickui.component.impl.impl.EnumComponent;
import com.etb.client.gui.clickui.component.impl.impl.NumberComponent;
import com.etb.client.gui.clickui.frame.Frame;
import com.etb.client.module.Module;
import com.etb.client.utils.value.Value;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {
    private ArrayList<Frame> frames = new ArrayList<>();

    public ClickGUI() {
        int posX = 2;
        int posY = 2;
        for (Module.Category category : Module.Category.values()) {
            frames.add(new Frame(StringUtils.capitalize(category.name().toLowerCase()), posX, posY, 150, 17) {
                @Override
                public void init() {
                    int posY = 0;
                    for (Module module : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
                        if (module.getCategory() == category) {
                            components.add(new ModButton(this, module, 0, posY, getWidth(), 14, 18) {
                                int propY = 0;
                                @Override
                                public void init() {
                                    if (!module.getValues().isEmpty()) {
                                        for (Value value : module.getValues()) {
                                            if (value instanceof BooleanValue) {
                                                getComponents().add(new BooleanComponent(this, ((BooleanValue) value), getPosX() + 5, propY + 1, getWidth() - 10, 18));
                                                propY += 16;
                                            } else if (value instanceof NumberValue) {
                                                getComponents().add(new NumberComponent(this, ((NumberValue) value), getPosX() + 5, propY + 1, getWidth() - 10, 18));
                                                propY += 16;
                                            } else if (value instanceof EnumValue) {
                                                getComponents().add(new EnumComponent(this, ((EnumValue) value), getPosX() + 5, propY + 1, getWidth() - 10, 18));
                                                propY += 16;
                                            }
                                        }
                                    }
                                }

                            });
                            posY += 12;
                        }
                    }
                }
            });
            if (posX + 310 > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                posX = 2;
                posY += 19;
            } else {
                posX += 155;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {
        super.keyTyped(character, key);
        frames.forEach(frame -> frame.keyTyped(character, key));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        frames.forEach(frame -> frame.drawScreen(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, state));
    }


}
