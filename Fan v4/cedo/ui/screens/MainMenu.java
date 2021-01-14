package cedo.ui.screens;

import cedo.Fan;
import cedo.altmanager.BetterAltManager;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.changelog.Change;
import cedo.ui.changelog.ChangeType;
import cedo.ui.elements.Draw;
import cedo.ui.elements.Rectangle;
import cedo.ui.particles.ParticleEngine;
import cedo.util.font.FontUtil;
import net.josephworks.GLSLSandbox.GLSLSandboxShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;


public class MainMenu extends GuiScreen { //TODO: make Fan Client banner hover around

    static boolean mainMenuInit;


    private final Rectangle exitButton = new Rectangle() {
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY))
                Minecraft.getMinecraft().shutdown();
        }
    };
    private final Rectangle[] circleRectangles = {new Rectangle() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY))
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectWorld(Minecraft.getMinecraft().currentScreen));
        }
    }, new Rectangle() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - mouseX, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - mouseY, 2)) <= this.getWidth() / 2f)
                Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(Minecraft.getMinecraft().currentScreen));
        }
    }, new Rectangle() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - mouseX, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - mouseY, 2)) <= this.getWidth() / 2f)
                Minecraft.getMinecraft().displayGuiScreen(new BetterAltManager(Minecraft.getMinecraft().currentScreen));
        }
    }, new Rectangle() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - mouseX, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - mouseY, 2)) <= this.getWidth() / 2f)
                Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(Minecraft.getMinecraft().currentScreen, Minecraft.getMinecraft().gameSettings));
        }
    }};
    private final SmoothStepAnimation[] animations = new SmoothStepAnimation[4];
    public ParticleEngine engine = new ParticleEngine();
    SmoothStepAnimation barAnimation = new SmoothStepAnimation(125, 1, Direction.BACKWARDS);
    //ArrayList<ButtonPackage> buttonPackages = new ArrayList<>();
    //double barHeight = 30;
    //String[] buttonText = {"Solo", "Servers", "Alts", "Settings", "Language", "Quit"}; //Add a new string to automatically create a new button
    ArrayList<Change> changelog = new ArrayList<>();

    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe


    public MainMenu() {
    }

    public void onGuiClosed() {
    }


    public void initGui() {

        /*for (String string : buttonText) { //For every string, make a ButtonPackage which stores an invisible button, the text, and the text's animation
            buttonPackages.add(new ButtonPackage(
                    string,
                    new Rectangle(0, 0, 0, 0, new Color(0, 0, 0, 0)),
                    new SmoothStepAnimation(250, 10, Direction.BACKWARDS)
            ));
        }*/


        exitButton.setWidth(width * 0.18);
        exitButton.setX(width - exitButton.getWidth() - 10);
        exitButton.setHeight(height * 0.16);
        exitButton.setY(height - exitButton.getHeight() - 15);

        float circleWidth = (float) (width * 0.166);
        for (int i = 0; i < circleRectangles.length; i++) {
            circleRectangles[i].setWidth(circleWidth);
            circleRectangles[i].setHeight(circleWidth);
            circleRectangles[i].setX((width / 5f * (i + 1)) - circleRectangles[i].getWidth() / 2);
            circleRectangles[i].setY(10);
        }

        changelog.clear();
        changelog.add(new Change(ChangeType.NEW, "Added LongJump back"));
        changelog.add(new Change(ChangeType.NEW, "Added LowHop"));
        changelog.add(new Change(ChangeType.NEW, "Added 5 new Sword animations"));
        changelog.add(new Change(ChangeType.UPDATE, "Improved Penis Animation"));
        changelog.add(new Change(ChangeType.UPDATE, "Recoded TabGUI"));
        changelog.add(new Change(ChangeType.UPDATE, "Recoded TargetStrafe"));
        changelog.add(new Change(ChangeType.UPDATE, "Recoded AutoClicker"));
        changelog.add(new Change(ChangeType.UPDATE, "Recoded AimAssist"));
        changelog.add(new Change(ChangeType.FIX, "Fixed Shader Crash"));
        changelog.add(new Change(ChangeType.NEW, "Updated to 1.8.9"));
        changelog.add(new Change(ChangeType.NEW, "Updated OptiFine"));
        changelog.add(new Change(ChangeType.NEW, "Reach Module"));

        Collections.reverse(changelog);
        try {
            this.backgroundShader = new GLSLSandboxShader("/assets/minecraft/Fan/Shaders/noise.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Background Shader");
        }
        initTime = System.currentTimeMillis();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //initTime = System.currentTimeMillis();
        //mc.getTextureManager().bindTexture(new ResourceLocation(Fan.getBackgroundLocation()));
        //drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/backgroundshader.png"));
        drawModalRectWithCustomSizedTexture(1, 1, 1, 1, this.width, this.height, this.width, this.height);
        GlStateManager.enableAlpha();
        this.backgroundShader.useShader(this.width * 2, this.height * 2, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();
        // Unbind shader
        GL20.glUseProgram(0);

        //engine.render(mouseX, mouseY);

        if (exitButton.isInside(mouseX, mouseY)) {
            barAnimation.setDirection(Direction.FORWARDS);
        } else {
            barAnimation.setDirection(Direction.BACKWARDS);
        }


        Draw.color(0xff0000ff);
        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/ExitButton.png"));
        drawModalRectWithCustomSizedTexture((float) exitButton.getX(), (float) exitButton.getY(), 0, 0, (float) exitButton.getWidth(), (float) exitButton.getHeight(),
                (float) exitButton.getWidth(), (float) exitButton.getHeight());
        Draw.color(0xffffffff);
        drawModalRectWithCustomSizedTexture((float) ((float) exitButton.getX() - ((barAnimation.getOutput() * 10) / 1920 * width)), (float) ((float) exitButton.getY() - ((barAnimation.getOutput() * 10) / 1920 * width)), 0, 0, (float) exitButton.getWidth(), (float) exitButton.getHeight(),
                (float) exitButton.getWidth(), (float) exitButton.getHeight());


        for (int i = 0; i < circleRectangles.length; i++) {
            GlStateManager.pushMatrix();

            mc.getTextureManager().bindTexture(new ResourceLocation("Fan/CircleMM.png"));
            if (circleRectangles[i].isInside(mouseX, mouseY)) {
                if (animations[i] == null) {
                    animations[i] = new SmoothStepAnimation(250, 1, Direction.FORWARDS);
                } else {
                    animations[i].setDirection(Direction.FORWARDS);
                }
            } else {
                if (animations[i] != null) {
                    animations[i].setDirection(Direction.BACKWARDS);
                }
            }
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            drawModalRectWithCustomSizedTexture((float) circleRectangles[i].getX(), animations[i] == null ? (float) circleRectangles[i].getY() : (float) ((float) circleRectangles[i].getY() + (animations[i].getOutput() * 12f)), 0, 0, (float) circleRectangles[i].getWidth(),
                    (float) circleRectangles[i].getHeight(), (float) circleRectangles[i].getWidth(), (float) circleRectangles[i].getHeight());
            switch (i) {
                case 0:
                    mc.getTextureManager().bindTexture(new ResourceLocation("Fan/SingleplayerMM.png"));
                    break;
                case 1:
                    mc.getTextureManager().bindTexture(new ResourceLocation("Fan/MultiplayerMM.png"));
                    break;
                case 2:
                    mc.getTextureManager().bindTexture(new ResourceLocation("Fan/AltManagerMM.png"));
                    break;
                case 3:
                    mc.getTextureManager().bindTexture(new ResourceLocation("Fan/SettingsMM.png"));

                    /*if(animations[i] != null) {
                        GlStateManager.translate((float) (circleRectangles[i].getX() + circleRectangles[i].getWidth() / 2),  (float) (circleRectangles[i].getY() + circleRectangles[i].getWidth() / 2 + (animations[i].getOutput() * 20f)), 0);
                        GlStateManager.rotate((float) (animations[i].getOutput() * 360), 0, 0, 1);
                        GlStateManager.translate((float) -(circleRectangles[i].getX() + circleRectangles[i].getWidth() / 2),  (float) -(circleRectangles[i].getY() + circleRectangles[i].getWidth() / 2 + (animations[i].getOutput() * 20f)), 0);
                    }*/
                    break;
            }
            drawModalRectWithCustomSizedTexture((float) circleRectangles[i].getX(), animations[i] == null ? (float) circleRectangles[i].getY() : (float) ((float) circleRectangles[i].getY() + (animations[i].getOutput() * 20f)), 0, 0, (float) circleRectangles[i].getWidth(),
                    (float) circleRectangles[i].getHeight(), (float) circleRectangles[i].getWidth(), (float) circleRectangles[i].getHeight());
            GlStateManager.rotate(0, 0, 0, 0);
            GlStateManager.popMatrix();
        }

        /*int stringSize = 0;
        for (String string : buttonText) {
            stringSize += FontUtil.regular.getStringWidth(string);
        }
        int distBetweenStrings = (width - stringSize) / (buttonText.length * 2);
        int currentX = 0;
        for (ButtonPackage buttonPackage : buttonPackages) { //Sets button coords.
            buttonPackage.getButton().setX(currentX);
            buttonPackage.getButton().setWidth(distBetweenStrings * 2 + FontUtil.regular.getStringWidth(buttonPackage.getString()));
            buttonPackage.getButton().setY(height - barHeight);
            buttonPackage.getButton().setHeight(barHeight);

            currentX += (distBetweenStrings * 2 + FontUtil.regular.getStringWidth(buttonPackage.getString()));
        }

        boolean barHovered = false;
        for (ButtonPackage buttonPackage : buttonPackages) { //Checks if any button is hovered over in order to get barHovered
            if (buttonPackage.getButton().isInside(mouseX, mouseY)) {
                barHovered = true;
                buttonPackage.setHovered(true);
                buttonPackage.getAnimation().setDirection(Direction.FORWARDS);
            } else {
                buttonPackage.setHovered(false);
                buttonPackage.getAnimation().setDirection(Direction.BACKWARDS);
            }
        }

        if (barHovered) {
            barAnimation.setDuration(250);
            barAnimation.setDirection(Direction.FORWARDS);
        } else {
            barAnimation.setDirection(Direction.BACKWARDS);
        }
        barHeight = barAnimation != null ? 30 + (barAnimation.getOutput() * 10): 30; //If animation is null, set height to 30
        drawRect(0, height - barHeight, width, height, 0xffBB40C2); //Draw bar

        for (ButtonPackage buttonPackage : buttonPackages) {
            double[] coords = buttonPackage.getButton().getCenter(); //Gets the center coords of corresponding rectangle
            MinecraftFontRenderer font = buttonPackage.isHovered() ? FontUtil.expandedfont : FontUtil.regular; //If corresponding rectangle is hovered over, change font
            font.drawCenteredStringWithShadow(buttonPackage.getString(), (float) coords[0], (float) (height - 24 - buttonPackage.getAnimation().getOutput()), buttonPackage.isHovered() ? 0xffff87c7 : -1); //Draw string
        }*/

        //mc.getTextureManager().bindTexture(new ResourceLocation("Fan/image.png"));
        //drawModalRectWithCustomSizedTexture(width / 2 - 1920 / 4, height / 2 - 1080 / 4 + 19, 0, 0, 1920 / 2, 1080 / 2, 1920 / 2, 1080 / 2);

        int changelogSize = FontUtil.cleanlarge.getHeight() + ((FontUtil.cleanSmall.getHeight() + 3) * changelog.size());
        float longestString = (float) FontUtil.cleanSmall.getStringWidth("Developed by Cedo, Foggy, Destiny, and JosephWorks") + 4;
        for (Change change : changelog)
            longestString = (float) Math.max(FontUtil.cleanSmall.getStringWidth(change.getDescription()) + 20, longestString);

        drawRect(1, height - ((FontUtil.cleanlarge.getHeight() + changelogSize) + 18 + FontUtil.cleanlarge.getHeight()), longestString + 4,
                height - 1, 0xaa000000);

        FontUtil.cleanlarge.drawString("Changes", 6, height - (FontUtil.cleanlarge.getHeight() + changelogSize + (18 + FontUtil.cleanSmall.getHeight())), Color.white.getRGB());
        FontUtil.cleanSmall.drawString("Developed by cedo, JosephWorks, Foggy, and Destiny", 4, height - (18 + FontUtil.cleanSmall.getHeight()), Color.white.getRGB());
        FontUtil.cleanSmall.drawString("© " + Year.now() + " Fan Development Team", 4, height - (10 + FontUtil.cleanSmall.getHeight()), Color.white.getRGB());
        FontUtil.cleanSmall.drawString("Version: " + Fan.version, 4, height - (2 + FontUtil.cleanSmall.getHeight()), Color.white.getRGB());

        int changeCount = 0;
        for (Change change : changelog) {
            float y = FontUtil.cleanlarge.getHeight() + ((FontUtil.cleanSmall.getHeight() + 3) * changeCount) + (18 + FontUtil.cleanSmall.getHeight());
            drawRect(12, height - y, 16, height - (4 + y), change.getType().getColor());
            FontUtil.cleanSmall.drawString(change.getDescription(), 20, height - (y + 4.5f), Color.white.getRGB());
            changeCount++;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        exitButton.mouseClicked(mouseX, mouseY, mouseButton);
        for (Rectangle circle : circleRectangles) {
            circle.mouseClicked(mouseX, mouseY, mouseButton);
        }

        /*for (ButtonPackage buttonPackage : buttonPackages) {
            if (buttonPackage.isHovered()) {
                switch (buttonPackage.getString()) { //Gets corresponding string in order to decide which screen to display
                    case "Solo":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;

                    case "Servers":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;

                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;

                    case "Language":
                        mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                        break;

                    case "Quit":
                        mc.shutdown();
                        break;

                    case "Alts":
                        mc.displayGuiScreen(new BetterAltManager(this));
                        break;
                }
            }
        }*/

    }


    /*private static class ButtonPackage {
        String string;
        Rectangle button;
        SmoothStepAnimation animation;
        boolean hovered;

        public ButtonPackage(String string, Rectangle button, SmoothStepAnimation animation) {
            this.string = string;
            this.button = button;
            this.animation = animation;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Rectangle getButton() {
            return button;
        }

        public void setButton(Rectangle button) {
            this.button = button;
        }

        public Animation getAnimation() {
            return animation;
        }

        public void setAnimation(SmoothStepAnimation animation) {
            this.animation = animation;
        }

        public boolean isHovered() {
            return hovered;
        }

        public void setHovered(boolean hovered) {
            this.hovered = hovered;
        }
    }*/

}
