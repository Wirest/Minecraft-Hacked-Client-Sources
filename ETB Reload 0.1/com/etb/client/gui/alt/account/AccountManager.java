package com.etb.client.gui.alt.account;

import java.io.File;
import java.util.ArrayList;

public class AccountManager {
    private ArrayList<Account> accounts = new ArrayList<>();
    private String alteningToken;
    private Account lastAlt;

    private AltSaving altSaving;

    public AccountManager(File dir) {
        altSaving = new AltSaving(dir);
        altSaving.setup();
    }

    public void setLastAlt(Account alt) {
        lastAlt = alt;
    }

    public Account getLastAlt() {
        return lastAlt;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public AltSaving getAltSaving() {
        return altSaving;
    }

    public String getAlteningToken() {
        return alteningToken;
    }

    public void setAlteningToken(String alteningToken) {
        this.alteningToken = alteningToken;
    }
}
