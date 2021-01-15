package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import nivia.gui.aclickgui.Element;
import nivia.gui.aclickgui.GuiAPX;
import nivia.modules.Module;
import nivia.utils.Helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apex on 8/19/2016.
 */
public class ModButton extends Element {

    private Module mod;
    private List<ModButton> modList = new ArrayList<>();
    public ModContainer container = null;
    public int height = 20;

    public ModButton(Panel panel, Module mod) {
        this.parent = panel;
        this.mod = mod;
        this.setWidth(100);
        this.setHeight(height);
        
        container = new ModContainer(this, mod);
        
    }

    private ResourceLocation arrow = new ResourceLocation("textures/pandora/arrow.png");

    public Panel parent;

    private int tickFade = 0;
    private float tickButton = 0;
    private boolean fade = false;


    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
        this.setPosX(posX);
        this.setPosY(posY);

        if ((posY+getRealHeight()) < (parent.getPosY()+14))
            return;

        FontRenderer modFont = Helper.mc().fontRendererObj;
        boolean hasValues = mod.getModuleProperties().size() > 0;
        boolean isOverValues = hasValues && (mouseX > getPosX() + 2 && mouseX <= getPosX() + 13 && mouseY > getPosY() && mouseY <= getPosY() + getHeight());
        float overX = getPosX() + (isOverValues ? 10 : 0);
        boolean isOver = mouseX > overX && mouseX <= overX + getWidth() && mouseY > getPosY() && mouseY <= getPosY() + getHeight();
        boolean enabled = mod.getState();

        int colorIfOver = 0xFF151515;
        int tickSpeed = 5;
        if (isOver) {
            fade = true;
        }

        if (fade) {
            if (tickFade < tickSpeed)
                tickFade++;
            else
                fade = false;
        } else if (tickFade > 0) {
            --tickFade;
        }

        if (tickFade != 0) {
            int multiplier = 75 / tickSpeed;
            colorIfOver = ((tickFade * multiplier)<<24)|colorIfOver;
        } else
            colorIfOver = 0;


        boolean flag = false;

        int color1 = flag ? 0x1F000000 : 0x5F000000;
        int colorIf = flag ? 0 : colorIfOver;
        int uicolor = GuiAPX.color;
        //(this.mod.getState() ? Helper.colorUtils().darker(new Color(ClickGui.color), 0.05) : 0xFF303030) : (this.mod.getState() ? Helper.colorUtils().darker(new Color(ClickGui.color), 0.2) : new Color(14,14,14).getRGB()), 0xAA000000)
        Helper.get2DUtils().drawBorderedRect(getPosX(), getPosY(), getPosX() + getWidth() - 3 , getPosY() + height - 1, isOver ? (mod.getState() ? Helper.colorUtils().darker(new Color(uicolor), 0.175) : colorIfOver) : (mod.getState() ? Helper.colorUtils().darker(new Color(uicolor), 0.1) : color1), 0);
       
        
        modFont.drawStringWithShadow(mod.getTag(), getPosX()  + 5, getPosY() + 6, flag ? 0xFF6F6F6F : (enabled ? uicolor : 0xFFFFFFFF));

//        if (hasValues && !flag) {
//            GL11.glPushMatrix();
//            GL11.glColor4f(1, 1, 1, 0.7F);
//
//
//            float speedMax = 20;
//            /*if (isOverValues || this.getHeight() > 20) {
//                if (tickButton < speedMax) {
//                    tickButton++;
//                }
//            } else if (tickButton > 0) {
//                tickButton--;
//            }*/
//
//            /*if (tickButton != 0) {
//                float rotation = (90 / speedMax) * tickButton;
//                float percent = 90 / rotation * 100;
//                float value = 5 / percent * 100;
//                GL11.glTranslatef(getPosX() + 1 + value*2, getPosY() + 0.1F, 0);
//                GlStateManager.rotate(rotation, 0, 0, 1);
//            } else
//                GL11.glTranslatef(getPosX() + 1, getPosY() , 0);*/
//
//            int aSize = 3;
//            GL11.glTranslatef(getPosX() + 5, getPosY() + 7F, 0);
//            if (isOverValues || this.getHeight() > 20) {
//            	if(tickButton < 90)
//                tickButton += 8;
//                if (tickButton >= 90) tickButton = 90 ;
//                
//                GlStateManager.translate(aSize, aSize, aSize);
//                GlStateManager.rotate(tickButton, 0, 0, 1F);
//                GlStateManager.translate(-aSize, -aSize, -aSize);
//                
//                Helper.get2DUtils().drawCustomImage(0,0, 6, 6, arrow);
//            } else {
//            	
//            	tickButton = 0;
//            	Helper.get2DUtils().drawCustomImage(0,0, 6, 6, arrow);
//            }
//            
//
//            GL11.glPopMatrix();
//        }

        /* Enabled Bar (RIGHT) translate if scrollbar */
        Helper.get2DUtils().drawRect(getPosX() + 2, getPosY() + 20, getPosX() + 4 , getPosY() + getHeight(), enabled ? uicolor : 0x1FE2E2E2);


        if (container != null) {
            container.drawElement(getPosX() + 1, getPosY() + 20, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)  {
        if (container != null)
            container.keyTyped(typedChar, keyCode);
    }

    @Override
    public float getRealHeight() {
        return container == null ? 20 : ((container.isExpanded ? container.getHeight() - 4 : 0) + 5);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hasValues = mod.getModuleProperties().size() > 0;
        boolean isOverValues = hasValues && (mouseX > getPosX() + 2 && mouseX <= getPosX() + 2 && mouseY > getPosY() && mouseY <= getPosY() + getHeight());
        float overX = getPosX() + (isOverValues ? 10 : 0);

        boolean isOver = mouseX > overX && mouseX <= overX + getWidth() && mouseY > getPosY() && mouseY <= getPosY() + 20;

        if (button == 0 && isOverValues || button == 1 && isOver) {
            if (getHeight() > 20) {
                container.moveUp = true;
                container.moveTicks = container.maxTicks;
            } else {
                container.isExpanded = true;
                container.moveDown = true;
            }

        } else if (button == 0 && isOver) {
            mod.Toggle();

        }
        if (container != null)
            container.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (container != null)
            container.mouseReleased(mouseX, mouseY, button);
    }
}
