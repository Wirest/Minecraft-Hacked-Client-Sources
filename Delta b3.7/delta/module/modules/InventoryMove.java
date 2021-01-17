/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender2D
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiCommandBlock
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.client.settings.KeyBinding
 *  org.lwjgl.input.Keyboard
 */
package delta.module.modules;

import cpw.mods.fml.relauncher.ReflectionHelper;
import delta.Class132;
import delta.Class41;
import delta.guis.click.ClickGUI;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender2D;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove
extends Module {
    private KeyBinding[] keybinds;
    private boolean cameraMove;

    public InventoryMove() {
        super("InventoryMove", Category.Movement);
        KeyBinding[] arrkeyBinding = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindSprint};
        this.keybinds = arrkeyBinding;
        this.setDescription("Permet de bouger en \u00e9tant dans les menus");
        this.addSetting(new Setting("CameraMove", (IModule)this, true));
    }

    @EventTarget
    public void onRender2D(EventRender2D eventRender2D) {
        if (this.mc.theWorld != null && this.mc.thePlayer != null && this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat) && !(this.mc.currentScreen instanceof GuiEditSign) && !(this.mc.currentScreen instanceof GuiCommandBlock) && this.getSetting("CameraMove").getCheckValue()) {
            this.mc.thePlayer.rotationPitch += Keyboard.isKeyDown((int)208) ? 3.0f : (Keyboard.isKeyDown((int)200) ? -3.0f : 0.0f);
            this.mc.thePlayer.rotationYaw += Keyboard.isKeyDown((int)205) ? 3.0f : (Keyboard.isKeyDown((int)203) ? -3.0f : 0.0f);
        }
    }

    public void onDisable() {
        this.setKeysDown();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (!(this.mc.theWorld == null || this.mc.thePlayer == null || this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiEditSign || this.mc.currentScreen instanceof GuiCommandBlock)) {
            this.cameraMove = true;
            if (this.mc.currentScreen instanceof ClickGUI) {
                ClickGUI clickGUI = (ClickGUI)this.mc.currentScreen;
                for (Class132 class132 : clickGUI.sprint$) {
                    if (!class132.worldcat$ || !(class132 instanceof Class41)) continue;
                    return;
                }
            }
            this.setKeysDown();
        }
        if (this.mc.currentScreen == null && this.cameraMove) {
            this.cameraMove = false;
            this.setKeysDown();
        }
    }

    public void setKeysDown() {
        for (KeyBinding keyBinding : this.keybinds) {
            String[] arrstring = new String[]{"pressed", "field_74513_e"};
            ReflectionHelper.setPrivateValue(KeyBinding.class, (Object)keyBinding, (Object)Keyboard.isKeyDown((int)keyBinding.getKeyCode()), (String[])arrstring);
        }
    }
}

