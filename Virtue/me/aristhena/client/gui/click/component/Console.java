// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click.component;

import me.aristhena.client.command.Command;
import me.aristhena.client.command.CommandManager;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.ScaledResolution;
import me.aristhena.utils.ClientUtils;
import me.aristhena.utils.minecraft.GuiTextField;

public class Console extends Component
{
    private GuiTextField textField;
    private boolean autoCompleteNext;
    
    public Console() {
        super(null, 0.0, 0.0, 0.0, 0.0);
        this.textField = new GuiTextField(-69, ClientUtils.clientFont(), 0, 0, 0, 0);
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        final ScaledResolution scaledRes = new ScaledResolution(ClientUtils.mc(), ClientUtils.mc().displayWidth, ClientUtils.mc().displayHeight);
        this.textField.xPosition = (int)(scaledRes.getScaledWidth_double() / 4.0);
        this.textField.width = (int)(scaledRes.getScaledWidth_double() / 2.0);
        this.textField.yPosition = 2;
        this.textField.height = 18;
        this.textField.drawTextBox();
    }
    
    @Override
    public void click(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void drag(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void release(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void keyPress(final int keyInt, final char keyChar) {
    }
    
    public boolean keyType(final int keyInt, final char keyChar) {
        if (this.textField.isFocused() && 1 == keyInt) {
            this.textField.setText("");
            this.textField.setFocused(false);
            return true;
        }
        if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) && 53 == keyInt) {
            this.textField.setText("");
            this.textField.setFocused(true);
        }
        else if (28 == keyInt) {
            final String[] args = this.textField.getText().split(" ");
            final Command commandFromMessage = CommandManager.getCommandFromMessage(this.textField.getText());
            commandFromMessage.runCommand(args);
            this.textField.setText("");
        }
        else if (this.textField.isFocused()) {
            this.autoCompleteNext = false;
            this.textField.textboxKeyTyped(keyChar, keyInt);
        }
        return false;
    }
}
