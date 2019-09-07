/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping;

import me.nicole.majoritysleeping.listeners.SleepListeners;
import me.nicole.majoritysleeping.runnable.WorldSleepRunnable;
import java.util.HashMap;
import java.util.Map;
import me.nicole.majoritysleeping.listeners.AFKListeners;
import me.nicole.majoritysleeping.runnable.AFKRunnable;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nicole
 */
public class MajoritySleeping extends JavaPlugin {
    MajoritySleeping instance;
    Map<World, WorldSleepRunnable> worldDict;
    AFKRunnable afk;
    int afktime;
    
    public void onEnable() {
        //Class variables
        instance = this;
        worldDict = new HashMap<World, WorldSleepRunnable>();
        afktime = 300;
        afk = new AFKRunnable(instance);    
        
        //Add event listeners
        new SleepListeners(instance);        
        new AFKListeners(instance);
        
        //Commands
        new AFKCommand(instance);
    }
    
    public void addSleepingPlayer(Player p) {
        //Check the world the player is in
        World playerWorld = p.getWorld();
        
        //If the world does not have an existing WorldSleepRunnable, create it
        if (!worldDict.containsKey(playerWorld)) {
            worldDict.put(playerWorld, new WorldSleepRunnable(instance, playerWorld));
        }
        
        //Add the player to the world
        worldDict.get(playerWorld).addSleepingPlayer(p);
    }
    
    public void removeSleepingPlayer(Player p) {
        //Check the world the player is in
        World playerWorld = p.getWorld();
        
        //If the world does not have an existing WorldSleepRunnable, create it (this should never run in theory, but it's a good fallback)
        if (!worldDict.containsKey(playerWorld)) {
            worldDict.put(playerWorld, new WorldSleepRunnable(instance, playerWorld));
        }
        
        //Add the player to the world
        worldDict.get(playerWorld).removeSleepingPlayer(p);
    }
    
    public void forceWorldCheck(World w) {
        //If the world does not have an existing WorldSleepRunnable, create it (this should never run in theory, but it's a good fallback)
        if (!worldDict.containsKey(w)) {
            worldDict.put(w, new WorldSleepRunnable(instance, w));
        }
        
        //Force the world check
        worldDict.get(w).performSleepCheck();
    }
    
    public AFKRunnable getAFK() {
        return afk;
    }

    public int getTime() {
        return afktime;
    }
}
