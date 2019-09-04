/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping.listeners;

import me.nicole.majoritysleeping.MajoritySleeping;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author Nicole
 */
public class AFKListeners implements Listener {
    MajoritySleeping instance;
    
    public AFKListeners(final MajoritySleeping instance) {
        instance.getServer().getPluginManager().registerEvents(this, instance);
        this.instance = instance;
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        instance.getAFK().resetPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        instance.getAFK().resetPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        instance.getAFK().resetPlayer(e.getPlayer());
    }
}
