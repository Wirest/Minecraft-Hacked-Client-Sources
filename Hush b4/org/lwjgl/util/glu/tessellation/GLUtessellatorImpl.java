// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

import org.lwjgl.util.glu.GLUtessellatorCallbackAdapter;
import org.lwjgl.util.glu.GLUtessellatorCallback;
import org.lwjgl.util.glu.GLUtessellator;

public class GLUtessellatorImpl implements GLUtessellator
{
    public static final int TESS_MAX_CACHE = 100;
    private int state;
    private GLUhalfEdge lastEdge;
    GLUmesh mesh;
    double[] normal;
    double[] sUnit;
    double[] tUnit;
    private double relTolerance;
    int windingRule;
    boolean fatalError;
    Dict dict;
    PriorityQ pq;
    GLUvertex event;
    boolean flagBoundary;
    boolean boundaryOnly;
    GLUface lonelyTriList;
    private boolean flushCacheOnNextVertex;
    int cacheCount;
    CachedVertex[] cache;
    private Object polygonData;
    private GLUtessellatorCallback callBegin;
    private GLUtessellatorCallback callEdgeFlag;
    private GLUtessellatorCallback callVertex;
    private GLUtessellatorCallback callEnd;
    private GLUtessellatorCallback callError;
    private GLUtessellatorCallback callCombine;
    private GLUtessellatorCallback callBeginData;
    private GLUtessellatorCallback callEdgeFlagData;
    private GLUtessellatorCallback callVertexData;
    private GLUtessellatorCallback callEndData;
    private GLUtessellatorCallback callErrorData;
    private GLUtessellatorCallback callCombineData;
    private static final double GLU_TESS_DEFAULT_TOLERANCE = 0.0;
    private static GLUtessellatorCallback NULL_CB;
    
    public GLUtessellatorImpl() {
        this.normal = new double[3];
        this.sUnit = new double[3];
        this.tUnit = new double[3];
        this.cache = new CachedVertex[100];
        this.state = 0;
        this.normal[0] = 0.0;
        this.normal[1] = 0.0;
        this.normal[2] = 0.0;
        this.relTolerance = 0.0;
        this.windingRule = 100130;
        this.flagBoundary = false;
        this.boundaryOnly = false;
        this.callBegin = GLUtessellatorImpl.NULL_CB;
        this.callEdgeFlag = GLUtessellatorImpl.NULL_CB;
        this.callVertex = GLUtessellatorImpl.NULL_CB;
        this.callEnd = GLUtessellatorImpl.NULL_CB;
        this.callError = GLUtessellatorImpl.NULL_CB;
        this.callCombine = GLUtessellatorImpl.NULL_CB;
        this.callBeginData = GLUtessellatorImpl.NULL_CB;
        this.callEdgeFlagData = GLUtessellatorImpl.NULL_CB;
        this.callVertexData = GLUtessellatorImpl.NULL_CB;
        this.callEndData = GLUtessellatorImpl.NULL_CB;
        this.callErrorData = GLUtessellatorImpl.NULL_CB;
        this.callCombineData = GLUtessellatorImpl.NULL_CB;
        this.polygonData = null;
        for (int i = 0; i < this.cache.length; ++i) {
            this.cache[i] = new CachedVertex();
        }
    }
    
    public static GLUtessellator gluNewTess() {
        return new GLUtessellatorImpl();
    }
    
    private void makeDormant() {
        if (this.mesh != null) {
            Mesh.__gl_meshDeleteMesh(this.mesh);
        }
        this.state = 0;
        this.lastEdge = null;
        this.mesh = null;
    }
    
    private void requireState(final int newState) {
        if (this.state != newState) {
            this.gotoState(newState);
        }
    }
    
    private void gotoState(final int newState) {
        while (this.state != newState) {
            if (this.state < newState) {
                if (this.state == 0) {
                    this.callErrorOrErrorData(100151);
                    this.gluTessBeginPolygon(null);
                }
                else {
                    if (this.state != 1) {
                        continue;
                    }
                    this.callErrorOrErrorData(100152);
                    this.gluTessBeginContour();
                }
            }
            else if (this.state == 2) {
                this.callErrorOrErrorData(100154);
                this.gluTessEndContour();
            }
            else {
                if (this.state != 1) {
                    continue;
                }
                this.callErrorOrErrorData(100153);
                this.makeDormant();
            }
        }
    }
    
    public void gluDeleteTess() {
        this.requireState(0);
    }
    
    public void gluTessProperty(final int which, final double value) {
        Label_0107: {
            switch (which) {
                case 100142: {
                    if (value < 0.0) {
                        break;
                    }
                    if (value > 1.0) {
                        break;
                    }
                    this.relTolerance = value;
                    return;
                }
                case 100140: {
                    final int windingRule = (int)value;
                    if (windingRule != value) {
                        break;
                    }
                    switch (windingRule) {
                        case 100130:
                        case 100131:
                        case 100132:
                        case 100133:
                        case 100134: {
                            this.windingRule = windingRule;
                            return;
                        }
                        default: {
                            break Label_0107;
                        }
                    }
                    break;
                }
                case 100141: {
                    this.boundaryOnly = (value != 0.0);
                    return;
                }
                default: {
                    this.callErrorOrErrorData(100900);
                    return;
                }
            }
        }
        this.callErrorOrErrorData(100901);
    }
    
    public void gluGetTessProperty(final int which, final double[] value, final int value_offset) {
        switch (which) {
            case 100142: {
                assert 0.0 <= this.relTolerance && this.relTolerance <= 1.0;
                value[value_offset] = this.relTolerance;
                break;
            }
            case 100140: {
                assert this.windingRule == 100134;
                value[value_offset] = this.windingRule;
                break;
            }
            case 100141: {
                assert !this.boundaryOnly;
                value[value_offset] = (this.boundaryOnly ? 1.0 : 0.0);
                break;
            }
            default: {
                value[value_offset] = 0.0;
                this.callErrorOrErrorData(100900);
                break;
            }
        }
    }
    
    public void gluTessNormal(final double x, final double y, final double z) {
        this.normal[0] = x;
        this.normal[1] = y;
        this.normal[2] = z;
    }
    
    public void gluTessCallback(final int which, final GLUtessellatorCallback aCallback) {
        switch (which) {
            case 100100: {
                this.callBegin = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100106: {
                this.callBeginData = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100104: {
                this.callEdgeFlag = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
                this.flagBoundary = (aCallback != null);
            }
            case 100110: {
                GLUtessellatorCallback null_CB;
                GLUtessellatorCallback callEdgeFlagData;
                if (aCallback == null) {
                    callEdgeFlagData = (null_CB = GLUtessellatorImpl.NULL_CB);
                }
                else {
                    callEdgeFlagData = aCallback;
                    null_CB = aCallback;
                }
                this.callBegin = null_CB;
                this.callEdgeFlagData = callEdgeFlagData;
                this.flagBoundary = (aCallback != null);
            }
            case 100101: {
                this.callVertex = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100107: {
                this.callVertexData = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100102: {
                this.callEnd = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100108: {
                this.callEndData = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100103: {
                this.callError = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100109: {
                this.callErrorData = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100105: {
                this.callCombine = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            case 100111: {
                this.callCombineData = ((aCallback == null) ? GLUtessellatorImpl.NULL_CB : aCallback);
            }
            default: {
                this.callErrorOrErrorData(100900);
            }
        }
    }
    
    private boolean addVertex(final double[] coords, final Object vertexData) {
        GLUhalfEdge e = this.lastEdge;
        if (e == null) {
            e = Mesh.__gl_meshMakeEdge(this.mesh);
            if (e == null) {
                return false;
            }
            if (!Mesh.__gl_meshSplice(e, e.Sym)) {
                return false;
            }
        }
        else {
            if (Mesh.__gl_meshSplitEdge(e) == null) {
                return false;
            }
            e = e.Lnext;
        }
        e.Org.data = vertexData;
        e.Org.coords[0] = coords[0];
        e.Org.coords[1] = coords[1];
        e.Org.coords[2] = coords[2];
        e.winding = 1;
        e.Sym.winding = -1;
        this.lastEdge = e;
        return true;
    }
    
    private void cacheVertex(final double[] coords, final Object vertexData) {
        if (this.cache[this.cacheCount] == null) {
            this.cache[this.cacheCount] = new CachedVertex();
        }
        final CachedVertex v = this.cache[this.cacheCount];
        v.data = vertexData;
        v.coords[0] = coords[0];
        v.coords[1] = coords[1];
        v.coords[2] = coords[2];
        ++this.cacheCount;
    }
    
    private boolean flushCache() {
        final CachedVertex[] v = this.cache;
        this.mesh = Mesh.__gl_meshNewMesh();
        if (this.mesh == null) {
            return false;
        }
        for (int i = 0; i < this.cacheCount; ++i) {
            final CachedVertex vertex = v[i];
            if (!this.addVertex(vertex.coords, vertex.data)) {
                return false;
            }
        }
        this.cacheCount = 0;
        this.flushCacheOnNextVertex = false;
        return true;
    }
    
    public void gluTessVertex(final double[] coords, final int coords_offset, final Object vertexData) {
        boolean tooLarge = false;
        final double[] clamped = new double[3];
        this.requireState(2);
        if (this.flushCacheOnNextVertex) {
            if (!this.flushCache()) {
                this.callErrorOrErrorData(100902);
                return;
            }
            this.lastEdge = null;
        }
        for (int i = 0; i < 3; ++i) {
            double x = coords[i + coords_offset];
            if (x < -1.0E150) {
                x = -1.0E150;
                tooLarge = true;
            }
            if (x > 1.0E150) {
                x = 1.0E150;
                tooLarge = true;
            }
            clamped[i] = x;
        }
        if (tooLarge) {
            this.callErrorOrErrorData(100155);
        }
        if (this.mesh == null) {
            if (this.cacheCount < 100) {
                this.cacheVertex(clamped, vertexData);
                return;
            }
            if (!this.flushCache()) {
                this.callErrorOrErrorData(100902);
                return;
            }
        }
        if (!this.addVertex(clamped, vertexData)) {
            this.callErrorOrErrorData(100902);
        }
    }
    
    public void gluTessBeginPolygon(final Object data) {
        this.requireState(0);
        this.state = 1;
        this.cacheCount = 0;
        this.flushCacheOnNextVertex = false;
        this.mesh = null;
        this.polygonData = data;
    }
    
    public void gluTessBeginContour() {
        this.requireState(1);
        this.state = 2;
        this.lastEdge = null;
        if (this.cacheCount > 0) {
            this.flushCacheOnNextVertex = true;
        }
    }
    
    public void gluTessEndContour() {
        this.requireState(2);
        this.state = 1;
    }
    
    public void gluTessEndPolygon() {
        try {
            this.requireState(1);
            this.state = 0;
            if (this.mesh == null) {
                if (!this.flagBoundary && Render.__gl_renderCache(this)) {
                    this.polygonData = null;
                    return;
                }
                if (!this.flushCache()) {
                    throw new RuntimeException();
                }
            }
            Normal.__gl_projectPolygon(this);
            if (!Sweep.__gl_computeInterior(this)) {
                throw new RuntimeException();
            }
            GLUmesh mesh = this.mesh;
            if (!this.fatalError) {
                boolean rc = true;
                if (this.boundaryOnly) {
                    rc = TessMono.__gl_meshSetWindingNumber(mesh, 1, true);
                }
                else {
                    rc = TessMono.__gl_meshTessellateInterior(mesh);
                }
                if (!rc) {
                    throw new RuntimeException();
                }
                Mesh.__gl_meshCheckMesh(mesh);
                if (this.callBegin != GLUtessellatorImpl.NULL_CB || this.callEnd != GLUtessellatorImpl.NULL_CB || this.callVertex != GLUtessellatorImpl.NULL_CB || this.callEdgeFlag != GLUtessellatorImpl.NULL_CB || this.callBeginData != GLUtessellatorImpl.NULL_CB || this.callEndData != GLUtessellatorImpl.NULL_CB || this.callVertexData != GLUtessellatorImpl.NULL_CB || this.callEdgeFlagData != GLUtessellatorImpl.NULL_CB) {
                    if (this.boundaryOnly) {
                        Render.__gl_renderBoundary(this, mesh);
                    }
                    else {
                        Render.__gl_renderMesh(this, mesh);
                    }
                }
            }
            Mesh.__gl_meshDeleteMesh(mesh);
            this.polygonData = null;
            mesh = null;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.callErrorOrErrorData(100902);
        }
    }
    
    public void gluBeginPolygon() {
        this.gluTessBeginPolygon(null);
        this.gluTessBeginContour();
    }
    
    public void gluNextContour(final int type) {
        this.gluTessEndContour();
        this.gluTessBeginContour();
    }
    
    public void gluEndPolygon() {
        this.gluTessEndContour();
        this.gluTessEndPolygon();
    }
    
    void callBeginOrBeginData(final int a) {
        if (this.callBeginData != GLUtessellatorImpl.NULL_CB) {
            this.callBeginData.beginData(a, this.polygonData);
        }
        else {
            this.callBegin.begin(a);
        }
    }
    
    void callVertexOrVertexData(final Object a) {
        if (this.callVertexData != GLUtessellatorImpl.NULL_CB) {
            this.callVertexData.vertexData(a, this.polygonData);
        }
        else {
            this.callVertex.vertex(a);
        }
    }
    
    void callEdgeFlagOrEdgeFlagData(final boolean a) {
        if (this.callEdgeFlagData != GLUtessellatorImpl.NULL_CB) {
            this.callEdgeFlagData.edgeFlagData(a, this.polygonData);
        }
        else {
            this.callEdgeFlag.edgeFlag(a);
        }
    }
    
    void callEndOrEndData() {
        if (this.callEndData != GLUtessellatorImpl.NULL_CB) {
            this.callEndData.endData(this.polygonData);
        }
        else {
            this.callEnd.end();
        }
    }
    
    void callCombineOrCombineData(final double[] coords, final Object[] vertexData, final float[] weights, final Object[] outData) {
        if (this.callCombineData != GLUtessellatorImpl.NULL_CB) {
            this.callCombineData.combineData(coords, vertexData, weights, outData, this.polygonData);
        }
        else {
            this.callCombine.combine(coords, vertexData, weights, outData);
        }
    }
    
    void callErrorOrErrorData(final int a) {
        if (this.callErrorData != GLUtessellatorImpl.NULL_CB) {
            this.callErrorData.errorData(a, this.polygonData);
        }
        else {
            this.callError.error(a);
        }
    }
    
    static {
        GLUtessellatorImpl.NULL_CB = new GLUtessellatorCallbackAdapter();
    }
}
