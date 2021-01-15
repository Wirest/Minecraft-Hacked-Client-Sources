// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.account;

import me.aristhena.client.account.LoginThread;
import java.util.Random;
import me.aristhena.client.gui.account.screen.ScreenEditAccount;
import me.aristhena.client.gui.account.screen.ScreenAddAccount;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import me.aristhena.utils.ClientUtils;
import java.util.Iterator;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import me.aristhena.client.account.AccountManager;
import java.util.ArrayList;
import me.aristhena.client.gui.account.component.Button;
import me.aristhena.client.account.Alt;
import me.aristhena.client.gui.account.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import me.aristhena.client.gui.account.component.AltButton;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class AccountScreen extends GuiScreen
{
    private static AccountScreen instance;
    private List<AltButton> buttonList;
    private DynamicTexture viewportTexture;
    private static final ResourceLocation[] titlePanoramaPaths;
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
    private int scrollOffset;
    private int scrollVelocity;
    private int buttonsPerColumn;
    private int prevWidth;
    private int prevHeight;
    public AltButton selectedButton;
    public Screen currentScreen;
    public String info;
    public Alt toRemove;
    public Alt lastAlt;
    Button addAltButton;
    Button delAltButton;
    Button editAltButton;
    Button randomAltButton;
    Button lastAltButton;
    
    static {
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/acm/panorama_0.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_1.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_2.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_3.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_4.png"), new ResourceLocation("textures/gui/title/background/acm/panorama_5.png") };
    }
    
    public AccountScreen() {
        this.buttonList = new ArrayList<AltButton>();
        this.scrollOffset = 0;
        this.scrollVelocity = 0;
        this.buttonsPerColumn = 5;
        this.info = "";
        this.toRemove = null;
        this.info = "§bWaiting...";
    }
    
    @Override
    public void initGui() {
        AccountScreen.instance = this;
        AccountManager.load();
        this.scrollOffset = 0;
        this.scrollVelocity = 0;
        this.viewportTexture = new DynamicTexture(256, 256);
        this.panoramaLoc = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.addAltButton = new AddButton();
        this.delAltButton = new DelButton();
        this.editAltButton = new EditButton();
        this.randomAltButton = new RandomButton();
        this.lastAltButton = new LastButton();
        this.addAltButtons();
        this.currentScreen = null;
        AccountManager.save();
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKey() == 1 && this.currentScreen != null) {
            this.currentScreen = null;
        }
        else if (Keyboard.getEventKeyState()) {
            if (this.currentScreen != null) {
                this.currentScreen.onKeyPress(Keyboard.getEventCharacter(), Keyboard.getEventKey());
            }
            super.handleKeyboardInput();
        }
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int mouseButton) throws IOException {
        if (this.currentScreen != null) {
            this.currentScreen.onClick(x, y, mouseButton);
            return;
        }
        for (final AltButton button : this.buttonList) {
            if (button.isOver()) {
                if (this.selectedButton != null && this.selectedButton.equals(button)) {
                    button.onClick(mouseButton);
                }
                else {
                    this.selectedButton = button;
                }
            }
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
        if (this.lastAltButton.isOver()) {
            this.lastAltButton.onClick(mouseButton);
        }
        super.mouseClicked(x, y, mouseButton);
    }
    
    public void drawCenteredString(String text, float scale, int xOffset, int yOffset) {
        boolean tooLong = false;
        while ((float)ClientUtils.clientFont().getStringWidth(text) * scale > (float)this.width) {
            text = text.substring(0, text.length() - 1);
            tooLong = true;
        }
        if (tooLong) {
            text = text.substring(0, text.length() - 4);
            text = String.valueOf(text) + "...";
        }
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        int strWidth = ClientUtils.clientFont().getStringWidth(text);
        strWidth = (int)((float)strWidth * scale);
        int x = this.width / 2 - strWidth / 2 + xOffset;
        int y = 4 + yOffset;
        x = (int)((float)x / scale);
        y = (int)((float)y / scale);
        ClientUtils.clientFont().drawStringWithShadow(text, x, y, -1);
        GL11.glScalef((float)(1.0f / scale), (float)(1.0f / scale), (float)(1.0f / scale));
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float par3) {
        GL11.glDisable(3008);
        this.renderSkybox(x, y, par3);
        GL11.glEnable(3008);
        final int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        final int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        if (this.buttonList.size() > 0) {
            if (this.prevWidth != this.width || this.prevHeight != this.height) {
                this.initGui();
                this.prevWidth = this.width;
                this.prevHeight = this.height;
            }
            if (Mouse.hasWheel() && this.buttonList.get(this.buttonList.size() - 1).getX2() - this.buttonList.get(0).getX1() > this.width && this.currentScreen == null) {
                final int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    this.scrollVelocity += 8;
                }
                else if (wheel > 0) {
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
            }
            else if (this.scrollVelocity > 0) {
                --this.scrollVelocity;
            }
            this.drawAltButtons(mouseX, mouseY);
        }
        this.drawCenteredString(this.info, 1.2f, 0, 2);
        this.drawCenteredString("§b" + AccountManager.accountList.size() + " Alt" + ((AccountManager.accountList.size() != 1) ? "s" : ""), 1.0f, 0, 14);
        this.addAltButton.draw(mouseX, mouseY);
        this.delAltButton.draw(mouseX, mouseY);
        this.editAltButton.draw(mouseX, mouseY);
        this.randomAltButton.draw(mouseX, mouseY);
        this.lastAltButton.draw(mouseX, mouseY);
        if (this.currentScreen != null) {
            this.currentScreen.draw(mouseX, mouseY);
        }
    }
    
    public void addAltButtons() {
        this.buttonList.clear();
        for (final Alt alt : AccountManager.accountList) {
            final AltButton altButton = new AltButton((alt.getUsername().length() > 0) ? alt.getUsername() : alt.getEmail(), 20, 140, 40, 66, -15921907, -16777216, alt);
            this.buttonList.add(altButton);
        }
        AccountManager.save();
    }
    
/*    public void drawAltButtons(final int mouseX, final int mouseY) {
        this.buttonsPerColumn = (this.height - 40) / 36 - 1;
        int index = 0;
        int x = 20;
        int y = 40;
        if (this.toRemove != null && AccountManager.accountList.contains(this.toRemove)) {
            AccountManager.removeAlt(this.toRemove);
            AccountManager.save();
            this.addAltButtons();
            this.toRemove = null;
        }
        for (final AltButton altButton : this.buttonList) {
            ++index;
            final Alt alt = altButton.getAlt();
            altButton.setText((alt.getUsername().length() < 1) ? alt.getEmail() : alt.getUsername());
            altButton.setX1(this.scrollOffset + x);
            altButton.setX2(this.scrollOffset + x + 120);
            altButton.setY1(y);
            altButton.setY2(y + 26);
            altButton.draw(mouseX, mouseY);
            y += 36;
            x = 20 + 130 * (int)(index / this.buttonsPerColumn + 0.5);
            if (index % this.buttonsPerColumn == 0) {
                y = 40;
            }
        }
    }*/
    
    public void drawAltButtons(int mouseX, int mouseY) {
        this.buttonsPerColumn = (this.height - 40) / 36 - 1;
        int index = 0;
        int x = 20;
        int y = 40;
        if (this.toRemove != null && AccountManager.accountList.contains(this.toRemove)) {
            AccountManager.removeAlt(this.toRemove);
            AccountManager.save();
            this.addAltButtons();
            this.toRemove = null;
        }
        for (final AltButton altButton : this.buttonList) {
            ++index;
            final Alt alt = altButton.getAlt();
            altButton.setText((alt.getUsername().length() < 1) ? alt.getEmail() : alt.getUsername());
            altButton.setX1(this.scrollOffset + x);
            altButton.setX2(this.scrollOffset + x + 120);
            altButton.setY1(y);
            altButton.setY2(y + 26);
            altButton.draw(mouseX, mouseY);
            y += 36;
            x = 20 + 130 * (int)(index / this.buttonsPerColumn + 0.5);
            if (index % this.buttonsPerColumn == 0) {
                y = 40;
            }
        }
    }
    
    public static AccountScreen getInstance() {
        return AccountScreen.instance;
    }
    
    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
        if (this.currentScreen != null) {
            this.currentScreen.update();
        }
    }
    
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        final byte var6 = 8;
        for (int var7 = 0; var7 < var6 * var6; ++var7) {
            GlStateManager.pushMatrix();
            final float var8 = (var7 % var6 / var6 - 0.5f) / 64.0f;
            final float var9 = (var7 / var6 / var6 - 0.5f) / 64.0f;
            final float var10 = 0.0f;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int var11 = 0; var11 < 6; ++var11) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (var11 == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (var11 == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(AccountScreen.titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.setColorRGBA_I(16777215, 255 / (var7 + 1));
                final float var12 = 0.0f;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, 0.0f + var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, -1.0, 1.0, 1.0f - var12, 0.0f + var12);
                var5.addVertexWithUV(1.0, 1.0, 1.0, 1.0f - var12, 1.0f - var12);
                var5.addVertexWithUV(-1.0, 1.0, 1.0, 0.0f + var12, 1.0f - var12);
                var4.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.panoramaLoc);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator var2 = Tessellator.getInstance();
        final WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        final byte var4 = 3;
        for (int var5 = 0; var5 < var4; ++var5) {
            var3.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f / (var5 + 1));
            final int var6 = this.width;
            final int var7 = this.height;
            final float var8 = (var5 - var4 / 2) / 256.0f;
            var3.addVertexWithUV(var6, var7, this.zLevel, 0.0f + var8, 1.0);
            var3.addVertexWithUV(var6, 0.0, this.zLevel, 1.0f + var8, 1.0);
            var3.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f + var8, 0.0);
            var3.addVertexWithUV(0.0, var7, this.zLevel, 0.0f + var8, 0.0);
        }
        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final Tessellator var4 = Tessellator.getInstance();
        final WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        final float var6 = (this.width > this.height) ? (120.0f / this.width) : (120.0f / this.height);
        final float var7 = this.height * var6 / 256.0f;
        final float var8 = this.width * var6 / 256.0f;
        var5.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
        final int var9 = this.width;
        final int var10 = this.height;
        var5.addVertexWithUV(0.0, var10, this.zLevel, 0.5f - var7, 0.5f + var8);
        var5.addVertexWithUV(var9, var10, this.zLevel, 0.5f - var7, 0.5f - var8);
        var5.addVertexWithUV(var9, 0.0, this.zLevel, 0.5f + var7, 0.5f - var8);
        var5.addVertexWithUV(0.0, 0.0, this.zLevel, 0.5f + var7, 0.5f + var8);
        var4.draw();
    }
    
    private class AddButton extends Button
    {
        public AddButton() {
            super("Add Alt", AccountScreen.this.width / 6 - 40, AccountScreen.this.width / 6 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }
        
        @Override
        public void onClick(final int button) {
            AccountScreen.this.currentScreen = new ScreenAddAccount();
        }
    }
    
    private class DelButton extends Button
    {
        public DelButton() {
            super("Remove Alt", AccountScreen.this.width / 6 * 3 - 40, AccountScreen.this.width / 6 * 3 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }
        
        @Override
        public void onClick(final int button) {
            if (AccountScreen.this.selectedButton != null) {
                AccountManager.removeAlt(AccountScreen.this.selectedButton.getAlt());
                AccountManager.save();
                AccountScreen.this.initGui();
                AccountScreen.this.info = "§cRemoved Account";
            }
        }
    }
    
    private class EditButton extends Button
    {
        public EditButton() {
            super("Edit Alt", AccountScreen.this.width / 6 * 2 - 40, AccountScreen.this.width / 6 * 2 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }
        
        @Override
        public void onClick(final int button) {
            if (AccountScreen.this.selectedButton != null) {
                AccountScreen.this.currentScreen = new ScreenEditAccount(AccountScreen.this.selectedButton.getAlt());
            }
        }
    }
    
    private class RandomButton extends Button
    {
        public RandomButton() {
            super("Random Alt", AccountScreen.this.width / 6 * 4 - 40, AccountScreen.this.width / 6 * 4 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }
        
        @Override
        public void onClick(final int button) {
            if (AccountManager.accountList.size() < 1) {
                return;
            }
            final Random random = new Random();
            int randomInt;
            if (AccountManager.accountList.size() == 1) {
                randomInt = 0;
            }
            else {
                randomInt = random.nextInt(AccountManager.accountList.size() - 1);
            }
            final Alt alt = AccountManager.accountList.get(randomInt);
            final LoginThread thread = new LoginThread(alt);
            thread.start();
        }
    }
    
    private class LastButton extends Button
    {
        public LastButton() {
            super("Last Alt", AccountScreen.this.width / 6 * 5 - 40, AccountScreen.this.width / 6 * 5 + 40, AccountScreen.this.height - 20 - 13, AccountScreen.this.height - 20 + 13, -15921907, -16777216);
            this.setBorderColor(1280595028);
        }
        
        @Override
        public void onClick(final int button) {
            if (AccountScreen.this.lastAlt == null) {
                AccountScreen.this.info = "§cLast Alt Empty";
            }
            else {
                final LoginThread thread = new LoginThread(AccountScreen.this.lastAlt);
                thread.start();
            }
        }
    }
}
