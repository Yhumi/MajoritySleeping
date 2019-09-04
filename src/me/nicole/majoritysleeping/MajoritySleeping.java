/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping;

import java.util.HashMap;
import java.util.Map;
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
    
    public void onEnable() {
        //Class variables
        instance = this;
        worldDict = new HashMap<World, WorldSleepRunnable>();
        
        //Add event listeners
        new SleepListeners(instance);
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
}
