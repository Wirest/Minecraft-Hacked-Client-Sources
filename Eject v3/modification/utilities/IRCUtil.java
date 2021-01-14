package modification.utilities;

import modification.interfaces.MCHook;
import modification.main.Modification;
import modification.modules.misc.IRC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public final class IRCUtil
        implements MCHook {
    public final void addUser(String paramString1, String paramString2) {
        try {
            String str = String.format("https://cheating-shop.de/eject-irc/index.php?action=add_user&aal_username=%s&account_username=%s&minecraft_server=%s&minecraft_client=%s&client_rank=%s", new Object[]{Modification.user, paramString1, paramString2, "Eject", Modification.rank});
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            localHttpURLConnection.setRequestProperty("User-Agent", "EJECT-IRC-T5QE7GWJPF");
            localHttpURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localHttpURLConnection.getInputStream()));
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public final void removeUser() {
        try {
            String str = String.format("https://cheating-shop.de/eject-irc/index.php?action=remove_user&aal_username=%s", new Object[]{Modification.user});
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            localHttpURLConnection.setRequestProperty("User-Agent", "EJECT-IRC-T5QE7GWJPF");
            localHttpURLConnection.connect();
            BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localHttpURLConnection.getInputStream()));
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public final String getUserList(String paramString) {
        String str1 = null;
        try {
            String str2 = String.format("https://cheating-shop.de/eject-irc/index.php?action=show_users&minecraft_server=%s", new Object[]{paramString});
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
            localHttpURLConnection.setRequestProperty("User-Agent", "EJECT-IRC-T5QE7GWJPF");
            localHttpURLConnection.connect();
            Scanner localScanner = new Scanner(localHttpURLConnection.getInputStream());
            str1 = localScanner.nextLine();
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return str1;
    }

    public final void checkList() {
        if (MC.getCurrentServerData() != null) {
            IRC.IRC_FRIENDS.clear();
            String str1 = Modification.IRC_UTIL.getUserList(MC.getCurrentServerData().serverIP);
            if ((str1 != null) && (str1.contains(":")) && (str1.contains(";"))) {
                String[] arrayOfString1 = str1.split(";");
                for (String str2 : arrayOfString1) {
                    if (str2.contains(":")) {
                        String[] arrayOfString3 = str2.split(":");
                        if (arrayOfString3.length == 4) {
                            IRC localIRC = (IRC) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("IRC"));
                            char c = 'a';
                            if (((Boolean) localIRC.rankColor.value).booleanValue()) {
                                switch (arrayOfString3[3]) {
                                    case "Admin":
                                        c = '4';
                                        break;
                                    case "Pro":
                                        c = 'd';
                                        break;
                                    case "Dev":
                                        c = 'b';
                                        break;
                                    case "Friend":
                                        c = '6';
                                        break;
                                    case "Staff":
                                        c = 'c';
                                }
                            } else {
                                c = '4';
                            }
                            IRC.IRC_FRIENDS.put(arrayOfString3[1], "§7[§f".concat(arrayOfString3[2]).concat("§7-§f").concat(arrayOfString3[3]).concat("§7] §").concat(Character.toString(c)).concat(arrayOfString3[0]));
                        }
                    }
                }
            }
        }
    }
}




