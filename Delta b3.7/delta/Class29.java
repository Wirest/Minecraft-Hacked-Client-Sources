/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  club.minnced.discord.rpc.DiscordEventHandlers
 *  club.minnced.discord.rpc.DiscordRPC
 *  club.minnced.discord.rpc.DiscordUser
 *  org.apache.logging.log4j.LogManager
 */
package delta;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordUser;
import delta.Class126;
import delta.Class179;
import delta.Class23;
import delta.Class32;
import delta.Class83;
import delta.utils.TimeHelper;
import delta.utils.Wrapper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.UUID;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;

@Deprecated
public class Class29 {
    private File acting$;
    private File updated$;
    private DiscordUser hunter$;

    void _texas() throws IOException {
        String string = Class23._diamond(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/d_id")).replace("\n", "");
        DiscordRPC discordRPC = DiscordRPC.INSTANCE;
        DiscordEventHandlers discordEventHandlers = new DiscordEventHandlers();
        discordEventHandlers.ready = this::_losses;
        discordRPC.Discord_Initialize(string, discordEventHandlers, 185 - 231 + 134 - 37 + -50, "");
        TimeHelper timeHelper = new TimeHelper();
        timeHelper.setLastMS();
        do {
            discordRPC.Discord_RunCallbacks();
            if (timeHelper.hasReached(50L - 88L + 27L + 10011L)) {
                LogManager.getLogger((String)"ELF").info("Overdue.");
                break;
            }
            try {
                Thread.sleep(172L - 281L + 47L - 2L + 1064L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } while (this.hunter$ == null);
        discordRPC.Discord_Shutdown();
    }

    void _louis() {
        Class32.zambia$ = 60 - 103 + 59 - 29 + 14;
        JOptionPane.showMessageDialog(null, "Vous avez \u00e9t\u00e9 Blacklist\u00e9.\nVous ne pouvez plus utiliser les services de nKosmos.\nPour plus d'informations: " + Class23._mustang(), "Delta Client // ELF", 90 - 104 + 31 - 7 + -10);
        Wrapper._occurs();
    }

    private void _losses(DiscordUser discordUser) {
        this.hunter$ = discordUser;
    }

    void _reset() throws IOException {
        if (!this.updated$.exists()) {
            this.updated$.createNewFile();
            Files.setAttribute(this.updated$.toPath(), "dos:hidden", 52 - 95 + 50 + -6, new LinkOption[186 - 346 + 164 + -4]);
        }
        if (!this.acting$.exists() && Class83._disclose() == Class126.baseball$) {
            this.acting$.createNewFile();
            Files.setAttribute(this.updated$.toPath(), "dos:hidden", 96 - 131 + 106 + -70, new LinkOption[69 - 116 + 21 + 26]);
        }
        this._louis();
    }

    void _anybody() throws IOException {
        String string = UUID.randomUUID().toString().replace("-", "").substring(253 - 449 + 36 - 8 + 168, 196 - 213 + 20 + 15);
        int n = 121 - 137 + 74 - 43 + -14;
        try {
            n = Class179._badly(string) ? 1 : 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Wrapper._occurs();
        }
        if (n == 0) {
            this._reset();
        }
    }

    public Class29() throws IOException {
        File file = new File(Class83._oxygen(), "d.x");
        this.updated$ = new File(Class83._oxygen(), ".xdelta");
        if (file.exists()) {
            file.renameTo(this.updated$);
            Files.setAttribute(file.toPath(), "dos:hidden", 253 - 374 + 121 + 1, new LinkOption[223 - 420 + 168 - 20 + 49]);
        }
        this.acting$ = new File("C:\\ProgramData\\msview.lock");
        if (this.updated$.exists()) {
            this._louis();
        }
        if (this.acting$.exists()) {
            this._louis();
        }
        this._anybody();
        this._texas();
        if (this.hunter$ == null) {
            return;
        }
        String string = this.hunter$.userId;
        int n = 177 - 281 + 228 - 228 + 104;
        try {
            n = Class179._badly(string) ? 1 : 0;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Wrapper._occurs();
        }
        if (n != 0) {
            this._reset();
        }
    }
}

