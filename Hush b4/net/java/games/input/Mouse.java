// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public abstract class Mouse extends AbstractController
{
    protected Mouse(final String name, final Component[] components, final Controller[] children, final Rumbler[] rumblers) {
        super(name, components, children, rumblers);
    }
    
    public Controller.Type getType() {
        return Controller.Type.MOUSE;
    }
    
    public Component getX() {
        return this.getComponent(Component.Identifier.Axis.X);
    }
    
    public Component getY() {
        return this.getComponent(Component.Identifier.Axis.Y);
    }
    
    public Component getWheel() {
        return this.getComponent(Component.Identifier.Axis.Z);
    }
    
    public Component getPrimaryButton() {
        Component primaryButton = this.getComponent(Component.Identifier.Button.LEFT);
        if (primaryButton == null) {
            primaryButton = this.getComponent(Component.Identifier.Button._1);
        }
        return primaryButton;
    }
    
    public Component getSecondaryButton() {
        Component secondaryButton = this.getComponent(Component.Identifier.Button.RIGHT);
        if (secondaryButton == null) {
            secondaryButton = this.getComponent(Component.Identifier.Button._2);
        }
        return secondaryButton;
    }
    
    public Component getTertiaryButton() {
        Component tertiaryButton = this.getComponent(Component.Identifier.Button.MIDDLE);
        if (tertiaryButton == null) {
            tertiaryButton = this.getComponent(Component.Identifier.Button._3);
        }
        return tertiaryButton;
    }
    
    public Component getLeft() {
        return this.getComponent(Component.Identifier.Button.LEFT);
    }
    
    public Component getRight() {
        return this.getComponent(Component.Identifier.Button.RIGHT);
    }
    
    public Component getMiddle() {
        return this.getComponent(Component.Identifier.Button.MIDDLE);
    }
    
    public Component getSide() {
        return this.getComponent(Component.Identifier.Button.SIDE);
    }
    
    public Component getExtra() {
        return this.getComponent(Component.Identifier.Button.EXTRA);
    }
    
    public Component getForward() {
        return this.getComponent(Component.Identifier.Button.FORWARD);
    }
    
    public Component getBack() {
        return this.getComponent(Component.Identifier.Button.BACK);
    }
    
    public Component getButton3() {
        return this.getComponent(Component.Identifier.Button._3);
    }
    
    public Component getButton4() {
        return this.getComponent(Component.Identifier.Button._4);
    }
}
