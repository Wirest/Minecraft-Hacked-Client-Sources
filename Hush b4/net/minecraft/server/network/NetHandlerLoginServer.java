// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import com.google.common.base.Charsets;
import java.security.PrivateKey;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.util.UUID;
import java.math.BigInteger;
import net.minecraft.util.CryptManager;
import java.util.Arrays;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.login.server.S03PacketEnableCompression;
import net.minecraft.network.Packet;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.crypto.SecretKey;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.ITickable;
import net.minecraft.network.login.INetHandlerLoginServer;

public class NetHandlerLoginServer implements INetHandlerLoginServer, ITickable
{
    private static final AtomicInteger AUTHENTICATOR_THREAD_ID;
    private static final Logger logger;
    private static final Random RANDOM;
    private final byte[] verifyToken;
    private final MinecraftServer server;
    public final NetworkManager networkManager;
    private LoginState currentLoginState;
    private int connectionTimer;
    private GameProfile loginGameProfile;
    private String serverId;
    private SecretKey secretKey;
    private EntityPlayerMP field_181025_l;
    
    static {
        AUTHENTICATOR_THREAD_ID = new AtomicInteger(0);
        logger = LogManager.getLogger();
        RANDOM = new Random();
    }
    
    public NetHandlerLoginServer(final MinecraftServer p_i45298_1_, final NetworkManager p_i45298_2_) {
        this.verifyToken = new byte[4];
        this.currentLoginState = LoginState.HELLO;
        this.serverId = "";
        this.server = p_i45298_1_;
        this.networkManager = p_i45298_2_;
        NetHandlerLoginServer.RANDOM.nextBytes(this.verifyToken);
    }
    
    @Override
    public void update() {
        if (this.currentLoginState == LoginState.READY_TO_ACCEPT) {
            this.tryAcceptPlayer();
        }
        else if (this.currentLoginState == LoginState.DELAY_ACCEPT) {
            final EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityplayermp == null) {
                this.currentLoginState = LoginState.READY_TO_ACCEPT;
                this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.field_181025_l);
                this.field_181025_l = null;
            }
        }
        if (this.connectionTimer++ == 600) {
            this.closeConnection("Took too long to log in");
        }
    }
    
    public void closeConnection(final String reason) {
        try {
            NetHandlerLoginServer.logger.info("Disconnecting " + this.getConnectionInfo() + ": " + reason);
            final ChatComponentText chatcomponenttext = new ChatComponentText(reason);
            this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
            this.networkManager.closeChannel(chatcomponenttext);
        }
        catch (Exception exception) {
            NetHandlerLoginServer.logger.error("Error whilst disconnecting player", exception);
        }
    }
    
    public void tryAcceptPlayer() {
        if (!this.loginGameProfile.isComplete()) {
            this.loginGameProfile = this.getOfflineProfile(this.loginGameProfile);
        }
        final String s = this.server.getConfigurationManager().allowUserToConnect(this.networkManager.getRemoteAddress(), this.loginGameProfile);
        if (s != null) {
            this.closeConnection(s);
        }
        else {
            this.currentLoginState = LoginState.ACCEPTED;
            if (this.server.getNetworkCompressionTreshold() >= 0 && !this.networkManager.isLocalChannel()) {
                this.networkManager.sendPacket(new S03PacketEnableCompression(this.server.getNetworkCompressionTreshold()), new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture p_operationComplete_1_) throws Exception {
                        NetHandlerLoginServer.this.networkManager.setCompressionTreshold(NetHandlerLoginServer.this.server.getNetworkCompressionTreshold());
                    }
                }, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
            }
            this.networkManager.sendPacket(new S02PacketLoginSuccess(this.loginGameProfile));
            final EntityPlayerMP entityplayermp = this.server.getConfigurationManager().getPlayerByUUID(this.loginGameProfile.getId());
            if (entityplayermp != null) {
                this.currentLoginState = LoginState.DELAY_ACCEPT;
                this.field_181025_l = this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile);
            }
            else {
                this.server.getConfigurationManager().initializeConnectionToPlayer(this.networkManager, this.server.getConfigurationManager().createPlayerForUser(this.loginGameProfile));
            }
        }
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
        NetHandlerLoginServer.logger.info(String.valueOf(this.getConnectionInfo()) + " lost connection: " + reason.getUnformattedText());
    }
    
    public String getConnectionInfo() {
        return (this.loginGameProfile != null) ? (String.valueOf(this.loginGameProfile.toString()) + " (" + this.networkManager.getRemoteAddress().toString() + ")") : String.valueOf(this.networkManager.getRemoteAddress());
    }
    
    @Override
    public void processLoginStart(final C00PacketLoginStart packetIn) {
        Validate.validState(this.currentLoginState == LoginState.HELLO, "Unexpected hello packet", new Object[0]);
        this.loginGameProfile = packetIn.getProfile();
        if (this.server.isServerInOnlineMode() && !this.networkManager.isLocalChannel()) {
            this.currentLoginState = LoginState.KEY;
            this.networkManager.sendPacket(new S01PacketEncryptionRequest(this.serverId, this.server.getKeyPair().getPublic(), this.verifyToken));
        }
        else {
            this.currentLoginState = LoginState.READY_TO_ACCEPT;
        }
    }
    
    @Override
    public void processEncryptionResponse(final C01PacketEncryptionResponse packetIn) {
        Validate.validState(this.currentLoginState == LoginState.KEY, "Unexpected key packet", new Object[0]);
        final PrivateKey privatekey = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.verifyToken, packetIn.getVerifyToken(privatekey))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = packetIn.getSecretKey(privatekey);
        this.currentLoginState = LoginState.AUTHENTICATING;
        this.networkManager.enableEncryption(this.secretKey);
        new Thread("User Authenticator #" + NetHandlerLoginServer.AUTHENTICATOR_THREAD_ID.incrementAndGet()) {
            @Override
            public void run() {
                final GameProfile gameprofile = NetHandlerLoginServer.this.loginGameProfile;
                try {
                    final String s = new BigInteger(CryptManager.getServerIdHash(NetHandlerLoginServer.this.serverId, NetHandlerLoginServer.this.server.getKeyPair().getPublic(), NetHandlerLoginServer.this.secretKey)).toString(16);
                    NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.server.getMinecraftSessionService().hasJoinedServer(new GameProfile(null, gameprofile.getName()), s));
                    if (NetHandlerLoginServer.this.loginGameProfile != null) {
                        NetHandlerLoginServer.logger.info("UUID of player " + NetHandlerLoginServer.this.loginGameProfile.getName() + " is " + NetHandlerLoginServer.this.loginGameProfile.getId());
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    }
                    else if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.logger.warn("Failed to verify username but will let them in anyway!");
                        NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.getOfflineProfile(gameprofile));
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    }
                    else {
                        NetHandlerLoginServer.this.closeConnection("Failed to verify username!");
                        NetHandlerLoginServer.logger.error("Username '" + NetHandlerLoginServer.this.loginGameProfile.getName() + "' tried to join with an invalid session");
                    }
                }
                catch (AuthenticationUnavailableException var3) {
                    if (NetHandlerLoginServer.this.server.isSinglePlayer()) {
                        NetHandlerLoginServer.logger.warn("Authentication servers are down but will let them in anyway!");
                        NetHandlerLoginServer.access$4(NetHandlerLoginServer.this, NetHandlerLoginServer.this.getOfflineProfile(gameprofile));
                        NetHandlerLoginServer.access$6(NetHandlerLoginServer.this, LoginState.READY_TO_ACCEPT);
                    }
                    else {
                        NetHandlerLoginServer.this.closeConnection("Authentication servers are down. Please try again later, sorry!");
                        NetHandlerLoginServer.logger.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }
        }.start();
    }
    
    protected GameProfile getOfflineProfile(final GameProfile original) {
        final UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + original.getName()).getBytes(Charsets.UTF_8));
        return new GameProfile(uuid, original.getName());
    }
    
    static /* synthetic */ void access$4(final NetHandlerLoginServer netHandlerLoginServer, final GameProfile loginGameProfile) {
        netHandlerLoginServer.loginGameProfile = loginGameProfile;
    }
    
    static /* synthetic */ void access$6(final NetHandlerLoginServer netHandlerLoginServer, final LoginState currentLoginState) {
        netHandlerLoginServer.currentLoginState = currentLoginState;
    }
    
    enum LoginState
    {
        HELLO("HELLO", 0), 
        KEY("KEY", 1), 
        AUTHENTICATING("AUTHENTICATING", 2), 
        READY_TO_ACCEPT("READY_TO_ACCEPT", 3), 
        DELAY_ACCEPT("DELAY_ACCEPT", 4), 
        ACCEPTED("ACCEPTED", 5);
        
        private LoginState(final String name, final int ordinal) {
        }
    }
}
