package dev.astroclient.client.property;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class Property {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDependency() {
        return dependency;
    }

    private String name;
    private boolean dependency;

    public Property(String name, boolean dependency) {
        this.name = name;
        this.dependency = dependency;
    }

    public Property(String name) {
        this.name = name;
        this.dependency = true;
    }
}
