package optifine.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class JSONWriter
{
    private Writer writer = null;
    private int indentStep = 2;
    private int indent = 0;

    public JSONWriter(Writer writer)
    {
        this.writer = writer;
    }

    public JSONWriter(Writer writer, int indentStep)
    {
        this.writer = writer;
        this.indentStep = indentStep;
    }

    public JSONWriter(Writer writer, int indentStep, int indent)
    {
        this.writer = writer;
        this.indentStep = indentStep;
        this.indent = indent;
    }

    public void writeObject(Object obj) throws IOException
    {
        if (obj instanceof JSONObject)
        {
            JSONObject jsonobject = (JSONObject)obj;
            this.writeJsonObject(jsonobject);
        }
        else if (obj instanceof JSONArray)
        {
            JSONArray jsonarray = (JSONArray)obj;
            this.writeJsonArray(jsonarray);
        }
        else
        {
            this.writer.write(JSONValue.toJSONString(obj));
        }
    }

    private void writeJsonArray(JSONArray jArr) throws IOException
    {
        this.writeLine("[");
        this.indentAdd();
        int i = jArr.size();

        for (int j = 0; j < i; ++j)
        {
            Object object = jArr.get(j);
            this.writeIndent();
            this.writeObject(object);

            if (j < jArr.size() - 1)
            {
                this.write(",");
            }

            this.writeLine("");
        }

        this.indentRemove();
        this.writeIndent();
        this.writer.write("]");
    }

    private void writeJsonObject(JSONObject jObj) throws IOException
    {
        this.writeLine("{");
        this.indentAdd();
        Set set = jObj.keySet();
        int i = set.size();
        int j = 0;

        for (String s : (Set<String>)set)
        {
            Object object = jObj.get(s);
            this.writeIndent();
            this.writer.write(JSONValue.toJSONString(s));
            this.writer.write(": ");
            this.writeObject(object);
            ++j;

            if (j < i)
            {
                this.writeLine(",");
            }
            else
            {
                this.writeLine("");
            }
        }

        this.indentRemove();
        this.writeIndent();
        this.writer.write("}");
    }

    private void writeLine(String str) throws IOException
    {
        this.writer.write(str);
        this.writer.write("\n");
    }

    private void write(String str) throws IOException
    {
        this.writer.write(str);
    }

    private void writeIndent() throws IOException
    {
        for (int i = 0; i < this.indent; ++i)
        {
            this.writer.write(32);
        }
    }

    private void indentAdd()
    {
        this.indent += this.indentStep;
    }

    private void indentRemove()
    {
        this.indent -= this.indentStep;
    }
}
