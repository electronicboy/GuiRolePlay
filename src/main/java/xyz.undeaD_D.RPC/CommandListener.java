package xyz.undeaD_D.RPC;
import java.sql.ResultSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


	public class CommandListener implements CommandExecutor {
		private RPC plugin;
		protected ItemStack reload;
		protected ItemStack nope;
		protected ItemStack view;
		protected ItemStack name;
		protected ItemStack gender;
		protected ItemStack age;
		protected ItemStack race;
		protected ItemStack description;
		protected ItemStack toggle;
		protected ItemStack other;
		protected ItemStack name2;
		protected ItemStack gender2;
		protected ItemStack age2;
		protected ItemStack race2;
		protected ItemStack description2;
		protected ItemStack help;

		
		public CommandListener(RPC rpc) {
			this.plugin = rpc;
			
			nope = new ItemStack(Material.BARRIER, 1);
			ItemMeta m = nope.getItemMeta();
			m.setDisplayName(ChatColor.RED + "-");
			nope.setItemMeta(m);
			
			help = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta m2 = (SkullMeta)help.getItemMeta();
			m2.setOwner("MHF_Question");
			m2.setDisplayName(ChatColor.WHITE + "" + ChatColor.UNDERLINE + "Help");
			List<String> l = plugin.config.getStringList("settings.help_item");
			for(String s : l){s = ChatColor.translateAlternateColorCodes('&', s);}
			m2.setLore(l);
			help.setItemMeta(m2);
			
			view = new ItemStack(Material.PAPER, 1);
			view.addEnchantment(plugin.ench, 10);
			
			name = new ItemStack(Material.BOOK, 1);
			m = name.getItemMeta();
			m.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Set name");
			name.setItemMeta(m);
			
			gender = new ItemStack(Material.BOOK, 1);
			m = gender.getItemMeta();
			m.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Set gender");
			gender.setItemMeta(m);
			
			age = new ItemStack(Material.BOOK, 1);
			m = age.getItemMeta();
			m.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Set age");
			age.setItemMeta(m);
			
			race = new ItemStack(Material.BOOK, 1);
			m = race.getItemMeta();
			m.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Set ethnicity");
			race.setItemMeta(m);
			
			description = new ItemStack(Material.BOOK, 1);
			m = description.getItemMeta();
			m.setDisplayName(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Set description");
			description.setItemMeta(m);
			
				name2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
				m = name2.getItemMeta();
				m.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Reset name");
				name2.setItemMeta(m);
				
				gender2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
				m = gender2.getItemMeta();
				m.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Reset gender");
				gender2.setItemMeta(m);
				
				age2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
				m = age2.getItemMeta();
				m.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Reset age");
				age2.setItemMeta(m);
				
				race2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
				m = race2.getItemMeta();
				m.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Reset ethnicity");
				race2.setItemMeta(m);
				
				description2 = new ItemStack(Material.ENCHANTED_BOOK, 1);
				m = description2.getItemMeta();
				m.setDisplayName(ChatColor.RED + "" + ChatColor.UNDERLINE + "Reset description");
				description2.setItemMeta(m);
			
			toggle = new ItemStack(Material.ENDER_CHEST, 1);
			m = toggle.getItemMeta();
			m.setDisplayName(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Toggle Cards");
			toggle.setItemMeta(m);
			
			other = new ItemStack(Material.BOOK_AND_QUILL, 1);
			m = other.getItemMeta();
			m.setDisplayName(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Edit other player");
			other.setItemMeta(m);
			
			reload = new ItemStack(Material.COMMAND, 1);
			m = reload.getItemMeta();
			m.setDisplayName(ChatColor.BLUE + "" + ChatColor.UNDERLINE + "Reload the config");
			reload.setItemMeta(m);
			
		}
	
	
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if(sender instanceof Player){
				if(sender.hasPermission("rpc.cmd")){
					openInv((Player)sender);
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("messages.no_perm")));
				}			
			}else{
				System.out.println(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("messages.cmd_ingame_only"))));
			}
			
			return true;
		}
		
		
		public void openInv(Player p){
						
			Inventory inv = plugin.getServer().createInventory(p, 45, ChatColor.translateAlternateColorCodes('&', plugin.config.getString("settings.inv_title").replace("%PLAYER%", p.getName())));

			try{
				ResultSet re = plugin.sql.get(p);
			    if(re != null && re.next()) {
					String re_nick = re.getString("NAME");
					String re_gender = re.getString("GENDER");
					String re_age = re.getString("AGE");
					String re_race = re.getString("RACE");
					String re_desc = re.getString("DESCRIPTION").replace("%AP%","'");
					if(re_desc.length() > 25){re_desc = re_desc.substring(0, 25) + "...";}
					re.close();
					
					ItemStack n = view.clone();
					ItemMeta m = n.getItemMeta();
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', re_nick));
					n.setItemMeta(m);
					
					ItemStack g = view.clone();
					m = g.getItemMeta();
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', re_gender));
					g.setItemMeta(m);
					
					ItemStack a = view.clone();
					m = a.getItemMeta();
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', re_age));
					a.setItemMeta(m);
					
					ItemStack r = view.clone();
					m = r.getItemMeta();
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', re_race));
					r.setItemMeta(m);
					
					ItemStack d = view.clone();
					m = d.getItemMeta();
					m.setDisplayName(ChatColor.translateAlternateColorCodes('&', re_desc));
					d.setItemMeta(m);
					
					inv.setItem(28, n);
					inv.setItem(29, g);
					inv.setItem(30, a);
					inv.setItem(31, r);
					inv.setItem(32, d);
			    }
			}catch(Exception ex){ }
							
			inv.setItem(10, name);
			inv.setItem(11, gender);
			inv.setItem(12, age);
			inv.setItem(13, race);
			inv.setItem(14, description);
			
				if(plugin.config.getBoolean("")){
					inv.setItem(8, help);
				}

				if(p.hasPermission("rpc.inv.view.toggle")){
					inv.setItem(16, toggle);
				}else{inv.setItem(16, nope);}
				
				if(p.hasPermission("rpc.inv.view.other")){
					inv.setItem(25, other);
				}else{inv.setItem(25, nope);}
				
				if(p.hasPermission("rpc.inv.view.reload")){
					inv.setItem(34, reload);
				}else{inv.setItem(34, nope);}
				
			p.openInventory(inv);
		}
		
		
		public void openInv_other(Player p, String nick){
			Inventory inv = plugin.getServer().createInventory(p, InventoryType.HOPPER, ChatColor.translateAlternateColorCodes('&', plugin.config.getString("settings.inv_title").replace("%PLAYER%", nick)));

				inv.setItem(0, name2);
				inv.setItem(1, gender2);
				inv.setItem(2, age2);
				inv.setItem(3, race2);
				inv.setItem(4, description2);
				
			p.openInventory(inv);
		}
		
		
	}
