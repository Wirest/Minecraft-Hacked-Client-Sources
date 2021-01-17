// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import java.security.PrivateKey;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import java.util.Random;
import java.math.BigInteger;
import org.bouncycastle.asn1.x500.X500Name;
import java.security.SecureRandom;
import java.security.KeyPair;
import java.security.Provider;

final class BouncyCastleSelfSignedCertGenerator
{
    private static final Provider PROVIDER;
    
    static String[] generate(final String fqdn, final KeyPair keypair, final SecureRandom random) throws Exception {
        final PrivateKey key = keypair.getPrivate();
        final X500Name owner = new X500Name("CN=" + fqdn);
        final X509v3CertificateBuilder builder = (X509v3CertificateBuilder)new JcaX509v3CertificateBuilder(owner, new BigInteger(64, random), SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER, owner, keypair.getPublic());
        final ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(key);
        final X509CertificateHolder certHolder = builder.build(signer);
        final X509Certificate cert = new JcaX509CertificateConverter().setProvider(BouncyCastleSelfSignedCertGenerator.PROVIDER).getCertificate(certHolder);
        cert.verify(keypair.getPublic());
        return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
    }
    
    private BouncyCastleSelfSignedCertGenerator() {
    }
    
    static {
        PROVIDER = (Provider)new BouncyCastleProvider();
    }
}
