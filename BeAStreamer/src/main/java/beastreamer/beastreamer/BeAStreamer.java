package beastreamer.beastreamer;


import beastreamer.beastreamer.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;



public final class BeAStreamer extends JavaPlugin {
    private static BeAStreamer instance;


    private Connection connection;
    public String host, database, username, password, table;
    public int port;


    @Override
    public void onEnable() {

        mySQLSetup();

        instance = this;




        // WORLDS

        WorldCreator connectWorld = new WorldCreator("connectWorld");
        connectWorld.type(WorldType.FLAT);
        connectWorld.generatorSettings("2;0;1;");
        connectWorld.createWorld();

        WorldCreator arenaWorld = new WorldCreator("arenaWorld");
        arenaWorld.type(WorldType.FLAT);
        arenaWorld.generatorSettings("2;0;1;");
        arenaWorld.createWorld();

        WorldCreator jobWorld = new WorldCreator("jobWorld");
        jobWorld.type(WorldType.FLAT);
        jobWorld.generatorSettings("2;0;1;");
        jobWorld.createWorld();



        Logger log = Bukkit.getLogger();
        log.info("ENABLE");

        getServer().getPluginManager().registerEvents(new RoomClass(this),this);
        getServer().getPluginManager().registerEvents(new JobClass(this),this);
        getServer().getPluginManager().registerEvents(new ArenaGame(this),this);
        getServer().getPluginManager().registerEvents(new Database(), this);
    }



    public static BeAStreamer getInstance(){
        return instance;

    }


//    MySQL

    public void mySQLSetup(){
        host = "localhost";
        port = 3306;
        database = "test1";
        password="";
        username = "root";
        table = "player_statistic";

        try{
            synchronized (this){
                if (getConnection()!=null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://"
                        + this.host + ":" + this.port + "/" + this.database, this.username, this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"DATABASE WORKING");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }




    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }





}
