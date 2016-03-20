package xyz.undeaD_D.RPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;


	@SuppressWarnings("SpellCheckingInspection")
	public class ConversationManager implements ConversationAbandonedListener {
		private ConversationFactory conv_1;
		private ConversationFactory conv_2;
		private ConversationFactory conv_3;
		private ConversationFactory conv_4;
		private ConversationFactory conv_5;
		private ConversationFactory conv_6;
		private RPC plugin;
		
		
		public ConversationManager(RPC rpc) {
			this.plugin = rpc;
			
			String ingame_only = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("messages.cmd_ingame_only")));
			
			conv_1 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start1())
			        .withTimeout(20)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
			
			conv_2 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start2())
			        .withTimeout(25)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
			
			conv_3 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start3())
			        .withTimeout(25)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
			
			conv_4 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start4())
			        .withTimeout(25)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
			
			conv_5 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start5())
			        .withTimeout(25)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
			
			conv_6 = new ConversationFactory(plugin)
			        .withModality(true)
			        .withFirstPrompt(new Start6())
			        .withTimeout(25)
			        .thatExcludesNonPlayersWithMessage(ingame_only)
			        .addConversationAbandonedListener(this);
		}
		
		
		public void startnew(int i, Player p){
			switch(i){
			case 0:
				// name
				conv_1.buildConversation((Conversable)p).begin();
				return;
			case 1:
				// gender
				conv_2.buildConversation((Conversable)p).begin();
				return;
			case 2:
				// age
				conv_3.buildConversation((Conversable)p).begin();
				return;
			case 3:
				// race
				conv_4.buildConversation((Conversable)p).begin();
				return;
			case 4:
				// description
				conv_5.buildConversation((Conversable)p).begin();
				return;
			case 5:
				// edit other player
				conv_6.buildConversation((Conversable)p).begin();
				return;
			}
		}
		
		
		@Override
		public void conversationAbandoned(ConversationAbandonedEvent e) {
			e.getContext().getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
		}
		
		
		// NAME -----------------------------------------------------------------------------------------------------------------
		
		
	    private class Start1 extends StringPrompt {
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	            return  ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.nick_start"));
	        }
	
	
			@Override
			public Prompt acceptInput(ConversationContext session, String data) {
				if(data != null || data != null){
					if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
						session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
						plugin.cmd.openInv((Player) session.getForWhom());
						return Prompt.END_OF_CONVERSATION;
					}
					session.setSessionData("nick", data);
					return new End1();
				}else{
					session.setSessionData("nick", "-");
					return new End1();
				}
			}
	    }
	
	    
	    private class End1 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	
	        	String nick = (String)session.getSessionData("nick");
	        	if(nick.length() > 16){nick = nick.substring(0, 15);}
	        	plugin.sql.change((Player) session.getForWhom(), 0, nick);
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.nick_done")) + nick;
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	        	plugin.cmd.openInv((Player) session.getForWhom());
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
		// GENDER -----------------------------------------------------------------------------------------------------------------
	
	    
	    private class Start2 extends ValidatingPrompt {
	    	private String[] allowedInputs = new String[] { "female", "male", "other", "none" };
	
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.gender_start")) + "[Female, Male, Other, None]";
	        }
	
	        @Override
	        protected boolean isInputValid(ConversationContext session, String data) {
	        	if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
	        		session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
	        		session.setSessionData("abort", true);
					return true;
				}
	        	
	            String[] split = data.split(" ");
	            if(split.length > 2)
	            {
	                session.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.gender_error1")));
	                return false;
	            }
	     
	            boolean found = false;
	            for(String allowed : this.allowedInputs)
	            {
	                if(allowed.equalsIgnoreCase(split[0]))
	                {
	                    found = true;
	                    break;
	                }
	            }
	     
	            if(!found)
	            {
	                session.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.gender_error2")));
	                return false;
	            }
	     
	            return true;
	        }
	        
	        @Override
	        protected Prompt acceptValidatedInput(ConversationContext session, String s) {
	        	if(session.getSessionData("abort") != null && (boolean) session.getSessionData("abort")){
	        		plugin.cmd.openInv((Player) session.getForWhom());
					return Prompt.END_OF_CONVERSATION;
	        	}else{
	        		session.setSessionData("gender", s);
	            	return new End2();
	        	}
	        }
	    }
	    
	    
	    private class End2 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	
	        	String gender = (String)session.getSessionData("gender");
	        	plugin.sql.change((Player) session.getForWhom(), 1, gender);
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.gender_done")) + gender;
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	        	plugin.cmd.openInv((Player) session.getForWhom());
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
		// AGE -----------------------------------------------------------------------------------------------------------------
	
	    private class Start3 extends ValidatingPrompt {	
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.age_start"));
	        }
	
	        @Override
	        protected boolean isInputValid(ConversationContext session, String data) {
	        	if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
	        		session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
	        		session.setSessionData("abort", true);
					return true;
				}
	        	
	        	try{
	        		Integer i = (int) Float.parseFloat(data);
	        		
	        		if(0 < i && i < 999999){
	        			return true;
	        		}else{
	        			session.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.age_error1")));
		                return false;
	        		}
	        	}catch(Exception ex){
	        		session.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.age_error2")));
	                return false;
	        	}
	        }
	        
	        @Override
	        protected Prompt acceptValidatedInput(ConversationContext session, String s) {
	        	if(session.getSessionData("abort") != null && (boolean) session.getSessionData("abort")){
	        		plugin.cmd.openInv((Player) session.getForWhom());
					return Prompt.END_OF_CONVERSATION;
	        	}else{
	        		session.setSessionData("age", (int) Float.parseFloat(s));
	 	            return new End3();
	        	}
	        }
	    }
	    
	    
	    private class End3 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	
	        	int age = (int) session.getSessionData("age");
	        	plugin.sql.change((Player) session.getForWhom(), 2, age + "");
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.age_done")) + age;
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	        	plugin.cmd.openInv((Player) session.getForWhom());
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
		// RACE -----------------------------------------------------------------------------------------------------------------
		
		
	    private class Start4 extends StringPrompt {
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	        	return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.race_start"));
	        }
	
	
			@Override
			public Prompt acceptInput(ConversationContext session, String data) {
				if(data != null || data != null){
					if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
						session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
						plugin.cmd.openInv((Player) session.getForWhom());
						return Prompt.END_OF_CONVERSATION;
					}
					session.setSessionData("race", data);
					return new End4();
				}else{
					session.setSessionData("race", "-");
					return new End4();
				}
			}
	    }
	
	    
	    private class End4 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	
	        	String race = (String)session.getSessionData("race");
	        	plugin.sql.change((Player) session.getForWhom(), 3, race);
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.race_done")) + race;
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	        	plugin.cmd.openInv((Player) session.getForWhom());
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
		// DESCRIPTION -----------------------------------------------------------------------------------------------------------------
		
		
	    private class Start5 extends StringPrompt {
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.desc_start"));
	        }
	
	
			@Override
			public Prompt acceptInput(ConversationContext session, String data) {
				if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
					session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
					plugin.cmd.openInv((Player) session.getForWhom());
					return Prompt.END_OF_CONVERSATION;
				}else{
					session.setSessionData("desc", data);
					return new Loop5();
				}
			}
	    }
	    
	    
	    private class Loop5 extends StringPrompt {
	        public String getPromptText(ConversationContext session) {
	            return "";
	        }
	
	
			@Override
			public Prompt acceptInput(ConversationContext session, String data) {
				if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
					session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
					return Prompt.END_OF_CONVERSATION;
				}else if(data.equalsIgnoreCase("done")){
					return new End5();
				}else{
					session.setSessionData("desc", session.getSessionData("desc") + " " + data);
					return new Loop5();
				}
			}
	    }
	
	    
	    private class End5 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	
	        	String desc = (String)session.getSessionData("desc");
	        	plugin.sql.change((Player) session.getForWhom(), 4, desc);
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.desc_done"));
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	        	plugin.cmd.openInv((Player) session.getForWhom());
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
		// OTHER PLAYER -----------------------------------------------------------------------------------------------------------------
	    
	    private class Start6 extends ValidatingPrompt {	
	        public String getPromptText(ConversationContext session) {
	        	session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.box")));
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.other_start"));
	        }
	
	        @Override
	        protected boolean isInputValid(ConversationContext session, String data) {
	        	if(data.equalsIgnoreCase("cancel") || data.equalsIgnoreCase("exit")){
	        		session.getForWhom().sendRawMessage( ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.cancel")));
	        		session.setSessionData("abort", true);
					return true;
				}
	        	
	        	try{
	        		Player p = plugin.getServer().getPlayer(data);
	        		
	        		if(p != null && p instanceof Player){
	        			return true;
	        		}else{
	        			session.getForWhom().sendRawMessage(ChatColor.RED + data + " is not online!");
		                return false;
	        		}
	        	}catch(Exception ex){
	        		session.getForWhom().sendRawMessage(ChatColor.RED + data + " is not online!");
	                return false;
	        	}
	        	
	        }
	        
	        @Override
	        protected Prompt acceptValidatedInput(ConversationContext session, String s) {
	        	if(session.getSessionData("abort") != null && (boolean) session.getSessionData("abort")){
					return Prompt.END_OF_CONVERSATION;
	        	}else{
	        		Player p = plugin.getServer().getPlayer(s);
	        		session.setSessionData("player",p);
		            return new End6();
	        	}
	        }
	    }
	    
	    
	    private class End6 extends MessagePrompt {
	        public String getPromptText(ConversationContext session) {
	        	Player p = (Player)session.getForWhom();
	        	Player target = (Player)session.getSessionData("player");
	        	plugin.cmd.openInv_other(p, target.getName());
	
	            return ChatColor.translateAlternateColorCodes('&', plugin.config.getString("conversations.other_done"));
	        }
	
	        @Override
	        protected Prompt getNextPrompt(ConversationContext session) {
	            return Prompt.END_OF_CONVERSATION;
	        }
	    }
	    
	    
	}
