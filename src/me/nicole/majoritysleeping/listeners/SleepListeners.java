/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping.listeners;

import me.nicole.majoritysleeping.MajoritySleeping;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Nicole
 */
public class SleepListeners implements Listener {
    MajoritySleeping instance;
    
    public SleepListeners(final MajoritySleeping instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
        this.instance = instance;
    }
    
    @EventHandler
    public void onPlayerSleepEvent(PlayerBedEnterEvent e) {
        //Ensure the sleep is successful
        if (e.getBedEnterResult() != BedEnterResult.OK) return;
        
        //Add the player to the sleeping list
        instance.addSleepingPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerLeaveBedEvent(PlayerBedLeaveEvent e) {
        instance.removeSleepingPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {       
        //Check if they're sleeping
        if (e.getPlayer().isSleeping()) {
            instance.addSleepingPlayer(e.getPlayer());
        }
        
        //Add player
        instance.getAFK().addPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        //Delay the task momentarily 
        Bukkit.getServer().getScheduler().runTaskLater(instance, new Runnable() {
            @Override
            public void run() {
                //Check if they're asleep
                if (e.getPlayer().isSleeping()) {
                    //Remove them from the sleep list if they are
                    instance.removeSleepingPlayer(e.getPlayer());
                } else {
                    //Just force a world check otherwise
                    instance.forceWorldCheck(e.getPlayer().getWorld());
                }
                
                //Remove them from AFK 
                instance.getAFK().removePlayer(e.getPlayer());
            }
        }, 5L);
    }
    
    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        //Delay the task momentarily 
        Bukkit.getServer().getScheduler().runTaskLater(instance, new Runnable() {
            @Override
            public void run() {
                //Force re-check in both worlds
                instance.forceWorldCheck(e.getFrom());
                instance.forceWorldCheck(e.getPlayer().getWorld());
            }
        }, 5L);
    }
}
