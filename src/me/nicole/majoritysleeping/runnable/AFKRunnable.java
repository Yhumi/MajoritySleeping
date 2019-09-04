/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping.runnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.nicole.majoritysleeping.MajoritySleeping;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Holds the amount of time, in seconds, since the player last interacted - their AFK timer.
 * @author Nicole
 */
public class AFKRunnable implements Runnable {
    MajoritySleeping instance;
    Map<Player, Integer> afkList;
    List<Player> afkAnnounced;
    
    /**
     * Constructor
     * @param instance 
     */
    public AFKRunnable(MajoritySleeping instance) {
        this.instance = instance;
        this.afkList = new HashMap<Player, Integer>();
        this.afkAnnounced = new ArrayList<Player>();
        
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(instance, this, 0L, 20L);
    }
    
    @Override
    public void run() {
        //Every second
        for (Player p : instance.getServer().getOnlinePlayers()) {
            //Time in seconds
            int afkSeconds = afkList.getOrDefault(p, 0);
            
            //Increment
            afkSeconds++;
            
            //Update them
            afkList.replace(p, afkSeconds);
            
            //Alert the server they are AFK if their time is over 
            if (afkSeconds >= instance.getTime() && !afkAnnounced.contains(p)) {
                instance.getServer().broadcastMessage(String.format("%s%s has gone AFK.", ChatColor.LIGHT_PURPLE, p.getName()));
                afkAnnounced.add(p);
                
                //re-calculate sleep
                instance.forceWorldCheck(p.getWorld());
            }
        }
    }
    
    public void resetPlayer(Player p) {
        //If they were AFK
        boolean checkWorld = false;
        if (afkList.getOrDefault(p, 0) >= instance.getTime()) {
            instance.getServer().broadcastMessage(String.format("%s%s is no longer AFK.", ChatColor.LIGHT_PURPLE, p.getName()));
            afkAnnounced.remove(p);
            checkWorld = true;
        }
        
        //Replace them
        afkList.replace(p, 0);
        
        //re-calculate sleep if needed
        if (checkWorld) instance.forceWorldCheck(p.getWorld());
    }
    
    public boolean isPlayerAFK(Player p, int seconds) {
        return afkList.getOrDefault(p, 0) >= seconds;
    }
    
    public void addPlayer(Player p) {
        afkList.put(p, 0);
    }
    
    public void removePlayer(Player p) {
        afkList.remove(p);
        if (afkAnnounced.contains(p)) afkAnnounced.remove(p);
    }
    
    public void setAFK(Player p) {
        afkList.replace(p, 300);
        instance.getServer().broadcastMessage(String.format("%s%s has gone AFK.", ChatColor.LIGHT_PURPLE, p.getName()));
        afkAnnounced.add(p);
        
        //re-calculate sleep
        instance.forceWorldCheck(p.getWorld());
    }
}
