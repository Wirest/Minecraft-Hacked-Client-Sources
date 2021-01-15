package nivia.gui.chod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.gui.chod.component.ChodsGuiButton;
import nivia.gui.chod.component.ChodsGuiScreen;
import nivia.modules.Module.Category;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ChodsGui extends GuiScreen {
    public Color outlineC      = new Color(0, 7, 7);
    public Color inlineC       = new Color(127, 127, 127);
    public Color backgroundC   = new Color(74, 77, 78);

    public Color insideColor1C = new Color(111, 111, 111);
    public Color insideInlineC = new Color(12, 19, 20);
    
    public static int tabcolor;
    public static int clickcolor;

    public int outline    = RenderUtils.getIntFromColor(outlineC);
    public int inline     = RenderUtils.getIntFromColor(inlineC);
    public int background = RenderUtils.getIntFromColor(backgroundC);

    public int insideColor1 = RenderUtils.getIntFromColor(insideColor1C);
    public int insideInline = RenderUtils.getIntFromColor(insideInlineC);

    public int backgroundWidth;
    public int backgroundHeight;
    public int posX;
    public int posY;
    private int dragX, dragY;
    private boolean dragging;

    int alpha;

    public ChodsGuiScreen selectedScreen;

    private ArrayList<ChodsGuiButton> buttons = new ArrayList<>();

    public ChodsGui() {
        this.backgroundWidth  = 680;
        this.backgroundHeight = 420;

        this.posX = Minecraft.getMinecraft().displayWidth/2 - backgroundWidth/2;
        this.posY = Minecraft.getMinecraft().displayHeight/2 - backgroundHeight/2;

        buttons.add(new ChodsGuiButton(0,0,0,0, Category.COMBAT,        new ResourceLocation("legacy/gui/Combat.png"), this));
        buttons.add(new ChodsGuiButton(0,0,0,0, Category.MISCELLANEOUS, new ResourceLocation("legacy/gui/Misc.png"), this));
        buttons.add(new ChodsGuiButton(0,0,0,0, Category.MOVEMENT,      new ResourceLocation("legacy/gui/Movement.png"), this));
        buttons.add(new ChodsGuiButton(0,0,0,0, Category.RENDER,        new ResourceLocation("legacy/gui/Render.png"), this));
        buttons.add(new ChodsGuiButton(0,0,0,0, Category.PLAYER,        new ResourceLocation("legacy/gui/Player.png"), this));
        buttons.add(new ChodsGuiButton(0,0,0,0, Category.EXPLOITS,          new ResourceLocation("legacy/gui/Config.png"), this));
        alpha = 0;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float target = 255;
        float dist = Math.abs(target - alpha);
        float change = target / 20;

        if (alpha != target) {
            alpha += change;
        }
        alpha = Math.min(255, alpha);
        alpha = Math.max(0, alpha);

        /*LEL*/
        Color outlineC      = new Color(this.outlineC.getRed(), this.outlineC.getGreen(), this.outlineC.getBlue(), 255);
        Color inlineC       = new Color(this.inlineC.getRed(), this.inlineC.getGreen(), this.inlineC.getBlue(), 255);
        Color backgroundC   = new Color(this.backgroundC.getRed(), this.backgroundC.getGreen(), this.backgroundC.getBlue(), 255);
        Color insideColor1C = new Color(this.insideColor1C.getRed(), this.insideColor1C.getGreen(), this.insideColor1C.getBlue(), 255);
        Color insideInlineC = new Color(this.insideInlineC.getRed(), this.insideInlineC.getGreen(), this.insideInlineC.getBlue(), 255);
        outline    = RenderUtils.getIntFromColor(outlineC);
        inline     = RenderUtils.getIntFromColor(inlineC);
        background = RenderUtils.getIntFromColor(backgroundC);
        insideColor1 = RenderUtils.getIntFromColor(insideColor1C);
        insideInline = RenderUtils.getIntFromColor(insideInlineC);
        
        
        
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        float scale = (float) sr.getScaleFactor() / (float) Math.pow(sr.getScaleFactor(), 2.0);
        GL11.glScalef(scale, scale, scale);

        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();
        if (dragging) {
            posX = (scaledMouseX - dragX);
            posY = (scaledMouseY - dragY);
        }

        GL11.glPushMatrix();
        Helper.get2DUtils().enableGL2D();
        /* <Actual code goes here> */
        if (selectedScreen == null) {
            int x1 = this.posX;
            int x2 = this.posX + backgroundWidth;
            int y1 = this.posY;
            int y2 = this.posY + backgroundHeight;
            RenderUtils.layeredRect(x1, y1, x2, y2, outline, inline, background);

            int insideBuffer = 10;
            RenderUtils.drawBorderedRect(x1 + insideBuffer, y1 + insideBuffer * 3, x2 - insideBuffer, y2 - insideBuffer, 1, insideColor1, insideInline);
            RenderUtils.cgothicgui.drawStringWithShadow(String.format("%s b%s", Pandora.getClientName(), Pandora.getClientVersion()) , x1 + insideBuffer, y1 + 9, -1);
           // Wrapper.getFontRenderer().drawStringWithShadow(EnumChatFormatting.BOLD + "Nivia b" + Pandora.getClientVersion(), x1 + insideBuffer, y1 + 9, -1);

            int interval = 1;
            int numberOfButtons = 6;
            int buttonWidth = 160;
            int buttonHeight = 160;
            int buffer = 70;
            int buttonOffset = 20;

            for (ChodsGuiButton button : buttons) {
                boolean firstRow = interval <= (numberOfButtons / 2);
                int horizontalOffset = 180;
                int verticalOffset = firstRow ? (buttonOffset + y1 + insideBuffer * 3) : (-buttonOffset + y2 - buttonHeight - insideBuffer);
                //ChodsGuiButton button = new ChodsGuiButton(x1 + insideBuffer + horizontalOffset, verticalOffset , buttonWidth, buttonHeight, cat.getName(), null);
                button.setPosX(buffer+ x1 + insideBuffer + (firstRow ? ((3-interval)*horizontalOffset) : (interval - 4) * horizontalOffset));
                button.setPosY(verticalOffset);
                button.setWidth(buttonWidth);
                button.setHeight(buttonHeight);
                button.drawButton(mouseX, mouseY);
                interval++;
            }
        }
        else {
            selectedScreen.drawScreen(mouseX, mouseY, partialTicks);
        }
        /* <Actual code stops here> */
        Helper.get2DUtils().disableGL2D();
        GL11.glPopMatrix();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();

        if (isInside(scaledMouseX, scaledMouseY, this.posX, this.posY, this.backgroundWidth * 2, 30)) {
            if (mouseButton == 0) {
                dragging = true;
                dragX = (scaledMouseX - posX);
                dragY = (scaledMouseY - posY);
            }
        }

        if (selectedScreen == null) {
            for (ChodsGuiButton button : buttons) {
                button.onClick(scaledMouseX, scaledMouseY, mouseButton);
            }
        }
        else {
            selectedScreen.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
        }
        super.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (selectedScreen != null) {
            selectedScreen.mouseReleased(mouseX, mouseY, state);
        }
        if (dragging) {
            dragging = false;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selectedScreen == null) {
            super.keyTyped(typedChar, keyCode);
        }
        else {
            selectedScreen.keyTyped(typedChar, keyCode);
        }
    }
    public boolean isInside(int pointX, int pointY, int x, int y, int width, int height) {
        return (pointX >= x && pointX <= x + width) && (pointY >= y && pointY <= y + height);
    }
}
