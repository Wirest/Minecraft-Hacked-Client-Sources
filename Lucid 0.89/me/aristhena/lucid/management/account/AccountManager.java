/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.account;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.management.account.Alt;
import me.aristhena.lucid.util.FileUtils;

public class AccountManager {
    private static final File ACCOUNT_DIR = FileUtils.getConfigFile("Accounts");
    public static List<Alt> accountList = new ArrayList<Alt>();
    public static final Alt nullAlt = new Alt("null", "null", "null");

    public static void init() {
        AccountManager.loadAccounts();
        AccountManager.saveAccounts();
    }

    public static void loadAccounts() {
        List<String> fileContent = FileUtils.read(ACCOUNT_DIR);
        accountList.clear();
        for (String line : fileContent) {
            try {
                String[] split = line.split(":");
                String email = split[0];
                if (split.length == 1) {
                    accountList.add(new Alt(email, "", ""));
                    continue;
                }
                String pass = split[1];
                String name = "";
                if (split.length > 2) {
                    name = split[0];
                    email = split[1];
                    pass = split[2];
                    accountList.add(new Alt(email, name, pass));
                    continue;
                }
                accountList.add(new Alt(email, "", pass));
                continue;
            }
            catch (Exception split) {
                // empty catch block
            }
        }
    }

    public static void saveAccounts() {
        ArrayList<String> fileContent = new ArrayList<String>();
        FileUtils.write(ACCOUNT_DIR, fileContent, true);
        for (Alt alt : accountList) {
            String email = alt.email;
            String pass = alt.pass;
            String name = alt.name;
            if (name.length() > 0) {
                fileContent.add(String.format("%s:%s:%s", name, email, pass));
                continue;
            }
            fileContent.add(String.format("%s:%s", email, pass));
        }
        FileUtils.write(ACCOUNT_DIR, fileContent, true);
    }

    public static void addAlt(Alt alt) {
        accountList.add(alt);
    }

    public static void addAlt(int pos, Alt alt) {
        accountList.add(pos, alt);
    }

    public static Alt getAlt(String email) {
        for (Alt account : accountList) {
            if (!account.email.equalsIgnoreCase(email)) continue;
            return account;
        }
        return nullAlt;
    }

    public static void removeAlt(Alt alt) {
        accountList.remove(alt);
    }
}

