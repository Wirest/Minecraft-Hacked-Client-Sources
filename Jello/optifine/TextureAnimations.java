package optifine;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class TextureAnimations
{
    private static TextureAnimation[] textureAnimations = null;

    public static void reset()
    {
        textureAnimations = null;
    }

    public static void update()
    {
        textureAnimations = null;
        IResourcePack[] rps = Config.getResourcePacks();
        textureAnimations = getTextureAnimations(rps);

        if (Config.isAnimatedTextures())
        {
            updateAnimations();
        }
    }

    public static void updateCustomAnimations()
    {
        if (textureAnimations != null)
        {
            if (Config.isAnimatedTextures())
            {
                updateAnimations();
            }
        }
    }

    public static void updateAnimations()
    {
        if (textureAnimations != null)
        {
            for (int i = 0; i < textureAnimations.length; ++i)
            {
                TextureAnimation anim = textureAnimations[i];
                anim.updateTexture();
            }
        }
    }

    public static TextureAnimation[] getTextureAnimations(IResourcePack[] rps)
    {
        ArrayList list = new ArrayList();

        for (int anims = 0; anims < rps.length; ++anims)
        {
            IResourcePack rp = rps[anims];
            TextureAnimation[] tas = getTextureAnimations(rp);

            if (tas != null)
            {
                list.addAll(Arrays.asList(tas));
            }
        }

        TextureAnimation[] var5 = (TextureAnimation[])((TextureAnimation[])list.toArray(new TextureAnimation[list.size()]));
        return var5;
    }

    public static TextureAnimation[] getTextureAnimations(IResourcePack rp)
    {
        String[] animPropNames = ResUtils.collectFiles(rp, "mcpatcher/anim", ".properties", (String[])null);

        if (animPropNames.length <= 0)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();

            for (int anims = 0; anims < animPropNames.length; ++anims)
            {
                String propName = animPropNames[anims];
                Config.dbg("Texture animation: " + propName);

                try
                {
                    ResourceLocation e = new ResourceLocation(propName);
                    InputStream in = rp.getInputStream(e);
                    Properties props = new Properties();
                    props.load(in);
                    TextureAnimation anim = makeTextureAnimation(props, e);

                    if (anim != null)
                    {
                        ResourceLocation locDstTex = new ResourceLocation(anim.getDstTex());

                        if (Config.getDefiningResourcePack(locDstTex) != rp)
                        {
                            Config.dbg("Skipped: " + propName + ", target texture not loaded from same resource pack");
                        }
                        else
                        {
                            list.add(anim);
                        }
                    }
                }
                catch (FileNotFoundException var10)
                {
                    Config.warn("File not found: " + var10.getMessage());
                }
                catch (IOException var11)
                {
                    var11.printStackTrace();
                }
            }

            TextureAnimation[] var12 = (TextureAnimation[])((TextureAnimation[])list.toArray(new TextureAnimation[list.size()]));
            return var12;
        }
    }

    public static TextureAnimation makeTextureAnimation(Properties props, ResourceLocation propLoc)
    {
        String texFrom = props.getProperty("from");
        String texTo = props.getProperty("to");
        int x = Config.parseInt(props.getProperty("x"), -1);
        int y = Config.parseInt(props.getProperty("y"), -1);
        int width = Config.parseInt(props.getProperty("w"), -1);
        int height = Config.parseInt(props.getProperty("h"), -1);

        if (texFrom != null && texTo != null)
        {
            if (x >= 0 && y >= 0 && width >= 0 && height >= 0)
            {
                texFrom = texFrom.trim();
                texTo = texTo.trim();
                String basePath = TextureUtils.getBasePath(propLoc.getResourcePath());
                texFrom = TextureUtils.fixResourcePath(texFrom, basePath);
                texTo = TextureUtils.fixResourcePath(texTo, basePath);
                byte[] imageBytes = getCustomTextureData(texFrom, width);

                if (imageBytes == null)
                {
                    Config.warn("TextureAnimation: Source texture not found: " + texTo);
                    return null;
                }
                else
                {
                    ResourceLocation locTexTo = new ResourceLocation(texTo);

                    if (!Config.hasResource(locTexTo))
                    {
                        Config.warn("TextureAnimation: Target texture not found: " + texTo);
                        return null;
                    }
                    else
                    {
                        TextureAnimation anim = new TextureAnimation(texFrom, imageBytes, texTo, locTexTo, x, y, width, height, props, 1);
                        return anim;
                    }
                }
            }
            else
            {
                Config.warn("TextureAnimation: Invalid coordinates");
                return null;
            }
        }
        else
        {
            Config.warn("TextureAnimation: Source or target texture not specified");
            return null;
        }
    }

    public static byte[] getCustomTextureData(String imagePath, int tileWidth)
    {
        byte[] imageBytes = loadImage(imagePath, tileWidth);

        if (imageBytes == null)
        {
            imageBytes = loadImage("/anim" + imagePath, tileWidth);
        }

        return imageBytes;
    }

    private static byte[] loadImage(String name, int targetWidth)
    {
        GameSettings options = Config.getGameSettings();

        try
        {
            ResourceLocation e = new ResourceLocation(name);
            InputStream in = Config.getResourceStream(e);

            if (in == null)
            {
                return null;
            }
            else
            {
                BufferedImage image = readTextureImage(in);
                in.close();

                if (image == null)
                {
                    return null;
                }
                else
                {
                    if (targetWidth > 0 && image.getWidth() != targetWidth)
                    {
                        double width = (double)(image.getHeight() / image.getWidth());
                        int ai = (int)((double)targetWidth * width);
                        image = scaleBufferedImage(image, targetWidth, ai);
                    }

                    int var20 = image.getWidth();
                    int height = image.getHeight();
                    int[] var21 = new int[var20 * height];
                    byte[] byteBuf = new byte[var20 * height * 4];
                    image.getRGB(0, 0, var20, height, var21, 0, var20);

                    for (int l = 0; l < var21.length; ++l)
                    {
                        int alpha = var21[l] >> 24 & 255;
                        int red = var21[l] >> 16 & 255;
                        int green = var21[l] >> 8 & 255;
                        int blue = var21[l] & 255;

                        if (options != null && options.anaglyph)
                        {
                            int j3 = (red * 30 + green * 59 + blue * 11) / 100;
                            int l3 = (red * 30 + green * 70) / 100;
                            int j4 = (red * 30 + blue * 70) / 100;
                            red = j3;
                            green = l3;
                            blue = j4;
                        }

                        byteBuf[l * 4 + 0] = (byte)red;
                        byteBuf[l * 4 + 1] = (byte)green;
                        byteBuf[l * 4 + 2] = (byte)blue;
                        byteBuf[l * 4 + 3] = (byte)alpha;
                    }

                    return byteBuf;
                }
            }
        }
        catch (FileNotFoundException var18)
        {
            return null;
        }
        catch (Exception var19)
        {
            var19.printStackTrace();
            return null;
        }
    }

    private static BufferedImage readTextureImage(InputStream par1InputStream) throws IOException
    {
        BufferedImage var2 = ImageIO.read(par1InputStream);
        par1InputStream.close();
        return var2;
    }

    public static BufferedImage scaleBufferedImage(BufferedImage image, int width, int height)
    {
        BufferedImage scaledImage = new BufferedImage(width, height, 2);
        Graphics2D gr = scaledImage.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gr.drawImage(image, 0, 0, width, height, (ImageObserver)null);
        return scaledImage;
    }
}
