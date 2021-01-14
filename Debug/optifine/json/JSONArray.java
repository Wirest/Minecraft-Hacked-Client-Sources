package optifine.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArray extends ArrayList implements List, JSONAware, JSONStreamAware
{
    private static final long serialVersionUID = 3957988303675231981L;

    public static void writeJSONString(List list, Writer out) throws IOException
    {
        if (list == null)
        {
            out.write("null");
        }
        else
        {
            boolean flag = true;
            Iterator iterator = list.iterator();
            out.write(91);

            while (iterator.hasNext())
            {
                if (flag)
                {
                    flag = false;
                }
                else
                {
                    out.write(44);
                }

                Object object = iterator.next();

                if (object == null)
                {
                    out.write("null");
                }
                else
                {
                    JSONValue.writeJSONString(object, out);
                }
            }

            out.write(93);
        }
    }

    public void writeJSONString(Writer out) throws IOException
    {
        writeJSONString(this, out);
    }

    public static String toJSONString(List list)
    {
        if (list == null)
        {
            return "null";
        }
        else
        {
            boolean flag = true;
            StringBuffer stringbuffer = new StringBuffer();
            Iterator iterator = list.iterator();
            stringbuffer.append('[');

            while (iterator.hasNext())
            {
                if (flag)
                {
                    flag = false;
                }
                else
                {
                    stringbuffer.append(',');
                }

                Object object = iterator.next();

                if (object == null)
                {
                    stringbuffer.append("null");
                }
                else
                {
                    stringbuffer.append(JSONValue.toJSONString(object));
                }
            }

            stringbuffer.append(']');
            return stringbuffer.toString();
        }
    }

    public String toJSONString()
    {
        return toJSONString(this);
    }

    public String toString()
    {
        return this.toJSONString();
    }
}
