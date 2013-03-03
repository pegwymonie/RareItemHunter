package com.ne0nx3r0.rareitemhunter.property.enchantments;


import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class MeltObsidian extends ItemProperty
{
    public MeltObsidian()
    {
        super(ItemPropertyTypes.ENCHANTMENT,"Melt Obsidian","Turns clicked lava into obsidian",6,1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null)
        {
            if(e.getClickedBlock().getType() == Material.OBSIDIAN)
            {
                e.getClickedBlock().setType(Material.LAVA);
                
                e.setCancelled(true);
                
                return true;
            }
        }
        return false;
    }
}