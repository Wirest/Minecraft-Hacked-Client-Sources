package info.sigmaclient.management.account;

import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import com.google.gson.annotations.Expose;

import info.sigmaclient.util.security.Crypto;
import info.sigmaclient.management.account.relation.UserStatus;
import info.sigmaclient.management.Saveable;
import info.sigmaclient.management.SubFolder;

public class Account extends Saveable {
    @Expose
    private String id, capeURL;
    @Expose
    private boolean premium;
    @Expose
    private HashMap<String, UserStatus> relationships = new HashMap<String, UserStatus>();
    @Expose
    private HashMap<String, String> aliases = new HashMap<String, String>();
    private String loginUser, loginPass, display;
    @Expose
    private String cryptoUser, cryptoPass, cryptoDisplay;

    /**
     * Constructor for loading from text AES encrypted text file.
     *
     * @param id
     */
    public Account(String id) {
        this.id = id;
        setFolderType(SubFolder.Alt);
        load();
    }

    /**
     * Constructor for initially creating the alt.
     *
     * @param loginUser
     * @param loginPass
     */
    public Account(String loginUser, String loginPass) {
        this.loginUser = loginUser;
        this.loginPass = loginPass;
        display = loginUser;
        premium = true;
        id = (loginUser.contains("@")) ? loginUser.substring(0, loginUser.indexOf("@")) : loginUser;
        updateCrypto();
        setFolderType(SubFolder.Alt);
    }

    /**
     * Loads non-saved values by running the encrypted saved values back through
     * AES.
     */
    @Override
    public Saveable load() {
        Account loaded = (Account) super.load();
        try {
            id = loaded.getID();
            loginUser = Crypto.decrypt(getSecret(), loaded.getCryptoUser());
            loginPass = Crypto.decrypt(getSecret(), loaded.getCryptoPass());
            display = Crypto.decrypt(getSecret(), loaded.getCryptoDisplay());
            premium = loaded.premium;
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateCrypto();
        return this;
    }

    private SecretKeySpec getSecret() {
        byte[] secret = Crypto.getUserKey(16);
        return new SecretKeySpec(secret, 0, secret.length, "AES");
    }

    public String getUser() {
        return loginUser;
    }

    public String getPass() {
        return loginPass;
    }

    public String getDisplay() {
        return display;
    }

    public String getID() {
        return id;
    }

    public String getCapeURL() {
        return capeURL;
    }

    public boolean hasCape() {
        return capeURL != null && capeURL.length() > 7;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return premium;
    }

    public HashMap<String, String> getAliases() {
        return aliases;
    }

    public HashMap<String, UserStatus> getRelationships() {
        return relationships;
    }

    /**
     * Updates a user's alias.
     *
     * @param username
     * @param newName
     */
    public void setAlias(String username, String newName) {
        if (aliases.containsKey(username)) {
            aliases.remove(username);
        }
        aliases.put(username, newName);
    }

    /**
     * Returns text with aliased names replaced.
     *
     * @param username
     * @param text
     * @return
     */
    public String getAliasedText(String username, String text) {
        return text.replace(username, aliases.get(username));
    }

    public UserStatus getRelation(String username) {
        if (!relationships.containsKey(username)) {
            return relationships.get(username);
        }
        return null;
    }

    public void setRelationships(HashMap<String, UserStatus> relationships) {
        this.relationships = relationships;
    }

    public void setPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public String getCryptoUser() {
        return cryptoUser;
    }

    public String getCryptoPass() {
        return cryptoPass;
    }

    public String getCryptoDisplay() {
        return cryptoDisplay;
    }

    public void setUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Populates the encrypted varients of the plaintext values.
     */
    public void updateCrypto() {
        try {
            cryptoUser = Crypto.encrypt(getSecret(), loginUser);
            cryptoPass = Crypto.encrypt(getSecret(), loginPass);
            cryptoDisplay = Crypto.encrypt(getSecret(), display);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileName() {
        return id + ".json";
    }
}
