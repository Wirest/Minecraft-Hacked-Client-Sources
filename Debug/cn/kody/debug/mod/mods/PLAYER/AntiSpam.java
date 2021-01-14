package cn.kody.debug.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventChatComponent;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class AntiSpam extends Mod
{
    private String lastMessage;
    private int line;
    private int amount;
    
    public AntiSpam() {
        super("AntiSpam", "Anti Spam", Category.PLAYER);
        this.lastMessage = "";
    }
    
    @EventTarget
    public void onChat(EventChatComponent event) {
        List<ChatLine> chatLines = event.getChatLines();
        if (chatLines.isEmpty()) {
            return;
        }
        GuiNewChat chatGUI = this.mc.ingameGUI.getChatGUI();
        List<IChatComponent> splitText = GuiUtilRenderComponents.splitText(event.getComponent(), floor(chatGUI.getChatWidth() / chatGUI.getChatScale()), this.mc.fontRendererObj, false, false);
        int n = 1;
        int n2 = 0;
        int i = chatLines.size() - 1;
        while (i >= 0) {
            String unformattedText = chatLines.get(i).getChatComponent().getUnformattedText();
            Label_0481: {
                if (n2 <= splitText.size() - 1) {
                    String unformattedText2 = splitText.get(n2).getUnformattedText();
                    if (n2 < splitText.size() - 1) {
                        if (unformattedText.equals(unformattedText2)) {
                            ++n2;
                            break Label_0481;
                        }
                        else {
                            n2 = 0;
                            break Label_0481;
                        }
                    }
                    else if (!unformattedText.startsWith(unformattedText2)) {
                        n2 = 0;
                        break Label_0481;
                    }
                    else {
                        if (i > 0 && n2 == splitText.size() - 1) {
                            String substring = (unformattedText + chatLines.get(i - 1).getChatComponent().getUnformattedText()).substring(unformattedText2.length());
                            if (substring.startsWith(" [x") && substring.endsWith("]")) {
                                String substring2 = substring.substring(3, substring.length() - 1);
                                if (isInteger(substring2)) {
                                    n += Integer.parseInt(substring2);
                                    ++n2;
                                    break Label_0481;
                                }
                            }
                        }
                        if (unformattedText.length() == unformattedText2.length()) {
                            ++n;
                        }
                        else {
                            String substring3 = unformattedText.substring(unformattedText2.length());
                            if (!substring3.startsWith(" [x") || !substring3.endsWith("]")) {
                                n2 = 0;
                                break Label_0481;
                            }
                            else {
                                String substring4 = substring3.substring(3, substring3.length() - 1);
                                if (!isInteger(substring4)) {
                                    n2 = 0;
                                    break Label_0481;
                                }
                                else {
                                    n += Integer.parseInt(substring4);
                                }
                            }
                        }
                    }
                }
                int j = i + n2;
                while (j >= i) {
                    chatLines.remove(j);
                    --j;
                }
                n2 = 0;
            }
            --i;
        }
        if (n > 1) {
            event.getComponent().appendText(" [x" + n + "]");
        }
    }
    
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static int floor(float n) {
        int n2 = (int)n;
        int n3;
        if (n < n2) {
            n3 = n2 - 1;
        }
        else {
            n3 = n2;
        }
        return n3;
    }
    
    public static int floor(double n) {
        int n2 = (int)n;
        int n3;
        if (n < n2) {
            n3 = n2 - 1;
        }
        else {
            n3 = n2;
        }
        return n3;
    }
}
