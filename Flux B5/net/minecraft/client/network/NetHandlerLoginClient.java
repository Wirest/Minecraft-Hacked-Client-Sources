package net.minecraft.client.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.m0jang.crystal.UI.MCLeaks;

public class NetHandlerLoginClient implements INetHandlerLoginClient
{
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft field_147394_b;
    private final GuiScreen field_147395_c;
    private final NetworkManager field_147393_d;
    private GameProfile field_175091_e;
    private static final String __OBFID = "CL_00000876";

    public NetHandlerLoginClient(NetworkManager p_i45059_1_, Minecraft mcIn, GuiScreen p_i45059_3_)
    {
        this.field_147393_d = p_i45059_1_;
        this.field_147394_b = mcIn;
        this.field_147395_c = p_i45059_3_;
    }

    public void handleEncryptionRequest(S01PacketEncryptionRequest packetIn) {
        final SecretKey var2 = CryptManager.createNewSharedKey();
        String var3 = packetIn.func_149609_c();
        PublicKey var4 = packetIn.func_149608_d();
        String var5 = (new BigInteger(CryptManager.getServerIdHash(var3, var4, var2))).toString(16);
        if (MCLeaks.isAltActive()) {
           System.out.println("trying to log in with mcleaks...");
           String mcLeaksSession = MCLeaks.getMCLeaksSession();
           System.out.println("loaded session " + mcLeaksSession);
           String mcName = MCLeaks.getMCName();
           System.out.println("loaded name " + mcName);
           
//           String server = String.valueOf(((InetSocketAddress)this.field_147393_d.getRemoteAddress()).getHostName()) + ":" + ((InetSocketAddress)this.field_147393_d.getRemoteAddress()).getPort();
          
//           System.out.println("loaded server " + server);

           try {
              String jsonBody = "{\"session\":\"" + mcLeaksSession + "\",\"mcname\":\"" + mcName + "\",\"serverhash\":\"" + var5 + "\",\"server\":\""/* + server */+ "\"}";
              System.out.println("request: " + jsonBody);
              HttpURLConnection connection = (HttpURLConnection)(new URL("http://auth.mcleaks.net/v1/joinserver")).openConnection();
              System.out.println("requesting...");
              connection.setConnectTimeout(10000);
              connection.setReadTimeout(10000);
              connection.setRequestMethod("POST");
              connection.setDoOutput(true);
              DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
              outputStream.write(jsonBody.getBytes("UTF-8"));
              outputStream.flush();
              outputStream.close();
              BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              StringBuilder out = new StringBuilder();

              String line;
              while((line = reader.readLine()) != null) {
                 out.append(line);
              }

              reader.close();
              System.out.println("reading respond json...");
              JsonElement jsonElement = (JsonElement)(new Gson()).fromJson(out.toString(), JsonElement.class);
              System.out.println("done, result ( dont include - ): -" + jsonElement + "- checking for errors in the reply...");
              if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")) {
                 System.out.print("bad reply");
                 this.field_147393_d.closeChannel(new ChatComponentTranslation("MCLeaks Logging Error, try an other token", new Object[0]));
                 return;
              }

              if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                 String errorMessage = "Received success=false from MCLeaks API";
                 if (jsonElement.getAsJsonObject().has("errorMessage")) {
                    errorMessage = jsonElement.getAsJsonObject().get("errorMessage").getAsString();
                 }

                 System.out.print("no success " + errorMessage);
                 this.field_147393_d.closeChannel(new ChatComponentTranslation("MCLeaks Logging Error, try an other token", new Object[0]));
                 return;
              }
           } catch (Exception var20) {
              System.out.println("Error whilst contacting MCLeaks API: " + var20.toString());
              this.field_147393_d.closeChannel(new ChatComponentTranslation("MCLeaks Logging Error, try an other token", new Object[0]));
              return;
           }
        }

        if (!MCLeaks.isAltActive()) {
           try {
              this.func_147391_c().joinServer(this.field_147394_b.getSession().getProfile(), this.field_147394_b.getSession().getToken(), var5);
           } catch (AuthenticationUnavailableException var17) {
              this.field_147393_d.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[]{new ChatComponentTranslation("disconnect.loginFailedInfo.serversUnavailable", new Object[0])}));
              return;
           } catch (InvalidCredentialsException var18) {
              this.field_147393_d.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[]{new ChatComponentTranslation("disconnect.loginFailedInfo.invalidSession", new Object[0])}));
              return;
           } catch (AuthenticationException var19) {
              this.field_147393_d.closeChannel(new ChatComponentTranslation("disconnect.loginFailedInfo", new Object[]{var19.getMessage()}));
              return;
           }
        }

        this.field_147393_d.sendPacket(new C01PacketEncryptionResponse(var2, var4, packetIn.func_149607_e()), new GenericFutureListener() {
           
      	  public void operationComplete(Future p_operationComplete_1_) {
              NetHandlerLoginClient.this.field_147393_d.enableEncryption(var2);
           }
        });
     }

    private MinecraftSessionService func_147391_c()
    {
        return this.field_147394_b.getSessionService();
    }

    public void handleLoginSuccess(S02PacketLoginSuccess packetIn)
    {
        this.field_175091_e = packetIn.func_179730_a();
        this.field_147393_d.setConnectionState(EnumConnectionState.PLAY);
        this.field_147393_d.setNetHandler(new NetHandlerPlayClient(this.field_147394_b, this.field_147395_c, this.field_147393_d, this.field_175091_e));
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(IChatComponent reason)
    {
        this.field_147394_b.displayGuiScreen(new GuiDisconnected(this.field_147395_c, "connect.failed", reason));
    }

    public void handleDisconnect(S00PacketDisconnect packetIn)
    {
        this.field_147393_d.closeChannel(packetIn.func_149603_c());
    }

    public void func_180464_a(S03PacketEnableCompression p_180464_1_)
    {
        if (!this.field_147393_d.isLocalChannel())
        {
            this.field_147393_d.setCompressionTreshold(p_180464_1_.func_179731_a());
        }
    }
}
