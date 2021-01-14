package moonx.ohare.client.utils.thealtening.domain;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String username;

    @SerializedName("premium")
    private boolean premium;

    @SerializedName("premium_name")
    private String premiumName;

    @SerializedName("expires")
    private String expiryDate;

    public String getUsername() {
        return username;
    }

    public boolean isPremium() {
        return premium;
    }

    public String getPremiumName() {
        return premiumName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
