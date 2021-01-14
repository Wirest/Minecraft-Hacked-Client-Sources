package tv.twitch;

public class AuthToken {
    public String data;

    public boolean getIsValid() {
        return (this.data != null) && (this.data.length() > 0);
    }
}




