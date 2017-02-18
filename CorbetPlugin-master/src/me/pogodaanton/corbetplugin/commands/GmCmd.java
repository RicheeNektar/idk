package me.pogodaanton.corbetplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmCmd implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player source = (Player) sender;
		if (cmdLabel.equalsIgnoreCase("gm")) {
			if (source.hasPermission("cc.gm")) {
				if (args.length == 1) {
					switch(args[0]) {
						case "0":
							source.setGameMode(GameMode.SURVIVAL);
							return true;
						case "1":
							source.setGameMode(GameMode.CREATIVE);
							return true;
						case "2":
							source.setGameMode(GameMode.ADVENTURE);
							return true;
						case "3":
							source.setGameMode(GameMode.SPECTATOR);
							return true;
					}
					source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Keine richtige Gamemode!");
					source.sendMessage(ChatColor.DARK_RED + "Vefügbare Gamemodes:"+ ChatColor.WHITE + " 0 (Survival), 1 (Creative), 2 (Adventure), 3 (Spectator)");
				} else if(args.length == 2) {
					Player target = source.getServer().getPlayer(args[1]);
					
					if (target != null) {
						switch(args[0]) {
							case "0":
								target.setGameMode(GameMode.SURVIVAL);
								source.sendMessage(ChatColor.DARK_GREEN + "Gamemode bei "+ ChatColor.RESET +""+ target.getDisplayName() +""+ ChatColor.DARK_GREEN +" auf Survival geändert!");
								return true;
							case "1":
								target.setGameMode(GameMode.CREATIVE);
								source.sendMessage(ChatColor.DARK_GREEN + "Gamemode bei "+ ChatColor.RESET +""+ target.getDisplayName() +""+ ChatColor.DARK_GREEN +" auf Creative geändert!");
								return true;
							case "2":
								target.setGameMode(GameMode.ADVENTURE);
								source.sendMessage(ChatColor.DARK_GREEN + "Gamemode bei "+ ChatColor.RESET +""+ target.getDisplayName() +""+ ChatColor.DARK_GREEN +" auf Adventure geändert!");
								return true;
							case "3":
								target.setGameMode(GameMode.SPECTATOR);
								source.sendMessage(ChatColor.DARK_GREEN + "Gamemode bei "+ ChatColor.RESET +""+ target.getDisplayName() +""+ ChatColor.DARK_GREEN +" auf Spectator geändert!");
								return true;
						}
						
					source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Keine richtige Gamemode!");
					} else
						source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Kein richtiger Playername!");
				} else {
					switch(source.getGameMode().toString()) {
						case "SURVIVAL":
							source.setGameMode(GameMode.CREATIVE);
							return true;
						case "CREATIVE":
							source.setGameMode(GameMode.SURVIVAL);
							return true;
					}
					source.setGameMode(GameMode.SURVIVAL);
					return true;
				}
			}
		}
		return false;
	}

}
