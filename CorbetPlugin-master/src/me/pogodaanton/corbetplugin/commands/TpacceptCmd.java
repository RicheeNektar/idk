package me.pogodaanton.corbetplugin.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpacceptCmd implements CommandExecutor{
	
	HashMap<Player, ArrayList<Player>> tpa = TpaCmd.tpa;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		Player source = (Player) sender;
		if (cmdLabel.equalsIgnoreCase("tpaccept")) {
			if (args.length == 1) {
				ArrayList<Player> playerlist = tpa.get(source);
				for (int i = 0; i < playerlist.size(); i++) {
					if(playerlist.get(i).getName().equalsIgnoreCase(args[0])) {
						playerlist.get(i).teleport(source);
						playerlist.get(i).sendMessage(ChatColor.GOLD + "Du wurdest zu "+ ChatColor.RESET +""+ source.getDisplayName() +""+ ChatColor.GOLD +" teleportiert.");
						playerlist.remove(i);
						tpa.put(source, playerlist);
						return true;
					}
				}
				source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Dieser Spieler steht nicht in der Anfrageliste!");
			} else {
				ArrayList<Player> playerlist = tpa.get(source);
				int i = (playerlist.size() - 1);
				if (i != -1) {
					playerlist.get(i).teleport(source);
					playerlist.get(i).sendMessage(ChatColor.GOLD + "Du wurdest zu "+ ChatColor.RESET +""+ source.getDisplayName() +""+ ChatColor.GOLD +" teleportiert.");
					playerlist.remove(i);
					tpa.put(source, playerlist);
				} else {
					source.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Derzeit gibt es keine Anfrage!");
				}
			}
		}
		return false;
	}

}
