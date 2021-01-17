// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.ObjectTreeParser;

public class ObjectParserTest
{
    public static void main(final String[] argv) throws SlickXMLException {
        final ObjectTreeParser parser = new ObjectTreeParser("org.newdawn.slick.tests.xml");
        parser.addElementMapping("Bag", ItemContainer.class);
        final GameData parsedData = (GameData)parser.parse("testdata/objxmltest.xml");
        parsedData.dump("");
    }
}
