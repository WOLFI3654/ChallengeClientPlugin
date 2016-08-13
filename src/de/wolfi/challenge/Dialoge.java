package de.wolfi.challenge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Dialoge {

	private static final TextComponent header = new TextComponent("-----------------");
	private static final TextComponent empty = new TextComponent("§r");
	private static final TextComponent arrows = new TextComponent(">> ");
	static{
		header.setColor(ChatColor.GRAY);
		header.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "q:**:-1"));
		arrows.setColor(ChatColor.DARK_PURPLE);
		
		empty.setColor(ChatColor.RESET);
	}
	private boolean q;
	final String player;
	final String message;
	final List<String> questions;
	final int orderFrom;
	public Dialoge(String player, String message){
		q = false;
		this.player = player;
		this.message = message;
		this.orderFrom = -1;
		this.questions = new ArrayList<>();
	}
	
	public Dialoge(List<String> questions, int o){
		q = true;
		player = null;
		this.orderFrom = o;
		message = null;
		this.questions = questions;
	}
	
	
	public void send(){
		if(q){
			
			send(header);
			for(int i = 0; i < questions.size(); i++){
				String txt = questions.get(i);
				send(empty);
				send("§e"+(i+1)+": §6\""+txt+"§6\"", "q:"+txt+":"+orderFrom);
			}
			send(empty);
			send(header);
		}else{
			TextComponent c = new TextComponent(player);
			c.setColor(ChatColor.DARK_GRAY);
			c.addExtra(arrows);
			TextComponent msg = new TextComponent(message);
			msg.setColor(ChatColor.GOLD);
			c.addExtra(msg);
			c.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "q:**:-1"));
			send(c);
//			send("§8"+player+"§5>> §6"+message, "q:**:-1");
		}
	}
	
	private void send(BaseComponent c){
		
		for(Player p : Bukkit.getOnlinePlayers()){
			p.spigot().sendMessage(c);
		}
	}
	
	private void send(String msg, String onClick){
		TextComponent t = new TextComponent(msg);
		t.setClickEvent(new ClickEvent(Action.RUN_COMMAND, onClick));
		send(t);
		
	}
}
