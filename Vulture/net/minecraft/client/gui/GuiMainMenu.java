package net.minecraft.client.gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.google.common.collect.Lists;

import de.iotacb.client.Client;
import de.iotacb.client.gui.changelog.GuiChangelog;
import de.iotacb.client.gui.elements.buttons.GuiTexturedButton;
import de.iotacb.client.gui.particle.ParticleManager;
import de.iotacb.client.utilities.math.Vec;
import de.iotacb.client.utilities.render.animations.AnimationUtil;
import de.iotacb.client.utilities.render.animations.easings.Elastic;
import de.iotacb.client.utilities.render.animations.easings.Quint;
import de.iotacb.client.utilities.render.image.ExternalImageDrawer;
import de.iotacb.cu.core.color.ColorUtil;
import de.iotacb.cu.core.math.MathUtil;
import de.iotacb.cu.core.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random RANDOM = new Random();

    /** Counts the number of screen updates. */
    private float updateCounter;

    /** The splash message. */
    private String splashText;
    private GuiButton buttonResetDemo;

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private DynamicTexture viewportTexture;
    private boolean field_175375_v = true;

    /**
     * The Object object utilized as a thread lock when performing non thread-safe operations
     */
    private final Object threadLock = new Object();

    /** OpenGL graphics card warning. */
    private String openGLWarning1;

    /** OpenGL graphics card warning. */
    private String openGLWarning2;

    /** Link to the Mojang Support about minimum requirements */
    private String openGLWarningLink;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");

    /** An array of all the paths to the panorama pictures. */
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;

    /** Minecraft Realms button. */
    private GuiButton realmsButton;
    
    ParticleManager pManager;

    public GuiMainMenu()
    {
    	startupRotation = 1;
        this.openGLWarning2 = field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            List<String> list = Lists.<String>newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = bufferedreader.readLine()) != null)
            {
                s = s.trim();

                if (!s.isEmpty())
                {
                    list.add(s);
                }
            }

            if (!list.isEmpty())
            {
                while (true)
                {
                    this.splashText = (String)list.get(RANDOM.nextInt(list.size()));

                    if (this.splashText.hashCode() != 125780783)
                    {
                        break;
                    }
                }
            }
        }
        catch (IOException var12)
        {
            ;
        }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException var11)
                {
                    ;
                }
            }
        }

        this.updateCounter = RANDOM.nextFloat();
        this.openGLWarning1 = "";

        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported())
        {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.panoramaTimer;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }
    
    private ExternalImageDrawer img;
    
    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
    	this.center = new Vec(width / 2, height / 2);
    	this.mouse = new Vec();
    	this.animUtil = new AnimationUtil(Elastic.class);
    	this.animUtil2 = new AnimationUtil(Quint.class);
    	this.animUtil2.getProgression(1338).setValue(1);
    	this.pManager = new ParticleManager();
    	
    	this.buttonList.add(new GuiTexturedButton(1, width / 2 - 201, height / 2 - 50, 100, 100, "client/designs/default/sub_buttons/single.png"));
    	this.buttonList.add(new GuiTexturedButton(2, width / 2 - 100, height / 2 - 50, 100, 100, "client/designs/default/sub_buttons/multi.png"));
    	this.buttonList.add(new GuiTexturedButton(3, width / 2 + 1, height / 2 - 50, 100, 100, "client/designs/default/sub_buttons/wheel.png"));
    	this.buttonList.add(new GuiTexturedButton(4, width / 2 + 102, height / 2 - 50, 100, 100, "client/designs/default/sub_buttons/exit.png"));
    	
    	this.buttonList.add(new GuiButton(5, width / 2 - 201, height / 2 + 51, 403, 20, "Changelog"));
    	this.buttonList.add(new GuiButton(6, width / 2 - 201, height / 2 - 71, 403, 20, "Change Background"));
    	super.initGui();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
    	if (button.id == 1) {
    		mc.displayGuiScreen(new GuiSelectWorld(this));
    	}
    	
    	if (button.id == 2) {
    		mc.displayGuiScreen(new GuiMultiplayer(this));
    	}
    	
    	if (button.id == 3) {
    		mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
    	}
    	
    	if (button.id == 4) {
    		mc.shutdown();
    	}
    	
    	if (button.id == 5) {
    		mc.displayGuiScreen(new GuiChangelog());
    	}
    	
    	if (button.id == 6) {
			final JFileChooser fileChooser = new JFileChooser();
			final JFrame frame = new JFrame();
			
			frame.setVisible(true);
			frame.toFront();
			frame.setVisible(false);
			
			fileChooser.setDialogTitle("Select select background");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			String path = "";
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				Client.INSTANCE.setBackgroundPath(fileChooser.getSelectedFile().getAbsolutePath());
				Client.INSTANCE.setCustomBackground(new ExternalImageDrawer(new File(Client.INSTANCE.getBackgroundPath())));
			}
			frame.dispose();
    	}
    }
    
    Vec center, mouse;
    AnimationUtil animUtil, animUtil2;
    
    double startupRotation;
    double blurring;
    
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	blurring = MathHelper.clamp_double(blurring, 0, 5);
    	if (areButtonsHovered()) {
    		animUtil2.getProgression(1338).reset();
    		blurring = animUtil2.easeOut(1337, 0, 5, 1);
    	} else {
    		animUtil2.getProgression(1337).reset();
    		blurring = 5 - animUtil2.easeOut(1338, 0, 5, 1);
    	}
    	
    	this.drawBackground(mouseX, mouseY);
    	
    	pManager.draw(mouseX, mouseY);
    	Client.BLUR_UTIL.blur(2);
    	
    	final String time = LocalTime.now().toString().split("\\.")[0];
    	Client.INSTANCE.getFontManager().getBigFont().drawStringWithShadow(time, (width - Client.INSTANCE.getFontManager().getBigFont().getWidth(time)) / 2, 40, Color.white);
    	final String date = new Date().toString().substring(0, 10).trim();
    	Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(date, (width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(date)) / 2, 40 + Client.INSTANCE.getFontManager().getBigFont().getHeight(time) + 5, Color.lightGray);
    	final String author = "by ".concat(Client.INSTANCE.getClientAuthor());
    	Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow(author, (width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth(author)) / 2, height - Client.INSTANCE.getFontManager().getDefaultFont().getHeight(author) - 10, Color.white);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private boolean areButtonsHovered() {
    	return buttonList.stream().filter(button -> button.hovered).findAny().orElse(null) != null;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        synchronized (this.threadLock)
        {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }
}
