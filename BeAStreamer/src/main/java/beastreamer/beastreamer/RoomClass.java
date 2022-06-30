package beastreamer.beastreamer;


import com.connorlinfoot.actionbarapi.ActionBarAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
//import net.md_5.bungee.api.chat.TextComponent;


import java.util.ArrayList;

public class RoomClass implements Listener {

    private final JavaPlugin plugin;



    public RoomClass(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    World roomWorld = Bukkit.getWorld("connectWorld");


    public ItemStack createItem(Material material, String displayName, ChatColor color) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(color + displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void addItemIntoInv(Inventory inv, int slot, ItemStack item){
        inv.setItem(slot, item);
    }


//    public Inventory invCreator(Inventory inv, int slots, String GUIname, int slot, ItemStack item){
//
//        inv = Bukkit.createInventory(null, slots, GUIname);
//        return inv;
//    }

    private int randomInt(int arg0){
        int i = (int) (Math.random()*36);
        return i;
    }

    public void statisticBoard(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard sb = manager.getNewScoreboard();
        Objective obj = sb.registerNewObjective("Статистика", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        String uuid = p.getUniqueId().toString();
        Score money = obj.getScore(ChatColor.BOLD +"Баланс:");
        money.setScore((Integer) plugin.getConfig().get("streamers.users."+uuid+".money"));

        Score followers = obj.getScore(ChatColor.BOLD +"Фоловеров:");
        followers.setScore((Integer) plugin.getConfig().get("streamers.users."+uuid+".followers"));

        Score maxEnergy = obj.getScore(ChatColor.BOLD +"Энергия:");
        maxEnergy.setScore((Integer) plugin.getConfig().get("streamers.users."+uuid+".energy.cur"));

        p.setScoreboard(sb);
    }

    private int curWeb = 0;

    Inventory warehouse = Bukkit.createInventory(null, 9, "Склад");



    Inventory postInv = Bukkit.createInventory(null, 36, "Почта");
    ItemStack eMail = createItem(Material.COOKIE, "Письмо", ChatColor.WHITE);





    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.teleport(roomWorld.getSpawnLocation());
        String uuid = p.getUniqueId().toString();
        TextComponent textComponent = new TextComponent("Hello");
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept"));
        p.spigot().sendMessage(textComponent);



        if (plugin.getConfig().contains("streamers.users."+uuid)){
            p.sendMessage("опять");
        }else {
            plugin.getConfig().set("streamers.users."+uuid,"");
            plugin.getConfig().set("streamers.users."+uuid+".boosters.money", 1);
            plugin.getConfig().set("streamers.users."+uuid+".boosters.follower", 1);
            plugin.getConfig().set("streamers.users."+uuid+".followers", 0);
            plugin.getConfig().set("streamers.users."+uuid+".money", 500);
            plugin.getConfig().set("streamers.users."+uuid+".energy", "");
            plugin.getConfig().set("streamers.users."+uuid+".energy.max", 50 );
            plugin.getConfig().set("streamers.users."+uuid+".energy.cur", 50 );
            plugin.saveConfig();
            p.sendTitle(ChatColor.GREEN +"Приветствую ", "", 10 , 40, 10);
        }
//        Vehicle allVeh = e.getPlayer().getLocation().getChunk().getEntities();
        statisticBoard(p);

    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e){
        if (e.getPlayer().isInsideVehicle()){
            Vehicle veh = (Vehicle) e.getPlayer().getVehicle();
            veh.eject();
        }
    }

    @EventHandler
    public void clickItemCancelled(InventoryClickEvent e){
        if (e.getCurrentItem().getType() != Material.AIR){
            if (e.getClickedInventory().getSize() == 36){
                e.setCancelled(true);

            }
        }

    }

    // STREAM ////////////////////////////////////////////

    @EventHandler
    public void startOfStream(VehicleEnterEvent e){
        Player p = (Player) e.getEntered();
        Inventory PCInv = Bukkit.createInventory(null, 36, "Компьютер");
        if (e.getVehicle() instanceof Minecart){
//            p.getLocation().setPitch((float) -1.54);
//            p.getLocation().setYaw((float) -93.34);
            addItemIntoInv(PCInv, 0, createItem(Material.EMERALD, "Начать стрим", ChatColor.GREEN ));
            addItemIntoInv(PCInv, 1, createItem(Material.BEACON, "Интернет магазин", ChatColor.GREEN ));
            addItemIntoInv(PCInv, 2, createItem(Material.PAPER, "Почта", ChatColor.GREEN ));
            p.openInventory(PCInv);


        }
    }

    ////// START STREAM ////////////////////////////////////////////////////////////////////////

    @EventHandler
    public void startOfStream(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().equals(createItem(Material.EMERALD, "Начать стрим", ChatColor.GREEN ))){
            if (plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".energy.cur")>=20){
                Inventory liveStream = Bukkit.createInventory(null, 36, "Прямой эфир");
                addItemIntoInv(liveStream, randomInt(36), createItem(Material.WEB, "Подключение", ChatColor.WHITE));
                p.openInventory(liveStream);
            }else{
                p.sendMessage("Я слишком устал, вздремнуть бы сейчас");
                p.closeInventory();
            }

        }
        int needWeb = 2;

        if (e.getCurrentItem().equals(createItem(Material.WEB, "Подключение", ChatColor.WHITE))){
            e.getCurrentItem().setType(Material.AIR);
            e.getClickedInventory().remove(e.getCurrentItem());
            addItemIntoInv(e.getClickedInventory(), randomInt(36), createItem(Material.WEB, "Подключение", ChatColor.WHITE));
            if (curWeb==needWeb){
                p.sendMessage("Вы выполнили");
                float curFollowers = plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".followers");

                if (curFollowers<1000){
                    int followersRand = randomInt(10);
                    int foll = (int) (curFollowers+followersRand);
                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас "+ChatColor.GREEN+followersRand+" новых фолловеров" + ChatColor.AQUA + " [" + plugin.getConfig().get("streamers.users."+p.getUniqueId()+".boosters.follower") +  "x]", 100);
                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".followers", foll);
                    statisticBoard(p);
                }
//                else if(curFollowers<500){
//                    int newFollowers = randomInt(50);
//                    int foll = curFollowers+newFollowers;
//                    p.sendMessage(String.valueOf(newFollowers));
//                    p.sendMessage(String.valueOf(curFollowers));
//                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас "+ChatColor.GREEN+newFollowers+" новых фолловеров", 100);
//                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".followers", foll);
//                    statisticBoard(p);
//                }else if(curFollowers<1000){
//                    int newFollowers = randomInt(100);
//                    int foll = curFollowers+newFollowers;
//                    p.sendMessage(String.valueOf(newFollowers));
//                    p.sendMessage(String.valueOf(curFollowers));
//                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас "+ChatColor.GREEN+newFollowers+" новых фолловеров", 100);
//                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".followers", foll);
//                    statisticBoard(p);
//
//                }


                curWeb = 0;
                p.closeInventory();
                int newEnergy = plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".energy.cur") - 20;
                plugin.getConfig().set("streamers.users."+p.getUniqueId()+".energy.cur", newEnergy);
                statisticBoard(p);
            }else{
                curWeb = curWeb+1;
                if(curWeb%10==0){
                    int a = needWeb -curWeb;
                    if (a==0){

                    }else {
                        p.sendMessage("Осталось: " + a);
                    }
                }
            }
        }
    }


    /////// NET MARKET ///////////////////////////////////////////////////////////
    @EventHandler
    public void netMarket(InventoryClickEvent e){
        ItemStack videoCard = createItem(Material.DAYLIGHT_DETECTOR, plugin.getConfig().getString("market.slots.videocard.name"),ChatColor.WHITE );

        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.BEACON)) {
            Inventory webMarketInv = Bukkit.createInventory(null, 36, "Интернет магазин");
            ItemMeta meta = videoCard.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            lore.add(String.valueOf(plugin.getConfig().getString("market.slots.videocard.price")));
            meta.setLore(lore);
            videoCard.setItemMeta(meta);
            webMarketInv.setItem(0, videoCard);
            p.openInventory(webMarketInv);
        }

        //////// VIDEOCARD ////////////////////////////////////////////////////////
        if (e.getCurrentItem().getType().equals(Material.DAYLIGHT_DETECTOR)){
            if (warehouse.contains(videoCard)){
                p.sendMessage(ChatColor.RED + "У вас уже куплен этот товар");
            }else {
                if (plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".money")>=plugin.getConfig().getInt("market.slots.videocard.price")){
                    ItemStack sold = createItem(Material.BARRIER, "КУПЛЕНО", ChatColor.RED);
                    int slot = e.getSlot();
                    e.getClickedInventory().setItem(slot, sold);
                    int newMoney = plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".money")-plugin.getConfig().getInt("market.slots.videocard.price");
                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".money", newMoney);
                    warehouse.addItem(videoCard);
                    statisticBoard(p);
                }else {
                    p.sendMessage("Пора на работу");
                }
            }

        }
    }
    Boolean notSended = true;
    Boolean partnerProg = false;

    @EventHandler
    public void post(InventoryClickEvent e){
        

        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.PAPER)){
            int curFollowers = plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".followers");
            if (curFollowers >100){
                if (notSended){
                    postInv.addItem(eMail);
                    notSended=false;
                }
            }
            p.openInventory(postInv);


        }
        if (e.getCurrentItem().getType().equals(Material.COOKIE)){
            partnerProg = true;
            p.sendMessage("");
            p.sendMessage("");
            p.sendMessage("Мы предлогаем вам воспользоваться нашей партнерской программой");
            p.sendMessage("");
            p.sendMessage("Чтобы принять предложение напишите " + ChatColor.GREEN + "ПРИНЯТЬ");
            p.sendMessage("Чтобы отклонить предложение напишите " + ChatColor.RED + "ПРИНЯТЬ");
            p.sendMessage("");
        }
    }

    @EventHandler
    public void acceptChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String msg = e.getMessage();
        float boostFoll = plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".boosters.follower");
        if (msg.equalsIgnoreCase("принять")){
            p.sendMessage("");
            p.sendMessage("");
            p.sendMessage("Вы приняли предложение");
            p.sendMessage("");
            p.sendMessage("Для вас активирован бустер фолловеров " + ChatColor.AQUA + "[x1.1]");

            float newBoostFoll = (float) (boostFoll + 0.1);
            plugin.getConfig().set("streamers.users."+p.getUniqueId()+".boosters.follower", newBoostFoll);

            p.sendMessage("Нынешний бустер: " + plugin.getConfig().get("streamers.users."+p.getUniqueId()+".boosters.follower"));
            p.sendMessage("");
            p.sendMessage("");

            partnerProg = false;
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void warehouse(PlayerInteractEvent e){
        Player p =e.getPlayer();
        if (e.getClickedBlock().getType() == (Material.CHEST)){
            e.setCancelled(true);
            p.openInventory(warehouse);


        }
    }



    @EventHandler
    public void playerEject(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
//        if (p.getInventory().equals(liveStream)){
//            if (p.isInsideVehicle()){
//                Vehicle veh = (Vehicle) e.getPlayer().getVehicle();
//                if (veh instanceof Minecart){
//                    veh.eject();
//                }
//            }
//        }
    }



    // SLEEP ////////////////////////////////////////////

    @EventHandler
    public void playerSleep(PlayerBedEnterEvent e){
        Player p = e.getPlayer();
        plugin.getConfig().set("streamers.users."+p.getUniqueId()+".energy.cur", plugin.getConfig().getInt("streamers.users."+p.getUniqueId()+".energy.max"));
        statisticBoard(p);


    }
    @EventHandler
    public void playerWakeUp(PlayerBedLeaveEvent e){



    }

    // GO OUT ////////////////////////////////////////////
    @EventHandler
    public void leaveOut(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Inventory placesInv = Bukkit.createInventory(null, 36, "Выбор места");
        Material Door = Material.WOODEN_DOOR;


        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(e.getClickedBlock().getType().equals(Door)){
                e.setCancelled(true);
                p.openInventory(placesInv);
            }
        }

    }


//    @EventHandler
//    public void setNight(PlayerInteractEvent e){
//        e.getPlayer().sendMessage(String.valueOf(e.getClickedBlock().getType()));
//
//        if (e.getPlayer().getWorld().getTime()<=13000){
//            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
//                e.getPlayer().getWorld().setTime(13000);
//            }
//        }
//    }
}
