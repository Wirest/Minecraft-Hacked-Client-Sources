package optifine.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONParser
{
    public static final int S_INIT = 0;
    public static final int S_IN_FINISHED_VALUE = 1;
    public static final int S_IN_OBJECT = 2;
    public static final int S_IN_ARRAY = 3;
    public static final int S_PASSED_PAIR_KEY = 4;
    public static final int S_IN_PAIR_VALUE = 5;
    public static final int S_END = 6;
    public static final int S_IN_ERROR = -1;
    private LinkedList handlerStatusStack;
    private Yylex lexer = new Yylex((Reader)null);
    private Yytoken token = null;
    private int status = 0;

    private int peekStatus(LinkedList statusStack)
    {
        if (statusStack.size() == 0)
        {
            return -1;
        }
        else
        {
            Integer integer = (Integer)statusStack.getFirst();
            return integer.intValue();
        }
    }

    public void reset()
    {
        this.token = null;
        this.status = 0;
        this.handlerStatusStack = null;
    }

    public void reset(Reader in)
    {
        this.lexer.yyreset(in);
        this.reset();
    }

    public int getPosition()
    {
        return this.lexer.getPosition();
    }

    public Object parse(String s) throws ParseException
    {
        return this.parse((String)s, (ContainerFactory)null);
    }

    public Object parse(String s, ContainerFactory containerFactory) throws ParseException
    {
        StringReader stringreader = new StringReader(s);

        try
        {
            return this.parse((Reader)stringreader, (ContainerFactory)containerFactory);
        }
        catch (IOException ioexception)
        {
            throw new ParseException(-1, 2, ioexception);
        }
    }

    public Object parse(Reader in) throws IOException, ParseException
    {
        return this.parse((Reader)in, (ContainerFactory)null);
    }

    public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseException
    {
        this.reset(in);
        LinkedList linkedlist = new LinkedList();
        LinkedList linkedlist1 = new LinkedList();

        try
        {
            while (true)
            {
                this.nextToken();
                label0:

                switch (this.status)
                {
                    case -1:
                        throw new ParseException(this.getPosition(), 1, this.token);

                    case 0:
                        switch (this.token.type)
                        {
                            case 0:
                                this.status = 1;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(this.token.value);
                                break label0;

                            case 1:
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(this.createObjectContainer(containerFactory));
                                break label0;

                            case 2:
                            default:
                                this.status = -1;
                                break label0;

                            case 3:
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(this.createArrayContainer(containerFactory));
                                break label0;
                        }

                    case 1:
                        if (this.token.type == -1)
                        {
                            return linkedlist1.removeFirst();
                        }

                        throw new ParseException(this.getPosition(), 1, this.token);

                    case 2:
                        switch (this.token.type)
                        {
                            case 0:
                                if (this.token.value instanceof String)
                                {
                                    String s3 = (String)this.token.value;
                                    linkedlist1.addFirst(s3);
                                    this.status = 4;
                                    linkedlist.addFirst(new Integer(this.status));
                                }
                                else
                                {
                                    this.status = -1;
                                }

                                break label0;

                            case 1:
                            case 3:
                            case 4:
                            default:
                                this.status = -1;
                                break label0;

                            case 2:
                                if (linkedlist1.size() > 1)
                                {
                                    linkedlist.removeFirst();
                                    linkedlist1.removeFirst();
                                    this.status = this.peekStatus(linkedlist);
                                }
                                else
                                {
                                    this.status = 1;
                                }

                            case 5:
                                break label0;
                        }

                    case 3:
                        switch (this.token.type)
                        {
                            case 0:
                                List list3 = (List)linkedlist1.getFirst();
                                list3.add(this.token.value);
                                break label0;

                            case 1:
                                List list2 = (List)linkedlist1.getFirst();
                                Map map4 = this.createObjectContainer(containerFactory);
                                list2.add(map4);
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(map4);
                                break label0;

                            case 2:
                            default:
                                this.status = -1;
                                break label0;

                            case 3:
                                List list1 = (List)linkedlist1.getFirst();
                                List list4 = this.createArrayContainer(containerFactory);
                                list1.add(list4);
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(list4);
                                break label0;

                            case 4:
                                if (linkedlist1.size() > 1)
                                {
                                    linkedlist.removeFirst();
                                    linkedlist1.removeFirst();
                                    this.status = this.peekStatus(linkedlist);
                                }
                                else
                                {
                                    this.status = 1;
                                }

                            case 5:
                                break label0;
                        }

                    case 4:
                        switch (this.token.type)
                        {
                            case 0:
                                linkedlist.removeFirst();
                                String s2 = (String)linkedlist1.removeFirst();
                                Map map3 = (Map)linkedlist1.getFirst();
                                map3.put(s2, this.token.value);
                                this.status = this.peekStatus(linkedlist);
                                break;

                            case 1:
                                linkedlist.removeFirst();
                                String s1 = (String)linkedlist1.removeFirst();
                                Map map2 = (Map)linkedlist1.getFirst();
                                Map map1 = this.createObjectContainer(containerFactory);
                                map2.put(s1, map1);
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(map1);
                                break;

                            case 2:
                            case 4:
                            case 5:
                            default:
                                this.status = -1;
                                break;

                            case 3:
                                linkedlist.removeFirst();
                                String s = (String)linkedlist1.removeFirst();
                                Map map = (Map)linkedlist1.getFirst();
                                List list = this.createArrayContainer(containerFactory);
                                map.put(s, list);
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));
                                linkedlist1.addFirst(list);

                            case 6:
                        }
                }

                if (this.status == -1)
                {
                    throw new ParseException(this.getPosition(), 1, this.token);
                }

                if (this.token.type == -1)
                {
                    break;
                }
            }

            throw new ParseException(this.getPosition(), 1, this.token);
        }
        catch (IOException ioexception)
        {
            throw ioexception;
        }
    }

    private void nextToken() throws ParseException, IOException
    {
        this.token = this.lexer.yylex();

        if (this.token == null)
        {
            this.token = new Yytoken(-1, (Object)null);
        }
    }

    private Map createObjectContainer(ContainerFactory containerFactory)
    {
        if (containerFactory == null)
        {
            return new JSONObject();
        }
        else
        {
            Map map = containerFactory.createObjectContainer();
            return (Map)(map == null ? new JSONObject() : map);
        }
    }

    private List createArrayContainer(ContainerFactory containerFactory)
    {
        if (containerFactory == null)
        {
            return new JSONArray();
        }
        else
        {
            List list = containerFactory.creatArrayContainer();
            return (List)(list == null ? new JSONArray() : list);
        }
    }

    public void parse(String s, ContentHandler contentHandler) throws ParseException
    {
        this.parse(s, contentHandler, false);
    }

    public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseException
    {
        StringReader stringreader = new StringReader(s);

        try
        {
            this.parse((Reader)stringreader, contentHandler, isResume);
        }
        catch (IOException ioexception)
        {
            throw new ParseException(-1, 2, ioexception);
        }
    }

    public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseException
    {
        this.parse(in, contentHandler, false);
    }

    public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseException
    {
        if (!isResume)
        {
            this.reset(in);
            this.handlerStatusStack = new LinkedList();
        }
        else if (this.handlerStatusStack == null)
        {
            isResume = false;
            this.reset(in);
            this.handlerStatusStack = new LinkedList();
        }

        LinkedList linkedlist = this.handlerStatusStack;

        try
        {
            while (true)
            {
                label0:

                switch (this.status)
                {
                    case -1:
                        throw new ParseException(this.getPosition(), 1, this.token);

                    case 0:
                        contentHandler.startJSON();
                        this.nextToken();

                        switch (this.token.type)
                        {
                            case 0:
                                this.status = 1;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.primitive(this.token.value))
                                {
                                    return;
                                }

                                break label0;

                            case 1:
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startObject())
                                {
                                    return;
                                }

                                break label0;

                            case 2:
                            default:
                                this.status = -1;
                                break label0;

                            case 3:
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startArray())
                                {
                                    return;
                                }

                                break label0;
                        }

                    case 1:
                        this.nextToken();

                        if (this.token.type == -1)
                        {
                            contentHandler.endJSON();
                            this.status = 6;
                            return;
                        }

                        this.status = -1;
                        throw new ParseException(this.getPosition(), 1, this.token);

                    case 2:
                        this.nextToken();

                        switch (this.token.type)
                        {
                            case 0:
                                if (this.token.value instanceof String)
                                {
                                    String s = (String)this.token.value;
                                    this.status = 4;
                                    linkedlist.addFirst(new Integer(this.status));

                                    if (!contentHandler.startObjectEntry(s))
                                    {
                                        return;
                                    }
                                }
                                else
                                {
                                    this.status = -1;
                                }

                                break label0;

                            case 1:
                            case 3:
                            case 4:
                            default:
                                this.status = -1;
                                break label0;

                            case 2:
                                if (linkedlist.size() > 1)
                                {
                                    linkedlist.removeFirst();
                                    this.status = this.peekStatus(linkedlist);
                                }
                                else
                                {
                                    this.status = 1;
                                }

                                if (!contentHandler.endObject())
                                {
                                    return;
                                }

                            case 5:
                                break label0;
                        }

                    case 3:
                        this.nextToken();

                        switch (this.token.type)
                        {
                            case 0:
                                if (!contentHandler.primitive(this.token.value))
                                {
                                    return;
                                }

                                break label0;

                            case 1:
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startObject())
                                {
                                    return;
                                }

                                break label0;

                            case 2:
                            default:
                                this.status = -1;
                                break label0;

                            case 3:
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startArray())
                                {
                                    return;
                                }

                                break label0;

                            case 4:
                                if (linkedlist.size() > 1)
                                {
                                    linkedlist.removeFirst();
                                    this.status = this.peekStatus(linkedlist);
                                }
                                else
                                {
                                    this.status = 1;
                                }

                                if (!contentHandler.endArray())
                                {
                                    return;
                                }

                            case 5:
                                break label0;
                        }

                    case 4:
                        this.nextToken();

                        switch (this.token.type)
                        {
                            case 0:
                                linkedlist.removeFirst();
                                this.status = this.peekStatus(linkedlist);

                                if (!contentHandler.primitive(this.token.value))
                                {
                                    return;
                                }

                                if (!contentHandler.endObjectEntry())
                                {
                                    return;
                                }

                                break label0;

                            case 1:
                                linkedlist.removeFirst();
                                linkedlist.addFirst(new Integer(5));
                                this.status = 2;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startObject())
                                {
                                    return;
                                }

                                break label0;

                            case 2:
                            case 4:
                            case 5:
                            default:
                                this.status = -1;
                                break label0;

                            case 3:
                                linkedlist.removeFirst();
                                linkedlist.addFirst(new Integer(5));
                                this.status = 3;
                                linkedlist.addFirst(new Integer(this.status));

                                if (!contentHandler.startArray())
                                {
                                    return;
                                }

                            case 6:
                                break label0;
                        }

                    case 5:
                        linkedlist.removeFirst();
                        this.status = this.peekStatus(linkedlist);

                        if (!contentHandler.endObjectEntry())
                        {
                            return;
                        }

                        break;

                    case 6:
                        return;
                }

                if (this.status == -1)
                {
                    throw new ParseException(this.getPosition(), 1, this.token);
                }

                if (this.token.type == -1)
                {
                    break;
                }
            }
        }
        catch (IOException ioexception)
        {
            this.status = -1;
            throw ioexception;
        }
        catch (ParseException parseexception)
        {
            this.status = -1;
            throw parseexception;
        }
        catch (RuntimeException runtimeexception)
        {
            this.status = -1;
            throw runtimeexception;
        }
        catch (Error error)
        {
            this.status = -1;
            throw error;
        }

        this.status = -1;
        throw new ParseException(this.getPosition(), 1, this.token);
    }

    public static Date parseDate(String input)
    {
        if (input == null)
        {
            return null;
        }
        else
        {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ssz");

            if (input.endsWith("Z"))
            {
                input = input.substring(0, input.length() - 1) + "GMT-00:00";
            }
            else
            {
                int i = 6;
                String s = input.substring(0, input.length() - i);
                String s1 = input.substring(input.length() - i, input.length());
                input = s + "GMT" + s1;
            }

            try
            {
                return simpledateformat.parse(input);
            }
            catch (java.text.ParseException parseexception)
            {
                System.out.println("Error parsing date: " + input);
                System.out.println(parseexception.getClass().getName() + ": " + parseexception.getMessage());
                return null;
            }
        }
    }
}
