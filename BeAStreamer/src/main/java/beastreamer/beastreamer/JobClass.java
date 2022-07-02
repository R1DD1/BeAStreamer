package beastreamer.beastreamer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class JobClass implements Listener {

    private final BeAStreamer plugin;

    public JobClass(BeAStreamer plugin) {
        this.plugin = plugin;
    }


    World jobWorld = Bukkit.getWorld("jobWorld");


    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        p.sendMessage("Вы попали в мир РАБОТЫ РАБОТАЙ ЧОРТ");
    }

}
