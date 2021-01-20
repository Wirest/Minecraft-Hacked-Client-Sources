package cedo.altmanager;

import cedo.Fan;
import cedo.util.Timer;
import cedo.util.fake.FakeGameProfile;
import cedo.util.fake.FakeNetHandlerPlayClient;
import cedo.util.fake.FakeWorld;
import cedo.util.font.FontUtil;
import cedo.util.render.EntityUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.*;

public class BetterAltManager extends GuiScreen {
    public static File altsFile = new File(Fan.fanDir, "Alts.json");
    public static List<Alt> alts = new ArrayList<Alt>();
    public static FakeGameProfile profile;

    static {
        altsFile.getParentFile().mkdirs();
        try {
            byte[] content = Files.readAllBytes(altsFile.toPath());
            alts = new ArrayList<Alt>(
                    Arrays.asList(
                            new Gson().fromJson(new String(content), Alt[].class)
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final GuiScreen back;
    public Timer timer = new Timer();
    public Alt selectedAlt;

    WorldClient world;
    EntityLivingBase randMob;
    EntityPlayerSP player;
    float scroll = 0;
    float scrollSpeed = 5; //Adjustable, lower # = faster scrolling
    private PasswordField password;
    private PasswordField username;

    public BetterAltManager(GuiScreen back) {
        this.back = back;
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        int boxWidth = (int) Math.max(120, Math.min(width / 2f - 100, 200));
        this.username = new PasswordField("Email / Combo / Altening", 0, 0, 60, boxWidth, 20);
        username.setMaxStringLength(128);
        this.password = new PasswordField("Password", 0, 0, 100, boxWidth, 20);
        password.setMaxStringLength(128);
        Keyboard.enableRepeatEvents(true);

        altsFile.getParentFile().mkdirs();
        try {
            byte[] content = Files.readAllBytes(altsFile.toPath());
            alts = new ArrayList<Alt>(
                    Arrays.asList(
                            new Gson().fromJson(new String(content), Alt[].class)
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (Mouse.hasWheel()) {
            scroll += Mouse.getDWheel() / scrollSpeed;
        }
        if (scroll < 0)
            scroll = 0;
        if (timer.hasTimeElapsed(15 * 1000, true)) {
            new Thread(() -> {
                try {
                    Files.write(altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(alts.toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Gui.drawRect(0, 0, this.width, this.height, -1);
        Gui.drawRect(0, 0, this.width, this.height, Color.darkGray.getRGB());
        GlStateManager.enableBlend();
        float bottomHeight = 258 / 2f;
        float rightWidth = 846 / 2;
        int moveMe = Math.max(100, width - 910);
        float rightX = this.width - rightWidth + moveMe;
        int boxWidth = (int) Math.max(120, Math.min(width / 2f - 100, 200));
        int moveRight = (-boxWidth + 200) / 2;

        username.xPosition = (int) ((width - rightX) / 2f + rightX - username.getWidth() / 2f) + moveRight;
        password.xPosition = (int) ((width - rightX) / 2f + rightX - username.getWidth() / 2f) + moveRight;

        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/altmanagerrightside.png"));
        drawModalRectWithCustomSizedTexture(this.width - rightWidth + moveMe - boxWidth + 200, 0, 0, 0, rightWidth, this.height, rightWidth, this.height);


        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/altmanagerbottom.png"));
        drawModalRectWithCustomSizedTexture(0, this.height - bottomHeight + 40, 0, 0, this.width, bottomHeight, this.width, 258 / 2);//altmanagerrightside.png   altmanagerbottom.png

        username.drawTextBox();
        password.drawPasswordBox();


        //login button
        FontUtil.clean.drawString("Login", username.xPosition + 4, 142, 0xffaaaaaa);
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawRect(username.xPosition, 160, username.xPosition + 100, 162, 0xffaaaaaa);
        GlStateManager.disableAlpha();

        //back button
        FontUtil.clean.drawString("Back", 14, 10, 0xffaaaaaa);
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawRect(10, 30, 10 + 55, 32, 0xffaaaaaa);
        GlStateManager.disableAlpha();

        //delete button
        FontUtil.clean.drawString("Delete Alt", 79, 10, 0xffaaaaaa);
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawRect(75, 30, 75 + FontUtil.clean.getStringWidth("Delete Alt") + 14, 32, 0xffaaaaaa);
        GlStateManager.disableAlpha();

        if (selectedAlt != null) {
            GlStateManager.pushMatrix();
            //player.gameProfile = profile;
            /*try {
                if (this.player == null || this.player.worldObj == null) {
                    this.init();
                }
                if (mc.getRenderManager().worldObj == null || mc.getRenderManager().livingPlayer == null) {
                    mc.getRenderManager().cacheActiveRenderInfo(this.world, mc.fontRendererObj, this.player, this.player, mc.gameSettings, 0.0f);
                }
                if (this.world != null && this.player != null) {
                    mc.thePlayer = this.player;
                    mc.theWorld = this.world;
                    final int distanceToSide = (mc.currentScreen.width / 2 - 98) / 2;
                    final float targetHeight = 73;
                    GlStateManager.color(1, 1, 1, 1);
                    EntityUtils.drawEntityOnScreen(width / 8, (int) (height / 2 + targetHeight / 2f), targetHeight, width / 8f - mouseX, height / 2f - mouseY - 120 + targetHeight / 2f, this.player);

                }
            } catch (Throwable e) {
                e.printStackTrace();
                this.player = null;
                this.randMob = null;
                this.world = null;
            }*/
            GlStateManager.popMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            mc.thePlayer = null;
            mc.theWorld = null;
            List<String> accountDetails = Lists.asList(selectedAlt.username, selectedAlt.email, selectedAlt.namemc.toArray(new String[0]));

            int count = 0;
            for (String s : accountDetails) {
                if (s != null) {
                    FontUtil.clean.drawString(s, 16, 64 + FontUtil.cleanlarge.getHeight() * count, 0xffaaaaaa);
                    GlStateManager.color(1, 1, 1, 1);
                    count++;
                }
            }
        }

        List<Alt> displayAlts = new ArrayList<Alt>(alts);
        Collections.reverse(displayAlts);
        int count = 0;
        float size = 55, margin = 10;
        for (Alt alt : displayAlts) {
            String username = alt.username;
            String uuid = alt.uuid;
            float left = 10 + count * (size + margin) - scroll, top = height - 65;
            boolean hovered = isHovered(left, top, left + size, top + size, mouseX, mouseY);
            //drawRect(left, top, left + size, top + size, -1);

            if (username != null) {

                ThreadDownloadImageData thread = AbstractClientPlayer.getDownloadImageHead(AbstractClientPlayer.getLocationSkin(username == null ? "Steve" : username), uuid == null ? "" : uuid);
                if (alt.skinChecks < 5) {
                    try {
                        thread.loadTexture(mc.getResourceManager());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    alt.skinChecks++;
                }
                alt.checkedSkin = thread.imageFound != null && thread.imageFound.booleanValue();
            }
            GL11.glColor4f(!hovered ? 1 : 0.7f, !hovered ? 1 : 0.7f, !hovered ? 1 : 0.7f, 1);

            mc.getTextureManager().bindTexture(username == null ? new ResourceLocation("Fan/steve.png") : AbstractClientPlayer.getLocationSkin(username == null ? "Steve" : username));
            if (username == null)
                drawModalRectWithCustomSizedTexture(left, top, 0, 0, size, size, size, size);
            else
                drawModalRectWithCustomSizedTexture(left, top, 0, 0, size, size, 90, 90);

            if (this.isHovered(left, top, left + size, top + size, mouseX, mouseY)) {
                GL11.glColor4f(1, 1, 1, 1);
                GlStateManager.enableBlend();
                mc.getTextureManager().bindTexture(new ResourceLocation("Fan/TooltipBackground.png"));
                drawModalRectWithCustomSizedTexture(left + 4.5f - 17 + 12, top - 63, 0, 0, 150, 41, 150, 41);

                int offset = 0;
                if (alt.username != null) {
                    GL11.glColor4f(1, 1, 1, 1);
                    FontUtil.cleanSmall.drawString(alt.username, left + 4.5f, top - 63 + 4.5f, -1);
                    offset += FontUtil.clean.getHeight() + 1;
                }

                if (alt.email != null) {
                    GL11.glColor4f(1, 1, 1, 1);
                    FontUtil.cleanSmall.drawString(alt.email, left + 4.5f, top - 63 + 4.5f + offset, -1);
                }

                int stage = alt.stage;
                if (stage != -1) {
                    float spriteWidth = stage <= 1 ? 10.5f : 28 / 2f;
                    float spriteX = left + 4.5f - 17 + 12 + 150 - 19 / 2f - spriteWidth, spriteY = top - 63 + 19 / 2f;
                    mc.getTextureManager().bindTexture(new ResourceLocation("Fan/AltSprites.png"));
                    if (stage == 0) {
                        GlStateManager.pushMatrix();
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                        GlStateManager.translate(spriteX + spriteWidth / 2f, spriteY + spriteWidth / 2f, 0);
                        GlStateManager.rotate(System.currentTimeMillis() % 360, 0, 0, 1);
                        GlStateManager.translate(-(spriteX + spriteWidth / 2f), -(spriteY + spriteWidth / 2f), 0);
                    }
                    drawModalRectWithCustomSizedTexture(spriteX, spriteY, stage * 10.5f, 0, spriteWidth, 10.5f, 35, 10.5f);
                    if (stage == 0)
                        GlStateManager.popMatrix();
                }
            }

            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            mc.getTextureManager().bindTexture(new ResourceLocation("Fan/altcircle.png"));
            drawModalRectWithCustomSizedTexture(left - 8, top - 8, 0, 0, 71, 71, 71, 71);

            count++;
        }
        if (scroll > count * (size + margin) + margin - width)
            scroll = count * (size + margin) + margin - width;

    }

    public boolean isHovered(float left, float top, float right, float bottom, int mouseX, int mouseY) {
        return mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom;
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void login() {
        String usernameS;
        String passwordS;
        if (username.getText().contains(":")) {
            String[] combo = username.getText().split(":");
            usernameS = combo[0];
            passwordS = combo[1];
        } else {
            usernameS = username.getText();
            passwordS = password.getText();
        }

        login(usernameS, passwordS);
    }

    public void login(String username, String password) {
        for (Alt alt : alts)
            if (alt.email.equals(username) && alt.password.equals(password)) {
                alt.stage = 0;
                alt.loginAsync();
                return;
            }

        Alt alt = new Alt(username, password);
        alts.add(alt);
        alt.stage = 0;
        alt.loginAsync();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);

        boolean hovered = isHovered(username.xPosition, 140, username.xPosition + 100, 160, mouseX, mouseY);

        if (isHovered(10, 10, 10 + 55, 30, mouseX, mouseY)) {
            mc.displayGuiScreen(back);
        }

        if (isHovered(75, 10, (float) (75 + FontUtil.clean.getStringWidth("Delete Alt") + 14), 30, mouseX, mouseY)) {
            if (selectedAlt != null) {
                alts.remove(selectedAlt);
                try {
                    Files.write(altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(alts.toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8));
                    //Show success message
                } catch (IOException e) {
                    e.printStackTrace();
                    //Show failure message
                }
            }
        }

        if (hovered) {
            login();
        }

        List<Alt> displayAlts = new ArrayList<Alt>(alts);
        Collections.reverse(displayAlts);
        int count = 0;
        boolean hit = false;
        for (Alt alt : displayAlts) {
            String username = alt.username;
            String uuid = alt.uuid;
            float size = 55, margin = 10, left = 10 + count * (size + margin), top = height - 65;
            boolean altHovered = isHovered(left, top, left + size, top + size, mouseX, mouseY);

            if (altHovered) {
                if (alt.uuid != null && alt.username != null)
                    profile = new FakeGameProfile(UUID.fromString(alt.uuid), alt.username);

                if (selectedAlt == alt)
                    login(alt.email != null ? alt.email : alt.username, alt.password);

                selectedAlt = alt;
                //selectedAlt.pullNameMC();
                hit = true;
            }

            count++;
        }

        if (!hit) {
            selectedAlt = null;
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            //login();
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    public void init() {
        try {
            final boolean createNewWorld = this.world == null;
            final WorldSettings worldSettings = new WorldSettings(0L, GameType.NOT_SET, true, false, WorldType.DEFAULT);
            final FakeNetHandlerPlayClient netHandler = new FakeNetHandlerPlayClient(mc);
            if (createNewWorld) {
                this.world = new FakeWorld(worldSettings, netHandler);
            }
            if (createNewWorld || this.player == null) {
                this.player = new EntityPlayerSP(mc, this.world, netHandler, null);
                int ModelParts = 0;
                for (EnumPlayerModelParts enumplayermodelparts : mc.gameSettings.getModelParts()) {
                    ModelParts |= enumplayermodelparts.getPartId();
                }
                this.player.getDataWatcher().updateObject(10, ModelParts);
                this.player.dimension = 0;
                this.player.movementInput = new MovementInputFromOptions(mc.gameSettings);
            }
            EntityUtils.updateLightmap(mc, this.world);
            mc.getRenderManager().cacheActiveRenderInfo(this.world, mc.fontRendererObj, this.player, this.player, mc.gameSettings, 0.0f);
        } catch (Throwable e) {
            e.printStackTrace();
            this.player = null;
            this.world = null;
        }
    }


}
