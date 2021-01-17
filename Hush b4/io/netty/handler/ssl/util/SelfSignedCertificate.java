// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl.util;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.cert.CertificateEncodingException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.io.File;
import java.util.Date;
import io.netty.util.internal.logging.InternalLogger;

public final class SelfSignedCertificate
{
    private static final InternalLogger logger;
    static final Date NOT_BEFORE;
    static final Date NOT_AFTER;
    private final File certificate;
    private final File privateKey;
    
    public SelfSignedCertificate() throws CertificateException {
        this("example.com");
    }
    
    public SelfSignedCertificate(final String fqdn) throws CertificateException {
        this(fqdn, ThreadLocalInsecureRandom.current(), 1024);
    }
    
    public SelfSignedCertificate(final String fqdn, final SecureRandom random, final int bits) throws CertificateException {
        KeyPair keypair;
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(bits, random);
            keypair = keyGen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
        String[] paths;
        try {
            paths = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random);
        }
        catch (Throwable t) {
            SelfSignedCertificate.logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t);
            try {
                paths = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random);
            }
            catch (Throwable t2) {
                SelfSignedCertificate.logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t2);
                throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.");
            }
        }
        this.certificate = new File(paths[0]);
        this.privateKey = new File(paths[1]);
    }
    
    public File certificate() {
        return this.certificate;
    }
    
    public File privateKey() {
        return this.privateKey;
    }
    
    public void delete() {
        safeDelete(this.certificate);
        safeDelete(this.privateKey);
    }
    
    static String[] newSelfSignedCertificate(final String fqdn, final PrivateKey key, final X509Certificate cert) throws IOException, CertificateEncodingException {
        final String keyText = "-----BEGIN PRIVATE KEY-----\n" + Base64.encode(Unpooled.wrappedBuffer(key.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
        final File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
        keyFile.deleteOnExit();
        OutputStream keyOut = new FileOutputStream(keyFile);
        try {
            keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
            keyOut.close();
            keyOut = null;
        }
        finally {
            if (keyOut != null) {
                safeClose(keyFile, keyOut);
                safeDelete(keyFile);
            }
        }
        final String certText = "-----BEGIN CERTIFICATE-----\n" + Base64.encode(Unpooled.wrappedBuffer(cert.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
        final File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
        certFile.deleteOnExit();
        OutputStream certOut = new FileOutputStream(certFile);
        try {
            certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
            certOut.close();
            certOut = null;
        }
        finally {
            if (certOut != null) {
                safeClose(certFile, certOut);
                safeDelete(certFile);
                safeDelete(keyFile);
            }
        }
        return new String[] { certFile.getPath(), keyFile.getPath() };
    }
    
    private static void safeDelete(final File certFile) {
        if (!certFile.delete()) {
            SelfSignedCertificate.logger.warn("Failed to delete a file: " + certFile);
        }
    }
    
    private static void safeClose(final File keyFile, final OutputStream keyOut) {
        try {
            keyOut.close();
        }
        catch (IOException e) {
            SelfSignedCertificate.logger.warn("Failed to close a file: " + keyFile, e);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
        NOT_BEFORE = new Date(System.currentTimeMillis() - 31536000000L);
        NOT_AFTER = new Date(253402300799000L);
    }
}
