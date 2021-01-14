package cn.kody.debug.ui.font;

import cn.kody.debug.ui.font.UnicodeFontRenderer;
import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.LanguageManager;

public class FontManager {
    public UnicodeFontRenderer tahoma10;
    public UnicodeFontRenderer tahoma11;
    public UnicodeFontRenderer tahoma12;
    public UnicodeFontRenderer tahoma13;
    public UnicodeFontRenderer tahoma14;
    public UnicodeFontRenderer tahoma15;
    public UnicodeFontRenderer tahoma16;
    public UnicodeFontRenderer tahoma17;
    public UnicodeFontRenderer tahoma18;
    public UnicodeFontRenderer tahoma19;
    public UnicodeFontRenderer tahoma20;
    public UnicodeFontRenderer tahoma21;
    public UnicodeFontRenderer tahoma22;
    public UnicodeFontRenderer tahoma23;
    public UnicodeFontRenderer tahoma24;
    public UnicodeFontRenderer tahoma25;
    public UnicodeFontRenderer tahoma30;
    public UnicodeFontRenderer tahoma35;
    public UnicodeFontRenderer tahoma40;
    public UnicodeFontRenderer tahoma45;
    public UnicodeFontRenderer tahoma50;
    public UnicodeFontRenderer tahoma70;
    private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
    public UnicodeFontRenderer icon10;
    public UnicodeFontRenderer icon11;
    public UnicodeFontRenderer icon12;
    public UnicodeFontRenderer icon13;
    public UnicodeFontRenderer icon14;
    public UnicodeFontRenderer icon15;
    public UnicodeFontRenderer icon16;
    public UnicodeFontRenderer icon17;
    public UnicodeFontRenderer icon18;
    public UnicodeFontRenderer icon19;
    public UnicodeFontRenderer icon20;
    public UnicodeFontRenderer icon21;
    public UnicodeFontRenderer icon22;
    public UnicodeFontRenderer icon23;
    public UnicodeFontRenderer icon24;
    public UnicodeFontRenderer icon25;
    public UnicodeFontRenderer icon30;
    public UnicodeFontRenderer icon35;
    public UnicodeFontRenderer icon40;
    public UnicodeFontRenderer icon45;
    public UnicodeFontRenderer icon50;
    public UnicodeFontRenderer icon60;
    public UnicodeFontRenderer icon70;
    public UnicodeFontRenderer icon80;
    public UnicodeFontRenderer icon90;
    public UnicodeFontRenderer icon100;
    public UnicodeFontRenderer icon110;
    public UnicodeFontRenderer wqy16;
    
    public FontManager() {
	  this.icon10 = this.getFont("icon", 10.0f, -1, -1, false, false);
	  this.icon11 = this.getFont("icon", 11.0f, -1, -1, false, false);
	  this.icon12 = this.getFont("icon", 12.0f, -1, -1, false, false);
	  this.icon13 = this.getFont("icon", 13.0f, -1, -1, false, false);
	  this.icon14 = this.getFont("icon", 14.0f, -1, -1, false, false);
	  this.icon15 = this.getFont("icon", 15.0f, -1, -1, false, false);
	  this.icon16 = this.getFont("icon", 16.0f, -1, -1, false, false);
	  this.icon17 = this.getFont("icon", 17.0f, -1, -1, false, false);
	  this.icon18 = this.getFont("icon", 18.0f, -1, -1, false, false);
	  this.icon19 = this.getFont("icon", 19.0f, -1, -1, false, false);
	  this.icon20 = this.getFont("icon", 20.0f, -1, -1, false, false);
	  this.icon21 = this.getFont("icon", 21.0f, -1, -1, false, false);
	  this.icon22 = this.getFont("icon", 22.0f, -1, -1, false, false);
	  this.icon23 = this.getFont("icon", 23.0f, -1, -1, false, false);
	  this.icon24 = this.getFont("icon", 24.0f, -1, -1, false, false);
	  this.icon25 = this.getFont("icon", 25.0f, -1, -1, false, false);
	  this.icon30 = this.getFont("icon", 30.0f, -1, -1, false, false);
	  this.icon35 = this.getFont("icon", 35.0f, -1, -1, false, false);
	  this.icon40 = this.getFont("icon", 40.0f, -1, -1, false, false);
	  this.icon45 = this.getFont("icon", 45.0f, -1, -1, false, false);
	  this.icon50 = this.getFont("icon", 50.0f, -1, -1, false, false);
	  this.icon60 = this.getFont("icon", 60.0f, -1, -1, false, false);
	  this.icon70 = this.getFont("icon", 70.0f, -1, -1, false, false);
	  this.icon80 = this.getFont("icon", 80.0f, -1, -1, false, false);
	  this.icon90 = this.getFont("icon", 90.0f, -1, -1, false, false);
	  this.icon100 = this.getFont("icon", 100.0f, -1, -1, false, false);
	  this.icon110 = this.getFont("icon", 110.0f, -1, -1, false, false);
	  this.wqy16 = this.getFont("wqy_zenhei", 16.0f, -1, -1, true, false);
	  this.tahoma10 = this.getFont("tahoma", 10.0f, -1, -1, false, false);
	    this.tahoma11 = this.getFont("tahoma", 11.0f, -1, -1, false, false);
	    this.tahoma12 = this.getFont("tahoma", 12.0f, -1, -1, false, false);
	    this.tahoma13 = this.getFont("tahoma", 13.0f, -1, -1, false, false);
	    this.tahoma14 = this.getFont("tahoma", 14.0f, -1, -1, false, false);
	    this.tahoma15 = this.getFont("tahoma", 15.0f, -1, -1, false, false);
	    this.tahoma16 = this.getFont("tahoma", 16.0f, -1, -1, false, false);
	    this.tahoma17 = this.getFont("tahoma", 17.0f, -1, -1, false, false);
	    this.tahoma18 = this.getFont("tahoma", 18.0f, -1, -1, false, false);
	    this.tahoma19 = this.getFont("tahoma", 19.0f, -1, -1, false, false);
	    this.tahoma20 = this.getFont("tahoma", 20.0f, -1, -1, false, false);
	    this.tahoma21 = this.getFont("tahoma", 21.0f, -1, -1, false, false);
	    this.tahoma22 = this.getFont("tahoma", 22.0f, -1, -1, false, false);
	    this.tahoma23 = this.getFont("tahoma", 23.0f, -1, -1, false, false);
	    this.tahoma24 = this.getFont("tahoma", 24.0f, -1, -1, false, false);
	    this.tahoma25 = this.getFont("tahoma", 25.0f, -1, -1, false, false);
	    this.tahoma30 = this.getFont("tahoma", 30.0f, -1, -1, false, false);
	    this.tahoma35 = this.getFont("tahoma", 35.0f, -1, -1, false, false);
	    this.tahoma40 = this.getFont("tahoma", 40.0f, -1, -1, false, false);
	    this.tahoma45 = this.getFont("tahoma", 45.0f, -1, -1, false, false);
	    this.tahoma50 = this.getFont("tahoma", 50.0f, -1, -1, false, false);
	    this.tahoma70 = this.getFont("tahoma", 70.0f, -1, -1, false, false);
    }	
    
    public UnicodeFontRenderer getFont(final String s, final float n, final int n2, final int n3, final boolean b, final boolean b2) {
        UnicodeFontRenderer UnicodeFontRenderer = null;
        try {
            if (this.fonts.containsKey(s) && this.fonts.get(s).containsKey(n)) {
                return this.fonts.get(s).get(n);
            }
            final Class<? extends FontManager> class1 = this.getClass();
            final StringBuilder append = new StringBuilder().append("/assets/minecraft/client/fonts/").append(s);
            String s2;
            if (b2) {
                s2 = ".otf";
            }
            else {
                s2 = ".ttf";
            }
            UnicodeFontRenderer = new UnicodeFontRenderer(Font.createFont(0, class1.getResourceAsStream(append.append(s2).toString())).deriveFont(n), n, n2, n3, b);
            UnicodeFontRenderer.setUnicodeFlag(true);
            UnicodeFontRenderer.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            final HashMap<Float, UnicodeFontRenderer> hashMap = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(s)) {
                hashMap.putAll(this.fonts.get(s));
            }
            hashMap.put(n, UnicodeFontRenderer);
            this.fonts.put(s, hashMap);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return UnicodeFontRenderer;
    }
    
}

