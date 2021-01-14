package cedo.altmanager;

import cedo.util.altening.SSLController;
import cedo.util.altening.TheAlteningAuthentication;
import cedo.util.altening.service.AlteningServiceType;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Alt {

    public static Minecraft mc = Minecraft.getMinecraft();
    private final SSLController ssl = new SSLController();
    private final TheAlteningAuthentication alteningAuthentication = TheAlteningAuthentication.mojang();
    @Expose
    @SerializedName("uuid")
    public String uuid;
    @Expose
    @SerializedName("username")
    public String username;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("password")
    public String password;
    @Expose
    @SerializedName("names")
    public List<String> namemc = new ArrayList<>();
    public int stage = -1, skinChecks;
    public boolean checkedSkin;

    public Alt() {
        stage = -1;
    }

    public Alt(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void login() {
        stage = 0;
        if (this.password.equals("")) {
            mc.session = new Session(this.username, "", "", "mojang");
            username = email;
            stage = 2;
            return;
        }
        Session auth = this.createSession(this.email, this.password);
        if (auth == null) {
            stage = 1;
        } else {
            mc.session = auth;
            uuid = auth.getPlayerID();
            username = auth.getUsername();
            stage = 2;
            //this.pullNameMC();
        }
    }

    public void loginAsync() {
        new Thread(() -> {
            login();

            new Thread(() -> {
                try {
                    Files.write(BetterAltManager.altsFile.toPath(), new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(BetterAltManager.alts.toArray(new Alt[0])).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }).start();
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
                .createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            if (username.endsWith("@alt.com")) {
                ssl.disableCertificateValidation();
                alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
            } else if (alteningAuthentication.getService() == AlteningServiceType.THEALTENING) {
                ssl.enableCertificateValidation();
                alteningAuthentication.updateService(AlteningServiceType.MOJANG);
            }
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    public void pullNameMC() {
        //System.out.println("TEST");
        if (uuid != null && !uuid.isEmpty() && namemc != null) {
            String url = String.format("https://api.mojang.com/user/profiles/%s/names", uuid);
            new Thread(() -> {
                try {
                    System.out.println("TEST");
                    HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                    con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line, buffer = "";
                    if ((line = reader.readLine()) != null) {
                        buffer += line;
                    }
                    //System.out.println(buffer + " " + con.getResponseCode());
                    String[] parts = buffer.split("\"name\": \"");
                    for (String part : parts) {
                        String name = part.split("\"")[0];
                        //System.out.println(name);
                        namemc.add(name);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
