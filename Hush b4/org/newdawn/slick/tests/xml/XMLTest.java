// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.tests.xml;

import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.xml.XMLParser;

public class XMLTest
{
    private static void fail(final String message) {
        throw new RuntimeException(message);
    }
    
    private static void assertNotNull(final Object object1) {
        if (object1 == null) {
            throw new RuntimeException("TEST FAILS: " + object1 + " must not be null");
        }
    }
    
    private static void assertEquals(final float a1, final float a2) {
        if (a1 != a2) {
            throw new RuntimeException("TEST FAILS: " + a1 + " should be " + a2);
        }
    }
    
    private static void assertEquals(final int a1, final int a2) {
        if (a1 != a2) {
            throw new RuntimeException("TEST FAILS: " + a1 + " should be " + a2);
        }
    }
    
    private static void assertEquals(final Object a1, final Object a2) {
        if (!a1.equals(a2)) {
            throw new RuntimeException("TEST FAILS: " + a1 + " should be " + a2);
        }
    }
    
    public static void main(final String[] argv) throws SlickException {
        final XMLParser parser = new XMLParser();
        final XMLElement root = parser.parse("testdata/test.xml");
        assertEquals(root.getName(), "testRoot");
        System.out.println(root);
        assertNotNull(root.getChildrenByName("simple").get(0).getContent());
        System.out.println(root.getChildrenByName("simple").get(0).getContent());
        final XMLElement parent = root.getChildrenByName("parent").get(0);
        assertEquals(parent.getChildrenByName("grandchild").size(), 0);
        assertEquals(parent.getChildrenByName("child").size(), 2);
        assertEquals(parent.getChildrenByName("child").get(0).getChildren().size(), 2);
        final XMLElement child = parent.getChildrenByName("child").get(0).getChildren().get(0);
        final String name = child.getAttribute("name");
        final String test = child.getAttribute("nothere", "defaultValue");
        final int age = child.getIntAttribute("age");
        assertEquals(name, "bob");
        assertEquals(test, "defaultValue");
        assertEquals(age, 1);
        final XMLElement other = root.getChildrenByName("other").get(0);
        final float x = (float)other.getDoubleAttribute("x");
        final float y = (float)other.getDoubleAttribute("y", 1.0);
        float z = (float)other.getDoubleAttribute("z", 83.0);
        assertEquals(x, 5.3f);
        assertEquals(y, 5.4f);
        assertEquals(z, 83.0f);
        try {
            z = (float)other.getDoubleAttribute("z");
            fail("Attribute z as a double should fail");
        }
        catch (SlickException ex) {}
    }
}
