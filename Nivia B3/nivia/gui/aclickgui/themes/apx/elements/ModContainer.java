package nivia.gui.aclickgui.themes.apx.elements;

import net.minecraft.client.Minecraft;
import nivia.gui.aclickgui.Element;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apex on 8/20/2016.
 */
class ModContainer  extends Element {

    private ModButton parent;
    private List<Element> valueList = new ArrayList<>();
    private Module mod;
    boolean isExpanded = false;
    public int moveTicks = 0;
    public float totalHeight = 20;
    public int maxTicks = 50;
    public boolean moveDown = false;
    public boolean moveUp = false;

    public ModContainer(ModButton panel, Module mod) {
        this.parent = panel;
        this.mod = mod;
        this.setHeight(0);
        this.setWidth(panel.getWidth());

        for (Property mv : mod.getModuleProperties()) {

        	if(mv.value instanceof Boolean)
        		valueList.add(new ValueButton(this, mod, mv));       	
        	else if(mv instanceof DoubleProperty) {
        		if(((DoubleProperty) mv).colorSlider) {
        			valueList.add(new HueSlider(this, mod, (DoubleProperty) mv));
        		            
        		}
        		 else        		
        			valueList.add(new ModSlider(this, mod, (DoubleProperty) mv));        
        	}
        	else if(mv.value instanceof Enum)
        		valueList.add(new ModMode(this, mod, mv));               
            }        
        valueList.add(new ModKeybind(this, mod));
    }

    private long startTime = 0;

    @Override
    public void drawElement(float posX, float posY, int mouseX, int mouseY, float partialTicks) {
        this.setPosX(posX);
        this.setPosY(posY);

        totalHeight = 0;
        for (Element e : valueList){
            totalHeight += e.getHeight();
        }

        int Speed = 30;
        if (startTime != 0) {

        }

        maxTicks = (30 * (1 + Minecraft.debugFPS)) / 150;

        if (moveDown) {
            if (moveTicks == 0) {
                startTime = System.currentTimeMillis();
            }
            if (moveTicks < maxTicks)
                ++moveTicks;
            else {
                moveDown = false;
                moveTicks = 0;
            }
        } else if (moveUp) {
            if (moveTicks > 0) {
                --moveTicks;
            } else {
                moveUp = false;
                this.setHeight(20);
                isExpanded = false;
                startTime = 0;
            }
        }

        if (isExpanded) {
            double total = ((getHeight() / maxTicks) * (moveDown ? moveTicks : maxTicks));
            double totalUP = ((getHeight() / maxTicks) * moveTicks);

            if (moveDown) {
                parent.setHeight((int) (22 + total));
            } else if (moveUp) {
                parent.setHeight((int) (20 + totalUP));
            }

            //Render.drawRoundedRect(getPosX() + 3, getPosY() + 2, getPosX() + getWidth() - 5, getPosY() + getHeight() - 2, 8, 0x4F000000);
            int height = 0;
            for (Element e : valueList) {
                e.setWidth(90);
                e.drawElement(posX + 4, posY + height + 4, mouseX, mouseY, partialTicks);
                height += e.getHeight();
            }
        }


    }

    @Override
    public float getHeight() {
        return 8 + (totalHeight);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        boolean overContainer = this.isExpanded && mouseX > getPosX() + 3 && mouseY > getPosY() + 2 && mouseX < getPosX() + getWidth() - 5 && mouseY < getPosY() + getHeight() - 2;
        if (overContainer) {
            for (Element e : valueList) {
                e.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)  {
        for (Element e : valueList) {
            e.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean overContainer = this.isExpanded && mouseX > getPosX() + 3 && mouseY > getPosY() + 2 && mouseX < getPosX() + getWidth() - 5 && mouseY < getPosY() + getHeight() - 2;
        if (overContainer) {
            for (Element e : valueList) {
                e.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
