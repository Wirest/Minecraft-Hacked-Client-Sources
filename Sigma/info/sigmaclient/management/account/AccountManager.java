package info.sigmaclient.management.account;

import java.io.File;

import info.sigmaclient.management.AbstractManager;
import info.sigmaclient.Client;
import info.sigmaclient.management.SubFolder;

public class AccountManager<E extends Account> extends AbstractManager<Account> {
    private Account current;

    public AccountManager(Class<Account> clazz) {
        super(clazz, 0);
    }

    @Override
    public void setup() {
        File accountDir = getAccountDir();
        if (accountDir.isDirectory()) {
            File[] accountFiles = accountDir.listFiles();
            int accounts = accountFiles.length;
            int i = 0;
            // Resize the array to accommodate for the incoming accounts.
            reset(accounts);
            for (File accountFile : accountFiles) {
                // The name of the file is the Account's ID. Loading the account
                // with just the ID will let it decrypt the information in the
                // encrypted file.
                Account account = new Account(accountFile.getName().substring(0, accountFile.getName().indexOf(".")));
                account.load();
                array[i] = account;
                i++;
            }
        }
    }

    public void reload() {
        for (Account account : array) {
            if (account == null) {
                continue;
            }
            File accountFile = account.getFile();
            if (accountFile.exists()) {
                account.load();
            }
        }
    }

    /**
     * Returns the current Account the user is on.
     *
     * @return
     */
    public Account getCurrent() {
        if (current == null) {
            Account account = new Account("ERROR", "ERROR");
            account.setPremium(false);
            current = account;
        }
        return current;
    }

    public void setCurrent(Account account) {
        this.current = account;
    }

    @Override
    public void remove(Account account) {
        File accountFile = account.getFile();
        if (accountFile.exists()) {
            accountFile.delete();
        }
        super.remove(account);
    }

    /**
     * Returns the account with the given username.
     *
     * @param username
     * @return
     */
    public Account getAccount(String username) {
        if (array.length == 0) {
            return null;
        }
        for (Account account : array) {
            if (account != null && account.getDisplay().equals(username)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Returns the directory that accounts are saved in.
     *
     * @return
     */
    private File getAccountDir() {
        File accountDir = new File(Client.getDataDir() + File.separator + SubFolder.Alt.getFolderName());
        if (!accountDir.exists()) {
            // Create the account directory if it does not exist.
            accountDir.mkdirs();
        }
        return accountDir;
    }
}
