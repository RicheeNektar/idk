package me.pogodaanton.corbetplugin.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.pogodaanton.corbetplugin.corbetplugin;

public class spawn {
	private static Location spawnpoint;
	
	public spawn () {
		getSpawn();
	}
	
	public void getSpawn() {
		File spawnfile = new File(corbetplugin.get().getDataFolder(), "Spawn.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(spawnfile);
		
		if (spawnfile.exists() && cfg.get("spawn") != null) {
			Location spawn = (Location) cfg.get("spawn");
			if (corbetplugin.get().getServer().getWorld(spawn.getWorld().getName()) != null) spawnpoint = spawn;
			else corbetplugin.log("The spawnworld is not loaded, so /spawn won't work for the moment.");
		} else {
			corbetplugin.log("No spawn set at the moment, in order to use /spawn and a custom respawnpoint, please set a spawnpoint with /setspawn.");
		}
	}
	
	public boolean saveSpawn(Location spawn) {
		File spawnfile = new File(corbetplugin.get().getDataFolder(), "Spawn.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(spawnfile);
		
		if (corbetplugin.get().getServer().getWorld(spawn.getWorld().getName()) != null) {
			try {
				cfg.set("spawn", spawn);
				cfg.save(spawnfile);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			corbetplugin.log("The spawnworld is not loaded, so /spawn won't work for the moment.");
			return false;
		}
	}

	public Location getSpawnpoint() {
		return spawnpoint;
	}
	
	public boolean setSpawnpoint(Location spawnpoint) {
		spawn.spawnpoint = spawnpoint;
		return saveSpawn(spawnpoint);
	}
}
