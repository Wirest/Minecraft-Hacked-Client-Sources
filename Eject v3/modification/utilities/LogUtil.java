package modification.utilities;

import modification.interfaces.MCHook;
import modification.main.Modification;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public final class LogUtil
        implements MCHook {
    public static final String PREFIX = "§8[§f".concat("Eject").concat("§8]§7 ");

    public final void sendConsoleMessage(String paramString) {
        System.out.println(EnumChatFormatting.getTextWithoutFormattingCodes(PREFIX.concat(paramString).concat("!")));
    }

    public final void sendChatMessage(String paramString) {
        MC.thePlayer.addChatMessage(new ChatComponentText(PREFIX.concat(paramString).concat("!")));
    }

    public final String readFromWebsite(String paramString, boolean paramBoolean) {
        StringBuilder localStringBuilder = new StringBuilder();
        try {
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new URL(paramString).openStream()));
            String str;
            while ((str = localBufferedReader.readLine()) != null) {
                localStringBuilder.append(str).append(paramBoolean ? System.lineSeparator() : "");
            }
            localBufferedReader.close();
        } catch (MalformedURLException localMalformedURLException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't connect to URL");
        } catch (IOException localIOException) {
            Modification.LOG_UTIL.sendConsoleMessage("Error: Couldn't read file");
        }
        return localStringBuilder.toString();
    }
}




