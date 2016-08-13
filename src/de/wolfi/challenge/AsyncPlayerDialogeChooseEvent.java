package de.wolfi.challenge;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class AsyncPlayerDialogeChooseEvent extends PlayerEvent{

	private static final HandlerList handlers = new HandlerList();
	private String value;
	private int order;
	public AsyncPlayerDialogeChooseEvent(Player who, String value, int order) {
		super(who);
		this.order = order;
		this.value = value;
		// TODO Auto-generated constructor stub
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}

	
	public String getValue() {
		return value;
	}

	public int getOrder() {
		return order;
	}

	public static HandlerList getHandlerList() {
		// TODO Auto-generated method stub
		return handlers;
	}

}
