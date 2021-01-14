package info.sigmaclient.module.impl.other;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.ModuleManager;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.movement.Bhop;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.movement.LongJump;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.misc.BlockUtils;
import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class ButPainSub {
    private Fly flyModule;
    private Scaffold scaffoldModule;
    private LongJump longjumpModule;
    private Bhop bhopModule;
    private boolean qualify;
    private Module fakeFlyModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            if (event instanceof EventUpdate) {

                EventUpdate em = (EventUpdate) event;

                mc.thePlayer.motionY = 0;
                mc.thePlayer.jumpMovementFactor = 0.31f + MoveUtils.getSpeedEffect() * 0.05f;
                if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2, mc.thePlayer.posZ);
                }
            }
        }
    };
    private Module fakeScaffoldModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            mc.thePlayer.motionX *= 0.9;
            mc.thePlayer.motionZ *= 0.9;
        }
    };
    private Module fakeLongjumpModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            float x2 = 1f + MoveUtils.getSpeedEffect() * 0.45f;
            if ((mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) && mc.thePlayer.onGround) {
                mc.thePlayer.motionX *= 1;
                mc.thePlayer.motionZ *= 1;
                mc.thePlayer.jump();
            }
            if (mc.thePlayer.onGround && BlockUtils.isOnGround(0.01)) {

            } else {
                mc.thePlayer.motionX *= 0;
                mc.thePlayer.motionZ *= 0;
                mc.thePlayer.jumpMovementFactor = 0.28f;
            }
        }
    };
    private Module fakeBhopModule = new Module(new ModuleData(ModuleData.Type.Other, "", "")) {
        @Override
        public void onEvent(Event event) {
            if (event instanceof EventUpdate) {
                if (mc.thePlayer.onGround) {
                    if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionX *= 0.9;
                        mc.thePlayer.motionZ *= 0.9;
                    }
                } else {
                    mc.thePlayer.motionX *= 1.1;
                    mc.thePlayer.motionZ *= 1.1;
                }
            }
        }
    };

    public ButPainSub() {
        ModuleManager m = Client.getModuleManager();
        flyModule = (Fly) m.get(Fly.class);
        scaffoldModule = (Scaffold) m.get(Scaffold.class);
        longjumpModule = (LongJump) m.get(LongJump.class);
        bhopModule = (Bhop) m.get(Bhop.class);

        /*try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.genKeyPair();
            System.out.println(new String(Base64.encodeBase64(keyPair.getPublic().getEncoded())));
            System.out.println(" ");
            System.out.println(new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())));

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(""));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            Signature sig = Signature.getInstance("SHA512withRSA");
            sig.initSign(privateKey);
            sig.update(Base64.encodeBase64(new byte[]{1,2,3,4}));
            System.out.println(new String(Base64.encodeBase64(sig.sign())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }*/

        try {
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgIty8oef2oqaVxZWZKDPPUdQ1yxA0FMtlGS/fr1cdx4+o6jguvKnuKZl6e5HNZzTS2g/bKUyVVVLsIH7JcF/zQgkQckyacMlhHCz72CvbmyFNJ+lUoUQGm23mQ/N6FXcyBvU8/dN+PS6kp9Jd/GGuRbMxQ1puuf130L56jNQzSZ+liFfqh+1JBZyLSbXJDhemayYr3DaDPtdL+hoJPansDl0DSIMRTkaufsnca1bES1C+lRU2rbDzB+H8aivAVkfAQhJjDdVqV6BUYkbFsyu1Lfx7hPTBUpFOfMtyK3FMEsI8ir3tCuF70qGksuORDU3IYmtfy7qA4CBWCYldH7ScwIDAQAB"));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey serverKey = keyFactory.generatePublic(publicKeySpec);

            Signature sig = Signature.getInstance("SHA512withRSA");
            sig.initVerify(serverKey);
            String premsTimestamp = Client.um.getPremsTimestamp();
            if (premsTimestamp != null) {
                String s[] = premsTimestamp.split(":");
                byte[] timestamp = Base64.decodeBase64(s[0]);
                byte[] signature = Base64.decodeBase64(s[1]);
                sig.update(timestamp);
                if (sig.verify(signature)) {
                    long timestampL = ((timestamp[0] & 0xFF) << 24)
                            + ((timestamp[1] & 0xFF) << 16)
                            + ((timestamp[2] & 0xFF) << 8)
                            + (timestamp[3] & 0xFF);
                    if (Math.abs(System.currentTimeMillis() - timestampL * 1000) < 12 * 60 * 60 * 1000) {
                        qualify = true;
                    }
                }
            }
        } catch (Exception ignore) {

        }
    }

    public void onUpdate() {
        if (!qualify) {
            if (flyModule.getPremiumAddon() != null) {
                flyModule.setPremiumAddon(fakeFlyModule);
            }
            if (scaffoldModule.getPremiumAddon() != null) {
                scaffoldModule.setPremiumAddon(fakeScaffoldModule);
            }
            if (longjumpModule.getPremiumAddon() != null) {
                longjumpModule.setPremiumAddon(fakeLongjumpModule);
            }
            if (bhopModule.getPremiumAddon() != null) {
                bhopModule.setPremiumAddon(fakeBhopModule);
            }
        }
    }
}
