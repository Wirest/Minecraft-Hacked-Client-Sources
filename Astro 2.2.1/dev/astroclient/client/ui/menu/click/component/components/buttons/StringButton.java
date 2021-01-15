package dev.astroclient.client.ui.menu.click.component.components.buttons;

import dev.astroclient.client.Client;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.components.Button;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.Timer;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class StringButton extends Button {
    private boolean editinig, rightTick;
    private String content = "";
    private Timer timer = new Timer();

    public StringButton(Category parent,String label, float posX, float posY, float width, float height) {
        super(parent, label, posX, posY, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 83.5f;
        Gui.drawRect(posX + 7.5f, posY - 1.5f, posX + getParent().getWidth() - 6, posY + 8.5f, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawBorderedRect(posX + 7.0f, posY - 2.0f, posX + getParent().getWidth() - 6, posY + 9.0f, 0.5f, 0xff282828, 0xff3C3C3C,false);
        Gui.drawRect(posX + 7.5f, posY - 1.5f, posX + getParent().getWidth() - 6.5F, posY + 8.5f, new Color(0, 0, 0, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
        Gui.drawGradientRect(posX + 8.0f, posY - 1.0f, posX + getParent().getWidth() - 7, posY + 8.0f, new Color(25, 25, 25, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB(), new Color(25, 25, 25, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());

        if (timer.hasReached(600) && isEditinig()) {
            rightTick ^= true;
            timer.reset();
        }
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(getLabel(), posX + 8, posY - 7, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        RenderUtils.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), posX + 7.5f, posY - 1.5f, 79.5f, 10.0f);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(isEditinig() ? content + (rightTick ? "_" : "") : content, posX + 10, posY + 2.5f, new Color(210, 210, 210, Math.min(getParent().getParent().getParent().getAlpha(), 255)).getRGB());
//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//        GL11.glPopMatrix();

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float posX = getParent().getParent().getPosX() + getParent().getPosX() + 50.5f + getPosX();
        float posY = getParent().getParent().getPosY() + getParent().getPosY() + 83.5f;
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 8.0f, posY - 1.0f, posX + getParent().getWidth() - 6, posY + 8.0f)) {
            if (mouseButton == 0) {
                setEditinig(!isEditinig());
                getParent().getParent().dragging = false;
//                if (isEditinig()) content = stringValue.getValue();
//                else stringValue.setValue(content);
                Keyboard.enableRepeatEvents(isEditinig());
            }
        } else {
            if (mouseButton == 0 && isEditinig()) {
                setEditinig(false);
                getParent().getParent().dragging = false;
              //  stringValue.setValue(content);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        super.keyTyped(typedChar, key);
        if (isEditinig()) {
            String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
            if (key == Keyboard.KEY_BACK) {
                if (content.length() > 1) {
                    content = content.substring(0, content.length() - 1);
                } else if (content.length() == 1) {
                    content = "";
                }
            } else if (key == Keyboard.KEY_RETURN) {
              //  stringValue.setValue(content);
                setEditinig(false);
            } else if (Character.isLetterOrDigit(typedChar) || Character.isSpaceChar(typedChar) || specialChars.contains(Character.toString(typedChar))) {
                if (Client.INSTANCE.smallFontRenderer.getStringWidth(content) < 230) {
                    content += Character.toString(typedChar);
                }
            }
        }

    }

    public String getContent() {
        return content;
    }

    public boolean isEditinig() {
        return editinig;
    }

    public void setEditinig(boolean editinig) {
        this.editinig = editinig;
    }
}