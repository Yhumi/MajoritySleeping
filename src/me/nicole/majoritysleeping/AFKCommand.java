/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.nicole.majoritysleeping;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nicole
 */
public class AFKCommand implements CommandExecutor {
    MajoritySleeping instance;
    
    public AFKCommand(MajoritySleeping instance) {
        instance.getCommand("afk").setExecutor(this);
        this.instance = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cs instanceof Player) {
            Player player = (Player) cs;           
            instance.getAFK().setAFK(player);
        }       
        return true;
    }
}
