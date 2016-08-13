package de.wolfi.challenge;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.MoveToGoal;
import net.citizensnpcs.api.trait.Trait;

public class DamageTrait extends Trait{

	public DamageTrait() {
		super("dmgfollow");
		
	}
	
	
	MoveToGoal last;
	@Override
	public void run() {
		if(!npc.isSpawned()){
			if(npc.getStoredLocation() == null){
				Bukkit.broadcastMessage("Wrong NPC: "+npc.getId());
				return;
			}
			npc.spawn(npc.getStoredLocation());
		}
		for(Entity player :npc.getEntity().getWorld().getNearbyEntities(npc.getEntity().getLocation(), 10,10, 10)){
			if(player instanceof Player && !CitizensAPI.getNPCRegistry().isNPC(player)){
				if(((Player) player).getGameMode() == GameMode.ADVENTURE) npc.getNavigator().setTarget(player.getLocation());
//				npc.getDefaultGoalController().addBehavior(new TargetNearbyEntityGoal.Builder(npc), 1);
//				 if(last != null) npc.getDefaultGoalController().removeGoal(last);
//				npc.getDefaultGoalController().addGoal((last = new MoveToGoal(npc, player.getLocation())), 1);
			}
		}
		
		for(Entity player :npc.getEntity().getWorld().getNearbyEntities(npc.getEntity().getLocation(), 1.2,2, 1.2)){
			if(player instanceof Player && !CitizensAPI.getNPCRegistry().isNPC(player)){
				if(((Player) player).getGameMode() == GameMode.ADVENTURE)((Damageable)player).damage(2,npc.getEntity());
			}
		}

		
	}
}
