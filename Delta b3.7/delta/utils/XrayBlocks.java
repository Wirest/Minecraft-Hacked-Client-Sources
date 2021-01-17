/*
 * Decompiled with CFR 0.150.
 */
package delta.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XrayBlocks {
    public int rapidly$ = -2;
    public List<XrayBlocks> grave$ = new ArrayList<XrayBlocks>();
    public int gentle$ = 0;

    public XrayBlocks() {
    }

    public boolean _levitra(int n, int n2) {
        for (XrayBlocks xrayBlocks : this.grave$) {
            if (xrayBlocks.gentle$ != n || xrayBlocks.rapidly$ != n2 && xrayBlocks.rapidly$ != -1 && n2 != -1) continue;
            return true;
        }
        return false;
    }

    public XrayBlocks(int n, int n2) {
        this.gentle$ = n;
        this.rapidly$ = n2;
    }

    public void _lighter(int n) {
        Iterator<XrayBlocks> iterator = this.grave$.iterator();
        while (iterator.hasNext()) {
            XrayBlocks xrayBlocks = iterator.next();
            if (xrayBlocks.gentle$ != n) continue;
            iterator.remove();
        }
    }

    public void _jeans(int n, int n2) {
        XrayBlocks xrayBlocks = null;
        for (XrayBlocks xrayBlocks2 : this.grave$) {
            if (xrayBlocks2.gentle$ != n || xrayBlocks2.rapidly$ != n2) continue;
            xrayBlocks = xrayBlocks2;
        }
        if (xrayBlocks != null) {
            this.grave$.remove(xrayBlocks);
        }
    }

    public void addBlock(int n, int n2) {
        if (!this._levitra(n, n2)) {
            this.grave$.add(new XrayBlocks(n, n2));
        }
    }
}

