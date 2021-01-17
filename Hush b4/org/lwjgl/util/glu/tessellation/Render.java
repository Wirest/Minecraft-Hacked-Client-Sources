// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Render
{
    private static final boolean USE_OPTIMIZED_CODE_PATH = false;
    private static final RenderFan renderFan;
    private static final RenderStrip renderStrip;
    private static final RenderTriangle renderTriangle;
    private static final int SIGN_INCONSISTENT = 2;
    
    private Render() {
    }
    
    public static void __gl_renderMesh(final GLUtessellatorImpl tess, final GLUmesh mesh) {
        tess.lonelyTriList = null;
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            f.marked = false;
        }
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            if (f.inside && !f.marked) {
                RenderMaximumFaceGroup(tess, f);
                assert f.marked;
            }
        }
        if (tess.lonelyTriList != null) {
            RenderLonelyTriangles(tess, tess.lonelyTriList);
            tess.lonelyTriList = null;
        }
    }
    
    static void RenderMaximumFaceGroup(final GLUtessellatorImpl tess, final GLUface fOrig) {
        final GLUhalfEdge e = fOrig.anEdge;
        FaceCount max = new FaceCount();
        max.size = 1L;
        max.eStart = e;
        max.render = Render.renderTriangle;
        if (!tess.flagBoundary) {
            FaceCount newFace = MaximumFan(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumFan(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumFan(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e.Lnext);
            if (newFace.size > max.size) {
                max = newFace;
            }
            newFace = MaximumStrip(e.Onext.Sym);
            if (newFace.size > max.size) {
                max = newFace;
            }
        }
        max.render.render(tess, max.eStart, max.size);
    }
    
    private static boolean Marked(final GLUface f) {
        return !f.inside || f.marked;
    }
    
    private static GLUface AddToTrail(final GLUface f, final GLUface t) {
        f.trail = t;
        f.marked = true;
        return f;
    }
    
    private static void FreeTrail(GLUface t) {
        while (t != null) {
            t.marked = false;
            t = t.trail;
        }
    }
    
    static FaceCount MaximumFan(final GLUhalfEdge eOrig) {
        final FaceCount newFace = new FaceCount(0L, (GLUhalfEdge)null, (renderCallBack)Render.renderFan);
        GLUface trail = null;
        for (GLUhalfEdge e = eOrig; !Marked(e.Lface); e = e.Onext) {
            trail = AddToTrail(e.Lface, trail);
            final FaceCount faceCount = newFace;
            ++faceCount.size;
        }
        GLUhalfEdge e;
        for (e = eOrig; !Marked(e.Sym.Lface); e = e.Sym.Lnext) {
            trail = AddToTrail(e.Sym.Lface, trail);
            final FaceCount faceCount2 = newFace;
            ++faceCount2.size;
        }
        newFace.eStart = e;
        FreeTrail(trail);
        return newFace;
    }
    
    private static boolean IsEven(final long n) {
        return (n & 0x1L) == 0x0L;
    }
    
    static FaceCount MaximumStrip(final GLUhalfEdge eOrig) {
        final FaceCount newFace = new FaceCount(0L, (GLUhalfEdge)null, (renderCallBack)Render.renderStrip);
        long headSize = 0L;
        long tailSize = 0L;
        GLUface trail = null;
        GLUhalfEdge e;
        for (e = eOrig; !Marked(e.Lface); e = e.Onext) {
            trail = AddToTrail(e.Lface, trail);
            ++tailSize;
            e = e.Lnext.Sym;
            if (Marked(e.Lface)) {
                break;
            }
            trail = AddToTrail(e.Lface, trail);
            ++tailSize;
        }
        final GLUhalfEdge eTail = e;
        for (e = eOrig; !Marked(e.Sym.Lface); e = e.Sym.Onext.Sym) {
            trail = AddToTrail(e.Sym.Lface, trail);
            ++headSize;
            e = e.Sym.Lnext;
            if (Marked(e.Sym.Lface)) {
                break;
            }
            trail = AddToTrail(e.Sym.Lface, trail);
            ++headSize;
        }
        final GLUhalfEdge eHead = e;
        newFace.size = tailSize + headSize;
        if (IsEven(tailSize)) {
            newFace.eStart = eTail.Sym;
        }
        else if (IsEven(headSize)) {
            newFace.eStart = eHead;
        }
        else {
            final FaceCount faceCount = newFace;
            --faceCount.size;
            newFace.eStart = eHead.Onext;
        }
        FreeTrail(trail);
        return newFace;
    }
    
    static void RenderLonelyTriangles(final GLUtessellatorImpl tess, GLUface f) {
        int edgeState = -1;
        tess.callBeginOrBeginData(4);
        while (f != null) {
            GLUhalfEdge e = f.anEdge;
            do {
                if (tess.flagBoundary) {
                    final int newState = e.Sym.Lface.inside ? 0 : 1;
                    if (edgeState != newState) {
                        edgeState = newState;
                        tess.callEdgeFlagOrEdgeFlagData(edgeState != 0);
                    }
                }
                tess.callVertexOrVertexData(e.Org.data);
                e = e.Lnext;
            } while (e != f.anEdge);
            f = f.trail;
        }
        tess.callEndOrEndData();
    }
    
    public static void __gl_renderBoundary(final GLUtessellatorImpl tess, final GLUmesh mesh) {
        for (GLUface f = mesh.fHead.next; f != mesh.fHead; f = f.next) {
            if (f.inside) {
                tess.callBeginOrBeginData(2);
                GLUhalfEdge e = f.anEdge;
                do {
                    tess.callVertexOrVertexData(e.Org.data);
                    e = e.Lnext;
                } while (e != f.anEdge);
                tess.callEndOrEndData();
            }
        }
    }
    
    static int ComputeNormal(final GLUtessellatorImpl tess, final double[] norm, final boolean check) {
        final CachedVertex[] v = tess.cache;
        final int vn = tess.cacheCount;
        final double[] n = new double[3];
        int sign = 0;
        if (!check) {
            final int n2 = 0;
            final int n3 = 1;
            final int n4 = 2;
            final double n5 = 0.0;
            norm[n4] = n5;
            norm[n2] = (norm[n3] = n5);
        }
        int vc = 1;
        double xc = v[vc].coords[0] - v[0].coords[0];
        double yc = v[vc].coords[1] - v[0].coords[1];
        double zc = v[vc].coords[2] - v[0].coords[2];
        while (++vc < vn) {
            final double xp = xc;
            final double yp = yc;
            final double zp = zc;
            xc = v[vc].coords[0] - v[0].coords[0];
            yc = v[vc].coords[1] - v[0].coords[1];
            zc = v[vc].coords[2] - v[0].coords[2];
            n[0] = yp * zc - zp * yc;
            n[1] = zp * xc - xp * zc;
            n[2] = xp * yc - yp * xc;
            final double dot = n[0] * norm[0] + n[1] * norm[1] + n[2] * norm[2];
            if (!check) {
                if (dot >= 0.0) {
                    final int n6 = 0;
                    norm[n6] += n[0];
                    final int n7 = 1;
                    norm[n7] += n[1];
                    final int n8 = 2;
                    norm[n8] += n[2];
                }
                else {
                    final int n9 = 0;
                    norm[n9] -= n[0];
                    final int n10 = 1;
                    norm[n10] -= n[1];
                    final int n11 = 2;
                    norm[n11] -= n[2];
                }
            }
            else {
                if (dot == 0.0) {
                    continue;
                }
                if (dot > 0.0) {
                    if (sign < 0) {
                        return 2;
                    }
                    sign = 1;
                }
                else {
                    if (sign > 0) {
                        return 2;
                    }
                    sign = -1;
                }
            }
        }
        return sign;
    }
    
    public static boolean __gl_renderCache(final GLUtessellatorImpl tess) {
        final CachedVertex[] v = tess.cache;
        final int vn = tess.cacheCount;
        final double[] norm = new double[3];
        if (tess.cacheCount < 3) {
            return true;
        }
        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0.0 && norm[1] == 0.0 && norm[2] == 0.0) {
            ComputeNormal(tess, norm, false);
        }
        final int sign = ComputeNormal(tess, norm, true);
        return sign != 2 && sign == 0;
    }
    
    static {
        renderFan = new RenderFan();
        renderStrip = new RenderStrip();
        renderTriangle = new RenderTriangle();
    }
    
    private static class FaceCount
    {
        long size;
        GLUhalfEdge eStart;
        renderCallBack render;
        
        private FaceCount() {
        }
        
        private FaceCount(final long size, final GLUhalfEdge eStart, final renderCallBack render) {
            this.size = size;
            this.eStart = eStart;
            this.render = render;
        }
    }
    
    private static class RenderTriangle implements renderCallBack
    {
        public void render(final GLUtessellatorImpl tess, final GLUhalfEdge e, final long size) {
            assert size == 1L;
            tess.lonelyTriList = AddToTrail(e.Lface, tess.lonelyTriList);
        }
    }
    
    private static class RenderFan implements renderCallBack
    {
        public void render(final GLUtessellatorImpl tess, GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(6);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert size == 0L;
            tess.callEndOrEndData();
        }
    }
    
    private static class RenderStrip implements renderCallBack
    {
        public void render(final GLUtessellatorImpl tess, GLUhalfEdge e, long size) {
            tess.callBeginOrBeginData(5);
            tess.callVertexOrVertexData(e.Org.data);
            tess.callVertexOrVertexData(e.Sym.Org.data);
            while (!Marked(e.Lface)) {
                e.Lface.marked = true;
                --size;
                e = e.Lnext.Sym;
                tess.callVertexOrVertexData(e.Org.data);
                if (Marked(e.Lface)) {
                    break;
                }
                e.Lface.marked = true;
                --size;
                e = e.Onext;
                tess.callVertexOrVertexData(e.Sym.Org.data);
            }
            assert size == 0L;
            tess.callEndOrEndData();
        }
    }
    
    private interface renderCallBack
    {
        void render(final GLUtessellatorImpl p0, final GLUhalfEdge p1, final long p2);
    }
}
