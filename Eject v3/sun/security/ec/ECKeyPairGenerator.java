package sun.security.ec;

import sun.security.ec.point.AffinePoint;
import sun.security.ec.point.MutablePoint;
import sun.security.jca.JCAUtil;
import sun.security.util.ECUtil;
import sun.security.util.SecurityProviderConstants;
import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.Optional;

public final class ECKeyPairGenerator
        extends KeyPairGeneratorSpi {
    private static final int KEY_SIZE_MIN = 112;
    private static final int KEY_SIZE_MAX = 571;
    private SecureRandom random;
    private int keySize;
    private AlgorithmParameterSpec params = null;

    public ECKeyPairGenerator() {
        initialize(SecurityProviderConstants.DEF_EC_KEY_SIZE, null);
    }

    private static void ensureCurveIsSupported(ECParameterSpec paramECParameterSpec)
            throws InvalidAlgorithmParameterException {
        AlgorithmParameters localAlgorithmParameters = ECUtil.getECParameters(null, true);
        byte[] arrayOfByte;
        try {
            localAlgorithmParameters.init(paramECParameterSpec);
            arrayOfByte = localAlgorithmParameters.getEncoded();
        } catch (InvalidParameterSpecException localInvalidParameterSpecException) {
            throw new InvalidAlgorithmParameterException("Unsupported curve: " + paramECParameterSpec.toString());
        } catch (IOException localIOException) {
            throw new RuntimeException(localIOException);
        }
        if (!isCurveSupported(arrayOfByte)) {
            throw new InvalidAlgorithmParameterException("Unsupported curve: " + localAlgorithmParameters.toString());
        }
    }

    private static native boolean isCurveSupported(byte[] paramArrayOfByte);

    private static native Object[] generateECKeyPair(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
            throws GeneralSecurityException;

    public void initialize(int paramInt, SecureRandom paramSecureRandom) {
        checkKeySize(paramInt);
        this.params = ECUtil.getECParameterSpec(null, paramInt);
        if (this.params == null) {
            throw new InvalidParameterException("No EC parameters available for key size " + paramInt + " bits");
        }
        this.random = paramSecureRandom;
    }

    public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
            throws InvalidAlgorithmParameterException {
        ECParameterSpec localECParameterSpec = null;
        Object localObject;
        if ((paramAlgorithmParameterSpec instanceof ECParameterSpec)) {
            localObject = (ECParameterSpec) paramAlgorithmParameterSpec;
            localECParameterSpec = ECUtil.getECParameterSpec(null, (ECParameterSpec) localObject);
            if (localECParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("Unsupported curve: " + paramAlgorithmParameterSpec);
            }
        } else if ((paramAlgorithmParameterSpec instanceof ECGenParameterSpec)) {
            localObject = ((ECGenParameterSpec) paramAlgorithmParameterSpec).getName();
            localECParameterSpec = ECUtil.getECParameterSpec(null, (String) localObject);
            if (localECParameterSpec == null) {
                throw new InvalidAlgorithmParameterException("Unknown curve name: " + (String) localObject);
            }
        } else {
            throw new InvalidAlgorithmParameterException("ECParameterSpec or ECGenParameterSpec required for EC");
        }
        ensureCurveIsSupported(localECParameterSpec);
        this.params = localECParameterSpec;
        this.keySize = localECParameterSpec.getCurve().getField().getFieldSize();
        this.random = paramSecureRandom;
    }

    public KeyPair generateKeyPair() {
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        try {
            Optional localOptional = generateKeyPairImpl(this.random);
            if (localOptional.isPresent()) {
                return (KeyPair) localOptional.get();
            }
            return generateKeyPairNative(this.random);
        } catch (Exception localException) {
            throw new ProviderException(localException);
        }
    }

    private byte[] generatePrivateScalar(SecureRandom paramSecureRandom, ECOperations paramECOperations, int paramInt) {
        int i = 128;
        byte[] arrayOfByte = new byte[paramInt];
        int j = 0;
        while (j < i) {
            paramSecureRandom.nextBytes(arrayOfByte);
            try {
                return paramECOperations.seedToScalar(arrayOfByte);
            } catch (ECOperations.IntermediateValueException localIntermediateValueException) {
                j++;
            }
        }
        throw new ProviderException("Unable to produce private key after " + i + " attempts");
    }

    private Optional<KeyPair> generateKeyPairImpl(SecureRandom paramSecureRandom)
            throws InvalidKeyException {
        ECParameterSpec localECParameterSpec = (ECParameterSpec) this.params;
        Optional localOptional = ECOperations.forParameters(localECParameterSpec);
        if (!localOptional.isPresent()) {
            return Optional.empty();
        }
        ECOperations localECOperations = (ECOperations) localOptional.get();
        IntegerFieldModuloP localIntegerFieldModuloP = localECOperations.getField();
        int i = localECParameterSpec.getOrder().bitLength();
        int j = i | 0x40;
        int k = -8;
        byte[] arrayOfByte = generatePrivateScalar(paramSecureRandom, localECOperations, k);
        ECPoint localECPoint1 = localECParameterSpec.getGenerator();
        ImmutableIntegerModuloP localImmutableIntegerModuloP1 = localIntegerFieldModuloP.getElement(localECPoint1.getAffineX());
        ImmutableIntegerModuloP localImmutableIntegerModuloP2 = localIntegerFieldModuloP.getElement(localECPoint1.getAffineY());
        AffinePoint localAffinePoint1 = new AffinePoint(localImmutableIntegerModuloP1, localImmutableIntegerModuloP2);
        MutablePoint localMutablePoint = localECOperations.multiply(localAffinePoint1, arrayOfByte);
        AffinePoint localAffinePoint2 = localMutablePoint.asAffine();
        ECPrivateKeyImpl localECPrivateKeyImpl = new ECPrivateKeyImpl(arrayOfByte, localECParameterSpec);
        ECPoint localECPoint2 = new ECPoint(localAffinePoint2.getX().asBigInteger(), localAffinePoint2.getY().asBigInteger());
        ECPublicKeyImpl localECPublicKeyImpl = new ECPublicKeyImpl(localECPoint2, localECParameterSpec);
        return Optional.of(new KeyPair(localECPublicKeyImpl, localECPrivateKeyImpl));
    }

    private KeyPair generateKeyPairNative(SecureRandom paramSecureRandom)
            throws Exception {
        ECParameterSpec localECParameterSpec = (ECParameterSpec) this.params;
        byte[] arrayOfByte1 = ECUtil.encodeECParameterSpec(null, localECParameterSpec);
        byte[] arrayOfByte2 = new byte[((this.keySize | 0x7) & 0x3 | 0x1) * 2];
        paramSecureRandom.nextBytes(arrayOfByte2);
        Object[] arrayOfObject = generateECKeyPair(this.keySize, arrayOfByte1, arrayOfByte2);
        BigInteger localBigInteger = new BigInteger(1, (byte[]) arrayOfObject[0]);
        ECPrivateKeyImpl localECPrivateKeyImpl = new ECPrivateKeyImpl(localBigInteger, localECParameterSpec);
        byte[] arrayOfByte3 = (byte[]) arrayOfObject[1];
        ECPoint localECPoint = ECUtil.decodePoint(arrayOfByte3, localECParameterSpec.getCurve());
        ECPublicKeyImpl localECPublicKeyImpl = new ECPublicKeyImpl(localECPoint, localECParameterSpec);
        return new KeyPair(localECPublicKeyImpl, localECPrivateKeyImpl);
    }

    private void checkKeySize(int paramInt)
            throws InvalidParameterException {
        if (paramInt < 112) {
            throw new InvalidParameterException("Key size must be at least 112 bits");
        }
        if (paramInt > 571) {
            throw new InvalidParameterException("Key size must be at most 571 bits");
        }
        this.keySize = paramInt;
    }
}




