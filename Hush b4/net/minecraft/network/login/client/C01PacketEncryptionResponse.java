// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.client;

import net.minecraft.network.INetHandler;
import java.security.PrivateKey;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.security.Key;
import net.minecraft.util.CryptManager;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.login.INetHandlerLoginServer;
import net.minecraft.network.Packet;

public class C01PacketEncryptionResponse implements Packet<INetHandlerLoginServer>
{
    private byte[] secretKeyEncrypted;
    private byte[] verifyTokenEncrypted;
    
    public C01PacketEncryptionResponse() {
        this.secretKeyEncrypted = new byte[0];
        this.verifyTokenEncrypted = new byte[0];
    }
    
    public C01PacketEncryptionResponse(final SecretKey secretKey, final PublicKey publicKey, final byte[] verifyToken) {
        this.secretKeyEncrypted = new byte[0];
        this.verifyTokenEncrypted = new byte[0];
        this.secretKeyEncrypted = CryptManager.encryptData(publicKey, secretKey.getEncoded());
        this.verifyTokenEncrypted = CryptManager.encryptData(publicKey, verifyToken);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.secretKeyEncrypted = buf.readByteArray();
        this.verifyTokenEncrypted = buf.readByteArray();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByteArray(this.secretKeyEncrypted);
        buf.writeByteArray(this.verifyTokenEncrypted);
    }
    
    @Override
    public void processPacket(final INetHandlerLoginServer handler) {
        handler.processEncryptionResponse(this);
    }
    
    public SecretKey getSecretKey(final PrivateKey key) {
        return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
    }
    
    public byte[] getVerifyToken(final PrivateKey key) {
        return (key == null) ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
    }
}
