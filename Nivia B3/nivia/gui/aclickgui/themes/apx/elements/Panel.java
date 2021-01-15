package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;
import nivia.gui.aclickgui.Element;
import nivia.gui.aclickgui.GuiAPX;
import nivia.utils.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apex on 8/19/2016.
 */
public class Panel extends Element {

    public String category;
    public List<Element> elements = new ArrayList<>();
    public int maxHeight = 350;

    public boolean dragging;
    public float newXd;
    public float newYd;

    protected int fadeTicks = 0;
    protected boolean fade = false;
    public boolean draggingBot;

    public boolean minimized = false;
    protected boolean maximizing = false;
    protected boolean minimizing = false;
    protected float minimizeFade = 0;

    public Panel(String category) {
        this.category = category;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            this.dragging = false;
            this.draggingBot = false;
        }
        for (Element elButton : elements) {
            elButton.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public int totalHeight = 0;

    @Override
    public void keyTyped(char typedChar, int keyCode)  {
        for (Element e : elements) {
            e.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
        boolean isOverAll = mouseX > getPosX() && mouseY > getPosY() && mouseX < getPosX() + getWidth() && mouseY < getPosY() + getHeight();


        if (dragging){
            setPosX(mouseX + newXd);
            setPosY(mouseY + newYd);
        }
        if (draggingBot) {
            int fuck = (int) (mouseY - (getPosY()));
            int height = 14;
            for (Element elButton : elements) {
                height += 14;
            }
            if (fuck < height) fuck = height;
            this.maxHeight = (int) fuck;
            if (maxHeight > getHeight()) maxHeight = (int) getHeight();
        }


        double speed = 0;
        if (isOverAll) {
            totalHeight = (int) getPosY();
            for (Element el : elements) {
                totalHeight += el.getHeight() * 1.11F;
            }
           

            speed = Math.round(scrollAmount / 20);
            if (scrollAmount > 0)
                speed = speed * 2;
            if (scrollAmount > 0) {
                if (scrollAmount - speed <= 0)
                    scrollAmount = 0;
                else
                    scrollAmount -= speed;
            } else if (scrollAmount < 0) {
                if (scrollAmount - speed >= 0)
                    scrollAmount = 0;
                else
                    scrollAmount -= speed;
            }

            if (scrollAmount != 0) {
                translateY += (scrollAmount / 25);
            }

            if (translateY < (-totalHeight + maxHeight) && getHeight() >= maxHeight) {
                translateY = (-totalHeight + maxHeight);
                scrollAmount = 0;
            }
            if (translateY > 0) {
                translateY = 0;
                scrollAmount = 0;
            }
        } else {
            scrollAmount = 0;
        }
        if (speed == 0) scrollAmount = 0;

        if (!minimizing && !maximizing)
            Helper.get2DUtils().prepareScissorBox((int) getPosX(), (int) getPosY() - 3, (int) getPosX() + this.getWidth(), (int) (getPosY() + maxHeight));
        else {
        	Helper.get2DUtils().prepareScissorBox((int) getPosX(), (int) getPosY() - 3, (int) getPosX() + this.getWidth(), (int) (getPosY() + minimizeFade));
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        if (!minimized) Helper.get2DUtils().drawRect(getPosX(), getPosY() + 14, getPosX() + getWidth(), getPosY() + getHeight(), 0xFF111111);
        FontRenderer titleFont = Helper.mc().fontRendererObj;
        String name = WordUtils.capitalize(category.toLowerCase());
        Helper.get2DUtils().drawRect(getPosX(), getPosY() - 100, getPosX() + getWidth(), getPosY() + 16, 0xFF202020);
        Helper.get2DUtils().drawRect(getPosX(), getPosY() - 5, getPosX() + getWidth(), getPosY() + 14, 0xFF252525);
        
        boolean overMinimize = mouseX > getPosX() + getWidth() - 12 && mouseY > getPosY() && mouseX < getPosX() + getWidth() && mouseY < getPosY() + 14;
        //Helper.get2DUtils().drawRect(getPosX() + getWidth() - 8, getPosY() + 5.5F, getPosX() + getWidth() - 3, getPosY() + 6.5F, 0xFFFFFFFF);

        titleFont.drawStringWithShadow(name, ((getPosX() + getWidth() / 2) - titleFont.getStringWidth(name) / 2) , getPosY() + 1.5f, 0xFFFFFFFF);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (!minimized) {
            if (getHeight() >= maxHeight && !minimizing && !maximizing) {
            	Helper.get2DUtils().drawBorderedRect(getPosX() + getWidth() - 3, getPosY() + 16, getPosX() + getWidth() - 1, getPosY() + maxHeight - 2, 1, 0x4FFFFFFF);

                float pval = (-translateY * 100 / ((totalHeight - maxHeight) + 0.001F));
                float percent = (pval * (maxHeight-80) / 100);
                Helper.get2DUtils().drawBorderedRect(

                        getPosX() + getWidth() - 3,
                        getPosY() + 16 + percent,
                        getPosX() + getWidth() - 1,
                        getPosY() + 40 + percent + 38,

                        GuiAPX.color, GuiAPX.color);
            }

            GlStateManager.pushMatrix();
            int height = 0;
            for (Element elButton : elements) {
                int endY = (int) (elButton.getPosY() + elButton.getHeight());
                if (minimizing || maximizing) {
                    endY = (int) (endY > (getPosY() + minimizeFade) ? (getPosY() + minimizeFade) : endY);
                }

                int startY = (int) getPosY() + 14;
                if (endY > (int) getPosY() + (maxHeight - 2))
                    endY = (int) getPosY() + (maxHeight - 2 );

                if (startY >= endY) {
                    startY = endY;
                }
                Helper.get2DUtils().prepareScissorBox((int) elButton.getPosX(), (int) startY  + 1, (int) elButton.getPosX() + this.getWidth(), (int) (endY + 1));
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                float butX = getPosX();
                float butY = getPosY() + 14 + height;

                elButton.drawElement(butX, butY + ((int) translateY), mouseX, mouseY, partialTicks);
                height += elButton.getHeight();
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
            GlStateManager.popMatrix();


            float nHeight = getHeight();
            if (nHeight > maxHeight - 1)
                nHeight = maxHeight - 1;

            boolean isOver = mouseX > getPosX() && mouseX < getPosX() + getWidth() && mouseY > getPosY() + nHeight && mouseY < getPosY() + nHeight + 8;


            int tickSpeed = 15;
            if (isOver || draggingBot) {
                fade = true;
            }

            if (fade) {
                if (fadeTicks < tickSpeed)
                    fadeTicks++;
                else
                    fade = false;
            } else if (fadeTicks > 0) {
                --fadeTicks;
            }

            if ((isOver || draggingBot || fadeTicks != 0) && getHeight() > maxHeight - 10) {
                int colorIfOverF = 0x00FFFFFF;
                int colorIfOver = 0x00000000;

                int multiplier = 150 / tickSpeed;
                colorIfOverF = (((fadeTicks / 2) * multiplier) << 24) | colorIfOverF;
                colorIfOver = ((fadeTicks * multiplier) << 24) | colorIfOver;

                Helper.get2DUtils().drawRect(getPosX(), getPosY() + nHeight, getPosX() + getWidth(), getPosY() + nHeight + 8, colorIfOver);
                Helper.get2DUtils().drawRect(getPosX() + getWidth() / 2 - 4, getPosY() + nHeight + 3.5f, getPosX() + getWidth() / 2 + 4, getPosY() + nHeight + 4.5f, colorIfOverF);
            }
            if (getHeight() < maxHeight) {
                if (translateY < 0)
                    translateY = 0;
            }
        }

        if (minimizing && minimizeFade > 14 + ((10 * (Minecraft.debugFPS + 1)) / 150))
            minimizeFade -= (10 * (Minecraft.debugFPS + 1)) / 150;
        else if (minimizing) {
            minimized = true;
            maximizing = false;
            minimizing = false;
            minimizeFade = 0;
        }

        if (maximizing) {
            if (minimizeFade >= maxHeight) {
                minimized = false;
                maximizing = false;
                minimizing = false;
                minimizeFade = 0;
            } else {
                minimizeFade += (10 * (Minecraft.debugFPS + 1)) / 150;
            }
        }
    }

    public double scrollAmount = 0;
    public int translateY = 0;

    public void handleMouseInput(int direction) {
        if (direction != 0 && getHeight()>=maxHeight) {
            scrollAmount += direction;
        }
    }

    public void update() {
        int height = 16;
        for (Element elButton : elements) {
            height += elButton.getHeight();
        }
        setHeight(height);
    }

    public boolean isOverPanel(int mouseX, int mouseY) {
        if (minimized)
            return mouseX > getPosX() && mouseX < getPosX() + getWidth() && mouseY > getPosY() && mouseY < getPosY() + 14;
        return mouseX > getPosX() && mouseX < getPosX() + getWidth() && mouseY > getPosY() && mouseY < getPosY() + getHeight() + 8;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean isOverDRAG = mouseX > getPosX() && mouseY > getPosY() && mouseX < getPosX() + getWidth() && mouseY < getPosY() + 14;
        float nHeight = getHeight();
        if (nHeight > maxHeight-1)
            nHeight = maxHeight-1;

        boolean isOver = mouseX > getPosX() && mouseX < getPosX() + getWidth() && mouseY > getPosY() + nHeight && mouseY < getPosY() + nHeight + 8;
        boolean overMinimize = mouseX > getPosX() + getWidth() - 12 && mouseY > getPosY() && mouseX < getPosX() + getWidth() && mouseY < getPosY() + 14;
        if (overMinimize && button == 0 || isOverDRAG && button == 1) {
            if (minimized) {
                maximizing = true;
                minimized = false;
                minimizing = false;
                minimizeFade = 14;
            } else {
                maximizing = false;
                minimized = false;
                minimizing = true;
                if (getHeight() > maxHeight)
                    minimizeFade = maxHeight;
                else
                    minimizeFade = getHeight();
            }

        } else if (isOverDRAG && button == 0) {
            this.dragging = true;
            this.newXd = (this.getPosX() - mouseX);
            this.newYd = (this.getPosY() - mouseY);
        } else if (isOver && button == 0) {
            this.draggingBot = true;
        } else {
            for (Element elButton : elements) {
                if (mouseY < (getPosY() + maxHeight))
                    elButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
