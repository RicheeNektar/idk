package me.pogodaanton.corbetplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickallCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (sender.hasPermission("cc.kickall")) {
			if (args.length >= 1) {
				List<World> bworlds = Bukkit.getWorlds();

				String fulltext = "";
				
				for (int i = 0; i < args.length; i++) {
					fulltext = fulltext + " " + args[i];
				}
				
				for(World w : bworlds) {
					for (Player p : w.getPlayers()) {
						if(p == null) continue;
						p.kickPlayer(ChatColor.translateAlternateColorCodes('&', fulltext));
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Kick-Grund fehlt!");
			}
		}
		return false;
	}

}
