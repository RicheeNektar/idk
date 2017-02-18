package me.pogodaanton.corbetplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.redcrew.kzak.skywars.SkyWarsReloaded;
import org.redcrew.kzak.skywars.game.GamePlayer;

import io.github.bedwarsrel.BedwarsRel.Main;
import me.pogodaanton.corbetplugin.commands.AdCmd;
import me.pogodaanton.corbetplugin.commands.BaseCmd;
import me.pogodaanton.corbetplugin.commands.BroadcastCmd;
import me.pogodaanton.corbetplugin.commands.ClearhomesCmd;
import me.pogodaanton.corbetplugin.commands.DelhomeCmd;
import me.pogodaanton.corbetplugin.commands.GmCmd;
import me.pogodaanton.corbetplugin.commands.HilfeCmd;
import me.pogodaanton.corbetplugin.commands.HomeCmd;
import me.pogodaanton.corbetplugin.commands.JumpCmd;
import me.pogodaanton.corbetplugin.commands.KickallCmd;
import me.pogodaanton.corbetplugin.commands.LobbyCmd;
import me.pogodaanton.corbetplugin.commands.MsgCmd;
import me.pogodaanton.corbetplugin.commands.RainCmd;
import me.pogodaanton.corbetplugin.commands.SethomeCmd;
import me.pogodaanton.corbetplugin.commands.SetspawnCmd;
import me.pogodaanton.corbetplugin.commands.SpawnCmd;
import me.pogodaanton.corbetplugin.commands.SunCmd;
import me.pogodaanton.corbetplugin.commands.TpaCmd;
import me.pogodaanton.corbetplugin.commands.TpacceptCmd;
import me.pogodaanton.corbetplugin.commands.TpdenyCmd;
import me.pogodaanton.corbetplugin.commands.dayNite;
import me.pogodaanton.corbetplugin.configs.spawn;
import me.pogodaanton.corbetplugin.listeners.AsyncChatListener;
import me.pogodaanton.corbetplugin.listeners.CommandPreprocessListener;
import me.pogodaanton.corbetplugin.listeners.DungeonChestListener;
import me.pogodaanton.corbetplugin.listeners.JumpListener;
import me.pogodaanton.corbetplugin.listeners.SpawnListeners;
import me.pogodaanton.kickdown.Kickdown;

public class corbetplugin extends JavaPlugin {
	
	private static corbetplugin instance;
	private static spawn sp;
	public PluginDescriptionFile pdfile = this.getDescription();
	public String pluginFolder = getDataFolder().getAbsolutePath();
	static Logger logger = Logger.getLogger("minecraft");
	static String juPrefix = ChatColor.RESET + "[" + ChatColor.AQUA + "Jump" + ChatColor.RESET + "]"+ ChatColor.GOLD +" ";


	@Override
	public void onEnable() {
		logger.info(pdfile.getName() + " v'" + pdfile.getVersion() +" enabled!");
		instance = this;
		sp = new spawn();
		
		if (Bukkit.getWorld("Xandrania") != null) Bukkit.getWorld("Xandrania").getWorldBorder().setWarningDistance(0);
		
		getCommand("tpa").setExecutor(new TpaCmd());
		getCommand("tpaccept").setExecutor(new TpacceptCmd());
		getCommand("tpdeny").setExecutor(new TpdenyCmd());
		getCommand("gm").setExecutor(new GmCmd());
		getCommand("ad").setExecutor(new AdCmd());
		getCommand("hilfe").setExecutor(new HilfeCmd());
		getCommand("sun").setExecutor(new SunCmd());
		getCommand("rain").setExecutor(new RainCmd());
		getCommand("sethome").setExecutor(new SethomeCmd());
		getCommand("delhome").setExecutor(new DelhomeCmd());
		getCommand("clearhomes").setExecutor(new ClearhomesCmd());
		getCommand("home").setExecutor(new HomeCmd());
		getCommand("lobby").setExecutor(new LobbyCmd()) ;
		getCommand("spawn").setExecutor(new SpawnCmd());
		getCommand("setspawn").setExecutor(new SetspawnCmd());
		getCommand("jump").setExecutor(new JumpCmd());
		getCommand("broadcast").setExecutor(new BroadcastCmd());
		getCommand("kickall").setExecutor(new KickallCmd());
		getCommand("msg").setExecutor(new MsgCmd());
		getCommand("cc").setExecutor(new BaseCmd());
		dayNite n = new dayNite();
		getCommand("day").setExecutor(n);
		getCommand("night").setExecutor(n);
		
		Bukkit.getPluginManager().registerEvents(new DungeonChestListener(), this);
		Bukkit.getPluginManager().registerEvents(new JumpListener(), this);
		Bukkit.getPluginManager().registerEvents(new SpawnListeners(), this);
		Bukkit.getPluginManager().registerEvents(new AsyncChatListener(), this);
		
		// Command blocking
		getServer().getPluginManager().registerEvents(new CommandPreprocessListener(this), this);
	}
	
	public void onDisabled() {
		logger.info(pdfile.getName() + " version" + pdfile.getVersion() +" disabled!");
	}
	
	public boolean saveHome(UUID id, String name, Location loc) {
		if (!getHomes(id).isEmpty()) {
			File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
			List<String> list = cfg.getStringList(id + "");
			String stringbuilder = name + " " + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
			
			Iterator<String> i = list.iterator();
			while (i.hasNext()) {
			   String s = i.next(); // must be called before you can call i.remove()
			   String[] stringlist = s.split(" ");
			   if (stringlist[0].equalsIgnoreCase(name)) i.remove();
			}
			
			list.add(stringbuilder);
			cfg.set(id + "", list);
			try {
				cfg.save(homesfile);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
			List<String> list = new ArrayList<String>();
			String stringbuilder = name + " " + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
			list.add(stringbuilder);
			cfg.set(id + "", list);
			try {
				cfg.save(homesfile);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public void delHome(UUID id, String name, Location loc, Player p) {
		if (!getHomes(id).isEmpty()) {
			File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
			List<String> list = cfg.getStringList(id + "");			
			String stringbuilder = name + " " + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
			list.remove(stringbuilder);
			cfg.set(id + "", list);
			try {
				cfg.save(homesfile);
				p.sendMessage(ChatColor.GREEN + "Home " + ChatColor.RESET + name + ChatColor.GREEN + " erfolgreich gelöscht.");
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.DARK_RED + "Ein Fehler ist aufgetaucht, bitte melde das einen Administrator.");
			}
		} else {
			p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du hast keine Homes!");
		}
	}
	
	public void clearHomes(UUID id, Player p) {
		if (!getHomes(id).isEmpty()) {
			File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
			List<String> list = new ArrayList<String>();
			cfg.set(id + "", list);
			try {
				cfg.save(homesfile);
				p.sendMessage(ChatColor.GREEN + "Alle homes wurden erfolgreich gelöscht.");
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage(ChatColor.DARK_RED + "Ein Fehler ist aufgetaucht, bitte melde das einen Administrator.");
			}
		} else {
			p.sendMessage(ChatColor.DARK_RED + "Fehler:"+ ChatColor.WHITE + " Du hast keine Homes!");
		}
	}
	
	public Location getHome(UUID id, String name) {
		File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
		List<String> list = cfg.getStringList(id + "");
		
		for (String s : list) {
			String[] stringlist = s.split(" ");
			if (stringlist[0].equals(name)) {
				if (Bukkit.getWorld(stringlist[1]) != null) {
					Location loc = new Location(null, 0, 0, 0, 0, 0);
					loc.setWorld(Bukkit.getWorld(stringlist[1]));
					loc.setX(Double.parseDouble(stringlist[2]));
					loc.setY(Double.parseDouble(stringlist[3]));
					loc.setZ(Double.parseDouble(stringlist[4]));
					loc.setYaw((float) Double.parseDouble(stringlist[5]));
					loc.setPitch((float) Double.parseDouble(stringlist[6]));
					return loc;
				} else return null;
			}
		}
		return null;
	}
	
	public String getHomeNames(UUID id) {
		File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
		List<String> list = cfg.getStringList(id + "");
		String stringbuilder = "";
		
		if (!list.isEmpty()) {
			for (String s : list) {
				String[] stringlist = s.split(" ");
				stringbuilder = stringbuilder + stringlist[0] + " ";
			}
			return stringbuilder;
		} else return ChatColor.ITALIC + "Du hast bisher noch kein Home gelegt!";
	}
	
	public List<String> getHomes(UUID id) {
		File homesfile = new File(corbetplugin.get().getDataFolder(), "Homes.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(homesfile);
		List<String> list = cfg.getStringList(id + "");
		return list;
	}
	
	public boolean getInGame(Player p) {
		if (Bukkit.getServer().getPluginManager().getPlugin("Kickdown") != null) {
			me.pogodaanton.kickdown.game.GamePlayer gp = Kickdown.getpManager().getPlayer(p.getUniqueId());
			
			if (gp.isInGame()) {
				return true;
			}
		}
		
		if (Bukkit.getServer().getPluginManager().getPlugin("BedwarsRel") != null) {
		      io.github.bedwarsrel.BedwarsRel.Game.Game game = Main.getInstance().getGameManager().getGameOfPlayer(p);

		      if (game == null) {
		    	  return false;
		      } else {
		    	  return true;
		      }
		}
		
		if (Bukkit.getServer().getPluginManager().getPlugin("SkyWarsReloaded") != null || Bukkit.getServer().getPluginManager().getPlugin("SkyWarsReloaded2") != null) {
			GamePlayer gPlayer = SkyWarsReloaded.getPC().getPlayer(p.getUniqueId());
			if (gPlayer.inGame() && !gPlayer.isSpectating()) {
				return true;
			} else if (SkyWarsReloaded.getCfg().spectatingEnabled()) {
				if (gPlayer.isSpectating()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void debug(String s) {
		logger.info("\u001B[32m[Debug] \u001B[0m"+ s);
	}	
	
	public static void log(String s) {
		logger.info("\u001B[32m[Info] \u001B[0m"+ s);
	}
	
	public static void error(String s) {
		logger.warning(s);
	}
	
	public static corbetplugin get() {
		return instance;
	}
	
	public static String getJuPrefix() {
		return juPrefix;
	}

	public static spawn getSp() {
		return sp;
	}
	
}
