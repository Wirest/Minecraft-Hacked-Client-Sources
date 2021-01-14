package modification.utilities;

import modification.extenders.Account;
import modification.files.AccountFile;
import modification.main.Modification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class PrixUtil {
    public final String getInformation(String paramString1, String paramString2) {
        String str1 = Modification.LOG_UTIL.readFromWebsite("https://cheating-shop.de/datas/client/?action=login&username=".concat(paramString1).concat("&password=").concat(paramString2), false);
        String str2 = "§4An unknown error has occured";
        switch (str1) {
            case "success":
                String str4 = "";
                try {
                    HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(String.format("https://cheating-shop.de/datas/accounts/?action=generate&username=%s&password=%s&type=Minecraft", new Object[]{paramString1, paramString2})).openConnection();
                    localHttpURLConnection.setRequestProperty("User-Agent", "EJECT");
                    localHttpURLConnection.connect();
                    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localHttpURLConnection.getInputStream()));
                    str4 = localBufferedReader.readLine();
                } catch (IOException localIOException) {
                    localIOException.printStackTrace();
                }
                switch (str4) {
                    case "":
                        str2 = "§4Error: Accounts are out of stock";
                        break;
                    case "no-access":
                        str2 = "§4Error: You haven't bought the Generator yet";
                        break;
                    case "expired":
                        str2 = "§4Error: Your sub has been expired";
                        break;
                    case "limit-reached":
                        str2 = "§4Error: Daily limit reached";
                        break;
                    default:
                        if (!str4.isEmpty()) {
                            new Account(str4, "Prix-Gen");
                            Modification.FILE_MANAGER.update(AccountFile.class);
                            str2 = "§aSuccessfully generated account";
                        }
                        break;
                }
                break;
            case "wrong":
                str2 = "§4Error: Wrong login data";
                break;
            case "not-verified":
                str2 = "§4Error: You haven't been verified yet";
                break;
            case "missing":
                str2 = "§4Error: You haven't entered your login data";
                break;
            case "banned":
                str2 = "§4Error: You are banned from Prix-Gen | Be careful or you'll get banned from Eject fucking dumbass";
        }
        return str2;
    }
}




