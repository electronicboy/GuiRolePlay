package xyz.undeaD_D.RPC;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;


	public class EventListener implements Listener {
		private RPC plugin;
		public boolean trail = false;
		
		
		public EventListener(RPC rpc) {
			this.plugin = rpc;
		}
	
		
		@EventHandler
	    public void onInventoryClick(InventoryClickEvent e) {
			// main rpc inv
			if(e.getInventory().getSize() == 45){
	
				if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().hasItemMeta()) {
								
					if(equals(e.getCurrentItem(), plugin.cmd.name)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(0, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.gender)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(1, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.help)) {
						e.setCancelled(true);
						return;
					}else if(equals(e.getCurrentItem(), plugin.cmd.age)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(2, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.race)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(3, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.description)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(4, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.toggle)) {
						plugin.sql.manager((Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.other)) {
						(e.getWhoClicked()).closeInventory();
						plugin.conversation.startnew(5, (Player) e.getWhoClicked());
					}else if(equals(e.getCurrentItem(), plugin.cmd.reload)) {
						plugin.reloadConfig();
						plugin.config = plugin.getConfig();
						plugin.prefix = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("messages.prefix"));
						
						((Player) e.getWhoClicked()).sendMessage(plugin.prefix + ChatColor.translateAlternateColorCodes('&', plugin.config.getString("messages.reload")));						
					}else{
						try{
							if(e.getCurrentItem().getEnchantmentLevel(plugin.ench) == 10){
								e.setCancelled(true);
								return;
							}
						}catch(Exception ex){}
						
						return;
					}
					
					e.setCancelled(true);
					return;
				}
				
			// edit other player inv
			} else if(e.getInventory().getType().equals(InventoryType.HOPPER)) {
				if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().hasItemMeta()) {
					String name = ChatColor.stripColor(e.getInventory().getTitle().substring(3, e.getInventory().getTitle().length()-1));
					Player p = plugin.getServer().getPlayer(name);
					
					if(p != null){
						if(e.getCurrentItem().equals(plugin.cmd.name2)) {
							(e.getWhoClicked()).closeInventory();
							plugin.sql.change(p, 0, "&c-");
						}else if(e.getCurrentItem().equals(plugin.cmd.gender2)) {	
							(e.getWhoClicked()).closeInventory();
							plugin.sql.change(p, 1, "&c-");
						}else if(e.getCurrentItem().equals(plugin.cmd.age2)) {
							(e.getWhoClicked()).closeInventory();
							plugin.sql.change(p, 2, "&c-");
						}else if(e.getCurrentItem().equals(plugin.cmd.race2)) {
							(e.getWhoClicked()).closeInventory();
							plugin.sql.change(p, 3, "&c-");
						}else if(e.getCurrentItem().equals(plugin.cmd.description2)) {	
							(e.getWhoClicked()).closeInventory();
							plugin.sql.change(p, 4, "&c-");
						}else{
							return;
						}
						
						e.setCancelled(true);
						plugin.cmd.openInv((Player)e.getWhoClicked());
						return;
					}
				}
			}
			
	    }
		
		
		private boolean equals(ItemStack a, ItemStack b){
			if(a.getType().equals(b.getType())){
				if(b.hasItemMeta()){
					if(a.getItemMeta().getDisplayName().equals(b.getItemMeta().getDisplayName())){
						return true;
					}
				}
			}
			return false;
		}
		

		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent e) {
			plugin.sql.insertUser(e.getPlayer().getUniqueId().toString());
		}

		
		@EventHandler
		public void onPlayerInteract(PlayerInteractEntityEvent e) {
			if(e.getRightClicked() instanceof Player) {
				if(e.getPlayer().hasPermission("rpc.get")){
					if(plugin.config.getBoolean("settings.must_shift_rightclick") && e.getPlayer().isSneaking()) {
							plugin.sql.display(e.getPlayer(), (Player)e.getRightClicked());
					}else {
						plugin.sql.display(e.getPlayer(), (Player)e.getRightClicked());
					}
				}
			}
		}
		
		
	}
