/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.WorldRenderer
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.Project
 */
package me.aristhena.lucid.ui.alt.screens.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.aristhena.lucid.Lucid;
import me.aristhena.lucid.management.account.AccountManager;
import me.aristhena.lucid.management.account.Alt;
import me.aristhena.lucid.management.account.LoginThread;
import me.aristhena.lucid.ui.alt.screens.account.component.AltButton;
import me.aristhena.lucid.ui.alt.screens.account.screen.Screen;
import me.aristhena.lucid.ui.alt.screens.account.screen.ScreenAddAccount;
import me.aristhena.lucid.ui.alt.screens.account.screen.ScreenEditAccount;
import me.aristhena.lucid.ui.alt.screens.component.Button;
import me.aristhena.lucid.util.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class AccountScreen
extends GuiScreen {
    private List<AltButton> buttonList = new ArrayList<AltButton>();
    private DynamicTexture viewportTexture;
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/acm/panorama_0.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_1.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_2.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_3.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_4.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_5.png")};
    private ResourceLocation panoramaLoc;
    private float panoramaTimer;
    private static final int GENERAL_BUTTON_WIDTH = 80;
    private static final int GENERAL_BUTTON_HEIGHT = 26;
    private static final int ALT_BUTTON_WIDTH = 120;
    private static final int ALT_BUTTON_HEIGHT = 26;
    private static final int WIDTH_BUFFER = 10;
    private static final int HEIGHT_BUFFER = 10;
    private static final int X_BASE = 20;
    private static final int Y_BASE = 40;
    private int scrollOffset = 0;
    private int scrollVelocity = 0;
    private int buttonsPerColumn = 5;
    private int prevWidth;
    private int prevHeight;
    private AltButton selectedButton;
    public Screen currentScreen;
    public String info = "\u00a7bWaiting...";
    public Alt toRemove = null;
    Button addAltButton;
    Button delAltButton;
    Button editAltButton;
    Button randomAltButton;

    public void initGui() {
        AccountManager.loadAccounts();
        this.scrollOffset = 0;
        this.scrollVelocity = 0;
        this.viewportTexture = new DynamicTexture(256, 256);
        this.panoramaLoc = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.addAltButton = new AddButton();
        this.delAltButton = new DelButton();
        this.editAltButton = new EditButton();
        this.randomAltButton = new RandomButton();
        this.addAltButtons();
        this.currentScreen = null;
        AccountManager.saveAccounts();
    }

    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKey() == 1 && this.currentScreen != null) {
            this.currentScreen = null;
        } else if (Keyboard.getEventKeyState()) {
            if (this.currentScreen != null) {
                this.currentScreen.onKeyPress(Keyboard.getEventCharacter(), Keyboard.getEventKey());
            }
            super.handleKeyboardInput();
        }
    }

    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        if (this.currentScreen != null) {
            this.currentScreen.onClick(x, y, mouseButton);
            return;
        }
        for (AltButton button : this.buttonList) {
            if (!button.isOver()) continue;
            if (this.selectedButton != null && this.selectedButton.equals(button)) {
                button.onClick(mouseButton);
                continue;
            }
            this.selectedButton = button;
        }
        if (this.addAltButton.isOver()) {
            this.addAltButton.onClick(mouseButton);
        }
        if (this.editAltButton.isOver()) {
            this.editAltButton.onClick(mouseButton);
        }
        if (this.delAltButton.isOver()) {
            this.delAltButton.onClick(mouseButton);
        }
        if (this.randomAltButton.isOver()) {
            this.randomAltButton.onClick(mouseButton);
        }
        super.mouseClicked(x, y, mouseButton);
    }

    public void drawCenteredString(String text, float scale, int xOffset, int yOffset) {
        boolean tooLong = false;
        while ((float)Lucid.font.getStringWidth(text) * scale > (float)this.width) {
            text = text.substring(0, text.length() - 1);
            tooLong = true;
        }
        if (tooLong) {
            text = text.substring(0, text.length() - 4);
            text = String.valueOf(text) + "...";
        }
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        int strWidth = Lucid.font.getStringWidth(text);
        strWidth = (int)((float)strWidth * scale);
        int x = this.width / 2 - strWidth / 2 + xOffset;
        int y = 4 + yOffset;
        x = (int)((float)x / scale);
        y = (int)((float)y / scale);
        Lucid.font.drawStringWithShadow(text, x, y, -1);
        GL11.glScalef((float)(1.0f / scale), (float)(1.0f / scale), (float)(1.0f / scale));
    }

    public void drawScreen(int x, int y, float par3) {
        GL11.glDisable((int)3008);
        this.renderSkybox(x, y, par3);
        GL11.glEnable((int)3008);
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        if (this.buttonList.size() > 0) {
            if (this.prevWidth != this.width || this.prevHeight != this.height) {
                this.initGui();
                this.prevWidth = this.width;
                this.prevHeight = this.height;
            }
            if (Mouse.hasWheel() && this.buttonList.get(this.buttonList.size() - 1).getX2() - this.buttonList.get(0).getX1() > this.width && this.currentScreen == null) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    this.scrollVelocity += 8;
                } else if (wheel > 0) {
                    this.scrollVelocity -= 8;
                }
                if (this.scrollVelocity > 40) {
                    this.scrollVelocity = 40;
                }
                if (this.scrollVelocity < -40) {
                    this.scrollVelocity = -40;
                }
                this.scrollOffset -= this.scrollVelocity;
                if (this.scrollOffset > 0 - this.scrollVelocity) {
                    this.scrollOffset = 0 - this.scrollVelocity;
                }
                if (this.buttonList.get(this.buttonList.size() - 1).getX2() - this.width + 20 < 0) {
                    this.scrollOffset = (this.buttonList.get(this.buttonList.size() - 1).getX2() - this.width + 20 - this.scrollOffset) * -1;
                }
            }
            if (this.scrollVelocity < 0) {
                ++this.scrollVelocity;
            } else if (this.scrollVelocity > 0) {
                --this.scrollVelocity;
            }
            this.drawAltButtons(mouseX, mouseY);
        }
        this.drawCenteredString(this.info, 1.2f, 0, 2);
        this.drawCenteredString("\u00a7b" + AccountManager.accountList.size() + " Alts", 1.0f, 0, 14);
        this.addAltButton.draw(mouseX, mouseY);
        this.delAltButton.draw(mouseX, mouseY);
        this.editAltButton.draw(mouseX, mouseY);
        this.randomAltButton.draw(mouseX, mouseY);
        if (this.currentScreen != null) {
            this.currentScreen.draw(mouseX, mouseY);
        }
    }

    public void addAltButtons() {
        this.buttonList.clear();
        for (Alt alt : AccountManager.accountList) {
            AltButton altButton = new AltButton(alt.name != "" ? alt.name : alt.email, 20, 140, 40, 66, -15921907, -16777216, alt);
            this.buttonList.add(altButton);
        }
        AccountManager.saveAccounts();
    }

    public void drawAltButtons(int mouseX, int mouseY) {
        this.buttonsPerColumn = (this.height - 40) / 36 - 1;
        int index = 0;
        int x = 20;
        int y = 40;
        if (this.toRemove != null && AccountManager.accountList.contains(this.toRemove)) {
            AccountManager.removeAlt(this.toRemove);
            AccountManager.saveAccounts();
            Lucid.accountScreen.addAltButtons();
            this.toRemove = null;
        }
        for (AltButton altButton : this.buttonList) {
            ++index;
            Alt alt = altButton.getAlt();
            if (this.selectedButton != null && this.selectedButton.equals(altButton)) {
                altButton.setBorderColor(-14540254);
            } else {
                altButton.setBorderColor(1280595028);
            }
            altButton.setText(alt.name != "" ? alt.name : alt.email);
            altButton.setX1(this.scrollOffset + x);
            altButton.setX2(this.scrollOffset + x + 120);
            altButton.setY1(y);
            altButton.setY2(y + 26);
            altButton.draw(mouseX, mouseY);
            y += 36;
            x = 20 + 130 * (int)((double)(index / this.buttonsPerColumn) + 0.5);
            if (index % this.buttonsPerColumn != 0) continue;
            y = 40;
        }
    }

    public void updateScreen() {
        this.panoramaTimer += 1.0f;
        if (this.currentScreen != null) {
            this.currentScreen.update();
        }
    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode((int)5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective((float)120.0f, (float)1.0f, (float)0.05f, (float)10.0f);
        GlStateManager.matrixMode((int)5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        int var6 = 8;
        int var7 = 0;
        while (var7 < var6 * var6) {
            GlStateManager.pushMatrix();
            float var8 = ((float)(var7 % var6) / (float)var6 - 0.5f) / 64.0f;
            float var9 = ((float)(var7 / var6) / (float)var6 - 0.5f) / 64.0f;
            float var10 = 0.0f;
            GlStateManager.translate((float)var8, (float)var9, (float)var10);
            GlStateManager.rotate((float)(MathHelper.sin((float)((this.panoramaTimer + p_73970_3_) / 400.0f)) * 25.0f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)((- this.panoramaTimer + p_73970_3_) * 0.1f), (float)0.0f, (float)1.0f, (float)0.0f);
            int var11 = 0;
            while (var11 < 6) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate((float)-90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate((float)-90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                }
                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.setColorRGBA_I(16777215, 255 / (var7 + 1));
                float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, (double)(0.0f + var12), (double)(0.0f + var12));
                var5.addVertexWithUV(1.0, -1.0, 1.0, (double)(1.0f - var12), (double)(0.0f + var12));
                var5.addVertexWithUV(1.0, 1.0, 1.0, (double)(1.0f - var12), (double)(1.0f - var12));
                var5.addVertexWithUV(-1.0, 1.0, 1.0, (double)(0.0f + var12), (double)(1.0f - var12));
                var4.draw();
                GlStateManager.popMatrix();
                ++var11;
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
            ++var7;
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
        GlStateManager.matrixMode((int)5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode((int)5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.panoramaLoc);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glCopyTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)0, (int)0, (int)256, (int)256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        int var4 = 3;
        int var5 = 0;
        while (var5 < var4) {
            var3.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f / (float)(var5 + 1));
            int var6 = this.width;
            int var7 = this.height;
            float var8 = (float)(var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV((double)var6, (double)var7, (double)this.zLevel, (double)(0.0f + var8), 1.0);
            var3.addVertexWithUV((double)var6, 0.0, (double)this.zLevel, (double)(1.0f + var8), 1.0);
            var3.addVertexWithUV(0.0, 0.0, (double)this.zLevel, (double)(1.0f + var8), 0.0);
            var3.addVertexWithUV(0.0, (double)var7, (double)this.zLevel, (double)(0.0f + var8), 0.0);
            ++var5;
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport((int)0, (int)0, (int)256, (int)256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport((int)0, (int)0, (int)this.mc.displayWidth, (int)this.mc.displayHeight);
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        float var6 = this.width > this.height ? 120.0f / (float)this.width : 120.0f / (float)this.height;
        float var7 = (float)this.height * var6 / 256.0f;
        float var8 = (float)this.width * var6 / 256.0f;
        var5.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
        int var9 = this.width;
        int var10 = this.height;
        var5.addVertexWithUV(0.0, (double)var10, (double)this.zLevel, (double)(0.5f - var7), (double)(0.5f + var8));
        var5.addVertexWithUV((double)var9, (double)var10, (double)this.zLevel, (double)(0.5f - var7), (double)(0.5f - var8));
        var5.addVertexWithUV((double)var9, 0.0, (double)this.zLevel, (double)(0.5f + var7), (double)(0.5f - var8));
        var5.addVertexWithUV(0.0, 0.0, (double)this.zLevel, (double)(0.5f + var7), (double)(0.5f + var8));
        var4.draw();
    }

    private class AddButton
    extends Button {
        public AddButton() {
            super("Add Alt", AccountScreen.this.width / 5 - 40, AccountScreen.this.width / 5 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }

        @Override
        public void onClick(int button) {
            AccountScreen.this.currentScreen = new ScreenAddAccount();
        }
    }

    private class DelButton
    extends Button {
        public DelButton() {
            super("Remove Alt", AccountScreen.this.width / 5 * 3 - 40, AccountScreen.this.width / 5 * 3 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }

        @Override
        public void onClick(int button) {
            if (AccountScreen.this.selectedButton != null) {
                AccountManager.removeAlt(AccountScreen.this.selectedButton.getAlt());
                AccountManager.saveAccounts();
                Lucid.accountScreen.initGui();
                Lucid.accountScreen.info = "\u00a7cRemoved Account";
            }
        }
    }

    private class EditButton
    extends Button {
        public EditButton() {
            super("Edit Alt", AccountScreen.this.width / 5 * 2 - 40, AccountScreen.this.width / 5 * 2 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }

        @Override
        public void onClick(int button) {
            if (AccountScreen.this.selectedButton != null) {
                AccountScreen.this.currentScreen = new ScreenEditAccount(AccountScreen.this.selectedButton.getAlt());
            }
        }
    }

    private class RandomButton
    extends Button {
        public RandomButton() {
            super("Random Alt", AccountScreen.this.width / 5 * 4 - 40, AccountScreen.this.width / 5 * 4 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }

        @Override
        public void onClick(int button) {
            if (AccountManager.accountList.size() < 1) {
                return;
            }
            Random random = new Random();
            int randomInt = random.nextInt(AccountManager.accountList.size() - 1);
            Alt alt = AccountManager.accountList.get(randomInt);
            LoginThread thread = new LoginThread(alt.email, alt.pass);
            thread.start();
        }
    }

}

