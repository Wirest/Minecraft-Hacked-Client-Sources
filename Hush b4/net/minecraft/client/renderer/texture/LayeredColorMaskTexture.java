// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.io.InputStream;
import net.minecraft.block.material.MapColor;
import java.awt.Graphics;
import java.io.IOException;
import net.minecraft.util.MathHelper;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.minecraft.client.resources.IResourceManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.item.EnumDyeColor;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class LayeredColorMaskTexture extends AbstractTexture
{
    private static final Logger LOG;
    private final ResourceLocation textureLocation;
    private final List<String> field_174949_h;
    private final List<EnumDyeColor> field_174950_i;
    
    static {
        LOG = LogManager.getLogger();
    }
    
    public LayeredColorMaskTexture(final ResourceLocation textureLocationIn, final List<String> p_i46101_2_, final List<EnumDyeColor> p_i46101_3_) {
        this.textureLocation = textureLocationIn;
        this.field_174949_h = p_i46101_2_;
        this.field_174950_i = p_i46101_3_;
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage2;
        try {
            final BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
            int i = bufferedimage1.getType();
            if (i == 0) {
                i = 6;
            }
            bufferedimage2 = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
            final Graphics graphics = bufferedimage2.getGraphics();
            graphics.drawImage(bufferedimage1, 0, 0, null);
            for (int j = 0; j < 17 && j < this.field_174949_h.size(); ++j) {
                if (j >= this.field_174950_i.size()) {
                    break;
                }
                final String s = this.field_174949_h.get(j);
                final MapColor mapcolor = this.field_174950_i.get(j).getMapColor();
                if (s != null) {
                    final InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
                    final BufferedImage bufferedimage3 = TextureUtil.readBufferedImage(inputstream);
                    if (bufferedimage3.getWidth() == bufferedimage2.getWidth() && bufferedimage3.getHeight() == bufferedimage2.getHeight() && bufferedimage3.getType() == 6) {
                        for (int k = 0; k < bufferedimage3.getHeight(); ++k) {
                            for (int l = 0; l < bufferedimage3.getWidth(); ++l) {
                                final int i2 = bufferedimage3.getRGB(l, k);
                                if ((i2 & 0xFF000000) != 0x0) {
                                    final int j2 = (i2 & 0xFF0000) << 8 & 0xFF000000;
                                    final int k2 = bufferedimage1.getRGB(l, k);
                                    final int l2 = MathHelper.func_180188_d(k2, mapcolor.colorValue) & 0xFFFFFF;
                                    bufferedimage3.setRGB(l, k, j2 | l2);
                                }
                            }
                        }
                        bufferedimage2.getGraphics().drawImage(bufferedimage3, 0, 0, null);
                    }
                }
            }
        }
        catch (IOException ioexception) {
            LayeredColorMaskTexture.LOG.error("Couldn't load layered image", ioexception);
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage2);
    }
}
