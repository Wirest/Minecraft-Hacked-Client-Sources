// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.account;

import java.util.Iterator;
import java.util.ArrayList;
import me.aristhena.utils.FileUtils;
import java.util.List;
import java.io.File;

public class AccountManager
{
    private static final File ACCOUNT_DIR;
    public static List<Alt> accountList;
    public static final Alt nullAlt;
    
    static {
        ACCOUNT_DIR = FileUtils.getConfigFile("Accounts");
        AccountManager.accountList = new ArrayList<Alt>();
        nullAlt = new Alt("null", "null", "null");
    }
    
    public static void init() {
        load();
        save();
    }
    
    public static void load() {
        final List<String> fileContent = FileUtils.read(AccountManager.ACCOUNT_DIR);
        AccountManager.accountList.clear();
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String email = split[0].replace("\u02a1", "");
                if (split.length == 1) {
                    AccountManager.accountList.add(new Alt(email, "", ""));
                }
                else {
                    final String pass = split[1].replace("\u02a1", "");
                    String name = "";
                    if (split.length > 2) {
                        name = split[2].replace("\u02a1", "");
                        AccountManager.accountList.add(new Alt(email, name, pass));
                    }
                    else {
                        AccountManager.accountList.add(new Alt(email, "", pass));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        FileUtils.write(AccountManager.ACCOUNT_DIR, fileContent, true);
        for (final Alt alt : AccountManager.accountList) {
            final String email = (alt.getEmail() == "") ? "\u02a1" : alt.getEmail();
            final String pass = (alt.getPassword().length() < 1) ? "\u02a1" : alt.getPassword();
            final String name = (alt.getUsername() == "") ? "\u02a1" : alt.getUsername();
            if (name.length() > 0) {
                fileContent.add(String.format("%s:%s:%s", email, pass, name));
            }
            else {
                fileContent.add(String.format("%s:%s", email, pass));
            }
        }
        FileUtils.write(AccountManager.ACCOUNT_DIR, fileContent, true);
    }
    
    public static void addAlt(final Alt alt) {
        AccountManager.accountList.add(alt);
    }
    
    public static void addAlt(final int pos, final Alt alt) {
        AccountManager.accountList.add(pos, alt);
    }
    
    public static Alt getAlt(final String email) {
        for (final Alt account : AccountManager.accountList) {
            if (account.getEmail().equalsIgnoreCase(email)) {
                return account;
            }
        }
        return AccountManager.nullAlt;
    }
    
    public static void removeAlt(final Alt alt) {
        AccountManager.accountList.remove(alt);
    }
}
