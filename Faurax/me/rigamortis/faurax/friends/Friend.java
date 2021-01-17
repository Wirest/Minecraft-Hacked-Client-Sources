package me.rigamortis.faurax.friends;

public class Friend
{
    private String name;
    private String alias;
    
    public Friend(final String name, final String alias) {
        this.setName(name);
        this.setAlias(alias);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
}
