// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.commons.codec.binary.Base64;
import java.util.Arrays;
import org.apache.http.util.EncodingUtils;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.security.MessageDigest;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import java.security.SecureRandom;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
final class NTLMEngineImpl implements NTLMEngine
{
    protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
    protected static final int FLAG_REQUEST_TARGET = 4;
    protected static final int FLAG_REQUEST_SIGN = 16;
    protected static final int FLAG_REQUEST_SEAL = 32;
    protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
    protected static final int FLAG_REQUEST_NTLMv1 = 512;
    protected static final int FLAG_DOMAIN_PRESENT = 4096;
    protected static final int FLAG_WORKSTATION_PRESENT = 8192;
    protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
    protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
    protected static final int FLAG_REQUEST_VERSION = 33554432;
    protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
    protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
    protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
    protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = Integer.MIN_VALUE;
    private static final SecureRandom RND_GEN;
    static final String DEFAULT_CHARSET = "ASCII";
    private String credentialCharset;
    private static final byte[] SIGNATURE;
    
    NTLMEngineImpl() {
        this.credentialCharset = "ASCII";
    }
    
    final String getResponseFor(final String message, final String username, final String password, final String host, final String domain) throws NTLMEngineException {
        String response;
        if (message == null || message.trim().equals("")) {
            response = this.getType1Message(host, domain);
        }
        else {
            final Type2Message t2m = new Type2Message(message);
            response = this.getType3Message(username, password, host, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
        }
        return response;
    }
    
    String getType1Message(final String host, final String domain) throws NTLMEngineException {
        return new Type1Message(domain, host).getResponse();
    }
    
    String getType3Message(final String user, final String password, final String host, final String domain, final byte[] nonce, final int type2Flags, final String target, final byte[] targetInformation) throws NTLMEngineException {
        return new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation).getResponse();
    }
    
    String getCredentialCharset() {
        return this.credentialCharset;
    }
    
    void setCredentialCharset(final String credentialCharset) {
        this.credentialCharset = credentialCharset;
    }
    
    private static String stripDotSuffix(final String value) {
        if (value == null) {
            return null;
        }
        final int index = value.indexOf(".");
        if (index != -1) {
            return value.substring(0, index);
        }
        return value;
    }
    
    private static String convertHost(final String host) {
        return stripDotSuffix(host);
    }
    
    private static String convertDomain(final String domain) {
        return stripDotSuffix(domain);
    }
    
    private static int readULong(final byte[] src, final int index) throws NTLMEngineException {
        if (src.length < index + 4) {
            throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
        }
        return (src[index] & 0xFF) | (src[index + 1] & 0xFF) << 8 | (src[index + 2] & 0xFF) << 16 | (src[index + 3] & 0xFF) << 24;
    }
    
    private static int readUShort(final byte[] src, final int index) throws NTLMEngineException {
        if (src.length < index + 2) {
            throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
        }
        return (src[index] & 0xFF) | (src[index + 1] & 0xFF) << 8;
    }
    
    private static byte[] readSecurityBuffer(final byte[] src, final int index) throws NTLMEngineException {
        final int length = readUShort(src, index);
        final int offset = readULong(src, index + 4);
        if (src.length < offset + length) {
            throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
        }
        final byte[] buffer = new byte[length];
        System.arraycopy(src, offset, buffer, 0, length);
        return buffer;
    }
    
    private static byte[] makeRandomChallenge() throws NTLMEngineException {
        if (NTLMEngineImpl.RND_GEN == null) {
            throw new NTLMEngineException("Random generator not available");
        }
        final byte[] rval = new byte[8];
        synchronized (NTLMEngineImpl.RND_GEN) {
            NTLMEngineImpl.RND_GEN.nextBytes(rval);
        }
        return rval;
    }
    
    private static byte[] makeSecondaryKey() throws NTLMEngineException {
        if (NTLMEngineImpl.RND_GEN == null) {
            throw new NTLMEngineException("Random generator not available");
        }
        final byte[] rval = new byte[16];
        synchronized (NTLMEngineImpl.RND_GEN) {
            NTLMEngineImpl.RND_GEN.nextBytes(rval);
        }
        return rval;
    }
    
    static byte[] hmacMD5(final byte[] value, final byte[] key) throws NTLMEngineException {
        final HMACMD5 hmacMD5 = new HMACMD5(key);
        hmacMD5.update(value);
        return hmacMD5.getOutput();
    }
    
    static byte[] RC4(final byte[] value, final byte[] key) throws NTLMEngineException {
        try {
            final Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(1, new SecretKeySpec(key, "RC4"));
            return rc4.doFinal(value);
        }
        catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }
    
    static byte[] ntlm2SessionResponse(final byte[] ntlmHash, final byte[] challenge, final byte[] clientChallenge) throws NTLMEngineException {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(challenge);
            md5.update(clientChallenge);
            final byte[] digest = md5.digest();
            final byte[] sessionHash = new byte[8];
            System.arraycopy(digest, 0, sessionHash, 0, 8);
            return lmResponse(ntlmHash, sessionHash);
        }
        catch (Exception e) {
            if (e instanceof NTLMEngineException) {
                throw (NTLMEngineException)e;
            }
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }
    
    private static byte[] lmHash(final String password) throws NTLMEngineException {
        try {
            final byte[] oemPassword = password.toUpperCase(Locale.US).getBytes("US-ASCII");
            final int length = Math.min(oemPassword.length, 14);
            final byte[] keyBytes = new byte[14];
            System.arraycopy(oemPassword, 0, keyBytes, 0, length);
            final Key lowKey = createDESKey(keyBytes, 0);
            final Key highKey = createDESKey(keyBytes, 7);
            final byte[] magicConstant = "KGS!@#$%".getBytes("US-ASCII");
            final Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
            des.init(1, lowKey);
            final byte[] lowHash = des.doFinal(magicConstant);
            des.init(1, highKey);
            final byte[] highHash = des.doFinal(magicConstant);
            final byte[] lmHash = new byte[16];
            System.arraycopy(lowHash, 0, lmHash, 0, 8);
            System.arraycopy(highHash, 0, lmHash, 8, 8);
            return lmHash;
        }
        catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }
    
    private static byte[] ntlmHash(final String password) throws NTLMEngineException {
        try {
            final byte[] unicodePassword = password.getBytes("UnicodeLittleUnmarked");
            final MD4 md4 = new MD4();
            md4.update(unicodePassword);
            return md4.getOutput();
        }
        catch (UnsupportedEncodingException e) {
            throw new NTLMEngineException("Unicode not supported: " + e.getMessage(), e);
        }
    }
    
    private static byte[] lmv2Hash(final String domain, final String user, final byte[] ntlmHash) throws NTLMEngineException {
        try {
            final HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
            hmacMD5.update(user.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
            if (domain != null) {
                hmacMD5.update(domain.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
            }
            return hmacMD5.getOutput();
        }
        catch (UnsupportedEncodingException e) {
            throw new NTLMEngineException("Unicode not supported! " + e.getMessage(), e);
        }
    }
    
    private static byte[] ntlmv2Hash(final String domain, final String user, final byte[] ntlmHash) throws NTLMEngineException {
        try {
            final HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
            hmacMD5.update(user.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
            if (domain != null) {
                hmacMD5.update(domain.getBytes("UnicodeLittleUnmarked"));
            }
            return hmacMD5.getOutput();
        }
        catch (UnsupportedEncodingException e) {
            throw new NTLMEngineException("Unicode not supported! " + e.getMessage(), e);
        }
    }
    
    private static byte[] lmResponse(final byte[] hash, final byte[] challenge) throws NTLMEngineException {
        try {
            final byte[] keyBytes = new byte[21];
            System.arraycopy(hash, 0, keyBytes, 0, 16);
            final Key lowKey = createDESKey(keyBytes, 0);
            final Key middleKey = createDESKey(keyBytes, 7);
            final Key highKey = createDESKey(keyBytes, 14);
            final Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
            des.init(1, lowKey);
            final byte[] lowResponse = des.doFinal(challenge);
            des.init(1, middleKey);
            final byte[] middleResponse = des.doFinal(challenge);
            des.init(1, highKey);
            final byte[] highResponse = des.doFinal(challenge);
            final byte[] lmResponse = new byte[24];
            System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
            System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
            System.arraycopy(highResponse, 0, lmResponse, 16, 8);
            return lmResponse;
        }
        catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }
    
    private static byte[] lmv2Response(final byte[] hash, final byte[] challenge, final byte[] clientData) throws NTLMEngineException {
        final HMACMD5 hmacMD5 = new HMACMD5(hash);
        hmacMD5.update(challenge);
        hmacMD5.update(clientData);
        final byte[] mac = hmacMD5.getOutput();
        final byte[] lmv2Response = new byte[mac.length + clientData.length];
        System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
        System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
        return lmv2Response;
    }
    
    private static byte[] createBlob(final byte[] clientChallenge, final byte[] targetInformation, final byte[] timestamp) {
        final byte[] blobSignature = { 1, 1, 0, 0 };
        final byte[] reserved = { 0, 0, 0, 0 };
        final byte[] unknown1 = { 0, 0, 0, 0 };
        final byte[] unknown2 = { 0, 0, 0, 0 };
        final byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + 8 + unknown1.length + targetInformation.length + unknown2.length];
        int offset = 0;
        System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
        offset += blobSignature.length;
        System.arraycopy(reserved, 0, blob, offset, reserved.length);
        offset += reserved.length;
        System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
        offset += timestamp.length;
        System.arraycopy(clientChallenge, 0, blob, offset, 8);
        offset += 8;
        System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
        offset += unknown1.length;
        System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
        offset += targetInformation.length;
        System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
        offset += unknown2.length;
        return blob;
    }
    
    private static Key createDESKey(final byte[] bytes, final int offset) {
        final byte[] keyBytes = new byte[7];
        System.arraycopy(bytes, offset, keyBytes, 0, 7);
        final byte[] material = { keyBytes[0], (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1), (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2), (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3), (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4), (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5), (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6), (byte)(keyBytes[6] << 1) };
        oddParity(material);
        return new SecretKeySpec(material, "DES");
    }
    
    private static void oddParity(final byte[] bytes) {
        for (int i = 0; i < bytes.length; ++i) {
            final byte b = bytes[i];
            final boolean needsParity = ((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0x0;
            if (needsParity) {
                final int n = i;
                bytes[n] |= 0x1;
            }
            else {
                final int n2 = i;
                bytes[n2] &= 0xFFFFFFFE;
            }
        }
    }
    
    static void writeULong(final byte[] buffer, final int value, final int offset) {
        buffer[offset] = (byte)(value & 0xFF);
        buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
        buffer[offset + 2] = (byte)(value >> 16 & 0xFF);
        buffer[offset + 3] = (byte)(value >> 24 & 0xFF);
    }
    
    static int F(final int x, final int y, final int z) {
        return (x & y) | (~x & z);
    }
    
    static int G(final int x, final int y, final int z) {
        return (x & y) | (x & z) | (y & z);
    }
    
    static int H(final int x, final int y, final int z) {
        return x ^ y ^ z;
    }
    
    static int rotintlft(final int val, final int numbits) {
        return val << numbits | val >>> 32 - numbits;
    }
    
    public String generateType1Msg(final String domain, final String workstation) throws NTLMEngineException {
        return this.getType1Message(workstation, domain);
    }
    
    public String generateType3Msg(final String username, final String password, final String domain, final String workstation, final String challenge) throws NTLMEngineException {
        final Type2Message t2m = new Type2Message(challenge);
        return this.getType3Message(username, password, workstation, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
    }
    
    static {
        SecureRandom rnd = null;
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (Exception ex) {}
        RND_GEN = rnd;
        final byte[] bytesWithoutNull = EncodingUtils.getBytes("NTLMSSP", "ASCII");
        System.arraycopy(bytesWithoutNull, 0, SIGNATURE = new byte[bytesWithoutNull.length + 1], 0, bytesWithoutNull.length);
        NTLMEngineImpl.SIGNATURE[bytesWithoutNull.length] = 0;
    }
    
    protected static class CipherGen
    {
        protected final String domain;
        protected final String user;
        protected final String password;
        protected final byte[] challenge;
        protected final String target;
        protected final byte[] targetInformation;
        protected byte[] clientChallenge;
        protected byte[] clientChallenge2;
        protected byte[] secondaryKey;
        protected byte[] timestamp;
        protected byte[] lmHash;
        protected byte[] lmResponse;
        protected byte[] ntlmHash;
        protected byte[] ntlmResponse;
        protected byte[] ntlmv2Hash;
        protected byte[] lmv2Hash;
        protected byte[] lmv2Response;
        protected byte[] ntlmv2Blob;
        protected byte[] ntlmv2Response;
        protected byte[] ntlm2SessionResponse;
        protected byte[] lm2SessionResponse;
        protected byte[] lmUserSessionKey;
        protected byte[] ntlmUserSessionKey;
        protected byte[] ntlmv2UserSessionKey;
        protected byte[] ntlm2SessionResponseUserSessionKey;
        protected byte[] lanManagerSessionKey;
        
        public CipherGen(final String domain, final String user, final String password, final byte[] challenge, final String target, final byte[] targetInformation, final byte[] clientChallenge, final byte[] clientChallenge2, final byte[] secondaryKey, final byte[] timestamp) {
            this.lmHash = null;
            this.lmResponse = null;
            this.ntlmHash = null;
            this.ntlmResponse = null;
            this.ntlmv2Hash = null;
            this.lmv2Hash = null;
            this.lmv2Response = null;
            this.ntlmv2Blob = null;
            this.ntlmv2Response = null;
            this.ntlm2SessionResponse = null;
            this.lm2SessionResponse = null;
            this.lmUserSessionKey = null;
            this.ntlmUserSessionKey = null;
            this.ntlmv2UserSessionKey = null;
            this.ntlm2SessionResponseUserSessionKey = null;
            this.lanManagerSessionKey = null;
            this.domain = domain;
            this.target = target;
            this.user = user;
            this.password = password;
            this.challenge = challenge;
            this.targetInformation = targetInformation;
            this.clientChallenge = clientChallenge;
            this.clientChallenge2 = clientChallenge2;
            this.secondaryKey = secondaryKey;
            this.timestamp = timestamp;
        }
        
        public CipherGen(final String domain, final String user, final String password, final byte[] challenge, final String target, final byte[] targetInformation) {
            this(domain, user, password, challenge, target, targetInformation, null, null, null, null);
        }
        
        public byte[] getClientChallenge() throws NTLMEngineException {
            if (this.clientChallenge == null) {
                this.clientChallenge = makeRandomChallenge();
            }
            return this.clientChallenge;
        }
        
        public byte[] getClientChallenge2() throws NTLMEngineException {
            if (this.clientChallenge2 == null) {
                this.clientChallenge2 = makeRandomChallenge();
            }
            return this.clientChallenge2;
        }
        
        public byte[] getSecondaryKey() throws NTLMEngineException {
            if (this.secondaryKey == null) {
                this.secondaryKey = makeSecondaryKey();
            }
            return this.secondaryKey;
        }
        
        public byte[] getLMHash() throws NTLMEngineException {
            if (this.lmHash == null) {
                this.lmHash = lmHash(this.password);
            }
            return this.lmHash;
        }
        
        public byte[] getLMResponse() throws NTLMEngineException {
            if (this.lmResponse == null) {
                this.lmResponse = lmResponse(this.getLMHash(), this.challenge);
            }
            return this.lmResponse;
        }
        
        public byte[] getNTLMHash() throws NTLMEngineException {
            if (this.ntlmHash == null) {
                this.ntlmHash = ntlmHash(this.password);
            }
            return this.ntlmHash;
        }
        
        public byte[] getNTLMResponse() throws NTLMEngineException {
            if (this.ntlmResponse == null) {
                this.ntlmResponse = lmResponse(this.getNTLMHash(), this.challenge);
            }
            return this.ntlmResponse;
        }
        
        public byte[] getLMv2Hash() throws NTLMEngineException {
            if (this.lmv2Hash == null) {
                this.lmv2Hash = lmv2Hash(this.domain, this.user, this.getNTLMHash());
            }
            return this.lmv2Hash;
        }
        
        public byte[] getNTLMv2Hash() throws NTLMEngineException {
            if (this.ntlmv2Hash == null) {
                this.ntlmv2Hash = ntlmv2Hash(this.domain, this.user, this.getNTLMHash());
            }
            return this.ntlmv2Hash;
        }
        
        public byte[] getTimestamp() {
            if (this.timestamp == null) {
                long time = System.currentTimeMillis();
                time += 11644473600000L;
                time *= 10000L;
                this.timestamp = new byte[8];
                for (int i = 0; i < 8; ++i) {
                    this.timestamp[i] = (byte)time;
                    time >>>= 8;
                }
            }
            return this.timestamp;
        }
        
        public byte[] getNTLMv2Blob() throws NTLMEngineException {
            if (this.ntlmv2Blob == null) {
                this.ntlmv2Blob = createBlob(this.getClientChallenge2(), this.targetInformation, this.getTimestamp());
            }
            return this.ntlmv2Blob;
        }
        
        public byte[] getNTLMv2Response() throws NTLMEngineException {
            if (this.ntlmv2Response == null) {
                this.ntlmv2Response = lmv2Response(this.getNTLMv2Hash(), this.challenge, this.getNTLMv2Blob());
            }
            return this.ntlmv2Response;
        }
        
        public byte[] getLMv2Response() throws NTLMEngineException {
            if (this.lmv2Response == null) {
                this.lmv2Response = lmv2Response(this.getLMv2Hash(), this.challenge, this.getClientChallenge());
            }
            return this.lmv2Response;
        }
        
        public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
            if (this.ntlm2SessionResponse == null) {
                this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(this.getNTLMHash(), this.challenge, this.getClientChallenge());
            }
            return this.ntlm2SessionResponse;
        }
        
        public byte[] getLM2SessionResponse() throws NTLMEngineException {
            if (this.lm2SessionResponse == null) {
                final byte[] clientChallenge = this.getClientChallenge();
                System.arraycopy(clientChallenge, 0, this.lm2SessionResponse = new byte[24], 0, clientChallenge.length);
                Arrays.fill(this.lm2SessionResponse, clientChallenge.length, this.lm2SessionResponse.length, (byte)0);
            }
            return this.lm2SessionResponse;
        }
        
        public byte[] getLMUserSessionKey() throws NTLMEngineException {
            if (this.lmUserSessionKey == null) {
                final byte[] lmHash = this.getLMHash();
                System.arraycopy(lmHash, 0, this.lmUserSessionKey = new byte[16], 0, 8);
                Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
            }
            return this.lmUserSessionKey;
        }
        
        public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
            if (this.ntlmUserSessionKey == null) {
                final byte[] ntlmHash = this.getNTLMHash();
                final MD4 md4 = new MD4();
                md4.update(ntlmHash);
                this.ntlmUserSessionKey = md4.getOutput();
            }
            return this.ntlmUserSessionKey;
        }
        
        public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
            if (this.ntlmv2UserSessionKey == null) {
                final byte[] ntlmv2hash = this.getNTLMv2Hash();
                final byte[] truncatedResponse = new byte[16];
                System.arraycopy(this.getNTLMv2Response(), 0, truncatedResponse, 0, 16);
                this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
            }
            return this.ntlmv2UserSessionKey;
        }
        
        public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
            if (this.ntlm2SessionResponseUserSessionKey == null) {
                final byte[] ntlmUserSessionKey = this.getNTLMUserSessionKey();
                final byte[] ntlm2SessionResponseNonce = this.getLM2SessionResponse();
                final byte[] sessionNonce = new byte[this.challenge.length + ntlm2SessionResponseNonce.length];
                System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
                System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
                this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, ntlmUserSessionKey);
            }
            return this.ntlm2SessionResponseUserSessionKey;
        }
        
        public byte[] getLanManagerSessionKey() throws NTLMEngineException {
            if (this.lanManagerSessionKey == null) {
                final byte[] lmHash = this.getLMHash();
                final byte[] lmResponse = this.getLMResponse();
                try {
                    final byte[] keyBytes = new byte[14];
                    System.arraycopy(lmHash, 0, keyBytes, 0, 8);
                    Arrays.fill(keyBytes, 8, keyBytes.length, (byte)(-67));
                    final Key lowKey = createDESKey(keyBytes, 0);
                    final Key highKey = createDESKey(keyBytes, 7);
                    final byte[] truncatedResponse = new byte[8];
                    System.arraycopy(lmResponse, 0, truncatedResponse, 0, truncatedResponse.length);
                    Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
                    des.init(1, lowKey);
                    final byte[] lowPart = des.doFinal(truncatedResponse);
                    des = Cipher.getInstance("DES/ECB/NoPadding");
                    des.init(1, highKey);
                    final byte[] highPart = des.doFinal(truncatedResponse);
                    System.arraycopy(lowPart, 0, this.lanManagerSessionKey = new byte[16], 0, lowPart.length);
                    System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
                }
                catch (Exception e) {
                    throw new NTLMEngineException(e.getMessage(), e);
                }
            }
            return this.lanManagerSessionKey;
        }
    }
    
    static class NTLMMessage
    {
        private byte[] messageContents;
        private int currentOutputPosition;
        
        NTLMMessage() {
            this.messageContents = null;
            this.currentOutputPosition = 0;
        }
        
        NTLMMessage(final String messageBody, final int expectedType) throws NTLMEngineException {
            this.messageContents = null;
            this.currentOutputPosition = 0;
            this.messageContents = Base64.decodeBase64(EncodingUtils.getBytes(messageBody, "ASCII"));
            if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
                throw new NTLMEngineException("NTLM message decoding error - packet too short");
            }
            for (int i = 0; i < NTLMEngineImpl.SIGNATURE.length; ++i) {
                if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
                    throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
                }
            }
            final int type = this.readULong(NTLMEngineImpl.SIGNATURE.length);
            if (type != expectedType) {
                throw new NTLMEngineException("NTLM type " + Integer.toString(expectedType) + " message expected - instead got type " + Integer.toString(type));
            }
            this.currentOutputPosition = this.messageContents.length;
        }
        
        protected int getPreambleLength() {
            return NTLMEngineImpl.SIGNATURE.length + 4;
        }
        
        protected int getMessageLength() {
            return this.currentOutputPosition;
        }
        
        protected byte readByte(final int position) throws NTLMEngineException {
            if (this.messageContents.length < position + 1) {
                throw new NTLMEngineException("NTLM: Message too short");
            }
            return this.messageContents[position];
        }
        
        protected void readBytes(final byte[] buffer, final int position) throws NTLMEngineException {
            if (this.messageContents.length < position + buffer.length) {
                throw new NTLMEngineException("NTLM: Message too short");
            }
            System.arraycopy(this.messageContents, position, buffer, 0, buffer.length);
        }
        
        protected int readUShort(final int position) throws NTLMEngineException {
            return readUShort(this.messageContents, position);
        }
        
        protected int readULong(final int position) throws NTLMEngineException {
            return readULong(this.messageContents, position);
        }
        
        protected byte[] readSecurityBuffer(final int position) throws NTLMEngineException {
            return readSecurityBuffer(this.messageContents, position);
        }
        
        protected void prepareResponse(final int maxlength, final int messageType) {
            this.messageContents = new byte[maxlength];
            this.currentOutputPosition = 0;
            this.addBytes(NTLMEngineImpl.SIGNATURE);
            this.addULong(messageType);
        }
        
        protected void addByte(final byte b) {
            this.messageContents[this.currentOutputPosition] = b;
            ++this.currentOutputPosition;
        }
        
        protected void addBytes(final byte[] bytes) {
            if (bytes == null) {
                return;
            }
            for (final byte b : bytes) {
                this.messageContents[this.currentOutputPosition] = b;
                ++this.currentOutputPosition;
            }
        }
        
        protected void addUShort(final int value) {
            this.addByte((byte)(value & 0xFF));
            this.addByte((byte)(value >> 8 & 0xFF));
        }
        
        protected void addULong(final int value) {
            this.addByte((byte)(value & 0xFF));
            this.addByte((byte)(value >> 8 & 0xFF));
            this.addByte((byte)(value >> 16 & 0xFF));
            this.addByte((byte)(value >> 24 & 0xFF));
        }
        
        String getResponse() {
            byte[] resp;
            if (this.messageContents.length > this.currentOutputPosition) {
                final byte[] tmp = new byte[this.currentOutputPosition];
                System.arraycopy(this.messageContents, 0, tmp, 0, this.currentOutputPosition);
                resp = tmp;
            }
            else {
                resp = this.messageContents;
            }
            return EncodingUtils.getAsciiString(Base64.encodeBase64(resp));
        }
    }
    
    static class Type1Message extends NTLMMessage
    {
        protected byte[] hostBytes;
        protected byte[] domainBytes;
        
        Type1Message(final String domain, final String host) throws NTLMEngineException {
            try {
                final String unqualifiedHost = convertHost(host);
                final String unqualifiedDomain = convertDomain(domain);
                this.hostBytes = (byte[])((unqualifiedHost != null) ? unqualifiedHost.getBytes("ASCII") : null);
                this.domainBytes = (byte[])((unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.US).getBytes("ASCII") : null);
            }
            catch (UnsupportedEncodingException e) {
                throw new NTLMEngineException("Unicode unsupported: " + e.getMessage(), e);
            }
        }
        
        @Override
        String getResponse() {
            final int finalLength = 40;
            this.prepareResponse(40, 1);
            this.addULong(-1576500735);
            this.addUShort(0);
            this.addUShort(0);
            this.addULong(40);
            this.addUShort(0);
            this.addUShort(0);
            this.addULong(40);
            this.addUShort(261);
            this.addULong(2600);
            this.addUShort(3840);
            return super.getResponse();
        }
    }
    
    static class Type2Message extends NTLMMessage
    {
        protected byte[] challenge;
        protected String target;
        protected byte[] targetInfo;
        protected int flags;
        
        Type2Message(final String message) throws NTLMEngineException {
            super(message, 2);
            this.readBytes(this.challenge = new byte[8], 24);
            this.flags = this.readULong(20);
            if ((this.flags & 0x1) == 0x0) {
                throw new NTLMEngineException("NTLM type 2 message has flags that make no sense: " + Integer.toString(this.flags));
            }
            this.target = null;
            if (this.getMessageLength() >= 20) {
                final byte[] bytes = this.readSecurityBuffer(12);
                if (bytes.length != 0) {
                    try {
                        this.target = new String(bytes, "UnicodeLittleUnmarked");
                    }
                    catch (UnsupportedEncodingException e) {
                        throw new NTLMEngineException(e.getMessage(), e);
                    }
                }
            }
            this.targetInfo = null;
            if (this.getMessageLength() >= 48) {
                final byte[] bytes = this.readSecurityBuffer(40);
                if (bytes.length != 0) {
                    this.targetInfo = bytes;
                }
            }
        }
        
        byte[] getChallenge() {
            return this.challenge;
        }
        
        String getTarget() {
            return this.target;
        }
        
        byte[] getTargetInfo() {
            return this.targetInfo;
        }
        
        int getFlags() {
            return this.flags;
        }
    }
    
    static class Type3Message extends NTLMMessage
    {
        protected int type2Flags;
        protected byte[] domainBytes;
        protected byte[] hostBytes;
        protected byte[] userBytes;
        protected byte[] lmResp;
        protected byte[] ntResp;
        protected byte[] sessionKey;
        
        Type3Message(final String domain, final String host, final String user, final String password, final byte[] nonce, final int type2Flags, final String target, final byte[] targetInformation) throws NTLMEngineException {
            this.type2Flags = type2Flags;
            final String unqualifiedHost = convertHost(host);
            final String unqualifiedDomain = convertDomain(domain);
            final CipherGen gen = new CipherGen(unqualifiedDomain, user, password, nonce, target, targetInformation);
            byte[] userSessionKey;
            try {
                if ((type2Flags & 0x800000) != 0x0 && targetInformation != null && target != null) {
                    this.ntResp = gen.getNTLMv2Response();
                    this.lmResp = gen.getLMv2Response();
                    if ((type2Flags & 0x80) != 0x0) {
                        userSessionKey = gen.getLanManagerSessionKey();
                    }
                    else {
                        userSessionKey = gen.getNTLMv2UserSessionKey();
                    }
                }
                else if ((type2Flags & 0x80000) != 0x0) {
                    this.ntResp = gen.getNTLM2SessionResponse();
                    this.lmResp = gen.getLM2SessionResponse();
                    if ((type2Flags & 0x80) != 0x0) {
                        userSessionKey = gen.getLanManagerSessionKey();
                    }
                    else {
                        userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
                    }
                }
                else {
                    this.ntResp = gen.getNTLMResponse();
                    this.lmResp = gen.getLMResponse();
                    if ((type2Flags & 0x80) != 0x0) {
                        userSessionKey = gen.getLanManagerSessionKey();
                    }
                    else {
                        userSessionKey = gen.getNTLMUserSessionKey();
                    }
                }
            }
            catch (NTLMEngineException e2) {
                this.ntResp = new byte[0];
                this.lmResp = gen.getLMResponse();
                if ((type2Flags & 0x80) != 0x0) {
                    userSessionKey = gen.getLanManagerSessionKey();
                }
                else {
                    userSessionKey = gen.getLMUserSessionKey();
                }
            }
            if ((type2Flags & 0x10) != 0x0) {
                if ((type2Flags & 0x40000000) != 0x0) {
                    this.sessionKey = NTLMEngineImpl.RC4(gen.getSecondaryKey(), userSessionKey);
                }
                else {
                    this.sessionKey = userSessionKey;
                }
            }
            else {
                this.sessionKey = null;
            }
            try {
                this.hostBytes = (byte[])((unqualifiedHost != null) ? unqualifiedHost.getBytes("UnicodeLittleUnmarked") : null);
                this.domainBytes = (byte[])((unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked") : null);
                this.userBytes = user.getBytes("UnicodeLittleUnmarked");
            }
            catch (UnsupportedEncodingException e) {
                throw new NTLMEngineException("Unicode not supported: " + e.getMessage(), e);
            }
        }
        
        @Override
        String getResponse() {
            final int ntRespLen = this.ntResp.length;
            final int lmRespLen = this.lmResp.length;
            final int domainLen = (this.domainBytes != null) ? this.domainBytes.length : 0;
            final int hostLen = (this.hostBytes != null) ? this.hostBytes.length : 0;
            final int userLen = this.userBytes.length;
            int sessionKeyLen;
            if (this.sessionKey != null) {
                sessionKeyLen = this.sessionKey.length;
            }
            else {
                sessionKeyLen = 0;
            }
            final int lmRespOffset = 72;
            final int ntRespOffset = 72 + lmRespLen;
            final int domainOffset = ntRespOffset + ntRespLen;
            final int userOffset = domainOffset + domainLen;
            final int hostOffset = userOffset + userLen;
            final int sessionKeyOffset = hostOffset + hostLen;
            final int finalLength = sessionKeyOffset + sessionKeyLen;
            this.prepareResponse(finalLength, 3);
            this.addUShort(lmRespLen);
            this.addUShort(lmRespLen);
            this.addULong(72);
            this.addUShort(ntRespLen);
            this.addUShort(ntRespLen);
            this.addULong(ntRespOffset);
            this.addUShort(domainLen);
            this.addUShort(domainLen);
            this.addULong(domainOffset);
            this.addUShort(userLen);
            this.addUShort(userLen);
            this.addULong(userOffset);
            this.addUShort(hostLen);
            this.addUShort(hostLen);
            this.addULong(hostOffset);
            this.addUShort(sessionKeyLen);
            this.addUShort(sessionKeyLen);
            this.addULong(sessionKeyOffset);
            this.addULong((this.type2Flags & 0x80) | (this.type2Flags & 0x200) | (this.type2Flags & 0x80000) | 0x2000000 | (this.type2Flags & 0x8000) | (this.type2Flags & 0x20) | (this.type2Flags & 0x10) | (this.type2Flags & 0x20000000) | (this.type2Flags & Integer.MIN_VALUE) | (this.type2Flags & 0x40000000) | (this.type2Flags & 0x800000) | (this.type2Flags & 0x1) | (this.type2Flags & 0x4));
            this.addUShort(261);
            this.addULong(2600);
            this.addUShort(3840);
            this.addBytes(this.lmResp);
            this.addBytes(this.ntResp);
            this.addBytes(this.domainBytes);
            this.addBytes(this.userBytes);
            this.addBytes(this.hostBytes);
            if (this.sessionKey != null) {
                this.addBytes(this.sessionKey);
            }
            return super.getResponse();
        }
    }
    
    static class MD4
    {
        protected int A;
        protected int B;
        protected int C;
        protected int D;
        protected long count;
        protected byte[] dataBuffer;
        
        MD4() {
            this.A = 1732584193;
            this.B = -271733879;
            this.C = -1732584194;
            this.D = 271733878;
            this.count = 0L;
            this.dataBuffer = new byte[64];
        }
        
        void update(final byte[] input) {
            int curBufferPos = (int)(this.count & 0x3FL);
            int inputIndex = 0;
            while (input.length - inputIndex + curBufferPos >= this.dataBuffer.length) {
                final int transferAmt = this.dataBuffer.length - curBufferPos;
                System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
                this.count += transferAmt;
                curBufferPos = 0;
                inputIndex += transferAmt;
                this.processBuffer();
            }
            if (inputIndex < input.length) {
                final int transferAmt = input.length - inputIndex;
                System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
                this.count += transferAmt;
                curBufferPos += transferAmt;
            }
        }
        
        byte[] getOutput() {
            final int bufferIndex = (int)(this.count & 0x3FL);
            final int padLen = (bufferIndex < 56) ? (56 - bufferIndex) : (120 - bufferIndex);
            final byte[] postBytes = new byte[padLen + 8];
            postBytes[0] = -128;
            for (int i = 0; i < 8; ++i) {
                postBytes[padLen + i] = (byte)(this.count * 8L >>> 8 * i);
            }
            this.update(postBytes);
            final byte[] result = new byte[16];
            NTLMEngineImpl.writeULong(result, this.A, 0);
            NTLMEngineImpl.writeULong(result, this.B, 4);
            NTLMEngineImpl.writeULong(result, this.C, 8);
            NTLMEngineImpl.writeULong(result, this.D, 12);
            return result;
        }
        
        protected void processBuffer() {
            final int[] d = new int[16];
            for (int i = 0; i < 16; ++i) {
                d[i] = (this.dataBuffer[i * 4] & 0xFF) + ((this.dataBuffer[i * 4 + 1] & 0xFF) << 8) + ((this.dataBuffer[i * 4 + 2] & 0xFF) << 16) + ((this.dataBuffer[i * 4 + 3] & 0xFF) << 24);
            }
            final int AA = this.A;
            final int BB = this.B;
            final int CC = this.C;
            final int DD = this.D;
            this.round1(d);
            this.round2(d);
            this.round3(d);
            this.A += AA;
            this.B += BB;
            this.C += CC;
            this.D += DD;
        }
        
        protected void round1(final int[] d) {
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[0], 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[1], 7);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[2], 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[3], 19);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[4], 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[5], 7);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[6], 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[7], 19);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[8], 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[9], 7);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[10], 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[11], 19);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[12], 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[13], 7);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[14], 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[15], 19);
        }
        
        protected void round2(final int[] d) {
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[0] + 1518500249, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[4] + 1518500249, 5);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[8] + 1518500249, 9);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[12] + 1518500249, 13);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[1] + 1518500249, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[5] + 1518500249, 5);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[9] + 1518500249, 9);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[13] + 1518500249, 13);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[2] + 1518500249, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[6] + 1518500249, 5);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[10] + 1518500249, 9);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[14] + 1518500249, 13);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[3] + 1518500249, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[7] + 1518500249, 5);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[11] + 1518500249, 9);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[15] + 1518500249, 13);
        }
        
        protected void round3(final int[] d) {
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[0] + 1859775393, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[8] + 1859775393, 9);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[4] + 1859775393, 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[12] + 1859775393, 15);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[2] + 1859775393, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[10] + 1859775393, 9);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[6] + 1859775393, 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[14] + 1859775393, 15);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[1] + 1859775393, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[9] + 1859775393, 9);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[5] + 1859775393, 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[13] + 1859775393, 15);
            this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[3] + 1859775393, 3);
            this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[11] + 1859775393, 9);
            this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[7] + 1859775393, 11);
            this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[15] + 1859775393, 15);
        }
    }
    
    static class HMACMD5
    {
        protected byte[] ipad;
        protected byte[] opad;
        protected MessageDigest md5;
        
        HMACMD5(final byte[] input) throws NTLMEngineException {
            byte[] key = input;
            try {
                this.md5 = MessageDigest.getInstance("MD5");
            }
            catch (Exception ex) {
                throw new NTLMEngineException("Error getting md5 message digest implementation: " + ex.getMessage(), ex);
            }
            this.ipad = new byte[64];
            this.opad = new byte[64];
            int keyLength = key.length;
            if (keyLength > 64) {
                this.md5.update(key);
                key = this.md5.digest();
                keyLength = key.length;
            }
            int i;
            for (i = 0; i < keyLength; ++i) {
                this.ipad[i] = (byte)(key[i] ^ 0x36);
                this.opad[i] = (byte)(key[i] ^ 0x5C);
            }
            while (i < 64) {
                this.ipad[i] = 54;
                this.opad[i] = 92;
                ++i;
            }
            this.md5.reset();
            this.md5.update(this.ipad);
        }
        
        byte[] getOutput() {
            final byte[] digest = this.md5.digest();
            this.md5.update(this.opad);
            return this.md5.digest(digest);
        }
        
        void update(final byte[] input) {
            this.md5.update(input);
        }
        
        void update(final byte[] input, final int offset, final int length) {
            this.md5.update(input, offset, length);
        }
    }
}
