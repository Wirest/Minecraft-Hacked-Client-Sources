package moonx.ohare.client.gui.account.system;

import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class AccountManager {
    private ArrayList<Account> accounts = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File altsFile;
    private String alteningKey,lastAlteningAlt;
    private Account lastAlt;
    public AccountManager(File parent) {
        altsFile = new File(parent.toString() + File.separator + "alts.json");
        load();
    }

    public void save() {
        if (altsFile == null) return;

        try {

            // Creates the file if it doesn't exist already.
            if (!altsFile.exists()) altsFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(altsFile);

            printWriter.write(gson.toJson(toJson()));
            printWriter.close();
        } catch (IOException ignored) {
        }
    }

    public void load() {
        if (!altsFile.exists()) {
            save();

            return;
        }

        try {
            JsonObject json = new JsonParser().parse(new FileReader(altsFile)).getAsJsonObject();

            fromJson(json);
        } catch (IOException ignored) {
        }
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        getAccounts().forEach(account -> jsonArray.add(account.toJson()));

        if (alteningKey != null) jsonObject.addProperty("altening", alteningKey);

        if (lastAlteningAlt != null) jsonObject.addProperty("alteningAlt", lastAlteningAlt);

        if (lastAlt != null) jsonObject.add("lastalt", lastAlt.toJson());

        jsonObject.add("accounts", jsonArray);

        return jsonObject;
    }

    public void fromJson(JsonObject json) {
        if (json.has("altening")) {
            alteningKey = json.get("altening").getAsString();
        }
        if (json.has("alteningAlt")) {
            lastAlteningAlt = json.get("alteningAlt").getAsString();
        }
        if (json.has("lastalt")) {
            Account account = new Account();
            account.fromJson(json.get("lastalt").getAsJsonObject());
            lastAlt = account;
        }
        JsonArray jsonArray = json.get("accounts").getAsJsonArray();

        jsonArray.forEach(jsonElement -> {

            JsonObject jsonObject = (JsonObject) jsonElement;

            Account account = new Account();

            account.fromJson(jsonObject);

            getAccounts().add(account);
        });
    }

    public void remove(String username) {
        for (Account account : getAccounts()) {
            if (account.getName().equalsIgnoreCase(username)) {
                getAccounts().remove(account);
            }
        }
    }

    public Account getAccountByEmail(String email) {
        for (Account account : getAccounts()) {
            if (account.getEmail().equalsIgnoreCase(email)) {
                return account;
            }
        }
        return null;
    }

        public String getLastAlteningAlt() {
        return lastAlteningAlt;
    }

    public void setLastAlteningAlt(String lastAlteningAlt) {
        this.lastAlteningAlt = lastAlteningAlt;
    }

    public String getAlteningKey() {
        return alteningKey;
    }

    public void setAlteningKey(String alteningKey) {
        this.alteningKey = alteningKey;
    }

    public Account getLastAlt() {
        return lastAlt;
    }

    public void setLastAlt(Account lastAlt) {
        this.lastAlt = lastAlt;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
