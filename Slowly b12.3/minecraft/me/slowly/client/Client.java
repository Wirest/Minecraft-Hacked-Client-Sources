/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.Display
 */
package me.slowly.client;

import me.slowly.client.irc.IRCChat;
import me.slowly.client.irc.ui.UICheckBoxes;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.world.IRC;
import me.slowly.client.ui.clickgui.Komplexe.UIKomplexe;
import me.slowly.client.ui.clickgui.UIClick;
import me.slowly.client.ui.clickgui.UITaskBar;
import me.slowly.client.ui.clickgui.newclickgui.ClickGui;
import me.slowly.client.ui.clickgui.other.UIOther;
import me.slowly.client.ui.clickgui.solstice.UISolstice;
import me.slowly.client.ui.clickgui.xave.UIXave;
import me.slowly.client.ui.hudcustomizer.UIHUDCustomizer;
import me.slowly.client.ui.tabgui.TabGui;
import me.slowly.client.util.FileUtil;
import me.slowly.client.util.command.CommandManager;
import me.slowly.client.util.fontmanager.FontManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.lwjgl.opengl.Display;

public class Client {
    public static final String CLIENT_NAME = "NMSL Client";
    public static final String CLIENT_VERSION = "b12.3";
    private static Client instance;
    private ModManager modManager;
    private FontManager fontManager;
    private ClickGui uiClick;
    private UIClick clickface;
    private TabGui tabGui;
    private CommandManager commandManager;
    private FileUtil fileUtil;
    private UIKomplexe kompl;
    private UIXave xave;
    private UIOther other;
    private UISolstice solstice;
    public static UIHUDCustomizer uiCustomizer;
    private IRCChat ircChat;
    private UITaskBar bar;

    public Client() {
        instance = this;
        this.bar = new UITaskBar();
        this.fontManager = new FontManager();
        this.modManager = new ModManager();
        this.uiClick = new ClickGui();
        this.clickface = new UIClick();
        this.xave = new UIXave();
        this.kompl = new UIKomplexe();
        this.other = new UIOther();
        this.solstice = new UISolstice();
        this.tabGui = new TabGui();
        this.commandManager = new CommandManager();
        this.fileUtil = new FileUtil();
        uiCustomizer = new UIHUDCustomizer();
        this.ircChat = new IRCChat();
        new me.slowly.client.irc.ui.UICheckBoxes();
        Display.setTitle((String)"Cyka Blayt Client! | " + title);
    }

    public UITaskBar getTaskBar() {
        return this.bar;
    }
    String title = sendGet("https://api.lwl12.com/hitokoto/v1/get?charset=utf-8") ;
    public static String sendGet(final String url) {
	    String result = "";
       try {
    	    final String urlNameString = url;
    	    final URL realurl = new URL(urlNameString);
	        HttpURLConnection httpUrlConn = (HttpURLConnection) realurl.openConnection();
	        httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream input = httpUrlConn.getInputStream();
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            BufferedReader br = new BufferedReader(read);
            String data = br.readLine();
            while(data!=null) {
                result = String.valueOf(result) + data + "\n";
                data=br.readLine();
                }
       br.close();  
       read.close();  
       input.close();  
       httpUrlConn.disconnect();  
   } catch (MalformedURLException e) {
       e.printStackTrace();
   } catch (IOException e) {
       e.printStackTrace();
   }
	    return result;
	}


    public UISolstice getSolstice() {
        return this.solstice;
    }

    public UIOther getUIOther() {
        return this.other;
    }

    public UIKomplexe getKomplexe() {
        return this.kompl;
    }

    public static Client getInstance() {
        return instance;
    }

    public ModManager getModManager() {
        return this.modManager;
    }

    public FontManager getFontManager() {
        return this.fontManager;
    }

    public ClickGui getUIClick() {
        return this.uiClick;
    }

    public TabGui getTabGui() {
        return this.tabGui;
    }

    public UIClick getClickInterface() {
        return this.clickface;
    }

    public FileUtil getFileUtil() {
        return this.fileUtil;
    }

    public UIXave getXave() {
        return this.xave;
    }

    public IRCChat getIrcChat() {
        return this.ircChat;
    }

    public class ClientImages {
        private static final String PREFIX_DIR = "slowly/";
        public static final String CLIENT_BACKGROUND = "slowly/wallpaper.jpg";
        public static final String SETTINGS_ICON = "slowly/icon/settings_icon.png";
    }

}

