package modification.extenders;

import modification.managers.AccountManager;

public final class Account {
    public final String email;
    public final String password;

    public Account(String paramString1, String paramString2) {
        this.email = paramString1;
        this.password = paramString2;
        AccountManager.ACCOUNTS.add(this);
    }
}




