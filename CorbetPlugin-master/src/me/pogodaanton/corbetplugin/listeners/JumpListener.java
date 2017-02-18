package me.pogodaanton.corbetplugin.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.pogodaanton.corbetplugin.corbetplugin;

public class JumpListener implements Listener {
	
	public static HashMap<Player, Location> jumpMap = new HashMap<Player, Location>();
	public static List<Player> gotJumpTp = new ArrayList<Player>();
	private String juPrefix = corbetplugin.getJuPrefix();
	
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack item = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
		
		if ((e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && item.getType() == Material.GOLD_BLOCK && item.getAmount() == 1 && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Jump'n Run Kit")) {
			ItemStack jumpStart = getPlateStack("start");
			ItemStack jumpCheck = getPlateStack("checkpoint");
			ItemStack jumpEnd = getPlateStack("ende");
			jumpCheck.setAmount(3);
			
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 6);
			p.getInventory().removeItem(item);
			p.getInventory().addItem(jumpStart);
			p.getInventory().addItem(jumpCheck);
			p.getInventory().addItem(jumpEnd);
		} else if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.GOLD_PLATE) {			
			List<String> startList = getJump("start");
			List<String> endList = getJump("ende");
			Location loc = e.getClickedBlock().getLocation();
			
			if (!startList.isEmpty()) {
				for (String s : startList) {
					String[] stringlist = s.split(" ");
					if (stringlist[0].equals(loc.getWorld().getName()) &&
						loc.getX() == Double.parseDouble(stringlist[1]) &&
						loc.getY() == Double.parseDouble(stringlist[2]) &&
						loc.getZ() == Double.parseDouble(stringlist[3])
						) {
							if (!jumpMap.containsKey(p)) {
								jumpMap.put(p, loc);
								p.sendMessage(juPrefix + "Jump'n Run gestartet, hüpf los!");
							} else {
								if (!jumpMap.get(p).equals(loc)) {
									jumpMap.put(p, loc);
									p.sendMessage(juPrefix + "Jump'n Run neugestartet, hüpf los!");
								}
							}
						}
				}
			}
			
			if (!endList.isEmpty()) {
				for (String s : endList) {
					String[] stringlist = s.split(" ");
					if (stringlist[0].equals(loc.getWorld().getName()) &&
						loc.getX() == Double.parseDouble(stringlist[1]) &&
						loc.getY() == Double.parseDouble(stringlist[2]) &&
						loc.getZ() == Double.parseDouble(stringlist[3])
						) {
							if (jumpMap.containsKey(p)) {
								File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
								FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
								String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
								ItemStack reward = cfg.getItemStack("rewards."+ stringbuilder);
								
								jumpMap.remove(p);
								p.sendMessage(juPrefix + "Jump'n Run erfolgreich beendet!");
								if (reward != null) {
									if (p.getInventory().firstEmpty() != -1) p.getInventory().addItem(reward);
									else p.sendMessage(juPrefix + "Leider konnten wir dir keine Belohnung geben, da dein Inventar voll ist. Bitte leere es erstmal aus!");
								}
							} else {
								p.sendMessage(juPrefix + "Du bist nicht in einem Jump'n Run, bitte starte beim Anfang!");
							}
						}
					}
				}
		} else if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock().getType() == Material.IRON_PLATE) {
			List<String> checkList = getJump("checkpoint");
			Location loc = e.getClickedBlock().getLocation();
			
			if (!checkList.isEmpty()) {
				for (String s : checkList) {
					String[] stringlist = s.split(" ");
					if (stringlist[0].equals(loc.getWorld().getName()) &&
						loc.getX() == Double.parseDouble(stringlist[1]) &&
						loc.getY() == Double.parseDouble(stringlist[2]) &&
						loc.getZ() == Double.parseDouble(stringlist[3])
						) {
							if (jumpMap.containsKey(p)) {
								if (!jumpMap.get(p).equals(loc)) {
									jumpMap.put(p, loc);
									p.sendMessage(juPrefix + "Jump'n Run Checkpoint erreicht! Schreibe " + ChatColor.AQUA + "/ju" + ChatColor.GOLD + ", um hierhin zu teleportieren.");
								}
							} else {
								p.sendMessage(juPrefix + "Du bist nicht in einem Jump'n Run, bitte starte beim Anfang!");
							}
						}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		ItemStack item = e.getItemInHand();
		Player p = e.getPlayer();
		Block placedBlock = e.getBlockPlaced();
		Location placedLoc = placedBlock.getLocation();
		Location underLoc = placedLoc.subtract(0.0,1.0,0.0);
		Block underBlock = underLoc.getBlock();
		
		if (item.getItemMeta().getDisplayName() != null) { 
			if (item.getType() == Material.GOLD_PLATE && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Jump'n Run: " + ChatColor.GREEN + "Start")) {
				if (underBlock.getType().equals(Material.SAND) || underBlock.getType().equals(Material.GRAVEL)) {
					p.sendMessage(juPrefix + ChatColor.RED + "Aus Sicherheitsgründen darfst du Jump'n Run-Platten nicht auf Sand und Kies bauen!");
					e.setCancelled(true);
				} else {
					placedLoc.add(0.0,1.0,0.0);
					saveJump("start", placedLoc);
					p.sendMessage(juPrefix + "Jump'n Run Start gesetzt!");
				}
			} else if (item.getType() == Material.GOLD_PLATE && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Jump'n Run: " + ChatColor.RED + "Ende")) {
				if (underBlock.getType().equals(Material.SAND) || underBlock.getType().equals(Material.GRAVEL)) {
					p.sendMessage(juPrefix + ChatColor.RED + "Aus Sicherheitsgründen darfst du Jump'n Run-Platten nicht auf Sand und Kies bauen!");
					e.setCancelled(true);
				} else {
					placedLoc.add(0.0,1.0,0.0);
					saveJump("ende", placedLoc);
					p.sendMessage(juPrefix + "Jump'n Run Ende gesetzt!");
				}
			} else if (item.getType() == Material.IRON_PLATE && item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Jump'n Run: " + ChatColor.GRAY + "Checkpoint")) {
				if (underBlock.getType().equals(Material.SAND) || underBlock.getType().equals(Material.GRAVEL)) {
					p.sendMessage(juPrefix + ChatColor.RED + "Aus Sicherheitsgründen darfst du Jump'n Run-Platten nicht auf Sand und Kies bauen!");
					e.setCancelled(true);
				} else {
					placedLoc.add(0.0,1.0,0.0);
					saveJump("checkpoint", placedLoc);
					p.sendMessage(juPrefix + "Jump'n Run Checkpoint gesetzt!");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e){        
		List<String> startList = getJump("start");
		List<String> checkList = getJump("checkpoint");
		List<String> endList = getJump("ende");
		Block b = e.getBlock();
		Location loc = b.getLocation();
		Player p = e.getPlayer();
		
		if (!startList.isEmpty()) {
			for (String s : startList) {
				String[] stringlist = s.split(" ");
				if (stringlist[0].equals(loc.getWorld().getName()) &&
					loc.getX() == Double.parseDouble(stringlist[1]) &&
					loc.getY() == Double.parseDouble(stringlist[2]) &&
					loc.getZ() == Double.parseDouble(stringlist[3])
					) {
						removeJump("start", loc);
						p.sendMessage(juPrefix + "Jump'n Run Start gelöscht!");
						kickAll(loc);
						
						if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
							b.setType(Material.AIR);
							b.getWorld().dropItemNaturally(b.getLocation().add(new Vector().setX(0.5).setY(0.5).setZ(0.5)), getPlateStack("start"));
							e.setCancelled(true);
						}
				} else if (stringlist[0].equals(loc.getWorld().getName()) &&
							loc.getX() == Double.parseDouble(stringlist[1]) &&
							(loc.getY() + 1.0) == Double.parseDouble(stringlist[2]) &&
							loc.getZ() == Double.parseDouble(stringlist[3])
							) {
					p.sendMessage(juPrefix + "Bitte bau erstmal die Jump'n Run-Platte über den Block ab!");
					e.setCancelled(true);
				}
			}
		}
		
		if (!checkList.isEmpty()) {
			for (String s : checkList) {
				String[] stringlist = s.split(" ");
				if (stringlist[0].equals(loc.getWorld().getName()) &&
					loc.getX() == Double.parseDouble(stringlist[1]) &&
					loc.getY() == Double.parseDouble(stringlist[2]) &&
					loc.getZ() == Double.parseDouble(stringlist[3])
					) {
						removeJump("checkpoint", loc);
						p.sendMessage(juPrefix + "Jump'n Run Checkpoint gelöscht!");
						kickAll(loc);
						
						if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
							b.setType(Material.AIR);
							b.getWorld().dropItemNaturally(b.getLocation().add(new Vector().setX(0.5).setY(0.5).setZ(0.5)), getPlateStack("checkpoint"));
							e.setCancelled(true);
						}
				} else if (stringlist[0].equals(loc.getWorld().getName()) &&
						loc.getX() == Double.parseDouble(stringlist[1]) &&
						(loc.getY() + 1.0) == Double.parseDouble(stringlist[2]) &&
						loc.getZ() == Double.parseDouble(stringlist[3])
						) {
					p.sendMessage(juPrefix + "Bitte bau erstmal die Jump'n Run-Platte über den Block ab!");
					e.setCancelled(true);
				}
			}
		}
		
		if (!endList.isEmpty()) {
			for (String s : endList) {
				String[] stringlist = s.split(" ");
				if (stringlist[0].equals(loc.getWorld().getName()) &&
					loc.getX() == Double.parseDouble(stringlist[1]) &&
					loc.getY() == Double.parseDouble(stringlist[2]) &&
					loc.getZ() == Double.parseDouble(stringlist[3])
					) {
						removeJump("ende", loc);
						p.sendMessage(juPrefix + "Jump'n Run Ende gelöscht!");
						kickAll(loc);
						
						if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
							b.setType(Material.AIR);
							b.getWorld().dropItemNaturally(b.getLocation().add(new Vector().setX(0.5).setY(0.5).setZ(0.5)), getPlateStack("ende"));
							e.setCancelled(true);
						}
				} else if (stringlist[0].equals(loc.getWorld().getName()) &&
						loc.getX() == Double.parseDouble(stringlist[1]) &&
						(loc.getY() + 1.0) == Double.parseDouble(stringlist[2]) &&
						loc.getZ() == Double.parseDouble(stringlist[3])
						) {
					p.sendMessage(juPrefix + "Bitte bau erstmal die Jump'n Run-Platte über den Block ab!");
					e.setCancelled(true);
				}
			}
		}
    }
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if (!gotJumpTp.contains(p) && jumpMap.containsKey(p)) {
			jumpMap.remove(p);
			p.sendMessage(juPrefix + "Jump'n Run-Modus verlassen!");
		} else {
			gotJumpTp.remove(p);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (jumpMap.containsKey(p)) {
			jumpMap.remove(p);
			p.sendMessage(juPrefix + "Jump'n Run-Modus verlassen!");
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (jumpMap.containsKey(p)) {
			jumpMap.remove(p);
			p.sendMessage(juPrefix + "Jump'n Run-Modus verlassen!");
		}
	}
	
	public void saveJump(String part, Location loc) {
		if (!getJump(part).isEmpty()) {
			File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
			List<String> list = cfg.getStringList(part);
			String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
			list.add(stringbuilder);
			cfg.set(part, list);
			try {
				cfg.save(jumpsfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
			List<String> list = new ArrayList<String>();
			String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
			list.add(stringbuilder);
			cfg.set(part, list);
			try {
				cfg.save(jumpsfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeJump(String part, Location loc) {
		if (!getJump(part).isEmpty()) {
			File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
			List<String> list = cfg.getStringList(part);			
			String stringbuilder = loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
			list.remove(stringbuilder);
			cfg.set(part, list);
			try {
				cfg.save(jumpsfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
	}
	
	public List<String> getJump(String point) {
		File jumpsfile = new File(corbetplugin.get().getDataFolder(), "Jumps.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(jumpsfile);
		List<String> list = cfg.getStringList(point);
		return list;
	}
	
	public void kickAll(Location loc) {
		for (Player p : jumpMap.keySet()) {
			if (jumpMap.get(p).equals(loc)) {
				jumpMap.remove(p);
				p.sendMessage(juPrefix + "Dein letzter Checkpoint wurde gelöscht, deshalb hast du den Jump'n Run automatisch verlassen.");
			}
		}
	}
	
	public ItemStack getPlateStack(String type) {
		ItemStack item = new ItemStack(Material.LOG, 1);
		
		if (type.equalsIgnoreCase("start")) {
			item = new ItemStack(Material.GOLD_PLATE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "Jump'n Run: " + ChatColor.GREEN + "Start");
			item.setItemMeta(meta);
			
		} else if (type.equalsIgnoreCase("checkpoint")) {
			item = new ItemStack(Material.IRON_PLATE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "Jump'n Run: " + ChatColor.GRAY + "Checkpoint");
			item.setItemMeta(meta);
			
		} else if (type.equalsIgnoreCase("ende")) {
			item = new ItemStack(Material.GOLD_PLATE, 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GOLD + "Jump'n Run: " + ChatColor.RED + "Ende");
			item.setItemMeta(meta);
			
		} else {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Fehler im Programm!!!111elfhundertelfzig");
			item.setItemMeta(meta);
		}
		
		return item;
	}
}
