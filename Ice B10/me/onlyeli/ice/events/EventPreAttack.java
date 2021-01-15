package me.onlyeli.ice.events;

import me.onlyeli.eventapi.events.Event;
import net.minecraft.entity.Entity;

public class EventPreAttack
  implements Event
{
  private Entity attacker;
  private Entity target;
  
  public EventPreAttack(Entity attacker, Entity target)
  {
    this.attacker = attacker;
    this.target = target;
  }
  
  public Entity getAttacker()
  {
    return this.attacker;
  }
  
  public Entity getTarget()
  {
    return this.target;
  }
}