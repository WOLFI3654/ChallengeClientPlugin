package de.wolfi.challenge;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import de.wolfi.utils.ParticleAPI;
import de.wolfi.utils.Reflection;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.api.trait.trait.MobType;
import net.citizensnpcs.trait.Gravity;
import net.citizensnpcs.trait.LookClose;

public class Main extends JavaPlugin implements Listener{

	protected static int number = (int)(Math.random() * 150);

	private static final int last = 5;
	
	private boolean dialoge = false;
	
	private static final Dialoge wrongAnswer = new Dialoge("WOLFI3654", "Wie kannst du es wagen! Foltert ihn!");
	
	private static final Dialoge lostSteve = new Dialoge("Hannigro", "Wenn Sie diesen Steve gefunden haben geben Sie ihn bitte bei Hannigro ab! Danke.");
	
	private static final Dialoge cakeThief = new Dialoge("Hannigro", "Du möchtest also Kuchen klauen!");
	
	private static final Dialoge cookieThief = new Dialoge("Krümelmonster", "Du willst also meine Kekse stehlen?");
	
	private Location startPoint= new Location(Bukkit.getWorld("world"), 345.5, 179.5, 227.5);
	
	private Location treasureOpen = new Location(Bukkit.getWorld("world"), 316, 179, 234);
	
	private Location plate = new Location(Bukkit.getWorld("world"), 313, 179, 237);
	
	private Location treasureIn = new Location(Bukkit.getWorld("world"), 311, 179, 239);
	
	private Location start = new Location(Bukkit.getWorld("world"), 72, 70, 471);

	private Location hole = new Location(Bukkit.getWorld("world"), 36, 87, 481);
	
	private Location punish = new Location(Bukkit.getWorld("world"),314.0, 191, 217.5);
	
	private Location finish = new Location(Bukkit.getWorld("world"),276, 59, 289);
	
	private Location rsp = null;
	
	public static final List<String> wrongAnswers;
	public static final HashMap<Integer,ArrayList<Dialoge>> dialogs = new HashMap<>();

	public static final String VERSION = "1.6.4-Hotfix";
	
	static{
		ArrayList<Dialoge> zero = new ArrayList<>();
		zero.add(new Dialoge("WOLFI3654", "Willkommen, junger Recke, in meinem wunderschönen Schloss!"));
		zero.add(new Dialoge(Arrays.asList("Hallo du alter Mann!","Grüezi ihr Almdudler!","Ich verneige mich vor euch, Majestät."), 1));
		ArrayList<Dialoge> one = new ArrayList<>();
		one.add(new Dialoge("WOLFI3654", "Herzlichen Glückwunsch, du hast alle Aufgaben mit Bravour gemeistert und dich als mutiger Kämpfer erwiesen."));
		one.add(new Dialoge(Arrays.asList("War’n ja auch nich‘ sonderlich schwer, ihr Luschen.","Vielen Dank, Hoheit","Easy going."), 2));
		ArrayList<Dialoge> two = new ArrayList<>();
		two.add(new Dialoge("Zangenbert", "Mir wurde auch nicht gratuliert, als ich alle Aufgaben testen musste!"));
		two.add(new Dialoge(Arrays.asList("Ruhe auf den billigen Plätzen!","Was bist du denn für ein Opfer?!","Das tut mir leid."), 3));
		ArrayList<Dialoge> three = new ArrayList<>();
		three.add(new Dialoge("Clexxer", "Hey, kennt ihr den schon? Hab‘ Gomme gestern ‘nen Limonadenwitz erzählt. Fanta lustig!"));
		three.add(new Dialoge(Arrays.asList("HAHAHAHAHAHAHAHA","Kenn‘ ich schon.","..."), 4));
		ArrayList<Dialoge> four = new ArrayList<>();
		four.add(new Dialoge("Reved", "Den hast du jetzt schon zum zehnten Mal erzählt."));
		four.add(new Dialoge("YouDid", "Genau genommen war es erst das neunte Mal."));
		four.add(new Dialoge("WOLFI3654", "Ruhe! Hier geht es ja zu wie im Irrenhaus! Zurück zu dir, tapferer Krieger: Als Belohnung werde ich dich mit Ruhm überschütten."));
		four.add(new Dialoge(Arrays.asList("Eigentlich hatte ich mir einen R8 erhofft...","Das ist sehr großzügig von ihnen.","Kann ich nicht lieber Kohle haben?"), 5));
		ArrayList<Dialoge> five = new ArrayList<>();
		five.add(new Dialoge("WOLFI3654", "Folge mir in die Schatzkammer!"));
		five.add(new Dialoge("WOLFI3654", "Ihr Inhalt soll dir gehören und mein Königreich!"));
		five.add(new Dialoge("WOLFI3654", "Ich werde ja langsam alt... Du sollst mein Nachfolger werden."));
		ArrayList<Dialoge> six = new ArrayList<>();
		six.add(new Dialoge("WOLFI3654", "AAAH. Mein Herz. Schon wieder..."));
		six.add(new Dialoge("WOLFI3654", "URG"));
		
		ArrayList<Dialoge> start = new ArrayList<>();
		start.add(new Dialoge("GommeHD", "Was ist das für ein Lärm?"));
		start.add(new Dialoge("GommeHD", "Warum ist es so hell draußen?"));
		start.add(new Dialoge("GommeHD", "... Oh. Man. Ein Wurmloch!"));
		start.add(new Dialoge("GommeHD", "Es hat einen Meteoritenschauer gegeben und einer hat einen Knick im Raum erzeugt!"));
		start.add(new Dialoge("GommeHD", "HILFE. ES ZIEHT MICH AN!!!!"));
		
		dialogs.put(-1, start);
		
		dialogs.put(0, zero);
		dialogs.put(1, one);
		dialogs.put(2, two);
		dialogs.put(3, three);
		dialogs.put(4, four);
		dialogs.put(5, five);
		dialogs.put(6, six);
		wrongAnswers = Arrays.asList("Hallo du alter Mann!",
				"War’n ja auch nich‘ sonderlich schwer, ihr Luschen.",
				"Das tut mir leid.",
				"HAHAHAHAHAHAHAHA",
				"Eigentlich hatte ich mir einen R8 erhofft...","Kann ich nicht lieber Kohle haben?");
	}
	private Plb server;
	
	@Override
	public void onEnable() {
		if(startPoint.getWorld() == null){
			Bukkit.reload();
			return;
		}
		cfg();
		Bukkit.getPluginManager().registerEvents(this, this);
		for(Player p : Bukkit.getOnlinePlayers()){
			try {
				Object handle = p.getClass().getMethod("getHandle").invoke(p);
				Object connection = handle.getClass().getField("playerConnection").get(handle);
				Object packet = Reflection.getNMSClass("PacketPlayOutGameStateChange").getConstructor(int.class,float.class).newInstance(9,1);
				connection.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, packet);
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(DamageTrait.class).withName("dmgfollow"));
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, ()->{if(!dialoge)playParticle();  }, 1, 3);
		Bukkit.getScheduler().runTaskTimer(this, ()->CitizensAPI.getNPCRegistry().forEach((n)->{if(n.hasTrait(DamageTrait.class)) n.getTrait(DamageTrait.class).run();}), 1, 1);
		Bukkit.getScheduler().runTaskAsynchronously(this, server = new Plb());
	}
		
	
	@Override
	public void onDisable() {
		server.stop();
	}
	@SuppressWarnings("unchecked")
	void cfg(){
		getConfig().addDefault("deco", new ArrayList<HashMap<String, Object>>());
		getConfig().options().copyDefaults(true);
		for(Object o : getConfig().getList("deco")){
			if(o instanceof Map<?, ?>){
				Location loc = Location.deserialize((Map<String, Object>) o);
				
				Bukkit.getScheduler().runTaskTimerAsynchronously(this, ()->ParticleAPI.getParticle("VILLAGER_HAPPY").play(loc, .2F, .5F,.2F, 0, 1), 2, 2);
			}
		}
	}
	
	private void loadPath(Block start,ArrayList<Location> use, ArrayList<Block> blocks, Material flood, Material pattern){
		for(BlockFace f : Arrays.asList(BlockFace.UP,BlockFace.DOWN,BlockFace.EAST,BlockFace.WEST,BlockFace.NORTH,BlockFace.SOUTH)){
			Block newe = start.getRelative(f);
			if(newe.getType() == pattern && !blocks.contains(newe)){
				use.add(newe.getLocation().add(0, 3, 0));
				blocks.add(newe);
				loadPath(newe, use, blocks, flood, pattern);
			}else if(flood == newe.getType() && !blocks.contains(newe)){
				blocks.add(newe);
				loadPath(newe, use, blocks, flood, pattern);
			}
		}

	}
	
	private void playParticle() {
		int amount = 20;
		double increment = (2 * Math.PI) / amount;
        
        for(int i = 0;i < amount; i++)
        {
            double angle = i * increment;
            double x = (1 * Math.cos(angle));
            double z = (1 * Math.sin(angle));
            
            ParticleAPI.getParticle("FLAME").play(startPoint.clone().add(x, 0, z),0,0,0,0,1);
            ParticleAPI.getParticle("FLAME").play(startPoint.clone().add(0, x, z),0,0,0,0,1);
            ParticleAPI.getParticle("FLAME").play(startPoint.clone().add(x, z, 0),0,0,0,0,1);
            
        }
        ParticleAPI.getParticle("SPELL").play(startPoint,0,0,0,0,1);
		
		
	}
	
	@EventHandler
	public void onRedstone(BlockRedstoneEvent e){
		if(e.getBlock().getRelative(BlockFace.UP).getType() == Material.SKULL){
			if(((Skull)e.getBlock().getRelative(BlockFace.UP).getState()).getOwner().equalsIgnoreCase("MHF_Chest")){
				cookieThief.send();
			}
		}
	}
	
	private boolean running = false;

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if(e.getMessage().equals("@adddeco")){
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> l = (List<Map<String, Object>>) getConfig().getList("deco");
			l.add(e.getPlayer().getLocation().serialize());
			getConfig().set("deco", l);
			saveConfig();
			
			return;
		}
		if(e.getMessage().equals(".@number")){
			e.setMessage("Ich bin "+number+" jahre alt :L");
			return;
		}
		if(e.getMessage().equals("Finish")){
			
			e.getPlayer().teleport(finish);
		Bukkit.getScheduler().runTask(this, new Runnable() {
			public void run() {
				rsp = (e.getPlayer().getLocation().clone().add(0, 5, 0));
				e.getPlayer().setGameMode(GameMode.ADVENTURE);
				for(NPC king : CitizensAPI.getNPCRegistry()){
					if(!(king.getEntity() instanceof Player)) continue;
					World w = king.getStoredLocation().getWorld();
					king.getTrait(LookClose.class).lookClose(true);
					Bukkit.getPluginManager().callEvent(new PlayerInteractEvent((Player) king.getEntity(), Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.BONE), king.getStoredLocation().getBlock(), BlockFace.UP));
					
					ArmorStand a = (ArmorStand) 
					w.spawnEntity(king.getStoredLocation().clone().add(0, .1, 0), EntityType.ARMOR_STAND);
					a.setGravity(false);
					a.setCustomName(getbyName(king.getName()));
					a.setSmall(false);
					a.setCustomNameVisible(true);
					a.setVisible(false);
					
				
			}
			}

			private String getbyName(String name) {
			switch (name) {
			case "WOLFI3654":
				return "§6[King]";
			case "Hannigro":
				return "§c[Stadthälter]";
			case "Zangenbert":
				return "§a[TestOpfer]";
			case "Dompra":
				return "§3[Schlossbauer]";
			case "Reved":
				return "§5[Wolkenfluffer]";
			case "YouDid":
				return "§e[Schriftgelehrte]";
			default:
				return "§e[§1H§2o§3f§4n§5a§6r§7r§9]";
				
			}
				
			}
		});	
//			e.getPlayer().teleport(e.getPlayer().getLocation().add(0,5,0));
		e.setCancelled(true);
		return;
		}else if(e.getMessage().equals("Start")){
			boolean flag = running;
			running = true;
			e.getPlayer().teleport(start);
			Bukkit.getScheduler().runTask(Main.this, ()->e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*60*3, 0)));
			
			Bukkit.getScheduler().runTaskAsynchronously(Main.this, new Runnable() {
				private Vector getVector(Entity e, Location to){
					Location loc2 = e.getLocation();//Get the location from the target player
					 
					double deltaX = loc2.getX() - to.getBlockX();//Get X Delta
					double deltaZ = loc2.getZ() - to.getBlockZ();//Get Z delta
					boolean higher = to.getBlockY() > loc2.getY();
					 
					Vector vec = new Vector(deltaX/5+Math.random(), 0, deltaZ/5+Math.random());//Create new vector
					vec.multiply(5 / (Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0))+ Math.random()));//Use a bit of trig to get 'h' then divide the max power by 'h' to get a fall off effect
					vec.setY(higher ? -1: 1);
					return vec.multiply(-1).normalize();
					
				}
				boolean t = false;
				boolean c= false;
				
				public void run() {
					Bukkit.getScheduler().runTask(Main.this, ()->{e.getPlayer().setGameMode(GameMode.ADVENTURE);e.getPlayer().setAllowFlight(true);rsp = (e.getPlayer().getLocation());});
					for(Dialoge d : dialogs.get(-1)){
						Bukkit.getScheduler().runTask(Main.this, ()->e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.FIZZ, .4F, .2F));
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!flag)d.send();
					}
					while(e.getPlayer().isOnline()){
						
						Bukkit.getScheduler().runTask(Main.this, ()->e.getPlayer().playEffect(hole, Effect.FLAME, null));
						
						try {
							Thread.sleep(400);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						if(e.getPlayer().getEyeLocation().getBlock().getType() == Material.PORTAL && !t){
							t = true;
							Bukkit.getScheduler().runTaskLater(Main.this, ()->{e.getPlayer().sendMessage("Lets Challenge!");c = true;e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*60*6, 50));},20*50);
							
						}
						Vector v = getVector(e.getPlayer(), hole);
						Bukkit.getScheduler().runTask(Main.this, ()->e.getPlayer().setVelocity(v));
						if(c){ running = false; return;}
					}
				}
			});
			e.setCancelled(true);
			return;
		}
		if(e.getMessage().startsWith("q:")){
			String[] msg = e.getMessage().split(":");
			Bukkit.getPluginManager().callEvent(new AsyncPlayerDialogeChooseEvent(e.getPlayer(), msg[1], Integer.parseInt(msg[2])));
			e.setCancelled(true);
		}
	}
	
	
	
	@EventHandler
	public void onDia(AsyncPlayerDialogeChooseEvent e){
		Thread thread = new Thread(new Runnable() {
			public void run() {
				if(!dialoge) return;
				if(wrongAnswers.contains(e.getValue())){
					if(!e.getValue().equals("**"))Bukkit.broadcastMessage("§8"+e.getPlayer().getName()+"§5>> §6"+e.getValue());
					Bukkit.getScheduler().runTaskLater(Main.this, ()->{punish(e.getPlayer());dialoge = false;},20*1);
					
					return;
				}
				if(e.getOrder() == -1){} else if(e.getOrder() == last){
					if(!e.getValue().equals("**"))Bukkit.broadcastMessage("§8"+e.getPlayer().getName()+"§5>> §6"+e.getValue());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					e.getPlayer().sendMessage(new String[5]);
					List<Dialoge> digs = dialogs.get(e.getOrder());
					NPC c = CitizensAPI.getNPCRegistry().getById(1);
					Location back = c.getStoredLocation();
					c.teleport(c.getStoredLocation(), TeleportCause.UNKNOWN);
					c.getNavigator().getDefaultParameters().range(50);
					c.getNavigator().setTarget(treasureOpen);
					for(Dialoge d : digs){
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						d.send();
						
					}

					while(c.getNavigator().isNavigating()){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					c.getNavigator().cancelNavigation();
					Bukkit.getScheduler().runTask(Main.this, ()->{c.setBukkitEntityType(EntityType.WOLF);e.getPlayer().playSound(c.getStoredLocation(), Sound.FIZZ, 1, 1);});
					ParticleAPI.getParticle("EXPLOSION_HUGE").play(c.getStoredLocation(), 0.1F, .1F, .1F, 0, 2);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					float old = c.getNavigator().getDefaultParameters().baseSpeed();
					c.getNavigator().getDefaultParameters().baseSpeed(old/2);
					c.getNavigator().setTarget(plate);
					while(c.getNavigator().isNavigating()){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					c.getNavigator().getDefaultParameters().baseSpeed(old);
					c.getNavigator().cancelNavigation();
					Bukkit.getScheduler().runTask(Main.this, ()->{c.setBukkitEntityType(EntityType.PLAYER);e.getPlayer().playSound(c.getStoredLocation(), Sound.FIZZ, 1, 1);});
					ParticleAPI.getParticle("EXPLOSION_HUGE").play(c.getStoredLocation(), 0.1F, .1F, .1F, 0, 2);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
					c.getNavigator().setTarget(treasureIn);
					while(c.getNavigator().isNavigating()){
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Bukkit.getScheduler().runTask(Main.this, ()->{((HumanEntity) c.getEntity()).setGameMode(GameMode.SURVIVAL);
					((Damageable) c.getEntity()).damage(0);
					});
					digs = dialogs.get(e.getOrder()+1);
					for(Dialoge d : digs){
						
						d.send();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					Bukkit.getScheduler().runTask(Main.this, ()->((Damageable) c.getEntity()).damage(100));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					dialoge = false;
					c.spawn(back);
					
				}else {
					List<Dialoge> digs = dialogs.get(e.getOrder());
					if(digs == null) return;
					if(!e.getValue().equals("**"))Bukkit.broadcastMessage("§8"+e.getPlayer().getName()+"§5>> §6"+e.getValue());
					for(Dialoge d : digs){
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						d.send();
						
					}
				}
			}
		},"DiaExecutor");
		thread.start();
		
	}
	
	public void punish(Player player){
		player.teleport(punish);
		player.sendMessage(new String[500]);
		wrongAnswer.send();
		Location loc = player.getLocation();
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Foltermeister");
				   /* 1305 */       npc.data().setPersistent("player-skin-name", "Herobrine");

				   /* 1312 */    
				  npc.spawn(loc);
				   npc.teleport(loc, TeleportCause.PLUGIN);
				   
				   npc.addTrait(DamageTrait.class);
				   ((LivingEntity)npc.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*20, 2));
//				   ((LivingEntity)npc.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*1, 100));

//				   npc.getTrait(Inventory.class).setContents(new ItemStack[]{new ItemStack(Material.RABBIT_STEW)});
				   Bukkit.getScheduler().runTaskLater(Main.this, ()->{npc.getOwningRegistry().deregister(npc);player.setHealth(player.getMaxHealth());}, 20*20);
			
			}
		}, 20*1+10);
		}
	
	@EventHandler
	public void Interact(PlayerInteractEntityEvent e){
		if(CitizensAPI.getNPCRegistry().isNPC(e.getRightClicked())){
			
			NPC p = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked());
			if(p.getName().equalsIgnoreCase("Reved")){
				p.getTrait(Gravity.class).gravitate(false);
				BukkitTask t = Bukkit.getScheduler().runTaskTimer(this,new Runnable() {
					public void run() {
						p.getDefaultSpeechController().speak(new SpeechContext("#ABGEHOBEN"));
						p.teleport(p.getStoredLocation().clone().add(0, 1, 0), TeleportCause.PLUGIN);
					}
				},5,2);
				Bukkit.getScheduler().runTaskLater(this, ()->{t.cancel();p.getTrait(Gravity.class).gravitate(false);}, 20*3);
			}else
			if(p.getName().equalsIgnoreCase("Hannigro")){
				lostSteve.send();
			}else
			if(p.getName().equalsIgnoreCase("Pup")){
				punish(e.getPlayer());
			}else
			if(p.getTrait(MobType.class).getType() == EntityType.PLAYER){
				p.getDefaultSpeechController().speak(new SpeechContext("Alle lieben banenen!"));
			}
			
			
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, NoSuchFieldException{
		if(!dialoge){
			if(e.getPlayer().getLocation().getWorld().getName().equals(startPoint.getWorld().getName()))
			if(e.getPlayer().getLocation().distance(startPoint) < 1){
				
				startDialoge(e.getPlayer());
			}
		}
		if(e.getPlayer().getLocation().getY() == e.getPlayer().getLocation().getBlockY() && e.getPlayer().getLocation().getBlock().getType() != Material.SNOW_BLOCK){
			if(e.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType()==Material.SNOW_BLOCK){
				
				
				e.getPlayer().setVelocity(new Vector(0,-1,0));
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10,10));
			}
		}
		
		if(e.getPlayer().getLocation().subtract(0,1,0).getBlock().getType() == Material.DIAMOND_BLOCK){
			Object handle = e.getPlayer().getClass().getMethod("getHandle").invoke(e.getPlayer());
			Object connection = handle.getClass().getField("playerConnection").get(handle);
			Object packet = Reflection.getNMSClass("PacketPlayOutGameStateChange").getConstructor(int.class,float.class).newInstance(4,0);
			connection.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, packet);
		
		}
	}

	private void startDialoge(Player player) {
		dialoge = true;
		player.chat("q:**:0");
		
	}

	@EventHandler
	public void onCake(PlayerItemConsumeEvent e){
		if(e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1){
			e.setCancelled(true);
			rsp = e.getPlayer().getEyeLocation();
			e.getPlayer().sendMessage("§6Spawnpoint gesetzt!");
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		
		if(e.getAction() == Action.PHYSICAL){
			if(e.getClickedBlock().getType() == Material.GOLD_PLATE){
				if(e.getClickedBlock().getRelative(BlockFace.DOWN).getType()==Material.SNOW_BLOCK){
					startLadder(e.getPlayer());
					e.setUseInteractedBlock(Result.DENY);
				}
			}else if(e.getClickedBlock().getType()==Material.IRON_PLATE){
				if(e.getClickedBlock().getRelative(BlockFace.DOWN).getType()==Material.ENDER_STONE){
					FallingBlock b = (FallingBlock) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation(), EntityType.FALLING_BLOCK);
					b.setPassenger(e.getPlayer());
					b.setDropItem(false);
					
					Location old = e.getClickedBlock().getLocation().clone();
					b.setMetadata("OldLocation", new FixedMetadataValue(this, old));
					b.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.2).setY(1.2));
					new BukkitRunnable() {
						
						@Override
						public void run() {
							b.getWorld().playEffect(b.getLocation(), Effect.WITCH_MAGIC, null);
							if(!b.isValid()){
								if(old.getBlock().getType() != Material.IRON_PLATE){
									old.getBlock().setType(Material.IRON_PLATE,false);
									cancel();
									
								}
							}
							
						}
					}.runTaskTimer(this, 10, 10);
				}
			}else if(e.getClickedBlock().getType()== Material.WOOD_PLATE){

				e.setUseInteractedBlock(Result.DENY);
				
				Block newe = e.getClickedBlock();
				e.setUseInteractedBlock(Result.DENY);
				FallingBlock b = (FallingBlock) newe.getWorld().spawnEntity(newe.getLocation(), EntityType.FALLING_BLOCK);
				b.setDropItem(false);
				b.setMetadata("NoDropp", new FixedMetadataValue(this, ""));
				Location old = newe.getLocation().clone();
				
				
				new BukkitRunnable() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						
						if(!b.isValid()){
							if(old.getBlock().getType() != b.getMaterial()){
								old.getBlock().setType(b.getMaterial(),false);
								old.getBlock().setData(b.getBlockData(), false);;
								cancel();
								
							}
						}
						
					}
				}.runTaskTimer(this, 20*10, 20*10);
				attackNearbyBlocks(e.getClickedBlock(), new ArrayList<>(Arrays.asList(e.getClickedBlock())),new ArrayList<>(Arrays.asList(Material.WOOD_PLATE,Material.COAL_BLOCK)), new ArrayList<>(Arrays.asList(Material.LOG)));
			}
		}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			
			if(e.getClickedBlock().getType() == Material.CAKE_BLOCK && !e.getPlayer().isSneaking() && e.getPlayer().getFoodLevel() < 20 && e.getPlayer().getGameMode() != GameMode.CREATIVE){
				cakeThief.send();
				Bukkit.getScheduler().runTaskLater(this, ()->e.getPlayer().damage(6000), 60);
				e.setCancelled(true);
			}else if(e.getClickedBlock().getType()== Material.STONE_BUTTON){
				Block newe = e.getClickedBlock();
				e.setUseInteractedBlock(Result.DENY);
				FallingBlock b = (FallingBlock) newe.getWorld().spawnEntity(newe.getLocation(), EntityType.FALLING_BLOCK);
				b.setDropItem(false);
				b.setMetadata("NoDropp", new FixedMetadataValue(this, ""));
				Location old = newe.getLocation().clone();
				
				
				new BukkitRunnable() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						
						if(!b.isValid()){
							if(old.getBlock().getType() != b.getMaterial()){
								old.getBlock().setType(b.getMaterial(),false);
								old.getBlock().setData(b.getBlockData(), false);;
								cancel();
								
							}
						}
						
					}
				}.runTaskTimer(this, 20*10, 20*10);
				
				attackNearbyBlocks(e.getClickedBlock(), new ArrayList<>(Arrays.asList(e.getClickedBlock())),new ArrayList<>(Arrays.asList(Material.STONE_BUTTON,Material.COAL_BLOCK)), new ArrayList<>(Arrays.asList(Material.LOG)));
			}else if(e.getClickedBlock().getType() == Material.LEVER){
				
				Bukkit.getScheduler().runTaskAsynchronously(this, ()->lightNearbyBlocks(e.getClickedBlock(),new ArrayList<>(), Material.TRIPWIRE,e.getClickedBlock().getBlockPower() != 0));
			}else if(e.getClickedBlock().getType() == Material.WOOD_BUTTON){
				Thread t = new Thread(new Runnable() {
					public void run() {
						
						ParticleAPI.Particle p = ParticleAPI.getParticle("HEART");
						ArrayList<Location> loc = new ArrayList<Location>();
						loadPath(e.getClickedBlock().getLocation().subtract(0, 3, 0).getBlock(), loc, new ArrayList<>(), Material.GOLD_BLOCK, Material.EMERALD_BLOCK);
						for(Location l : loc){
							p.play(l, .1F, .2F, .1F, 0, 3);
							
							try {
								Thread.sleep(750);
							} catch (InterruptedException e1) {
								
								e1.printStackTrace();
							}
						}
						p = ParticleAPI.getParticle("FLAME");
						for(Location l : loc){
							p.play(l, 0, 0, 0, 0, 1);
							
						}
						
					}
				});
				t.start();
			}
		}
	}
	
	private void attackNearbyBlocks(Block start, ArrayList<Block> used, ArrayList<Material> flood, ArrayList<Material> parent){
		for(BlockFace f : Arrays.asList(BlockFace.UP,BlockFace.DOWN,BlockFace.EAST,BlockFace.WEST,BlockFace.NORTH,BlockFace.SOUTH)){
			Block newe = start.getRelative(f);
			if(flood.contains(newe.getType()) && !used.contains(newe)){
				FallingBlock b = (FallingBlock) newe.getWorld().spawnEntity(newe.getLocation(), EntityType.FALLING_BLOCK);
				b.setDropItem(false);
				b.setMetadata("NoDropp", new FixedMetadataValue(this, ""));
				Location old = newe.getLocation().clone();
				
				used.add(newe);
				new BukkitRunnable() {
					
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						
						if(!b.isValid()){
							if(old.getBlock().getType() != b.getMaterial()){
								old.getBlock().setType(b.getMaterial(),false);
								old.getBlock().setData(b.getBlockData(), false);;
								cancel();
								
							}
						}
						
					}
				}.runTaskTimer(this, 20*5, 20*5);
				attackNearbyBlocks(newe, used,flood,parent);
			}else if(parent.contains(newe.getType()) && !used.contains(newe)){
				used.add(newe);
				attackNearbyBlocks(newe, used, flood, parent);
			}
		}
	}
	
	private void lightNearbyBlocks(Block start, ArrayList<Block> used, Material flood, boolean on){
		for(BlockFace f : Arrays.asList(BlockFace.UP,BlockFace.DOWN,BlockFace.EAST,BlockFace.WEST,BlockFace.NORTH,BlockFace.SOUTH)){
			Block newe = start.getRelative(f);
			if((newe.getType() == Material.REDSTONE_LAMP_OFF || newe.getType() == Material.GLOWSTONE) && !used.contains(newe)){
				Bukkit.getScheduler().runTask(this, ()->newe.setType(on?Material.GLOWSTONE:Material.REDSTONE_LAMP_OFF,false));
				
				used.add(newe);
				lightNearbyBlocks(newe, used,flood,on);
			}else if(flood == newe.getType() && !used.contains(newe)){
				used.add(newe);
				
				lightNearbyBlocks(newe, used, flood,on);
			}
		}
	}
	
	@EventHandler
	public void onRSpawn(PlayerRespawnEvent e){
		if(rsp != null) e.setRespawnLocation(rsp);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChange(EntityChangeBlockEvent e){
		
		if(e.getEntity().hasMetadata("NoDropp") && e.getTo() != Material.AIR){
			e.setCancelled(true);
			return;
		}
		if(e.getEntity().hasMetadata("OldLocation") && (e.getTo() != Material.AIR || (e.getTo() == Material.AIR && e.getBlock().getType() == Material.AIR))){
			e.setCancelled(true);
			FallingBlock s = (FallingBlock) e.getEntity();
			Location loc = (Location) e.getEntity().getMetadata("OldLocation").get(0).value();
			loc.getBlock().setType(s.getMaterial(),false);
			loc.getBlock().setData(s.getBlockData(), false);
		}
	}
	
	private void startLadder(Player player) {
		
		player.setVelocity(new Vector(0, 2, 0));
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(this,new Runnable() {
			public void run() {
				Block loc = player.getLocation().getBlock();
				Material m = loc.getType();
				loc.getWorld().playEffect(loc.getLocation(), Effect.CLOUD, null);
				loc.setType(Material.STAINED_GLASS,false);
				if(m != Material.STAINED_GLASS)Bukkit.getScheduler().runTaskLater(Main.this, ()->loc.setType(m,false), 20*3);
				player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				player.setFallDistance(0);
			}
		}, 1, 1);
		Bukkit.getScheduler().runTaskLater(this, ()->{task.cancel();player.getVelocity().setY(.2);}, 20*3);
		
	}


	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		for(PotionEffect t : e.getPlayer().getActivePotionEffects()){
			e.getPlayer().removePotionEffect(t.getType());
		}
	}


	@EventHandler
	public void onWin(EntitySpawnEvent e) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(e.getEntityType() == EntityType.SNOWMAN){
			Object packet = Reflection.getNMSClass("PacketPlayOutGameStateChange").getConstructor(int.class,float.class).newInstance(9,1);
			for(Player p : Bukkit.getOnlinePlayers()){
				
				try {
					Object handle = p.getClass().getMethod("getHandle").invoke(p);
					Object connection = handle.getClass().getField("playerConnection").get(handle);
					connection.getClass().getMethod("sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, packet);
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
