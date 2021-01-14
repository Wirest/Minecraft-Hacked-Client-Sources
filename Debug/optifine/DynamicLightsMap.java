package optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicLightsMap
{
    private Map<Integer, DynamicLight> map = new HashMap();
    private List<DynamicLight> list = new ArrayList();
    private boolean dirty = false;

    public DynamicLight put(int p_put_1_, DynamicLight p_put_2_)
    {
        DynamicLight dynamiclight = (DynamicLight)this.map.put(Integer.valueOf(p_put_1_), p_put_2_);
        this.setDirty();
        return dynamiclight;
    }

    public DynamicLight get(int p_get_1_)
    {
        return (DynamicLight)this.map.get(Integer.valueOf(p_get_1_));
    }

    public int size()
    {
        return this.map.size();
    }

    public DynamicLight remove(int p_remove_1_)
    {
        DynamicLight dynamiclight = (DynamicLight)this.map.remove(Integer.valueOf(p_remove_1_));

        if (dynamiclight != null)
        {
            this.setDirty();
        }

        return dynamiclight;
    }

    public void clear()
    {
        this.map.clear();
        this.setDirty();
    }

    private void setDirty()
    {
        this.dirty = true;
    }

    public List<DynamicLight> valueList()
    {
        if (this.dirty)
        {
            this.list.clear();
            this.list.addAll(this.map.values());
            this.dirty = false;
        }

        return this.list;
    }
}
