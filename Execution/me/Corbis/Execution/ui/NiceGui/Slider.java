package me.Corbis.Execution.ui.NiceGui;

import de.Hero.settings.Setting;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class Slider extends Component{
    public Slider(int x, int y, Setting set, SettingsWindow window){
        super(x, y, set, window);
    }

    boolean isSliding = false;

    @Override
    public void draw(UnicodeFontRenderer ufr, int mouseX, int mouseY) {
        int height = 20;
        int width = 100;
        double progress;
        if (this.isSliding) {
            progress = (double)mouseX;
            double mouseProgress = (progress - (double)this.x) / (double)width;
            this.set.setValDouble((this.set.getMax() - this.set.getMin()) * mouseProgress);
        }

        if (this.set.getValDouble() <= this.set.getMin()) {
            this.set.setValDouble(this.set.getMin());
        }

        if (this.set.getValDouble() >= this.set.getMax()) {
            this.set.setValDouble(this.set.getMax());
        }

        progress = this.set.getValDouble() / this.set.getMax();
        Gui.drawRect(x, y + height / 2 - 1, x + width, y + height / 2 + 1, new Color(150, 150, 150, 255).getRGB());
        RenderingUtil.drawFilledCircle((int) (this.x + (double)width * progress)    , y + height / 2, 3, new Color(200, 200, 200, 255));
        ufr.drawString(this.set.getName() + " : " + (this.set.onlyInt() ? (double)((int)this.set.getValDouble()) : (double)Math.round(this.set.getValDouble() * 1000.0D) / 1000.0D), x + width + 5, y + 1, 0xFF000000);

        super.draw(ufr, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if(isWithinComponent(mouseX, mouseY)){
            this.isSliding = true;
        }
        super.mouseClicked(mouseX, mouseY);
    }

    public boolean isWithinComponent(int mouseX, int mouseY){
        int height = 20;
        int width = 100;
        return mouseX > x && x < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void mouseRelease() {
        isSliding = false;
        super.mouseRelease();
    }
}
