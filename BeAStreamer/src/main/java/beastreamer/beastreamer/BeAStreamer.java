package beastreamer.beastreamer;


import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;



public final class BeAStreamer extends JavaPlugin {
    private static BeAStreamer instance;


    @Override
    public void onEnable() {

        instance = this;




        // WORLDS

        WorldCreator connectWorld = new WorldCreator("connectWorld");
        connectWorld.type(WorldType.FLAT);
        connectWorld.generatorSettings("2;0;1;");
        connectWorld.createWorld();

//        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
//        protocolManager.addPacketListener();


//        WorldCreator roomWorld = new WorldCreator("roomWorld");
//        connectWorld.type(WorldType.FLAT);
//        connectWorld.generatorSettings("2;0;1;");
//        connectWorld.createWorld();

        Logger log = Bukkit.getLogger();
        log.info("ENABLE");

//        getServer().getPluginManager().registerEvents(new PlayerConnectClass(), this);
        getServer().getPluginManager().registerEvents(new RoomClass(this),this);
        getServer().getPluginManager().registerEvents(new JobClass(this),this);
        getServer().getPluginManager().registerEvents(new ArenaGame(this),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BeAStreamer getInstance(){
        return instance;

    }


}
