package com.ne0nx3r0.rareitemhunter.listeners;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import com.ne0nx3r0.utils.FireworkVisualEffect;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.util.Vector;

public class RareItemHunterEntityListener implements Listener
{
    private final RareItemHunter plugin;

    public RareItemHunterEntityListener(RareItemHunter plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void Damage(EntityDamageByEntityEvent e)
    {
        //TODO: Snowballs or eggs do extra damage to certain vulnerable bosses. each boss has a weakness
        Entity eAttacker = e.getDamager();

        if((eAttacker instanceof Arrow))
        {
            eAttacker = ((Arrow) eAttacker).getShooter();
        }       
        if((eAttacker instanceof Fireball))
        {
            eAttacker = ((Fireball) eAttacker).getShooter();
        }      

        Boss bossAttacker = plugin.bossManager.getBoss(eAttacker);
        Boss bossAttacked = plugin.bossManager.getBoss(e.getEntity());

        //boss on boss violence... A sad social problem.
        if(bossAttacker != null && bossAttacked != null)
        {
            e.setCancelled(true);
            
            return;
        }
        
        if(bossAttacker != null)
        {
            e.setDamage(bossAttacker.getAttackPower());
            
            return;
        }

        if(bossAttacked != null)
        {     
            LivingEntity leBossAttacked = (LivingEntity) e.getEntity();

            int iRemainingHP = bossAttacked.takeDamage(e.getDamage());
            
            if(iRemainingHP > 0)
            {
                leBossAttacked.setHealth(leBossAttacked.getMaxHealth());

                if(!bossAttacked.activateEvent(e,eAttacker))
                {
                    bossAttacked.activateRandomSkill(e,eAttacker);
                }

                if(eAttacker instanceof Player)
                {
                    Player pAttacker = (Player) eAttacker;

                    pAttacker.sendMessage(bossAttacked.getName()+" HP: "+iRemainingHP+"/"+bossAttacked.getMaxHP());
                }
                
                e.setDamage(1);
            }
            else //Dead
            {
                leBossAttacked.setHealth(1);

                try
                {
                    new FireworkVisualEffect().playFirework(
                        leBossAttacked.getWorld(), leBossAttacked.getLocation(),
                        FireworkEffect
                            .builder()
                            .with(FireworkEffect.Type.CREEPER)
                            .withColor(Color.RED)
                            .build()
                    );
                }
                catch (Exception ex)
                {
                    plugin.getLogger().log(Level.SEVERE, null, ex);
                }

                if(eAttacker instanceof Player)
                {
                    Player pAttacker = (Player) eAttacker;
                    
                    plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN+pAttacker.getName()+" has defeated a legendary boss!");
                }
                else
                {
                    plugin.getServer().broadcastMessage("A legendary boss has been defeated!");
                }
                
                Random random = new Random();
                
                for(int i=0;i<bossAttacked.getEssenceDropCount();i++)
                {
                    //TODO: randomize drop spots somewhat
                    
                    Item droppedItem = leBossAttacked.getWorld().dropItemNaturally(leBossAttacked.getLocation(), plugin.recipeManager.getEssenceItem());
                    
                    droppedItem.setVelocity(new Vector(random.nextInt(2),random.nextInt(2),random.nextInt(2)));
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e)
    {
        //TODO: verify events to cancel, snow men melting, being in lava, etc.
        //http://jd.bukkit.org/apidocs/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent e)
    {
        //TODO: fire immunity, rather than every boss being immune
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent e)
    {
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }
    
    
}
