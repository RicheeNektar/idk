package me.pogodaanton.corbetplugin.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.pogodaanton.corbetplugin.corbetplugin;

public class CommandPreprocessListener implements Listener {
	private final corbetplugin plugin;
	public List<String> Disabled;

    public CommandPreprocessListener(corbetplugin instance) {
        plugin = instance;
        loadDisabledCommands();
    }
    
    public void loadDisabledCommands() {
        try {
        	Disabled = new ArrayList<String>();
        	File f = new File(plugin.getDataFolder(), "disabled.txt");
			if (!f.exists()) {
				corbetplugin.log("No commands to disable");
			} else {
				BufferedReader rdr = new BufferedReader(new FileReader(f));
				String line;
				while ((line = rdr.readLine()) != null) {
					line = line.trim();
					if (line.length() < 1) continue;
					Disabled.add(line.trim().toLowerCase());
    			}
    			rdr.close();
    			Collections.sort(Disabled);
    			corbetplugin.log("Disabled " + Disabled.size() + " commands");
			}
        } catch (Exception e) {
        	corbetplugin.error("Unexpected error: " + e.getMessage());
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    	Player p = event.getPlayer();
    	if (p != null && p.hasPermission("cc.maydisabled")) return;
    	
    	String[] split = event.getMessage().split(" ");
    	if (split.length < 1) return;
    	// if (split.length > 1) corbetplugin.log("length:" + split.length  + ", split[0]:" + split[0].trim().toLowerCase() + ", cmd:" + split[0].trim().substring(1).toLowerCase() + ", split[1]:" + split[1].trim().toLowerCase());
    	
    	String cmd = split[0].trim().substring(1).toLowerCase();
    	
    	if (Collections.binarySearch(Disabled, cmd) >= 0) {
    		event.setCancelled(true);
    		if (p != null) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command nicht benutzen!");
    	} else if (split.length >= 2 && cmd.equalsIgnoreCase("kill") && split[1].trim().toLowerCase().indexOf("@e") >= 0) {
    		event.setCancelled(true);
    		if (p != null) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command nicht benutzen!");
    	} else if (split.length >= 2 && cmd.equalsIgnoreCase("tp") && (split[1].trim().toLowerCase().indexOf("pogodaanton") >= 0 || split[1].trim().toLowerCase().indexOf("contercrafter") >= 0)) {
    		event.setCancelled(true);
    		if (p != null && p.hasPermission("cc.test")) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command bei Pogodaanton/Contercrafter nicht benutzen! " + ChatColor.GOLD + "Benutze /tpa stattdessen.");
    	} else if (split.length >= 2 && cmd.equalsIgnoreCase("kill") && (split[1].trim().toLowerCase().indexOf("pogodaanton") >= 0 || split[1].trim().toLowerCase().indexOf("contercrafter") >= 0)) {
    		event.setCancelled(true);
    		if (p != null && p.hasPermission("cc.test")) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command bei Pogodaanton/Contercrafter nicht benutzen!");
    	} else if (split.length >= 2 && cmd.equalsIgnoreCase("kick") && (split[1].trim().toLowerCase().indexOf("pogodaanton") >= 0 || split[1].trim().toLowerCase().indexOf("contercrafter") >= 0)) {
    		event.setCancelled(true);
    		if (p != null && p.hasPermission("cc.test")) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command bei Pogodaanton/Contercrafter nicht benutzen!");
    	} else if (split.length >= 3 && cmd.equalsIgnoreCase("gm") && (split[2].trim().toLowerCase().indexOf("pogodaanton") >= 0 || split[2].trim().toLowerCase().indexOf("contercrafter") >= 0)) {
    		event.setCancelled(true);
    		if (p != null && p.hasPermission("cc.test")) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command bei Pogodaanton/Contercrafter nicht benutzen!");
    	} else if (split.length >= 3 && cmd.equalsIgnoreCase("gamemode") && (split[2].trim().toLowerCase().indexOf("pogodaanton") >= 0 || split[2].trim().toLowerCase().indexOf("contercrafter") >= 0)) {
    		event.setCancelled(true);
    		if (p != null && p.hasPermission("cc.test")) p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du darfst diesen Command bei Pogodaanton/Contercrafter nicht benutzen!");
    	}
    }
}
