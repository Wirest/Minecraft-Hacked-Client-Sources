// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.util.ArrayList;
import java.util.List;
import java.awt.image.ConvolveOp;
import java.awt.RenderingHints;
import java.awt.image.Kernel;
import java.util.Iterator;
import java.awt.Composite;
import java.awt.AlphaComposite;
import org.newdawn.slick.font.Glyph;
import org.newdawn.slick.UnicodeFont;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class ShadowEffect implements ConfigurableEffect
{
    public static final int NUM_KERNELS = 16;
    public static final float[][] GAUSSIAN_BLUR_KERNELS;
    private Color color;
    private float opacity;
    private float xDistance;
    private float yDistance;
    private int blurKernelSize;
    private int blurPasses;
    
    static {
        GAUSSIAN_BLUR_KERNELS = generateGaussianBlurKernels(16);
    }
    
    public ShadowEffect() {
        this.color = Color.black;
        this.opacity = 0.6f;
        this.xDistance = 2.0f;
        this.yDistance = 2.0f;
        this.blurKernelSize = 0;
        this.blurPasses = 1;
    }
    
    public ShadowEffect(final Color color, final int xDistance, final int yDistance, final float opacity) {
        this.color = Color.black;
        this.opacity = 0.6f;
        this.xDistance = 2.0f;
        this.yDistance = 2.0f;
        this.blurKernelSize = 0;
        this.blurPasses = 1;
        this.color = color;
        this.xDistance = (float)xDistance;
        this.yDistance = (float)yDistance;
        this.opacity = opacity;
    }
    
    @Override
    public void draw(final BufferedImage image, Graphics2D g, final UnicodeFont unicodeFont, final Glyph glyph) {
        g = (Graphics2D)g.create();
        g.translate(this.xDistance, this.yDistance);
        g.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.round(this.opacity * 255.0f)));
        g.fill(glyph.getShape());
        for (final Effect effect : unicodeFont.getEffects()) {
            if (effect instanceof OutlineEffect) {
                final Composite composite = g.getComposite();
                g.setComposite(AlphaComposite.Src);
                g.setStroke(((OutlineEffect)effect).getStroke());
                g.draw(glyph.getShape());
                g.setComposite(composite);
                break;
            }
        }
        g.dispose();
        if (this.blurKernelSize > 1 && this.blurKernelSize < 16 && this.blurPasses > 0) {
            this.blur(image);
        }
    }
    
    private void blur(final BufferedImage image) {
        final float[] matrix = ShadowEffect.GAUSSIAN_BLUR_KERNELS[this.blurKernelSize - 1];
        final Kernel gaussianBlur1 = new Kernel(matrix.length, 1, matrix);
        final Kernel gaussianBlur2 = new Kernel(1, matrix.length, matrix);
        final RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        final ConvolveOp gaussianOp1 = new ConvolveOp(gaussianBlur1, 1, hints);
        final ConvolveOp gaussianOp2 = new ConvolveOp(gaussianBlur2, 1, hints);
        final BufferedImage scratchImage = EffectUtil.getScratchImage();
        for (int i = 0; i < this.blurPasses; ++i) {
            gaussianOp1.filter(image, scratchImage);
            gaussianOp2.filter(scratchImage, image);
        }
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public float getXDistance() {
        return this.xDistance;
    }
    
    public void setXDistance(final float distance) {
        this.xDistance = distance;
    }
    
    public float getYDistance() {
        return this.yDistance;
    }
    
    public void setYDistance(final float distance) {
        this.yDistance = distance;
    }
    
    public int getBlurKernelSize() {
        return this.blurKernelSize;
    }
    
    public void setBlurKernelSize(final int blurKernelSize) {
        this.blurKernelSize = blurKernelSize;
    }
    
    public int getBlurPasses() {
        return this.blurPasses;
    }
    
    public void setBlurPasses(final int blurPasses) {
        this.blurPasses = blurPasses;
    }
    
    public float getOpacity() {
        return this.opacity;
    }
    
    public void setOpacity(final float opacity) {
        this.opacity = opacity;
    }
    
    @Override
    public String toString() {
        return "Shadow";
    }
    
    @Override
    public List getValues() {
        final List values = new ArrayList();
        values.add(EffectUtil.colorValue("Color", this.color));
        values.add(EffectUtil.floatValue("Opacity", this.opacity, 0.0f, 1.0f, "This setting sets the translucency of the shadow."));
        values.add(EffectUtil.floatValue("X distance", this.xDistance, Float.MIN_VALUE, Float.MAX_VALUE, "This setting is the amount of pixels to offset the shadow on the x axis. The glyphs will need padding so the shadow doesn't get clipped."));
        values.add(EffectUtil.floatValue("Y distance", this.yDistance, Float.MIN_VALUE, Float.MAX_VALUE, "This setting is the amount of pixels to offset the shadow on the y axis. The glyphs will need padding so the shadow doesn't get clipped."));
        final List options = new ArrayList();
        options.add(new String[] { "None", "0" });
        for (int i = 2; i < 16; ++i) {
            options.add(new String[] { String.valueOf(i) });
        }
        final String[][] optionsArray = options.toArray(new String[options.size()][]);
        values.add(EffectUtil.optionValue("Blur kernel size", String.valueOf(this.blurKernelSize), optionsArray, "This setting controls how many neighboring pixels are used to blur the shadow. Set to \"None\" for no blur."));
        values.add(EffectUtil.intValue("Blur passes", this.blurPasses, "The setting is the number of times to apply a blur to the shadow. Set to \"0\" for no blur."));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        for (final Value value : values) {
            if (value.getName().equals("Color")) {
                this.color = (Color)value.getObject();
            }
            else if (value.getName().equals("Opacity")) {
                this.opacity = (float)value.getObject();
            }
            else if (value.getName().equals("X distance")) {
                this.xDistance = (float)value.getObject();
            }
            else if (value.getName().equals("Y distance")) {
                this.yDistance = (float)value.getObject();
            }
            else if (value.getName().equals("Blur kernel size")) {
                this.blurKernelSize = Integer.parseInt((String)value.getObject());
            }
            else {
                if (!value.getName().equals("Blur passes")) {
                    continue;
                }
                this.blurPasses = (int)value.getObject();
            }
        }
    }
    
    private static float[][] generateGaussianBlurKernels(final int level) {
        final float[][] pascalsTriangle = generatePascalsTriangle(level);
        final float[][] gaussianTriangle = new float[pascalsTriangle.length][];
        for (int i = 0; i < gaussianTriangle.length; ++i) {
            float total = 0.0f;
            gaussianTriangle[i] = new float[pascalsTriangle[i].length];
            for (int j = 0; j < pascalsTriangle[i].length; ++j) {
                total += pascalsTriangle[i][j];
            }
            final float coefficient = 1.0f / total;
            for (int k = 0; k < pascalsTriangle[i].length; ++k) {
                gaussianTriangle[i][k] = coefficient * pascalsTriangle[i][k];
            }
        }
        return gaussianTriangle;
    }
    
    private static float[][] generatePascalsTriangle(int level) {
        if (level < 2) {
            level = 2;
        }
        final float[][] triangle = new float[level][];
        triangle[0] = new float[1];
        triangle[1] = new float[2];
        triangle[0][0] = 1.0f;
        triangle[1][0] = 1.0f;
        triangle[1][1] = 1.0f;
        for (int i = 2; i < level; ++i) {
            (triangle[i] = new float[i + 1])[0] = 1.0f;
            triangle[i][i] = 1.0f;
            for (int j = 1; j < triangle[i].length - 1; ++j) {
                triangle[i][j] = triangle[i - 1][j - 1] + triangle[i - 1][j];
            }
        }
        return triangle;
    }
}
