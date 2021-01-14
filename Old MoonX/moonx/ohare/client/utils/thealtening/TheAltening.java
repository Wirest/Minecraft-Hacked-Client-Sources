package moonx.ohare.client.utils.thealtening;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import moonx.ohare.client.utils.thealtening.domain.AlteningAlt;
import moonx.ohare.client.utils.thealtening.domain.User;
import moonx.ohare.client.utils.thealtening.utils.Utilities;

public final class TheAltening {
    private final String apiKey;
    private final String website = "http://api.thealtening.com/v1/";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public TheAltening(final String apiKey) {
        this.apiKey = apiKey;
    }

    public User getUser() throws IOException {
        final URLConnection licenseEndpoint = new URL(attach(this.website + "license")).openConnection();
        final String userInfo = new String(Utilities.getInstance().readAllBytes(licenseEndpoint.getInputStream()));
        return gson.fromJson(userInfo, User.class);
    }

    public AlteningAlt generateAccount(User user) throws IOException {
        final URLConnection generateEndpoint = new URL(attach(this.website + "generate")).openConnection();

        final String accountInfo = new String(Utilities.getInstance().readAllBytes(generateEndpoint.getInputStream()));
        if (user.isPremium())
            return gson.fromJson(accountInfo, AlteningAlt.class);
        return null;
    }

    public boolean favoriteAccount(AlteningAlt account) throws IOException {
        final URLConnection favoriteAccount = new URL(attachAccount(this.website + "favorite", account)).openConnection();
        final String info = new String(Utilities.getInstance().readAllBytes(favoriteAccount.getInputStream()));
        return info.isEmpty();
    }

    public boolean privateAccount(AlteningAlt account) throws IOException {
        final URLConnection privateAccount = new URL(attachAccount(this.website + "private", account)).openConnection();
        final String info = new String(Utilities.getInstance().readAllBytes(privateAccount.getInputStream()));
        return info.isEmpty();
    }

    private String attach(final String website) {
        return website + "?token=" + apiKey;
    }

    private String attachAccount(final String website, final AlteningAlt account) {
        return website + "?token=" + apiKey + "&acctoken=" + account.getToken();
    }


}
