package skyline.specc.extras.buttons;

public class MouseHovering
{
    public float x;
    public float y;
    public float x1;
    public float y1;
    
    public MouseHovering(final float x, final float y, final float x1, final float y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }
    
    public boolean mouseHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.x && mouseX <= this.x1 && mouseY >= this.y && mouseY < this.y1;
    }
}
