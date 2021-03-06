package com.ne0nx3r0.rareitemhunter.property.skill;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CallLightning extends ItemProperty
{
    public CallLightning()
    {
        super(ItemPropertyTypes.SKILL,"Call Lightning","10% chance to strike an opponent with lightning per level",5,4);
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(100) > level * 10
        && e.getEntity() instanceof LivingEntity)
        {
            Location l = e.getEntity().getLocation();

            l.getWorld().strikeLightningEffect(l);
            
            for(Entity ent : e.getEntity().getNearbyEntities(5, 5, 5))
            {                
                if(ent == p)
                {
                    continue;
                }
                
                if(ent instanceof LivingEntity)
                {
                    LivingEntity lent = (LivingEntity) ent;
                    
                    //Emulate damaged by lightning
                    lent.damage(level*2, p);
                }
            }
                    
            return true;
        }
        return false;
    }
}