package me.pogodaanton.corbetplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdCmd implements CommandExecutor{
	
	//TODO: 10s Cooldown
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player source = (Player) sender;
		if (cmdLabel.equalsIgnoreCase("ad")) {
			if (args.length >= 1) {
				if (source.getWorld().getName().equals("ASkyBlock") || source.getWorld().getName().equals("skyblock")) {
					List<Player> names = Bukkit.getWorld("ASkyBlock").getPlayers();
					List<Player> names2 = Bukkit.getWorld("skyblock").getPlayers();
	
					String fulltext = "";
					
					for (int i = 0; i < args.length; i++) {
						fulltext = fulltext + " " + args[i];
					}
					
					for(Player p : names) {
					    if(p == null) {
					        continue;
					    }
					    p.sendMessage("["+ ChatColor.RED +"Werbung"+ ChatColor.WHITE +"]"+ ChatColor.GOLD +""+ fulltext + " "+ ChatColor.WHITE +"("+ ChatColor.RED +""+ source.getDisplayName() +""+ ChatColor.WHITE +")");
					}
					
					for(Player p : names2) {
					    if(p == null) {
					        continue;
					    }
					    p.sendMessage("["+ ChatColor.RED +"Werbung"+ ChatColor.WHITE +"]"+ ChatColor.GOLD +""+ fulltext + " "+ ChatColor.WHITE +"("+ ChatColor.RED +""+ source.getDisplayName() +""+ ChatColor.WHITE +")");
					}
				} else
					source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du musst dafür in der Skyblock Welt sein!");
			} else {
				source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Werbungstext fehlt!");
			}
		}
		return false;
	}

}
