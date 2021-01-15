// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import cf.euphoria.euphorical.Events.EventRender2D;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class InvMove extends Mod
{
    public InvMove() {
        super("InvMove", Category.PLAYER);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown(200)) {
                final EntityPlayerSP thePlayer = this.mc.thePlayer;
                thePlayer.rotationPitch -= 2.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                thePlayer2.rotationPitch += 2.0f;
            }
            if (Keyboard.isKeyDown(203)) {
                final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                thePlayer3.rotationYaw -= 3.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                thePlayer4.rotationYaw += 3.0f;
            }
            Minecraft.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindForward.getKeyCode());
            Minecraft.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindBack.getKeyCode());
            Minecraft.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindLeft.getKeyCode());
            Minecraft.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindRight.getKeyCode());
            Minecraft.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindJump.getKeyCode());
            Minecraft.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(Minecraft.gameSettings.keyBindSneak.getKeyCode());
        }
    }
}
