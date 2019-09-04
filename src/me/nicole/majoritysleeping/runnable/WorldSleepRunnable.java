/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping.runnable;

import java.util.ArrayList;
import java.util.List;
import me.nicole.majoritysleeping.MajoritySleeping;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Nicole
 */
public class WorldSleepRunnable implements Runnable {
    MajoritySleeping instance;
    List<Player> sleepingPlayersInWorld;
    World world;
    
    public WorldSleepRunnable(final MajoritySleeping instance, final World w) {
        this.instance = instance;
        this.world = w;
        sleepingPlayersInWorld = new ArrayList<Player>();
    }
    
    @Override
    public void run() {
        //Check to ensure that the sleep count matches up still
        if (sleepingPlayersInWorld.size() < ((int) Math.floor(world.getPlayers().size() / 2)) + 1) return;
        
        //Skip time & weather
        world.setTime(0L);
        world.setStorm(false);
        world.setThundering(false);
    }
    
    public void addSleepingPlayer(Player p) {
        //Add the player to the list
        this.sleepingPlayersInWorld.add(p);
        
        //Broadcast to the world
        world.getPlayers().forEach(player -> player.sendMessage(String.format("%s%s has entered a bed (%s/%s)", ChatColor.LIGHT_PURPLE, p.getName(), sleepingPlayersInWorld.size(), activeCount())));
        
        //Perform sleepcheck
        performSleepCheck();
    }
    
    public void removeSleepingPlayer(Player p) {
        this.sleepingPlayersInWorld.remove(p);
    }
    
    public void performSleepCheck() {
        //Check to see if the player count for this world has matched up
        int majorityTarget = ((int) Math.floor(activeCount() / 2)) + 1;
        
        //If there's nobody around, or nobody sleeping, we're good
        if (sleepingPlayersInWorld.size() == 0 || activeCount() == 0 || world.getPlayers().size() == 0) return;
        
        if (sleepingPlayersInWorld.size() >= majorityTarget) {
            //Broadcast to the world
            world.getPlayers().forEach(player -> player.sendMessage(String.format("%sThe majority of active players in the world are in bed, executing a sleep cycle in 5 seconds.", ChatColor.LIGHT_PURPLE)));
            
            //Run this after 100 ticks
            Bukkit.getServer().getScheduler().runTaskLater((Plugin) instance, this, 100L);
        }
    }
    
    private int activeCount() {
        int out = 0;
        for (Player p : world.getPlayers()) {
            if (!instance.getAFK().isPlayerAFK(p, instance.getTime())) {
                out++;
            }
        }
        return out;
    }
}
