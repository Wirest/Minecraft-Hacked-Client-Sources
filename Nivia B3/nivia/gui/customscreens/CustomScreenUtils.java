package nivia.gui.customscreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import nivia.gui.altmanager.GuiAddAlt;
import nivia.gui.altmanager.GuiAltLogin;
import nivia.gui.altmanager.GuiAltManager;
import nivia.gui.altmanager.GuiRenameAlt;
import nivia.gui.mainmenu.PandoraMainMenu;
import nivia.utils.Helper;


public class CustomScreenUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static ResourceLocation mainLogo = new ResourceLocation("textures/pandora/nivialogo.png");
    public static ResourceLocation alt = new ResourceLocation("textures/pandora/alt.jpg");
    public static ResourceLocation mainMenu = new ResourceLocation("textures/pandora/mainmenu.png");
    public static ResourceLocation buttons = new ResourceLocation("textures/pandora/buttons.png");
    private PandoraMainMenu mm;

    public CustomScreenUtils() {
    }

    public static int width() {
        return Helper.get2DUtils().scaledRes().getScaledWidth();
    }
    public static int height() {
        return Helper.get2DUtils().scaledRes().getScaledHeight();
    }
    public static void drawMainBackGround() {
        int par1 = width();
        int par2 = height();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.getTextureManager().bindTexture(mainMenu);
        Tessellator var3 = Tessellator.getInstance();
        WorldRenderer var4 = var3.getWorldRenderer();
        var4.startDrawingQuads();
        var4.addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
        var4.addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
        var4.addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
        var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    public static void drawBackground(ResourceLocation location) {
        int par1 = width();
        int par2 = height();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.getTextureManager().bindTexture(location);
        Tessellator var3 = Tessellator.getInstance();
        WorldRenderer var4 = var3.getWorldRenderer();
        var4.startDrawingQuads();
        var4.addVertexWithUV(0.0D, par2, -90.0D, 0.0D, 1.0D);
        var4.addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
        var4.addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
        var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    public static boolean drawCustomButtons() {
    	GuiButton e;
        GuiScreen s = Helper.mc().currentScreen;
        return s instanceof GuiAltManager || s instanceof GuiRenameAlt || s instanceof GuiAddAlt || s instanceof GuiAltLogin;
    }
}
