package beastreamer.beastreamer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ArenaGame implements Listener {
    private final BeAStreamer plugin;

    public ArenaGame(BeAStreamer plugin) {
        this.plugin = plugin;
    }


    //    ПОЛУЧЕНИЕ ВОРЛДОВ (для нг)
    World arenaWorld = Bukkit.getWorld("arenaWorld");


    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        World fromWorld = e.getFrom();
        if(fromWorld.equals(arenaWorld)){
            p.sendMessage("Вы попали на арену, через мир " + fromWorld);
        }
    }
}
