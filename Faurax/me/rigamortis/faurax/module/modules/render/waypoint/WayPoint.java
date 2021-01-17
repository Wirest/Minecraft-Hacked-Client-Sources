package me.rigamortis.faurax.module.modules.render.waypoint;

import javax.vecmath.*;

public class WayPoint
{
    public Vector3f pos;
    public String name;
    
    public WayPoint(final Vector3f vec3, final String name) {
        this.pos = vec3;
        this.name = name;
    }
}
