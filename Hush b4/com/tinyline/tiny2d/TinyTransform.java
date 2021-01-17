// 
// Decompiled by Procyon v0.5.36
// 

package com.tinyline.tiny2d;

public class TinyTransform
{
    public static final short TRANSFORM_UNKNOWN = 0;
    public static final short TRANSFORM_MATRIX = 1;
    public static final short TRANSFORM_TRANSLATE = 2;
    public static final short TRANSFORM_SCALE = 3;
    public static final short TRANSFORM_ROTATE = 4;
    public static final short TRANSFORM_SKEWX = 5;
    public static final short TRANSFORM_SKEWY = 6;
    public short type;
    public TinyMatrix matrix;
    public int angle;
    public int rotateOriginX;
    public int rotateOriginY;
    
    public TinyTransform() {
        this.init();
    }
    
    public TinyTransform(final TinyTransform tinyTransform) {
        this.type = tinyTransform.type;
        this.matrix = new TinyMatrix(tinyTransform.matrix);
        this.angle = tinyTransform.angle;
        this.rotateOriginX = tinyTransform.rotateOriginX;
        this.rotateOriginY = tinyTransform.rotateOriginY;
    }
    
    public void init() {
        this.type = 1;
        this.matrix = new TinyMatrix();
        this.angle = 0;
        final int n = 0;
        this.rotateOriginY = n;
        this.rotateOriginX = n;
    }
    
    public void setMatrix(final TinyMatrix matrix) {
        this.init();
        this.matrix = matrix;
    }
    
    public void setTranslate(final int n, final int n2) {
        this.init();
        this.type = 2;
        this.matrix.translate(n, n2);
    }
    
    public void setScale(final int n, final int n2) {
        this.init();
        this.type = 3;
        this.matrix.scale(n, n2);
    }
    
    public void setRotate(final int angle, final int rotateOriginX, final int rotateOriginY) {
        this.init();
        this.type = 4;
        this.matrix.rotate(angle, rotateOriginX, rotateOriginY);
        this.angle = angle;
        this.rotateOriginX = rotateOriginX;
        this.rotateOriginY = rotateOriginY;
    }
    
    public void setSkewX(final int angle) {
        this.init();
        this.type = 5;
        this.matrix.skew(angle, 0);
        this.angle = angle;
    }
    
    public void setSkewY(final int angle) {
        this.init();
        this.type = 6;
        this.matrix.skew(0, angle);
        this.angle = angle;
    }
    
    public static TinyMatrix getTinyMatrix(final TinyVector tinyVector) {
        final TinyMatrix tinyMatrix = new TinyMatrix();
        for (int count = tinyVector.count, i = 0; i < count; ++i) {
            tinyMatrix.preConcatenate(((TinyTransform)tinyVector.data[i]).matrix);
        }
        return tinyMatrix;
    }
}
