package me.Corbis.Execution.ui.NiceGui;

import de.Hero.settings.Setting;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ComboBox extends Component{

    public ComboBox(int x, int y, Setting set, SettingsWindow window){
        super(x, y, set, window);
    }

    @Override
    public void draw(UnicodeFontRenderer ufr, int mouseX, int mouseY) {
        RenderingUtil.drawRoundedRect(x, y, x + 150, y + 16, 3,new Color(230, 230, 230, 255));
        ufr.drawString(set.getName() + " : " + set.getValString(),x + 2, y + 1, 0xFF000000);

        super.draw(ufr, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if(isWithinComponent(mouseX, mouseY)){
            int index = set.getOptions().indexOf(set.getValString());
            if(index + 1 >= set.getOptions().size()){
                set.setValString(set.getOptions().get(0));
            }else {
                set.setValString(set.getOptions().get(index + 1));
            }
        }
        super.mouseClicked(mouseX, mouseY);
    }

    public boolean isWithinComponent(int mouseX, int mouseY){
        return mouseX > x && mouseX < x + 80 && mouseY > y && mouseY < y + 20;
    }
}
