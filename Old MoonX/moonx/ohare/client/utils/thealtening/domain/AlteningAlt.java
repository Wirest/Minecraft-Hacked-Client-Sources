package moonx.ohare.client.utils.thealtening.domain;


import com.google.gson.annotations.SerializedName;

public class AlteningAlt {
    @SerializedName("token")
    private String token;

    @SerializedName(("username"))
    private String username;

    @SerializedName("expires")
    private String expiryDate;

    @SerializedName("limit")
    private boolean isLimitReached;

    @SerializedName("skin")
    private String skinHash;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public boolean isLimitReached() {
        return isLimitReached;
    }

    public String getSkinHash() {
        return skinHash;
    }
}
