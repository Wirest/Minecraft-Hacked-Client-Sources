package info.sigmaclient.management.users;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import info.sigmaclient.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Challenge {
    private String uid;
    private boolean captcha;
    private ResourceLocation captchaLoc = null;
    private long creationDate = System.currentTimeMillis();
    private boolean valid = true;
    private String answer = "";

    public Challenge(String uid, boolean captcha) {
        this.uid = uid;
        this.captcha = captcha;

        if (captcha) {
            MinecraftProfileTexture mpt = new MinecraftProfileTexture("https://sabrinaprg.sigmaclient.info/captcha/" + uid + ".png", new HashMap());
            ResourceLocation rl = new ResourceLocation("captcha/" + mpt.getHash());
            IImageBuffer iib = new IImageBuffer() {
                ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage var1) {
                    return var1;
                }

                public void func_152634_a() {
                    captchaLoc = rl;
                }
            };
            ThreadDownloadImageData textureArt = new ThreadDownloadImageData((File) null, mpt.getUrl(), (ResourceLocation) null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
        }
    }

    public ResourceLocation getCaptcha() {
        return captchaLoc;
    }

    public boolean isValid() {
        return valid && creationDate > System.currentTimeMillis() - 5 * 60 * 1000;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUid() {
        return uid;
    }

    /*public void compute() {
        int count = 4;
        byte[] difficulty = {0, 0, 0};
        byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] mod = {0, 0, 0, 0, 0, 0, 0, 0};
        ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();

        for (int tc = 0; tc < count; tc++) {
            int finalTc = tc;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashData = new byte[data.length + mod.length];
                        for (int i = 0; i < data.length; i++) {
                            hashData[i] = data[i];
                        }
                        hashData[hashData.length - 1] = (byte) finalTc;
                        while (true) {
                            byte[] hash = digest.digest(hashData);

                            if (isSuccessfull(hash, difficulty)) {
                                results.add(Client.um.bytesToHex(hash));
                                System.out.println("Found");
                                break;
                            }

                            for (int i = 0; i < mod.length; i++) {
                                hashData[i + data.length]++;
                                if (hashData[i + data.length] != 0) {
                                    break;
                                }
                            }
                        }

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        while (results.size() < count) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while(!results.isEmpty()) {
            System.out.println("Success " + results.poll());
        }

    }*/

    /*private boolean isSuccessfull(byte[] hash, byte[] difficulty) {
        for (int i = 0; i < difficulty.length; i++) {
            if ((hash[i] & 0xff) > (difficulty[i] & 0xff)) {
                return false;
            } else if ((hash[i] & 0xff) < (difficulty[i] & 0xff)) {
                return true;
            }
        }
        return true;
    }*/
}
