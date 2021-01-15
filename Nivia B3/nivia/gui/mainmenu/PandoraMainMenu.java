package nivia.gui.mainmenu;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import nivia.gui.altmanager.GuiAltManager;
import nivia.utils.Helper;
import nivia.utils.Wrapper;
import nivia.utils.font.Vec2f;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PandoraMainMenu extends GuiScreen {
	
    public static java.util.List<Vec2f> textCoords = new ArrayList<>();
    private ArrayList<CircleButton> buttons;
    int animation = 0;
    
    @Override
    public void initGui() {
    	 final ScaledResolution scaledRes = Wrapper.getScaledRes();

         buttons = new ArrayList<>();
         this.buttons.add(new CircleButton(0, scaledRes.getScaledWidth() / 2 - scaledRes.getScaledWidth() / 2.77f,
                 scaledRes.getScaledHeight() / 4 + scaledRes.getScaledHeight() / 2.77f, scaledRes.getScaledHeight() / 8, "Singleplayer",
                 new Color(108, 158, 255), new ResourceLocation("textures/pandora/singleplayer.png"), true,
                 new GuiSelectWorld(this)));
         
         this.buttons.add(new CircleButton(1, scaledRes.getScaledWidth() / 2 - scaledRes.getScaledWidth() / 5.5f,
                 scaledRes.getScaledHeight() / 4 + scaledRes.getScaledHeight() / 2.77f, scaledRes.getScaledHeight() / 8, "Multiplayer",
                 new Color(108, 158, 255), new ResourceLocation("textures/pandora/multiplayer.png"), true,
                 new GuiMultiplayer(this)));
         
         this.buttons.add(new CircleButton(2, scaledRes.getScaledWidth() / 2 ,
                 scaledRes.getScaledHeight() / 4 + scaledRes.getScaledHeight() / 2.77f, scaledRes.getScaledHeight() / 8, "Alt Manager",
                 new Color(108, 158, 255), new ResourceLocation("textures/pandora/altmanager.png"), true,
                 new GuiAltManager()));      
         
         this.buttons.add(new CircleButton(3, scaledRes.getScaledWidth() / 2 + scaledRes.getScaledWidth() / 5.5f,
                 scaledRes.getScaledHeight() / 4 + scaledRes.getScaledHeight() / 2.77f, scaledRes.getScaledHeight() / 8, "Settings", 
                 new Color(48, 158, 255), new ResourceLocation("textures/pandora/settings.png"), true,
                 new GuiOptions(this, this.mc.gameSettings)));
         
         this.buttons.add(new CircleButton(5, scaledRes.getScaledWidth() / 2 + scaledRes.getScaledWidth() / 2.77f,
                 scaledRes.getScaledHeight() / 4 + scaledRes.getScaledHeight() / 2.77f, scaledRes.getScaledHeight() / 8, "Exit",
                 new Color(108, 158, 255), new ResourceLocation("textures/pandora/exit.png"), true, null));
         //setupParticle();
    }
    
    int oldWidth, oldHeight, oldScale;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	++animation;
    	++animation;
    	Random r = new Random();
        int swidth, sheight, sscale;
        ScaledResolution res = Wrapper.getScaledRes();
        swidth = res.getScaledWidth();
        sheight = res.getScaledHeight();
        sscale = res.getScaleFactor();
        if ((swidth != oldWidth) || (sheight != oldHeight) || (sscale != oldScale)) {
            //setupParticle();
        }
        oldWidth = swidth;
        oldHeight = sheight;
        oldScale = sscale;

        mc.getTextureManager().bindTexture(new ResourceLocation("textures/pandora/mainmenu.png"));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, res.getScaledWidth(), res.getScaledHeight(),
                res.getScaledWidth(), res.getScaledHeight(), res.getScaledWidth(),
                res.getScaledHeight());
       
        //mc.getTextureManager().bindTexture(new ResourceLocation("textures/pandora/nivialogo.png"));
        //Gui.drawScaledCustomSizeModalRect(res.getScaledWidth() / 2 - 65, 0, 0,0, 10,10, 128, 128, 10.0F, 10.0F);
        //Helper.get2DUtils().drawCustomImage(res.getScaledWidth() / 2 - 57, 0, 110, 128, new ResourceLocation("textures/pandora/nivialogo.png"));
      
        //GL11.glEnable(GL11.GL_BLEND);
        //System.out.println(height / 4.24);
       // Helper.get2DUtils().drawRect(width / 2, height / 2 - 90, width / 2 - 4, height / 2 - 90 + 60, 0xff000000);
      // Helper.get2DUtils().drawRect(width / 2, height / 2 - 90, width / 2 + (15), height / 2 - 86, 0xff000000);
     //   Helper.get2DUtils().drawRect(width / 2, height / 2 - 58, width / 2 + (15), height / 2 - 62, 0xff000000);
		/*CircleButton.drawFullCircle(width / 2 + 9, this.height / 2 - 60, 50f, 360, 10f, 360, 0xff000000);
		CircleButton.drawFullCircle(width / 2 + 14, this.height / 2 - 74, 14f, 180, 7.5f, 180, 0xff000000);
		CircleButton.drawFullCircle(width / 2 + 9, this.height / 2 - 60, 50f, (int) (animation * 1.0), 5f, 180, -1);
		
		CircleButton.drawFullCircle((float) (width / 2 - width / 2.25), (float) (height / 2 - height / 2.55), res.getScaledHeight() / 12, 360, 5, 360, -1);
		
        Helper.get2DUtils().drawRect(width / 2, (float) (height / 2 - height / 2.55), width / 2 - height / 100, (float) (height / 2 - height / 2.77 + 60), 0xff000000);
        Helper.get2DUtils().drawRect(width / 2, (float) (height / 2 - height / 2.55), width / 2 + (15), (float) (height / 2 - height / 2.62), 0xff000000);
        Helper.get2DUtils().drawRect(width / 2, (float) (height / 2 - height / 2.88), width / 2 + (15), (float) (height / 2 - height / 2.8), 0xff000000);
        CircleButton.drawFullCircle(width / 2 + width / 40, (float) (this.height / 2 - height / 2.738), res.getScaledHeight() / 45, 180, 10f, 180, 0xff000000);*/
		//CircleButton.drawFullCircle(width / 2 + 10, this.height / 2 - 60, 50f, animation, 5.4f, 180, 0xff000000);
		//RenderHelper.drawRect(width / 2 - 150, height / 2 - 30, width / 2 - 149, height / 2 - 30 + 60, 0xff000000);
		//RenderHelper.drawRect(width / 2 - 150, height / 2 - 30, width / 2 - 150 + (15), height / 2 - 29,
		//		0xff000000);
		//RenderHelper.drawRect(width / 2 - 150, height / 2, width / 2 - 150 + (15), height / 2 - 1, 0xff000000);
        /*if (ParticleEngine.pEngine != null) {
            ParticleEngine.pEngine.onRenderIntoGui(mouseX, mouseY, new ScaledResolution(mc));
        }*/


        //RenderHelper.roboto.drawString("FPS: " + mc.getDebugFPS(),1,this.height - 7, new Color(131, 214, 206).getRGB());

        buttons.forEach((button) -> {
           // Helper.get2DUtils().drawCustomImage(0,0,0,0, new ResourceLocation("textures/pascalhook/background.jpg"));
            button.drawButton(mc, mouseX, mouseY);
        });
        
        Helper.get2DUtils().drawBorderedRect(0, 0, 0, 0, -1, -1);

        //RenderHelper.drawRect(0,0,0,0,-1);

        /*ResourceLocation loc = new ResourceLocation("textures/pascalhook/information.png");
        RenderHelper.drawIcon(loc,50,50,600,600);
        */


        super.drawScreen(mouseX,mouseY,partialTicks);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        buttons.forEach((button) -> {
            if (button.isWithinButton(mouseX, mouseY)) {
                if (button.getActionValue() == CircleButton.Action.GuiScreen) {
                    GuiScreen screen = (GuiScreen) button.getAction();
                    mc.displayGuiScreen(screen);
                   // Athena.INST.getRadioUtil().pauseRadio();
                } 
            }
        });
        // mc.displayGuiScreen(new TestMenu());
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void circle(double n, double n2, double n3, int n4) {
        float n5 = (n4 >> 24 & 0xFF) / 255.0f;
        float n6 = (n4 >> 16 & 0xFF) / 255.0f;
        float n7 = (n4 >> 8 & 0xFF) / 255.0f;
        float n8 = (n4 & 0xFF) / 255.0f;
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.color(n6, n7, n8, n5);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.func_179090_x();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator instance = Tessellator.getInstance();
        WorldRenderer worldRenderer = instance.getWorldRenderer();
        for (double n9 = 0.0; n9 < 360.0; ++n9) {
            double n10 = n9 * 3.141592653589793 / 180.0;
            double n11 = (n9 - 1.0) * 3.141592653589793 / 180.0;
            double[] array = { Math.cos(n10) * n3, -Math.sin(n10) * n3, Math.cos(n11) * n3, -Math.sin(n11) * n3 };
            worldRenderer.startDrawing(6);
            worldRenderer.addVertex(n + array[2], n2 + array[3], 0.0);
            worldRenderer.addVertex(n + array[0], n2 + array[1], 0.0);
            worldRenderer.addVertex(n, n2, 0.0);
            instance.draw();
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.func_179098_w();
        GlStateManager.alphaFunc(516, 0.1f);
    }

}
