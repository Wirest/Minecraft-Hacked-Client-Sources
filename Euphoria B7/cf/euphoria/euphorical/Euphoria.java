// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical;

import org.lwjgl.opengl.Display;
import cf.euphoria.euphorical.UI.TabGUI;
import cf.euphoria.euphorical.Mod.Mod;
import cf.euphoria.euphorical.Mod.Collection.Misc.Commands;
import org.darkstorm.minecraft.gui.GuiManager;
import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import cf.euphoria.euphorical.Mod.BoolOption;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import cf.euphoria.euphorical.UI.GuiKeybindMgr;
import cf.euphoria.euphorical.UI.GuiMgr;
import cf.euphoria.euphorical.Utils.CmdMgr;
import cf.euphoria.euphorical.Utils.FriendUtils;
import cf.euphoria.euphorical.Mod.Mods;

public class Euphoria
{
    private static Euphoria theClient;
    public Mods theMods;
    public FriendUtils friendUtils;
    public CmdMgr theCmds;
    private GuiMgr guiMgr;
    private GuiKeybindMgr guiKeybindMgr;
    private GuiManagerDisplayScreen guiKeybindDisplay;
    private GuiManagerDisplayScreen guiDisplay;
    public BoolOption noCheatPlus;
    
    static {
        Euphoria.theClient = new Euphoria();
    }
    
    public static Euphoria getEuphoria() {
        return Euphoria.theClient;
    }
    
    public GuiMgr getGuiMgr() {
        if (this.guiMgr == null) {
            (this.guiMgr = new GuiMgr()).setTheme(new SimpleTheme());
            this.getGuiMgr().setup();
        }
        return this.guiMgr;
    }
    
    public GuiManagerDisplayScreen getGui() {
        if (this.guiDisplay == null) {
            this.guiDisplay = new GuiManagerDisplayScreen(this.getGuiMgr());
        }
        return this.guiDisplay;
    }
    
    public GuiKeybindMgr getKeybindGuiMgr() {
        if (this.guiKeybindMgr == null) {
            (this.guiKeybindMgr = new GuiKeybindMgr()).setTheme(new SimpleTheme());
            this.guiKeybindMgr.setup();
        }
        return this.guiKeybindMgr;
    }
    
    public GuiManagerDisplayScreen getKeybindGui() {
        if (this.guiKeybindDisplay == null) {
            this.guiKeybindDisplay = new GuiManagerDisplayScreen(this.getKeybindGuiMgr());
        }
        return this.guiKeybindDisplay;
    }
    
    public static String getBuildName() {
        return "Euphoria (b7)";
    }
    
    public static String getName() {
        return "Euphoria";
    }
    
    public void start() {
        this.noCheatPlus = new BoolOption("NoCheatPlus");
        this.theMods = new Mods();
        this.theMods.getMod(Commands.class).setEnabled(true);
        this.friendUtils = new FriendUtils();
        this.theCmds = new CmdMgr();
        TabGUI.init();
        Display.setTitle(String.valueOf(getBuildName()) + " [1.8]");
    }
}
