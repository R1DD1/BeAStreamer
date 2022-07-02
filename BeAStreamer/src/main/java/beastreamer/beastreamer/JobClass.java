package beastreamer.beastreamer;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class JobClass implements Listener {

    //PLUGIN
    BeAStreamer plugin;

    public JobClass(BeAStreamer instanse) {
        plugin = instanse;
    }

    private final RoomClass RoomClassObj = new RoomClass(plugin);

    //START WORLD//

    World jobWorld = Bukkit.getWorld("jobWorld");


    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("Вы попали в мир РАБОТЫ РАБОТАЙ ЧОРТ");
    }

    //VARIABLES

    ItemStack Work[] = Work();
    boolean RESPAWN_WEB = true;

    //METHODS
    public ItemStack[] Work() {
        ItemStack Work[] = new ItemStack[9];
        Work[0] = RoomClassObj.createItem(Material.CLAY_BALL, "Домработник", ChatColor.GRAY);
        Work[1] = RoomClassObj.createItem(Material.FISHING_ROD, "Рыбак", ChatColor.DARK_GRAY);
        Work[2] = RoomClassObj.createItem(Material.BREAD, "Повар", ChatColor.AQUA);
        Work[3] = RoomClassObj.createItem(Material.BOOK, "Бухалтер", ChatColor.BLUE);
        Work[4] = RoomClassObj.createItem(Material.BLAZE_ROD, "Дизайнер", ChatColor.LIGHT_PURPLE);
        Work[5] = RoomClassObj.createItem(Material.IRON_BLOCK, "Стажер-Программист", ChatColor.DARK_PURPLE);
        Work[6] = RoomClassObj.createItem(Material.GOLD_BLOCK, "Junior-Программист", ChatColor.GREEN);
        Work[7] = RoomClassObj.createItem(Material.DIAMOND_BLOCK, "Middle-Программист", ChatColor.YELLOW);
        Work[8] = RoomClassObj.createItem(Material.EMERALD_BLOCK, "Senior-Программист", ChatColor.RED);
        return Work;
    }

    public Location[] PlaceWork(Player p) {
        Location PLocation[] = new Location[9];
        PLocation[0] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[1] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[2] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[3] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[4] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[5] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[6] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[7] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[8] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        return PLocation;
    }

    //LOGIC//

    @EventHandler
    public void GoToWork(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.WORKBENCH)) {

                Inventory placesInv = Bukkit.createInventory(null, 9, "Работа");
                e.setCancelled(true);

                for (int i = 0; i < 9; i++) {
                    RoomClassObj.addItemIntoInv(placesInv, i, Work[i]);
                }
                p.openInventory(placesInv);


            }

        }
    }

    @EventHandler
    public void ChooseWork(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        Location location[] = PlaceWork(p);
        for (int i = 0; i < 9; i++) {
            if (e.getCurrentItem().getType().equals(Work[i].getType())) {
                e.setCancelled(true);
                p.teleport(location[i]);
                break;
            }
        }
    }

    @EventHandler
    public void Housekeeper(BlockBreakEvent block) {
        if (block.getBlock().getType() == Material.WEB && RESPAWN_WEB) {
            new BukkitRunnable(){
                @Override
                public void run(){
                   block.getBlock().setType(Material.WEB);
                }
            }.runTaskLater(plugin, 140);
        }

    }
}