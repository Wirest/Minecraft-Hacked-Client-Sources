// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.gui.click;

import me.aristhena.client.option.OptionManager;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import java.util.ArrayList;
import me.aristhena.client.gui.click.component.window.Window;
import java.util.List;
import java.util.Iterator;
import me.aristhena.client.module.ModuleManager;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.module.Module;
import me.aristhena.utils.FileUtils;
import me.aristhena.client.gui.click.component.Console;
import me.aristhena.client.gui.click.component.window.ModuleWindow;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.File;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    private static final File GUI_DIR;
    private static final float SCALE = 2.0f;
    private CopyOnWriteArrayList<ModuleWindow> windows;
    private static ClickGui instance;
    private static Console console;
    private boolean binding;
    
    static {
        GUI_DIR = FileUtils.getConfigFile("Gui");
    }
    
    public static void start() {
        ClickGui.instance = new ClickGui();
    }
    
    private ClickGui() {
        this.windows = new CopyOnWriteArrayList<ModuleWindow>();
        double x = 20.0;
        final double y = 50.0;
        double width = 0.0;
        Module.Category[] values;
        for (int length = (values = Module.Category.values()).length, i = 0; i < length; ++i) {
            final Module.Category category = values[i];
            width = Math.max(ClientUtils.clientFont().getStringWidth(category.name()) * 1.1, width);
            for (final Module module : ModuleManager.getModules()) {
                if (module.getCategory().equals(category)) {
                    width = Math.max(width, ClientUtils.clientFont().getStringWidth(module.getDisplayName()));
                }
            }
            final ModuleWindow window = new ModuleWindow(category, x, y, width);
            this.windows.add(window);
            x += 100.0;
        }
        this.load();
        this.save();
        ClickGui.console = new Console();
    }
    
    public void load() {
        final List<String> fileContent = FileUtils.read(ClickGui.GUI_DIR);
        for (final String line : fileContent) {
            final String[] split = line.split(":");
            final String windowName = split[0];
            final String windowPosX = split[1];
            final String windowPosY = split[2];
            final String windowExtended = split[3];
            final Window window = this.getWindow(windowName);
            final int posX = Integer.parseInt(windowPosX);
            final int posY = Integer.parseInt(windowPosY);
            final Boolean extended = Boolean.parseBoolean(windowExtended);
            window.setStartOffset(new double[] { 0.0, 0.0 });
            window.setDragging(true);
            if (window.getHandle() != null) {
                window.getHandle().drag(posX, posY, 0);
            }
            window.setDragging(false);
            window.setExtended(extended);
        }
    }
    
    public void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final ModuleWindow window : this.windows) {
            final String windowName = window.getParent().name();
            final String windowPosX = new StringBuilder().append((int)window.getX()).toString();
            final String windowPosY = new StringBuilder().append((int)window.getY()).toString();
            final String windowExtended = Boolean.toString(window.isExtended());
            fileContent.add(String.format("%s:%s:%s:%s", windowName, windowPosX, windowPosY, windowExtended));
        }
        FileUtils.write(ClickGui.GUI_DIR, fileContent, true);
    }
    
    private ModuleWindow getWindow(final String name) {
        for (final ModuleWindow window : this.windows) {
            if (window.getParent().name().equalsIgnoreCase(name)) {
                return window;
            }
        }
        return null;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        float scale = scaledRes.getScaleFactor() / (float)Math.pow(scaledRes.getScaleFactor(), 2.0) * 2.0f;
        scale /= (float)(1920.0 / Display.getWidth());
        GlStateManager.scale(scale, scale, scale);
        final int realMouseX = (int)Math.round(Mouse.getX() / 2.0f * (1920.0 / Display.getWidth()));
        final int realMouseY = (int)Math.round((Display.getHeight() - Mouse.getY()) / 2.0f * (1920.0 / Display.getWidth()));
        for (final Window window : this.windows) {
            window.draw(realMouseX, realMouseY);
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f, scale, 1.0f);
        ClickGui.console.draw(realMouseX, realMouseY);
        GlStateManager.popMatrix();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        final int realMouseX = (int)Math.round(Mouse.getX() / 2.0f * (1920.0 / Display.getWidth()));
        final int realMouseY = (int)Math.round((Display.getHeight() - Mouse.getY()) / 2.0f * (1920.0 / Display.getWidth()));
        for (final Window window : this.getWindows()) {
            window.click(realMouseX, realMouseY, mouseButton);
        }
        this.save();
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        final int realMouseX = (int)Math.round(Mouse.getX() / 2.0f * (1920.0 / Display.getWidth()));
        final int realMouseY = (int)Math.round((Display.getHeight() - Mouse.getY()) / 2.0f * (1920.0 / Display.getWidth()));
        for (final Window window : this.windows) {
            if (window.getHandle() != null) {
                window.getHandle().drag(realMouseX, realMouseY, clickedMouseButton);
            }
        }
        this.save();
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        final int realMouseX = (int)Math.round(Mouse.getX() / 2.0f * (1920.0 / Display.getWidth()));
        final int realMouseY = (int)Math.round((Display.getHeight() - Mouse.getY()) / 2.0f * (1920.0 / Display.getWidth()));
        for (final Window window : this.windows) {
            window.release(realMouseX, realMouseY, state);
        }
        this.save();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.binding && !ClickGui.console.keyType(keyCode, typedChar)) {
            super.keyTyped(typedChar, keyCode);
        }
        for (final Window window : this.windows) {
            window.keyPress(keyCode, typedChar);
        }
    }
    
    @Override
    public void onGuiClosed() {
        ModuleManager.save();
        OptionManager.save();
        super.onGuiClosed();
    }
    
    public Window getTopWindow(final int mouseX, final int mouseY) {
        final List<ModuleWindow> hoveringWindows = new ArrayList<ModuleWindow>();
        for (final ModuleWindow window : this.windows) {
            if (window.hovering(mouseX, mouseY) || window.getHandle().hovering(mouseX, mouseY)) {
                hoveringWindows.add(window);
            }
        }
        if (!hoveringWindows.isEmpty()) {
            final ModuleWindow lastWindow = hoveringWindows.get(hoveringWindows.size() - 1);
            this.windows.remove(lastWindow);
            this.windows.add(lastWindow);
            return lastWindow;
        }
        return null;
    }
    
    public static ClickGui getInstance() {
        return ClickGui.instance;
    }
    
    public CopyOnWriteArrayList<ModuleWindow> getWindows() {
        return this.windows;
    }
    
    public boolean isBinding() {
        return this.binding;
    }
    
    public void setBinding(final boolean binding) {
        this.binding = binding;
    }
}
