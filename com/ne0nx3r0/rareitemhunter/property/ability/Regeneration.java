package com.ne0nx3r0.rareitemhunter.property.ability;

import com.ne0nx3r0.rareitemhunter.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.entity.Player;

public class Regeneration extends ItemPropertyRepeatingEffect
{    
    public Regeneration()
    {
        super(ItemPropertyTypes.ABILITY,"Regeneration","Regenerate 1 HP / lvl / 10 seconds",3,0);
        
        this.createRepeatingAppliedEffect(this,20*10);
    }

    @Override
    public void applyEffectToPlayer(Player p,int level)
    {
        if(p.getHealth() < 20)
        {
            int iNewHP = p.getHealth() + level * 1;
            
            if(iNewHP > 20)
            {
                iNewHP = 20;
            }
            
            p.setHealth(iNewHP);
        }
    }
}
