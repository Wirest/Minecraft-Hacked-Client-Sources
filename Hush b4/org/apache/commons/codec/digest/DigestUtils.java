// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.digest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class DigestUtils
{
    private static final int STREAM_BUFFER_LENGTH = 1024;
    
    private static byte[] digest(final MessageDigest digest, final InputStream data) throws IOException {
        return updateDigest(digest, data).digest();
    }
    
    public static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public static MessageDigest getMd2Digest() {
        return getDigest("MD2");
    }
    
    public static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }
    
    public static MessageDigest getSha1Digest() {
        return getDigest("SHA-1");
    }
    
    public static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }
    
    public static MessageDigest getSha384Digest() {
        return getDigest("SHA-384");
    }
    
    public static MessageDigest getSha512Digest() {
        return getDigest("SHA-512");
    }
    
    @Deprecated
    public static MessageDigest getShaDigest() {
        return getSha1Digest();
    }
    
    public static byte[] md2(final byte[] data) {
        return getMd2Digest().digest(data);
    }
    
    public static byte[] md2(final InputStream data) throws IOException {
        return digest(getMd2Digest(), data);
    }
    
    public static byte[] md2(final String data) {
        return md2(StringUtils.getBytesUtf8(data));
    }
    
    public static String md2Hex(final byte[] data) {
        return Hex.encodeHexString(md2(data));
    }
    
    public static String md2Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(md2(data));
    }
    
    public static String md2Hex(final String data) {
        return Hex.encodeHexString(md2(data));
    }
    
    public static byte[] md5(final byte[] data) {
        return getMd5Digest().digest(data);
    }
    
    public static byte[] md5(final InputStream data) throws IOException {
        return digest(getMd5Digest(), data);
    }
    
    public static byte[] md5(final String data) {
        return md5(StringUtils.getBytesUtf8(data));
    }
    
    public static String md5Hex(final byte[] data) {
        return Hex.encodeHexString(md5(data));
    }
    
    public static String md5Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(md5(data));
    }
    
    public static String md5Hex(final String data) {
        return Hex.encodeHexString(md5(data));
    }
    
    @Deprecated
    public static byte[] sha(final byte[] data) {
        return sha1(data);
    }
    
    @Deprecated
    public static byte[] sha(final InputStream data) throws IOException {
        return sha1(data);
    }
    
    @Deprecated
    public static byte[] sha(final String data) {
        return sha1(data);
    }
    
    public static byte[] sha1(final byte[] data) {
        return getSha1Digest().digest(data);
    }
    
    public static byte[] sha1(final InputStream data) throws IOException {
        return digest(getSha1Digest(), data);
    }
    
    public static byte[] sha1(final String data) {
        return sha1(StringUtils.getBytesUtf8(data));
    }
    
    public static String sha1Hex(final byte[] data) {
        return Hex.encodeHexString(sha1(data));
    }
    
    public static String sha1Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha1(data));
    }
    
    public static String sha1Hex(final String data) {
        return Hex.encodeHexString(sha1(data));
    }
    
    public static byte[] sha256(final byte[] data) {
        return getSha256Digest().digest(data);
    }
    
    public static byte[] sha256(final InputStream data) throws IOException {
        return digest(getSha256Digest(), data);
    }
    
    public static byte[] sha256(final String data) {
        return sha256(StringUtils.getBytesUtf8(data));
    }
    
    public static String sha256Hex(final byte[] data) {
        return Hex.encodeHexString(sha256(data));
    }
    
    public static String sha256Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha256(data));
    }
    
    public static String sha256Hex(final String data) {
        return Hex.encodeHexString(sha256(data));
    }
    
    public static byte[] sha384(final byte[] data) {
        return getSha384Digest().digest(data);
    }
    
    public static byte[] sha384(final InputStream data) throws IOException {
        return digest(getSha384Digest(), data);
    }
    
    public static byte[] sha384(final String data) {
        return sha384(StringUtils.getBytesUtf8(data));
    }
    
    public static String sha384Hex(final byte[] data) {
        return Hex.encodeHexString(sha384(data));
    }
    
    public static String sha384Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha384(data));
    }
    
    public static String sha384Hex(final String data) {
        return Hex.encodeHexString(sha384(data));
    }
    
    public static byte[] sha512(final byte[] data) {
        return getSha512Digest().digest(data);
    }
    
    public static byte[] sha512(final InputStream data) throws IOException {
        return digest(getSha512Digest(), data);
    }
    
    public static byte[] sha512(final String data) {
        return sha512(StringUtils.getBytesUtf8(data));
    }
    
    public static String sha512Hex(final byte[] data) {
        return Hex.encodeHexString(sha512(data));
    }
    
    public static String sha512Hex(final InputStream data) throws IOException {
        return Hex.encodeHexString(sha512(data));
    }
    
    public static String sha512Hex(final String data) {
        return Hex.encodeHexString(sha512(data));
    }
    
    @Deprecated
    public static String shaHex(final byte[] data) {
        return sha1Hex(data);
    }
    
    @Deprecated
    public static String shaHex(final InputStream data) throws IOException {
        return sha1Hex(data);
    }
    
    @Deprecated
    public static String shaHex(final String data) {
        return sha1Hex(data);
    }
    
    public static MessageDigest updateDigest(final MessageDigest messageDigest, final byte[] valueToDigest) {
        messageDigest.update(valueToDigest);
        return messageDigest;
    }
    
    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
        final byte[] buffer = new byte[1024];
        for (int read = data.read(buffer, 0, 1024); read > -1; read = data.read(buffer, 0, 1024)) {
            digest.update(buffer, 0, read);
        }
        return digest;
    }
    
    public static MessageDigest updateDigest(final MessageDigest messageDigest, final String valueToDigest) {
        messageDigest.update(StringUtils.getBytesUtf8(valueToDigest));
        return messageDigest;
    }
}
