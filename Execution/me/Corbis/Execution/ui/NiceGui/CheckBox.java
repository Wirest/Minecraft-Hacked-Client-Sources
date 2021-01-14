package me.Corbis.Execution.ui.NiceGui;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CheckBox extends Component{

    public CheckBox(int x, int y, Setting set, SettingsWindow window){
        super(x,y,set,window);
    }

    @Override
    public void draw(UnicodeFontRenderer ufr, int mouseX, int mouseY) {
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f );
        ufr.drawString(set.getName(),x + 15, y + 2 , 0xFF000000);

        if(!set.getValBoolean()) {
            RenderingUtil.drawCircle(x + 4, y + 8, 6, 100, 0xFF505050);
        }else {
            RenderingUtil.drawCircle(x + 4, y + 8, 6, 100, 0xFF505050);
            RenderingUtil.drawFilledCircle(x + 4, y + 8, 4, new Color(50, 50, 50, 255));
        }
        super.draw(ufr, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if(isWithinComponent(mouseX, mouseY)) {
            set.setValBoolean(!set.getValBoolean());
        }
        super.mouseClicked(mouseX, mouseY);
    }

    public boolean isWithinComponent(int mouseX, int mouseY){
        return mouseX > x && mouseX < x + 15 && mouseY > y && mouseY < y + 12;
    }
}
